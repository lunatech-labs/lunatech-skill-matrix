package models.requests

import models.EnumTypes.SkillLevel.SkillLevel
import models.Tech
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}


case class UpdateSkillRequest(tech: Tech, skillLevel: SkillLevel)

object UpdateSkillRequest {
  implicit val addSkillRequestReads: Reads[UpdateSkillRequest] = (
    (JsPath \ "tech").read[Tech] and
      (JsPath \ "skillLevel").read[SkillLevel]
    ) (UpdateSkillRequest.apply _)
}