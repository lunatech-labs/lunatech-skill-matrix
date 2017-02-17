package models

import play.api.libs.json.{Format, JsString, JsSuccess, JsValue}
import slick.driver.PostgresDriver.api._


object EnumTypes {

  object SkillLevel extends Enumeration {
    type SkillLevel = Value
    val CAN_TEACH = Value("CAN_TEACH")
    val COMFORTABLE = Value("COMFORTABLE")
    val DABBED = Value("DABBED")
    val FOSSIL = Value("FOSSIL")

    implicit val skillLevelFormat = new Format[SkillLevel] {
      def reads(json: JsValue) = JsSuccess(SkillLevel.withName(json.as[String].value))
      def writes(skillLevel: SkillLevel) = JsString(skillLevel.toString)

    }

    implicit val skillLevel = MappedColumnType.base[SkillLevel, String](
      e => e.toString,
      s => SkillLevel.withName(s)
    )
  }

  object TechType extends Enumeration {
    type TechType = Value
    val LANGUAGE = Value("LANGUAGE")
    val LIBRARY = Value("LIBRARY")
    val FRAMEWORK = Value("FRAMEWORK")
    val CONCEPTUAL = Value("CONCEPTUAL")

    implicit val techTypeFormat = new Format[TechType] {
      def reads(json: JsValue) = JsSuccess(TechType.withName(json.as[String].value))
      def writes(techType: TechType) = JsString(techType.toString)

    }

    implicit val techType = MappedColumnType.base[TechType, String](
      e => e.toString,
      s => TechType.withName(s)
    )
  }
}

