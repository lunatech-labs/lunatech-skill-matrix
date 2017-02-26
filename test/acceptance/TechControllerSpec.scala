//package acceptance
//
//import play.api.test._
//import play.api.test.Helpers._
//import acceptance.TestData._
//import play.api.libs.json._
//
//
//class TechControllerSpec  extends AcceptanceSpec {
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
//  info("The GET /alltech for getting the list of tech")
//  feature("It should return a list of Tech object"){
//    scenario("Everything is fine") {
//      val request = FakeRequest("GET", "/alltech")
//      val response = route(app, request).get
//
//      val respScala = allTechScala.
//        transform(__.json.update((__ \ 'id).json.put(JsNumber(dataMap(ID_TECH_SCALA))))).get
//      val respFunctional = allTechFunctional
//        .transform(__.json.update((__ \ 'id).json.put(JsNumber(dataMap(ID_TECH_FUNCTIONAL))))).get
//      val respDefense = allTechDefense
//        .transform(__.json.update((__ \ 'id).json.put(JsNumber(dataMap(ID_TECH_DEFENSE))))).get
//      val respDarkArts = allTechDarkArts
//        .transform(__.json.update((__ \ 'id).json.put(JsNumber(dataMap(ID_TECH_DARK_ARTS))))).get
//
//      status(response) mustEqual 200
//      contentAsString(response) must include(respScala.toString())
//      contentAsString(response) must include(respFunctional.toString())
//      contentAsString(response) must include(respDefense.toString())
//      contentAsString(response) must include(respDarkArts.toString())
//
//    }
//  }
//
//}
