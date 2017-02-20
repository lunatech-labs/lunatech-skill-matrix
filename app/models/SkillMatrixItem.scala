package models

import models._
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



