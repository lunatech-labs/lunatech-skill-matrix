package services

import javax.inject.{Inject, Singleton}
import com.typesafe.config.Config
import services.oauth.{GoogleUser, OauthService}

@Singleton
class FakeOauthService @Inject()(config: Config) extends OauthService{

  override def verifyToken(idTokenString: String): Option[GoogleUser] = idTokenString.split("-").head match {
    case level: String =>
      Some(GoogleUser(
        config.getString(s"users.$level.googleId"),
        config.getString(s"users.$level.email"),
        config.getString(s"users.$level.name"),
        config.getString(s"users.$level.familyName"),
        config.getString(s"users.$level.givenName")
      ))
  }
}
