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

  $scope.$on('$RESETUSER', function(){
    $scope.getUsers(true);
  });

  $scope.setPredicate = function(predicate){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = false;
    }
  }

  $scope.getDate = function(date){
    return utils.getDate(date);
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
      triggerAlert('Your edits were saved', 'editUserProfile', 'body', 6000);
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
  $scope.userProfileForm = angular.copy(profile);
  $scope.userProfileForm.mySwitch = true;
  $scope.EMAIL_REGEXP = utils.EMAIL_REGEXP;
  Business.lookupservice.getUserTypeCodes().then(function(result){
    if (result) {
      $scope.userTypeCodes = result;
      $scope.userProfileForm.userRole = _.find($scope.userTypeCodes, {'code': $scope.userProfileForm.userTypeCode});
    } else {
      $scope.userTypeCodes = [];
    }
  });
  $scope.saveUserProfile = function(){
    console.log('$scope.userProfileForm', $scope.userProfileForm);
    $scope.userProfileForm.mySwitch = false;
    $scope.ok();
  }

  $scope.cancelUserProfile = function() {
    $scope.cancel()
  }

  $scope.ok = function () {
    // myCheckValue
    // mask form
    $scope.$emit('$TRIGGERLOAD', 'userLoad');

    //validate form
    $scope.userProfileForm.userTypeCode = $scope.userProfileForm.userRole.code;

    var error = false;
    var errorObjt = angular.copy(utils.errorObj);

    if (!$scope.userProfileForm.firstName){
      errorObjt.add('firstName','A first name is required.');
    } else if ($scope.userProfileForm.firstName.length > 80){
      errorObjt.add('firstName','Your first name has exceeded the accepted input length');
    }
    if (!$scope.userProfileForm.lastName){
      errorObjt.add('lastName','A last name is required.');
    } else if ($scope.userProfileForm.lastName.length > 80){
      errorObjt.add('lastName','Your last name has exceeded the accepted input length');
    }
    if (!$scope.userProfileForm.email){
      errorObjt.add('email','A valid email is required.');
    } else if ($scope.userProfileForm.email.length > 80){
      errorObjt.add('email','Your email has exceeded the accepted input length');
    }
    if (!$scope.userProfileForm.organization){
      errorObjt.add('organization','An organization is required.');
    } else if ($scope.userProfileForm.organization.length > 120){
      errorObjt.add('organization','Your organization has exceeded the accepted input length');
    }
    if (!$scope.userProfileForm.userTypeCode){
      errorObjt.add('userRole','A valid user type code is required.');
    }

    if (error) {
      triggerError(errorObjt);
      return false;
    }

    Business.userservice.saveThisProfile($scope.userProfileForm).then(function(result){
      profile = $scope.userProfileForm;
      $scope.$emit('$TRIGGERUNLOAD', 'userLoad');
      $scope.$emit('$TRIGGEREVENT', '$RESETUSER');
      $uiModalInstance.close('success');
    }, function(){
      // triggerAlert();
      console.log('There was an error');
      
      $scope.$emit('$TRIGGERUNLOAD', 'userLoad');
    })
    // Business.userservice.saveCurrentUserProfile($scope.userProfileForm).then(
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);