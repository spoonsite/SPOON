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

/*global setupMain*/

app.controller('MainCtrl', ['$scope', 'business', 'localCache', '$location', '$rootScope', '$timeout', function ($scope, Business, localCache, $location, $rootScope, $timeout) {/*jshint unused: false*/
  //////////////////////////////////////////////////////////////////////////////
  // Variables
  //////////////////////////////////////////////////////////////////////////////
  $scope._scopename = 'main';
  $scope.pageTitle  = 'DI2E';
  $scope.subTitle   = 'Storefront';
  $scope.typeahead  = null;
  $scope.goToLand   = false;
  $scope.svcv4Mode  = false;
  $scope.diagramToggleAllState = true;
  $scope.diagramToggleAllText = "Expand All";
  $scope.svcv4data = MOCKDATA2.parsedSvcv4;
  $scope.searchKey  = $rootScope.searchKey;
  $scope.filters    = Business.getFilters();
  $scope.filters    = _.filter($scope.filters, function(item) {
    return item.showOnFront;
  });

  /***************************************************************
  * Set up typeahead, and then watch for selection made
  ***************************************************************/
  Business.componentservice.getComponentDetails().then(function(result) {
    Business.typeahead(result, 'name').then(function(value){
      $scope.typeahead = value;
    });
  });
  //////////////////////////////////////////////////////////////////////////////
  // Event Watchers
  //////////////////////////////////////////////////////////////////////////////

  /***************************************************************
  * Catch the enter/select event here
  ***************************************************************/
  $scope.$on('$typeahead.select', function(event, value, index) {
    if (value !== undefined) {
      $scope.goToSearch('search', $scope.searchKey);
      $scope.$apply();
    } else {
      $scope.goToSearch('search', 'All');
      $scope.$apply();
    }
  });
  
  //////////////////////////////////////////////////////////////////////////////
  // Functions
  //////////////////////////////////////////////////////////////////////////////

  /*******************************************************************************
  * This and the following functions send the user to the search filling the 
  * data object with the search key 
  * params: type -- This is the code of the type that was clicked on
  *******************************************************************************/
  $scope.goToSearch = function(searchType, searchKey, override){ /*jshint unused:false*/
    $(window).scrollTop(0);
    var search = null;
    if (searchType === 'search' && !override) {
      if (!$scope.searchKey) {
        if (searchKey) {
          search = searchKey;
        } else {
          search = 'All';
        }
      } else {
        search = $scope.searchKey;
      }
      Business.componentservice.search(searchType, search);
      $location.search('type', searchType);
      $location.path('/results');
      if (search === '') {
        $location.search('code', 'All');
      } else {
        $location.search('code', search);
      }

    } else {
      Business.componentservice.search(searchType, searchKey);
      $location.path('/results');
      $location.search('type', searchType);
      $location.search('code', searchKey);
    }
    return;
  };

$scope.diagramToggleAll = function(diagramData){
   _.each(diagramData.children, function(level1){ 
     level1.show = $scope.diagramToggleAllState;      
     _.each(level1.children, function(level1child){ 
       level1child.show= $scope.diagramToggleAllState; 
     }); 
   }); 
   $scope.diagramToggleAllState = !$scope.diagramToggleAllState;
   $scope.diagramToggleAllState ?  $scope.diagramToggleAllText = "Expand All" :  $scope.diagramToggleAllText = "Collapse All";

};

  /*******************************************************************************
  * This and the following functions send the user to the search filling the 
  * data object with the search key 
  * params: type -- This is the code of the type that was clicked on
  *******************************************************************************/
  $scope.goToLanding = function(route, searchType, searchKey){ /*jshint unused:false*/
    $(window).scrollTop(0);
    localCache.save('landingRoute', route);
    localCache.save('landingType', searchType);
    localCache.save('landingCode', searchKey);
    // Business.landingPage('landing', route, true).then(function() {
      $location.search('route', route);
      $location.path('/landing');
    // });
    return false;
  };

  /***************************************************************
  * This function us initiated by the 'GetStarted button'
  ***************************************************************/
  $scope.focusOnSearch = function() {
    $('#mainSearchBar').focus();
  };


  //////////////////////////////////////////////////////////////////////////////
  // Scope Watchers
  //////////////////////////////////////////////////////////////////////////////

  /*******************************************************************************
  * This function sets the rootScope's search key so that if you did it in the
  * controller search, it is still preserved across the page.
  * params: param name -- param description
  * returns: Return name -- return description
  *******************************************************************************/
  $rootScope.$watch('searchKey', function() {
    $scope.searchKey = $rootScope.searchKey;
  });
  $scope.$watch('searchKey', function() {
    $rootScope.searchKey = $scope.searchKey;
  });



  // this calls the setup for the page-specific js
  setupMain();

  // Extras for testing....
  // var errorObject = {
  //   'success': false,
  //   'errors': [
  //     //
  //     {
  //       'mainSearchBar' : 'Your input was invalid. Please try again.'
  //     }
  //   //
  //   ]
  // };
  // triggerError(errorObject);
  // triggerAlert('Check out this alert!', '1', 'body');

}]);