package controllers

import javax.inject.Inject

import models.SkillMatrixItem
import play.api.libs.json._
import play.api.mvc._
import services.SkillMatrixDAOService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by tatianamoldovan on 06/02/2017.
  */


class SkillMatrixController @Inject()(skillMatrixDAOService: SkillMatrixDAOService) extends Controller {
  def addSkillByUserId(userId: Int) = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[SkillMatrixItem].map {
      case (skillMatrixItem: SkillMatrixItem) =>
        skillMatrixDAOService.addSkillByUserIdToSkillMatrix(userId, skillMatrixItem.skill, skillMatrixItem.skillLevel).map(m =>
          Created(Json.obj("status" -> "Success", "skillAdded" -> Json.toJson(m))))
    }.recoverTotal {
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)
      )))
    }
  }

  def updateSkillByUserId(userId: Int, userSkillId: Int) = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[SkillMatrixItem].map {
      case (skillMatrixItem: SkillMatrixItem) =>
        skillMatrixDAOService.updateSkill(userSkillId, userId, skillMatrixItem.skill, skillMatrixItem.skillLevel).map {
          case Some(t) => Ok(Json.obj("status" -> "Success", "updatedSkill" -> Json.toJson(t)))
          case None => NotFound(Json.obj("status" -> "skill could not be found"))
        }
    }.recoverTotal {
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)
      )))
    }
  }

  def deleteSkillByUserId(userId: Int, userSkillId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.deleteSkillByUserId(userSkillId).map(_ =>
      Ok(Json.obj("status" -> "Success"))
    )
  }

  def getSkillsByUserId(userId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getAllSkillsByUserId(userId).map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m)))
    )
  }

  /*def getSkills() = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixService.getAllSkillsFromSkillMatrix().map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m))))
  }*/

  def getSkillById(skillId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getSkillById(skillId).map(m =>
      Ok(Json.obj("status" -> "Success", "skills" -> Json.toJson(m))))
  }
}
