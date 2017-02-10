package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.UserDAOService

import scala.concurrent._
import ExecutionContext.Implicits.global
/**
  * Created by tatianamoldovan on 07/02/2017.
  */
class UserController @Inject() (userDAOService: UserDAOService) extends Controller {

  def getUserById(userId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    userDAOService.getUserById(userId).map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m))))
  }

}
