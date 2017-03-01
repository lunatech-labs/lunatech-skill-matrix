angular.module('techmatrix').controller('UserHomeController',[
    '$scope',
    'RestService',
    'techType',
    'level',
    '$cookies',
    '$location',
    'RestErrorService',
    function($scope,RestService,techType,level,$cookies,$location,RestErrorService){

    var successAlert = true;
    var failureAlert = false;

    $scope.data = {};

    $scope.data.techType = techType;
    $scope.data.level = level;

    $scope.data.user = undefined;

    $scope.data.message ={
        show:false,
        value: '',
        class:''
    };

    $scope.data.skillForm = {
        name:undefined,
        techType:$scope.data.techType['LANGUAGE'],
        skillLevel:$scope.data.level['COMFORTABLE']
    }

    function onInit(){
        $scope.data.user = $cookies.getObject('user');
        if($scope.data.user === undefined){
            $location.path('/skillmatrix/nouser');
        }else{
            RestService.getMyProfile($scope.data.user.id).then(function(response){
                $scope.data.user.skills = response.data.userSkills.skill.map(addSearchFilter);
                $scope.data.newInput = true;
            },function(response){
                RestErrorService.errorHandler(response)
                $scope.data.user.skills = [];
                $scope.data.newInput = true;
                showMessage('Error getting user skills',failureAlert);
            });
        }
    }

    function addSearchFilter(skill){
        skill.searchFilter = skill.tech.name + "," + skill.tech.techType + "," + skill.skillLevel;
        return skill;
    }

    $scope.addSkill = function(){
        if(isValidateForm()){
            hideMessage();
            var data = {
                userId:$scope.data.user.id,
                body:{
                    tech:{
                        name:$scope.data.skillForm.name,
                        techType:$scope.data.skillForm.techType.value
                    },
                    skillLevel:$scope.data.skillForm.skillLevel.value
                }
            };
            RestService.addSKill(data).then(function(response){
                $scope.data.skillForm = getNewSkillForm();
                $scope.data.user.skills.push(addSearchFilter(response.data.skillAdded));
                $scope.data.newInput = true;
                showMessage('Tech added',successAlert);
            },function(response){
                RestErrorService.errorHandler(response)
                showMessage('Error adding tech',failureAlert);
            });

        }else{
            showMessage('Please fill all field before adding a tech',failureAlert);
        }
    };


    function isValidateForm(){
        return $scope.data.skillForm.name !== undefined &&
        $scope.data.skillForm.name !== ''&&
        $scope.data.skillForm.techType !== undefined &&
        $scope.data.skillForm.skillLevel !== undefined;
    }

    function getNewSkillForm(){
        return {
           name:undefined,
           techType:$scope.data.techType['LANGUAGE'],
           skillLevel:$scope.data.level['COMFORTABLE']
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
            showMessage('Tech removed',successAlert);
        },function(response){
            RestErrorService.errorHandler(response)
            showMessage('Error removing tech',failureAlert);
        });
    };

    $scope.updateSkill = function(skill){
        angular.forEach($scope.data.user.skills,function(s){
            s.updating = false;
        });
        skill.updating = true;
        $scope.data.updatingSkill = $scope.data.level[skill.skillLevel]
    };

    $scope.finishUpdatingSkill = function(skill){
        var data = {
            userId:$scope.data.user.id,
            skillId:skill.id,
            body:{
                tech:{
                    id:skill.tech.id,
                    name:skill.tech.name,
                    techType:skill.tech.techType
                },
                skillLevel:$scope.data.updatingSkill.value
            }
        };
        RestService.updateSkill(data).then(function(response){
            skill.updating = false;
            skill.skillLevel = response.data.updatedSkill.skillLevel;
            showMessage('Tech updated',successAlert)
        },function(response){
            RestErrorService.errorHandler(response)
            showMessage('Error updating tech',failureAlert)
        })
    };

    function showMessage(message,isSuccess){
        $scope.data.message.show = true;
        $scope.data.message.class = isSuccess ? 'alert-success' : 'alert-danger';
        $scope.data.message.value = message;
    }

    function hideMessage(){
        $scope.data.message.show = false;
        $scope.data.message.class = '';
        $scope.data.message.value = '';
    }

    $scope.dismissAlert = function(){
        hideMessage();
    };

    onInit();
}]);