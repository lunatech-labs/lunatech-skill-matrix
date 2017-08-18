package services

import javax.inject.Inject

import common.DBConnection
import models.db.UserSchedulerAudit
import play.api.libs.json.JsValue

import scala.concurrent.Future

class UserSchedulerAuditService @Inject()(implicit connection: DBConnection) {

  def addLog(status: String, body: JsValue ): Future[Int] = {
    UserSchedulerAudit.addLog(status, body)
  }

  def getLatestJobIno: Future[Option[models.UserSchedulerAudit]] = {
    UserSchedulerAudit.getLatestJobInfo()
  }

}
