package models.db

import common.DBConnection
import models.User
import slick.driver.PostgresDriver.api._
import slick.lifted.{ProvenShape, TableQuery}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def firstName: Rep[String] = column[String]("firstname")

  def lastName: Rep[String] = column[String]("lastname")

  def email: Rep[String] = column[String]("email")

  def * : ProvenShape[User] = (id.?, firstName, lastName, email) <> ((User.apply _).tupled, User.unapply)
}

object Users {
  val userTable: TableQuery[Users] = TableQuery[Users]

  def getUserById(userId: Int)(implicit connection: DBConnection): Future[Option[User]] = {
    exists(userId).flatMap {
      case true =>
        val query = userTable.filter(x => x.id === userId)
        connection.db.run(query.result.headOption)
      case false => Future(None)
    }
  }

  def getUserByEmail(email: String)(implicit connection: DBConnection): Future[Option[User]] = {
    val query = userTable.filter(_.email === email)
    connection.db.run(query.result.headOption)
  }

  def getUserIdByEmail(email: String)(implicit connection: DBConnection): Future[Option[Int]] = {
    val query = userTable.filter(_.email === email).map(_.id)
    connection.db.run(query.result.headOption)
  }

  def getAllUsers(implicit connection: DBConnection): Future[Seq[User]] = {
    connection.db.run(userTable.result)
  }

  def exists(id: Int)(implicit connection: DBConnection): Future[Boolean] = {
    connection.db.run(userTable.filter(_.id === id).exists.result)
  }

  def add(user: User)(implicit connection: DBConnection): Future[Int] = {
    val query = userTable returning userTable.map(_.id) += user
    connection.db.run(query)
  }
}


