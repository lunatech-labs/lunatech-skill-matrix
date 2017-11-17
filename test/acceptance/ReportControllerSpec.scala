package acceptance

import play.api.test._
import play.api.test.Helpers._
import data.TestData._
import data.TestDatabaseProvider
import pactdependent.Stubber

class ReportControllerSpec extends AcceptanceSpec {
  var dataMap: Map[String, Int] = _
  val stubber: Stubber = Stubber()

  override def beforeAll {
    stubber.startStubber()
    setupDatabase()
  }

  before {
    dataMap = insertUserData()
  }

  after {
    cleanUserData()
  }

  override def afterAll {
    stubber.stopStubber()
    dropDatabase()
  }

  info("The GET /report/dm/dmUserId for getting the report if manager")
  feature("It should return 200 when user a manager") {
    scenario("User is a manager") {
      val request = FakeRequest("GET", s"/report/lastupdate").withHeaders(("X-AUTH-TOKEN", authTokenManagement))
      val response = route(app, request).get

      status(response) mustEqual OK
    }
  }
  feature("It should return 403 when user is not manager"){
    scenario("user is not manager") {
      val request = FakeRequest("GET", s"/report/lastupdate").withHeaders(("X-AUTH-TOKEN", authToken))
      val response = route(app, request).get

      status(response) mustEqual FORBIDDEN
    }
  }

}
