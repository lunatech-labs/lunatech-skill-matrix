angular.module('techmatrix').service('RestUrlService',[function(){
    var api = {
        getUserProfile:function(userId){
            return '/users/'+userId+'/skills';
        },
        addSkill:function(userId){
            return '/users/'+userId+'/skills';
        },
        removeSkill:function(userId,skillId){
            return '/users/'+userId+'/skill/'+skillId;
        },
        updateSkill:function(userId,skillId){
            return '/users/'+userId+'/skill/'+skillId;
        }
    };

    return api;
}]);