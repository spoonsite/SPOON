'use strict';

app.factory('auth', ['$rootScope', 'business', '$q', function($rootScope, Business, $q) {

  var authState = {};

  var Auth = {
    // register: function (user) { /*jshint unused: false*/
    //   // return auth.$createUser(user.email, user.password);
    // },
    signedIn: function () {
      return authState.user? true: false;
    },
    login: function (user) { /*jshint unused: false*/
      var deferred = $q.defer();
    // Business.userservice.login(user).then(function(result){
      Business.userservice.getCurrentUserProfile().then(function(result){
        authState.user = result;
        deferred.resolve(result);
        $rootScope.$broadcast('$login', authState.user);
      });
      return deferred.promise;
    },
    logout: function () {
      var deferred = $q.defer();
    // Business.userservice.logout().then(function(){
      authState.user = null;
      deferred.resolve(null);
      // });
      //
      return deferred.promise;
    }
  };

  $rootScope.signedIn = function () {
    return Auth.signedIn();
  };

  return Auth;
}]);