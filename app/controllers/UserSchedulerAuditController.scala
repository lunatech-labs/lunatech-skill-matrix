package controllers

import javax.inject.{Inject, Singleton}

import com.typesafe.scalalogging.LazyLogging
import common.{ApiErrors, Authentication}
import io.kanaka.monadic.dsl._
import models.AccessLevel
import models.ImplicitFormats._
import play.api.libs.json._
import play.api.mvc._
import services.UserSchedulerAuditService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserSchedulerAuditController @Inject()(userSchedulerAuditService: UserSchedulerAuditService, auth: Authentication, components: ControllerComponents) extends AbstractController(components) with LazyLogging {

  def getPeopleApiProcessStatus: Action[Unit] = auth.UserAction(AccessLevel.Admin).async(components.parsers.empty) { request =>
    for {
      auditResponseBody <- userSchedulerAuditService.getLatestJobIno              ?| ApiErrors.USER_AUDIT_FAILURE
    } yield Ok(Json.toJson(auditResponseBody))
  }

}
