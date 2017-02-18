package services

import javax.inject.Inject

import models.EnumTypes.SkillLevel.SkillLevel
import models._
import models.responses.{SkillMatrixResponse, SkillMatrixUsersAndLevel, UserSkillResponse}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class SkillMatrixDAOService @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                       techDAOService: TechDAOService,
                                       userDAOService: UserDAOService
                                      ){

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val skillTable = TableQuery[MyTable.Skills]

  /*
      Adds a skill entered by user to skill matrix table.
      It checks if the skill exists in the skill table, and if not, adds it to the database
   */
  def addSkillByUserIdToSkillMatrix(userId: Int, tech: Tech, skillLevel: SkillLevel): Future[Skill] = {
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
          } yield Skill(skillId, userId = userId, techId = techId, skillLevel = skillLevel)
        case false =>
          val userSkillObject = Skill(
          userId = userId,
          techId = techId,
          skillLevel = skillLevel)
          val addSkillToSkillMatrixQuery = skillTable returning skillTable += userSkillObject
          dbConfig.db.run(addSkillToSkillMatrixQuery)
      }
    }
  }

  def getSkillId(userId: Int, techId: Int): Future[Option[Int]] = {
    val query = skillTable.filter(x => x.userId === userId && x.techId === techId).map(_.id).take(1)
    dbConfig.db.run(query.result.headOption)
  }

  def getAllSkillsByUserId(userId: Int): Future[Seq[Skill]] = {
    userDAOService.exists(userId).flatMap {
      case true =>
        val query = skillTable.filter(x => x.userId === userId)
        dbConfig.db.run(query.result)
      case false => Future(List())
    }
  }

  def getAllSkillMatrixByUserId(userId: Int): Future[Option[UserSkillResponse]] = {
    userDAOService.exists(userId).flatMap {
      case true =>
        for {
          user <- userDAOService.getUserById(userId)
          skills <- getAllSkillsByUserId(userId)
          tech <- techDAOService.getAllTech()
        } yield computeUserSkillResponse(user.get, skills, tech)

      case false => Future(None)
    }
  }

  private def computeUserSkillResponse(user: User, skills: Seq[Skill], tech: Seq[Tech]): Option[UserSkillResponse]  =  skills.size match {
    case  0 => None
    case _ =>
      Some(UserSkillResponse(
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
    val userSkillObject = Skill(
      userId = userId,
      techId = skillId,
      skillLevel = skillLevel)
    dbConfig.db.run(skillTable returning skillTable += userSkillObject)
  }

  def updateSkill(skillId: Int, userId: Int, tech: Tech, skillLevel: SkillLevel): Future[Option[Skill]] = {
    val nrOfUpdatedRows: Future[Int] = dbConfig.db.run(
      skillTable
      .filter(x => x.id === skillId && x.userId === userId && x.techId === tech.id.get) // VALIDATE IN THE CONTROLLER THAT ID IS PRESENT
      .map(x => x.skillLevel)
      .update(skillLevel))

    nrOfUpdatedRows.flatMap {
        case 0 => Future(None)
        case _ =>
          techDAOService.updateTech(tech.id.get, tech)
          val selectQuery = skillTable.filter(_.id === skillId)
          dbConfig.db.run(selectQuery.result.headOption)
    }
  }

  def deleteSkillByUserId(userId: Int, skillId: Int) = {
    skillExistsForUser(skillId, userId).flatMap {
      case false => Future(None)
      case true => dbConfig.db.run(skillTable.filter(_.id === skillId).delete)
    }
  }

  def getAllSkills(): Future[Seq[SkillMatrixResponse]] = {
    for {
      techList <- techDAOService.getAllTech()
      skillList <- getAll()
      userList <- userDAOService.getAllUsers()
    } yield computeSkillMatrix(techList, skillList, userList)
  }

  def getSkillById(skillId: Int): Future[Option[Skill]] = {
    val query = skillTable.filter(x => x.id === skillId)
    dbConfig.db.run(query.result.headOption)
  }

  def getSkillByTechId(techId: Int) = {
    val query = skillTable.filter(x => x.techId === techId)
    dbConfig.db.run(query.result)
  }

  def getSkillMatrixByTechId(techId: Int): Future[Option[SkillMatrixResponse]] = {
    techDAOService.getTechById(techId).flatMap {
      case Some(tech) =>
        for {
          skillList <- getSkillByTechId(techId)
          userList <- userDAOService.getAllUsers()
        } yield computeSkillMatrixByTechId(tech, skillList, userList)
      case None => Future(None)
    }
  }

  private def skillExistsForUser(skillId: Int, userId: Int): Future[Boolean] = {
    dbConfig.db.run(skillTable.filter(x => x.id === skillId && x.userId === userId).exists.result)
  }

  private def skillExistsByTechAndUserId(techId: Int, userId: Int): Future[Boolean] = {
    dbConfig.db.run(skillTable.filter(x => x.techId === techId && x.userId === userId).exists.result)
  }

  private def getAll() = {
    dbConfig.db.run(skillTable.result)
  }

  private def computeSkillMatrixByTechId(tech: Tech, skillList: Seq[Skill], userList: Seq[User]): Option[SkillMatrixResponse] = {
    Some(SkillMatrixResponse(tech.id.get, tech.name, tech.techType, getAllUsersAndLevelInfo(tech.id.get, skillList, userList)))
  }

  private def computeSkillMatrix(techList: Seq[Tech], skillList: Seq[Skill], userList: Seq[User]): Seq[SkillMatrixResponse] = {
    techList.map(tech => SkillMatrixResponse(tech.id.get, tech.name, tech.techType, getAllUsersAndLevelInfo(tech.id.get, skillList, userList)))

  }

  private def getAllUsersAndLevelInfo(techId: Int, skillList: Seq[Skill], userList: Seq[User]) = {
    skillList.filter(skill => skill.techId == techId).map(skill =>
      SkillMatrixUsersAndLevel(getUserName(skill.userId, userList), skill.skillLevel))
  }

  private def getUserName(userId: Int, userList: Seq[User]) = {
    userList.filter(user => user.id.get == userId).map(user => user.firstName + " " + user.lastName).head
  }

}
