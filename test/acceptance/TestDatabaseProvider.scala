package acceptance

import slick.driver.JdbcProfile
import javax.inject.Inject

import models._
import models.responses.{SkillMatrixResponse, SkillMatrixUsersAndLevel, UserSkillResponse}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration.Duration
import acceptance.TestData._
import play.libs.F.Tuple


object TestDatabaseProvider {

  implicit val app = play.api.Play.current


  val dbConfig = DatabaseConfigProvider.get[JdbcProfile]

  val skillTable = TableQuery[Skills]
  val techTable = TableQuery[Techs]
  val userTable = TableQuery[Users]


  /*val database = Databases(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://localhost:5432/test"
  )*/


  /*val database = Databases.inMemory("test"urlOptions = Map(
    "DB_CLOSE_DELAY" -> "-1",
    "MODE" -> "PostgreSQL",
    "DATABASE_TO_UPPER" -> "false"
  ))*/


  // val database  = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")

  def setupDatabase() = {
    // println("creating database")
    // import play.api.db.evolutions._
    // Evolutions.applyEvolutions(database)
    val setup = DBIO.seq((skillTable.schema ++ techTable.schema ++ userTable.schema).create)
    dbConfig.db.run(setup)
  }

  def insertTestData(): (Int, Int, Int, Int, Int, Int, Int, Int) = {
    val idUserTanya: Int = Await.result(dbConfig.db.run(userTable returning userTable.map(_.id) += userTanya), Duration.Inf)
    val idUserSeverus: Int = Await.result(dbConfig.db.run(userTable returning userTable.map(_.id) += userSeverus), Duration.Inf)

    val idTechScala: Int = Await.result(dbConfig.db.run(techTable returning techTable.map(_.id) += techScala), Duration.Inf)
    val idTechFunctional: Int = Await.result(dbConfig.db.run(techTable returning techTable.map(_.id) += techFunctional), Duration.Inf)
    val idTechDefense: Int = Await.result(dbConfig.db.run(techTable returning techTable.map(_.id) += techDefense), Duration.Inf)
    val idTechDarkArts: Int = Await.result(dbConfig.db.run(techTable returning techTable.map(_.id) += techDarkArts), Duration.Inf)


    val skillTanyaScala = Skill(None, idUserTanya, idTechScala, SkillLevel.COMFORTABLE)
    val skillTanyaFunctional = Skill(None, idUserTanya, idTechFunctional, SkillLevel.COMFORTABLE)
    val skillSeverusDefense = Skill(None, idUserSeverus, idTechDefense, SkillLevel.CAN_TEACH)
    val skillSeverusDarkArts = Skill(None, idUserSeverus, idTechDarkArts, SkillLevel.CAN_TEACH)

    val idSkillTanyaScala: Int = Await.result(dbConfig.db.run(skillTable returning skillTable.map(_.id) += skillTanyaScala), Duration.Inf)
    Await.result(dbConfig.db.run(skillTable += skillTanyaFunctional), Duration.Inf)
    val idSkillSeverusDefense: Int = Await.result(dbConfig.db.run(skillTable returning skillTable.map(_.id) += skillSeverusDefense), Duration.Inf)
    Await.result(dbConfig.db.run(skillTable += skillSeverusDarkArts), Duration.Inf)

    (idUserTanya, idUserSeverus, idTechScala, idTechDefense, idSkillTanyaScala, idTechFunctional, idSkillSeverusDefense, idTechDarkArts)

  }

  def cleanTestData() = {
    Await.result(dbConfig.db.run(skillTable.delete), Duration.Inf)
    Await.result(dbConfig.db.run(techTable.delete), Duration.Inf)
    Await.result(dbConfig.db.run(userTable.delete), Duration.Inf)
  }

  def dropDatabase() = {
    // import play.api.db.evolutions._
    // Evolutions.cleanupEvolutions(database)
    val setup = DBIO.seq((skillTable.schema ++ techTable.schema ++ userTable.schema).drop)
    dbConfig.db.run(setup)
  }

}
