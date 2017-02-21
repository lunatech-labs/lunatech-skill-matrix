package models

import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._
import scala.concurrent._
import ExecutionContext.Implicits.global

import slick.driver.PostgresDriver.api._


case class UserAuth(id: Option[Int] = None, userId: Int, key: String, secret: String)

class UsersAuth(tag: Tag) extends Table[UserAuth](tag, "user_auth") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("user_id")
  def key = column[String]("user_key")
  def secret = column[String]("secret")
  def * = (id.?, userId, key, secret) <> ((UserAuth.apply _).tupled, UserAuth.unapply _)

  def user = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)
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
    )(unlift(UserAuth.unapply _))
}

