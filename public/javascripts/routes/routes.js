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
      .when('/user/list', {
        templateUrl: '/assets/javascripts/user/list.html',
        controller: 'UserListController'
      })
      .when('/alltech/list', {
         templateUrl: '/assets/javascripts/tech/list.html',
         controller: 'TechListController'
      })
      .when('/user/profile', {
        templateUrl: '/assets/javascripts/user/profile.html',
        controller: 'UserProfileController'
      })
      .when('/user/report', {
        templateUrl: '/assets/javascripts/user/dm.report.html',
        controller: 'DMReportController'
      })
      .when('/help', {
       templateUrl: '/assets/javascripts/help/help.html',
       controller: 'HelpController'
      })
      .otherwise('/skillmatrix');
  }]);
})()