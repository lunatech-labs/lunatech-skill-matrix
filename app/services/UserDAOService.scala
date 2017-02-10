package services

import javax.inject.Inject

import models.MyTable
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider
/**
  * Created by tatianamoldovan on 10/02/2017.
  */
class UserDAOService @Inject() (dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val userTable = TableQuery[MyTable.Users]

  def getUserById(userId: Int) = {
    val query = userTable.filter(x => x.id === userId)
    dbConfig.db.run(query.result)
  }
}
