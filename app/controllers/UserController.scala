package controllers

import io.kanaka.monadic.dsl._
import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.UserService

import scala.concurrent._
import ExecutionContext.Implicits.global

/**
  * User Controller
  */
@Singleton
class UserController @Inject()(userService: UserService) extends Controller {

  def getUserById(userId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    for {
      m <- userService.getUserById(userId) ?| NotFound(Json.obj("message" -> "User not found"))
    } yield Ok(Json.obj("user" -> Json.toJson(m)))
  }

  def getAllUsers = Action.async(BodyParsers.parse.empty) { _ =>
    userService.getAll.map { users =>
      Ok(Json.obj("users" -> Json.toJson(users)))
    }
  }

}
