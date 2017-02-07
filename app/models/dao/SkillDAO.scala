package models.dao

import javax.inject.Inject

import models.EnumTypes.SkillType.SkillType
import models.{MyTable, Skill}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


/**
  * Created by tatianamoldovan on 02/02/2017.
  */
class SkillDAO @Inject() (dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val skillTable = TableQuery[MyTable.Skills]

  def getSkillIdByNameAndType(name: String, skillType: SkillType): Int = {
    val query = skillTable.filter( x => x.name === name && x.skillType === skillType).map(_.id).take(1)
    val results = dbConfig.db.run(query.result.headOption)
    val id = Await.result(results, Duration.Inf) // sorry :( ;(
    id.getOrElse(-1)
  }

  def createSkill(name: String, skillType: SkillType): Int = {
    val skill: Skill = Skill(
      id = None,
      name = name,
      skillType = skillType
    )
    val results = dbConfig.db.run(skillTable returning skillTable.map(_.id) += skill)
    Await.result(results, Duration.Inf) // ;( :( sorryyyy https://www.youtube.com/watch?v=5ypUz1WYens
  }

  def getSkills() = {
    dbConfig.db.run(skillTable.result)
  }
}
