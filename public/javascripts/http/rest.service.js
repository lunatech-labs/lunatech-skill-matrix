angular.module('techmatrix').service('RestService',['$http',function($http){

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
        getUserInfo:function(params){
            return basicRequest('GET','/users', params);
        }
    };

    return api;
}])