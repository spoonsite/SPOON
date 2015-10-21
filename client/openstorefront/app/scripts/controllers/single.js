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

/*global isEmpty*/

app.controller('SingleCtrl', ['$scope', 'localCache', 'business', '$filter', '$timeout', '$location', '$rootScope', '$q', '$route',  function ($scope,  localCache, Business, $filter, $timeout, $location, $rootScope, $q, $route) { /*jshint unused: false*/

  //////////////////////////////////////////////////////////////////////////////
  // Variables
  //////////////////////////////////////////////////////////////////////////////
  $scope.data              = {};
  $scope.details           = {};
  $scope.modal             = {};
  $scope.single            = true;
  $scope.details.details   = null;
  $scope.modal.isLanding   = false;
  $scope.showDetails       = false;
  $scope.showBreadCrumbs    = false;

  $scope.eventHistory = $rootScope.eventHistory;
  $scope.goToBreadcrumb = $rootScope.goToBreadcrumb;

  var label = $route.routes[$location.$$path].label || $location.$$path;
  if ($location.$$path === '/single'){
    label = 'Component ' + $location.search().id;
  }
  // $rootScope.eventHistory.push({path: $location.$$path, search: $location.search(), label: label});

  $rootScope.$watch('eventHistory', function(){
    $scope.eventHistory = $rootScope.eventHistory;
  })

  Business.componentservice.getComponentDetails().then(function(result) {
    if (result) {
      $scope.data.data = result;
    } else {
      $scope.data.data = null;
    }
  }, function(){
    $scope.data.data = null;
  });
  Business.getProsConsList().then(function(result){
    if (result) {
      $scope.prosConsList = result;
    } else {
      $scope.prosConsList = null;
    }
  }, function(){
    $scope.prosConsList = null;
  });
  Business.userservice.getWatches().then(function(result){
    if (result) {
      $scope.watches = result;
    } else {
      $scope.watches = null;
    }
  }, function(){
    $scope.watches = null;
  });



  /***************************************************************
  * Event to trigger an update of the details that are shown
  ***************************************************************/
  $scope.$on('$detailsUpdated', function(event, id) {/*jshint unused: false*/
    if ($scope.details.details && $scope.details.details.componentId === id) {
      $timeout(function() {
        $scope.updateDetails($scope.details.details.componentId);
      });
    }
  });

  //////////////////////////////////////////////////////////////////////////////
  // Functions
  //////////////////////////////////////////////////////////////////////////////
  /***************************************************************
  * This function is called once we have the search request from the business layer
  ***************************************************************/
  $scope.updateDetails = function(id){
    $scope.$emit('$TRIGGERLOAD', 'fullDetailsLoader');
    $scope.showDetails = false;
    Business.componentservice.getComponentDetails(id, true).then(function(result){
      if (result)
      {
        // console.log('result', result);
        _.each($rootScope.eventHistory, function(item){
          if (item.path === $location.$$path && item.search === $location.search()){
            item.label = result.name;
          }
        })
        // grab evaluation schedule.

        $scope.sendPageView(result.name);
        $scope.details.details = result;
        var found = _.find($scope.watches, {'componentId': $scope.details.details.componentId});

        // Code here will be linted with JSHint.
        /* jshint ignore:start */
        // Code here will be linted with ignored by JSHint.

        if ($scope.details.details.attributes[0] !== undefined) {
          var foundEvaluation = null;
          _.each($scope.details.details.attributes, function(attribute) {
            if (attribute.type === 'DI2ELEVEL') {
              foundEvaluation = attribute;
            }
          });
          $scope.details.details.evaluationAttribute = foundEvaluation;
        }

        /* jshint ignore:end */
        if ($scope.details.details.lastActivityDts && $scope.details.details.lastViewedDts)
        {
          var update = new Date($scope.details.details.lastActivityDts);
          var view = new Date($scope.details.details.lastViewedDts);
          if (view < update) {
            showUpdateNotify();
          }else {
            resetUpdateNotify();
          }
        } else {
          resetUpdateNotify();
        }

        if (found) {
          $scope.details.details.watched = true;
        }
      }
      $scope.$emit('$TRIGGERUNLOAD', 'fullDetailsLoader');
      $scope.showDetails = true;
    });
  }; //

  /***************************************************************
  * This function grabs the search key and resets the page in order to update the search
  ***************************************************************/
  var callSearch = function() {
    Business.componentservice.search(false, false, true).then(
    //This is the success function on returning a value from the business layer 
    function(id) {
      var query = null;

      if (id === null) {
        if (!isEmpty($location.search()))
        {
          query = $location.search();
          if (query.id) {
            id = query.id;
          }
        }
        $scope.updateDetails(id);
      } else {
        if (!isEmpty($location.search()))
        {
          query = $location.search();
          if (query.id) {
            id = query.id;
          } else {
            id = null;
          }
        }
        $scope.updateDetails(id);
      }
    },
    // This is the failure function that handles a returned error
    function(error) {
      console.warn('WARN: ', error);
      var id = null;
      if (!isEmpty($location.search()))
      {
        var query = $location.search();
        if (query.id) {
          id = query.id;
        }
      }
      $scope.updateDetails(id);
    });
  };

  callSearch();

}]);
