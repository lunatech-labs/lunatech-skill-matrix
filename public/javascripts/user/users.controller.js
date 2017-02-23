angular.module('techmatrix').controller('UsersController',[
    '$scope',
    'RestService',
    '$cookies',
    '$location',
    function($scope,RestService,$cookies,$location){

    $scope.data = {};
    $scope.data.users = [];

    function onInit(){
        RestService.getAllUsers().then(function(response){
            $scope.data.users = response.data.users.map(addSearchFilter);
        },function(response){
            console.log(response);
        })
    }

    function addSearchFilter(user){
        user.searchFilter = user.firstName + "," + user.lastName + "," + user.email;
        return user;
    }

    $scope.logIn = function(user){
        $cookies.putObject('user',user);
        $location.path('/user/home')
    }

    onInit();
}]);