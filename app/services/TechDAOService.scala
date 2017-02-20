package services

import javax.inject.Inject

import models._
import models.{MyTable, Tech}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent._

class TechDAOService @Inject()(dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val techTable = TableQuery[MyTable.Tech]

  def addTech(tech: Tech): Future[Int] = {
    dbConfig.db.run((techTable returning techTable.map(_.id)) += tech)
  }

  def createTech(name: String, techType: TechType): Future[Int] = {
    val tech = Tech(
      id = None,
      name = name.toLowerCase(),
      techType = techType
    )
    dbConfig.db.run((techTable returning techTable.map(_.id)) += tech)
  }

  def getTechIdByNameAndType(tech: Tech) : Future[Option[Int]] = {
    val getTechIdQuery = techTable.filter(x => x.name.toLowerCase === tech.name.toLowerCase() && x.techType === tech.techType).map(_.id).take(1)
    dbConfig.db.run(getTechIdQuery.result.headOption)
  }

  def getAllTech() = {
    dbConfig.db.run(techTable.result)
  }

  def getTechById(techId: Int): Future[Option[Tech]] = {
    val getByIdQuery = techTable.filter(_.id === techId)
    dbConfig.db.run(getByIdQuery.result.headOption)
  }

  def updateTech(techId: Int, tech: Tech) = {
    val r = (techTable returning techTable).insertOrUpdate(tech)
    dbConfig.db.run(r)
  }

}
