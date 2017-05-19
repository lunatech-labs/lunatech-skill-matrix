package common

object Utils {
  def isAccessLogger(incomingLoggerName: String): Boolean = {
    List(
      "common.Authentication",
      "common.ApiErrors",
      "controllers.GoogleSignInController",
      "models.db.Skills",
      "models.db.Users",
      "models.db.Techs")
      .exists(
        loggerName => incomingLoggerName.contains(loggerName))
  }

}
