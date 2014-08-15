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

/*global setUpDropdown*/

app.controller('NavCtrl', ['$scope', '$location', '$rootScope', 'business', '$route', '$timeout', 'auth', function ($scope, $location, $rootScope, Business, $route, $timeout, Auth) { /*jshint unused: false*/

  /*******************************************************************************
  * This Controller gives us a place to add functionality to the navbar
  *******************************************************************************/
  //////////////////////////////////////////////////////////////////////////////
  // Variables
  //////////////////////////////////////////////////////////////////////////////
  $scope._scopename   = 'nav';
  $scope.navLocation  = 'views/nav/nav.html';
  $scope.searchKey    = $rootScope.searchKey;
  $scope.user         = {};
  $scope.beforeLogin  = null;
  $scope.typeahead    = null;

  /***************************************************************
  * Set up typeahead, and then watch for selection made
  ***************************************************************/
  Business.componentservice.getComponentDetails().then(function(result) {
    Business.typeahead(result, null).then(function(value){
      $scope.typeahead = value;
    });
  });


  //////////////////////////////////////////////////////////////////////////////
  // Event Watches
  //////////////////////////////////////////////////////////////////////////////
  $scope.$on('$beforeLogin', function(event, path, search){
    $scope.beforeLogin = {};
    $scope.beforeLogin.path = path;
    $scope.beforeLogin.search = search;
    Auth.login($scope.user).then(function () {
    }, function (error) {
      $scope.error = error.toString();
    });

    // $location.path('/login');
  });

  /***************************************************************
  * This function watches for the content loaded event and then checks
  * to see if they're logged in.
  ***************************************************************/
  $scope.$on('$includeContentLoaded', function(event){
    $scope.user.isLoggedIn = Auth.signedIn();
  });

  /***************************************************************
  * This function watches for the login event in order to adjust
  * the navigation and possibly redirect the page back to where they were.
  ***************************************************************/
  $scope.$on('$login', function(event, user){
    $scope.user.info = user;
    $scope.user.isLoggedIn = Auth.signedIn();
    if ($scope.beforeLogin && $scope.beforeLogin.path !== '/login') {
      var temp = $scope.beforeLogin.path;
      var temp2 = $scope.beforeLogin.search;
      $scope.beforeLogin = null;
      $location.search(temp2);
      $location.path(temp);
    } else {
      $scope.beforeLogin = null;
      // $location.search({});
      // $location.path('/');
    }
  });


  /***************************************************************
  * Catch the enter/select event here
  ***************************************************************/
  $scope.$on('$typeahead.select', function(event, value, index) {
    $scope.searchKey = value;
    if (value !== undefined) {
      $scope.goToSearch();
      $scope.$apply();
    } else {
      $scope.goToSearch();
      $scope.$apply();
    }
  });
  
  /***************************************************************
  * Catch the navigation location change event here
  ***************************************************************/
  $scope.$on('$changenav', function(event, value, index) {
    $scope.navLocation = value;
  });
  
  //////////////////////////////////////////////////////////////////////////////
  // Functions
  //////////////////////////////////////////////////////////////////////////////

  /***************************************************************
  * This function sends the routing to the results page with a specified
  * search key saved in the localCache
  ***************************************************************/
  $scope.goToSearch = function(){ /*jshint unused:false*/
    $scope.closeNavbarItem('searchNavButton');
    var key = 'All';
    if ($scope.searchKey) {
      key = $scope.searchKey;
      $rootScope.searchKey = $scope.searchKey;
    }
    $location.search({
      'type': 'search',
      'code': key
    });
    Business.componentservice.search('search', key, true).then(function (key) {
      if($location.path() === '/results') {
        $rootScope.$broadcast('$callSearch');
      } else {
        $location.path('/results');
      }
    });
  };

  /***************************************************************
  * This function sends the routing to the results page with a specified
  * search key saved in the localCache
  ***************************************************************/
  $scope.goToLogin = function(){ /*jshint unused:false*/

    $location.search({});
    $location.path('/login');
  };

  /***************************************************************
  * This function sends the person home
  ***************************************************************/
  $scope.sendHome = function(){ /*jshint unused:false*/
    Business.componentservice.search('search', $scope.searchKey);
    $location.path('/');
  };

  /***************************************************************
  * This function sends the navigation to a specified route.
  ***************************************************************/
  $scope.send = function(route) {
    $location.path(route);
  };

  /***************************************************************
  * Log out the user
  ***************************************************************/
  $scope.logout = function () {
    Auth.logout();
    $scope.user.isLoggedIn = false;
    $route.reload();
  };

  //////////////////////////////////////////////////////////////////////////////
  // Scope Watches
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


  // We have to manually connect the list item to the dropdown toggle because the
  // routing and nav load somehow delays it which makes the dropdown not work
  // until the second click. This makes it work on first click.
  $scope.$watch('navLocation', function() {
    $timeout(function() {
      setUpDropdown('dropTheMenu');
    }, 500);
  });


  /***************************************************************
  * Automatically login the user for the demo... DELETE THIS LATER
  ***************************************************************/

  $scope.$emit('$TRIGGEREVENT','$beforeLogin', $location.path(), $location.search());

}]);
