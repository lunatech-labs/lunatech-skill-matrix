package models

import models.db.CustomPostgresProfile.api._
import play.api.libs.json._

sealed trait SkillLevel


object SkillLevel {
  def apply(skillLevel: String): SkillLevel = skillLevel match {
    case "EXPERT" => EXPERT
    case "PROFICIENT" => PROFICIENT
    case "COMPETENT" => COMPETENT
    case "ADVANCED_BEGINNER" => ADVANCED_BEGINNER
    case "NOVICE" => NOVICE
  }

  implicit val skillLevelFormat: Format[SkillLevel] = new Format[SkillLevel] {
    def reads(json: JsValue): JsResult[SkillLevel] = json match {
      case JsString(s) =>
        try {
          JsSuccess(SkillLevel(json.as[String]))
        } catch {
          case _: scala.MatchError => JsError("Value is not in the list")
        }
      case _ => JsError("String values are expected")
    }

    def writes(skillLevel: SkillLevel) = JsString(skillLevel.toString)

  }

  implicit val skillLevel = MappedColumnType.base[SkillLevel, String](
    e => e.toString,
    s => SkillLevel(s)
  )

  case object EXPERT extends SkillLevel

  case object PROFICIENT extends SkillLevel

  case object COMPETENT extends SkillLevel

  case object ADVANCED_BEGINNER extends SkillLevel

  case object NOVICE extends SkillLevel


  val orderingList = List(NOVICE, ADVANCED_BEGINNER, COMPETENT, PROFICIENT, EXPERT)
}


sealed trait TechType

object TechType {

  def apply(techType: String): TechType = techType match {
    case "LANGUAGE" => LANGUAGE
    case "LIBRARY" => LIBRARY
    case "FRAMEWORK" => FRAMEWORK
    case "CONCEPT" => CONCEPT
    case "DATABASE" => DATABASE
    case "OTHER" => OTHER
  }

  implicit val techTypeFormat: Format[TechType] = new Format[TechType] {
    def reads(json: JsValue): JsResult[TechType] = json match {
      case JsString(s) =>
        try {
          JsSuccess(TechType(json.as[String]))
        } catch {
          case _: scala.MatchError => JsError("Value is not in the list")
        }
      case _ => JsError("String values are expected")
    }


    def writes(techType: TechType) = JsString(techType.toString)

  }

  implicit val techType = MappedColumnType.base[TechType, String](
    e => e.toString,
    s => TechType(s)
  )

  case object LANGUAGE extends TechType

  case object LIBRARY extends TechType

  case object FRAMEWORK extends TechType

  case object CONCEPT extends TechType

  case object DATABASE extends TechType

  case object OTHER extends TechType

}

sealed trait AccessLevel

object AccessLevel {

  case object Basic               extends AccessLevel
  case object Developer           extends AccessLevel
  case object Management          extends AccessLevel
  case object Admin               extends AccessLevel
  case object Administrative      extends AccessLevel
  case object Office              extends AccessLevel
  case object CEO                 extends AccessLevel

  def apply(accessLevel: String): AccessLevel = accessLevel match {
    case "Basic"          => Basic
    case "Management"     => Management
    case "Developer"      => Developer
    case "Admin"          => Admin
    case "Administrative" => Administrative
    case "Office"         => Office
    case "CEO"            => CEO
    case _                => Basic
  }

  implicit val accessLevelFormat: Format[AccessLevel] = new Format[AccessLevel] {
    def reads(json: JsValue): JsResult[AccessLevel] = json match {
      case JsString(s) =>
        try {
          JsSuccess(AccessLevel(json.as[String]))
        } catch {
          case _: scala.MatchError => JsError("Value is not in the list")
        }
      case _ => JsError("String values are expected")
    }

    def writes(accessLevel: AccessLevel) = JsString(accessLevel.toString)
  }

  def isAccessible(userLevels: List[AccessLevel], accessLevel: AccessLevel): Boolean = accessLevel match {
    case AccessLevel.Basic => true
    case _ => userLevels.contains(accessLevel) || userLevels.contains(Admin)
  }

}


sealed trait Operation

object Operation {

  case object Equal extends Operation {
    override def toString = "EQUAL"
  }

  case object GreaterThanOrEqual extends Operation {
    override def toString = "GTE"
  }

  case object LowerThanOrEqual extends Operation {
    override def toString = "LTE"
  }

  case object Any extends Operation {
    override def toString = "ANY"
  }

  def apply(accessLevel: String): Operation = accessLevel match {
    case "EQUAL" => Equal
    case "GTE" => GreaterThanOrEqual
    case "LTE" => LowerThanOrEqual
    case "ANY" => Any
  }

  implicit val operationFormat: Format[Operation] = new Format[Operation] {
    def reads(json: JsValue): JsResult[Operation] = json match {
      case JsString(_) =>
        try {
          JsSuccess(Operation(json.as[String]))
        } catch {
          case _: scala.MatchError => JsError("Value is not in the list")
        }
      case _ => JsError("String values are expected")
    }


    def writes(operation: Operation) = JsString(operation.toString)
  }
}


sealed trait Status {
}

object Status {

  case object Active extends Status

  case object Inactive extends Status

  def apply(value: String): Status = value match {
    case "Active" => Active
    case "Inactive" => Inactive
  }

  implicit val statusMappedColumn = MappedColumnType.base[Status, String](
    e => e.toString,
    s => Status(s)
  )

  implicit val statusFormat: Format[Status] = new Format[Status] {
    def reads(json: JsValue): JsResult[Status] = json match {
      case JsBoolean(_) =>
        try {
          JsSuccess(Status(json.as[String]))
        } catch {
          case _: scala.MatchError => JsError("Value is not in the list")
        }
      case _ => JsError("String values are expected")
    }


    def writes(status: Status) = JsString(status.toString)
  }
}


sealed trait EntryAction

object EntryAction {

  def addInfo(skillLevel: SkillLevel): String = s"Skill added with level $skillLevel"

  def updateInfo(oldSkillLevel: Option[SkillLevel], newSkillLevel: SkillLevel): String = oldSkillLevel.fold("")(osk => s"Skill updated from level $osk to $newSkillLevel")

  def removeInfo(skillLevel: Option[SkillLevel]): String = skillLevel.fold("Unable to find skill when creating entry")(sk => s"Skill removed with level $sk")

  case object Add extends EntryAction

  case object Remove extends EntryAction

  case object Update extends EntryAction

  def apply(value: String): EntryAction = value match {
    case "Add" => Add
    case "Remove" => Remove
    case "Update" => Update
  }

  implicit val statusMappedColumn = MappedColumnType.base[EntryAction, String](
    e => e.toString,
    s => EntryAction(s)
  )

  implicit val entryActionFormat: Format[EntryAction] = new Format[EntryAction] {
    def reads(json: JsValue): JsResult[EntryAction] = json match {
      case JsBoolean(_) =>
        try {
          JsSuccess(EntryAction(json.as[String]))
        } catch {
          case _: scala.MatchError => JsError("Value is not in the list")
        }
      case _ => JsError("String values are expected")
    }


    def writes(entryAction: EntryAction) = JsString(entryAction.toString)
  }
}