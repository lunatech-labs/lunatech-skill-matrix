/*package acceptance

import play.api.test._
import play.api.test.Helpers._
import acceptance.TestData._
import play.api.libs.json._


class TechControllerSpec  extends AcceptanceSpec {


  var idTechScala: Int = _
  var idTechDefense: Int = _
  var idTechFunctional: Int = _
  var idTechDarkArts: Int = _

  override def beforeAll  {
    TestDatabaseProvider.setupDatabase()
  }

  before {
    val (_, _, idTechScala1, idDefense1, _, idTechFunctional1, _, idTechDarkArts1) =
      TestDatabaseProvider.insertTestData()
    idTechScala = idTechScala1
    idTechDefense = idDefense1
    idTechFunctional = idTechFunctional1
    idTechDarkArts = idTechDarkArts1
  }

  after {
    TestDatabaseProvider.cleanTestData()
  }

  override def afterAll {
    TestDatabaseProvider.dropDatabase()
  }


  info("The GET /alltech for getting the list of tech")
  feature("It should return a list some object"){ //will update later
    scenario("Everything is fine") {
      val request = FakeRequest("GET", "/alltech")
      val response = route(app, request).get

      val respScala = allTechScala.
        transform(__.json.update((__ \ 'techId).json.put(JsNumber(idTechScala)))).get
      val respFunctional = allTechFunctional
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(idTechFunctional)))).get
      val respDefense = allTechDefense
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(idTechDefense)))).get
      val respDarkArts = allTechDarkArts
        .transform(__.json.update((__ \ 'techId).json.put(JsNumber(idTechDarkArts)))).get

      status(response) mustEqual 200
      contentAsString(response) must include(respScala.toString())
      contentAsString(response) must include(respFunctional.toString())
      contentAsString(response) must include(respDefense.toString())
      contentAsString(response) must include(respDarkArts.toString())

    }
  }

}
*/