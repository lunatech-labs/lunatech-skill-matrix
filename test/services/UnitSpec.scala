package services

import common.DBConnectionProvider
import data.TestDatabaseProvider
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.OneAppPerSuite
import play.api.Application
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend


class UnitSpec
    extends WordSpec
    with MustMatchers
    with DBConnectionProvider
    with TestDatabaseProvider
    with BeforeAndAfter
    with BeforeAndAfterAll
    with ScalaFutures
    with OneAppPerSuite {

  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  implicit override lazy val app: Application = new GuiceApplicationBuilder().build

  override def db: JdbcBackend#DatabaseDef = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get[JdbcProfile].db
}
