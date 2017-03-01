package acceptance

import play.api.test._
import play.api.test.Helpers._
import data.TestData._
import data.TestDatabaseProvider
import play.api.libs.json._

class TechControllerSpec extends AcceptanceSpec with TestDatabaseProvider {

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

      val respScala = allTechScala.
        transform(__.json.update((__ \ 'id).json.put(JsNumber(dataMap(ID_TECH_SCALA))))).get
      val respFunctional = allTechFunctional
        .transform(__.json.update((__ \ 'id).json.put(JsNumber(dataMap(ID_TECH_FUNCTIONAL))))).get
      val respDefense = allTechDefense
        .transform(__.json.update((__ \ 'id).json.put(JsNumber(dataMap(ID_TECH_DEFENSE))))).get
      val respDarkArts = allTechDarkArts
        .transform(__.json.update((__ \ 'id).json.put(JsNumber(dataMap(ID_TECH_DARK_ARTS))))).get

      status(response) mustEqual 200
      contentAsString(response) must include(respScala.toString())
      contentAsString(response) must include(respFunctional.toString())
      contentAsString(response) must include(respDefense.toString())
      contentAsString(response) must include(respDarkArts.toString())

    }
  }

}
