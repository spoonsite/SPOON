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

app.controller('AdminEditcodesCtrl', ['$scope', '$uiModalInstance', 'type', 'size', 'business', function ($scope, $uiModalInstance, type, size, Business) {
  $scope.type = type;
  $scope.size = size;
  $scope.predicate = 'activeStatus';
  $scope.reverse = false;
  $scope.data = {};
  $scope.data.windowSize = 20;
  $scope.data.pageNumber = 1;
  $scope.current = 0;

  var setupPaging = function() {
    if ($scope.data.pageNumber > $scope.getMaxPage()) {
      $scope.data.pageNumber = $scope.getMaxPage();
    } else if ($scope.data.pageNumber <= 0) {
      $scope.data.pageNumber = 1;
    } else if (isNaN($scope.data.pageNumber)) {
      $scope.data.pageNumber = 1;
    }
    if ($scope.data.windowSize > 100) {
      $scope.data.windowSize = 100;
    } else if ($scope.data.windowSize <= 0) {
      $scope.data.windowSize = 1;
    } else if (isNaN($scope.data.windowSize)) {
      $scope.data.windowSize = 1;
    }
    $scope.current = $scope.data.windowSize * ($scope.data.pageNumber - 1);
  }

  $scope.pageChange = function() {
    if ($scope.data.pageNumber) {
      setupPaging();
    }
  }

  $scope.windowChange = function(){
    if ($scope.data.windowSize) {
      $scope.data.pageNumber = Math.ceil($scope.current / $scope.data.windowSize);
      if (!$scope.data.pageNumber) {
        $scope.data.pageNumber = 1;
      }
      setupPaging();
    }
  }

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  }


  $scope.next = function () {
    if (($scope.current + $scope.data.windowSize) <= $scope.type.codes.length) {
      $scope.pageNumber++;
      setupPaging();
    }
  }

  $scope.previous = function () {
    if (($scope.current - $scope.data.windowSize) >= 0) {
      $scope.pageNumber--;
      setupPaging();
    } 
  }

  $scope.getMaxPage = function(){
    return Math.ceil($scope.type.codes.length / $scope.data.windowSize);
  }


  $scope.ok = function () {
    $uiModalInstance.close('success');
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);



app.filter('pageme', function () {
  // first we make sure that the pageMeCache is empty
  var pageMeCache = {};
  var filter = function(arr, current, distance) {
    // if we don't have an array to search return...
    if (!arr) { return; }
    
    // otherwise find our sub array
    var newArr = angular.copy(arr.slice(current, (current + distance)));

    // then stringify the array
    var arrString = JSON.stringify(arr);

    // set up the pageMeCache
    var frompageMeCache = pageMeCache[arrString+current];

    // and from the pageMeCache, return the results
    console.log('frompageMeCache', frompageMeCache);
    console.log('newArr', newArr);
    
    console.log('JSON.stringify cache', JSON.stringify(frompageMeCache));
    
    if (JSON.stringify(frompageMeCache) === JSON.stringify(newArr)) {
      return frompageMeCache;
    }
    pageMeCache[arrString+current] = newArr;
    return newArr;
  };
  return filter;
});
