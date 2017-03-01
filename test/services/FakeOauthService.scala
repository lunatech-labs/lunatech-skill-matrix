package services

import javax.inject.{Inject, Singleton}
import com.typesafe.config.Config
import services.oauth.{GoogleUser, OauthService}

@Singleton
class FakeOauthService @Inject()(config: Config) extends OauthService{

  override def verifyToken(idTokenString: String): Option[GoogleUser] = {
    Some(GoogleUser(
      config.getString("user.googleId"),
      config.getString("user.email"),
      config.getString("user.name"),
      config.getString("user.familyName"),
      config.getString("user.givenName")
    ))
  }
}
