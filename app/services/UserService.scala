package services

import models._

import scala.concurrent._

class UserService() {

  def getUserById(userId: Int): Future[Option[User]] = {
    Users.getUserById(userId)
  }

  def getAll: Future[Seq[User]] = {
    Users.getAllUsers
  }


}
