package models

import models.EnumTypes.SkillLevel.SkillLevel
import models.EnumTypes.SkillType.SkillType
import slick.driver.PostgresDriver.api._


// need to interact with postgres

/**
  * Created by tatianamoldovan on 03/02/2017.
  */
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

  class Skills(tag: Tag) extends Table[Skill](tag, "skills") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("skill_name")
    def skillType = column[SkillType]("skill_type")
    def * =  (id.?, name, skillType) <> ((Skill.apply _).tupled, Skill.unapply _)
  }

  class SkillMatrix(tag: Tag) extends Table[models.SkillMatrix](tag, "skill_matrix") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("user_id")
    def skillId = column[Int]("skill_id")
    def skillLevel = column[SkillLevel]("skill_level")
    def * = (id.?, userId, skillId, skillLevel) <> ((models.SkillMatrix.apply _).tupled, models.SkillMatrix.unapply _)

    def user = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)
    def skill = foreignKey("SKILL_FK", skillId, TableQuery[Skills])(_.id)
  }
}
