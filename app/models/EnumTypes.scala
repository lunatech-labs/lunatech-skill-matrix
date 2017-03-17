package models

import play.api.libs.json._
import slick.driver.PostgresDriver.api._

sealed trait SkillLevel

object SkillLevel {
  def apply(skillLevel: String): SkillLevel = {
    skillLevel match {
      case "EXPERT" => EXPERT
      case "INTERMEDIATE" => INTERMEDIATE
      case "NOVICE" => NOVICE
      case "LONG_TIME_AGO" => LONG_TIME_AGO
    }

  }

  implicit val skillLevelFormat = new Format[SkillLevel] {
    def reads(json: JsValue): JsResult[SkillLevel] = json match {
      case JsString(s) =>
        try {
          JsSuccess(SkillLevel(json.as[String].value.toString))
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

  case object INTERMEDIATE extends SkillLevel

  case object NOVICE extends SkillLevel

  case object LONG_TIME_AGO extends SkillLevel

}


sealed trait TechType

object TechType {

  def apply(techType: String): TechType = {
    techType match {
      case "LANGUAGE" => LANGUAGE
      case "LIBRARY" => LIBRARY
      case "FRAMEWORK" => FRAMEWORK
      case "CONCEPTUAL" => CONCEPTUAL
    }
  }

  implicit val techTypeFormat = new Format[TechType] {
    def reads(json: JsValue): JsResult[TechType] = json match {
      case JsString(s) =>
        try {
          JsSuccess(TechType(json.as[String].value.toString))
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

  case object CONCEPTUAL extends TechType

}