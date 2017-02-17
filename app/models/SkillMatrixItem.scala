package models

import models.EnumTypes.SkillLevel.SkillLevel
import models.EnumTypes.TechType.TechType
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes}

case class SkillMatrixItem(tech: Tech, skillLevel: SkillLevel)
object SkillMatrixItem {
  implicit val skillMatrixReads: Reads[SkillMatrixItem] = (
    (JsPath \ "tech").read[Tech] and
      (JsPath \ "skillLevel").read[SkillLevel]
    )(SkillMatrixItem.apply _)

  implicit val skillMatrixItemWrites: Writes[SkillMatrixItem] = (
    (JsPath \ "tech").write[Tech] and
      (JsPath \ "skillLevel").write[SkillLevel]
    )(unlift(SkillMatrixItem.unapply _))
}

case class SkillMatrixResult(techId: Int, techName: String, techType: TechType, users: Seq[SkillMatrixUsersAndLevel])
case class SkillMatrixUsersAndLevel(userId: Int, level: SkillLevel)

object SkillMatrixUsersAndLevel {
  implicit val SkillMatrixUsersAndLevelWrites: Writes[SkillMatrixUsersAndLevel] = (
    (JsPath \ "userId").write[Int] and
      (JsPath \ "level").write[SkillLevel]
    )(unlift(SkillMatrixUsersAndLevel.unapply _))
}

object SkillMatrixResult {
  implicit val skillMatrixResultWrites: Writes[SkillMatrixResult] = (
    (JsPath \ "techId").write[Int] and
      (JsPath \ "techName").write[String] and
      (JsPath \ "techType").write[TechType] and
      (JsPath \ "users").write[Seq[SkillMatrixUsersAndLevel]]
    )(unlift(SkillMatrixResult.unapply _))
}

