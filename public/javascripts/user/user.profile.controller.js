angular.module('techmatrix').controller('UserProfileController',[
    '$scope',
    'RestService',
    '$routeParams',
    'techType',
    'level',
    'RestErrorService',
    function($scope,RestService,$routeParams,techType,level,RestErrorService){

    $scope.data = {};
    $scope.data.user = undefined;

    $scope.data.techType = techType;
    $scope.data.level = level;

    function onInit(){
        RestService.getUserProfile($routeParams.userId).then(function(response){
            $scope.data.user = response.data.userSkills;
        },function(response){
            RestErrorService.errorHandler(response)
            console.log(response);
        })
    }

    function addSearchFilter(user){
        user.searchFilter = user.firstName + "," + user.lastName + "," + user.email;
        return user;
    }

    onInit();
}]);