angular.module('techmatrix').run(['$rootScope','$cookies','$location','$q',function($rootScope,$cookies,$location,$q) {
   $rootScope.$on('$locationChangeStart',function(event, next, current){
       var user = $cookies.getObject("user")
       $rootScope.auth = (user !== undefined)

       if(!$rootScope.auth && $location.path() !== 'auth'){
            $location.path("auth")
       }
   });

    $rootScope.logout = function(){
        console.log("Logging out...")
        console.log(gapi);
//        gapi.load('auth2', function () {
//          var auth2 = gapi.auth2.getAuthInstance();
//          auth2.signOut().then(function () {
//              $cookies.remove("user");
//              console.log('User signed out.');
//              $location.path("auth");
//          });
//      });
//      //TODO sign out the user from google, make his token expire
      $cookies.remove("user");
      console.log('User signed out.');
      $location.path("auth");
      }
}]);