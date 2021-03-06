package integration

import common.{DBConnection, DBConnectionProvider}
import data.TestDatabaseProvider
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import slick.jdbc.{JdbcBackend, JdbcProfile}

class IntegrationSpec
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
      "slick.dbs.default.driver" -> "slick.driver.PostgresDriver$",
      "slick.dbs.default.db.driver" -> "org.postgresql.Driver",
      "slick.dbs.default.db.url" -> "jdbc:postgresql://localhost:5432/test?user=postgres&password=root"
    ))
    .build

  val dbConn: DBConnection = app.injector.instanceOf(classOf[DBConnection])

  override def profile: JdbcProfile = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get[JdbcProfile].profile
  override def db: JdbcBackend#DatabaseDef = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get[JdbcProfile].db
}
