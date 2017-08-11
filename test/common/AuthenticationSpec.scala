package common

import play.api.test.FakeRequest
import services.UnitSpec
import play.api.test.Helpers._

class AuthenticationSpec extends UnitSpec {
  override def beforeAll {
    setupDatabase()
  }

  before {
    insertTechData()
  }

  after {
    cleanTechData()
  }

  override def afterAll {
    dropDatabase()
  }

  "Authentication" should {
    "Allow request with the API secret" in {
      val request = FakeRequest("GET","/alltech").withHeaders(("X-ID-TOKEN","api-secret-test"))
      val response = route(app,request).get

      status(response) mustBe OK
    }
  }

}
