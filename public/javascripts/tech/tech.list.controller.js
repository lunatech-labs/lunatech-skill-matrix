(function () {
  'use strict';

  angular.module('techmatrix').controller('TechListController',[
    '$scope',
    'RestService',
    'techType',
    'level',
    '$cookies',
    '$location',
    'RestErrorService',
    '$mdToast',
    '$mdDialog',
    function($scope,RestService,techType,level,$cookies,$location,RestErrorService,$mdToast,$mdDialog){

      var successAlert = true;
      var failureAlert = false;

      $scope.data = {};
      $scope.data.allTech = [];
      $scope.data.techType = techType;

      $scope.data.groupedTech = {};
      $scope.data.groupedTechOld = {};

      $scope.data.currentUpdatingName = '';

      function onInit(){
        RestService.getAllTech().then(function(response){
          $scope.data.allTech = response.data.map(addSearchFilter);
          angular.forEach($scope.data.techType, function(techType){
            $scope.data.groupedTech[techType.value] = $scope.data.allTech.filter(function(s){
                return s.techType === techType.value
            })
          });

        },function(response){
          RestErrorService.errorHandler(response)
          $scope.data.allTech = [];
          showMessage('Error getting tech',false)
        });
      }

      function addSearchFilter(tech){
        tech.searchFilter = tech.name + "," + tech.techType;
        return tech;
      }

      function updateSearchFilter(tech){
              tech.searchFilter = tech.name + "," + tech.techType;
              return tech;
            }

      function showMessage(message,isSuccess){
        $mdToast.show(
          $mdToast.simple()
            .textContent(message)
            .hideDelay(3000)
        );
      }

      $scope.isSelectedTypeClass = function(tech, t){
              if(tech.techType !== t){
                return 'shadow';
              }
              return '';
            }

      $scope.updateTech = function(tech){
                angular.forEach($scope.data.allTech,function(s){
                  if(tech.id !== s.id){
                    s.updating = false;
                  }
                  s.isUpdating = false;
                });
                tech.updating = !tech.updating;
            };

      $scope.editTechName = function(techLabel, tech) {
        var data = {
                     techId:tech.id,
                     body:{
                        label:techLabel,
                        name:tech.name,
                        techType:tech.techType
                     }
                     };

        RestService.updateTech(data).then(function(response){
            tech = updateSearchFilter(tech)

            $scope.data.groupedTech[tech.techType] = $scope.data.groupedTech[tech.techType].map(function(s){
              if (s.id == tech.id) {
                s.name = tech.name
                s.searchFilter = tech.searchFilter
              }
              return s;
            });

            showMessage('Tech updated',successAlert)
            tech.isUpdating = false
        },function(response){
            tech.name = $scope.data.currentUpdatingName;
            RestErrorService.errorHandler(response)
            showMessage('Error updating tech',failureAlert)
        })

      }

      $scope.beginUpdatingName = function(tech) {
        $scope.data.allTech = $scope.data.allTech.map(function(t) {
            t.isUpdating = false;
            t.updating = false;
            return t;
        })




        tech.isUpdating = true;
        $scope.data.currentUpdatingName = tech.name;
      }

      $scope.cancel = function(tech) {
        tech.isUpdating = false
        tech.name = $scope.data.currentUpdatingName;
      }

      $scope.finishUpdatingTechType = function(tech, techType){
                var data = {
                    techId:tech.id,
                    body:{
                        name:tech.name,
                        techType:techType.value
                    }
                };
                RestService.updateTech(data).then(function(response){
                    $scope.data.groupedTech[tech.techType] = $scope.data.groupedTech[tech.techType].filter(function(s){
                       return s.id !== tech.id;
                    })
                    tech.techType = response.data.techType;
                    tech = updateSearchFilter(tech)
                    $scope.data.groupedTech[tech.techType].push(tech)
                    showMessage('Tech updated',successAlert)
                },function(response){
                    RestErrorService.errorHandler(response)
                    showMessage('Error updating tech',failureAlert)
                })
            };

      $scope.removeTech = function(tech){
        var confirm = $mdDialog.confirm()
                  .title('Remove tech?')
                  .textContent('The '+ tech.name + 'tech will be removed along side all the user skills that contain it?')
                  .ok('Remove')
                  .cancel('Cancel');

            $mdDialog.show(confirm).then(function() {
              RestService.removeTech(tech.id).then(function(response){
                $scope.data.allTech = $scope.data.allTech.filter(function(s){
                    return s.id !== tech.id;
                })
                $scope.data.groupedTech[tech.techType] = $scope.data.groupedTech[tech.techType].filter(function(s){
                    return s.id !== tech.id;
                })
                showMessage('Tech removed', successAlert)
              }, function(response) {
                RestErrorService.errorHandler(response)
                showMessage('Error removing user', failureAlert)
              })
            }, function() {
            });
      };

      onInit();
    }])
})()
