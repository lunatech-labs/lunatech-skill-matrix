angular.module('techmatrix').controller('AuthController',[
    '$scope',
    'RestService',
    '$cookies',
    '$location',
    function($scope,RestService,$cookies,$location){
    function onInit(){
        var user = $cookies.getObject('user')
        if(user){
            $location.path("user/home")
        }
    }

    $scope.$on('event:google-plus-signin-success', function (event,authResult) {
        console.log("Signed in...")
        // Useful data for your client-side scripts:
        var profile = authResult.getBasicProfile();
        // The ID token you need to pass to your backend:
        var id_token = authResult.getAuthResponse().id_token;

        //Register the user in backend
        RestService.googleAuth({token: id_token}).then(function(response){
            $cookies.putObject('user',{
                          name: profile.getGivenName(),
                          last_name: profile.getFamilyName(),
                          avatar: profile.getImageUrl(),
                          email: profile.getEmail,
                          full_name: profile.getName(),
                          googleId: profile.getId(),
                          token:id_token
            });
            $location.path('/user/home')
        },function(response){
            showMessage('Error google authentication',failureAlert)
        });
    });
    $scope.$on('event:google-plus-signin-failure', function (event,authResult) {
        // Auth failure or signout detected
        console.log("Authenticate failed");
        console.log(authResult);
        $location.path('/auth')
    });
    onInit();
}]);