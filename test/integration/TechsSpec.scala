package integration

import common.DBConnection
import data.TestData._
import models.{Tech, TechType}
import models.db.Techs._

class TechsSpec extends IntegrationSpec {

  var dataMap: Map[String, Int] = _

  override def beforeAll {
    setupDatabase()
  }

  before {
    dataMap = insertTechData()
  }

  after {
    cleanTechData()
  }

  override def afterAll {
    dropDatabase()
  }

  "Techs" should {
    "add tech to database" in {
      val newTech = Tech(None, "Brand new skill", "brand new skill", TechType.CONCEPT)
      val response = add(newTech)(dbConn).futureValue
      dataMap.values.exists(_ === response) mustBe false
    }

    "get techId by name and type" in {
      val response = getTechIdByName(Tech(None, "Dark Arts", "dark arts", TechType.CONCEPT))(dbConn).futureValue
      response.get mustEqual dataMap(ID_TECH_DARK_ARTS)
    }

    "get all tech" in {
      val expectedResponse = Seq(
        Tech(Some(dataMap(ID_TECH_SCALA)), "Scala", "scala", TechType.LANGUAGE),
        Tech(Some(dataMap(ID_TECH_FUNCTIONAL)), "Functional Programming", "functional programming", TechType.CONCEPT),
        Tech(Some(dataMap(ID_TECH_DEFENSE)), "Defense Against the Dark Arts", "defense against the dark arts", TechType.CONCEPT),
        Tech(Some(dataMap(ID_TECH_DARK_ARTS)), "Dark Arts", "dark arts", TechType.CONCEPT)
      )
      val response = getAllTech(dbConn).futureValue

      response must contain theSameElementsAs expectedResponse
    }

    "get tech by id" in {
      getTechById(dataMap(ID_TECH_DARK_ARTS))(dbConn).futureValue.get mustEqual Tech(Some(dataMap(ID_TECH_DARK_ARTS)), "Dark Arts", "dark arts", TechType.CONCEPT)
    }

    "return None when tech not found in getTechById" in {
      getTechById(nonExistentId)(dbConn).futureValue mustBe None
    }

    "update tech" in {
      val response = updateTech(dataMap(ID_TECH_DARK_ARTS), Tech(None, "DARK ARTS", "dark arts", TechType.CONCEPT))(dbConn).futureValue
      response mustBe 1
    }

    "return none when tech not found for update operation" in {
      updateTech(nonExistentId, Tech(None, "tech", "tech", TechType.OTHER))(dbConn).futureValue mustBe 0
    }
  }

}
