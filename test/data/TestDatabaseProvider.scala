package data

import java.nio.file.Paths

import common.DBConnectionProvider
import models._
import slick.lifted.TableQuery
import data.TestData._
import models.db._
import models.db.CustomPostgresProfile.api._
import org.joda.time.DateTime
import play.api.libs.json.Json
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig
import scala.concurrent.{Await, _}
import scala.concurrent.duration.Duration


trait TestDatabaseProvider {
  self: DBConnectionProvider =>

  val skillTable: TableQuery[Skills] = TableQuery[Skills]
  val techTable: TableQuery[Techs] = TableQuery[Techs]
  val userTable: TableQuery[Users] = TableQuery[Users]
  val entriesTable: TableQuery[Entries] = TableQuery[Entries]
  val userSchedulerAuditTable: TableQuery[models.db.UserSchedulerAudit] = TableQuery[models.db.UserSchedulerAudit]

  //TODO: try to have different configs for db in application-test.conf
  //TODO: and use DI to choose the right profile AND apply the right evolutions using scripts
  //TODO: Or just use a postgres db (make a test db alongside the PRD db)
  /**
    * We have an array field in the users table which causes problems with H2 db
    * Postgres arrays and H2 arrays apparently are not compatible, hence we need different create sql for users table
    */
  def setupDatabase(): Unit = profile.toString() match {
    case "slick.jdbc.H2Profile$" => setupH2Database()
    case _ => setupPostgresDatabase()
  }

  def setupPostgresDatabase(): Unit = {
    val setup = DBIO.seq((skillTable.schema ++ techTable.schema ++ userTable.schema ++ entriesTable.schema ++ userSchedulerAuditTable.schema).create)
    Await.result(db.run(setup), Duration.Inf)
  }

  def setupH2Database(): Unit = {
    val setup = DBIO.seq(
      sqlu"""CREATE TABLE IF NOT EXISTS users (id SERIAL NOT NULL PRIMARY KEY, firstName VARCHAR NOT NULL, lastName VARCHAR NOT NULL, email VARCHAR NOT NULL,accesslevels ARRAY,status VARCHAR NOT NULL);""",
      sqlu"""CREATE TABLE IF NOT EXISTS tech(id SERIAL NOT NULL PRIMARY KEY, tech_name VARCHAR NOT NULL, tech_label VARCHAR NOT NULL, tech_type VARCHAR NOT NULL);""",
      sqlu"""CREATE TABLE IF NOT EXISTS user_skills (id SERIAL NOT NULL PRIMARY KEY,user_id integer REFERENCES users (id) ON DELETE CASCADE,tech_id integer REFERENCES tech (id) ON DELETE CASCADE,skill_level varchar(255),status varchar(30));""",
      sqlu"""CREATE TABLE IF NOT EXISTS entries(id SERIAL NOT NULL PRIMARY KEY, user_id   integer REFERENCES users (id) ON DELETE CASCADE, skill_id  integer REFERENCES user_skills (id) ON DELETE CASCADE, entry_action VARCHAR NOT NULL, occurrence VARCHAR NOT NULL);"""
    )
    Await.result(db.run(setup), Duration.Inf)
  }

  def setupUserAudit(): Unit = {
    val setup = DBIO.seq(userSchedulerAuditTable.schema.create)
    Await.result(db.run(setup), Duration.Inf)
  }

  def insertUserData(): Map[String, Int] = {
    val idUserOdersky: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userOdersky), Duration.Inf)
    val idUserSeverus: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userSeverus), Duration.Inf)
    val idUserGandalf: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userGandalf), Duration.Inf)
    val idUserDumbledore: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userDumbledore), Duration.Inf)
    val idUserVoldemort: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userVoldemort), Duration.Inf)
    val idUserPettigrew: Int = Await.result(db.run(userTable returning userTable.map(_.id) += userPettigrew), Duration.Inf)
    Map(
      ID_USER_ODERSKY -> idUserOdersky, ID_USER_SNAPE -> idUserSeverus, ID_USER_GANDALF -> idUserGandalf, ID_USER_DUMBLEDORE -> idUserDumbledore,
      ID_USER_VOLDEMORT -> idUserVoldemort, ID_USER_PETTIGREW -> idUserPettigrew)

  }

  def insertTechData(): Map[String, Int] = {
    val techScala = Tech(None, "scala", "Scala", TechType.LANGUAGE)
    val techFunctional = Tech(None,"functional programming", "Functional Programming", TechType.CONCEPT)
    val techDefense = Tech(None, "defense against the dark arts", "Defense Against the Dark Arts", TechType.CONCEPT)
    val techDarkArts = Tech(None, "dark arts", "Dark Arts", TechType.CONCEPT)

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
    val skillDumbledoreDarkArts = Skill(None, userMap(ID_USER_DUMBLEDORE), techMap(ID_TECH_DARK_ARTS), SkillLevel.EXPERT, Status.Inactive)
    val skillVoldemortDarkArts = Skill(None, userMap(ID_USER_VOLDEMORT), techMap(ID_TECH_DARK_ARTS), SkillLevel.EXPERT, Status.Inactive)

    val idSkillOderskyScala: Int = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillOderskyScala), Duration.Inf)
    val idSkillOderskyFunctional: Int = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillOderskyFunctional), Duration.Inf)
    val idSkillSeverusDefense: Int = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillSeverusDefense), Duration.Inf)
    val idSkillSeverusDarkArts = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillSeverusDarkArts), Duration.Inf)
    val idSkillDumbledoreDarkArts = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillDumbledoreDarkArts), Duration.Inf)
    val idSkillVoldemortDarkArts = Await.result(db.run(skillTable returning skillTable.map(_.id) += skillVoldemortDarkArts), Duration.Inf)

    userMap ++ techMap ++ Map(
      SKILL_ODERSKY_SCALA -> idSkillOderskyScala,
      SKILL_ODERSKY_FUNCTIONAL -> idSkillOderskyFunctional,
      SKILL_SEVERUS_DEFENSE -> idSkillSeverusDefense,
      SKILL_SEVERUS_DARK_ARTS -> idSkillSeverusDarkArts,
      SKILL_DUMBLEDORE_DARK_ARTS -> idSkillDumbledoreDarkArts,
      SKILL_VOLDEMORT_DARK_ARTS -> idSkillVoldemortDarkArts
    )
  }

  def insertUserAuditData(): Int = {
    val audit = models.UserSchedulerAudit(None, DateTime.now(), "Success", Json.obj("usersWithUpdatedRoles" -> "[]", "usersToActivate" -> "[]", "usersToDeactivate" -> "[]"))
    Await.result(db.run(userSchedulerAuditTable returning userSchedulerAuditTable.map(_.id) += audit), Duration.Inf)
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

  def cleanUserAuditData(): Int = {
    Await.result(db.run(userSchedulerAuditTable.delete), Duration.Inf)
  }

  def dropDatabase(): Unit = profile.toString() match {
    case "slick.jdbc.H2Profile$" => dropH2Database()
    case _ => dropPostgresDatabase()
  }

  def dropPostgresDatabase(): Unit = {
    val setup = DBIO.seq((skillTable.schema ++ techTable.schema ++ userTable.schema ++ entriesTable.schema ++ userSchedulerAuditTable.schema).drop)
    Await.result(db.run(setup), Duration.Inf)
  }

  def dropH2Database(): Unit = {
    val setup = DBIO.seq(
      sqlu"""DROP TABLE IF EXISTS users;""",
      sqlu"""DROP TABLE IF EXISTS tech;""",
      sqlu"""DROP TABLE IF EXISTS user_skills;""",
      sqlu"""DROP TABLE IF EXISTS entries;"""
    )
    Await.result(db.run(setup), Duration.Inf)
  }

  def dropUserAudit(): Unit = {
    val setup = DBIO.seq(userSchedulerAuditTable.schema.drop)
    Await.result(db.run(setup), Duration.Inf)
  }

}
