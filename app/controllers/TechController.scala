package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.TechService

import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class TechController @Inject()(techService: TechService) extends Controller {

  def getAllTech(): Action[Unit] = Action.async(BodyParsers.parse.empty) { _ =>
    techService.getAllTech.map(m =>
      Ok(Json.obj("tech" -> Json.toJson(m))))
  }
}
