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

app.controller('LoginCtrl', ['$scope', '$location', 'auth', function ($scope, $location, Auth) {
  $scope.login = function () {
    Auth.login($scope.user).then(function () {
    }, function (error) {
      $scope.error = error.toString();
    });
  };

  // $scope.register = function () {
  //   Auth.register($scope.user).then(function (authUser) { jshint unused: false
  //     $location.path('/');
  //   }, function (error) {
  //     $scope.error = error.toString();
  //   });
  // };

  $scope.logout = function () {
    Auth.logout();
  };

}]);
