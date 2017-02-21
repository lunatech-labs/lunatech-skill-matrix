package acceptance

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FeatureSpec, MustMatchers}
import org.scalatestplus.play.OneServerPerSuite
import play.api.test.FakeApplication
import play.api.test.Helpers.inMemoryDatabase


class AcceptanceSpec extends FeatureSpec
  with MustMatchers
  with ScalaFutures
  with BeforeAndAfter
  with BeforeAndAfterAll
  with OneServerPerSuite {

  implicit override lazy val app = FakeApplication(additionalConfiguration = Map(
//    "slick.dbs.test.driver" -> "slick.driver.H2Driver$",
//    "slick.dbs.test.db.driver" -> "org.h2.Driver",
//    "slick.dbs.test.db.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1"
    "slick.dbs.default.db.url" -> "jdbc:postgresql://localhost:5432/test?user=postgres&password=root"
  ))

  implicit lazy val portL = 9000
  val baseUrl = "https://localhost:" + portL.toString
}