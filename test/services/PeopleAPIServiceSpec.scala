package services

import com.itv.scalapact.ScalaPactForger.{forgePact, interaction}
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcWSClient
import services.PeopleAPIService.Person

import scala.concurrent.ExecutionContext.Implicits.global

class PeopleAPIServiceSpec  extends UnitSpec {

  implicit val mat: akka.stream.Materializer = app.materializer
  var wsClient: WSClient = _

  val config: Config = app.injector.instanceOf(classOf[Config])
  val email: String = "tanya.moldovan@lunatech.com"
  val getAllPeoplePath: String = config.getString("people-api.getAllPeoplePath")
  val getPersonPath: String= config.getString("people-api.getPersonByEmailPath").replace("$email",email)

  override def  beforeAll { wsClient = AhcWSClient() }

  override def afterAll { wsClient.close() }

  "People Service API" should {
    "return a list of people" in {
      forgePact
        .between("TechMatrix")
        .and("People Service API")
        .addInteraction(
            interaction
            .description("Get a list of people")
            .uponReceiving(getAllPeoplePath)
            .willRespondWith(
              200,
              """[
                  {
                     "email":"severus.snape@hogwarts.com",
                     "name":{"fullName":"Severus Sname","familyName":"Snape","givenName":"Severus"},
                     "thumbnail":"my-very-awesome-thumbnail-link",
                     "managers":["erik.janssen@lunatech.com"],
                     "currentProjects":[],
                     "roles":["developer","development manager", "some-other-awesome-role"],
                     "github":""
                  },
                  {
                     "email":"gandalf@youshallpass.com",
                     "name":{"fullName":"Gandalf YouShallPass","familyName":"YouShallPass","givenName":"Gandalf"},
                     "thumbnail":"my-very-awesome-thumbnail-link",
                     "managers":["erik.janssen@lunatech.com"],
                     "currentProjects":[],
                     "roles":["developer","techmatrix-admin"],
                     "github":""
                  },
                  {
                     "email":"newuser.shouldbeignored@gmail.com",
                     "name":{"fullName":"New User","familyName":"User","givenName":"New"},
                     "thumbnail":"my-very-awesome-thumbnail-link",
                     "managers":["erik.janssen@lunatech.com"],
                     "currentProjects":[],
                     "roles":["developer"],
                     "github":""
                  },
                  {
                      "email":"albus@dumbledore.com",
                      "name":{"fullName":"Albus Percival Wulfric Brian Dumbledore","familyName":"Dumbledore","givenName":"Albus Percival Wulfric Brian"},
                      "thumbnail":"my-very-awesome-thumbnail-link",
                      "managers":["erik.janssen@lunatech.com"],
                      "currentProjects":[],
                      "roles":["techmatrix-admin"],
                      "github":""
                  }
                ]
              """.stripMargin))
        .runConsumerTest { mockConfig =>
          val newConfig = config.withValue("people-api.host", ConfigValueFactory.fromAnyRef(mockConfig.baseUrl))
          val peopleAPIService: PeopleAPIService = new PeopleAPIService(wsClient, newConfig)


          peopleAPIService.getAllPeople.futureValue.map(_.email) mustEqual List("severus.snape@hogwarts.com", "gandalf@youshallpass.com", "newuser.shouldbeignored@gmail.com", "albus@dumbledore.com" )
        }
    }
    "return a person by email" in {
      forgePact
        .between("TechMatrix")
        .and("PeopleServiceAPI")
        .addInteraction(
          interaction
            .description("Get a person by email")
            .uponReceiving(getPersonPath)
            .willRespondWith(200,
            """
              {
                "email":"tanya.moldovan@lunatech.com",
                "name":{"fullName":"Tanya Moldovan","familyName":"Moldovan","givenName":"Tanya"},
                "thumbnail":"my-very-awesome-thumbnail-link",
                "managers":["erik.janssen@lunatech.com"],
                "currentProjects":[],
                "roles":["developer","techmatrix-admin"],
                "github":"tanyamoldovan-lunatech"
              }
            """.stripMargin))
        .runConsumerTest{ mockConfig =>
          val newConfig = config.withValue("people-api.host", ConfigValueFactory.fromAnyRef(mockConfig.baseUrl))

          val peopleAPIService: PeopleAPIService = new PeopleAPIService(wsClient, newConfig)

          peopleAPIService.getPersonByEmail(email).futureValue mustEqual Person("tanya.moldovan@lunatech.com", Seq("erik.janssen@lunatech.com"), Seq("developer", "techmatrix-admin") )
        }
    }
  }

}
