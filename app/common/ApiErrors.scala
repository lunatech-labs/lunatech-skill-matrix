package common

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results._

case class Error(code: String, message: String)

object ApiErrors extends LazyLogging {

  implicit val errorFormat: Format[Error] = Json.format[Error]

  val USER_NOT_FOUND = NotFound(Json.toJson(Error("NOT_FOUND", "User not found")))
  val TECH_NOT_FOUND = NotFound(Json.toJson(Error("NOT_FOUND", "Tech not found")))
  val DUPLICATE_TECH_NAME = Conflict(Json.toJson(Error("DUPLICATE_TECH_NAME", "Either tech id was not found in the database or there is a tech with the name you're trying to save")))
  val SKILL_NOT_FOUND = NotFound(Json.toJson(Error("NOT_FOUND", "Skill not found")))
  val UNAUTHORIZED = Unauthorized(Json.toJson(Error("INVALID_TOKEN", "User token is invalid!")))
  val INTERNAL_SERVER_ERROR = InternalServerError

  def badRequest(message: JsValue) = BadRequest(Json.toJson(Error("INVALID_JSON", message.toString)))

  def internalServerError(message: String): Result = {
    logger.info("internal server error with message {}", message)
    InternalServerError(Json.toJson(Error("INTERNAL_ERROR", message)))
  }

}
