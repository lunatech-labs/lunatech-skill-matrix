(function () {
  'use strict';

  angular.module('techmatrix').controller('MenuController', [
    '$scope',
    '$location',
    '$mdSidenav',
    function($scope, $location, $mdSidenav){

      $scope.goTo = function(path){
        $mdSidenav('menu').toggle();
        $location.path(path);
      }
  }]);
})()