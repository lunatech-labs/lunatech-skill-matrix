angular.module('techmatrix').controller('SkillController',[
    '$scope',
    'RestService',
    'techType',
    'level',
    '$routeParams',
    function($scope,RestService,techType,level,$routeParams){

    $scope.data = {};
    $scope.data.skills = [];
    $scope.data.techType = techType;
    $scope.data.level = level;

    $scope.data.loading = false;

    $scope.data.error = undefined;

    function onInit(){
        if($routeParams.error === 'nouser'){
            $scope.data.error = "You aren't logged with any user. Please user the Users page to log in."
        }
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