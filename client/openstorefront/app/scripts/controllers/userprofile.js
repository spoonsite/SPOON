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

/*global MOCKDATA2, jQuery*/

app.controller('UserProfileCtrl', ['$scope', 'business', '$rootScope', function($scope, Business, $rootScope) {

  //////////////////////////////////////////////////////////////////////////////
  // Variables
  //////////////////////////////////////////////////////////////////////////////
  var immageHack          = 0;
  var images              = [
    //
    'images/wess_logo.png',
    'images/core-map-api.png',
    'images/maps-icon.png'
  //
  ];

  $scope.total            = {};
  Business.componentservice.getComponentDetails().then(function(result) {
    $scope.total          = result;
    resetData();
  });
  $scope._scopename       = 'userprofile';
  $scope.pageTitle        = 'DI2E Storefront Catalog';
  $scope.defaultTitle     = 'Browse Categories';
  $scope.watches          = Business.getWatches();
  $scope.feedbackDetails  = [
    //
    {'id': '1', 'date': 'Jan 4, 2014 8:25 am', 'comments': 'This VANTAGE WESS OZONE Widget is really cool', 'author': 'Jim Calhoun'},
    {'id': '2', 'date': '01/05/2014 9:25 am', 'comments': 'This VANTAGE WESS OZONE Widget is really cool', 'author': 'Jill Calhoun'},
    {'id': '3', 'date': '01/06/2014 10:25 am', 'comments': 'This VANTAGE WESS OZONE Widget is really cool', 'author': 'Jay Calhoun'}
  //
  ];
  $scope.nav              = {
    'current': null,
    'bars': [
      //
      { 'title': 'User Profile', 'include': 'views/userprofiletab.html' },
      { 'title': 'Watches', 'include': 'views/watchestab.html' },
      { 'title': 'Component Feedback', 'include': 'views/feedbacktab.html' }
    //
    ]
  };


  $scope.$on('$updatedWatches', function(event){/*jshint unused:false*/
    $scope.watches = Business.getWatches();
    resetData();
  });

  /***************************************************************
  * This function grabs the userCodes
  ***************************************************************/
  Business.lookupservice.getUserTypeCodes().then(function(lookup) {
    $scope.userTypeCodes = lookup;
    loadUserProfile();
  });


  //////////////////////////////////////////////////////////////////////////////
  // Event Watches
  //////////////////////////////////////////////////////////////////////////////
  $rootScope.$on('$profileModal', function(event) { /*jshint unused: false*/
    if ($rootScope.current) {
      $scope.nav.current = $rootScope.current;
    } else {
      $scope.nav.current = 'User Profile';
    }
    resetData();
    // we re-initialize anything else here
  });



  //////////////////////////////////////////////////////////////////////////////
  // Functions
  //////////////////////////////////////////////////////////////////////////////
  /***************************************************************
  * This function takes the watch list, and the total data we got back, and 
  * grabs the data items that are really on the watch list. This will change
  * once we actually have a database to query from.
  ***************************************************************/
  var resetData = function() {
    $scope.data = [];
    _.each($scope.watches, function(watch) {
      var component = _.find($scope.total, {'componentId': watch.componentId});
      if (component) {
        if (immageHack > 2) {
          immageHack = 0;
        }
        component.src = images[immageHack];
        immageHack = immageHack + 1;
        component.watched = watch.watched;
        $scope.data.push(component);
      }
    });
  };

  /***************************************************************
  * Load the User profile 
  ***************************************************************/
  var loadUserProfile = function() {
    //show load mask on form
    Business.userservice.getCurrentUserProfile().then(function(profile) {
      $scope.userProfile = profile;
      $scope.userProfileForm = angular.copy(profile);

      _.each($scope.userTypeCodes, function(element, index, list) { /*jshint unused:false*/
        if (element.code === $scope.userProfileForm.userTypeCode) {
          $scope.userProfileForm.userRole = element;
        }
      });
      //hide load mask
    });
  };

  /***************************************************************
  * Save the user profile
  ***************************************************************/
  $scope.saveUserProfile = function() {
    //validate form
    $scope.userProfileForm.userTypeCode = $scope.userProfileForm.userRole.code;

    //mask form and disable save button
    var success = function(data, status, headers, config) { /*jshint unused:false*/
      loadUserProfile();
      //Show message toaster
    };

    var failure = function(data, status, headers, config) { /*jshint unused:false*/
      //mark fields that are bad (add error class) and show our error messages div
    };
    Business.userservice.saveCurrentUserProfile($scope.userProfileForm, success, failure);
  };


  /***************************************************************
  * This function saves the profile changes in the scope by copying them from
  * the user variable into the backup variable (this function would be where
  * you send the saved data to the database to store it)
  ***************************************************************/ //
  $scope.saveProfileChanges = function() {
    $scope.userBackup = jQuery.extend(true, {}, $scope.user);
  };

  /***************************************************************
  * This function reverts the changes in the profile form by just copying back
  * the details in the backup. This function most likely won't change when
  * we get a database to work with.
  ***************************************************************/
  $scope.revertProfileChanges = function() {
    $scope.user = jQuery.extend(true, {}, $scope.userBackup);
  };


  /***************************************************************
  * This function removes a watch from the watch list. It probably won't change
  * when we get a database to work with.
  * params: id -- the id of the component we want to take off our watch list.
  ***************************************************************/
  $scope.removeFromWatches = function(id){
    var a = _.find($scope.watches, {'componentId': id});

    if (a) {
      $scope.watches.splice(_.indexOf($scope.watches, a), 1);
    }

    Business.setWatches($scope.watches);
    $scope.$emit('$triggerEvent', '$detailsUpdated', id);
    _.where(MOCKDATA2.componentList, {'componentId': id})[0].watched = false;
    Business.updateCache('component_'+id, _.where(MOCKDATA2.componentList, {'componentId': id})[0]);
  };
}]);

