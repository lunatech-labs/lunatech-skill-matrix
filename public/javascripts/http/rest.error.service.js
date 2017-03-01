angular.module('techmatrix').service('RestErrorService',['$rootScope',function($rootScope){

    var api = {
        errorHandler:function(response){
            if(response.status === 401){
                $rootScope.logout();
            }else {
                console.log(response);
            }
        }
    };

    return api;
}])