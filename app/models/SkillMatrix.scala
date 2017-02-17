package models

import models.EnumTypes.SkillLevel.SkillLevel
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

case class SkillMatrix(id: Option[Int] = None, userId: Int, techId: Int, skillLevel: SkillLevel)

object SkillMatrix {
  implicit val userSkillReads: Reads[SkillMatrix] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "userId").read[Int] and
      (JsPath \ "techId").read[Int] and
      (JsPath \ "skillLevel").read[SkillLevel]
    )(SkillMatrix.apply _)

  implicit val userSkillWrites: Writes[SkillMatrix] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "userId").write[Int] and
      (JsPath \ "techId").write[Int] and
      (JsPath \ "skillLevel").write[SkillLevel]
    )(unlift(SkillMatrix.unapply _))
}

