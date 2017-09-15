package common

import models.{AccessLevel, Person, Status, User}

class PeopleToUserMapper {

  def getUsersWithUpdatedAccessLevel(people: Seq[Person], users: Seq[User]): Seq[User] = {
    val emailsAndAccessLevels: Seq[(String, Seq[AccessLevel])] = people.filter(person =>  users.map(_.email) contains person.email).map(person => (person.email, fromRolesToAccessLevels(person.roles)))
    val foundActiveUsers: Seq[User] = users.filter(user => people.map(_.email) contains user.email).filter(user => user.status == Status.Active)

    foundActiveUsers
      .filter{ user =>
        val newAccessLevel: (String, Seq[AccessLevel]) = emailsAndAccessLevels.filter(x => x._1.equals(user.email)).head
        user.accessLevels.toSet != newAccessLevel._2.toSet}
      .map{ user =>
        val newAccessLevel: (String, Seq[AccessLevel]) = emailsAndAccessLevels.filter(x => x._1.equals(user.email)).head
        User(user.id, user.firstName, user.lastName, user.email, newAccessLevel._2.toList, user.status)
    }
  }

  /**
    *
    * @param people
    * @param users
    * @return a list of users whose status needs to be activated and list of users whose status has the be deactived from our db
    */
  def getActiveAndInactiveUsers(people: Seq[Person], users: Seq[User]): (Seq[User], Seq[User]) = {
    val usersToActivate: Seq[User] = users.filter(user => people.map(_.email) contains user.email).filter(user => user.status == Status.Inactive)
    val usersToDeactivate: Seq[User] = users.filterNot(user => people.map(_.email) contains user.email).filter(user => user.status == Status.Active)
    (usersToActivate, usersToDeactivate)
  }

  private def fromRolesToAccessLevels(roles: Seq[String]): Seq[AccessLevel] = {
    roles.map {
      case "developer" => AccessLevel.Developer
      case "development manager" | "development-manager" => AccessLevel.Management
      case "techmatrix-admin" => AccessLevel.Admin
      case "ceo" => AccessLevel.CEO
      case "administrative" => AccessLevel.Administrative
      case _ => AccessLevel.Basic
    }
  }

}
