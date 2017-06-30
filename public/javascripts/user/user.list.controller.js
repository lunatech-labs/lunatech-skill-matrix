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

      $scope.data = {};
      $scope.data.users = [];

      function onInit(){
        RestService.getAllUsers().then(function(response){
          $scope.data.users = response.data.map(function(user) {
            user.searchFilter = user.firstName + user.lastName + user.email;
            return user;
          });
        },function(response){
          //TODO
        });
      }

      $scope.remove = function(user){
        var confirm = $mdDialog.confirm()
                  .title('Remove user?')
                  .textContent('All '+ user.firstName + ' ' + user.lastName + ' skills will be remove.')
                  .ok('Remove')
                  .cancel('Cancel');

            $mdDialog.show(confirm).then(function() {
              console.log('Confirm') //TODO
            }, function() {
              console.log('Cancel') //TODO
            });
      };

      $scope.edit = function(user){
        //TODO
      }

      onInit();
    }])
})()