package pactdependent

import com.itv.scalapactcore.Pact
import com.itv.scalapactcore.common.CommandArguments.parseArguments
import com.itv.scalapactcore.common.LocalPactFileLoader._
import com.itv.scalapactcore.common.{Arguments, ConfigAndPacts}
import com.itv.scalapactcore.stubber.InteractionManager
import com.itv.scalapactcore.stubber.PactStubService.{runServer, stopServer}
import com.typesafe.scalalogging.LazyLogging
import org.http4s.server.Server

class Stubber() extends  LazyLogging {
  var server: Option[Server] = None

  def startStubber(): Unit = {
    logger.info("Starting Stubber")
    val args = Seq.empty[String]
    val interactionManager: InteractionManager = new InteractionManager
    server = Some((parseArguments andThen loadAllPactFiles(Seq("target/pacts")) andThen interactionManager.addToInteractionManager andThen runServer(interactionManager)(10))(args))
  }

  def stopStubber(): Unit = {
    server.map { s =>
      stopServer(s)
      server = None
    } getOrElse {
      logger.warn("Stubber not running, so not stopping!")
    }
  }


  val loadAllPactFiles: Seq[String] => (Arguments) => ConfigAndPacts = paths => args =>
    paths.foldLeft[ConfigAndPacts](ConfigAndPacts(args, List.empty[Pact])) {
      case (configAndPacts, path) =>
        configAndPacts.copy(pacts = configAndPacts.pacts ++ loadPactFiles(path)(args).pacts)
    }
}

object Stubber {
  def apply(): Stubber = new Stubber()
}