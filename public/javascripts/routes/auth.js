angular.module('techmatrix').run(['$rootScope','$cookies','$location','$q',function($rootScope,$cookies,$location,$q) {
   $rootScope.$on('$locationChangeStart',function(event, next, current){
       var user = $cookies.getObject("user")
       $rootScope.auth = (user !== undefined)

       if(!$rootScope.auth && $location.path() !== 'auth'){
            $location.path("auth")
       }
   });

    $rootScope.logout = function(){
//        gapi.load('auth2', function () {
//          var googleAuthObj =
//          gapi.auth2.init({
//              client_id: "1019050035781-53idbaq625ek9o8dkp2ig3i52q36m8s0.apps.googleusercontent.com",
//              cookie_policy: 'single_host_origin'
//          });
//
//          var auth2 = gapi.auth2.getAuthInstance();
//          auth2.signOut().then(function () {
//              $cookies.remove("user");
//              console.log('User signed out.');
//              $location.path("auth");
//          });
//      });
      //TODO sign out the user from google, make his token expire
      $cookies.remove("user");
      console.log('User signed out.');
      $location.path("auth");
    }
}]);