package acceptance

import play.api.test._
import play.api.test.Helpers._
import data.TestData._
import data.TestDatabaseProvider
import play.api.libs.json._

class TechControllerSpec extends AcceptanceSpec {

  var dataMap: Map[String, Int] = _

  override def beforeAll {
    setupDatabase()
  }

  before {
    //we need users to authenticate the fake token
    insertUserData()
    dataMap = insertTechData()
  }

  after {
    cleanTechData()
  }

  override def afterAll {
    dropDatabase()
  }

  info("The GET /alltech for getting the list of tech")
  feature("It should return a list of Tech object") {
    scenario("Everything is fine") {
      val request = FakeRequest("GET", "/alltech")
        .withHeaders(("X-AUTH-TOKEN", authToken))

      val response = route(app, request).get

      (request, response) must validateAgainstSwagger(swaggerPath)
    }
  }

}
