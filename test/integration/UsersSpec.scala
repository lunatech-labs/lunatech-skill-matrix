package integration

import common.DBConnection
import data.TestData.{ID_USER_SNAPE, nonExistentId}
import models.User
import models.db.Users._

class UsersSpec extends IntegrationSpec  {

  var dataMap: Map[String, Int] = _

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

  "Users" should {
    "get user by id" in {
      val response = getUserById(dataMap(ID_USER_SNAPE))(dbConn).futureValue
      response.map(_.firstName).get mustBe "Severus"
    }

    "return None when user id  is not found" in {
      getUserById(nonExistentId)(dbConn).futureValue mustBe None
    }

    "get user by email" in {
      val response = getUserByEmail("severus.snape@hogwarts.com")(dbConn).futureValue
      response.map(_.firstName).get mustBe "Severus"
    }

    "return None when user is not found in getUserByEmail" in {
      getUserByEmail("non-existent-email@test.com")(dbConn).futureValue mustBe None
    }

    "get userId by email" in {
      val response = getUserIdByEmail("severus.snape@hogwarts.com")(dbConn).futureValue
      response.get mustBe dataMap(ID_USER_SNAPE)
    }

    "return None when user is not found in getUserIdByEmail" in {
      getUserIdByEmail("non-existent-email@test.com")(dbConn).futureValue mustBe None
    }

    "get all users" in {
      getAllUsers(dbConn).futureValue.map(_.firstName) mustBe List("Martin", "Severus")
    }

    "return true if user exists in the database" in {
      exists(dataMap(ID_USER_SNAPE))(dbConn).futureValue mustEqual true
    }

    "return false if user exists in the database" in {
      exists(nonExistentId)(dbConn).futureValue mustEqual false
    }

    "add user to the database" in {
      val newUser = User(None, "Joe", "Armstrong", "joe.armstrong@erlang.com")
      val response = add(newUser)(dbConn).futureValue

      dataMap.values.exists(_ === response) mustBe false
    }

  }
}
