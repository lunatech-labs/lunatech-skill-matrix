package services

import data.TestData.{ID_USER_SNAPE, nonExistentId}
import models._

class UserServiceSpec extends UnitSpec {
  var dataMap: Map[String, Int] = _
  val userService: UserService = app.injector.instanceOf(classOf[UserService])

  override def beforeAll {
    setupDatabase()
  }

  before {
    dataMap = insertSkillData()
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
      response.map(_.firstName) mustBe List("Martin", "Severus", "Gandalf")
    }

    "search with matching filter" in {
      val response = userService.searchUsers(Seq(TechFilter("scala",Operation.Equal,Some(SkillLevel.EXPERT)))).futureValue
      response.map(_.firstName) mustBe List("Martin")
    }

    "search without matching filter" in {
      val response = userService.searchUsers(Seq(TechFilter("scala",Operation.Equal,Some(SkillLevel.NOVICE)))).futureValue
      response mustBe Nil
    }

    "get userId by email when user is in database when calling getOrCreateUserByEmail" in {
      val response = userService.getOrCreateUserByEmail("Severus", "Snape", "severus.snape@hogwarts.com").futureValue

      response mustEqual dataMap(ID_USER_SNAPE)
    }

    "create user by email when user is not in the database" in {
      val response = userService.getOrCreateUserByEmail("Minerva", "McGonagall", "minerva.mcgonagall@hogwarts.com").futureValue

      dataMap.values.exists(_ === response) mustBe false
    }
  }

}
