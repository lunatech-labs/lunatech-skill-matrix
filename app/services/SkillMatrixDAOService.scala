package services

import javax.inject.Inject

import models.EnumTypes.SkillLevel.SkillLevel
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SkillMatrixDAOService @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                       techDAOService: TechDAOService,
                                       userDAOService: UserDAOService
                                      ){

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val skillMatrixTable = TableQuery[MyTable.SkillMatrix]

  /*
      Adds a skill entered by user to skill matrix table.
      It checks if the skill exists in the skill table, and if not, adds it to the database
   */
  def addSkillByUserIdToSkillMatrix(userId: Int, tech: Tech, skillLevel: SkillLevel): Future[SkillMatrix] = {
    val techId: Future[Int] = techDAOService.getTechIdByNameAndType(tech).flatMap {
      case None => techDAOService.addTech(tech)
      case id => Future(id.get)
    }

    techId.flatMap { id =>
      val userSkillObject = SkillMatrix(
        userId = userId,
        techId = id,
        skillLevel = skillLevel)
      val addSkillToSkillMatrixQuery = skillMatrixTable returning skillMatrixTable += userSkillObject
      dbConfig.db.run(addSkillToSkillMatrixQuery)
    }
  }

  def getSkillId(userId: Int, skillId: Int): Future[Option[Int]] = {
    val query = skillMatrixTable.filter(x => x.userId === userId && x.techId === skillId).map(_.id).take(1)
    dbConfig.db.run(query.result.headOption)
  }

  def getAllSkillsByUserId(userId: Int): Future[Seq[SkillMatrix]] = {
    userDAOService.exists(userId)flatMap {
      case true =>
        val query = skillMatrixTable.filter(x => x.userId === userId)
        dbConfig.db.run(query.result)
      case false => Future(List())
    }
  }

  def createSkill(userId: Int, skillId: Int, skillLevel: SkillLevel) = {
    val userSkillObject = SkillMatrix(
      userId = userId,
      techId = skillId,
      skillLevel = skillLevel)
    dbConfig.db.run(skillMatrixTable returning skillMatrixTable += userSkillObject)
  }

  def updateSkill(skillId: Int, userId: Int, tech: Tech, skillLevel: SkillLevel): Future[Option[SkillMatrix]] = {
    //this is very ugly :(
    skillExists(skillId, userId).flatMap {
      case true =>
        val oldSkill = getSkillById(skillId)
        oldSkill.flatMap {
          case Some(skill) =>
            val skillObject = SkillMatrix(
              id = Some(skillId),
              userId = userId,
              techId = skill.techId,
              skillLevel = skillLevel
            )
            techDAOService.updateTech(skill.techId, tech)
            val r = (skillMatrixTable returning skillMatrixTable).insertOrUpdate(skillObject)
            dbConfig.db.run(r)
          case _ => Future(None)
        }
      case false => Future(None)
    }

    //WIP: not working
    /*for {
      oldSkill: Option[SkillMatrix] <- getSkillById(skillId) if Await.result(skillExists(skillId, userId), Duration.Inf)
     // _ <- techDAOService.updateTech(oldSkill.get.techId, tech)
      skillObject: SkillMatrix = SkillMatrix(
        id = Some(skillId),
        userId = userId,
        techId = oldSkill.get.techId,
        skillLevel = skillLevel
      )
      result <- dbConfig.db.run((skillMatrixTable returning skillMatrixTable).insertOrUpdate(skillObject))
    } yield result*/
  }

  def deleteSkillByUserId(userId: Int, skillId: Int) = {
    skillExists(skillId, userId).flatMap {
      case false => Future(None)
      case true => dbConfig.db.run(skillMatrixTable.filter(_.id === skillId).delete)
    }
  }

  def getAllSkills(): Future[Seq[SkillMatrixResult]] = {
    for {
      techList <- techDAOService.getAllTech()
      skillList <- getAll()
      result <- computeSkillMatrix(techList, skillList)
    } yield result
  }

  def getSkillByTechId(techId: Int) = {
    val query = skillMatrixTable.filter(x => x.techId === techId)
    dbConfig.db.run(query.result)
  }

  def getSkillById(skillId: Int): Future[Option[SkillMatrix]] = {
    val query = skillMatrixTable.filter(x => x.id === skillId)
    dbConfig.db.run(query.result.headOption)
  }

  private def skillExists(skillId: Int, userId: Int): Future[Boolean] = {
    dbConfig.db.run(skillMatrixTable.filter(x => x.id === skillId && x.userId === userId).exists.result)
  }

  private def getAll() = {
    dbConfig.db.run(skillMatrixTable.result)
  }

  //  not sure if this is the right thing to do
  private def computeSkillMatrix(techList: Seq[Tech], skillList: Seq[SkillMatrix]) = {
    Future(techList.map(tech => SkillMatrixResult(tech.id.get, tech.name, tech.techType, getAllUsersAndLevelInfo(tech.id.get, skillList))))

  }

  private def getAllUsersAndLevelInfo(techId: Int, skillList: Seq[SkillMatrix]) = {
    skillList.filter(skill => skill.techId == techId).map(skill => SkillMatrixUsersAndLevel(skill.userId, skill.skillLevel))
  }


}
