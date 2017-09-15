package scheduler

import javax.inject.Inject

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import models.ImplicitFormats._
import models.User
import org.joda.time.DateTime
import play.api.inject.ApplicationLifecycle
import play.api.libs.json.{JsValue, Json}
import services.{PeopleAPIProcessor, UserSchedulerAuditService}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class UpdateUserRolesAndStatusScheduler @Inject()(actorSystem: ActorSystem, lifecycle: ApplicationLifecycle, peopleAPIProcessor: PeopleAPIProcessor, userSchedulerAuditService: UserSchedulerAuditService)(implicit executionContext: ExecutionContext) {

  val taskTime: DateTime = DateTime.now.withTimeAtStartOfDay().plusDays(1).minusMinutes(5)
  val delay: Long = taskTime.getMillis - DateTime.now.getMillis


  val sa: ActorRef = actorSystem.actorOf(Props(new ScheduleActor), "scheduleActor")
  actorSystem.scheduler.scheduleOnce(0.millis, sa, ScheduleActor.RunOnStart)
  actorSystem.scheduler.scheduleOnce(delay.millis, sa, ScheduleActor.RunUser)

  // This is necessary to avoid thread leaks, specially if you are
  // using a custom ExecutionContext
  lifecycle.addStopHook{ () =>
    Future.successful(actorSystem.terminate())
  }

  class ScheduleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case ScheduleActor.RunOnStart =>
        updateUsers()
      case ScheduleActor.RunUser =>
        updateUsers()
        context.system.scheduler.scheduleOnce(1.day,self,ScheduleActor.RunUser)
      case unknown => log.error(s"Received unknown message: $unknown")
    }

    private def updateUsers() = {
      for {
        userWithUpdatedRoles <- peopleAPIProcessor.updateAccessLevels()
        (usersToActivate, usersToDeactivate) <- peopleAPIProcessor.updateUsersStatus()
        jsonbody: JsValue = Json.obj("usersWithUpdatedRoles" -> Json.toJson(userWithUpdatedRoles), "usersToActivate" -> Json.toJson(usersToActivate), "usersToDeactivate" -> Json.toJson(usersToDeactivate))
        _ <- userSchedulerAuditService.addLog("Success", jsonbody)
        _ = log.info("Processed changes from People API. Response is {}", jsonbody)
      } yield jsonbody
    }
  }

  object ScheduleActor {
    sealed trait ScheduleActorMessage
    case object RunUser extends ScheduleActorMessage
    case object RunOnStart extends ScheduleActorMessage
  }
}