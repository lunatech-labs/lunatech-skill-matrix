package data

import play.api.libs.json.{JsValue, _}

object TestData {

  val ID_USER_SAYED_ALI = "idUserSayedAli"
  val ID_USER_ODERSKY = "idUserOdersky"
  val ID_USER_SNAPE = "idUserSnape"

  val ID_TECH_SCALA = "idTechScala"
  val ID_TECH_FUNCTIONAL = "idTechFunctional"
  val ID_TECH_DEFENSE = "idTechDefense"
  val ID_TECH_DARK_ARTS = "idTechDarkArts"

  val SKILL_ODERSKY_SCALA = "skillOderskyScala"
  val SKILL_ODERSKY_FUNCTIONAL = "skillOderskyFunctional"
  val SKILL_SEVERUS_DEFENSE = "skillSeverusDefense"
  val SKILL_SEVERUS_DARK_ARTS = "skillSeverusDarkArts"

  val X_AUTH_TOKEN = "X-AUTH-TOKEN"

  val nonExistentId = 4242

  val addSkillRequestJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "brainfuck",
      |		"techType": "LANGUAGE"
      |	},
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

  val addSkillRequestWithMissingTechJson: JsValue = Json.parse(
    """
      |{
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

  val addSkillRequestWithMissingSkillLevelJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "brainfuck",
      |		"techType": "LANGUAGE"
      |	}
      |}
    """.stripMargin)

  val addSkillRequestWithMissingTechNameJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"techType": "LANGUAGE"
      |	},
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

  val addSkillRequestWithMissingTechTypeJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "brainfuck"
      |	},
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

  val putSkillRequestJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"id": 5,
      |		"name": "scala",
      |		"techType": "LANGUAGE"
      |		},
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechJson: JsValue = Json.parse(
    """
      |{
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingSkillLevelJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"id": 5,
      |		"name": "scala",
      |		"techType": "LANGUAGE"
      |		}
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechNameJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"id": 5,
      |		"techType": "LANGUAGE"
      |		},
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechTypeJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"id": 5,
      |		"name": "scala"
      |		},
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechIdJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "scala",
      |		"techType": "LANGUAGE"
      |		},
      |	"skillLevel": "NOVICE"
      |}
    """.stripMargin)

}
