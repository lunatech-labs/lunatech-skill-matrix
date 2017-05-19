package models.db

import com.typesafe.scalalogging.LazyLogging
import common.DBConnection
import models._
import slick.driver.PostgresDriver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape, TableQuery}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class Skills(tag: Tag) extends Table[models.Skill](tag, "user_skills") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def userId: Rep[Int] = column[Int]("user_id")

  def techId: Rep[Int] = column[Int]("tech_id")

  def skillLevel: Rep[SkillLevel] = column[SkillLevel]("skill_level")

  def * : ProvenShape[Skill] = (id.?, userId, techId, skillLevel) <> ((models.Skill.apply _).tupled, models.Skill.unapply _)

  def user: ForeignKeyQuery[Users, User] = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)

  def tech: ForeignKeyQuery[Techs, Tech] = foreignKey("TECH_FK", techId, TableQuery[Techs])(_.id)
}


object Skills extends LazyLogging {
  val skillTable: TableQuery[Skills] = TableQuery[Skills]

  def add(userId: Int, techId: Int, skillLevel: SkillLevel)(implicit connection: DBConnection): Future[Int] = {
    skillExistsByTechAndUserId(techId, userId).flatMap {
      case true =>
          for {
            skillId <- getSkillId(userId, techId)
          } yield skillId.get
      case false =>
          val userSkillObject = Skill(
            userId = userId,
            techId = techId,
            skillLevel = skillLevel)
          logger.info("adding new skill for userId {} and techId {} with skillLevel {}", userId.toString, techId.toString, skillLevel)
          val addSkillToSkillMatrixQuery = skillTable returning skillTable.map(_.id) += userSkillObject
          connection.db.run(addSkillToSkillMatrixQuery)
    }
  }

  def getAllSkillMatrixByUser(userId: Int)(implicit connection: DBConnection): Future[Seq[(Skill, Tech)]] = {
    val join = for {
      skill <- skillTable.filter(_.userId === userId)
      tech <- Techs.techTable if skill.techId === tech.id
    } yield {
      (skill, tech)
    }
    connection.db.run(join.result)
  }

  //TODO:  refactor this method
  def update(skillId: Int, userId: Int, techId: Int, skillLevel: SkillLevel)(implicit connection: DBConnection): Future[Option[Int]] = {
    logger.info("info updating skillId {} for user {} and techId {} with skillLevel {}", skillId.toString, userId.toString, techId.toString, skillLevel)
    val nrOfUpdatedRows: Future[Int] = connection.db.run(
      skillTable
        .filter(skill => skill.id === skillId && skill.userId === userId && skill.techId === techId)
        .map(skill => skill.skillLevel)
        .update(skillLevel))

    nrOfUpdatedRows.flatMap {
      case 0 => Future(None)
      case _ =>
        val selectQuery = skillTable.filter(_.id === skillId).map(_.id)
        connection.db.run(selectQuery.result.headOption)
    }
  }

  def delete(userId: Int, skillId: Int)(implicit connection: DBConnection): Future[Int] = {
    skillExistsForUser(skillId, userId).flatMap {
      case false => Future(0)
      case true =>
        logger.info("deleting skillId {} for userId {}", skillId.toString, userId.toString)
        connection.db.run(skillTable.filter(_.id === skillId).delete)
    }
  }

  def getAllSkills(implicit connection: DBConnection): Future[Seq[(Skill, User, Tech)]] = {
    val join = for {
      skill <- skillTable
      user <- Users.userTable if skill.userId === user.id
      tech <- Techs.techTable if skill.techId === tech.id
    } yield {
      (skill, user, tech)
    }

    connection.db.run(join.result)
  }

  def getSkillByTechId(techId: Int)(implicit connection: DBConnection): Future[Seq[Skill]] = {
    val query = skillTable.filter(skill => skill.techId === techId)
    connection.db.run(query.result)
  }

  private def getSkillId(userId: Int, techId: Int)(implicit connection: DBConnection): Future[Option[Int]] = {
    val query = skillTable.filter(x => x.userId === userId && x.techId === techId).map(_.id).take(1)
    connection.db.run(query.result.headOption)
  }

  private def skillExistsForUser(skillId: Int, userId: Int)(implicit connection: DBConnection): Future[Boolean] = {
    connection.db.run(skillTable.filter(skill => skill.id === skillId && skill.userId === userId).exists.result)
  }

  private def skillExistsByTechAndUserId(techId: Int, userId: Int)(implicit connection: DBConnection): Future[Boolean] = {
    connection.db.run(skillTable.filter(skill => skill.techId === techId && skill.userId === userId).exists.result)
  }

}