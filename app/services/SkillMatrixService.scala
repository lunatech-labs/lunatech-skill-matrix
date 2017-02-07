package services

import javax.inject.Inject

import models.EnumTypes.SkillLevel.SkillLevel
import models.Skill
import models.dao.SkillMatrixDAO

/**
  * Created by tatianamoldovan on 06/02/2017.
  */
class SkillMatrixService @Inject() (skillMatrixDAO: SkillMatrixDAO,
                                    skillService: SkillService) {

  def addSkillByUserId(userId: Int, skill: Skill, skillLevel: SkillLevel) = {
    val skillId = skillService.getOrCreateSkillId(skill.name, skill.skillType)
    skillMatrixDAO.createSkill(userId, skillId, skillLevel)
  }

  def updateSkillByUserId(userSkillId: Int, userId: Int, skill: Skill, skillLevel: SkillLevel) = {
    val skillId = skillService.getOrCreateSkillId(skill.name, skill.skillType)
    skillMatrixDAO.updateSkill(userSkillId, userId, skillId, skillLevel)
  }

  def deleteSkillByUserId(userSkillId: Int) = {
    skillMatrixDAO.deleteSkillByUserId(userSkillId)
  }

  def getAllSkillsByUserId(userId: Int) = {
    skillMatrixDAO.getAllSkillsByUserId(userId)
  }

  /*def getAllSkillsFromSkillMatrix() = {
    skillMatrixDAO.getAllSkillsFromSkillMatrix()
  }*/

  def getSkillById(skillId: Int) = {
    skillMatrixDAO.getSkillById(skillId)
  }

}
