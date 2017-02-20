package acceptance

import play.api.test._
import play.api.test.Helpers._
import acceptance.TestData._
import play.api.libs.json._

class SkillMatrixControllerSpec  extends AcceptanceSpec {

  var idUserTanya: Int = _
  var idUserSeverus: Int = _
  var idTechScala: Int = _
  var idDefense: Int = _
  var idSkillTanyaScala: Int = _
  var idTechFunctional: Int = _
  var idTechDarkArts: Int = _
  var idSkillSeverusDefense: Int = _

  override def beforeAll  {
    TestDatabaseProvider.setupDatabase()
  }

  before {
    val (idUserTanya1, idUserSeverus1, idTechScala1, idDefense1, idSkillTanyaScala1, idTechFunctional1, idSkillSeverusDefense1, idTechDarkArts1) =
      TestDatabaseProvider.insertTestData()
    idUserTanya = idUserTanya1
    idUserSeverus = idUserSeverus1
    idTechScala = idTechScala1
    idDefense = idDefense1
    idSkillTanyaScala = idSkillTanyaScala1
    idTechFunctional = idTechFunctional1
    idSkillSeverusDefense = idSkillSeverusDefense1
    idTechDarkArts = idTechDarkArts1
  }

  after {
    TestDatabaseProvider.cleanTestData()
  }

 override def afterAll {
   TestDatabaseProvider.dropDatabase()
  }

  info("The POST /users/:userId/skills for adding a skill to a user")
  feature("It should return a successful response containing the added skill when everything is fine") {
    scenario("Everything is fine") {
      val request = FakeRequest("POST", s"/users/$idUserTanya/skills").withBody(addSkillRequestJson)
      val response = route(app, request).get
      status(response) mustEqual 201

      //The return json will contain the id of the new added skill.
      // I do not know how to test the whole response, so I will be testing parts of it

      /*contentAsString(response) must include (""""skillLevel": "DABBED"""")
      contentAsString(response) must include (""""techType": "LANGUAGE"""")
      contentAsString(response) must include (""""name": "Brainfuck"""")
      contentAsString(response) must include ("tech")
      contentAsString(response) must include ("id")*/

      contentAsString(response) must include ("skillLevel")
      contentAsString(response) must include ("DABBED")
      contentAsString(response) must include ("techType")
      contentAsString(response) must include ("LANGUAGE")
      contentAsString(response) must include ("name")
      contentAsString(response) must include ("Brainfuck")
      contentAsString(response) must include ("tech")
      contentAsString(response) must include ("id")
    }
  }

  feature("It should return an error") {
    scenario("It should return a bad request when tech is missing") {
      val request = FakeRequest("POST", s"/users/$idUserTanya/skills").withBody(addSkillRequestWithMissingTechJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechMissing)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return a bad request when skillLevel is missing"){
      val request = FakeRequest("POST", s"/users/$idUserTanya/skills").withBody(addSkillRequestWithMissingSkillLevelJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objSkillLevelMissing)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return a bad request when techName is missing"){
      val request = FakeRequest("POST", s"/users/$idUserTanya/skills").withBody(addSkillRequestWithMissingTechNameJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechNameMissing)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return a bad request when techType is missing") {
      val request = FakeRequest("POST", s"/users/$idUserTanya/skills").withBody(addSkillRequestWithMissingTechTypeJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechTypeMissing)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return a InternalServerError when something goes wrong") (pending)
  }


  info("The PUT /users/:userId/skill/:skillId for updating a skill of a user")
  feature("It should return a successful response containing the updated skill when everything is fine"){
    scenario("Everything is fine") {
      val putSkillRequestJsonWithId = putSkillRequestJson
        .transform(__.json.update((__ \ 'tech \ 'id).json.put(JsNumber(idTechScala)))).get
      val request = FakeRequest("PUT", s"/users/$idUserTanya/skill/$idSkillTanyaScala")
        .withBody(putSkillRequestJsonWithId)
      val response = route(app, request).get
      status(response) mustEqual 200

      //The return json will contain the id of the new added skill.
      // I do not know how to test the whole response, so I will be testing parts of it
      /*contentAsString(response) must include(""""skillLevel": "DABBED"""")
      contentAsString(response) must include(""""techType": "LANGUAGE"""")
      contentAsString(response) must include(""""name": "Scala"""")
      contentAsString(response) must include(s"""""id": ${idTechScala.toString}""")*/

      contentAsString(response) must include ("skillLevel")
      contentAsString(response) must include ("DABBED")
      contentAsString(response) must include ("techType")
      contentAsString(response) must include ("LANGUAGE")
      contentAsString(response) must include ("name")
      contentAsString(response) must include ("Scala")
      contentAsString(response) must include ("tech")
      contentAsString(response) must include ("id")
      contentAsString(response) must include (idTechScala.toString)
    }
  }

  feature("It should return an error4") {
    scenario("It should return a bad request when tech is missing") {
      val request = FakeRequest("PUT", s"/users/$idUserTanya/skill/$idSkillTanyaScala")
        .withBody(putSkillRequestWithMissingTechJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechMissing)
      contentAsString(response) must include(genericPathMissing)

    }
    scenario("It should return a bad request when skillLevel is missing") {
      val request = FakeRequest("PUT", s"/users/$idUserTanya/skill/$idSkillTanyaScala")
        .withBody(putSkillRequestWithMissingSkillLevelJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objSkillLevelMissing)
      contentAsString(response) must include(genericPathMissing)

    }
    scenario("It should return a bad request when techName is missing") {
      val request = FakeRequest("PUT", s"/users/$idUserTanya/skill/$idSkillTanyaScala")
        .withBody(putSkillRequestWithMissingTechNameJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechNameMissing)
      contentAsString(response) must include(genericPathMissing)

    }
    scenario("It should return a bad request when techType is missing") {
      val request = FakeRequest("PUT", s"/users/$idUserTanya/skill/$idSkillTanyaScala")
        .withBody(putSkillRequestWithMissingTechTypeJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechTypeMissing)
      contentAsString(response) must include(genericPathMissing)
    }

    // THIS TEST WILL FAIL because the presence of the id is not validated
    scenario("It should return a bad request when techId is missing") {
      val request = FakeRequest("PUT", s"/users/$idUserTanya/skill/$idSkillTanyaScala")
        .withBody(putSkillRequestWithMissingTechIdJson)
      val response = route(app, request).get
      status(response) mustEqual 400
      contentAsString(response) must include(objTechIdMissing)
      contentAsString(response) must include(genericPathMissing)
    }
    scenario("It should return NotFound if it can't find the skill for the user") {
      val request = FakeRequest("PUT", s"/users/$idUserTanya/skill/5769")
        .withBody(putSkillRequestJson)
      val response = route(app, request).get
      status(response) mustEqual 404
      contentAsString(response) must include("skill could not be found")
    }
  }

  info("The DELETE /users/:userId/skill/:skillId for deleting a skill of a user")
  feature("It should return a successful response with no content") {
    scenario("Everything is fine") {
      val request = FakeRequest("DELETE", s"/users/$idUserTanya/skill/$idSkillTanyaScala")
      val response = route(app, request).get
      status(response) mustEqual 204
    }
  }
  feature("It should return a 404 when skill is not found"){
    scenario("Skill not found") {
      val request = FakeRequest("DELETE", s"/users/$idUserTanya/skill/1234")
      val response = route(app, request).get
      status(response) mustEqual 404
      contentAsString(response) must include(skillNotFound)
    }
  }

  info("The GET /users/:userId/skills for getting the skills of a user")
  feature("It should return a successful response") {
    scenario("It should return an UserSkillResponse object with the list of the user's skills") {
      val request = FakeRequest("GET", s"/users/$idUserSeverus/skills")
      val response = route(app, request).get
      status(response) mustEqual 200
      contentAsString(response) must include(techDefense.name)
    }
  }
  feature("it should return an error"){
    scenario("It should return 404 Not Found when the user is not found in the system") {
      val request = FakeRequest("GET", "/users/1090/skills")
      val response = route(app, request).get
      status(response) mustEqual 404
      contentAsString(response) must include(errorUserNotFound)
    }
  }


  info("The GET /skillmatrix for getting the info about all tech introduced by users")
  feature("It should return a list of SkillMatrixResponse object"){
    scenario("Everything is fine") {
      val request = FakeRequest("GET", "/skillmatrix")
      val response = route(app, request).get

      val respScala = skillMatrixResultTechScala
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(idTechScala)))).get
      val respFunctional = skillMatrixResultTechFunctional
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(idTechFunctional)))).get
      val respDefense = skillMatrixResultTechDefense
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(idDefense)))).get
      val respDarkArts = skillMatrixResultTechDarkArts
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(idTechDarkArts)))).get

      status(response) mustEqual 200
      contentAsString(response) must include(respScala.toString())
      contentAsString(response) must include(respFunctional.toString())
      contentAsString(response) must include(respDefense.toString())
      contentAsString(response) must include(respDarkArts.toString())

    }
  }


  info("The GET /skillmatrix/:techId for getting all info about one tech")
  feature("It should return a successful response"){
    scenario("It should return a SkillMatrixResponse object when everything is fine") {
      val request = FakeRequest("GET", s"/skillmatrix/$idTechDarkArts")
      val response = route(app, request).get
      val responseWithCorrectId = getSkillMatrixByTechIdResponseJson
        .transform(__.json.update((__ \ 'skills \ 'techId).json.put(JsNumber(idTechDarkArts)))).get
      status(response) mustEqual 200
      contentAsJson(response) mustEqual responseWithCorrectId
    }
  }
  // The test will fail because the controller is missing the functionality
  feature("It should return an error2"){
    scenario("It should return 404 NotFound when the techId is not present in the database") {
      val request = FakeRequest("GET", "/skillmatrix/76548")
      val response = route(app, request).get
      status(response) mustEqual 404
      contentAsString(response) must include("not found")
    }
  }



  //// THESE TWO TESTS SHOULD BE IN OTHER FILES. AFTER THE DATABASE IS CORRECTLY CONFIGURED, IT WILL BE MOVED TO THE CORRECT FILE

  info("The GET /alltech for getting the list of tech")
  feature("It should return a list some object"){ //will update later
    scenario("Everything is fine") {
      val request = FakeRequest("GET", "/alltech")
      val response = route(app, request).get

      val respScala = allTechScala.
        transform(__.json.update((__ \ 'id).json.put(JsNumber(idTechScala)))).get
      val respFunctional = allTechFunctional
        .transform(__.json.update((__ \ 'id).json.put(JsNumber(idTechFunctional)))).get
      val respDefense = allTechDefense
        .transform(__.json.update((__ \ 'id).json.put(JsNumber(idDefense)))).get
      val respDarkArts = allTechDarkArts
        .transform(__.json.update((__ \ 'id).json.put(JsNumber(idTechDarkArts)))).get

      status(response) mustEqual 200
      contentAsString(response) must include(respScala.toString())
      contentAsString(response) must include(respFunctional.toString())
      contentAsString(response) must include(respDefense.toString())
      contentAsString(response) must include(respDarkArts.toString())

    }
  }


  info("The GET /users/4 for getting the info about an user")
  feature("It should return some object"){ //will update later
    scenario("Everything is fine") {
      val request = FakeRequest("GET", s"/users/$idUserSeverus")
      val response = route(app, request).get

      val respSeverus = getUserByIdResponse.
        transform(__.json.update((__ \ 'user \ 'id).json.put(JsNumber(idUserSeverus)))).get

      status(response) mustEqual 200
      contentAsString(response) must include(respSeverus.toString())

    }
  }

}
