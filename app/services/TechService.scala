package services

import javax.inject.Inject

import common.DBConnection
import models.{Tech, Techs}

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
    Techs.getTechIdByNameAndType(tech).flatMap {
      case Some(id) => Future(id)
      case None => Techs.add(tech)
    }
  }

  def getById(techId: Int): Future[Option[Tech]] = {
    Techs.getTechById(techId)
  }
}
