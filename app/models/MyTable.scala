package models

import models._
import slick.driver.PostgresDriver.api._

object MyTable {
  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("firstname")
    def lastName = column[String]("lastname")
    def email = column[String]("email")
    def * = (id.?, firstName, lastName, email) <> ((User.apply _).tupled, User.unapply _)
  }

  class UsersAuth(tag: Tag) extends Table[UserAuth](tag, "user_auth") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("user_id")
    def key = column[String]("user_key")
    def secret = column[String]("secret")
    def * = (id.?, userId, key, secret) <> ((UserAuth.apply _).tupled, UserAuth.unapply _)

    def user = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)
  }

  class Tech(tag: Tag) extends Table[models.Tech](tag, "tech") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("tech_name")
    def techType = column[TechType]("tech_type")
    def * =  (id.?, name, techType) <> ((models.Tech.apply _).tupled, models.Tech.unapply _)
  }

  class Skills(tag: Tag) extends Table[models.Skill](tag, "user_skills") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("user_id")
    def techId = column[Int]("tech_id")
    def skillLevel = column[SkillLevel]("skill_level")
    def * = (id.?, userId, techId, skillLevel) <> ((models.Skill.apply _).tupled, models.Skill.unapply _)

    def user = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)
    def skill = foreignKey("TECH_FK", techId, TableQuery[MyTable.Tech])(_.id)
  }
}
