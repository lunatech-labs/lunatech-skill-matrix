package acceptance

import data.TestData._
import data.TestDatabaseProvider
import play.api.libs.json._
import play.api.mvc.{Request, Result}
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future


class SkillMatrixControllerSpec extends AcceptanceSpec {

  var dataMap: Map[String, Int] = _

  override def beforeAll {
    setupDatabase()
  }

  before {
    dataMap = insertSkillData()
  }

  after {
    cleanSkillData()
  }

  override def afterAll {
    dropDatabase()
  }

  info("The POST /users/me/skillmatrix for adding a skill to a user")
  feature("It should return a successful response containing the added skill when everything is fine") {
    scenario("Everything is fine") {

      val request: Request[JsValue] = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response: Future[Result] = route(app, request).get

      status(response) mustEqual 201
      (request, response) must validateAgainstSwagger(swaggerPath)
    }
  }

  feature("It should return an error") {
    scenario("It should return a bad request when tech is missing") {
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithMissingTechJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when skillLevel is missing") {
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithMissingSkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }
    scenario("It should return a bad request when skillLevel is an empty string") {
      val addSkillRequestWithEmptySkillLevelJson = addSkillRequestJson
        .transform(__.json.update((__ \ 'skillLevel).json.put(JsString("")))).get
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithEmptySkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when the provided skillLevel is not part of the values we defined") {
      val addSkillRequestWithNonExistentSkillLevelJson = addSkillRequestJson
        .transform(__.json.update((__ \ 'skillLevel).json.put(JsString("non-existent-value")))).get

      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithNonExistentSkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when techName is missing") {
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithMissingTechNameJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when techType is missing") {
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithMissingTechTypeJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when techType is an empty string") {
      val addSkillRequestWithEmptyTechTypeJson = addSkillRequestJson
        .transform(__.json.update((__ \ 'tech \ 'techType).json.put(JsString("")))).get

      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithEmptyTechTypeJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when the provided techType is not part of the values we defined") {
      val addSkillRequestWithNonExistentTechTypeJson = addSkillRequestJson
        .transform(__.json.update((__ \ 'tech \ 'techType).json.put(JsString("non-existent-value")))).get

      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithNonExistentTechTypeJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a InternalServerError when something goes wrong")(pending)
  }


  info("The PUT /users/me/skillmatrix/:skillId for updating a skill of a user")
  feature("It should return a successful response containing the updated skill when everything is fine") {
    scenario("Everything is fine") {
      val putSkillRequestJsonWithId = putSkillRequestJson
        .transform(__.json.update((__ \ 'tech \ 'id).json.put(JsNumber(dataMap(ID_TECH_SCALA))))).get
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestJsonWithId)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 200
      (request, response) must validateAgainstSwagger(swaggerPath)
    }
  }

  feature("It should return an error4") {
    scenario("It should return a bad request when tech is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingTechJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when skillLevel is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingSkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when skillLevel is an empty string") {
      val putSkillRequestWithEmptySkillLevelJson = putSkillRequestJson
        .transform(__.json.update((__ \ 'skillLevel).json.put(JsString("")))).get
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithEmptySkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when the provided skillLevel is not part of the values we defined") {
      val putSkillRequestWithNonExistentSkillLevelJson = putSkillRequestJson
        .transform(__.json.update((__ \ 'skillLevel).json.put(JsString("non-existent-value")))).get

      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithNonExistentSkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when techName is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingTechNameJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when techType is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingTechTypeJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return a bad request when techId is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingTechIdJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 400
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }

    scenario("It should return NotFound if it can't find the skill for the user") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/5769")
        .withBody(putSkillRequestJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 404
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }
  }

  info("The DELETE /users/me/skillmatrix/:skillId for deleting a skill of a user")
  feature("It should return a successful response with no content") {
    scenario("Everything is fine") {
      val request = FakeRequest("DELETE", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 204
      (request, response) must validateAgainstSwagger(swaggerPath)
    }
  }
  feature("It should return a 404 when skill is not found") {
    scenario("Skill not found") {
      val request = FakeRequest("DELETE", s"/users/me/skillmatrix/1234").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get

      status(response) mustEqual 404
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }
  }

  info("The GET /users/:userId/skills for getting the skills of a user")
  feature("It should return a successful response") {
    ignore("It should return an UserSkillResponse object with the list of the user's skills") {
      val request = FakeRequest("GET", s"/users/me/skillmatrix").withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 200
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }
  }
  feature("it should return an error") {
    scenario("It should return 404 Not Found when the user is not found in the system") {
      val request = FakeRequest("GET", "/users/1090/skills").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get

      status(response) mustEqual 404
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }
  }

  info("The GET /skillmatrix for getting the info about all tech introduced by users")
  feature("It should return a list of SkillMatrixResponse object") {
    scenario("Everything is fine") {
      val request = FakeRequest("GET", "/skillmatrix").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get

      status(response) mustEqual 200
      (request, response) must validateAgainstSwagger(swaggerPath)

    }
  }

  info("The GET /skillmatrix/:techId for getting all info about one tech")
  feature("It should return a successful response") {
    scenario("It should return a SkillMatrixResponse object when everything is fine") {
      val request = FakeRequest("GET", s"/skillmatrix/${dataMap(ID_TECH_DARK_ARTS)}").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get

      status(response) mustEqual 200
      (request, response) must validateAgainstSwagger(swaggerPath)
    }
  }

  feature("It should return an error2") {
    scenario("It should return 404 NotFound when the techId is not present in the database") {
      val request = FakeRequest("GET", "/skillmatrix/76548").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get

      status(response) mustEqual 404
      (request, response) must validateResponseAgainstSwagger(swaggerPath)
    }
  }
}
