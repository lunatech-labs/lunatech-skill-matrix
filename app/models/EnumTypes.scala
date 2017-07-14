package models

import play.api.libs.json._
import slick.driver.PostgresDriver.api._

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
  case object All        extends AccessLevel
  case object Management extends AccessLevel
  case object Admin      extends AccessLevel

  def apply(accessLevel: String): AccessLevel = accessLevel match {
    case "All"        => All
    case "Management" => Management
    case "Admin"      => Admin
    case _            => All
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

  implicit val accessLevelMappedColumn = MappedColumnType.base[AccessLevel, String](
    e => e.toString,
    s => AccessLevel(s)
  )

  def isAccessible(userLevel:AccessLevel,accessLevel:AccessLevel):Boolean = {
    userLevel match {
      case Admin => true
      case Management => accessLevel == Management || accessLevel == All
      case All => accessLevel == All
      case _ => false
    }
  }

}