package common

import javax.inject.Inject

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

class Authentication @Inject()(userService: UserService, oauth: OauthService) {

  object UserAction extends
    ActionBuilder[UserRequest] {

    def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {

      val xAuthToken = request.headers.get("X-AUTH-TOKEN").getOrElse(throw new Exception("You are not logged in."))
      //.orElse(request.cookies.get("X-AUTH-TOKEN").map{_.value})

      oauth.verifyToken(xAuthToken) match {
        //Have a session cookie
        case Some(gUser) =>
          userService.getUserByEmail(gUser.email).flatMap {
            case Some(user: User) =>
              block(new UserRequest(xAuthToken, user, request))
            case _ =>
              Future.successful(Results.Unauthorized(Json.obj("message" -> s"No user was found for the token!")))
          }
        //No token or session cookie; APIs protected by this ActionBuilder
        case _ =>
          Future.successful(Results.Unauthorized(Json.obj("message" -> "User token is invalid!")))
      }
    }
  }

}

