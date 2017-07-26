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

  def searchUsers(filters:Seq[TechFilter]):Future[Seq[User]] = {
    Users.searchUsers(filters)
  }

  def getOrCreateUserByEmail(name: String, familyName: String, email: String): Future[Option[User]] = {
    Users.getUserByEmail(email).flatMap {
      case Some(user: User) =>
        Future.successful(Some(user))
      case _ =>
        val user = User(None, name, familyName, email, AccessLevel.Basic)
        Users.add(user)
        Future.successful(Some(user))
    }
  }

}
