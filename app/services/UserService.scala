package services

import javax.inject.Inject

import common.DBConnection
import models._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class UserService @Inject() (implicit val connection: DBConnection) {

  def getUserById(userId: Int): Future[Option[User]] = {
    Users.getUserById(userId)
  }

  def getUserByEmail(email: String): Future[Option[User]] = {
    Users.getUserByEmail(email)
  }

  def getUserByGoogleId(googleId: String): Future[Option[User]] = {
    Users.getUserByGoogleId(googleId)
  }

  def getAll: Future[Seq[User]] = {
    Users.getAllUsers
  }

  def getOrCreateUserByEmail(user: User): Future[User] = {
    Users.getUserByEmail(user.email).flatMap {
      case Some(user: User) =>
        Future.successful(user)
      case _ =>
        Users.add(user)
    }
  }

}
