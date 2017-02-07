package services

import javax.inject.Inject

import models.EnumTypes.SkillType.SkillType
import models.dao.SkillDAO

import scala.concurrent.Future
import scala.util.Success

/**
  * Created by tatianamoldovan on 03/02/2017.
  */
class SkillService @Inject()(skillDAO: SkillDAO) {

  def getOrCreateSkillId(name: String, skillType: SkillType): Int = {
    var id = skillDAO.getSkillIdByNameAndType(name, skillType)
    if(id == -1) id = skillDAO.createSkill(name, skillType)
    id
  }

  def getSkills() = {
    skillDAO.getSkills()
  }

}
