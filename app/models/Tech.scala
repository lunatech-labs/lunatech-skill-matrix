package models

import models._
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._
import scala.concurrent._
import ExecutionContext.Implicits.global

import slick.driver.PostgresDriver.api._


case class Tech(id: Option[Int], name: String, techType: TechType)

object Tech {
  implicit val techReads: Reads[Tech] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "techType").read[TechType]
    )(Tech.apply _)

  implicit val techWrites: Writes[Tech] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "techType").write[TechType]
    )(unlift(Tech.unapply _))
}

class Techs(tag: Tag) extends Table[models.Tech](tag, "tech") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("tech_name")
  def techType = column[TechType]("tech_type")
  def * =  (id.?, name, techType) <> ((models.Tech.apply _).tupled, models.Tech.unapply _)
}

object Techs{
  val techTable = TableQuery[Techs]

  def add(tech: Tech): Future[Int] = {
    Connection.db.run((techTable returning techTable.map(_.id)) += tech)
  }

  def add(name: String, techType: TechType): Future[Int] = {
    val tech = Tech(
      id = None,
      name = name.toLowerCase,
      techType = techType
    )
    add(tech)
  }

  def getTechIdByNameAndType(tech: Tech) : Future[Option[Int]] = {
    val getTechIdQuery = techTable.filter(t => t.name === tech.name.toLowerCase && t.techType === tech.techType).map(_.id).take(1)
    Connection.db.run(getTechIdQuery.result.headOption)
  }

  def getAllTech = {
    Connection.db.run(techTable.result)
  }

  def getTechById(techId: Int): Future[Option[Tech]] = {
    val getByIdQuery = techTable.filter(_.id === techId)
    Connection.db.run(getByIdQuery.result.headOption)
  }

  def updateTech(techId: Int, tech: Tech) = {
    val r = (techTable returning techTable).insertOrUpdate(tech)
    Connection.db.run(r)
  }
}
