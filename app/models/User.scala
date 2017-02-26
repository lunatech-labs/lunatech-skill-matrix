package models

import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

import scala.concurrent._
import ExecutionContext.Implicits.global
import slick.driver.PostgresDriver.api._
import slick.lifted.{ProvenShape, TableQuery}


case class User(id: Option[Int] = None, firstName: String, lastName: String, email: String) {
  def fullName: String = {
    this.firstName + " " + this.lastName
  }
}

object User {
  implicit val userReads: Reads[User] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String]
    ) (User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "email").write[String]
    ) (unlift(User.unapply))
}

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def firstName: Rep[String] = column[String]("firstname")

  def lastName: Rep[String] = column[String]("lastname")

  def email: Rep[String] = column[String]("email")

  def * : ProvenShape[User] = (id.?, firstName, lastName, email) <> ((User.apply _).tupled, User.unapply)
}

object Users {
  val userTable: TableQuery[Users] = TableQuery[Users]

  def getUserById(userId: Int): Future[Option[User]] = {
    exists(userId).flatMap {
      case true =>
        val query = userTable.filter(x => x.id === userId)
        Connection.db.run(query.result.headOption)
      case false => Future(None)
    }
  }


  def getAllUsers: Future[Seq[User]] = {
    Connection.db.run(userTable.result)
  }

  def exists(id: Int): Future[Boolean] = {
    Connection.db.run(userTable.filter(_.id === id).exists.result)
  }
}


