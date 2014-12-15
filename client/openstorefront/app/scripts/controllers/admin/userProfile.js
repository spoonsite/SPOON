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

app.controller('AdminUserProfileCtrl', ['$scope', 'business', '$timeout', '$uiModal', function ($scope, Business, $timeout, $uiModal) {
  $scope.predicate = 'activeStatus';
  $scope.deactivateButtons = false;
  $scope.getUsers = function(override) {
    Business.userservice.getAllUserProfiles(true, override).then(function(result){
      console.log('result', result);

      $scope.userProfiles = result? result: []
    }, function() {
      $scope.userProfiles = [];
    })
  }
  $scope.getUsers(false);
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

  $scope.getDate = function(date){
    if (date)
    {
      var d = new Date(date);
      var currDate = d.getDate();
      var currMonth = d.getMonth();
      var currYear = d.getFullYear();
      return ((currMonth + 1) + '/' + currDate + '/' + currYear);
    }
    return null;
  };

  $scope.editUserProfile = function(profile){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/userprofiletab.html',
      controller: 'adminEditUserProfileCtrl',
      size: 'sm',
      resolve: {
        profile: function () {
          return profile;
        }
      }
    });

    modalInstance.result.then(function (result) {
      //do something
    }, function () {
      // cancled or failed
    });
  }


  $scope.changeActivity = function(user){
    var cont = confirm("You are about to change the active status of a user (Enabled or disabled). Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      if (user.activeStatus === 'A') {
        $scope.$emit('$TRIGGERLOAD', 'adminUserProfile');
        Business.userservice.deactivateUser(user.username).then(function(){
          $timeout(function(){
            $scope.getUsers(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminUserProfile');
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getUsers(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminUserProfile');
          }, 1000);
        })
      } else {
        $scope.$emit('$TRIGGERLOAD', 'adminUserProfile');
        Business.userservice.activateUser(user.username).then(function() {
          $timeout(function(){
            $scope.getUsers(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminUserProfile');
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getUsers(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminUserProfile');
          }, 1000);
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

app.controller('adminEditUserProfileCtrl',['$scope', '$uiModalInstance', 'profile', 'business', function ($scope, $uiModalInstance, profile, Business) {
  $scope.userProfileForm = profile;

  $scope.ok = function () {
    $uiModalInstance.close('success');
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);