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

/*global MOCKDATA2, jQuery, confirm*/

app.controller('UserProfileCtrl', ['$scope', 'business', '$rootScope', '$location', '$timeout', function($scope, Business, $rootScope, $location, $timeout) {

  //////////////////////////////////////////////////////////////////////////////
  // Variables
  //////////////////////////////////////////////////////////////////////////////
  var immageHack          = 0;

  $scope.total            = {};
  Business.componentservice.getComponentDetails().then(function(result) {
    $scope.total          = result;
    resetData();
  });
  Business.userservice.getReviews('Dawson TEST').then(function(result){
    if (result) {
      $scope.username = 'Dawson TEST';
      $scope.reviews = result;
      // console.log('result', result);
    }
  });
  $scope._scopename       = 'userprofile';
  $scope.pageTitle        = 'DI2E Storefront Catalog';
  $scope.defaultTitle     = 'Browse Categories';
  Business.userservice.getWatches().then(function(result) {
    if (result) {
      $scope.watches = result;
      $scope.watches = _.sortBy($scope.watches, function(item) {
        return item.componentName;
      });
    } else {
      $scope.watches = null;
    }
  });
  
  $scope.nav              = {
    'current': null,
    'bars': [
      //
      { 'title': 'User Profile', 'include': 'views/userprofiletab.html' },
      { 'title': 'Watches', 'include': 'views/watchestab.html' },
      { 'title': 'Component Reviews', 'include': 'views/feedbacktab.html' }
    //
    ]
  };

  $scope.expertise          = [
    //
    {'value':'1', 'label': 'Less than 1 month'},
    {'value':'2', 'label': 'Less than 3 months'},
    {'value':'3', 'label': 'Less than 6 months'},
    {'value':'4', 'label': 'Less than 1 year'},
    {'value':'5', 'label': 'Less than 3 years'},
    {'value':'6', 'label': 'More than 3 years'}
  //
  ];
  $scope.userRoles          = [
    //
    {'code':'ENDUSER', 'description': 'User'},
    {'code':'DEV', 'description': 'Developer'},
    {'code':'PM', 'description': 'Project Manager'}
  //
  ];

  Business.getProsConsList().then(function(result) {
    if (result) {
      $scope.prosConsList = result;
    } else {
      $scope.prosConsList = null;
    }
  });

  $scope.$on('$includeContentLoaded', function(){
    $timeout(function() {
      $('[data-toggle=\'tooltip\']').tooltip();
    }, 300);
  });

  $scope.EMAIL_REGEXP = /^[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*$/i;


  /***************************************************************
  * This function toggles a checkbox
  ***************************************************************/
/* $scope.changeSwitch = function(tval){
  
 // var myValue = $scope.mySwitch;
  //alert(tval);
  $scope.toggle = true;

  //($scope.mySwitch) ? $scope.mySwitch2 = "YES" : $scope.mySwitch2 = "NO";
  // $scope.mySwitch = true;
  // $scope.mySwitch2 = false;
  //($scope.mySwitch) ? $scope.mySwitch2 = 'false' : $scope.mySwitch2 = 'true';
 
  //$scope.mySwitch = true;
  //$scope.mySwitch2 = true;

  // ($scope.mySwitch) ? alert("true") : alert("not true") ;


  //console.log(myCheckBox);

    if (tval == true)
    {
       $scope.toggle = true;
      // $scope.mySwitch = true;
      // $scope.mySwitch2 = false;
     // alert("I am checked");
   //   $scope.mySwitch2.checked = 'false';
   //   $scope.mycheckbox = false;
    }
    else {
      $scope.toggle = false;
     // $scope.mySwitch = false;
     //  $scope.mySwitch2 = true;
     // alert("I am NOT checked");
    //  $scope.mySwitch2.checked = 'true';
    //  $scope.mycheckbox = true;
    }
  };*/


  /***************************************************************
  * This function converts a timestamp to a displayable date
  ***************************************************************/
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



  /***************************************************************
  * This function adds a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.goToFullPage = function(id){
    var url = $location.absUrl().substring(0, $location.absUrl().length - $location.url().length);
    url = url + '/single?id=' + id;
    window.open(url, 'Component ' + id, 'scrollbars');
    // $location.search({
    //   'id': id
    // });
    // $location.path('/single');
  };

  /***************************************************************
  * This function converts a timestamp to a displayable date
  ***************************************************************/
  $scope.isNewer = function(updateDate, viewDate){
    return parseInt(updateDate) > parseInt(viewDate);
  };

  /***************************************************************
  * This function converts a timestamp to a displayable date
  ***************************************************************/
  $scope.setNotify = function(id, value){
    _.find($scope.watches, {'componentId': id}).notifyFlag = value;
  };


  $scope.$on('$updatedWatches', function(event){/*jshint unused:false*/
    Business.userservice.getWatches().then(function(result){
      if (result) {
        $scope.watches = result;
      }
    });
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


  $scope.toggleCollapse = function(id){
    $('#' + id).collapse('toggle');
  };


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
        component.watched = watch.watched;
        $scope.data.push(component);
      }
    });

  };

  //?fix
  // cancel user profile edits
   $scope.cancelUserProfile = function() {
     // return fields to latest data
     console.log("cancel Edits");

    $scope.mySwitch = false;

     console.log($scope.mySwitch);

/*     alert("sldjkl");
  $scope.mySwitch.checked = false;
  $scope.mySwitch = 0;*/

    Business.userservice.getCurrentUserProfile().then(function(profile) {
      $scope.userProfile = profile;
      $scope.userProfileForm = angular.copy(profile);

      _.each($scope.userTypeCodes, function(element, index, list) { /*jshint unused:false*/
        if (element.code === $scope.userProfileForm.userTypeCode) {
          $scope.userProfileForm.userRole = element;
        }
      });
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

   // myCheckValue

   // mask form
    $scope.$emit("$TRIGGERLOAD", 'userLoad');

    $scope.mySwitch = false;
   // $scope.myCheckValue = false;
  
    //validate form
    $scope.userProfileForm.userTypeCode = $scope.userProfileForm.userRole.code;

    // //mask form and disable save button
    // var success = function(data, status, headers, config) { /*jshint unused:false*/
    //   loadUserProfile();
    //   //Show message toasterg
    // };

    // var failure = function(data, status, headers, config) { /*jshint unused:false*/
    //   //mark fields that are bad (add error class) and show our error messages div
    // };

    // Business.userservice.saveCurrentUserProfile($scope.userProfileForm, success, failure);
    Business.userservice.saveCurrentUserProfile($scope.userProfileForm).then(
      function(data, status, headers, config){ //SUCCESS:: data = return value
        $timeout(function(){
          $scope.$emit("$TRIGGERUNLOAD", 'userLoad');
        }, 1000);
        loadUserProfile();
        console.log(data);
      },
      function(value){ //FAILURE:: value = reason why it failed
        $timeout(function(){
          triggerError(value);
              $scope.$emit("$TRIGGERUNLOAD", 'userLoad');
        }, 1000);
        console.log(value);
      }
    );
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
    var answer = confirm ('You are about to remove a component from your watch list. Are you sure you want to do this?');
    if (answer) {

      var a = _.find($scope.watches, {'componentId': id});

      if (a) {
        $scope.watches.splice(_.indexOf($scope.watches, a), 1);
      }

      Business.userservice.setWatches($scope.watches);
      $scope.$emit('$triggerEvent', '$detailsUpdated', id);
      _.where(MOCKDATA2.componentList, {'componentId': id})[0].watched = false;
      Business.updateCache('component_'+id, _.where(MOCKDATA2.componentList, {'componentId': id})[0]);
    }
  };
}]);

