package services

import javax.inject.Inject

import com.typesafe.config.Config
import play.api.http.Status.OK
import play.api.libs.ws.{WSClient, WSRequest}
import models.ImplicitFormats._
import models.Person

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class PeopleAPIService @Inject()(ws: WSClient, config: Config)(implicit val ec: ExecutionContext) {

  import PeopleAPIService._

  val host: String = config.getString("people-api.host")

  def getAllPeople: Future[Seq[Person]] = {
    val path: String = config.getString("people-api.getAllPeoplePath")
    val wsRequest: WSRequest = ws.url(host+path)

    for {
      response <- wsRequest.get()
      _ = if (response.status != OK) sys.error(s"PeopleAPI returned the status ${response.status} and response ${response.body}")
      people: Seq[Person] = wrapOnException(ex => s"The response from PeopleAPI could not be parsed, error is ${ex.getMessage}") {
        response.json.as[Seq[Person]]
      }
    } yield people
  }

  def getPersonByEmail(email: String): Future[Person] = {
    println("in get person by email")
    val path: String = config.getString("people-api.getPersonByEmailPath").replace("email",email)
    println(Console.MAGENTA + path + Console.RESET)
    println(Console.MAGENTA + email + Console.RESET)
    val wsRequest: WSRequest = ws.url(host+path)

    for {
      response <- wsRequest.get()
      _ = if (response.status != OK) sys.error(s"PeopleAPI returned the status ${response.status} and response ${response.body}")
    _ = println(Console.MAGENTA + response.body + Console.RESET)
      person: Person = wrapOnException(ex => s"The response from PeopleAPI could not be parsed, error is ${ex.getMessage}") {
        response.json.as[Person]
      }
    } yield person
  }

}

object PeopleAPIService {

  def wrapOnException[A](msg: Throwable => String)(fn: => A): A = {
    try {
      fn
    } catch {
      case NonFatal(cause) => throw new RuntimeException(msg(cause), cause)
    }
  }
}
