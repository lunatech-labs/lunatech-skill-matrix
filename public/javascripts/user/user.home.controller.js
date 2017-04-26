angular.module('techmatrix').controller('UserHomeController',[
    '$scope',
    'RestService',
    'techType',
    'level',
    '$cookies',
    '$location',
    'RestErrorService',
    '$mdToast',
    function($scope,RestService,techType,level,$cookies,$location,RestErrorService,$mdToast){

    var successAlert = true;
    var failureAlert = false;

    $scope.data = {};

    $scope.data.techType = techType;
    $scope.data.level = level;

    $scope.data.groupedSkills = {};
    $scope.data.user = undefined;

    $scope.data.message ={
        show:false,
        value: '',
        class:''
    };

    $scope.data.skillForm = {
        name:undefined,
        techType:$scope.data.techType['LANGUAGE'].value,
        skillLevel:$scope.data.level['COMPETENT'].value
    }

    function onInit(){
        $scope.data.user = $cookies.getObject('user');
        if($scope.data.user === undefined){
            $location.path('/skillmatrix/nouser');
        }else{
            RestService.getMyProfile($scope.data.user.id).then(function(response){
                $scope.data.user.skills = response.data.skills.map(addSearchFilter);
                angular.forEach($scope.data.techType,function(techType){
                  $scope.data.groupedSkills[techType.value] = $scope.data.user.skills.filter(function(s){
                    return s.tech.techType === techType.value;
                  })
                });
            },function(response){
                RestErrorService.errorHandler(response)
                $scope.data.user.skills = [];
                showMessage('Error getting user skills',failureAlert);
            });
        }
    }

    function addSearchFilter(skill){
        skill.searchFilter = skill.tech.name + "," + skill.tech.techType + "," + skill.skillLevel;
        return skill;
    }

    $scope.addSkill = function(){
        $scope.data.skillForm.name = $scope.data.selectedTech ? $scope.data.selectedTech.name : $scope.data.searchText;
        if(isValidateForm()){
            var data = {
                userId:$scope.data.user.id,
                body:{
                    tech:{
                        name:$scope.data.skillForm.name,
                        techType:$scope.data.skillForm.techType
                    },
                    skillLevel:$scope.data.skillForm.skillLevel
                }
            };
            RestService.addSKill(data).then(function(response){
                $scope.data.skillForm = getNewSkillForm();
                $scope.data.selectedTech = undefined;
                $scope.data.searchText = undefined;
                var skillWithFilter = addSearchFilter(response.data);
                $scope.data.user.skills.push(skillWithFilter);
                $scope.data.groupedSkills[skillWithFilter.tech.techType].push(skillWithFilter);
                showMessage('Tech added',successAlert);
            },function(response){
                RestErrorService.errorHandler(response)
                showMessage('Error adding tech',failureAlert);
            });

        }
    };


    function isValidateForm(){
        valid = true;
        valid = $scope.data.skillForm.name !== undefined &&
        $scope.data.skillForm.name !== ''&&
        $scope.data.skillForm.techType !== undefined &&
        $scope.data.skillForm.skillLevel !== undefined;

        if(!valid){
            showMessage('Please fill all field before adding a tech',failureAlert);
        }else{
            valid = unknownSkill($scope.data.skillForm);
            if(!valid){
                showMessage('Already known skill',failureAlert);
            }
        }
        return valid;
    }

    function unknownSkill(skill){
        var known = $scope.data.user.skills.filter(function(s){
            return s.tech.name.toLowerCase() === skill.name.toLowerCase() && s.tech.techType === skill.techType;
        })
        return known.length === 0
    }

    function getNewSkillForm(){
        return {
           name:undefined,
           techType:$scope.data.techType['LANGUAGE'].value,
           skillLevel:$scope.data.level['COMPETENT'].value
       }
    }

    $scope.removeSkill = function(skill){
        //TODO add confirmation dialog
        var params = {
            userId:$scope.data.user.id,
            skillId: skill.id,
        }
        RestService.removeSkill(params).then(function(response){
            $scope.data.user.skills = $scope.data.user.skills.filter(function(s){
                return s.id !== skill.id;
            })
            $scope.data.groupedSkills[skill.tech.techType] = $scope.data.groupedSkills[skill.tech.techType].filter(function(s){
              return s.id !== skill.id;
            })
            showMessage('Tech removed',successAlert);
        },function(response){
            RestErrorService.errorHandler(response)
            showMessage('Error removing tech',failureAlert);
        });
    };

    $scope.updateSkill = function(skill){
        angular.forEach($scope.data.user.skills,function(s){
          if(skill.id !== s.id){
            s.updating = false;
          }
        });
        skill.updating = !skill.updating;
    };

    $scope.finishUpdatingSkill = function(skill, level){
        var data = {
            userId:$scope.data.user.id,
            skillId:skill.id,
            body:{
                tech:{
                    id:skill.tech.id,
                    name:skill.tech.name,
                    techType:skill.tech.techType
                },
                skillLevel:level.value
            }
        };
        RestService.updateSkill(data).then(function(response){
            skill.skillLevel = response.data.skillLevel;
            showMessage('Tech updated',successAlert)
        },function(response){
            RestErrorService.errorHandler(response)
            showMessage('Error updating tech',failureAlert)
        })
    };

    function showMessage(message,isSuccess){
        $mdToast.show(
            $mdToast.simple()
              .textContent(message)
              .hideDelay(3000)
        );
    }

    function hideMessage(){
        $scope.data.message.show = false;
        $scope.data.message.class = '';
        $scope.data.message.value = '';
    }

    $scope.dismissAlert = function(){
        hideMessage();
    };

    $scope.getLevelClass = function(skill,l){
      if(l){
        skill = {
          skillLevel: l
        };
      }
      switch(skill.skillLevel){
        case level.NOVICE.value:
          return 'long-time-bg-color';
        case level.ADVANCED_BEGINNER.value:
          return 'novice-bg-color';
        case level.COMPETENT.value:
          return 'expert-bg-color';
        case level.PROFICIENT.value:
          return 'intermediate-bg-color';
        case level.EXPERT.value:
          return 'expert-bg-color';
        default:
          return '';
      }
    };

    $scope.isSelectedLevelClass = function(skill, l){
      if(skill.skillLevel !== l){
        return 'shadow';
      }
      return '';
    }

    $scope.queryTechs = function(query){
      return RestService.getTechs(query).then(function(response){
        return response.data;
      },function(response){
        return [];
      });
    };

    onInit();
}]);