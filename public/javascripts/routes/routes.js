angular.module('techmatrix').config(['$routeProvider',function($routeProvider) {
    $routeProvider
    .when('/skillmatrix', {
     templateUrl: '/assets/javascripts/skill/skillMatrix.html',
     controller: 'SkillController'
    })
    .when('/skillmatrix/:error', {
     templateUrl: '/assets/javascripts/skill/skillMatrix.html',
     controller: 'SkillController'
    })
    .when('/users', {
     templateUrl: '/assets/javascripts/user/users.html',
     controller: 'UsersController'
    })
    .when('/user/profile/:userId', {
     templateUrl: '/assets/javascripts/user/userProfile.html',
     controller: 'UserProfileController'
    })
    .when('/user/home', {
     templateUrl: '/assets/javascripts/user/home.html',
     controller: 'UserHomeController'
    })
    .otherwise('/skillmatrix');
}]);