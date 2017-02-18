package models.responses

import models.SkillMatrixItem
import play.api.libs.functional.syntax._
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Writes}

case class UserSkillResponse(userId: Int, firstName: String, lastName: String, skills: Seq[SkillMatrixItem])

object UserSkillResponse {
  implicit val skillMatrixForUserResultWrites: Writes[UserSkillResponse] = (
    (JsPath \ "userId").write[Int] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "skill").write[Seq[SkillMatrixItem]]
    )(unlift(UserSkillResponse.unapply _))
}