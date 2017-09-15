package acceptance

import common.DBConnectionProvider
import data.TestDatabaseProvider
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FeatureSpec, MustMatchers}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import services.{FakeOauthService, PeopleAPIService}
import services.oauth.OauthService
import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcBackend

class AcceptanceSpec extends FeatureSpec
  with MustMatchers
  with ScalaFutures
  with BeforeAndAfter
  with BeforeAndAfterAll
  with GuiceOneAppPerSuite
  with DBConnectionProvider
  with SwaggerValidator
  with TestDatabaseProvider {

  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map(
      "people-api.key" -> "awesome-key",
      "people-api.host" -> "http://localhost:1234",
      "people-api.getAllPeoplePath" -> "/someapiname?apiKey=\"${people-api.key}",
      "people-api.getPersonByEmailPath" -> "/someapiname/email?apiKey=\"${people-api.key}"
    ))
    .overrides(bind[OauthService].to[FakeOauthService])
    .build

  override def profile: JdbcProfile = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get[JdbcProfile].profile
  override def db: JdbcBackend#DatabaseDef = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get[JdbcProfile].db

  implicit lazy val portL: Int = 9000
  lazy val authToken = "basic-xxx-xxx"
  lazy val apiToken = "api-secret-test"
  lazy val authTokenManagement = "management-xxx-xx"
  lazy val authTokenAdmin = "admin-xxx-xx"
  val baseUrl: String = "https://localhost:" + portL.toString
  val swaggerPath: String = "resources/swagger.json"
}