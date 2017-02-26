package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes}
import slick.driver.PostgresDriver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}


case class UserAuth(id: Option[Int] = None, userId: Int, key: String, secret: String)

class UsersAuth(tag: Tag) extends Table[UserAuth](tag, "user_auth") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId: Rep[Int] = column[Int]("user_id")
  def key: Rep[String] = column[String]("user_key")
  def secret: Rep[String] = column[String]("secret")
  def * : ProvenShape[UserAuth] = (id.?, userId, key, secret) <> ((UserAuth.apply _).tupled, UserAuth.unapply)

  def user: ForeignKeyQuery[Users, User] = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)
}

object UserAuth {
  implicit val userReads: Reads[UserAuth] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "userId").read[Int] and
      (JsPath \ "key").read[String] and
      (JsPath \ "secret").read[String]
    )(UserAuth.apply _)

  implicit val userWrites: Writes[UserAuth] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "userId").write[Int] and
      (JsPath \ "key").write[String] and
      (JsPath \ "secret").write[String]
    )(unlift(UserAuth.unapply))
}

