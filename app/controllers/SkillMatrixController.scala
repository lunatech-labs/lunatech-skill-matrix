package controllers

import javax.inject.Inject

import io.kanaka.monadic.dsl._
import models.{SkillMatrix, SkillMatrixItem, Tech}
import org.omg.CosNaming.NamingContextPackage.NotFound
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

  def addSkillByUserId(userId: Int) = Action.async(BodyParsers.parse.json) {
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

  def updateSkillByUserId(userId: Int, skillId: Int) = Action.async(BodyParsers.parse.json) {
    request =>
      for {
        skillMatrixItem: SkillMatrixItem <- request.body.validate[SkillMatrixItem] ?|
          (err => BadRequest(Json.obj("message" -> JsError.toJson(err))))

        updatedSkill <- skillMatrixDAOService.updateSkill(skillId, userId, skillMatrixItem.tech, skillMatrixItem.skillLevel) ?|
          NotFound(Json.obj("message" -> "skill could not be found"))

        skillMatrixItem <- getSkillMatrixItem(updatedSkill) ?| InternalServerError
      } yield Ok(Json.obj("updatedSkill" -> Json.toJson(skillMatrixItem)))
  }

  def deleteSkillByUserSkillId(userId: Int, userSkillId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.deleteSkillByUserId(userId, userSkillId).map {
      case None => NotFound(Json.obj("message" -> "Skill for this user could not be found"))
      case _ => NoContent
   }
  }

  def getSkillsByUserId(userId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getAllSkillMatrixByUserId(userId).map {
      case None=> NotFound(Json.obj("message" -> "User not found"))
      case m => Ok(Json.obj("user" -> Json.toJson(m)))
    }
  }

  def getSkills() = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getAllSkills().map ( result =>
      Ok(Json.obj("skills" -> Json.toJson(result)))
    )
  }

  def getSkillByTechId(techId: Int) = Action.async(BodyParsers.parse.empty) { _ =>
    skillMatrixDAOService.getSkillMatrixByTechId(techId).map(m =>
      Ok(Json.obj("skills" -> Json.toJson(m))))
  }



  private def getSkillMatrixItem(skill: SkillMatrix): Future[SkillMatrixItem] = {
    for {
      tech <- techDAOService.getTechById(skill.techId)
    } yield SkillMatrixItem(tech = tech.get, skillLevel = skill.skillLevel)
  }
}
