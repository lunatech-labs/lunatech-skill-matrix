package services.oauth

import javax.inject.Singleton
import javax.inject.Inject

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload
import com.google.api.client.googleapis.auth.oauth2.{GoogleIdTokenVerifier, GooglePublicKeysManager}
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.typesafe.config.Config
import play.api.inject.ApplicationLifecycle

import scala.collection.JavaConverters._
import scala.util.Try


@Singleton
class GoogleOauthService @Inject()(lifecycle: ApplicationLifecycle, config: Config) extends OauthService {

  private val validDomains = config.getString("oauth.google.domain").split(",").toList

  def verifyToken(idTokenString: String): Option[GoogleUser] = {

    val transport = new NetHttpTransport()
    val jsonFactory = new JacksonFactory()
    val googlePublicKeysManager = new GooglePublicKeysManager.Builder(transport, jsonFactory).build()

    val verifier = new GoogleIdTokenVerifier.Builder(googlePublicKeysManager)
      .setAudience(List(config.getString("oauth.google.clientId")).asJava)
      .build()

    Try(verifier.verify(idTokenString)).toOption
      .flatMap {
        case null => None
        case idToken => verifyUserDomain(idToken.getPayload, validDomains)
      }
  }

  private def verifyUserDomain(payload: Payload, domains: List[String]) = if (domains.contains(payload.getHostedDomain)) {
    // Get profile information from payload
    Some(GoogleUser(
      payload.getSubject,
      payload.getEmail,
      payload.get("name").asInstanceOf[String],
      payload.get("family_name").asInstanceOf[String],
      payload.get("given_name").asInstanceOf[String]
    ))
  } else {
    None
  }

}