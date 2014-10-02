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

app.controller('UserProfileCtrl', ['$scope', 'business', '$rootScope', '$location', '$timeout', '$q', function($scope, Business, $rootScope, $location, $timeout, $q) {

  //////////////////////////////////////////////////////////////////////////////
  // Variables
  //////////////////////////////////////////////////////////////////////////////
  $scope.total            = {};
  $scope.userProfileForm  = {};
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
  $scope.$on('$newReview', function(){
    loadUserProfile();
  })
  $scope.$on('$RESETUSER', function(){
    loadUserProfile();
  })

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
      loadUserProfile();
    } else {
      $scope.userTypeCodes = [];
    }
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
    var update = new Date(updateDate);
    var view = new Date(viewDate);
    return view < update;
  };

  /***************************************************************
  * This function converts a timestamp to a displayable date
  ***************************************************************/
  $scope.setNotify = function(id, value){
    _.find($scope.watches, {'componentId': id}).notifyFlg = value;
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

  /***************************************************************
  * Load the User profile 
  ***************************************************************/
  var loadUserProfile = function() {
    //show load mask on form
    Business.userservice.getCurrentUserProfile(true).then(function(profile) {
      if (profile) {
        // console.log('profile', profile);
        
        $scope.userProfile = profile;
        $scope.userProfileForm = angular.copy(profile);
        // console.log('Code', $scope.userProfile.userTypeCode);
        // console.log('list', $scope.userTypeCodes);
        // console.log('found', _.find($scope.userTypeCodes, {'code': $scope.userProfile.userTypeCode}));
        $scope.userProfileForm.userRole = _.find($scope.userTypeCodes, {'code': $scope.userProfile.userTypeCode});
        if ($scope.user.info && $scope.user.info.username) {
          Business.userservice.getReviews($scope.user.info.username).then(function(result){
            if (result) {
              $scope.reviews = result;
            } else {
              $scope.reviews = null;
            }
          });  
        }
      }
      //hide load mask
    });
  };

  $scope.cancelUserProfile = function() {
    loadUserProfile();
    $scope.userProfileForm.mySwitch = false;
  };

  /***************************************************************
  * Save the user profile
  ***************************************************************/
  $scope.saveUserProfile = function() {
    // myCheckValue
    // mask form
    $scope.$emit('$TRIGGERLOAD', 'userLoad');

    //validate form
    $scope.userProfileForm.userTypeCode = $scope.userProfileForm.userRole.code;

    // Business.userservice.saveCurrentUserProfile($scope.userProfileForm, success, failure);
    Business.userservice.saveCurrentUserProfile($scope.userProfileForm).then(
      function(data, status, headers, config){ /* jshint unused:false */
        //SUCCESS:: data = return value
        triggerAlert('Your profile was saved', 'profileSave', '#profileModal .modal-body', 6000, true);
        $timeout(function() {
          $scope.$emit('$TRIGGERUNLOAD', 'userLoad');
          $scope.$emit('$triggerEvent', '$RESETUSER');
          $scope.userProfileForm.mySwitch = false;
        })
      },
      function(value){ //FAILURE:: value = reason why it failed
        triggerError(value);
        $scope.$emit('$TRIGGERUNLOAD', 'userLoad');
      });
  };

  /***************************************************************
  * This function is looked at for auto suggestions for the tag list
  * if a ' ' is the user's entry, it will auto suggest the next 20 tags that
  * are not currently in the list of tags. Otherwise, it will look at the
  * string and do a substring search.
  * params: query -- The input that the user has typed so far
  * params: list -- The list of tags already tagged on the item
  * params: source -- The source of the tags options
  * returns: deferred.promise -- The promise that we will return a resolved tags list
  ***************************************************************/
  $scope.checkTagsList = function(query, list, source) {
    var deferred = $q.defer();
    var subList = null;
    if (query === ' ') {
      subList = _.reject(source, function(item) {
        return !!(_.where(list, {'text': item}).length);
      });
    } else {
      subList = _.filter(source, function(item) {
        return item.toLowerCase().indexOf(query.toLowerCase()) > -1;
      });
    }
    deferred.resolve(subList);
    return deferred.promise;
  };


  /***************************************************************
  * This function reverts the changes in the profile form by just copying back
  * the details in the backup. This function most likely won't change when
  * we get a database to work with.
  ***************************************************************/
  $scope.revertProfileChanges = function() {
  };

  $scope.deleteReview = function(reviewId, componentId) {
    // console.log('reviewId', reviewId);
    Business.componentservice.deleteReview(componentId, reviewId).then(function(result) {
      $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', componentId);
      $scope.$emit('$TRIGGEREVENT', '$newReview');
    }, function(result) {
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
      if ($scope.watches && $scope.watches.length > 0) {
        var watch = _.find($scope.watches, {componentId: id});
        if ($scope.user.info && watch.watchId) {
          Business.userservice.removeWatch($scope.user.info.username, watch.watchId).then(null, function(result){
            $scope.$emit('$TRIGGEREVENT', '$updatedWatches');
            $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', id);
          });
        }
      }
    }
  };


  $scope.updateWatch = function(watch){
    var watchId = watch.watchId;
    delete watch.componentName;
    delete watch.lastUpdateDts;
    delete watch.watchId;
    delete watch.createDts;
    delete watch.$$hashKey;
    // console.log('watch', watch);
    if ($scope.user.info) {
      Business.userservice.saveWatch($scope.user.info.username, watch, watchId).then(function(result){
        if (result) {
          $scope.$emit('$TRIGGEREVENT', '$updatedWatches');
          $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', watch.componentId);
        }
      });
    }
  }

  $scope.$on('$includeContentLoaded', function(){
    $timeout(function() {
      $('[data-toggle=\'tooltip\']').tooltip();
    }, 300);
  });

  $scope.resetWatches = function(hard) {
    if ($scope.user.info) {
      Business.userservice.getWatches($scope.user.info.username, hard).then(function(result){
        if (result) {
          $scope.watches = result;
          $scope.watches = _.sortBy($scope.watches, function(item) {
            return item.componentName;
          });
        } else {
          $scope.watches = null;
        }
      });
    } else {
      Business.userservice.getCurrentUserProfile().then(function(result){
        if (result) {
          Business.userservice.getWatches(result.username, hard).then(function(result){
            if (result) {
              if (!$scope.user) {
                $scope.user = {};
              }
              if (!$scope.user.info) {
                $scope.user.info = result;
              }
              $scope.watches = result;
              $scope.watches = _.sortBy($scope.watches, function(item) {
                return item.componentName;
              });
            } else {
              $scope.watches = null;
            }
          }); 
        } else {
          $scope.watches = null;
        }
      })
    }
  };
  $scope.resetWatches(false);
  $scope.$on('$updatedWatches', function(event){/*jshint unused:false*/
    $scope.resetWatches(true);
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

