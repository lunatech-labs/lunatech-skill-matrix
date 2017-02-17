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

    techId.flatMap { techId =>
      skillExistsByTechAndUserId(techId, userId).flatMap {
        case true =>
          // in the end we should return a message that the skill already exists, not sure what to pass to the controller
          // in order to make this method work.
          for {
            skillId <- getSkillId(userId, techId)
          } yield SkillMatrix(skillId, userId = userId, techId = techId, skillLevel = skillLevel)
        case false =>
          val userSkillObject = SkillMatrix(
          userId = userId,
          techId = techId,
          skillLevel = skillLevel)
          val addSkillToSkillMatrixQuery = skillMatrixTable returning skillMatrixTable += userSkillObject
          dbConfig.db.run(addSkillToSkillMatrixQuery)
      }
    }
  }

  def getSkillId(userId: Int, techId: Int): Future[Option[Int]] = {
    val query = skillMatrixTable.filter(x => x.userId === userId && x.techId === techId).map(_.id).take(1)
    dbConfig.db.run(query.result.headOption)
  }

  def getAllSkillsByUserId(userId: Int): Future[Seq[SkillMatrix]] = {
    userDAOService.exists(userId).flatMap {
      case true =>
        val query = skillMatrixTable.filter(x => x.userId === userId)
        dbConfig.db.run(query.result)
      case false => Future(List())
    }
  }

  def getAllSkillMatrixByUserId(userId: Int): Future[Option[SkillMatrixForUserResult]] = {
    userDAOService.exists(userId).flatMap {
      case true =>
        for {
          user <- userDAOService.getUserById(userId)
          skills <- getAllSkillsByUserId(userId)
          tech <- techDAOService.getAllTech()
        } yield computeSkillMatrixForUser(user.get, skills, tech)

      case false => Future(None)
    }
  }

  private def computeSkillMatrixForUser(user: User, skills: Seq[SkillMatrix], tech: Seq[Tech]): Option[SkillMatrixForUserResult]  =  skills.size match {
    case  0 => None
    case _ =>
      Some(SkillMatrixForUserResult(
        userId = user.id.get,
        firstName = user.firstName,
        lastName = user.lastName,
        skills = skills.map { skill =>
          SkillMatrixItem(
            tech = tech.filter(_.id.contains(skill.techId)).head,
            skillLevel = skill.skillLevel
          )
        }))
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
    /*skillExists(skillId, userId).flatMap {
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
        }
      case false => Future(None)
    }*/

    //WIP: not working
    val oldSkill = for {
      oldSkill <- getSkillById(skillId) if Await.result(skillExists(skillId, userId), Duration.Inf)
    } yield oldSkill

    oldSkill.flatMap{
      case Some(skill) =>
        val skillObject: SkillMatrix = SkillMatrix(
          id = Some(skillId),
          userId = userId,
          techId = skill.techId,
          skillLevel = skillLevel
        )
        techDAOService.updateTech(skill.techId, tech)
        dbConfig.db.run((skillMatrixTable returning skillMatrixTable).insertOrUpdate(skillObject))
      case _ => Future(None)
    }

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
      userList <- userDAOService.getAllUsers()
    } yield computeSkillMatrix(techList, skillList, userList)
  }

  def getSkillById(skillId: Int): Future[Option[SkillMatrix]] = {
    val query = skillMatrixTable.filter(x => x.id === skillId)
    dbConfig.db.run(query.result.headOption)
  }

  def getSkillByTechId(techId: Int) = {
    val query = skillMatrixTable.filter(x => x.techId === techId)
    dbConfig.db.run(query.result)
  }

  def getSkillMatrixByTechId(techId: Int): Future[Option[SkillMatrixResult]] = {
    techDAOService.getTechById(techId).flatMap {
      case Some(tech) =>
        for {
          skillList <- getSkillByTechId(techId)
          userList <- userDAOService.getAllUsers()
        } yield computeSkillMatrixByTechId(tech, skillList, userList)
      case None => Future(None)
    }
  }

  private def skillExists(skillId: Int, userId: Int): Future[Boolean] = {
    dbConfig.db.run(skillMatrixTable.filter(x => x.id === skillId && x.userId === userId).exists.result)
  }

  private def skillExistsByTechAndUserId(techId: Int, userId: Int): Future[Boolean] = {
    dbConfig.db.run(skillMatrixTable.filter(x => x.techId === techId && x.userId === userId).exists.result)
  }

  private def getAll() = {
    dbConfig.db.run(skillMatrixTable.result)
  }

  //  not sure if this is the right thing to do
  private def computeSkillMatrixByTechId(tech: Tech, skillList: Seq[SkillMatrix], userList: Seq[User]): Option[SkillMatrixResult] = {
    Some(SkillMatrixResult(tech.id.get, tech.name, tech.techType, getAllUsersAndLevelInfo(tech.id.get, skillList, userList)))
  }

  //  not sure if this is the right thing to do
  private def computeSkillMatrix(techList: Seq[Tech], skillList: Seq[SkillMatrix], userList: Seq[User]): Seq[SkillMatrixResult] = {
    techList.map(tech => SkillMatrixResult(tech.id.get, tech.name, tech.techType, getAllUsersAndLevelInfo(tech.id.get, skillList, userList)))

  }

  private def getAllUsersAndLevelInfo(techId: Int, skillList: Seq[SkillMatrix], userList: Seq[User]) = {
    skillList.filter(skill => skill.techId == techId).map(skill =>
      SkillMatrixUsersAndLevel(getUserName(skill.userId, userList), skill.skillLevel))
  }

  private def getUserName(userId: Int, userList: Seq[User]) = {
    userList.filter(user => user.id.get == userId).map(user => user.firstName + " " + user.lastName).head
  }

}
