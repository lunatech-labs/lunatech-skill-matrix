package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.TechService

import scala.concurrent._
import ExecutionContext.Implicits.global

/**
  * Skill Controller.
  */
class TechController @Inject()(techService: TechService) extends Controller {

  def getAllTech() = Action.async(BodyParsers.parse.empty) { _ =>
    techService.getAllTech.map(m =>
      Ok(Json.obj("tech" -> Json.toJson(m))))
  }
}
