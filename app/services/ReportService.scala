package services

import javax.inject.Inject

import common.DBConnection
import models._
import models.db.{Entries, Skills}

import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global

class ReportService @Inject()(
                               peopleAPIService: PeopleAPIService,
                               userService: UserService)(implicit val connection: DBConnection) {

  def lastUpdateReport: Future[Seq[UserLastSkillUpdates]] = {
    for {
      people <- peopleAPIService.getAllPeople
      updates = people.map(_.email).map(lastSkillUpdate)
      report <- Future.sequence(updates)
    } yield report
  }

  def lastSkillUpdate(email: String): Future[UserLastSkillUpdates] = {
    for {
      user <- userService.getUserByEmail(email)
      updates <- user match {
        case Some(u) =>
          for {
            entriesDB <- Entries.getByUser(u.id)
            grouped = entriesDB.groupBy(_.skillId)
            latest = grouped.values.map { seq =>
              seq.sortWith((e1, e2) => e1.occurrence.isAfter(e2.occurrence)).head
            }.toSeq
            entriesWithTech = latest.map { e =>
              Skills.getSkill(e.skillId).map {
                case Some((_, _, tech: Tech)) => (e, tech.name)
                case None => (e, "Tech not found")
              }
            }
            correctedEntriesWithTech <- Future.sequence(entriesWithTech)
            entriesReport <- Future(correctedEntriesWithTech.map(e => LastUpdateSkill(e._2, e._1.entryAction, e._1.occurrence)))
          } yield UserLastSkillUpdates(u.fullName, entriesReport)
        case None => Future(UserLastSkillUpdates(email, Nil))
      }
    } yield updates
  }
}

object ReportService {

  //FIXME move to more appropriate place
  object MissingUser extends Throwable

}
