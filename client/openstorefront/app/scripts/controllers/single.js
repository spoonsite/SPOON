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
  $scope.data              = Business.getData();
  $scope.data.data         = $scope.data;
  $scope.prosConsList      = Business.getProsConsList();
  $scope.details           = {};
  $scope.modal             = {};
  $scope.single            = true;
  $scope.details.details   = null;
  $scope.modal.isLanding   = false;


  //////////////////////////////////////////////////////////////////////////////
  // Functions
  //////////////////////////////////////////////////////////////////////////////
  /***************************************************************
  * This function is called once we have the search request from the business layer
  ***************************************************************/
  $scope.updateDetails = function(id){
    $scope.$emit('$TRIGGERLOAD', 'fullDetailsLoader');
    $timeout(function() {
      var temp =  _.where($scope.data.data, {'id': parseInt(id)})[0];
      if (temp)
      {
        $scope.details.details = temp;
      }
      $scope.$emit('$TRIGGERUNLOAD', 'fullDetailsLoader');
    }, 1500);
  };

  /***************************************************************
  * This function grabs the search key and resets the page in order to update the search
  ***************************************************************/
  var callSearch = function() {
    Business.search(false, false, true).then(
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
      console.error('ERROR: ', error);
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
    //
  };

  
  callSearch();
}]);
