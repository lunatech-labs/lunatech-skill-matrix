package models.requests

import models.EnumTypes.SkillLevel.SkillLevel
import models.Tech
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class AddSkillRequest(newTech: Tech, skillLevel: SkillLevel)
object AddSkillRequest {
  implicit val addSkillRequestReads: Reads[AddSkillRequest] = (
    (JsPath \ "tech").read[Tech] and
      (JsPath \ "skillLevel").read[SkillLevel]
    ) (AddSkillRequest.apply _)
}
