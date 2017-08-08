package integration

import common.DBConnection
import data.TestData._
import models._
import models.db.Skills._
import models.db.Techs

class SkillsSpec extends IntegrationSpec {

  var dataMap: Map[String, Int] = _

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

  "Skills" should {
    "return skillId when skill exists in database" in {
      val response = add(dataMap(ID_USER_SNAPE), dataMap(ID_TECH_DEFENSE), SkillLevel.ADVANCED_BEGINNER)(dbConn).futureValue
      response mustEqual dataMap(SKILL_SEVERUS_DEFENSE)
    }

    "add skill to database when tech exists" in {
      val newTechId = Techs.add(Tech(None, "new tech", TechType.OTHER))(dbConn).futureValue
      val response = add(dataMap(ID_USER_SNAPE), newTechId, SkillLevel.EXPERT)(dbConn).futureValue
      dataMap.values.exists(_ === response) mustBe false
    }

    "get all skill matrix by user id" in {
      val expectedResponse = Seq(
        (
          Skill(Some(dataMap(SKILL_SEVERUS_DEFENSE)), dataMap(ID_USER_SNAPE), dataMap(ID_TECH_DEFENSE), SkillLevel.EXPERT, Status.Active),
          Tech(Some(dataMap(ID_TECH_DEFENSE)), "defense against the dark arts", TechType.CONCEPT)
        ),
        (
          Skill(Some(dataMap(SKILL_SEVERUS_DARK_ARTS)), dataMap(ID_USER_SNAPE), dataMap(ID_TECH_DARK_ARTS), SkillLevel.EXPERT, Status.Active),
          Tech(Some(dataMap(ID_TECH_DARK_ARTS)), "dark arts", TechType.CONCEPT)
        ))
      val response = getAllSkillMatrixByUser(dataMap(ID_USER_SNAPE))(dbConn).futureValue
      response must contain theSameElementsAs expectedResponse
    }

    "get all skill matrix" in {
      val expectedResponse = Seq(
        (
          Skill(Some(dataMap(SKILL_SEVERUS_DEFENSE)), dataMap(ID_USER_SNAPE), dataMap(ID_TECH_DEFENSE), SkillLevel.EXPERT, Status.Active),
          User(Some(dataMap(ID_USER_SNAPE)), "Severus", "Snape", "severus.snape@hogwarts.com", AccessLevel.Basic, Status.Active),
          Tech(Some(dataMap(ID_TECH_DEFENSE)), "defense against the dark arts", TechType.CONCEPT)
        ),
        (
          Skill(Some(dataMap(SKILL_SEVERUS_DARK_ARTS)), dataMap(ID_USER_SNAPE), dataMap(ID_TECH_DARK_ARTS), SkillLevel.EXPERT, Status.Active),
          User(Some(dataMap(ID_USER_SNAPE)), "Severus", "Snape", "severus.snape@hogwarts.com", AccessLevel.Basic, Status.Active),
          Tech(Some(dataMap(ID_TECH_DARK_ARTS)), "dark arts", TechType.CONCEPT)
        ),
        (
          Skill(Some(dataMap(SKILL_ODERSKY_SCALA)), dataMap(ID_USER_ODERSKY), dataMap(ID_TECH_SCALA), SkillLevel.EXPERT, Status.Active),
          User(Some(dataMap(ID_USER_ODERSKY)), "Martin", "Odersky", "martin.odersky@gmail.com", AccessLevel.Basic, Status.Active),
          Tech(Some(dataMap(ID_TECH_SCALA)), "scala", TechType.LANGUAGE)
        ),
        (
          Skill(Some(dataMap(SKILL_ODERSKY_FUNCTIONAL)), dataMap(ID_USER_ODERSKY), dataMap(ID_TECH_FUNCTIONAL), SkillLevel.EXPERT, Status.Active),
          User(Some(dataMap(ID_USER_ODERSKY)), "Martin", "Odersky", "martin.odersky@gmail.com", AccessLevel.Basic, Status.Active),
          Tech(Some(dataMap(ID_TECH_FUNCTIONAL)), "functional programming", TechType.CONCEPT)
        ))
      val response = getAllSkills(dbConn).futureValue
      response must contain theSameElementsAs expectedResponse
    }

    "return a list of skill by tech id" in {
      val expectedResponse = Seq(Skill(Some(dataMap(SKILL_SEVERUS_DEFENSE)), dataMap(ID_USER_SNAPE), dataMap(ID_TECH_DEFENSE), SkillLevel.EXPERT, Status.Active))
      getSkillByTechId(dataMap(ID_TECH_DEFENSE))(dbConn).futureValue mustBe expectedResponse
    }
  }

}
