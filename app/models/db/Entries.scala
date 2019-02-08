package models.db


import com.typesafe.scalalogging.LazyLogging
import common.DBConnection
import models._
import org.joda.time.DateTime
import slick.lifted.{ForeignKeyQuery, ProvenShape, TableQuery}
import CustomPostgresProfile.api._

import scala.concurrent.Future

class Entries(tag: Tag) extends Table[models.Entry](tag, "entries") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def userId: Rep[Int] = column[Int]("user_id")

  def skillId: Rep[Int] = column[Int]("skill_id")

  def entryAction: Rep[EntryAction] = column[EntryAction]("entry_action")

  def occurrence: Rep[DateTime] = column[DateTime]("occurrence")

  def info: Rep[String] = column[String]("info")

  def * : ProvenShape[Entry] = (id.?, userId, skillId, entryAction, occurrence, info) <> ((models.Entry.apply _).tupled, models.Entry.unapply _)

  def user: ForeignKeyQuery[Users, User] = foreignKey("ENTRY_USER_FK", userId, TableQuery[Users])(_.id, onDelete = ForeignKeyAction.Cascade)

  def skill: ForeignKeyQuery[Skills, Skill] = foreignKey("ENTRY_SKILL_FK", skillId, TableQuery[Skills])(_.id, onDelete = ForeignKeyAction.Cascade)
}


object Entries extends LazyLogging {
  val entryActionsTable: TableQuery[Entries] = TableQuery[Entries]

  def add(userId: Int, skillId: Int, entryAction: EntryAction, info: String)(implicit connection: DBConnection): Future[Int] = {
    val entry = Entry(userId = userId, skillId = skillId, entryAction = entryAction, occurrence = DateTime.now(), info = info)
    val query = entryActionsTable returning entryActionsTable.map(_.id) += entry
    connection.db.run(query)
  }

  def getByUser(userId: Option[Int])(implicit connection: DBConnection): Future[Seq[Entry]] = {
    val query = entryActionsTable.filter(_.userId === userId)
    connection.db.run(query.result)
  }
}