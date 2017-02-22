angular.module('techmatrix').service('RestUrlService',[function(){
    var api = {
        getUserProfile:function(userId){
            return '/users/'+userId+'/skills';
        }
    };

    return api;
}]);