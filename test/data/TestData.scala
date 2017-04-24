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

  val userNotFound = "User not found"
  val skillNotFound = "Skill not found"
  val techNotFound = "Tech not found"

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

  val getSkillMatrixByTechIdResponseJson: JsValue = Json.parse(
    """
      |{
      |    "techId": 1,
      |    "techName": "dark arts",
      |    "techType": "CONCEPT",
      |    "users": [
      |      {
      |        "userName": "Severus Snape",
      |        "level": "EXPERT"
      |      }
      |    ]
      |}
    """.stripMargin)


  val skillMatrixResultTechScala: JsValue = Json.parse(
    """
      |{
      |      "techId": 7,
      |      "techName": "scala",
      |      "techType": "LANGUAGE",
      |      "users": [
      |        {
      |          "userName": "Martin Odersky",
      |          "level": "EXPERT"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val skillMatrixResultTechFunctional: JsValue = Json.parse(
    """
      |{
      |      "techId": 1,
      |      "techName": "functional programming",
      |      "techType": "CONCEPT",
      |      "users": [
      |        {
      |          "userName": "Martin Odersky",
      |          "level": "EXPERT"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val skillMatrixResultTechDefense: JsValue = Json.parse(
    """
      |{
      |      "techId": 1,
      |      "techName": "defense against the dark arts",
      |      "techType": "CONCEPT",
      |      "users": [
      |        {
      |          "userName": "Severus Snape",
      |          "level": "EXPERT"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val skillMatrixResultTechDarkArts: JsValue = Json.parse(
    """
      |{
      |      "techId": 1,
      |      "techName": "dark arts",
      |      "techType": "CONCEPT",
      |      "users": [
      |        {
      |          "userName": "Severus Snape",
      |          "level": "EXPERT"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val allTechScala: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "scala",
      |      "techType": "LANGUAGE"
      |}
    """.stripMargin)

  val allTechFunctional: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "functional programming",
      |      "techType": "CONCEPT"
      |}
    """.stripMargin)

  val allTechDefense: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "defense against the dark arts",
      |      "techType": "CONCEPT"
      |}
    """.stripMargin)

  val allTechDarkArts: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "dark arts",
      |      "techType": "CONCEPT"
      |}
    """.stripMargin)

  val getUserByIdResponse: JsValue = Json.parse(
    """
      |{
      |   "id": 4,
      |   "firstName": "Severus",
      |   "lastName": "Snape",
      |   "email": "severus.snape@hogwarts.com"
      |}
    """.stripMargin)

  val genericPathMissing = "error.path.missing"
  val genericEnumValueWrong = "Value is not in the list"
  val objTechMissing = "obj.tech"
  val objSkillLevelWrong = "obj.skillLevel"
  val objTechNameMissing = "obj.tech.name"
  val objTechTypeWrong = "obj.tech.techType"
  val objTechIdMissing = "obj.tech.id"

}
