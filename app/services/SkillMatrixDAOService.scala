package services

import javax.inject.Inject

import models.EnumTypes.SkillLevel.SkillLevel
import models.{MyTable, Skill, SkillMatrix}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class SkillMatrixDAOService @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                       skillDAOService: SkillDAOService,
                                       userDAOService: UserDAOService
                                      ){

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val skillMatrixTable = TableQuery[MyTable.SkillMatrix]

  /*
      Adds a skill entered by user to skill matrix table.
      It checks if the skill exists in the skill table, and if not, adds it to the database
   */
  def addSkillByUserIdToSkillMatrix(userId: Int, skill: Skill, skillLevel: SkillLevel): Future[SkillMatrix] = {
    val skillId: Future[Int] = skillDAOService.getSkillIdByNameAndType(skill).flatMap {
      case None => skillDAOService.addSkill(skill)
      case id => Future(id.get)
    }

    skillId.flatMap { id =>
      val userSkillObject = SkillMatrix(
        userId = userId,
        skillId = id,
        skillLevel = skillLevel)
      val addSkillToSkillMatrixQuery = skillMatrixTable returning skillMatrixTable += userSkillObject
      dbConfig.db.run(addSkillToSkillMatrixQuery)
    }
  }

  def getSkillId(userId: Int, skillId: Int): Future[Option[Int]] = {
    val query = skillMatrixTable.filter(x => x.userId === userId && x.skillId === skillId).map(_.id).take(1)
    dbConfig.db.run(query.result.headOption)
  }

  def getAllSkillsByUserId(userId: Int): Future[Seq[SkillMatrix]] = {
    val userExists = userDAOService.exists(userId)
    userExists.flatMap {
      case true =>
        val query = skillMatrixTable.filter(x => x.userId === userId)
        dbConfig.db.run(query.result)
      case false => Future(List())
    }
  }

  def createSkill(userId: Int, skillId: Int, skillLevel: SkillLevel) = {
    val userSkillObject = SkillMatrix(
      userId = userId,
      skillId = skillId,
      skillLevel = skillLevel)
    dbConfig.db.run(skillMatrixTable returning skillMatrixTable += userSkillObject)
  }

  def updateSkill(skillMId: Int, userId: Int, skill: Skill, skillLevel: SkillLevel): Future[Option[SkillMatrix]] = {
    val skillId: Future[Option[Int]] = skillDAOService.getSkillIdByNameAndType(skill)

    skillId.flatMap {
      case None => Future(None)
      case id =>
        val userSkillObject = SkillMatrix(
          id = Some(skillMId),
          userId = userId,
          skillId = id.get,
          skillLevel = skillLevel)
        val r = (skillMatrixTable returning skillMatrixTable).insertOrUpdate(userSkillObject)
        dbConfig.db.run(r)
    }
  }

  def deleteSkillByUserId(userId: Int, userSkillId: Int) = {
    val skillExists: Future[Boolean] = existsForSpecificUSer(userSkillId, userId)
    skillExists.flatMap {
      case false => Future(None)
      case true => dbConfig.db.run(skillMatrixTable.filter(_.id === userSkillId).delete)
    }
  }

  /* THE FUNCTIONALY OF THIS METHOD NEEDS TO BE DEFINED
  def getAllSkillsFromSkillMatrix() = {
    val query = skillMatrixTable.groupBy(_.skillId)
    dbConfig.db.run(query.result)

  }*/

  def getSkillById(skillId: Int) = {
    val query = skillMatrixTable.filter(x => x.skillId === skillId)
    dbConfig.db.run(query.result)
  }

  private def existsForSpecificUSer(skillId: Int, userId: Int): Future[Boolean] = {
    dbConfig.db.run(skillMatrixTable.filter(x => x.id === skillId && x.userId === userId).exists.result)
  }


}
