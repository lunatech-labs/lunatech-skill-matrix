(function () {
  'use strict';

  angular.module('techmatrix').run([
      '$rootScope',
      '$cookies',
      '$location',
      'GoogleApi',function($rootScope,$cookies,$location,GoogleApi) {
     $rootScope.$on('$locationChangeStart',function(event, next, current){
         var user = $cookies.getObject("user")
         $rootScope.auth = (user !== undefined)

         if(!$rootScope.auth && $location.path() !== 'auth'){
              $location.path("auth")
         }
     });

      $rootScope.logout = function(){
          console.log("Logging out...")
          GoogleApi.load().then(function(){
              GoogleApi.signOut().then(function(){
                  $rootScope.auth = false;
                  $cookies.remove("user");
                  $location.path("auth");
              });
          })
      }
  }]);
})()