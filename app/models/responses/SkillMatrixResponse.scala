package models.responses

import models._
import play.api.libs.functional.syntax._
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Writes}

case class SkillMatrixResponse(techId: Int, techName: String, techType: TechType, users: Seq[SkillMatrixUsersAndLevel])

object SkillMatrixResponse {
  implicit val skillMatrixResultWrites: Writes[SkillMatrixResponse] = (
    (JsPath \ "techId").write[Int] and
      (JsPath \ "techName").write[String] and
      (JsPath \ "techType").write[TechType] and
      (JsPath \ "users").write[Seq[SkillMatrixUsersAndLevel]]
    )(unlift(SkillMatrixResponse.unapply _))
}


case class SkillMatrixUsersAndLevel(userName: String, level: SkillLevel)
object SkillMatrixUsersAndLevel {
  implicit val SkillMatrixUsersAndLevelWrites: Writes[SkillMatrixUsersAndLevel] = (
    (JsPath \ "userName").write[String] and
      (JsPath \ "level").write[SkillLevel]
    )(unlift(SkillMatrixUsersAndLevel.unapply _))
}



