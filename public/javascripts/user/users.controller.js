angular.module('techmatrix').controller('UsersController',[
    '$scope',
    'RestService',
    function($scope,RestService){

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

    onInit();
}]);