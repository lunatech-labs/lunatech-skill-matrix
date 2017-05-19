package common

import javax.inject.Inject

import com.typesafe.scalalogging.LazyLogging
import models.User
import play.api.libs.json.Json
import play.api.mvc._
import services.UserService
import services.oauth.OauthService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class UserRequest[A](
                           token: String,
                           user: User,
                           request: Request[A]
                         ) extends WrappedRequest[A](request)

class Authentication @Inject()(userService: UserService, oauth: OauthService) extends LazyLogging {

  object UserAction extends
    ActionBuilder[UserRequest] {

    def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      logger.info("received request {} ", request.uri)

      val xAuthToken = request.headers.get("X-AUTH-TOKEN").getOrElse(throw new Exception("You are not logged in."))

      oauth.verifyToken(xAuthToken) match {
        //Have a session cookie
        case Some(gUser) =>
          userService.getUserByEmail(gUser.email).flatMap {
            case Some(user: User) =>
              logger.info("Logged in user {}", user)
              block(new UserRequest(xAuthToken, user, request))
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
  }

}

