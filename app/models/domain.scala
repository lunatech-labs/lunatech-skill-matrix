package models

import play.api.libs.json.{Format, Json}

case class User(id: Option[Int] = None, firstName: String, lastName: String, email: String) {
  require(email != null && !email.isEmpty, "Email field shouldn't be empty")

  def fullName: String = {
    this.firstName + " " + this.lastName
  }

  def getUserId: Int = {
    id.getOrElse(throw new Exception("User id is not defined!"))
  }
}

case class Tech(id: Option[Int], name: String, techType: TechType)

case class Skill(id: Option[Int] = None, userId: Int, techId: Int, skillLevel: SkillLevel)

case class SkillMatrixItem(tech: Tech, skillLevel: SkillLevel, id: Option[Int])

case class SkillMatrixUsersAndLevel(fullName: String, level: SkillLevel)

case class SkillMatrixResponse(techId: Int, techName: String, techType: TechType, users: Seq[SkillMatrixUsersAndLevel])

case class UserSkillResponse(userId: Int, firstName: String, lastName: String, skills: Seq[SkillMatrixItem])

object ImplicitFormats {

  implicit val userFormat: Format[User] = Json.format[User]
  implicit val techFormat: Format[Tech] = Json.format[Tech]
  implicit val userSkillFormat: Format[Skill] = Json.format[Skill]
  implicit val skillMatrixItemFormat: Format[SkillMatrixItem] = Json.format[SkillMatrixItem]
  implicit val skillMatrixUsersAndLevelFormat: Format[SkillMatrixUsersAndLevel] = Json.format[SkillMatrixUsersAndLevel]
  implicit val skillMatrixResponseFormat: Format[SkillMatrixResponse] = Json.format[SkillMatrixResponse]
  implicit val userSkillResponseFormat: Format[UserSkillResponse] = Json.format[UserSkillResponse]

}
