package services

import data.TestData._
import models._

class SkillMatrixServiceSpec extends UnitSpec {

  var dataMap: Map[String, Int] = _
  val skillMatrixService: SkillMatrixService = app.injector.instanceOf(classOf[SkillMatrixService])

  override def beforeAll {
    setupDatabase()
  }

  before {
    dataMap = insertSkillData()
  }

  after {
    cleanSkillData()
  }

  override def afterAll {
    dropDatabase()
  }

  "SkillMatrixService" should {
    "add user skill to the database and return its id" in {
      val newTech = Tech(None, "Sarcasm", TechType.CONCEPT)
      val response = skillMatrixService.addUserSkill(dataMap(ID_USER_SNAPE), newTech, SkillLevel.EXPERT).futureValue

      dataMap.values.exists(_ === response) mustBe false
    }

    "update a user skill and return its id" in {
      val response = skillMatrixService.updateUserSkill(dataMap(SKILL_SEVERUS_DEFENSE), dataMap(ID_USER_SNAPE),  dataMap(ID_TECH_DEFENSE), SkillLevel.PROFICIENT).futureValue
      response.get mustBe dataMap(SKILL_SEVERUS_DEFENSE)
    }

    "return None when skillId does not exist for update operation" in {
      val response = skillMatrixService.updateUserSkill(nonExistentId, dataMap(ID_USER_SNAPE),  dataMap(ID_TECH_DEFENSE), SkillLevel.PROFICIENT).futureValue
      response mustBe None
    }

    "delete the skill from the database" in {
      val response = skillMatrixService.deleteUserSkill(dataMap(ID_USER_SNAPE), dataMap(SKILL_SEVERUS_DEFENSE)).futureValue
      response.get mustEqual 1
    }

    "return None when skillId does not exist in the database for the delete operation" in {
      val response = skillMatrixService.deleteUserSkill(dataMap(ID_USER_SNAPE), nonExistentId).futureValue
      response mustBe None
    }

    "return all user skills" in {
      val response = skillMatrixService.getUserSkills(dataMap(ID_USER_SNAPE)).futureValue
      val expectedResponse = UserSkillResponse(
        dataMap(ID_USER_SNAPE),
        "Severus",
        "Snape",
        Seq(
          SkillMatrixItem(Tech(Some(dataMap(ID_TECH_DEFENSE)), "defense against the dark arts", TechType.CONCEPT), SkillLevel.EXPERT, Some(dataMap(SKILL_SEVERUS_DEFENSE))),
          SkillMatrixItem(Tech(Some(dataMap(ID_TECH_DARK_ARTS)), "dark arts", TechType.CONCEPT), SkillLevel.EXPERT, Some(dataMap(SKILL_SEVERUS_DARK_ARTS)))
        )
      )
      response.get mustBe expectedResponse
    }

    "return None when user not found when getting all user skills" in {
      val response = skillMatrixService.getUserSkills(nonExistentId).futureValue
      response mustBe None
    }

    "return all skill matrix when calling getAllSkills" in {
      val response = skillMatrixService.getAllSkills.futureValue
      val expectedResponse = Seq(
        SkillMatrixResponse(dataMap(ID_TECH_SCALA), "scala", TechType.LANGUAGE, Seq(SkillMatrixUsersAndLevel("Martin Odersky", SkillLevel.EXPERT))),
        SkillMatrixResponse(dataMap(ID_TECH_FUNCTIONAL), "functional programming", TechType.CONCEPT, Seq(SkillMatrixUsersAndLevel("Martin Odersky", SkillLevel.EXPERT))),
        SkillMatrixResponse(dataMap(ID_TECH_DEFENSE), "defense against the dark arts", TechType.CONCEPT, Seq(SkillMatrixUsersAndLevel("Severus Snape", SkillLevel.EXPERT))),
        SkillMatrixResponse(dataMap(ID_TECH_DARK_ARTS), "dark arts", TechType.CONCEPT, Seq(SkillMatrixUsersAndLevel("Severus Snape", SkillLevel.EXPERT)))
      )

      response must contain theSameElementsAs expectedResponse
    }

    "return SkillMatrixResponse when techId is found in the skill matrix" in {
      val response = skillMatrixService.getSkillMatrixByTechId(dataMap(ID_TECH_SCALA)).futureValue
      val expectedResponse = SkillMatrixResponse(dataMap(ID_TECH_SCALA), "scala", TechType.LANGUAGE, Seq(SkillMatrixUsersAndLevel("Martin Odersky", SkillLevel.EXPERT)))

      response.get mustBe expectedResponse
    }

    "return None when techId is not found in the skill matrix" in {
      val response = skillMatrixService.getSkillMatrixByTechId(nonExistentId).futureValue
      response mustBe None
    }

  }

}
