package acceptance

import models._
import play.api.libs.json.{JsValue, _}



object TestData {

  val userTanya = User(None, "Tanya", "Moldovan", "tanya.moldovan@gmail.com")
  val userSeverus = User(None, "Severus", "Snape", "severus.snape@hogwarts.com")


  val techScala = Tech(None, "Scala", TechType.LANGUAGE)
  val techFunctional = Tech(None, "Functional Programming", TechType.CONCEPTUAL)
  val techDefense = Tech(None, "Defense against the Dark Arts", TechType.CONCEPTUAL)
  val techDarkArts= Tech(None, "Dark Arts", TechType.CONCEPTUAL)

  val errorUserNotFound = "User not found"
  val skillNotFound = "Skill for this user could not be found"

  val addSkillRequestJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "Brainfuck",
      |		"techType": "LANGUAGE"
      |	},
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val addSkillRequestWithMissingTechJson: JsValue = Json.parse(
    """
      |{
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val addSkillRequestWithMissingSkillLevelJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "Brainfuck",
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
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val addSkillRequestWithMissingTechTypeJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "Brainfuck"
      |	},
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val putSkillRequestJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"id": 5,
      |		"name": "Scala",
      |		"techType": "LANGUAGE"
      |		},
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechJson: JsValue = Json.parse(
    """
      |{
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingSkillLevelJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"id": 5,
      |		"name": "Scala",
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
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechTypeJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"id": 5,
      |		"name": "Scala"
      |		},
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val putSkillRequestWithMissingTechIdJson: JsValue = Json.parse(
    """
      |{
      |	"tech": {
      |		"name": "Scala",
      |		"techType": "LANGUAGE"
      |		},
      |	"skillLevel": "DABBED"
      |}
    """.stripMargin)

  val getSkillMatrixByTechIdResponseJson: JsValue = Json.parse(
    """
      |{
      |  "skills": {
      |    "techId": 1,
      |    "techName": "Dark Arts",
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
      |      "techName": "Scala",
      |      "techType": "LANGUAGE",
      |      "users": [
      |        {
      |          "userName": "Tanya Moldovan",
      |          "level": "COMFORTABLE"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val skillMatrixResultTechFunctional: JsValue = Json.parse(
    """
      |{
      |      "techId": 1,
      |      "techName": "Functional Programming",
      |      "techType": "CONCEPTUAL",
      |      "users": [
      |        {
      |          "userName": "Tanya Moldovan",
      |          "level": "COMFORTABLE"
      |        }
      |      ]
      |    }
    """.stripMargin)

  val skillMatrixResultTechDefense: JsValue = Json.parse(
    """
      |{
      |      "techId": 1,
      |      "techName": "Defense against the Dark Arts",
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
      |      "techName": "Dark Arts",
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
      |      "name": "Scala",
      |      "techType": "LANGUAGE"
      |}
    """.stripMargin)

  val allTechFunctional: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "Functional Programming",
      |      "techType": "CONCEPTUAL"
      |}
    """.stripMargin)

  val allTechDefense: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "Defense against the Dark Arts",
      |      "techType": "CONCEPTUAL"
      |}
    """.stripMargin)

  val allTechDarkArts: JsValue = Json.parse(
    """
      |{
      |      "id": 4,
      |      "name": "Dark Arts",
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
