//TODO: uncomment after switching to embedded postgres. With H2 (the db we are using for acceptance test) it is impossible to test

//package acceptance
//
//import play.api.mvc.{Request, Result}
//import play.api.test.FakeRequest
//import play.api.test.Helpers._
//
//import scala.concurrent.Future
//
//class UserSchedulerAuditControllerSpec extends AcceptanceSpec  {
//
//  override def beforeAll {
//    setupDatabase()
//  }
//
//  before {
//    insertUserData()
//    insertUserAuditData()
//  }
//
//  after {
//    cleanUserData()
//    cleanUserAuditData()
//  }
//
//  override def afterAll {
//    dropDatabase()
//  }
//
//
//  info("The GET /user-audit/latest for getting the info about the last job run")
//  feature("It should return a successful response containing a list of activated users, a list of deactivated users and list of users with updated roles ") {
//    scenario("Everything is fine") {
//
//      val request = FakeRequest("GET", "/user-audit/latest")
//        .withHeaders("X-AUTH-TOKEN" -> authTokenAdmin)
//
//      val response: Future[Result] = route(app, request).get
//
//      println(contentAsString(response))
//
//      status(response) mustEqual 200
//      contentAsString(response).contains("Success") mustEqual true
//    }
//  }
//
//
//}
