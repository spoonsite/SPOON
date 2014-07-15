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

  $scope._scopename = 'nav';
  $scope.user = {};
  $scope.beforeLogin = null;


  $scope.$on('$beforeLogin', function(event, path, search){
    $scope.beforeLogin = {};
    $scope.beforeLogin.path = path;
    $scope.beforeLogin.search = search;
    $location.path('/login');
  });

  $scope.$on('$includeContentLoaded', function(event){
    $scope.user.isLoggedIn = Auth.signedIn();
  });

  $scope.$on('$login', function(event, user){
    console.log('Auth.signedIn()', Auth.signedIn());
    console.log('user', user);
    console.log('beforeLogin', $scope.beforeLogin);
    $scope.user.info = user;
    $scope.user.isLoggedIn = Auth.signedIn();
    if ($scope.beforeLogin && $scope.beforeLogin.path !== '/login') {
      var temp = $scope.beforeLogin.path;
      $scope.beforeLogin = null;
      console.log('About to send you to ', temp);
      
      $location.path(temp);
    } else {
      $scope.beforeLogin = null;
      $location.path('/');
    }
  });
  
  $scope.navLocation = 'views/nav/nav.html';

  // Here we grab the rootScope searchKey in order to preserve the last search
  $scope.searchKey = $rootScope.searchKey;
  
  $scope.typeahead  = null;


  /***************************************************************
  * Set up typeahead, and then watch for selection made
  ***************************************************************/
  if ($rootScope.typeahead) {
    $scope.typeahead  = $rootScope.typeahead;
  } else {
    $scope.typeahead  = Business.typeahead(Business.getData, 'name');
  }


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
  

  /***************************************************************
  * This function sends the routing to the results page with a specified
  * search key saved in the localCache
  ***************************************************************/
  $scope.goToSearch = function(){ /*jshint unused:false*/

    $rootScope.searchKey = $scope.searchKey;
    $location.search({
      'type': 'search',
      'code': $scope.searchKey
    });
    Business.search('search', $scope.searchKey, true).then(function (key) {
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
    Business.search('search', $scope.searchKey);
    $location.path('/');
  };

  /***************************************************************
  * This function sends the navigation to a specified route.
  ***************************************************************/
  $scope.send = function(route) {
    $location.path(route);
  };

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

}]);
