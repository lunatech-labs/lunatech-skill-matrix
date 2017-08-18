package services

import javax.inject.Inject

import common.PeopleToUserMapper
import models.User

import scala.concurrent.{ExecutionContext, Future}

class PeopleAPIProcessor @Inject()(
                                    peopleAPIService: PeopleAPIService,
                                    userService: UserService,
                                    skillService: SkillMatrixService,
                                    peopleToUserMapper: PeopleToUserMapper)(implicit ec: ExecutionContext) {

  def updateAccessLevels(): Future[Seq[User]] = {
    for {
      people <- peopleAPIService.getAllPeople
      users <- userService.getAll
      userWithUpdatedRoles = peopleToUserMapper.getUsersWithUpdatedAccessLevel(people, users)
      _ <- userService.batchUpdateAccessLevels(userWithUpdatedRoles)
    } yield userWithUpdatedRoles
  }

  def updateUsersStatus(): Future[(Seq[User], Seq[User])] = {
    for {
      people <- peopleAPIService.getAllPeople
      users <- userService.getAll
      (usersToActivate, usersToDeactivate) = peopleToUserMapper.getActiveAndInactiveUsers(people, users)
      _ <- userService.batchActivateUsers(usersToActivate)
      _ <- userService.batchDeactivateUser(usersToDeactivate)
    } yield (usersToActivate, usersToDeactivate)
  }

}
