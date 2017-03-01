package services.oauth

import javax.inject.Singleton
import com.google.api.client.googleapis.auth.oauth2.{GoogleIdTokenVerifier, GooglePublicKeysManager}
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.google.inject.Inject
import com.typesafe.config.Config
import io.circe._
import io.circe.generic.semiauto._
import play.api.inject.ApplicationLifecycle
import scala.collection.JavaConverters._
import scala.util.Try

@Singleton
class GoogleOauthService @Inject()(lifecycle: ApplicationLifecycle, config: Config) {

  case class GoogleUser(userId: String, email: String, name: String, familyName: String, givenName: String)

  object GoogleUser {
    implicit val GoogleUserEncoder: Encoder[GoogleUser] = deriveEncoder
  }

  def verifyToken(idTokenString: String): Option[GoogleUser] = {

    val transport = new NetHttpTransport()
    val jsonFactory = new JacksonFactory()
    val googlePublicKeysManager = new GooglePublicKeysManager.Builder(transport, jsonFactory).build()

    val verifier = new GoogleIdTokenVerifier.Builder(googlePublicKeysManager)
      .setAudience(List(config.getString("oauth.google.clientId")).asJava)
      .build()
    Try(verifier.verify(idTokenString)).toOption
      .flatMap {
        case null =>
          None
        case idToken =>
          val payload = idToken.getPayload();

          // Get profile information from payload
          Some(GoogleUser(
            payload.getSubject(),
            payload.getEmail,
            payload.get("name").asInstanceOf[String],
            payload.get("family_name").asInstanceOf[String],
            payload.get("given_name").asInstanceOf[String]
          ))
      }
  }
}