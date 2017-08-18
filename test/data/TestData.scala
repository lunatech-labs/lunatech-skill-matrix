package data

import models.{AccessLevel, Status, User}
import play.api.libs.json.{JsValue, _}

object TestData {

  val ID_USER_ODERSKY = "idUserOdersky"
  val ID_USER_SNAPE = "idUserSnape"
  val ID_USER_GANDALF = "idUserGandalf"
  val ID_USER_DUMBLEDORE = "idUserDumbledore"
  val ID_USER_VOLDEMORT = "idUserVoldemort"
  val ID_USER_PETTIGREW = "idUserPettigrew"

  val ID_TECH_SCALA = "idTechScala"
  val ID_TECH_FUNCTIONAL = "idTechFunctional"
  val ID_TECH_DEFENSE = "idTechDefense"
  val ID_TECH_DARK_ARTS = "idTechDarkArts"

  val SKILL_ODERSKY_SCALA = "skillOderskyScala"
  val SKILL_ODERSKY_FUNCTIONAL = "skillOderskyFunctional"
  val SKILL_SEVERUS_DEFENSE = "skillSeverusDefense"
  val SKILL_SEVERUS_DARK_ARTS = "skillSeverusDarkArts"
  val SKILL_DUMBLEDORE_DARK_ARTS = "skillDumbledoreDarkArts"
  val SKILL_VOLDEMORT_DARK_ARTS = "skillVoldemortDarkArts"


  val userOdersky = User(None, "Martin", "Odersky", "martin.odersky@gmail.com",List(AccessLevel.Basic), Status.Active)
  val userSeverus = User(None, "Severus", "Snape", "severus.snape@hogwarts.com",List(AccessLevel.Management), Status.Active)
  val userGandalf = User(None, "Gandalf", "YouShallPass", "gandalf@youshallpass.com", List(AccessLevel.Admin), Status.Active)
  val userDumbledore = User(None, "Albus Percival Wulfric Brian", "Dumbledore", "albus@dumbledore.com", List(AccessLevel.Admin), Status.Inactive)
  val userVoldemort= User(None, "Tom Riddle", " Lord Voldemort", "lord@voldemort.com", List(AccessLevel.Basic), Status.Inactive)
  val userPettigrew = User(None, "Peter", "pettigrew", "peterpettigrew@traitor.com", List(AccessLevel.Basic), Status.Inactive)

  val allUsersNames = List(
    userOdersky.firstName, userSeverus.firstName, userGandalf.firstName, userDumbledore.firstName,
    userVoldemort.firstName, userPettigrew.firstName)



  val X_AUTH_TOKEN = "X-AUTH-TOKEN"

  val nonExistentId = 4242

  val updateTechJson: JsValue = Json.parse(
    """
       {
        "name": "New updated name",
        "techType": "LANGUAGE"
       }
    """.stripMargin
  )

  val updateTechDuplicateNameJson: JsValue = Json.parse(
    """
       {
        "name": "scala",
        "techType": "LANGUAGE"
       }
    """.stripMargin
  )

  val updateTechJsonWithMissingName: JsValue = Json.parse(
    """
       {
        "techType": "LANGUAGE"
       }
    """.stripMargin
  )

  val updateTechJsonWithMissingType: JsValue = Json.parse(
    """
       {
        "name": "New updated name"
       }
    """.stripMargin
  )

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

  val emptyTechFilter: JsValue = Json.parse(
    """
      |[]
    """.stripMargin)

  val wrongTechFilter: JsValue = Json.parse(
    """
      |[
      | {
      |   "format":"wrong"
      | }
      |]
    """.stripMargin)

}
