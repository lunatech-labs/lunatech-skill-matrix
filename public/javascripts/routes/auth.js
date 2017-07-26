(function () {
  'use strict';

  angular.module('techmatrix').run([
      '$rootScope',
      '$cookies',
      '$location',
      'GoogleApi',
      'accessLevel',function($rootScope,$cookies,$location,GoogleApi,accessLevel) {

      $rootScope.accessLevel = accessLevel;
      $rootScope.validateAccessLevel = function(userLevel, access){
        if (userLevel === accessLevel.Admin.value){ return true }
        else if (userLevel === accessLevel.Management.value) { return access === accessLevel.Management || access === accessLevel.Basic}
        else if (userLevel === accessLevel.Basic.value) { return access === accessLevel.Basic}
      };

     $rootScope.$on('$locationChangeStart',function(event, next, current){
         var user = $cookies.getObject("user")
         $rootScope.auth = (user !== undefined)

         if(!$rootScope.auth && $location.path() !== 'auth'){
              $location.path("auth")
         }

         $rootScope.details = {
           avatar: user ? user.avatar : '',
           name: user ? user.full_name : '',
           accessLevel: user ? user.accessLevel : 'Basic'
         };
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