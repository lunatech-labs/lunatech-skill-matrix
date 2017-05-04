package services

import javax.inject.Inject

import common.DBConnection
import models._
import models.db.Skills
import models.{SkillMatrixResponse, SkillMatrixUsersAndLevel, UserSkillResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class SkillMatrixService @Inject()(techService: TechService,
                                   userService: UserService)(implicit connection: DBConnection) {

  def addUserSkill(userId: Int, tech: Tech, skillLevel: SkillLevel): Future[Int] = {
    val techId: Future[Int] = techService.getTechIdOrInsert(tech)

    techId.flatMap { tId =>
      Skills.add(userId, tId, skillLevel)
    }
  }

  def updateUserSkill(skillId: Int, userId: Int, techId: Int, skillLevel: SkillLevel): Future[Option[Int]] = {
    Skills.update(skillId, userId, techId, skillLevel)
  }

  def deleteUserSkill(userId: Int, skillId: Int): Future[Option[Int]] = {
    Skills.delete(userId, skillId).map {
      case 0 => None
      case nrOfRows@_ => Some(nrOfRows)
    }
  }

  def getUserSkills(userId: Int): Future[Option[UserSkillResponse]] = {
    userService.getUserById(userId).flatMap {
      case Some(user) =>
        val result = Skills.getAllSkillMatrixByUser(userId)
        result.map { skills =>
          computeUserSkillResponse(user, skills)
        }
      case None => Future(None)
    }
  }

  def getAllSkills: Future[Seq[SkillMatrixResponse]] = {
    val result: Future[Seq[(Skill, User, Tech)]] = Skills.getAllSkills
    result.map { matrix =>
      computeSkillMatrix(matrix.groupBy(_._3))
    }
  }

  def getSkillMatrixByTechId(techId: Int): Future[Option[SkillMatrixResponse]] = {
    techService.getById(techId).flatMap {
      case Some(tech) =>
        val skills = Skills.getSkillByTechId(techId)
        val users = userService.getAll

        for {
          s <- skills
          u <- users
        } yield computeSkillMatrixByTechId(tech, s, u)
      case None => Future(None)
    }
  }


  private def computeSkillMatrixByTechId(tech: Tech, skillList: Seq[Skill], userList: Seq[User]): Option[SkillMatrixResponse] = {
    Some(SkillMatrixResponse(tech.id.get, tech.name, tech.techType, getAllUsersAndLevelInfo(tech.id.get, skillList, userList)))
  }

  private def getAllUsersAndLevelInfo(techId: Int, skillList: Seq[Skill], userList: Seq[User]) = {
    skillList.filter(skill => skill.techId == techId).map { skill =>
      val user = userList.filter(_.id.getOrElse(0) == skill.userId).head
      SkillMatrixUsersAndLevel(user.fullName, skill.skillLevel)
    }
  }

  private def computeSkillMatrix(matrix: Map[Tech, Seq[(Skill, User, Tech)]]): Seq[SkillMatrixResponse] = {
    matrix.map { case (techKey, items) =>
      SkillMatrixResponse(
        techKey.id.get,
        techKey.name,
        techKey.techType,
        items.map { case (skill, user, _) => SkillMatrixUsersAndLevel(user.fullName, skill.skillLevel) }
      )
    }.toSeq
  }

  private def computeUserSkillResponse(user: User, skillsAndTech: Seq[(Skill, Tech)]): Option[UserSkillResponse] = {
    val resultedSkills = skillsAndTech.size match {
      case 0 => Seq()
      case _ => skillsAndTech map Function.tupled((skill, tech) => SkillMatrixItem(tech, skill.skillLevel, skill.id))
    }
    Some(UserSkillResponse(user.id.get, user.firstName, user.lastName, resultedSkills))
  }
}
