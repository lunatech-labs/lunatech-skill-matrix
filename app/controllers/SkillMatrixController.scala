package controllers

import javax.inject.Inject

import models.EnumTypes.SkillLevel.SkillLevel
import models.Skill
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import services.SkillMatrixService

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by tatianamoldovan on 06/02/2017.
  */
class SkillMatrixController @Inject()(skillMatrixService: SkillMatrixService) extends Controller {

  def addSkillByUserId(userId: Int) = Action.async(BodyParsers.parse.json) { request =>
    val skill = (request.body \ "skill").validate[Skill]
    val skillLevel = (request.body \ "skillLevel").validate[SkillLevel]
    skill.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)
      ))),
      _ =>
        skillMatrixService.addSkillByUserId(userId, skill.get, skillLevel.get).map(m =>
          Ok(Json.obj("status" -> "Success", "skillAdded" -> Json.toJson(m))))
    )
  }

  def updateSkillByUserId(userId: Int, userSkillId: Int) = Action.async(BodyParsers.parse.json) { request =>
    val skill = (request.body \ "skill").validate[Skill]
    val skillLevel = (request.body \ "skillLevel").validate[SkillLevel]
    skill.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)
      ))),
      _ =>
        skillMatrixService.updateSkillByUserId(userSkillId, userId, skill.get, skillLevel.get).map(_ =>
          Ok(Json.obj("status" -> "Success")))
    )
  }

  def deleteSkillByUserId(userId: Int, userSkillId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixService.deleteSkillByUserId(userSkillId).map(_ =>
      Ok(Json.obj("status" -> "Success"))
    )
  }

  def getSkillsByUserId(userId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixService.getAllSkillsByUserId(userId).map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m)))
    )
  }

  /*def getSkills() = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixService.getAllSkillsFromSkillMatrix().map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m))))
  }*/

  def getSkillById(skillId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixService.getSkillById(skillId).map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m))))
  }
}
