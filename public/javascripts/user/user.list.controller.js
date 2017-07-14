(function () {
  'use strict';

  angular.module('techmatrix').controller('UserListController',[
    '$scope',
    'RestService',
    'techType',
    'level',
    '$cookies',
    '$location',
    'RestErrorService',
    '$mdToast',
    '$mdDialog',
    function($scope,RestService,techType,level,$cookies,$location,RestErrorService,$mdToast,$mdDialog){

      var successAlert = true;
      var failureAlert = false;

      $scope.data = {};
      $scope.data.users = [];

      function onInit(){
        RestService.getAllUsers().then(function(response){
          $scope.data.users = response.data.map(function(user) {
            user.searchFilter = user.firstName + user.lastName + user.email;
            return user;
          });
        },function(response){
          showMessage('Error getting users',false)
        });
      }

      function showMessage(message,isSuccess){
        $mdToast.show(
          $mdToast.simple()
            .textContent(message)
            .hideDelay(3000)
        );
      }

      $scope.remove = function(user){
        var confirm = $mdDialog.confirm()
                  .title('Remove user?')
                  .textContent('All '+ user.firstName + ' ' + user.lastName + ' skills will be remove.')
                  .ok('Remove')
                  .cancel('Cancel');

            $mdDialog.show(confirm).then(function() {
              RestService.removeUser(user.id).then(function(response){
                $scope.data.users = $scope.data.users.filter(function(s){
                    return s.id !== user.id;
                })
                showMessage('User removed', successAlert)
              }, function(response) {
                RestErrorService.errorHandler(response)
                showMessage('Error removing user', failureAlert)
              })
            }, function() {
            });
      };

      onInit();
    }])
})()