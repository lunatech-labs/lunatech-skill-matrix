package services

import javax.inject.Inject

import models.EnumTypes.SkillType.SkillType
import models.{MyTable, Skill}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent._

class SkillDAOService @Inject() (dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val skillTable = TableQuery[MyTable.Skills]

  def addSkill(skill: Skill): Future[Int] = {
    dbConfig.db.run((skillTable returning skillTable.map(_.id)) += skill)
  }

  def createSkill(name: String, skillType: SkillType): Future[Int] = {
    val skill = Skill(
      id = None,
      name = name,
      skillType = skillType
    )
    dbConfig.db.run((skillTable returning skillTable.map(_.id)) += skill)
  }

  def getSkillIdByNameAndType(skill: Skill) : Future[Option[Int]] = {
    val getSkillIdQuery = skillTable.filter( x => x.name === skill.name && x.skillType === skill.skillType).map(_.id).take(1)
    dbConfig.db.run(getSkillIdQuery.result.headOption)
  }

  def getSkills() = {
    dbConfig.db.run(skillTable.result)
  }

}
