'use strict';

app.factory('httpStatusCodeInterceptorFactory', ['$log', '$q', '$timeout', function($log, $q, $timeout){
  // /**
  // * Global error handling
  // */
  var httpStatusCodeInterceptorFactory = {
    // if there is a 500 error, something went bad on the server...
    responseError: function (rejection) {
      console.log('Failed with', rejection.status, 'status');
      if (rejection.status === 500) {
        showServerError(rejection.data);
      }
      return $q.reject(rejection);
    },
    // if there is a validation error, you will see it here.
    response: function(response) {
      var deferred = $q.defer();
      
      $timeout(function(){
        triggerError(response.data);
      });
      deferred.resolve(response);
      return deferred.promise;
    }

  };
  return httpStatusCodeInterceptorFactory;
}])
