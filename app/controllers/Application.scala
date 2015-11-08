package controllers

import format._
import anorm._
import play.api.libs.json.Json
import play.api.mvc._
import log.Loggable
import models._
import format.MessageFormat._

class Application extends Controller with Loggable{

  def index = Action {
    Redirect(routes.Application.createMessage)
  }

  def createMessage = Action(parse.json) {request =>
    log.debug("create message request body: " + request.body.toString)
    val message = request.body.as[Message]
    val newMessage = Message.create(message)
    Ok(CreateResponseFormat("201", "Message Created", newMessage.id.get).json)
  }

  def getMessageById(id: Long) = Action { request =>
    log.debug("retrieve messages by id: " + id)
    val message = Message(id)
    message match {
      case Some(m) => Ok(IdResponseFormat(m).json)
      case None => Ok(Json.obj("status" -> "failed", "error" -> "didn't find the message of the specific id"))
    }
  }

  def getMessagesByUsername(username: String) = Action {request =>
    log.debug("retrieve messages by username: " + username)
    val messages = Message.findForUsername(username).map(m => MessageForUsername(m.id.get, m.content))
    messages.size match {
      case 0 => Ok(Json.obj("status" -> "failed", "error" -> "didn't find the message of the specific username"))
      case _ => {
        Message.setExpired(messages.map(_.id))
        Ok(UsernameResponseFormat(messages).json)
      }
    }
  }

}
