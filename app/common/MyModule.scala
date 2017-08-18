package common

import play.api.Configuration
import play.api.Environment
import play.api.inject._
import scheduler.UpdateUserRolesAndStatusScheduler
import services.oauth.{GoogleOauthService, OauthService}

class MyModule extends Module {

  def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(
      bind[OauthService].to[GoogleOauthService].eagerly(),
      bind[UpdateUserRolesAndStatusScheduler].toSelf.eagerly()
    )
  }
}
