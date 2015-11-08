package dao

import anorm.SqlParser._
import anorm.SqlParser.get
import anorm._
import play.api.db.DB
import play.api.Play.current
import models.Message
import org.joda.time.DateTime
import log.Loggable
import format.JodaFormat.rowToDateTime
import format.JodaFormat.dateTimeToStatement

/**
 * @author Ziguang Xu
 */

object MessageDao extends Loggable{
  val messageParser = {
    get[Option[Long]]("messages.id") ~
      get[String]("messages.username") ~
      get[String]("messages.content") ~
      get[DateTime]("messages.expiration_date") map {
      case id ~ username ~ content ~ expirationDate =>
        Message(id, username, content, expirationDate)
    }
  }

  def create(message: Message): Message = {
    val newMessage = message.copy(id = Some(DB.withConnection {
      implicit connection =>
        SQL(
          """
            INSERT INTO messages (username, content, expiration_date)
            VALUES ({username}, {content}, {expirationDate});
          """).on(
            "username" -> message.username,
            "content" -> message.content,
            "expirationDate" -> message.expirationDate).executeInsert(scalar[Long].single)
    }))
    log.debug("Created message in DB: " + newMessage.logString)
    newMessage
  }

  def apply(id: Long): Option[Message] = {
    val sql = "SELECT * FROM messages WHERE id = {id}"
    DB.withConnection { implicit connection =>
      SQL(sql).on("id" -> id).as(messageParser.singleOpt)
    }
  }

  def findForUser(username: String, time: DateTime): List[Message] = {
    val sql = "SELECT * FROM messages WHERE username = {username} AND expiration_date > {time}"
    DB.withConnection {
      implicit connection =>
        SQL(sql).on("username" -> username, "time" -> time).as(messageParser *)
    }
  }

  def setExpired(ids: List[Long], time: DateTime) = {
    val idString = "(" + ids.mkString(", ") + ")"
    val sql = "UPDATE messages SET expiration_date = {time} WHERE id IN " + idString
    DB.withConnection {
      implicit connection =>
        SQL(sql).on("time" -> time).executeUpdate()
    }
  }
}
