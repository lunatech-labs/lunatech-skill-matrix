package services

import javax.inject.Inject

import common.DBConnection
import models._
import models.db.{Skills, Users}

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class UserService @Inject() (implicit val connection: DBConnection) {

  def getUserById(userId: Int): Future[Option[User]] = {
    Users.getUserById(userId)
  }

  def removeUser(userId: Int): Future[Int] = {
    for {
      result <- Users.remove(userId)
      _ <- Skills.deactivateByUserId(userId)
    } yield result
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
        val user = User(None, name, familyName, email, List(AccessLevel.Basic),Status.Active)
        Users.add(user)
        Future.successful(Some(user))
    }
  }

  def updateAccessLevels(user: User): Future[Int] = {
    Users.updateAccessLevels(user)
  }

  def batchUpdateAccessLevels(users: Seq[User]): Future[Int] = {
    Future.sequence(users.map {user =>
      updateAccessLevels(user)
    }).map(list => list.sum)
  }

  def activateUser(user: User): Future[Int] = {
    for {
      result <- Users.activateUser(user)
      _ <- Skills.activateByUserId(user.id.get)
    } yield result
  }

  def batchActivateUsers(users: Seq[User]): Future[Int] = {
    Future.sequence(users.map{ user=>
      activateUser(user)
    }).map{ list => list.sum }
  }

  def deactivateUser(user: User): Future[Int] = {
    for {
      result <- Users.deactivateUser(user)
      _ <- Skills.deactivateByUserId(user.id.get)
    } yield result
  }

  def batchDeactivateUser(users: Seq[User]): Future[Int] = {
    Future.sequence(users.map{ user =>
      deactivateUser(user)
    }).map{ list => list.sum }
  }

}
