package services

import javax.inject.Inject

import common.DBConnection
import models._
import models.db.Users

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class UserService @Inject() (implicit val connection: DBConnection) {

  def getUserById(userId: Int): Future[Option[User]] = {
    Users.getUserById(userId)
  }

  def getUserByEmail(email: String): Future[Option[User]] = {
    Users.getUserByEmail(email)
  }

  def getAll: Future[Seq[User]] = {
    Users.getAllUsers
  }

  def getOrCreateUserByEmail(user: User): Future[Int] = {
    Users.getUserIdByEmail(user.email).flatMap {
      case Some(id: Int) =>
        Future.successful(id)
      case _ =>
        Users.add(user)
    }
  }

}
