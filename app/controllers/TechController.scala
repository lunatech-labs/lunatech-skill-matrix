package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.TechDAOService

import scala.concurrent._
import ExecutionContext.Implicits.global

/**
  * Skill Controller.
  */
class TechController @Inject()(techDAOService: TechDAOService) extends Controller {

  def getAllTech() = Action.async(BodyParsers.parse.empty) { _ =>
    techDAOService.getAllTech().map(m =>
      Ok(Json.obj("status" -> "Success", "tech" -> Json.toJson(m))))
  }

}
