(function () {
  'use strict';

  angular.module('techmatrix').controller('HelpController',[
      '$scope',
      '$location',
      function($scope, $location){

        $scope.goTo = function(path){
          $location.path(path);
        }

      }]);
})()