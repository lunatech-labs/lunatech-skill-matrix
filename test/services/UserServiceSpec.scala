package services

import data.TestData.{ID_USER_SNAPE, nonExistentId}
import models.User

class UserServiceSpec extends UnitSpec {
  var dataMap: Map[String, Int] = _
  val userService: UserService = app.injector.instanceOf(classOf[UserService])

  override def beforeAll {
    setupDatabase()
  }

  before {
    dataMap = insertUserData()
  }

  after {
    cleanUserData()
  }

  override def afterAll {
    dropDatabase()
  }

  "UserService" should {

    "get user by id" in {
      val response = userService.getUserById(dataMap(ID_USER_SNAPE)).futureValue
      response.map(_.firstName).get mustBe "Severus"
    }

    "return None when user id is not found" in {
      val response = userService.getUserById(nonExistentId).futureValue
      response mustBe None
    }

    "get user by email" in {
      val response = userService.getUserByEmail("severus.snape@hogwarts.com").futureValue
      response.map(_.firstName).get mustBe "Severus"
    }

    "return None when user is not found" in {
      val response = userService.getUserByEmail("non-existent-email@test.com").futureValue
      response mustBe None
    }

    "get all users" in {
      val response = userService.getAll.futureValue
      response.map(_.firstName) mustBe List("Martin", "Severus")
    }

    "get userId by email when user is in database when calling getOrCreateUserByEmail" in {
      val severus = User(Some(dataMap(ID_USER_SNAPE)), "Severus", "Snape", "severus.snape@hogwarts.com")
      val response = userService.getOrCreateUserByEmail(severus).futureValue

      response mustEqual dataMap(ID_USER_SNAPE)
    }

    "create user by email when user is not in the database" in {
      val minerva = User(None, "Minerva", "McGonagall", "minerva.mcgonagall@hogwarts.com")
      val response = userService.getOrCreateUserByEmail(minerva).futureValue

      dataMap.values.exists(_ === response) mustBe false
    }
  }

}
