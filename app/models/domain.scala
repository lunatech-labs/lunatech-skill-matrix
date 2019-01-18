package models

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(id: Option[Int] = None, firstName: String, lastName: String, email: String, accessLevels: List[AccessLevel], status:Status) {
  require(email != null && !email.isEmpty, "Email field shouldn't be empty")

  def fullName: String = {
    this.firstName + " " + this.lastName
  }

  def getUserId: Int = {
    id.getOrElse(throw new Exception("User id is not defined!"))
  }
}

case class Tech(id: Option[Int], name: String, label: String, techType: TechType)

case class Skill(id: Option[Int] = None, userId: Int, techId: Int, skillLevel: SkillLevel, status: Status)

case class Entry(id: Option[Int] = None, userId: Int, skillId: Int, entryAction:EntryAction, occurrence: DateTime)

case class SkillMatrixItem(tech: Tech, skillLevel: SkillLevel, id: Option[Int])

case class SkillMatrixUsersAndLevel(fullName: String, level: SkillLevel)

case class SkillMatrixResponse(techId: Int, techName: String, labelName: String, techType: TechType, users: Seq[SkillMatrixUsersAndLevel])

case class UserSkillResponse(userId: Int, firstName: String, lastName: String, skills: Seq[SkillMatrixItem])

case class UserLastSkillUpdates(name:String,entries: Seq[LastUpdateSkill])

case class LastUpdateSkill(tech:String,entryAction:EntryAction,occurrence: DateTime)

final case class Person(email: String, managers: Seq[String], roles: Seq[String])

case class TechFilter(tech:String,operation:Operation,level:Option[SkillLevel]){
  def validate(skill: Skill,tech: Tech):Boolean = {
    if (this.tech == tech.name) {
      this.operation match {
        case Operation.Equal => this.level.map( _ == skill.skillLevel).getOrElse(false)
        case Operation.GreaterThanOrEqual =>
          this.level.map(SkillLevel.orderingList.indexOf(_)  <= SkillLevel.orderingList.indexOf(skill.skillLevel)).getOrElse(false)
        case Operation.LowerThanOrEqual =>
          this.level.map(SkillLevel.orderingList.indexOf(_)  >= SkillLevel.orderingList.indexOf(skill.skillLevel)).getOrElse(false)
        case Operation.Any => true
      }
    }else{
      false
    }
  }
}

case class UserSchedulerAudit(id: Option[Int] = None, createdAt: DateTime, status: String, body: JsValue)

object ImplicitFormats {

  val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSZ")

  implicit val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
    def writes(date: DateTime): JsValue = JsString(date.toString(dateFormatter))
  }

  implicit val jodaDateReads: Reads[DateTime] = Reads[DateTime](js =>
    js.validate[String].map[DateTime] { dtString =>
      DateTime.parse(dtString, dateFormatter)
    })

  implicit val userFormat: Format[User] = Json.format[User]
  implicit val skillFormat :Format[Tech] = Json.format[Tech]
  implicit val userSkillFormat: Format[Skill] = Json.format[Skill]
  implicit val skillMatrixItemFormat: Format[SkillMatrixItem] = Json.format[SkillMatrixItem]
  implicit val skillMatrixUsersAndLevelFormat: Format[SkillMatrixUsersAndLevel] = Json.format[SkillMatrixUsersAndLevel]
  implicit val skillMatrixResponseFormat: Format[SkillMatrixResponse] = Json.format[SkillMatrixResponse]
  implicit val userSkillResponseFormat: Format[UserSkillResponse] = Json.format[UserSkillResponse]
  implicit val techFilterFormat: Format[TechFilter] = Json.format[TechFilter]
  implicit val userSchedulerAuditFilterFormat: Format[UserSchedulerAudit] = Json.format[UserSchedulerAudit]
  implicit val personFormat: Format[Person] = Json.format[Person]
  implicit val lastUpdateSkillFormat: Format[LastUpdateSkill] = Json.format[LastUpdateSkill]
  implicit val userLastSkillUpdatesFormat: Format[UserLastSkillUpdates] = Json.format[UserLastSkillUpdates]

}

