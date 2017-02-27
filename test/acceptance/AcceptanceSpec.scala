package acceptance

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FeatureSpec, MustMatchers}
import org.scalatestplus.play.OneServerPerSuite
import play.api.test.FakeApplication


class AcceptanceSpec extends FeatureSpec
  with MustMatchers
  with ScalaFutures
  with BeforeAndAfter
  with BeforeAndAfterAll
  with OneServerPerSuite {

  implicit override lazy val app = FakeApplication(additionalConfiguration = Map(
    //"slick.dbs.default.driver" -> "slick.driver.H2Driver$",
    //"slick.dbs.default.db.driver" -> "org.h2.Driver",
    //"slick.dbs.default.db.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;INIT=runscript from 'test/resources/create_schema.sql'"
    "slick.dbs.default.db.url" -> "jdbc:postgresql://localhost:5432/test?user=postgres&password=root"
  ))

  implicit lazy val portL = 9000
  val baseUrl: String = "https://localhost:" + portL.toString
}