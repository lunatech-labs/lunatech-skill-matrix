package models

import models._
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

case class Skill(id: Option[Int] = None, userId: Int, techId: Int, skillLevel: SkillLevel)

object Skill {
  implicit val userSkillReads: Reads[Skill] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "userId").read[Int] and
      (JsPath \ "techId").read[Int] and
      (JsPath \ "skillLevel").read[SkillLevel]
    )(Skill.apply _)

  implicit val userSkillWrites: Writes[Skill] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "userId").write[Int] and
      (JsPath \ "techId").write[Int] and
      (JsPath \ "skillLevel").write[SkillLevel]
    )(unlift(Skill.unapply _))
}

