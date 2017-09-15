(function () {
  'use strict';

  angular.module('techmatrix').service('RestService',['$http','RestUrlService','$cookies',function($http,RestUrlService,$cookies){

      var baseUrl = '';

      function AuthenticatedRequest(method,url,data){
          return $http({
                method: method,
                url: url,
                data:data,
                headers: {
                  "X-AUTH-TOKEN": $cookies.getObject("user") !== undefined ? $cookies.getObject("user").token : ''
                }
              });
      }

      function basicRequest(method,url,data){
          return $http({
            method: method,
            url: url,
            data:data
          });
      }

      var api = {
          getSkillMatrix:function(){
              return AuthenticatedRequest('GET','/skillmatrix');
          },
          getAllUsers: function(){
              return AuthenticatedRequest('GET','/users');
          },
          searchUsers: function(data){
              return AuthenticatedRequest('POST', '/users/search', data);
          },
          getUserProfile:function(params){
              var url = RestUrlService.getUserProfile(params);
              return AuthenticatedRequest('GET',url);
          },
          getMyProfile:function(params){
              var url = RestUrlService.getMyProfile(params);
              return AuthenticatedRequest('GET',url);
          },
          addSKill:function(data){
              var url = RestUrlService.addSkill()
              return AuthenticatedRequest('POST',url,data.body);
          },
          removeSkill:function(params){
              var url = RestUrlService.removeSkill(params.skillId);
              return AuthenticatedRequest('DELETE',url);
          },
          updateSkill:function(data){
              var url = RestUrlService.updateSkill(data.skillId);
              return AuthenticatedRequest('PUT',url,data.body);
          },
          googleAuth:function(data){
              var url = RestUrlService.googleAuth();
              return basicRequest('POST',url,data);
          },
          getTechs:function(params){
            var url = RestUrlService.getTechs(params);
            return AuthenticatedRequest('GET',url);
          },
          removeUser:function(userId){
             var url = RestUrlService.removeUser(userId);
             return AuthenticatedRequest('DELETE',url);
          },
          getAllTech: function(){
             return AuthenticatedRequest('GET','/alltech');
          },
          removeTech:function(techId){
             var url = RestUrlService.removeTech(techId);
             return AuthenticatedRequest('DELETE',url);
          },
          updateTech:function(data){
             var url = RestUrlService.updateTech(data.techId);
             return AuthenticatedRequest('PUT',url,data.body);
          },
          dmReport:function(data){
             var url = RestUrlService.dmReport(data);
             return AuthenticatedRequest('GET', url);
          }
      };

      return api;
  }])
})()