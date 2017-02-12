package models

import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

case class User(id: Option[Int] = None, firstName: String, lastName: String, email: String)

object User {
  implicit val userReads: Reads[User] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String]
    )(User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "email").write[String]
    )(unlift(User.unapply _))
}

