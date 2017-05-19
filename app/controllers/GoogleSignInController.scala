package controllers

import javax.inject.{Inject, Singleton}

import com.typesafe.scalalogging.LazyLogging
import common.ApiErrors
import models.User
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{Action, BodyParsers, Controller}
import services.UserService
import services.oauth.OauthService
import io.kanaka.monadic.dsl._
import models.ImplicitFormats._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class GoogleSignInController @Inject()(userService: UserService, oauth: OauthService) extends Controller with LazyLogging {

  def authenticate(): Action[JsValue] = Action.async(BodyParsers.parse.json) { implicit request =>
    for {
      token <- request.body.\("token").validate[String]                                                      ?| (err => ApiErrors.badRequest(JsError.toJson(err)))
      gUser <- oauth.verifyToken(token)                                                                      ?| ApiErrors.UNAUTHORIZED
      id <- userService.getOrCreateUserByEmail(User(None, gUser.givenName, gUser.familyName, gUser.email))   ?| ApiErrors.USER_NOT_FOUND
      _ = logger.info("logged user with id {}",id)
    } yield Ok(Json.obj("user" -> Json.toJson(User(Some(id), gUser.givenName, gUser.familyName, gUser.email))))
  }
}
