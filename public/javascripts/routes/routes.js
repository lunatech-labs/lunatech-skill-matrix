(function () {
  'use strict';

  angular.module('techmatrix').config(['$routeProvider',function($routeProvider) {
      $routeProvider
      .when('/auth', {
           templateUrl: '/assets/javascripts/auth/login.html',
           controller: 'AuthController'
          })

      .when('/skillmatrix', {
       templateUrl: '/assets/javascripts/skill/skillMatrix.html',
       controller: 'SkillController'
      })
      .when('/skillmatrix/:error', {
       templateUrl: '/assets/javascripts/skill/skillMatrix.html',
       controller: 'SkillController'
      })
      .when('/user/home', {
       templateUrl: '/assets/javascripts/user/home.html',
       controller: 'UserHomeController'
      })
      .otherwise('/skillmatrix');
  }]);
})()