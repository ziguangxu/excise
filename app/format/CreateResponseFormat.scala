package format

import java.time.format.DateTimeFormatter

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.Json
import models._

/**
 * @author Ziguang Xu
 */

case class CreateResponseFormat(status: String, message: String, id: Long) {
  def json = Json.obj("status" -> status, "message" -> message, "id" -> id)
}

case class IdResponseFormat(message: Message) {
  val fmt = DateTimeFormat.forPattern("yyyy-mm-dd HH:mm:ss")
  val time: String = fmt.print(message.expirationDate)
  def json = Json.obj("username" -> message.username, "content" -> message.content, "expiration_date" -> time)
}

case class UsernameResponseFormat(messages: List[MessageForUsername]) {
  implicit val usernameMessageFormat = Json.format[MessageForUsername]
  def json = Json.obj("messages" -> messages)
}
