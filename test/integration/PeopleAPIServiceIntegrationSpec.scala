package integration

import com.typesafe.config.ConfigFactory
import models.Person
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcWSClient
import services.PeopleAPIService
import models.ImplicitFormats.personFormat

import scala.concurrent.ExecutionContext.Implicits.global

class PeopleAPIServiceIntegrationSpec  extends IntegrationSpec {
  implicit val mat: akka.stream.Materializer = app.materializer
  var wsClient: WSClient = _

  override def  beforeAll { wsClient = AhcWSClient() }

  override def afterAll { wsClient.close() }

  val person: Person = Person(
  "tanya.moldovan@lunatech.com",
      List("erik.janssen@lunatech.com"),
      List("developer", "techmatrix-admin"))

  "People Api Service" should {
    "get a list of people" in {
      val peopleService = new PeopleAPIService(wsClient, ConfigFactory.load())
      val people: Seq[Person] = peopleService.getAllPeople.futureValue

      people.size must be > 1
      people.contains(person)
    }

    "get one person" in {
      val peopleService = new PeopleAPIService(wsClient, ConfigFactory.load())
      peopleService.getPersonByEmail("tanya.moldovan@lunatech.com").futureValue mustEqual person
    }
  }
}
