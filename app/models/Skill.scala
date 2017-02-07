package models

import models.EnumTypes.SkillType.SkillType
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

/**
  * Created by tatianamoldovan on 02/02/2017.
  */

case class Skill(id: Option[Int], name: String, skillType: SkillType)

object Skill {
  implicit val skillReads: Reads[Skill] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "skillType").read[SkillType]
  )(Skill.apply _)

  implicit val skillWrites: Writes[Skill] = (
      (JsPath \ "id").writeNullable[Int] and
        (JsPath \ "name").write[String] and
        (JsPath \ "skillType").write[SkillType]
  )(unlift(Skill.unapply _))
}
