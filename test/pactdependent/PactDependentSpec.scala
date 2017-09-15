package pactdependent

import com.typesafe.config.ConfigValueFactory
import common.DBConnectionProvider
import data.TestDatabaseProvider
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Application, Configuration}
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import services.PeopleAPIService
import slick.jdbc.{JdbcBackend, JdbcProfile}


/**
  * This is for tests that need pact files in order to run.
  * Before running these kind of tests always run 'sbt pact-test' or 'sbt clean test' before that.
  */
class PactDependentSpec
  extends WordSpec
    with MustMatchers
    with DBConnectionProvider
    with TestDatabaseProvider
    with BeforeAndAfter
    with BeforeAndAfterAll
    with ScalaFutures
    with GuiceOneAppPerSuite {

  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map(
      "people-api.key" -> "awesome-key",
      "people-api.host" -> "http://localhost:1234",
      "people-api.getAllPeoplePath" -> "/someapiname?apiKey=\"${people-api.key}",
      "people-api.getPersonByEmailPath" -> "/someapiname/email?apiKey=\"${people-api.key}"
    ))
    .build

  override def profile: JdbcProfile = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get[JdbcProfile].profile
  override def db: JdbcBackend#DatabaseDef = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get[JdbcProfile].db
}