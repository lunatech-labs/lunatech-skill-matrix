package services

import javax.inject.Inject

import models.dao.UserDAO

/**
  * Created by tatianamoldovan on 07/02/2017.
  */
class UserService @Inject() (userDAO: UserDAO) {

  def getUserById(userId: Int) = {
    userDAO.getUserById(userId)
  }

}
