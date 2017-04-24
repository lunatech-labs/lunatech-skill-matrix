package controllers

import javax.inject.{Inject, Singleton}

import common.Authentication
import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.TechService
import models.ImplicitFormats._

import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class TechController @Inject()(techService: TechService, auth: Authentication) extends Controller {

  def getAllTech: Action[Unit] = auth.UserAction.async(BodyParsers.parse.empty) { _ =>
    techService.getAllTech.map(m => Ok(Json.obj("tech" -> Json.toJson(m))))
  }

  def search(query: String): Action[Unit] = auth.UserAction.async(BodyParsers.parse.empty) { _ =>
    techService.search(query).map(techs => Ok(Json.toJson(techs)))
  }
}
