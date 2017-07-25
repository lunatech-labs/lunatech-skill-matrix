(function () {
  'use strict';

  angular.module('techmatrix').controller('UserProfileController',[
    '$scope',
    'RestService',
    'techType',
    'level',
    'operations',
    '$cookies',
    '$location',
    'RestErrorService',
    '$mdToast',
    '$mdDialog',
    function($scope,RestService,techType,level,operations,$cookies,$location,RestErrorService,$mdToast,$mdDialog){
      $scope.data = {};
      $scope.data.users = [];
      $scope.data.techs = [];

      var successAlert = true;
      var failureAlert = false;

      $scope.data.filters = [];

      $scope.addFilter = function(){
        $mdDialog.show({
          controller: function(techs, $scope, $mdDialog, level, operations, $mdToast){
            $scope.techs = techs;
            $scope.level = level;
            $scope.operations = operations;
            $scope.showLevel = true;
            function validate(){
              var validLevel =  $scope.operation === operations.ANY ? true : $scope.l;
              return $scope.tech && $scope.operation && validLevel;
            }
            $scope.validateLevel = function(op){
              if(op === operations.ANY){
                $scope.showLevel = false;
                $scope.l = undefined;
              }
            };
            $scope.close = function(){
              $mdDialog.cancel();
            };
            $scope.confirm = function(){
              if(validate()){
                $mdDialog.hide({
                  tech: $scope.tech,
                  operation: $scope.operation,
                  level: $scope.l
                });
              }else{
                $mdToast.show(
                  $mdToast.simple()
                    .textContent('Filter not valid')
                    .hideDelay(3000)
                );
              }
            };
          },
          templateUrl: 'assets/javascripts/user/filter.add.dialog.html',
          locals: {
            techs: $scope.data.techs
          },
          parent: angular.element(document.body),
          clickOutsideToClose:true
        }).then(function(filter){
          $scope.data.filters = $scope.data.filters.filter(function(f){
            return f.tech !== filter.tech;
          });
          $scope.data.filters.push(filter);
          getUsers();
        })
      };

      $scope.removeFilter = function(filter){
        $scope.data.filters.splice($scope.data.filters.indexOf(filter), 1);
        getUsers();
      };

      function getUsers(){
        var data = $scope.data.filters.map(function(filter){
          return {
            tech: filter.tech,
            operation: filter.operation.value,
            level: filter.level ? filter.level.value : filter.level
          };
        });
        RestService.searchUsers(data).then(function(response){
          $scope.data.users = response.data;
        },function(response){
          showMessage('Error getting users',failureAlert)
        })
      }

      function getTechs(){
        RestService.getAllTech().then(function(response){
          $scope.data.techs = response.data;
        },function(response){
          showMessage('Error getting techs',failureAlert)
        })
      }

      function onInit(){
        getUsers();
        getTechs();
      }

      function showMessage(message,isSuccess){
        $mdToast.show(
          $mdToast.simple()
            .textContent(message)
            .hideDelay(3000)
        );
      }


      onInit()
    }])
})()