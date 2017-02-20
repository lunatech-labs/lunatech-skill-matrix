/*package acceptance

import play.api.test._
import play.api.test.Helpers._
import acceptance.TestData._
import play.api.libs.json._


class UserControllerSpec  extends AcceptanceSpec {

  var idSeverus: Int = _

  override def beforeAll  {
    TestDatabaseProvider.setupDatabase()
  }

  before {
    val (_, idSeverus1, _, _, _, _, _, _) =
      TestDatabaseProvider.insertTestData()
    idSeverus = idSeverus1
  }

  after {
    TestDatabaseProvider.cleanTestData()
  }

  override def afterAll {
    TestDatabaseProvider.dropDatabase()
  }


  info("The GET /users/4 for getting the info about an user")
  feature("It should return some object"){ //will update later
    scenario("Everything is fine") {
      val request = FakeRequest("GET", s"/users/idSeverus")
      val response = route(app, request).get

      val respSeverus = getUserByIdResponse.
        transform(__.json.update((__ \ 'user \ 'id).json.put(JsNumber(idSeverus)))).get

      status(response) mustEqual 200
      contentAsString(response) mustEqual respSeverus

    }
  }

}
*/