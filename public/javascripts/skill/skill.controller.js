angular.module('techmatrix').controller('SkillController',[
    '$scope',
    'RestService',
    'techType',
    'level',
    function($scope,RestService,techType,level){

    $scope.data = {};
    $scope.data.skills = [];
    $scope.data.techType = techType;
    $scope.data.level = level;

    $scope.data.loading = false;

    function onInit(){
        $scope.data.loading = true;
        RestService.getSkillMatrix().then(function(response){
            $scope.data.loading = false;
            $scope.data.skills = response.data.skills.map(addSearchFilter);
        },function(response){
            $scope.data.loading = false;
            console.log(response)
        });
    }

    function addSearchFilter(skill){
        skill.searchFilter = skill.techName + "," + skill.techType;
        return skill;
    }

    onInit();
}]);