package models

import models.EnumTypes.SkillLevel.SkillLevel
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

/**
  * Created by tatianamoldovan on 05/02/2017.
  */
case class SkillMatrix(id: Option[Int] = None, userId: Int, skillId: Int, skillLevel: SkillLevel)

object SkillMatrix {
  implicit val userSkillReads: Reads[SkillMatrix] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "userId").read[Int] and
      (JsPath \ "skillId").read[Int] and
      (JsPath \ "skillLevel").read[SkillLevel]
    )(SkillMatrix.apply _)

  implicit val userSkillWrites: Writes[SkillMatrix] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "userId").write[Int] and
      (JsPath \ "skillId").write[Int] and
      (JsPath \ "skillLevel").write[SkillLevel]
    )(unlift(SkillMatrix.unapply _))
}

