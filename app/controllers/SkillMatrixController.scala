package controllers

import javax.inject.Inject

import io.kanaka.monadic.dsl._
import models.SkillMatrixItem
import play.api.libs.json._
import play.api.mvc._
import services.SkillMatrixDAOService

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Skill Matrix Controller.
  * The controller is responsible for the operations related to a users's skills.
  */


class SkillMatrixController @Inject()(skillMatrixDAOService: SkillMatrixDAOService) extends Controller {

  def addSkillByUserId(userId: Int) = Action.async(BodyParsers.parse.json) {
    request =>
      for {
        skillMatrixItem: SkillMatrixItem <- request.body.validate[SkillMatrixItem] ?|
          (err => BadRequest(Json.obj("message" -> JsError.toJson(err))))
        createdSkill <- skillMatrixDAOService.addSkillByUserIdToSkillMatrix(userId, skillMatrixItem.skill, skillMatrixItem.skillLevel) ?|
          InternalServerError(Json.obj("message" -> "unknown error"))
      } yield Created(Json.obj("skillAdded" -> Json.toJson(createdSkill)))

  }

  def updateSkillByUserId(userId: Int, userSkillId: Int) = Action.async(BodyParsers.parse.json) {
    request =>
      for {
        skillMatrixItem: SkillMatrixItem <- request.body.validate[SkillMatrixItem] ?|
          (err => BadRequest(Json.obj("message" -> JsError.toJson(err))))
        updatedSkill <- skillMatrixDAOService.updateSkill(userSkillId, userId, skillMatrixItem.skill, skillMatrixItem.skillLevel) ?|
          NotFound(Json.obj("message" -> "skill could not be found"))
      } yield Ok(Json.obj("updatedSkill" -> Json.toJson(updatedSkill)))
  }

  def deleteSkillByUserSkillId(userId: Int, userSkillId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.deleteSkillByUserId(userId, userSkillId).map {
      case None => NotFound(Json.obj("message" -> "Skill for this user could not be found"))
      case _ => NoContent
   }
  }

  def getSkillsByUserId(userId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getAllSkillsByUserId(userId).map {
      case List() => NotFound(Json.obj("message" -> "User not found"))
      case m => Ok(Json.obj("skills" -> Json.toJson(m)))
    }
  }

  /* TO BE DEFINED
  def getSkills() = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixService.getAllSkillsFromSkillMatrix().map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m))))
  }*/

  def getSkillById(skillId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getSkillById(skillId).map( m =>
      Ok(Json.obj("skills" -> Json.toJson(m))))
  }
}
