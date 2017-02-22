angular.module('techmatrix').config(['$routeProvider',function($routeProvider) {
    $routeProvider
    .when('/skillmatrix', {
     templateUrl: '/assets/javascripts/skill/skillMatrix.html',
     controller: 'SkillController'
    })
    .when('/user/:userId', {
     templateUrl: '/assets/javascripts/user/userSkills.html',
     controller: 'UserController'
    })
    .otherwise('/skillmatrix');
}]);