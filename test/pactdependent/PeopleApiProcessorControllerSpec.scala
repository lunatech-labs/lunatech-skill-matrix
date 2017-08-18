//package pactdependent
//
//import acceptance.AcceptanceSpec
//import play.api.test.FakeRequest
//import play.api.test.Helpers._
//
//class PeopleApiProcessorControllerSpec extends PactDependentSpec {
//
//  override def beforeAll {
//    setupDatabase()
//  }
//
//  before {
//    insertUserData()
//  }
//
//  after {
//    cleanUserData()
//  }
//
//  override def afterAll {
//    dropDatabase()
//  }
//
//  "PeopleApiProcessorController" should {
//    "return an object containing the activated, deactivated and updated users for whom the access levels were changed" in {
//      val request = FakeRequest("POST", "/update-users")
//        .withHeaders(("X-ID-TOKEN", apiToken))
//
//      val response = route(app, request).get
//
//      status(response) mustEqual OK
//    }
//  }
//}
