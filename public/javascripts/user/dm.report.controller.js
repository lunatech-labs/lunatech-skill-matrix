(function () {
  'use strict';

  angular.module('techmatrix').controller('DMReportController',[
      '$scope',
      'RestService',
      '$cookies',
      function($scope,RestService,$cookies){

      var successAlert = true;
      var failureAlert = false;

      $scope.data = {};
      $scope.data.users = [];


      function onInit(){
        var userId = $cookies.getObject('user') ? $cookies.getObject('user').id : 0;

        RestService.dmReport(userId).then(function(response){
          $scope.data.users = response.data.map(function(u){
            u.entries = u.entries.map(function(e){
              e.occurrence = e.occurrence.split(" ")[0];
              return e;
            })
            return u;
          });
        },function(response){
          console.log(response);
        });
      }

      
      onInit();
  }]);
})()