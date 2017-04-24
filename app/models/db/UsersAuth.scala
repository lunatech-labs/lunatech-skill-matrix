package models.db

import models.{User, UserAuth}
import slick.driver.PostgresDriver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}

class UsersAuth(tag: Tag) extends Table[UserAuth](tag, "user_auth") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId: Rep[Int] = column[Int]("user_id")
  def key: Rep[String] = column[String]("user_key")
  def secret: Rep[String] = column[String]("secret")
  def * : ProvenShape[UserAuth] = (id.?, userId, key, secret) <> ((UserAuth.apply _).tupled, UserAuth.unapply)

  def user: ForeignKeyQuery[Users, User] = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)
}
