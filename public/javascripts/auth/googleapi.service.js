(function () {
  'use strict';

  angular.module('techmatrix').service('GoogleApi',['$q','config',function($q,config){
      var self = this;
      this.load = function(){
        var deferred = $q.defer();
        gapi.load('auth2', function(){
          var auth2 = gapi.auth2.init({
              client_id:config.oAuth.clientId
          });
          //normally I'd just pass resolve and reject, but page keeps crashing (probably gapi bug)
          auth2.then(function(){
            deferred.resolve();
          });
          addAuth2Functions(auth2);
        });
        return deferred.promise;
      };

      function addAuth2Functions(auth2){
        self.signIn = function() {
          var deferred = $q.defer();
          auth2.signIn().then(deferred.resolve, deferred.reject);
          return deferred.promise;
        };

        self.isSignedIn = function(){
          return auth2.isSignedIn.get();
        }

        self.signOut = function(){
          var deferred = $q.defer();
          auth2.signOut().then(deferred.resolve, deferred.reject);
          return deferred.promise;
        };
      }
  }])
})()