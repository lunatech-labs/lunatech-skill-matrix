package models

import models._
import models.responses._
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._
import scala.concurrent._
import ExecutionContext.Implicits.global

import slick.driver.PostgresDriver.api._


case class Skill(id: Option[Int] = None, userId: Int, techId: Int, skillLevel: SkillLevel)

object Skill {
  implicit val userSkillReads: Reads[Skill] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "userId").read[Int] and
      (JsPath \ "techId").read[Int] and
      (JsPath \ "skillLevel").read[SkillLevel]
    ) (Skill.apply _)

  implicit val userSkillWrites: Writes[Skill] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "userId").write[Int] and
      (JsPath \ "techId").write[Int] and
      (JsPath \ "skillLevel").write[SkillLevel]
    ) (unlift(Skill.unapply _))
}

class Skills(tag: Tag) extends Table[models.Skill](tag, "user_skills") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def userId = column[Int]("user_id")

  def techId = column[Int]("tech_id")

  def skillLevel = column[SkillLevel]("skill_level")

  def * = (id.?, userId, techId, skillLevel) <> ((models.Skill.apply _).tupled, models.Skill.unapply _)

  def user = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)

  def tech = foreignKey("TECH_FK", techId, TableQuery[Techs])(_.id)
}


object Skills {
  val skillTable = TableQuery[Skills]

  def add(userId: Int, techId: Int, skillLevel: SkillLevel): Future[Skill] = {
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
        Connection.db.run(addSkillToSkillMatrixQuery)
    }

  }

  def getSkillId(userId: Int, techId: Int): Future[Option[Int]] = {
    val query = skillTable.filter(x => x.userId === userId && x.techId === techId).map(_.id).take(1)
    Connection.db.run(query.result.headOption)
  }

  def getAllSkillMatrixByUser(user: User): Future[Seq[(Skill, Tech)]] = {
    val join = for {
      (skill, tech) <- skillTable join Techs.techTable on (_.techId === _.id)
    } yield {
      (skill, tech)
    }
    Connection.db.run(join.result)
  }

  def update(skillId: Int, userId: Int, tech: Tech, skillLevel: SkillLevel): Future[Option[Skill]] = {
    val nrOfUpdatedRows: Future[Int] = Connection.db.run(
      skillTable
        .filter(skill => skill.id === skillId && skill.userId === userId && skill.techId === tech.id.get)
        .map(skill => skill.skillLevel)
        .update(skillLevel))

    nrOfUpdatedRows.flatMap {
      case 0 => Future(None)
      case _ =>
        val selectQuery = skillTable.filter(_.id === skillId)
        Connection.db.run(selectQuery.result.headOption)
    }
  }

  def delete(userId: Int, skillId: Int): Future[Int] = {
    skillExistsForUser(skillId, userId).flatMap {
      case false => Future(0)
      case true => Connection.db.run(skillTable.filter(_.id === skillId).delete)
    }
  }

  def getAllSkills: Future[Seq[(Skill, User, Tech)]] = {
    val join = for {
      ((skill, user), tech) <- skillTable join Users.userTable on (_.userId === _.id) join Techs.techTable on (_._1.techId === _.id)
    } yield {
      (skill, user, tech)
    }

    Connection.db.run(join.result)
  }

  def getSkillById(skillId: Int): Future[Option[Skill]] = {
    val query = skillTable.filter(skill => skill.id === skillId)
    Connection.db.run(query.result.headOption)
  }

  def getSkillByTechId(techId: Int): Future[Seq[Skill]] = {
    val query = skillTable.filter(skill => skill.techId === techId)
    Connection.db.run(query.result)
  }

  private def skillExistsForUser(skillId: Int, userId: Int): Future[Boolean] = {
    Connection.db.run(skillTable.filter(skill => skill.id === skillId && skill.userId === userId).exists.result)
  }

  private def skillExistsByTechAndUserId(techId: Int, userId: Int): Future[Boolean] = {
    Connection.db.run(skillTable.filter(skill => skill.techId === techId && skill.userId === userId).exists.result)
  }

  private def getAll: Future[Seq[Skill]] = {
    Connection.db.run(skillTable.result)
  }
}