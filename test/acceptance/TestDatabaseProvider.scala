package acceptance

import models.MyTable
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


object TestDatabaseProvider  {

  implicit val app = play.api.Play.current


  val dbConfig =  DatabaseConfigProvider.get[JdbcProfile]
  val skillTable = TableQuery[MyTable.Skills]
  val techTable = TableQuery[MyTable.Tech]
  val userTable = TableQuery[MyTable.Users]


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

  }

  def insertUserData(): Map[String, Int] = {
    val userOdersky = User(None, "Martin", "Odersky", "martin.odersky@scala.com")
    val userSeverus = User(None, "Severus", "Snape", "severus.snape@hogwarts.com")

    val idUserOdersky: Int = Await.result(dbConfig.db.run(userTable returning userTable.map(_.id) += userOdersky), Duration.Inf)
    val idUserSeverus: Int = Await.result(dbConfig.db.run(userTable returning userTable.map(_.id) += userSeverus), Duration.Inf)
    Map(ID_USER_ODERSKY -> idUserOdersky, ID_USER_SNAPE -> idUserSeverus)

  }

  def insertTechData(): Map[String, Int] = {
    val techScala = Tech(None, "Scala", TechType.LANGUAGE)
    val techFunctional = Tech(None, "Functional Programming", TechType.CONCEPTUAL)
    val techDefense = Tech(None, "Defense against the Dark Arts", TechType.CONCEPTUAL)
    val techDarkArts= Tech(None, "Dark Arts", TechType.CONCEPTUAL)

    val idTechScala: Int = Await.result(dbConfig.db.run(techTable returning techTable.map(_.id) += techScala), Duration.Inf)
    val idTechFunctional: Int = Await.result(dbConfig.db.run(techTable returning techTable.map(_.id)  += techFunctional), Duration.Inf)
    val idTechDefense: Int = Await.result(dbConfig.db.run(techTable returning techTable.map(_.id)  += techDefense), Duration.Inf)
    val idTechDarkArts: Int = Await.result(dbConfig.db.run(techTable returning techTable.map(_.id)  += techDarkArts), Duration.Inf)

    Map(ID_TECH_SCALA -> idTechScala, ID_TECH_FUNCTIONAL -> idTechFunctional, ID_TECH_DEFENSE -> idTechDefense, ID_TECH_DARK_ARTS -> idTechDarkArts)
  }

  def insertSkillData(): Map[String, Int] = {
    val userMap = insertUserData()
    val techMap = insertTechData()

    val skillOderskyScala = Skill(None, userMap(ID_USER_ODERSKY), techMap(ID_TECH_SCALA), SkillLevel.CAN_TEACH)
    val skillOderskyFunctional = Skill(None, userMap(ID_USER_ODERSKY), techMap(ID_TECH_FUNCTIONAL), SkillLevel.CAN_TEACH)
    val skillSeverusDefense = Skill(None, userMap(ID_USER_SNAPE), techMap(ID_TECH_DEFENSE), SkillLevel.CAN_TEACH)
    val skillSeverusDarkArts = Skill(None, userMap(ID_USER_SNAPE), techMap(ID_TECH_DARK_ARTS), SkillLevel.CAN_TEACH)

    val idSkillOderskyScala: Int = Await.result(dbConfig.db.run(skillTable returning skillTable.map(_.id) += skillOderskyScala), Duration.Inf)
    Await.result(dbConfig.db.run(skillTable += skillOderskyFunctional), Duration.Inf)
    val idSkillSeverusDefense: Int = Await.result(dbConfig.db.run(skillTable returning skillTable.map(_.id) += skillSeverusDefense), Duration.Inf)
    Await.result(dbConfig.db.run(skillTable += skillSeverusDarkArts), Duration.Inf)

    userMap ++ techMap ++ Map(
      SKILL_ODERSKY_SCALA -> idSkillOderskyScala,
      SKILL_SEVERUS_DEFENSE ->  idSkillSeverusDefense
    )
  }

  def cleanUserData() = {
    Await.result(dbConfig.db.run(skillTable.delete), Duration.Inf)
  }

  def cleanTechData() = {
    Await.result(dbConfig.db.run(techTable.delete), Duration.Inf)
  }

  def cleanSkillData() = {
    cleanUserData()
    cleanTechData()
    Await.result(dbConfig.db.run(userTable.delete), Duration.Inf)
  }

  def dropDatabase() = {
   // import play.api.db.evolutions._
   // Evolutions.cleanupEvolutions(database)

  }

}
