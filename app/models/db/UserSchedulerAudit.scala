package models.db

import com.typesafe.scalalogging.LazyLogging
import common.DBConnection
import models.db.CustomPostgresProfile.api._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.json.JsValue
import slick.lifted.ProvenShape

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class UserSchedulerAudit(tag: Tag)  extends Table[models.UserSchedulerAudit](tag, "users_scheduler_audit") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def createdAt: Rep[DateTime] = column[DateTime]("createdat")
  def status: Rep[String] = column[String]("status")
  def body: Rep[JsValue] = column[JsValue]("body")

  override def * : ProvenShape[models.UserSchedulerAudit] = (id.?, createdAt, status, body) <> ((models.UserSchedulerAudit.apply _).tupled, models.UserSchedulerAudit.unapply _)
}

object UserSchedulerAudit extends LazyLogging {
  val userSchedulerAuditTable: TableQuery[UserSchedulerAudit] = TableQuery[UserSchedulerAudit]

  def addLog(status: String, body: JsValue)(implicit connection: DBConnection): Future[Int] = {
    val newLog = models.UserSchedulerAudit(None, createdAt = DateTime.now(), status = status, body = body)
    val query = userSchedulerAuditTable += newLog
    connection.db.run(query)
  }

  def getLatestJobInfo()(implicit connection: DBConnection): Future[Option[models.UserSchedulerAudit]] = {
    val query = userSchedulerAuditTable.sortBy(_.createdAt.desc).take(1)
    connection.db.run(query.result.headOption)
  }
}