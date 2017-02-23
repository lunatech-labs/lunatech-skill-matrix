angular.module('techmatrix').service('RestService',['$http','RestUrlService',function($http,RestUrlService){

    var baseUrl = '';

    function basicRequest(method,url,data){
        return $http({
          method: method,
          url: url,
          data:data
        });
    }

    var api = {
        getSkillMatrix:function(){
            return basicRequest('GET','/skillmatrix');
        },
        getAllUsers: function(){
            return basicRequest('GET','/users');
        },
        getUserProfile:function(params){
            var url = RestUrlService.getUserProfile(params);
            return basicRequest('GET',url);
        },
        addSKill:function(data){
            var url = RestUrlService.addSkill(data.userId)
            return basicRequest('POST',url,data.body);
        },
        removeSkill:function(params){
            var url = RestUrlService.removeSkill(params.userId,params.skillId);
            return basicRequest('DELETE',url);
        }
    };

    return api;
}])