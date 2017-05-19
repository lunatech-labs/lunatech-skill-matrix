package common

import com.google.inject.AbstractModule
import com.typesafe.config.{Config, ConfigFactory}
import services.oauth.{GoogleOauthService, OauthService}

class Module extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[Config]).toInstance(ConfigFactory.load())

    bind(classOf[OauthService]).to(classOf[GoogleOauthService]).asEagerSingleton()
  }
}
