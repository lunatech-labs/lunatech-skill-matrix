package common

import javax.inject.Inject

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import models.{AccessLevel, Status, User}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.UserService
import services.oauth.OauthService

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

case class UserRequest[A](
                           token: String,
                           user: User,
                           request: Request[A]
                         ) extends WrappedRequest[A](request)

class Authentication @Inject()(userService: UserService, oauth: OauthService, config:Config, cc: ControllerComponents) extends LazyLogging {

  object UserAction {
    def apply(): UserAction = new UserAction(AccessLevel.Basic)

    def apply(userLevel: AccessLevel): UserAction = new UserAction(userLevel)
  }

  class UserAction(accessLevel: AccessLevel) extends
    ActionBuilder[UserRequest, AnyContent] {

    def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      logger.info("received request {} ", request.uri)
      val apiToken = request.headers.get("X-ID-TOKEN")

      apiToken match {
        case Some(key) if key == config.getString("api.secret") => authenticateApi(request, block)
        case _ => authenticateUser(request, block)

      }
    }

    private def authenticateApi[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      block(new UserRequest("", User(None, "api", "call", "api@call.com", List(AccessLevel.Admin), Status.Active), request))
    }

    private def authenticateUser[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      val xAuthToken = request.headers.get("X-AUTH-TOKEN").getOrElse(throw new Exception("You are not logged in."))
      oauth.verifyToken(xAuthToken) match {
        //Have a session cookie
        case Some(gUser) =>
          userService.getUserByEmail(gUser.email).flatMap {
            case Some(user: User) =>
              if (AccessLevel.isAccessible(user.accessLevels, accessLevel)) block(new UserRequest(xAuthToken, user, request))
              else Future.successful(Results.Forbidden(Json.obj("message" -> s"User doesn't have the necessary access level.")))
            case _ =>
              logger.info("No user was found for the given token")
              Future.successful(Results.Unauthorized(Json.obj("message" -> s"No user was found for the token!")))
          }
        //No token or session cookie; APIs protected by this ActionBuilder
        case _ =>
          logger.info("User token is invalid")
          Future.successful(Results.Unauthorized(Json.obj("message" -> "User token is invalid!")))
      }
    }

    override def parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser

    override protected def executionContext: ExecutionContext = cc.executionContext

  }

}

