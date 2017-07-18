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

  info("The PUT /alltech/techId for updating a tech")
  feature("It should return the updated tech") {
    scenario("The tech was found and updated") {
      val request = FakeRequest("PUT", s"/alltech/${dataMap(ID_TECH_DARK_ARTS)}")
        .withBody(updateTechJson)
        .withHeaders(("X-AUTH-TOKEN", authTokenManagement))

      val response = route(app, request).get

      status(response) mustEqual 200
      (request, response) must validateAgainstSwagger(swaggerPath)
    }
  }

  feature("It should return an error when something is wrong"){
    scenario("The new tech name is missing") {
      val request = FakeRequest("PUT", s"/alltech/${dataMap(ID_TECH_DARK_ARTS)}")
        .withBody(updateTechJsonWithMissingName)
        .withHeaders(("X-AUTH-TOKEN", authTokenManagement))

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("The new tech type is missing") {
      val request = FakeRequest("PUT", s"/alltech/${dataMap(ID_TECH_DARK_ARTS)}")
        .withBody(updateTechJsonWithMissingType)
        .withHeaders(("X-AUTH-TOKEN", authTokenManagement))

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("An empty JSON is received") {
      val request = FakeRequest("PUT", s"/alltech/${dataMap(ID_TECH_DARK_ARTS)}")
        .withBody(Json.parse("{}"))
        .withHeaders(("X-AUTH-TOKEN", authTokenManagement))

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("tech id is not found") {
      val request = FakeRequest("PUT", s"/alltech/$nonExistentId")
        .withBody(updateTechJson)
        .withHeaders(("X-AUTH-TOKEN", authTokenManagement))

      val response = route(app, request).get

      status(response) mustEqual 404
      (request, response) must validateAgainstSwagger(swaggerPath)
    }

    scenario("tech name is already in the database") {
      val request = FakeRequest("PUT", s"/alltech/${dataMap(ID_TECH_DARK_ARTS)}")
        .withBody(updateTechDuplicateNameJson)
        .withHeaders(("X-AUTH-TOKEN", authTokenManagement))

      val response = route(app, request).get

      status(response) mustEqual 409
      (request, response) must validateAgainstSwagger(swaggerPath)
    }

    scenario("user is unauthorized for update operation") {
      val request = FakeRequest("PUT", s"/alltech/$nonExistentId")
        .withBody(updateTechJson)
        .withHeaders(("X-AUTH-TOKEN", authToken))

      val response = route(app, request).get

      status(response) mustEqual 403
      (request, response) must validateAgainstSwagger(swaggerPath)

    }
  }

  info("The DELETE /alltech/techId for deleting a tech")
  feature("It should successfuly delete a tech") {
    scenario("techId is found in the db") {
      val request = FakeRequest("DELETE", s"/alltech/${dataMap(ID_TECH_DARK_ARTS)}")
        .withHeaders(("X-AUTH-TOKEN", authTokenAdmin))

      val response = route(app, request).get

      status(response) mustEqual 204
      (request, response) must validateAgainstSwagger(swaggerPath)

    }
  }

  feature("It should return an error for delete operation"){
    scenario("techId is not found"){
      val request = FakeRequest("DELETE", s"/alltech/$nonExistentId")
        .withHeaders(("X-AUTH-TOKEN", authTokenAdmin))

      val response = route(app, request).get

      status(response) mustEqual 404
      (request, response) must validateAgainstSwagger(swaggerPath)
    }

    scenario("user is unauthorized for delete operation") {
      val request = FakeRequest("DELETE", s"/alltech/${dataMap(ID_TECH_DARK_ARTS)}")
        .withHeaders(("X-AUTH-TOKEN", authTokenManagement))

      val response = route(app, request).get

      status(response) mustEqual 403
      (request, response) must validateAgainstSwagger(swaggerPath)

    }
  }

}
