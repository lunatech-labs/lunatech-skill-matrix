package integration

import common.DBConnection
import data.TestData.{ID_USER_SNAPE,ID_USER_GANDALF,ID_USER_ODERSKY, nonExistentId}
import models.{AccessLevel, Status, User}
import models.db.Users._

class UsersSpec extends IntegrationSpec {

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
      val expectedResponse = Seq(
        User(Some(dataMap(ID_USER_SNAPE)), "Severus", "Snape", "severus.snape@hogwarts.com", AccessLevel.Management, Status.Active),
        User(Some(dataMap(ID_USER_GANDALF)), "Gandalf", "YouShallPass", "gandalf@youshallpass.com", AccessLevel.Admin, Status.Active),
        User(Some(dataMap(ID_USER_ODERSKY)), "Martin","Odersky","martin.odersky@gmail.com", AccessLevel.Basic, Status.Active)
      )
      val result = getAllUsers(dbConn).futureValue
      result must contain theSameElementsAs expectedResponse
    }

    "return true if user exists in the database" in {
      exists(dataMap(ID_USER_SNAPE))(dbConn).futureValue mustEqual true
    }

    "return false if user exists in the database" in {
      exists(nonExistentId)(dbConn).futureValue mustEqual false
    }

    "add user to the database" in {
      val newUser = User(None, "Joe", "Armstrong", "joe.armstrong@erlang.com", AccessLevel.Basic, Status.Active)
      val response = add(newUser)(dbConn).futureValue

      dataMap.values.exists(_ === response) mustBe false
    }

  }
}
