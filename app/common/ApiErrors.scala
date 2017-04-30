package common

import play.api.libs.json._
import play.api.libs.json.Json
import play.api.mvc.Results._

case class Error(code: String, message: String)

object ApiErrors {

  implicit val errorFormat: Format[Error] = Json.format[Error]

  val USER_NOT_FOUND = NotFound(Json.toJson(Error("NOT_FOUND", "User not found")))
  val TECH_NOT_FOUND = NotFound(Json.toJson(Error("NOT_FOUND", "Tech not found")))
  val SKILL_NOT_FOUND = NotFound(Json.toJson(Error("NOT_FOUND", "Skill not found")))
  val UNAUTHORIZED = Unauthorized(Json.toJson(Error("INVALID_TOKEN", "User token is invalid!")))
  val INTERNAL_SERVER_ERROR = InternalServerError

  def badRequest(message: JsValue) = BadRequest(Json.toJson(Error("INVALID_JSON", message.toString)))
  def internalServerError(message: String) = InternalServerError(Json.toJson(Error("INTERNAL_ERROR", message)))

}
