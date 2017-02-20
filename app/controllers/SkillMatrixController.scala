package controllers

import javax.inject.Inject

import io.kanaka.monadic.dsl._
import models.{Skill, SkillMatrixItem, Tech}
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.mvc._
import services.{SkillMatrixDAOService, TechDAOService}

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Skill Matrix Controller.
  * The controller is responsible for the operations related to a users's skills.
  */
class SkillMatrixController @Inject()(skillMatrixDAOService: SkillMatrixDAOService,
                                      techDAOService: TechDAOService) extends Controller {

  def addSkill(userId: Int) = Action.async(BodyParsers.parse.json) {
    request =>
      for {
        skillMatrixItem: SkillMatrixItem <- request.body.validate[SkillMatrixItem] ?|
          (err => BadRequest(Json.obj("message" -> JsError.toJson(err))))
        createdSkill <- skillMatrixDAOService.addSkillByUserIdToSkillMatrix(userId, skillMatrixItem.tech, skillMatrixItem.skillLevel) ?|
          (err => InternalServerError(Json.obj("message" -> err.getMessage)))
      } yield Created(Json.obj(
        "skillAdded" -> Json.toJson(
          SkillMatrixItem(
            tech = Tech(Some(createdSkill.techId), skillMatrixItem.tech.name, skillMatrixItem.tech.techType),
            skillLevel = createdSkill.skillLevel))))
  }

  def updateSkill(userId: Int, skillId: Int) = Action.async(BodyParsers.parse.json) {
    request =>
      for {
        skillMatrixItem: SkillMatrixItem <- request.body.validate[SkillMatrixItem] ?|
          (err => BadRequest(Json.obj("message" -> JsError.toJson(err))))

         _techID <- validateTechIdPresentForUpdateOperation(skillMatrixItem) ?|
            BadRequest(Json.obj("message" -> getBadRequestResponseForUpdateOperation()))

        updatedSkill <- skillMatrixDAOService.updateSkill(skillId, userId, skillMatrixItem.tech, skillMatrixItem.skillLevel) ?|
          NotFound(Json.obj("message" -> "skill could not be found"))

        skillMatrixItem <- getResponseForUpdateOperation(updatedSkill) ?| InternalServerError
      } yield Ok(Json.obj("updatedSkill" -> Json.toJson(skillMatrixItem)))
  }

  def deleteSkill(userId: Int, userSkillId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.deleteSkillByUserId(userId, userSkillId).map {
      case None => NotFound(Json.obj("message" -> "Skill for this user could not be found"))
      case _ => NoContent
   }
  }

  def getUserSkills(userId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getAllSkillMatrixByUserId(userId).map {
      case None=> NotFound(Json.obj("message" -> "User not found"))
      case m => Ok(Json.obj("userSkills" -> Json.toJson(m)))
    }
  }

  def getSkillMatrix() = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getAllSkills().map ( result =>
      Ok(Json.obj("skills" -> Json.toJson(result)))
    )
  }

  def getSkillMatrixByTechId(techId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getSkillMatrixByTechId(techId).map{
      case None => NotFound(Json.obj("message" -> "Tech not found"))
      case m => Ok(Json.obj("skills" -> Json.toJson(m)))}
  }

  private def validateTechIdPresentForUpdateOperation(skillMatrixItem: SkillMatrixItem): Future[Option[Int]] = skillMatrixItem.tech.id match {
    case None => Future(None)
    case id => Future(id)
  }

  private def getBadRequestResponseForUpdateOperation(): JsValueWrapper = {
    Json.parse("""{
        "obj.tech.id": [
      {
        "msg": [
        "error.path.missing"
        ],
        "args": []
      }
        ]
      }""".stripMargin)
  }

  private def getResponseForUpdateOperation(skill: Skill): Future[SkillMatrixItem] = {
    for {
      tech <- techDAOService.getTechById(skill.techId)
    } yield SkillMatrixItem(tech = tech.get, skillLevel = skill.skillLevel)
  }
}
