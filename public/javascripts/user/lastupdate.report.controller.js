(function () {
  'use strict';

  angular.module('techmatrix').controller('LastUpdateReportController',[
      '$scope',
      'RestService',
      '$cookies',
      function($scope,RestService,$cookies){

      var successAlert = true;
      var failureAlert = false;

      $scope.data = {};
      $scope.data.users = [];
      $scope.data.selectedUsers = [];
      $scope.data.letters = 'abcdefghijklmnopqrstuvwxyz'.split('');
      $scope.data.selectedLetters = [];

      function onInit(){
        RestService.lastUpdateReport().then(function(response){
          $scope.data.users = response.data.map(function(u){
            u.entries = u.entries.map(function(e){
              e.occurrence = e.occurrence.split(" ")[0];
              return e;
            })
            return u;
          });
          refreshSelectedUsers();
        },function(response){
          console.log(response);
        });
      }


      $scope.selected = function(letter){
        if($scope.data.selectedLetters.indexOf(letter) > -1){
          return 'md-primary';
        } else {
          return '';
        }
      }

      $scope.toggle = function(letter){
        var index = $scope.data.selectedLetters.indexOf(letter);
        if (index > -1) {
          $scope.data.selectedLetters.splice(index, 1);
        } else {
          $scope.data.selectedLetters.push(letter);
        }
        refreshSelectedUsers();
      }

      function refreshSelectedUsers(){
        if ($scope.data.selectedLetters.length) {
          $scope.data.selectedUsers = $scope.data.users.filter(function(u){
            return $scope.data.selectedLetters.indexOf(u.name.charAt(0).toLowerCase()) > -1;
          });
        } else {
          $scope.data.selectedUsers = $scope.data.users.filter(function(u){ return true; });
        }
      }
      
      onInit();
  }]);
})()