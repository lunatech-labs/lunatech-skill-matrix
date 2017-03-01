package controllers

import io.kanaka.monadic.dsl._
import javax.inject.{Inject, Singleton}
import common.Authentication
import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.UserService
import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class UserController @Inject()(userService: UserService, auth: Authentication) extends Controller {

  def getUserById(userId: Int): Action[Unit] = auth.UserAction.async(BodyParsers.parse.empty) { _ =>
    for {
      user <- userService.getUserById(userId) ?| NotFound(Json.obj("message" -> "User not found"))
    } yield Ok(Json.obj("user" -> Json.toJson(user)))
  }

  def getAllUsers: Action[Unit] = auth.UserAction.async(BodyParsers.parse.empty) { _ =>
    userService.getAll.map { users =>
      Ok(Json.obj("users" -> Json.toJson(users)))
    }
  }
}
