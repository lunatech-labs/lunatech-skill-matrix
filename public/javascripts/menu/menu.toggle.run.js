(function () {
  'use strict';

  angular.module('techmatrix').run(['$rootScope', '$mdSidenav','$cookies', function($rootScope, $mdSidenav, $cookies){

    $rootScope.toggleMenu = function(){
      $mdSidenav('menu').toggle();
    }
    $rootScope.details = {
      avatar: $cookies.getObject('user') ? $cookies.getObject('user').avatar : '',
      name: $cookies.getObject('user') ? $cookies.getObject('user').full_name : ''
    };

  }]);
})()