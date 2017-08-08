package models.db

import com.typesafe.scalalogging.LazyLogging
import common.DBConnection
import models._
import slick.driver.PostgresDriver.api._
import slick.lifted.{ProvenShape, TableQuery}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def firstName: Rep[String] = column[String]("firstname")

  def lastName: Rep[String] = column[String]("lastname")

  def email: Rep[String] = column[String]("email")

  def accessLevel: Rep[AccessLevel] = column[AccessLevel]("accesslevel")

  def status: Rep[Status] = column[Status]("status")

  def * : ProvenShape[User] = (id.?, firstName, lastName, email, accessLevel, status) <> ((User.apply _).tupled, User.unapply)
}

object Users extends LazyLogging {
  val userTable: TableQuery[Users] = TableQuery[Users]

  def getUserById(userId: Int)(implicit connection: DBConnection): Future[Option[User]] = {
    exists(userId).flatMap {
      case true =>
        val query = userTable.filter(_.id === userId)
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
    logger.info("add new user {}", user)
    val query = userTable returning userTable.map(_.id) += user
    connection.db.run(query)
  }

  def remove(userId:Int)(implicit connection: DBConnection): Future[Int] = {
    val query = for {
      user <- userTable.filter(_.id === userId)
    } yield user.status

    connection.db.run(query.update(Status.Inactive))
  }

  def searchUsers(filters:Seq[TechFilter])(implicit connection: DBConnection):Future[Seq[User]] = {
    Skills.getAllSkills.map{ skills =>
      val userSkills = skills.groupBy(_._2)
      (for {
        (user, skills) <- userSkills
      } yield {
        if(validateFilters(filters,skills)) Some(user)
        else None
      }).toSeq.flatten
    }
  }

  private def validateFilters(filters:Seq[TechFilter], skills:Seq[(Skill,User,Tech)]):Boolean = {
    filters.map{ filter =>
      skills.map{
        case (skill,_,tech) => filter.validate(skill,tech)
      }.fold(false)( (a1,acc) => a1 || acc )
    }.fold(true)( (a1,acc) => a1 && acc )
  }
}


