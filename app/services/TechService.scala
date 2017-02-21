package services

import models.{Tech, Techs}

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class TechService() {

  def getAllTech: Future[Seq[Tech]] = {
    Techs.getAllTech
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
