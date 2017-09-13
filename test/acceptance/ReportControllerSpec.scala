package acceptance

import play.api.test._
import play.api.test.Helpers._
import data.TestData._
import data.TestDatabaseProvider

class ReportControllerSpec extends AcceptanceSpec {
  var dataMap: Map[String, Int] = _

  override def beforeAll {
    setupDatabase()
  }

  before {
    dataMap = insertUserData()
  }

  after {
    cleanUserData()
  }

  override def afterAll {
    dropDatabase()
  }

  info("The GET /report/dm/dmUserId for getting the report if manager")
  feature("It should return 404 when user is not on people api") {
    scenario("User is not on people api") {
      val request = FakeRequest("GET", s"/report/dm/${dataMap(ID_USER_SNAPE)}").withHeaders(("X-AUTH-TOKEN", authTokenManagement))
      val response = route(app, request).get

      status(response) mustEqual NOT_FOUND
    }
  }
  feature("It should return 403 when user is not manager"){
    scenario("user is not manager") {
      val request = FakeRequest("GET", s"/report/dm/${dataMap(ID_USER_ODERSKY)}").withHeaders(("X-AUTH-TOKEN", authToken))
      val response = route(app, request).get

      status(response) mustEqual FORBIDDEN
    }
  }

}
