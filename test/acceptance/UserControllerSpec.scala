//package acceptance
//
//import play.api.test._
//import play.api.test.Helpers._
//import acceptance.TestData._
//import play.api.libs.json._
//
//
//class UserControllerSpec  extends AcceptanceSpec {
//
//  var dataMap: Map[String, Int] = _
//
//  before {
//    dataMap = TestDatabaseProvider.insertUserData()
//  }
//
//  after {
//    TestDatabaseProvider.cleanUserData()
//  }
//
//  info("The GET /users/4 for getting the info about an user")
//  feature("It should return UserSkillResponse object"){
//    scenario("Everything is fine") {
//      val request = FakeRequest("GET", s"/users/${dataMap(ID_USER_SNAPE)}")
//      val response = route(app, request).get
//
//      val respSeverus = getUserByIdResponse.
//        transform(__.json.update((__ \ 'user \ 'id).json.put(JsNumber(dataMap(ID_USER_SNAPE))))).get
//
//      status(response) mustEqual 200
//      contentAsString(response) must include(respSeverus.toString())
//
//    }
//  }
//
//}
