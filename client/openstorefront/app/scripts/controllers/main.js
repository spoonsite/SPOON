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
  $scope.subTitle   = 'Clearinghouse';
  $scope.typeahead  = null;
  $scope.goToLand   = false;
  $scope.searchKey  = $rootScope.searchKey;
  $scope.openAdminMessage = $rootScope.openAdminMessage;
  $scope.appverison = '';

  Business.systemservice.getAppVersion().then(function(result){
    $scope.appverison = result;
  });  

  Business.componentservice.getCount().then(function(result){
    $scope.componentCount = result? result: 0;
  });

  // Business.getFilters().then(function(result){
  //   if (result) {
  //     $scope.filters = result;
  //     $scope.filters    = _.filter($scope.filters, function(item) {
  //       return item.showOnFront;
  //     });
  //   } else {
  //     $scope.filters = null;
  //   }
  // });

Business.highlightservice.getHighlights().then(function(result){
  if (result) {
    $scope.highlights = result;
  } else {
    $scope.highlights = null;
  }
});

Business.highlightservice.getRecentlyAdded().then(function(result){
  if (result) {
    var recents = [];
    _.each(result, function(item){
      var temp = item;
      recents.push(temp);
    })
    $scope.recentlyAdded = recents;
    $timeout(function(){
      $('.shortDescription').dotdotdot({
        /*  The text to add as ellipsis. */
        ellipsis  : '... ',

        /*  How to cut off the text/html: 'word'/'letter'/'children' */
        wrap    : 'word',

        /*  Wrap-option fallback to 'letter' for long words */
        fallbackToLetter: true,

        /*  jQuery-selector for the element to keep and put after the ellipsis. */
        after   : null,

        /*  Whether to update the ellipsis: true/'window' */
        watch   : true,

        /*  Optionally set a max-height, if null, the height will be measured. */
        height    : 55,

        /*  Deviation for the height-option. */
        tolerance : 0,

          /*  Callback function that is fired after the ellipsis is added,
          receives two parameters: isTruncated(boolean), orgContent(string). */
          callback  : function( isTruncated, orgContent ) {},

          lastCharacter : {

            /*  Remove these characters from the end of the truncated text. */
            remove    : [ ' ', ',', ';', '.', '!', '?' ],

            /*  Don't add an ellipsis if this array contains 
            the last character of the truncated text. */
            noEllipsis  : []
          }
        })
      })//
    } else { //
      $scope.recentlyAdded = null;
    }
  });

$scope.getTypeahead = function(){
  Business.typeahead($scope.searchKey).then(function(result){
    result = result || [];
    _.forEach(result, function(item){
      item.description = '"' + item.description + '"';
    });
    
    $scope.typeahead = result;
  }, function(){
    $scope.typeahead = [];
  })
}

$scope.$watch('searchKey', function(newValue, oldValue){
  if ($scope.searchKey) {
    $rootScope.searchKey = $scope.searchKey;
    $scope.getTypeahead();
  }
})

  //////////////////////////////////////////////////////////////////////////////
  // Event Watchers
  //////////////////////////////////////////////////////////////////////////////

  /***************************************************************
  * Catch the enter/select event here
  ***************************************************************/
  $scope.$on('$typeahead.select', function(event, value, index) {
    if (typeof value === 'object') {
      value = '"' + value.description + '"';
    }
    $scope.searchKey = value;
    $rootScope.searchKey = value;
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

    } else if (searchType === 'attribute') {
      if (searchKey && searchKey.keyType) {
        var keyKey = searchKey.keyKey? searchKey.keyKey : 'all';
        $location.search({
          'type': 'attribute',
          'keyType': searchKey.keyType,
          'keyKey': keyKey
        });
      }
      $location.path('/results');
    } else {
      if (searchKey && searchKey.keyType) {
        var keyKey = searchKey.keyKey? searchKey.keyKey : 'all';
        $location.search({
          'type': 'attribute',
          'keyType': searchKey.keyType,
          'keyKey': keyKey
        });
      } else {
        $location.search({
          'type': 'search',
          'code': 'all'
        });
      }
      // Business.componentservice.search(searchType, searchKey);
      $location.path('/results');
    }
    return;
  };

  /*******************************************************************************
  * This and the following functions send the user to the search filling the 
  * data object with the search key 
  * params: type -- This is the code of the type that was clicked on
  *******************************************************************************/
  $scope.goToLanding = function(searchType, searchKey){ /*jshint unused:false*/
    $(window).scrollTop(0);
    localCache.save('type', searchType);
    localCache.save('code', searchKey);
    $location.search({
      'type': searchType,
      'code': searchKey
    });
    $location.path('/landing');
    return false; //
  };

  /***************************************************************
  * This function us initiated by the 'GetStarted button'
  ***************************************************************/
  $scope.focusOnSearch = function() {
    $('#mainSearchBar').focus();
  };
  
  $scope.showTopicsViewInitially=false;
  $scope.showTopics = function() {
      console.log("SHOW TOPICS");
      $scope.$broadcast('setVis');
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
