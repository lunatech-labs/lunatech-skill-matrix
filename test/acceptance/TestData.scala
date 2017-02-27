package acceptance

import play.api.libs.json.{JsValue, _}

object TestData {

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

  val errorUserNotFound = "User not found"
  val skillNotFound = "Skill for this user could not be found"

  val addSkillRequestJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "brainfuck",
      |		"techType": "LANGUAGE"
      |	},
      |	"skillLevel": "DABBLED"
      |}
    """.stripMargin)

  val addSkillRequestWithMissingTechJson: JsValue = Json.parse(
    """
      |{
      |	"skillLevel": "DABBLED"
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
      |	"skillLevel": "DABBLED"
      |}
    """.stripMargin)

  val addSkillRequestWithMissingTechTypeJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "brainfuck"
      |	},
      |	"skillLevel": "DABBLED"
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
      |	"skillLevel": "DABBLED"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechJson: JsValue = Json.parse(
    """
      |{
      |	"skillLevel": "DABBLED"
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
      |	"skillLevel": "DABBLED"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechTypeJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"id": 5,
      |		"name": "scala"
      |		},
      |	"skillLevel": "DABBLED"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechIdJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "scala",
      |		"techType": "LANGUAGE"
      |		},
      |	"skillLevel": "DABBLED"
      |}
    """.stripMargin)

  val getSkillMatrixByTechIdResponseJson: JsValue = Json.parse(
    """
      |{
      |  "skills": {
      |    "techId": 1,
      |    "techName": "dark arts",
      |    "techType": "CONCEPTUAL",
      |    "users": [
      |      {
      |        "userName": "Severus Snape",
      |        "level": "CAN_TEACH"
      |      }
      |    ]
      |  }
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
      |          "level": "CAN_TEACH"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val skillMatrixResultTechFunctional: JsValue = Json.parse(
    """
      |{
      |      "techId": 1,
      |      "techName": "functional programming",
      |      "techType": "CONCEPTUAL",
      |      "users": [
      |        {
      |          "userName": "Martin Odersky",
      |          "level": "CAN_TEACH"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val skillMatrixResultTechDefense: JsValue = Json.parse(
    """
      |{
      |      "techId": 1,
      |      "techName": "defense against the dark arts",
      |      "techType": "CONCEPTUAL",
      |      "users": [
      |        {
      |          "userName": "Severus Snape",
      |          "level": "CAN_TEACH"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val skillMatrixResultTechDarkArts: JsValue = Json.parse(
    """
      |{
      |      "techId": 1,
      |      "techName": "dark arts",
      |      "techType": "CONCEPTUAL",
      |      "users": [
      |        {
      |          "userName": "Severus Snape",
      |          "level": "CAN_TEACH"
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
      |      "techType": "CONCEPTUAL"
      |}
    """.stripMargin)

  val allTechDefense: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "defense against the dark arts",
      |      "techType": "CONCEPTUAL"
      |}
    """.stripMargin)

  val allTechDarkArts: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "dark arts",
      |      "techType": "CONCEPTUAL"
      |}
    """.stripMargin)

  val getUserByIdResponse: JsValue = Json.parse(
    """
      |{
      |  "user": {
      |    "id": 4,
      |    "firstName": "Severus",
      |    "lastName": "Snape",
      |    "email": "severus.snape@hogwarts.com"
      |  }
      |}
    """.stripMargin)

  val genericPathMissing = "error.path.missing"
  val objTechMissing = "obj.tech"
  val objSkillLevelMissing = "obj.skillLevel"
  val objTechNameMissing = "obj.tech.name"
  val objTechTypeMissing = "obj.tech.techType"
  val objTechIdMissing = "obj.tech.id"

}
