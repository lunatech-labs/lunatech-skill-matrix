package acceptance

import play.api.test._
import play.api.test.Helpers._
import data.TestData._
import data.TestDatabaseProvider
import play.api.libs.json._
import play.test.WithApplication


class SkillMatrixControllerSpec extends AcceptanceSpec with TestDatabaseProvider {

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

      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get

      status(response) mustEqual 201

      //The return json will contain the id of the new added skill.
      // I do not know how to test the whole response, so I will be testing parts of it

      contentAsString(response) must include("skillLevel")
      contentAsString(response) must include("NOVICE")
      contentAsString(response) must include("techType")
      contentAsString(response) must include("LANGUAGE")
      contentAsString(response) must include("name")
      contentAsString(response) must include("brainfuck")
      contentAsString(response) must include("tech")
      contentAsString(response) must include("id")
    }
  }

  feature("It should return an error") {
    scenario("It should return a bad request when tech is missing") {
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithMissingTechJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechMissing)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return a bad request when skillLevel is missing") {
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithMissingSkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objSkillLevelWrong)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return a bad request when skillLevel is an empty string") {
      val addSkillRequestWithEmptySkillLevelJson = addSkillRequestJson
        .transform(__.json.update((__ \ 'skillLevel).json.put(JsString("")))).get
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithEmptySkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objSkillLevelWrong)
      contentAsString(response) must include(genericEnumValueWrong)
    }
    scenario("It should return a bad request when the provided skillLevel is not part of the values we defined") {
      val addSkillRequestWithNonExistentSkillLevelJson = addSkillRequestJson
        .transform(__.json.update((__ \ 'skillLevel).json.put(JsString("non-existent-value")))).get
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithNonExistentSkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objSkillLevelWrong)
      contentAsString(response) must include(genericEnumValueWrong)
    }
    scenario("It should return a bad request when techName is missing") {
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithMissingTechNameJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechNameMissing)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return a bad request when techType is missing") {
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithMissingTechTypeJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechTypeWrong)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return a bad request when techType is an empty string") {
      val addSkillRequestWithEmptyTechTypeJson = addSkillRequestJson
        .transform(__.json.update((__ \ 'tech \ 'techType).json.put(JsString("")))).get
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithEmptyTechTypeJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechTypeWrong)
      contentAsString(response) must include(genericEnumValueWrong)
    }
    scenario("It should return a bad request when the provided techType is not part of the values we defined") {
      val addSkillRequestWithNonExistentTechTypeJson = addSkillRequestJson
        .transform(__.json.update((__ \ 'tech \ 'techType).json.put(JsString("non-existent-value")))).get
      val request = FakeRequest("POST", s"/users/me/skillmatrix")
        .withBody(addSkillRequestWithNonExistentTechTypeJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechTypeWrong)
      contentAsString(response) must include(genericEnumValueWrong)
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

      //The return json will contain the id of the new added skill.
      // I do not know how to test the whole response, so I will be testing parts of it

      contentAsString(response) must include("skillLevel")
      contentAsString(response) must include("NOVICE")
      contentAsString(response) must include("techType")
      contentAsString(response) must include("LANGUAGE")
      contentAsString(response) must include("name")
      contentAsString(response) must include("scala")
      contentAsString(response) must include("tech")
      contentAsString(response) must include("id")
      contentAsString(response) must include(dataMap(ID_TECH_SCALA).toString)
    }
  }

  feature("It should return an error4") {
    scenario("It should return a bad request when tech is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingTechJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechMissing)
      contentAsString(response) must include(genericPathMissing)

    }
    scenario("It should return a bad request when skillLevel is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingSkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objSkillLevelWrong)
      contentAsString(response) must include(genericPathMissing)

    }
    scenario("It should return a bad request when skillLevel is an empty string") {
      val putSkillRequestWithEmptySkillLevelJson = putSkillRequestJson
        .transform(__.json.update((__ \ 'skillLevel).json.put(JsString("")))).get
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithEmptySkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objSkillLevelWrong)
      contentAsString(response) must include(genericEnumValueWrong)

    }
    scenario("It should return a bad request when the provided skillLevel is not part of the values we defined") {
      val putSkillRequestWithNonExistentSkillLevelJson = putSkillRequestJson
        .transform(__.json.update((__ \ 'skillLevel).json.put(JsString("non-existent-value")))).get
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithNonExistentSkillLevelJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objSkillLevelWrong)
      contentAsString(response) must include(genericEnumValueWrong)

    }
    scenario("It should return a bad request when techName is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingTechNameJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechNameMissing)
      contentAsString(response) must include(genericPathMissing)

    }
    scenario("It should return a bad request when techType is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingTechTypeJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechTypeWrong)
      contentAsString(response) must include(genericPathMissing)
    }

    // THIS TEST WILL FAIL because the presence of the id is not validated
    scenario("It should return a bad request when techId is missing") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withBody(putSkillRequestWithMissingTechIdJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechIdMissing)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return NotFound if it can't find the skill for the user") {
      val request = FakeRequest("PUT", s"/users/me/skillmatrix/5769")
        .withBody(putSkillRequestJson)
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 404
      contentAsString(response) must include("skill could not be found")
    }
  }

  info("The DELETE /users/me/skillmatrix/:skillId for deleting a skill of a user")
  feature("It should return a successful response with no content") {
    scenario("Everything is fine") {
      val request = FakeRequest("DELETE", s"/users/me/skillmatrix/${dataMap(SKILL_ODERSKY_SCALA)}")
        .withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 204
    }
  }
  feature("It should return a 404 when skill is not found") {
    scenario("Skill not found") {
      val request = FakeRequest("DELETE", s"/users/me/skillmatrix/1234").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get
      status(response) mustEqual 404
      contentAsString(response) must include(skillNotFound)
    }
  }

  info("The GET /users/:userId/skills for getting the skills of a user")
  //TODO check if we still need this scenario
  feature("It should return a successful response") {
    ignore("It should return an UserSkillResponse object with the list of the user's skills") {
      val request = FakeRequest("GET", s"/users/me/skillmatrix").withHeaders("X-AUTH-TOKEN" -> authToken)

      val response = route(app, request).get
      status(response) mustEqual 200
      contentAsString(response) must include("defense against the dark arts")
    }
  }
  feature("it should return an error") {
    scenario("It should return 404 Not Found when the user is not found in the system") {
      val request = FakeRequest("GET", "/users/1090/skills").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get
      status(response) mustEqual 404
      contentAsString(response) must include(errorUserNotFound)
    }
  }

  info("The GET /skillmatrix for getting the info about all tech introduced by users")
  feature("It should return a list of SkillMatrixResponse object") {
    scenario("Everything is fine") {
      val request = FakeRequest("GET", "/skillmatrix").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get

      val respScala = skillMatrixResultTechScala
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(dataMap(ID_TECH_SCALA))))).get
      val respFunctional = skillMatrixResultTechFunctional
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(dataMap(ID_TECH_FUNCTIONAL))))).get
      val respDefense = skillMatrixResultTechDefense
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(dataMap(ID_TECH_DEFENSE))))).get
      val respDarkArts = skillMatrixResultTechDarkArts
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(dataMap(ID_TECH_DARK_ARTS))))).get

      status(response) mustEqual 200
      contentAsString(response) must include(respScala.toString())
      contentAsString(response) must include(respFunctional.toString())
      contentAsString(response) must include(respDefense.toString())
      contentAsString(response) must include(respDarkArts.toString())

    }
  }

  info("The GET /skillmatrix/:techId for getting all info about one tech")
  feature("It should return a successful response") {
    scenario("It should return a SkillMatrixResponse object when everything is fine") {
      val request = FakeRequest("GET", s"/skillmatrix/${dataMap(ID_TECH_DARK_ARTS)}").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get
      val responseWithCorrectId = getSkillMatrixByTechIdResponseJson
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(dataMap(ID_TECH_DARK_ARTS))))).get
      status(response) mustEqual 200
      contentAsJson(response) mustEqual responseWithCorrectId
    }
  }

  feature("It should return an error2") {
    scenario("It should return 404 NotFound when the techId is not present in the database") {
      val request = FakeRequest("GET", "/skillmatrix/76548").withHeaders("X-AUTH-TOKEN" -> authToken)
      val response = route(app, request).get
      status(response) mustEqual 404
      contentAsString(response) must include("Tech not found")
    }
  }
}
