package data

import common.DBConnectionProvider
import models._
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery
import data.TestData._
import models.db.{Entries, Skills, Techs, Users}

import scala.concurrent._
import scala.concurrent.duration.Duration


trait TestDatabaseProvider {
  self: DBConnectionProvider =>

  val skillTable: TableQuery[Skills] = TableQuery[Skills]
  val techTable: TableQuery[Techs] = TableQuery[Techs]
  val userTable: TableQuery[Users] = TableQuery[Users]
  val entriesTable: TableQuery[Entries] = TableQuery[Entries]

  def setupDatabase(): Unit = {
    val setup = DBIO.seq((skillTable.schema ++ techTable.schema ++ userTable.schema ++ entriesTable.schema).create)
    Await.result(db.run(setup), Duration.Inf)
  }

  def insertUserData(): Map[String, Int] = {
    val userOdersky = User(None, "Martin", "Odersky", "martin.odersky@gmail.com",AccessLevel.Basic, Status.Active)
    val userSeverus = User(None, "Severus", "Snape", "severus.snape@hogwarts.com",AccessLevel.Management, Status.Active)
    val userGandalf = User(None, "Gandalf", "YouShallPass", "gandalf@youshallpass.com", AccessLevel.Admin, Status.Active)

    val idUserOdersky: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userOdersky), Duration.Inf)
    val idUserSeverus: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userSeverus), Duration.Inf)
    val idUserGandalf: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userGandalf), Duration.Inf)
    Map(ID_USER_ODERSKY -> idUserOdersky, ID_USER_SNAPE -> idUserSeverus, ID_USER_GANDALF -> idUserGandalf)

  }

  def insertTechData(): Map[String, Int] = {
    val techScala = Tech(None, "scala", TechType.LANGUAGE)
    val techFunctional = Tech(None, "functional programming", TechType.CONCEPT)
    val techDefense = Tech(None, "defense against the dark arts", TechType.CONCEPT)
    val techDarkArts = Tech(None, "dark arts", TechType.CONCEPT)

    val idTechScala: Int = Await.result(db.run(techTable returning techTable.map(_.id) += techScala), Duration.Inf)
    val idTechFunctional: Int = Await.result(db.run(techTable returning techTable.map(_.id) += techFunctional), Duration.Inf)
    val idTechDefense: Int = Await.result(db.run(techTable returning techTable.map(_.id) += techDefense), Duration.Inf)
    val idTechDarkArts: Int = Await.result(db.run(techTable returning techTable.map(_.id) += techDarkArts), Duration.Inf)

    Map(ID_TECH_SCALA -> idTechScala, ID_TECH_FUNCTIONAL -> idTechFunctional, ID_TECH_DEFENSE -> idTechDefense, ID_TECH_DARK_ARTS -> idTechDarkArts)
  }

  def insertSkillData(): Map[String, Int] = {
    val userMap = insertUserData()
    val techMap = insertTechData()

    val skillOderskyScala = Skill(None, userMap(ID_USER_ODERSKY), techMap(ID_TECH_SCALA), SkillLevel.EXPERT, Status.Active)
    val skillOderskyFunctional = Skill(None, userMap(ID_USER_ODERSKY), techMap(ID_TECH_FUNCTIONAL), SkillLevel.EXPERT, Status.Active)
    val skillSeverusDefense = Skill(None, userMap(ID_USER_SNAPE), techMap(ID_TECH_DEFENSE), SkillLevel.EXPERT, Status.Active)
    val skillSeverusDarkArts = Skill(None, userMap(ID_USER_SNAPE), techMap(ID_TECH_DARK_ARTS), SkillLevel.EXPERT, Status.Active)

    val idSkillOderskyScala: Int = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillOderskyScala), Duration.Inf)
    val idSkillOderskyFunctional: Int = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillOderskyFunctional), Duration.Inf)
    val idSkillSeverusDefense: Int = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillSeverusDefense), Duration.Inf)
    val idSkillDarkArts = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillSeverusDarkArts), Duration.Inf)

    userMap ++ techMap ++ Map(
      SKILL_ODERSKY_SCALA -> idSkillOderskyScala,
      SKILL_ODERSKY_FUNCTIONAL -> idSkillOderskyFunctional,
      SKILL_SEVERUS_DEFENSE -> idSkillSeverusDefense,
      SKILL_SEVERUS_DARK_ARTS -> idSkillDarkArts
    )
  }

  def cleanUserData(): Int = {
    Await.result(db.run(userTable.delete), Duration.Inf)
  }

  def cleanTechData(): Int = {
    Await.result(db.run(techTable.delete), Duration.Inf)
  }

  def cleanSkillData(): Int = {
    Await.result(db.run(skillTable.delete), Duration.Inf)
    cleanUserData()
    cleanTechData()
  }

  def dropDatabase(): Unit = {
    val setup = DBIO.seq((skillTable.schema ++ techTable.schema ++ userTable.schema ++ entriesTable.schema).drop)
    Await.result(db.run(setup), Duration.Inf)
  }

}
