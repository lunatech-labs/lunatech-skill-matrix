package controllers

import io.kanaka.monadic.dsl._
import javax.inject.{Inject, Singleton}

import common.{ApiErrors, Authentication}
import models.{AccessLevel, Tech}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{Action, BodyParsers, Controller}
import services.TechService
import models.ImplicitFormats._

import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class TechController @Inject()(techService: TechService, auth: Authentication) extends Controller {

  def getAllTech: Action[Unit] = auth.UserAction(AccessLevel.Basic).async(BodyParsers.parse.empty) { _ =>
    techService.getAllTech.map(m => Ok(Json.toJson(m)))
  }

  def search(query: String): Action[Unit] = auth.UserAction().async(BodyParsers.parse.empty) { _ =>
    techService.search(query).map(techs => Ok(Json.toJson(techs)))
  }

  def updateTech(techId: Int): Action[JsValue] = auth.UserAction(AccessLevel.Management).async(BodyParsers.parse.json) { implicit request =>
    for {
      updatedTech: Tech <- request.body.validate[Tech]  ?| (err => ApiErrors.badRequest(JsError.toJson(err)))
      _ <- techService.getById(techId)                  ?| ApiErrors.TECH_NOT_FOUND
      _ <- techService.updateTech(techId, updatedTech)  ?| ApiErrors.DUPLICATE_TECH_NAME
    } yield Ok(Json.toJson(updatedTech.copy(id = Some(techId))))
  }

  def removeTech(techId: Int): Action[Unit] = auth.UserAction(AccessLevel.Admin).async(BodyParsers.parse.empty) { _=>
    for {
      _ <- techService.removeTech(techId)  ?| ApiErrors.TECH_NOT_FOUND
    } yield NoContent
  }
}
