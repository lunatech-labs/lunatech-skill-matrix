package common

import models.{AccessLevel, Status, User}
import services.PeopleAPIService.Person
import data.TestData._
import services.UnitSpec

class PeopleToUserMapperSpec extends UnitSpec {

  val peopleToUserMapper: PeopleToUserMapper = app.injector.instanceOf(classOf[PeopleToUserMapper])

  val idSeverus = 1
  val idDumbledore = 2
  val idGandalf = 3
  val idOdersky = 4

  val currentUsers: Seq[User] = Seq(
    userOdersky.copy(id = Some(idOdersky)),
    userSeverus.copy(id = Some(idSeverus)),
    userGandalf.copy(id = Some(idGandalf)),
    userDumbledore.copy(id = Some(idDumbledore))
  )

  val people: Seq[Person] = Seq(
    Person("severus.snape@hogwarts.com", Seq("erik.janssen@lunatech.com"), Seq("developer", "development manager", "some-other-awesome-role")),
    Person("gandalf@youshallpass.com", Seq("erik.janssen@lunatech.com"), Seq("developer", "techmatrix-admin")),
    Person("newuser.shouldbeignored@gmail.com", Seq("erik.janssen@lunatech.com"), Seq("developer")),
    Person("albus@dumbledore.com", Seq("erik.janssen@lunatech.com"), Seq("techmatrix-admin"))
  )

  "PeopleToUserMapper" should {
    "return a list of users with updated access levels" in {
      val expectedResult: Seq[User]= Seq(
        userSeverus.copy(id = Some(idSeverus), accessLevels = List(AccessLevel.Developer, AccessLevel.Management, AccessLevel.Basic)),
        userGandalf.copy(id = Some(idGandalf), accessLevels = List(AccessLevel.Developer, AccessLevel.Admin))
      )

      val result: Seq[User] = peopleToUserMapper.getUsersWithUpdatedAccessLevel(people, currentUsers)
      result mustBe expectedResult
    }

    "return a tuple composed of a list of users who need to be activated and a list of users who need to be deactivated" in {
      val expectedResult: (Seq[User], Seq[User]) = (
        Seq(userDumbledore.copy(id = Some(idDumbledore))),
        Seq(userOdersky.copy(id = Some(idOdersky)))
      )

      val result : (Seq[User], Seq[User]) = peopleToUserMapper.getActiveAndInactiveUsers(people, currentUsers)

      result mustBe expectedResult
    }
  }
}
