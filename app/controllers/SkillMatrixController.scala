package controllers

import javax.inject.{Inject, Singleton}

import common.{Authentication}
import io.kanaka.monadic.dsl._
import models.{Skill, SkillMatrixItem, Tech}
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.mvc._
import services.{SkillMatrixService, TechService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@Singleton
class SkillMatrixController @Inject()(skillMatrixService: SkillMatrixService,
                                      techService: TechService,
                                      auth: Authentication
                                     ) extends Controller {

  def addSkill: Action[JsValue] = auth.UserAction.async(BodyParsers.parse.json) {
    request =>
      for {
        skillMatrixItem: SkillMatrixItem <- request.body.validate[SkillMatrixItem] ?|
          (err => BadRequest(Json.obj("message" -> JsError.toJson(err))))
        createdSkill <- skillMatrixService.addUserSkill(request.user.getUserId(), skillMatrixItem.tech, skillMatrixItem.skillLevel) ?|
          (err => InternalServerError(Json.obj("message" -> err.getMessage)))
      } yield
        Created(Json.obj(
          "skillAdded" -> Json.toJson(
            SkillMatrixItem(
              tech = Tech(Some(createdSkill.techId), skillMatrixItem.tech.name, skillMatrixItem.tech.techType),
              skillLevel = createdSkill.skillLevel,
              id = createdSkill.id))))
  }

  def updateSkill(skillId: Int): Action[JsValue] = auth.UserAction.async(BodyParsers.parse.json) { implicit request =>
    for {
      skillMatrixItem: SkillMatrixItem <- request.body.validate[SkillMatrixItem] ?|
        (err => BadRequest(Json.obj("message" -> JsError.toJson(err))))

      _ <- validateTechIdPresentForUpdateOperation(skillMatrixItem) ?|
        BadRequest(Json.obj("message" -> getBadRequestResponseForUpdateOperation))

      updatedSkill <- skillMatrixService.updateUserSkill(skillId, request.user.getUserId(), skillMatrixItem.tech, skillMatrixItem.skillLevel) ?|
        NotFound(Json.obj("message" -> "skill could not be found"))

      skillMatrixItem <- getResponseForUpdateOperation(updatedSkill) ?| InternalServerError
    } yield Ok(Json.obj("updatedSkill" -> Json.toJson(skillMatrixItem)))
  }

  def deleteSkill(skillId: Int): Action[Unit] = auth.UserAction.async(BodyParsers.parse.empty) { implicit request =>
    for {
      _ <- skillMatrixService.deleteUserSkill(request.user.getUserId(), skillId) ?| NotFound(Json.obj("message" -> "Skill for this user could not be found"))
    } yield NoContent
  }

  def getUserSkills(userId: Int): Action[Unit] = auth.UserAction.async(BodyParsers.parse.empty) { _ =>
    for {
      userSkills <- skillMatrixService.getUserSkills(userId) ?| NotFound(Json.obj("message" -> "User not found"))
    } yield Ok(Json.obj("userSkills" -> Json.toJson(userSkills)))
  }

  def getSkillMatrix: Action[Unit] = auth.UserAction.async(BodyParsers.parse.empty) { _ =>
    skillMatrixService.getAllSkills.map(result =>
      Ok(Json.obj("skills" -> Json.toJson(result)))
    )
  }

  def getSkillMatrixByTechId(techId: Int): Action[Unit] = auth.UserAction.async(BodyParsers.parse.empty) { _ =>
    for {
      skillMatrixForTech <- skillMatrixService.getSkillMatrixByTechId(techId) ?| NotFound(Json.obj("message" -> "Tech not found"))
    } yield Ok(Json.obj("skills" -> Json.toJson(skillMatrixForTech)))
  }


  def getMySkills: Action[AnyContent] = auth.UserAction.async { implicit request =>
    for {
      userSkills <- skillMatrixService.getUserSkills(request.user.getUserId()) ?| NotFound(Json.obj("message" -> "User not found"))
    } yield Ok(Json.obj("userSkills" -> Json.toJson(userSkills)))
  }

  private def validateTechIdPresentForUpdateOperation(skillMatrixItem: SkillMatrixItem): Future[Option[Int]] = skillMatrixItem.tech.id match {
    case None => Future(None)
    case id => Future(id)
  }

  val getBadRequestResponseForUpdateOperation: JsValueWrapper = {
    Json.parse(
      """{
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
      tech <- techService.getById(skill.techId)
    } yield SkillMatrixItem(tech = tech.get, skillLevel = skill.skillLevel, id = skill.id)
  }
}
