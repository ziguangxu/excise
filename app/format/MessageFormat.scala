package format

import models._
import org.joda.time.DateTime
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Reads
/**
 * @author Ziguang Xu
 */

object MessageFormat {
  implicit object MessageReads extends Reads[Message] {
    def reads(json: JsValue): JsSuccess[Message] = {
      val username = (json \ "username").asOpt[String].getOrElse("username")
      val content = (json \ "content").asOpt[String].getOrElse("content")
      val duration = (json \ "timeout").asOpt[Int].getOrElse(60)
      val expirationDate = new DateTime().plusSeconds(duration)
      JsSuccess(Message(None, username, content, expirationDate))
    }
  }
}
