package acceptance

import play.api.test._
import play.api.test.Helpers._
import data.TestData._
import data.TestDatabaseProvider
import play.api.libs.json._


class UserControllerSpec extends AcceptanceSpec with TestDatabaseProvider {

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

  info("The GET /users/4 for getting the info about an user")
  feature("It should return UserSkillResponse object") {
    scenario("Everything is fine") {
      val request = FakeRequest("GET", s"/users/${dataMap(ID_USER_SNAPE)}").withHeaders(("X-AUTH-TOKEN", authToken))
      val response = route(app, request).get

      val respSeverus = getUserByIdResponse.
        transform(__.json.update((__ \ 'user \ 'id).json.put(JsNumber(dataMap(ID_USER_SNAPE))))).get

      status(response) mustEqual 200
      contentAsString(response) must include(respSeverus.toString())

    }
  }

}
