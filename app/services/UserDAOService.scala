package services

import javax.inject.Inject

import models.MyTable
import models.User
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent._
import ExecutionContext.Implicits.global

class UserDAOService @Inject() (dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val userTable = TableQuery[MyTable.Users]

  def getUserById(userId: Int): Future[Option[User]] = {
    exists(userId).flatMap {
      case true =>
        val query = userTable.filter(x => x.id === userId)
        dbConfig.db.run(query.result.headOption)
      case false => Future(None)
    }
  }

  def getAllUsers() = {
    dbConfig.db.run(userTable.result)
  }

  def exists(id: Int): Future[Boolean] = {
    dbConfig.db.run(userTable.filter(_.id === id).exists.result)
  }
}
