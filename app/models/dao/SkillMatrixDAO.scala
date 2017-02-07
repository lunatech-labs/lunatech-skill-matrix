package models.dao

import javax.inject.Inject

import models.EnumTypes.SkillLevel.SkillLevel
import models.{MyTable, SkillMatrix}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by tatianamoldovan on 06/02/2017.
  */
class SkillMatrixDAO @Inject() (dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val skillMatrixTable = TableQuery[MyTable.SkillMatrix]

  def getSkillId(userId: Int, skillId: Int) = {
    val query = skillMatrixTable.filter(x => x.userId === userId && x.skillId === skillId).map(_.id).take(1)
    val results = dbConfig.db.run(query.result.headOption)
    val id = Await.result(results, Duration.Inf) // sorry :( ;(
    id.getOrElse(-1)
  }

  def getAllSkillsByUserId(userId: Int) = {
    val query = skillMatrixTable.filter(x => x.userId === userId)
    dbConfig.db.run(query.result)
  }

  def createSkill(userId: Int, skillId: Int, skillLevel: SkillLevel) = {
    val userSkillObject = SkillMatrix(
      userId = userId,
      skillId = skillId,
      skillLevel = skillLevel)
    dbConfig.db.run(skillMatrixTable returning skillMatrixTable += userSkillObject)
  }

  def updateSkill(id: Int, userId: Int, skillId: Int, skillLevel: SkillLevel) = {
    val userSkillObject = SkillMatrix(
      id = Some(id),
      userId = userId,
      skillId = skillId,
      skillLevel = skillLevel)
    val r = (skillMatrixTable returning skillMatrixTable).insertOrUpdate(userSkillObject)
    dbConfig.db.run(r)
  }

  def deleteSkillByUserId(userSkillId: Int) = {
    dbConfig.db.run(skillMatrixTable.filter(_.id === userSkillId).delete)
  }

  /*def getAllSkillsFromSkillMatrix() = {
    val query = skillMatrixTable.groupBy(_.skillId)
    dbConfig.db.run(query.result)

  }*/

  def getSkillById(skillId: Int) = {
    val query = skillMatrixTable.filter(x => x.skillId === skillId)
    dbConfig.db.run(query.result)
  }

}
