package acceptance

import com.typesafe.config.{Config, ConfigFactory}
import common.DBConnectionProvider
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FeatureSpec, MustMatchers}
import org.scalatestplus.play.OneServerPerSuite
import play.api.db.slick.DatabaseConfigProvider
import services.FakeOauthService
import services.oauth.OauthService
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend

class AcceptanceSpec extends FeatureSpec
  with MustMatchers
  with ScalaFutures
  with BeforeAndAfter
  with BeforeAndAfterAll
  with OneServerPerSuite
  with DBConnectionProvider {

  implicit override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[OauthService].to[FakeOauthService])
    //.overrides(bind[Config].toInstance(ConfigFactory.load()))
    .build

  override def db: JdbcBackend#DatabaseDef = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get[JdbcProfile].db

  implicit lazy val portL = 9000
  lazy val authToken = "xxxx.xxx.xxx"
  val baseUrl: String = "https://localhost:" + portL.toString
}