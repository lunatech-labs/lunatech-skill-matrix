package services

import javax.inject.Inject

import common.DBConnection
import models.Tech
import models.db.Techs

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class TechService @Inject() (implicit val connection: DBConnection)  {

  def getAllTech: Future[Seq[Tech]] = {
    Techs.getAllTech
  }

  def search(query: String): Future[Seq[Tech]] = {
    Techs.search(query)
  }

  def getTechIdOrInsert(tech: Tech): Future[Int] = {
    getTechIdByName(tech).flatMap {
      case Some(id) => Future(id)
      case None => Techs.add(tech)
    }
  }

  def getTechIdByName(tech: Tech): Future[Option[Int]] = {
    Techs.getTechIdByName(tech)
  }

  def getById(techId: Int): Future[Option[Tech]] = {
    Techs.getTechById(techId)
  }

  /**
    * Updates the tech type and/or name
    * When updating tech name the function first checks if the new name exists in the database
    * If yes - it will return Future(None), otherwise the name will be updated
    * Future(None) means that 0 rows were updated
    * @param techId
    * @param tech
    * @return The number of rows updated
    */
  def updateTech(techId: Int, tech: Tech): Future[Option[Int]] = {
    getTechIdByName(tech).flatMap {
      case Some(id) if id == techId => update(techId, tech) // updating tech type
      case Some(id) => Future(None)                         // error when updating name when the new name is in the database
      case None => update(techId, tech)                     // updating tech name
    }
  }

  def removeTech(techId: Int): Future[Option[Int]] = {
    Techs.removeTech(techId).map {
      case 0 => None
      case nrOfRows@_ => Some(nrOfRows)
    }
  }

  private def update(techId: Int, tech: Tech): Future[Option[Int]] = Techs.updateTech(techId, tech: Tech).map {
      case 0 => None
      case nrOfRows@_ => Some(nrOfRows)
  }
}
