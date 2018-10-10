package controllers

import io.kanaka.monadic.dsl._
import javax.inject.{Inject, Singleton}

import common.{ApiErrors, Authentication}
import models.AccessLevel
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import models.ImplicitFormats._
import services.ReportService

import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class ReportController @Inject()(reportService: ReportService, auth: Authentication, components: ControllerComponents) extends AbstractController(components) {


  def getLastUpdateReport: Action[Unit] = auth.UserAction(AccessLevel.Management).async(components.parsers.empty) { _ =>
    for {
      users <- reportService.lastUpdateReport ?| ApiErrors.USER_NOT_FOUND
    } yield Ok(Json.toJson(users))
  }

  def getDMReport: Action[Unit] = auth.UserAction(AccessLevel.Management).async(components.parsers.empty) { req =>
    for {
      user <- reportService.dmReport(req.user.email) ?| ApiErrors.USER_NOT_FOUND
    } yield Ok(Json.toJson(user))
  }

}
