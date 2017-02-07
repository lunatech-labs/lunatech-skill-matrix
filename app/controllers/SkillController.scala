package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.SkillService
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
  * Created by tatianamoldovan on 07/02/2017.
  */
class SkillController @Inject() (skillService: SkillService) extends Controller {

  def getSkills() = Action.async(BodyParsers.parse.empty) { _ =>
    skillService.getSkills().map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m))))
  }

}
