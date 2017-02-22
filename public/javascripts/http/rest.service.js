angular.module('techmatrix').service('RestService',['$http','RestUrlService',function($http,RestUrlService){

    var baseUrl = '';

    function basicRequest(method,url){
        return $http({
          method: method,
          url: url
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
        }
    };

    return api;
}])