package models

import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

/**
  * Created by tatianamoldovan on 05/02/2017.
  */
case class UserAuth(id: Option[Int] = None, userId: Int, key: String, secret: String)

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

