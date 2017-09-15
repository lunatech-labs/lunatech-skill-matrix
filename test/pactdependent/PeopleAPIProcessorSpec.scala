package pactdependent

import models.User
import services.PeopleAPIProcessor

class PeopleAPIProcessorSpec extends PactDependentSpec {

  var dataMap: Map[String, Int] = _
  val stubber: Stubber = Stubber()
  val peopleApiProcessor: PeopleAPIProcessor = app.injector.instanceOf(classOf[PeopleAPIProcessor])

  override def beforeAll {
    stubber.startStubber()
    setupDatabase()
  }

  before {
    dataMap = insertUserData()
  }

  after {
    cleanUserData()
  }

  override def afterAll {
    stubber.stopStubber()
    dropDatabase()
  }

  "PeopleAPIProcessorSpec" should {
    "return the number of users whose access levels were updated" in {
      val response: Seq[User] = peopleApiProcessor.updateAccessLevels().futureValue

      response.map(_.firstName) must contain theSameElementsAs Seq("Severus", "Gandalf")

    }
    "return a tuple containing the number of activated and deactivated users" in {
      val (activatedUsers, deactivatedUsers): (Seq[User], Seq[User]) = peopleApiProcessor.updateUsersStatus().futureValue

      activatedUsers.map(user => user.firstName.equals("Albus Percival Wulfric Brian")).exists(_ === true) mustEqual true
      deactivatedUsers.map(user => user.firstName.equals("Martin")).exists(_ === true) mustEqual true
    }
  }

}
