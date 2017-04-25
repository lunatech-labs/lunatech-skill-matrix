package common

import play.api.libs.json._
import play.api.libs.json.Json
import play.api.mvc.Results._

case class Error(code: Int, message: String)

object ApiErrors {

  implicit val errorFormat: Format[Error] = Json.format[Error]

  val USER_NOT_FOUND = NotFound(Json.toJson(Error(404, "User not found")))
  val TECH_NOT_FOUND = NotFound(Json.toJson(Error(404, "Tech not found")))
  val SKILL_NOT_FOUND = NotFound(Json.toJson(Error(404, "Skill not found")))
  val UNAUTHORIZED = Unauthorized(Json.toJson(Error(401, "User token is invalid!")))
  val INTERNAL_SERVER_ERROR = InternalServerError

  def badRequest(message: JsValue) = BadRequest(Json.toJson(Error(400, message.toString)))
  def internalServerError(message: String) = InternalServerError(Json.toJson(Error(500, message)))

}
