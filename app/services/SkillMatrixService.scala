package services

import javax.inject.Inject

import common.DBConnection
import models._
import models.responses.{SkillMatrixResponse, SkillMatrixUsersAndLevel, UserSkillResponse}

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

  def updateUserSkill(skillId: Int, userId: Int, tech: Tech, skillLevel: SkillLevel): Future[Option[Int]] = {
    val techId: Future[Int] = techService.getTechIdOrInsert(tech)

    techId.flatMap { tId =>
      Skills.update(skillId, userId, tId, skillLevel)
    }
  }

  def deleteUserSkill(userId: Int, skillId: Int): Future[Option[Int]] = {
    Skills.delete(userId, skillId).map {
      case 0 => None
      case rows@_ => Some(rows)
    }
  }

  def getUserSkills(userId: Int): Future[Option[UserSkillResponse]] = {
    userService.getUserById(userId).flatMap {
      case Some(user) =>
        val result = Skills.getAllSkillMatrixByUser(user)
        result.map { skills =>
          computeUserSkillResponse(user, skills)
        }
      case None => Future(None)
    }
  }

  def getAllSkills: Future[Seq[SkillMatrixResponse]] = {
    val result = Skills.getAllSkills
    result.map { matrix =>
      computeSkillMatrix(matrix.groupBy(_._3))
    }
  }

  def getSkillMatrixByTechId(techId: Int): Future[Option[SkillMatrixResponse]] = {
    techService.getById(techId).flatMap {
      case Some(tech) =>
        val skills = Skills.getSkillByTechId(tech.id.getOrElse(0))
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

  private def computeUserSkillResponse(user: User, skills: Seq[(Skill, Tech)]): Option[UserSkillResponse] = skills.size match {
    case 0 => Some(UserSkillResponse(
      userId = user.id.get,
      firstName = user.firstName,
      lastName = user.lastName,
      skills = Seq()))
    case _ =>
      Some(UserSkillResponse(
        userId = user.id.get,
        firstName = user.firstName,
        lastName = user.lastName,
        skills = skills.map { skill =>
          SkillMatrixItem(
            tech = skill._2,
            skillLevel = skill._1.skillLevel,
            id = skill._1.id
          )
        }))
  }
}
