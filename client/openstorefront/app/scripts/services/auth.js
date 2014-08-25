/* 
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

'use strict';

app.factory('auth', ['$rootScope', 'business', '$q', function($rootScope, Business, $q) {

  // This variable will hold the state. (there is only 1 per factory -- singleton)  
  var authState = {};

  // this is what we'll return so that they can do their functionality
  var Auth = {
    signedIn: function () {
      return authState.user? true: false;
    },
    login: function (user) { /*jshint unused: false*/
      var deferred = $q.defer();
    // Business.userservice.login(user).then(function(result){
      Business.userservice.getCurrentUserProfile().then(function(result){
        if (result) {
          authState.user = result;
          deferred.resolve(result);
          $rootScope.$broadcast('$login', authState.user);
        } else {
          authState.user = null;
          deferred.reject('There was an error retrieving user information');
        }
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

  return Auth;
}]);