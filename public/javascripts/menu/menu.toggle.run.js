angular.module('techmatrix').run(['$rootScope', '$mdSidenav', function($rootScope, $mdSidenav){

  $rootScope.toggleMenu = function(){
    $mdSidenav('menu').toggle();
  }
}]);