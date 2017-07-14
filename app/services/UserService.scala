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

  def removeUser(userId: Int): Future[Int] = {
    Users.remove(userId)
  }

  def getUserByEmail(email: String): Future[Option[User]] = {
    Users.getUserByEmail(email)
  }

  def getAll: Future[Seq[User]] = {
    Users.getAllUsers
  }

  def getOrCreateUserByEmail(name: String, familyName: String, email: String): Future[Int] = {
    Users.getUserIdByEmail(email).flatMap {
      case Some(id: Int) =>
        Future.successful(id)
      case _ =>
        Users.add(User(None, name, familyName, email, AccessLevel.Basic))
    }
  }

}
