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

/*global MOCKDATA2, jQuery, confirm, triggerError*/

app.controller('UserProfileCtrl', ['$scope', 'business', '$rootScope', '$location', '$timeout', function($scope, Business, $rootScope, $location, $timeout) {

  //////////////////////////////////////////////////////////////////////////////
  // Variables
  //////////////////////////////////////////////////////////////////////////////
  $scope.total            = {};
  $scope._scopename       = 'userprofile';
  $scope.pageTitle        = 'DI2E Storefront Catalog';
  $scope.defaultTitle     = 'Browse Categories';
  $scope.review           = null;
  $scope.user             = {};
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


  Business.userservice.getCurrentUserProfile().then(function(result){
    if (result) {
      $scope.user.info = result;
      // console.log('result', result);
      
    }
  });

  $scope.log = $rootScope.log;

  $scope.old = $scope.nav.current;
  $scope.$watch('nav', function(){
    if ($scope.old && $scope.nav.current !== $scope.old) {
      $rootScope.sendPageView('Modal-ProfileModal-tabSwitchTo-'+$scope.nav.current);
    }
    $scope.old = $scope.nav.current;
  }, true);

  Business.componentservice.getComponentDetails().then(function(result) {
    if (result) {
      $scope.total = result;
    } else {
      $scope.total = null;
    }
    resetData();
  });

  // TODO: Set this up so it is actually calling with the user's username. 
  $scope.getReviews = function() {
    Business.userservice.getReviews('ANONYMOUS').then(function(result){
      if (result) {
        console.log('result', result);
        
        $scope.username = 'ANONYMOUS';
        $scope.reviews = result;
      } else {
        $scope.reviews = null;
      }
    });  
  }
  $scope.getReviews();
  $scope.$on('$newReview', function(){
    $scope.getReviews();
  })

  Business.userservice.getReviews('ANONYMOUS').then(function(result){
    if (result) {
      console.log('result', result);
      
      $scope.username = 'ANONYMOUS';
      $scope.reviews = result;
    } else {
      $scope.reviews = null;
    }
  });
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
  Business.lookupservice.getExpertise().then(function(result){
    if (result) {
      $scope.expertise = result;
    } else {
      $scope.expertise = [];
    }
  });
  Business.lookupservice.getUserTypeCodes().then(function(result){
    if (result) {
      $scope.userTypeCodes = result;
    } else {
      $scope.userTypeCodes = [];
    }
    loadUserProfile();
  });
  Business.getProsConsList().then(function(result) {
    if (result) {
      $scope.prosConsList = result;
    } else {
      $scope.prosConsList = null;
    }
  });



  $scope.EMAIL_REGEXP = /^[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*$/i;


  //////////////////////////////////////////////////////////////////////////////
  // Functions
  //////////////////////////////////////////////////////////////////////////////

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

  $scope.toggleCollapse = function(id){
    $('#' + id).collapse('toggle');
  };

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
        component.watched = watch.watched;
        $scope.data.push(component);
      }
    });
  };

  $scope.cancelUserProfile = function() {

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
    $scope.$emit('$TRIGGERLOAD', 'userLoad');

    $scope.mySwitch = false;

    //validate form
    $scope.userProfileForm.userTypeCode = $scope.userProfileForm.userRole.code;

    // Business.userservice.saveCurrentUserProfile($scope.userProfileForm, success, failure);
    Business.userservice.saveCurrentUserProfile($scope.userProfileForm).then(
      function(data, status, headers, config){ /* jshint unused:false */
        //SUCCESS:: data = return value
        $scope.$emit('$TRIGGERUNLOAD', 'userLoad');
        $scope.$emit('$triggerEvent', '$RESETUSER');
      },
      function(value){ //FAILURE:: value = reason why it failed
        triggerError(value);
        $scope.$emit('$TRIGGERUNLOAD', 'userLoad');
      });
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

  $scope.deleteReview = function(reviewId, componentId) {
    console.log('reviewId', reviewId);
    Business.componentservice.deleteReview(componentId, reviewId).then(function(result) {
      $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', componentId);
      $scope.$emit('$TRIGGEREVENT', '$newReview');
    });
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


  $scope.saveNotifyChange = function(id) {
    Business.userservice.setWatches($scope.watches);
    Business.updateCache('component_'+id, _.where(MOCKDATA2.componentList, {'componentId': id})[0]);
  };

  $scope.$on('$includeContentLoaded', function(){
    $timeout(function() {
      $('[data-toggle=\'tooltip\']').tooltip();
    }, 300);
  });

  
  $scope.$on('$updatedWatches', function(event){/*jshint unused:false*/
    Business.userservice.getWatches().then(function(result){
      if (result) {
        $scope.watches = result;
      }
    });
    resetData();
  });


  //////////////////////////////////////////////////////////////////////////////
  // Event Watches
  //////////////////////////////////////////////////////////////////////////////
  $rootScope.$on('$profileModal', function(event) { /*jshint unused: false*/
    $scope.old = null;
    if ($rootScope.current) {
      $scope.nav.current = $rootScope.current;
    } else {
      $scope.nav.current = 'User Profile';
    }
    resetData();
    // we re-initialize anything else here
  });

}]);

