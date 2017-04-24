package controllers

import javax.inject.{Inject, Singleton}

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
class GoogleSignInController @Inject()(
                                        userService: UserService,
                                        oauth: OauthService) extends Controller {

  def authenticate(): Action[JsValue] = Action.async(BodyParsers.parse.json) { implicit request =>
    for {
      token <- request.body.\("token").validate[String]                                                      ?| (err => ApiErrors.badRequest(JsError.toJson(err)))
      gUser <- oauth.verifyToken(token)                                                                      ?| ApiErrors.UNAUTHORIZED
      user <- userService.getOrCreateUserByEmail(User(None, gUser.givenName, gUser.familyName, gUser.email)) ?| ApiErrors.USER_NOT_FOUND
    } yield Ok(Json.obj("user" -> Json.toJson(user)))
  }
}
