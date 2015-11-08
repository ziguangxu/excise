package models

import log.Loggable
import org.joda.time.DateTime
import dao.MessageDao

/**
 * @author Ziguang Xu
 */
case class Message(id: Option[Long],
  username: String,
  content: String,
  expirationDate: DateTime) extends Loggable {
  val logString = "id: <" + id + "> content: <" + content + "> to user " + username
}

object Message {
  def create(message: Message): Message = {
    MessageDao.create(message)
  }

  def apply(id: Long): Option[Message] = {
    MessageDao.apply(id)
  }

  def findForUsername(username: String): List[Message] = {
    val time = new DateTime()
    MessageDao.findForUser(username, time)
  }

  def setExpired(ids: List[Long]) = {
    MessageDao.setExpired(ids, new DateTime())
  }
}

case class MessageForUsername(id: Long, content: String)
