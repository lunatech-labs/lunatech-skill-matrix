package services

import data.TestData._
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
      response.map(_.firstName) mustBe allUsersNames
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

      response.map(_.id.getOrElse(0)).getOrElse(0) mustEqual dataMap(ID_USER_SNAPE)
    }

    "create user by email when user is not in the database" in {
      val response = userService.getOrCreateUserByEmail("Minerva", "McGonagall", "minerva.mcgonagall@hogwarts.com").futureValue

      dataMap.values.exists(_ === response) mustBe false
    }

    "don't create user in the database if the user is already there" in {
      val response: Option[User] = userService.getOrCreateUserByEmail("Gandalf", "YouShallPass", "gandalf@youshallpass.com").futureValue

      response.get.id.get mustEqual dataMap(ID_USER_GANDALF)
    }

    "update a user AccessLeves" in {
      val newAccessLevel = List(AccessLevel.Management, AccessLevel.Admin)
      val result: Int = userService.updateAccessLevels(userSeverus.copy(id = Some(dataMap(ID_USER_SNAPE)), accessLevels = newAccessLevel)).futureValue
      val updatedUser: Option[User] = userService.getUserById(dataMap(ID_USER_SNAPE)).futureValue

      result mustEqual 1
      updatedUser.map(_.accessLevels.toSet == newAccessLevel.toSet ) mustBe Some(true)

    }

    "update the access level of multiple users" in {
      val newAccessLevelSnape = List(AccessLevel.Management, AccessLevel.Admin)
      val newAccessLevelDumbledore = List(AccessLevel.CEO, AccessLevel.Admin)
      val users = List(
        userSeverus.copy(id = Some(dataMap(ID_USER_SNAPE)), accessLevels = newAccessLevelSnape),
        userDumbledore.copy(id = Some(dataMap(ID_USER_DUMBLEDORE)), accessLevels = newAccessLevelDumbledore)
      )
      val result: Int = userService.batchUpdateAccessLevels(users).futureValue
      val updatedSnape: Option[User] = userService.getUserById(dataMap(ID_USER_SNAPE)).futureValue
      val updatedDumbledore: Option[User] = userService.getUserById(dataMap(ID_USER_DUMBLEDORE)).futureValue

      result mustEqual 2
      updatedSnape.map(_.accessLevels.toSet == newAccessLevelSnape.toSet ) mustBe Some(true)
      updatedDumbledore.map(_.accessLevels.toSet == newAccessLevelDumbledore.toSet ) mustBe Some(true)

    }

    "deactivate user" in {
      val result: Int = userService.deactivateUser(userSeverus.copy(id = Some(dataMap(ID_USER_SNAPE)), status = Status.Inactive)).futureValue
      val updatedUser: Option[User] = userService.getUserById(dataMap(ID_USER_SNAPE)).futureValue

      result mustEqual 1
      updatedUser.map(_.status == Status.Inactive ) mustBe Some(true)

    }

    "deactivate multiple users" in {
      val users = List(
        userSeverus.copy(id = Some(dataMap(ID_USER_SNAPE)), status = Status.Inactive),
        userDumbledore.copy(id = Some(dataMap(ID_USER_DUMBLEDORE)), status = Status.Inactive)
      )
      val result: Int = userService.batchDeactivateUser(users).futureValue
      val updatedSnape: Option[User] = userService.getUserById(dataMap(ID_USER_SNAPE)).futureValue
      val updatedDumbledore: Option[User] = userService.getUserById(dataMap(ID_USER_DUMBLEDORE)).futureValue

      result mustEqual 2
      updatedSnape.map(_.status == Status.Inactive) mustBe Some(true)
      updatedDumbledore.map(_.status == Status.Inactive) mustBe Some(true)

    }

    "activate user" in {
      val result: Int = userService.activateUser(userPettigrew.copy(id = Some(dataMap(ID_USER_PETTIGREW)), status = Status.Active)).futureValue
      val updatedUser: Option[User] = userService.getUserById(dataMap(ID_USER_PETTIGREW)).futureValue

      result mustEqual 1
      updatedUser.map(_.status == Status.Active ) mustBe Some(true)

    }

    "activate multiple users" in {
      val users = List(
        userPettigrew.copy(id = Some(dataMap(ID_USER_PETTIGREW)), status = Status.Active),
        userVoldemort.copy(id = Some(dataMap(ID_USER_VOLDEMORT)), status = Status.Active)
      )
      val result: Int = userService.batchActivateUsers(users).futureValue
      val updatedPettigrew: Option[User] = userService.getUserById(dataMap(ID_USER_PETTIGREW)).futureValue
      val rebornVoldemort: Option[User] = userService.getUserById(dataMap(ID_USER_VOLDEMORT)).futureValue

      result mustEqual 2
      updatedPettigrew.map(_.status == Status.Active) mustBe Some(true)
      rebornVoldemort.map(_.status == Status.Active) mustBe Some(true)

    }
  }

}
