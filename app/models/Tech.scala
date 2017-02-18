package models

import models.EnumTypes.TechType.TechType
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

case class Tech(id: Option[Int], name: String, techType: TechType)

object Tech {
  implicit val techReads: Reads[Tech] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "techType").read[TechType]
    )(Tech.apply _)

  implicit val techWrites: Writes[Tech] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "techType").write[TechType]
    )(unlift(Tech.unapply _))
}



//WIP
/*case class NewTech(id: Option[Int], name: String, techType: TechType)

object NewTech {
  implicit val techReads: Reads[NewTech] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "techType").read[TechType]
    )(NewTech.apply _)

  implicit val techWrites: Writes[NewTech] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "techType").write[TechType]
    )(unlift(NewTech.unapply _))
}*/
