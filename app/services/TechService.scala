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
    getTechIdByNameAndType(tech).flatMap {
      case Some(id) => Future(id)
      case None => Techs.add(tech)
    }
  }

  def getTechIdByNameAndType(tech: Tech): Future[Option[Int]] = {
    Techs.getTechIdByNameAndType(tech)
  }

  def getById(techId: Int): Future[Option[Tech]] = {
    Techs.getTechById(techId)
  }
}
