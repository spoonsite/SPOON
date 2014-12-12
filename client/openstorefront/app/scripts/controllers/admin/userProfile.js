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

app.controller('AdminUserProfileCtrl', ['$scope', 'business', function ($scope, Business) {
  $scope.predicate = 'activeStatus';
  $scope.deactivateButtons = false;
  $scope.getUsers = function() {
    Business.userservice.getAllUserProfiles(true).then(function(result){
      console.log('result', result);

      $scope.userProfiles = result? result: []
    }, function() {
      $scope.userProfiles = [];
    })
  }
  $scope.getUsers();
  Business.lookupservice.getUserTypeCodes().then(function(result){
    $scope.userTypes = result? result: []
  }, function() {
    $scope.userTypes = [];
  })


  $scope.setPredicate = function(predicate){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = false;
    }
  }

  $scope.changeActivity = function(user){
    var cont = confirm("You are about to change the active status of a user (Enabled or disabled). Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      if (user.activeStatus === 'A') {
        Business.userservice.deactivateUser(user.username).then(function(){
          $scope.getUsers();
          $scope.deactivateButtons = false;
        }, function(){
          $scope.getUsers();
          $scope.deactivateButtons = false;
        })
      } else {
        Business.userservice.activateUser(user.username).then(function() {
          $scope.getUsers();
          $scope.deactivateButtons = false;
        }, function(){
          $scope.getUsers();
          $scope.deactivateButtons = false;
        })
      }
    }
  }

  $scope.getUserType = function(code){
    var type = _.find($scope.userTypes, {'code': code});
    if (type) {
      return type.description;
    } else {
      return 'End User';
    }
  }


}]);