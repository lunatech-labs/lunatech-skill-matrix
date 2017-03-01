package services.oauth

import io.circe._
import io.circe.generic.semiauto._

case class GoogleUser(userId: String, email: String, name: String, familyName: String, givenName: String)

object GoogleUser {
  implicit val GoogleUserEncoder: Encoder[GoogleUser] = deriveEncoder
}

trait OauthService {
  def verifyToken(idTokenString: String): Option[GoogleUser]

}
