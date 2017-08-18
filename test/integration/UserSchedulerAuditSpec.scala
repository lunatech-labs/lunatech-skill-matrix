package integration

import models.UserSchedulerAudit
import models.db.UserSchedulerAudit._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

class UserSchedulerAuditSpec extends IntegrationSpec {

  override def beforeAll {
    setupDatabase()
  }

  before {
    insertUserData()
  }

  after {
      cleanUserData()
  }

  override def afterAll {
    dropDatabase()
  }

  "UserSchedulerAudit" should {
    "add log to table" in {
      addLog("Success", Json.toJson("""{"message": "test"}"""))(dbConn).futureValue > 0 mustEqual true
    }

    "get most recent update" in {
      val recentStatus: String = "most recent success"

      addLog("Success", Json.toJson("""{"message": "test"}"""))(dbConn).map ( _ =>
        addLog(recentStatus, Json.toJson("""{"message": "second test"}"""))(dbConn)).futureValue

      val response: Option[UserSchedulerAudit] = getLatestJobInfo()(dbConn).futureValue
      response.map(log => log.status.equals(recentStatus)) mustBe Some(true)
    }
  }
}
