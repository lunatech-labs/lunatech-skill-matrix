(function () {
  'use strict';

  angular.module('techmatrix').service('RestUrlService',[function(){
      var api = {
          getUserProfile:function(userId){
              return '/users/'+userId+'/skills';
          },
          getMyProfile:function(){
              return '/users/me/skillmatrix';
          },
          addSkill:function(){
              return '/users/me/skillmatrix';
          },
          removeSkill:function(skillId){
              return '/users/me/skillmatrix/'+skillId;
          },
          updateSkill:function(skillId){
              return '/users/me/skillmatrix/'+skillId;
          },
          googleAuth:function(token){
              return '/google/auth';
          },
          getTechs:function(params){
            return '/techs/' + encodeURIComponent(params);
          },
          removeUser:function(userId){
            return '/users/' + userId;
          },
          removeTech:function(techId){
            return '/alltech/' + techId;
          },
          updateTech:function(techId){
            return '/alltech/'+techId;
          },
      };

      return api;
  }]);
})()