package services

import data.TestData.ID_TECH_SCALA
import models.{Tech, TechType}

class TechServiceSpec extends UnitSpec {
  var dataMap: Map[String, Int] = _
  val techService: TechService = app.injector.instanceOf(classOf[TechService])

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

  "TechService" should {
    "return a list of all tech" in {
      val response = techService.getAllTech.futureValue
      response.map(_.name) mustBe List("scala", "functional programming", "defense against the dark arts", "dark arts")
    }

    "return existing techId when tech name is found in the database even when type is different" in {
      val response: Int = techService.getTechIdOrInsert(Tech(None, "scala", TechType.FRAMEWORK)).futureValue
      response mustBe dataMap(ID_TECH_SCALA)
    }

    "return the id of the newly created tech when tech not found in the database" in {
      val response: Int = techService.getTechIdOrInsert(Tech(None, "brand new skill", TechType.CONCEPT)).futureValue
      dataMap.values.exists(_ === response) mustBe false
    }

    "return the id of tech by name and type" in {
      val response: Option[Int] = techService.getTechIdByName(Tech(None, "scala", TechType.LANGUAGE)).futureValue
      response.get mustBe dataMap(ID_TECH_SCALA)
    }

    "return none when tech is not found by name and type" in {
      val response: Option[Int] = techService.getTechIdByName(Tech(None, "brand new skill", TechType.CONCEPT)).futureValue
      response mustBe None
    }

    "return tech when techId is found in the database" in {
      val response: Option[Tech] = techService.getById(dataMap(ID_TECH_SCALA)).futureValue
      response.get.name mustBe "scala"
    }

    "return None when techId is not found in the Database" in {
      val response: Option[Tech] = techService.getById(42).futureValue
      response mustBe None
    }

  }

}
