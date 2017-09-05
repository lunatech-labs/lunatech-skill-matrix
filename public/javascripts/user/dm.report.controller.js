(function () {
  'use strict';

  angular.module('techmatrix').controller('DMReportController',[
      '$scope',
      'RestService',
      '$cookies',
      function($scope,RestService,$cookies){

      var successAlert = true;
      var failureAlert = false;

      $scope.data = {};
      $scope.data.users = [
        {
          "name":"Pedro Ferreira",
          entries:[
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "12/08/2017"
            },
            {
              "tech":"Sbt",
              "entryAction":"Modify",
              "occurrence": "12/08/2017"
            }

          ]
        },
        {
          "name":"Tanya Moldovan",
          entries:[
            {
              "tech":"Scala",
              "entryAction":"Add",
              "occurrence": "13/08/2017"
            },
            {
              "tech":"Erlang",
              "entryAction":"Remove",
              "occurrence": "12/08/2017"
            }
          ]
        }
      ];

      $scope.currentNavItem = 0;


      function onInit(){

      }

      
      onInit();
  }]);
})()