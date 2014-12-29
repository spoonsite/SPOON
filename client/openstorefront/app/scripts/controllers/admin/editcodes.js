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

var pagesMemoizeCache;

app.controller('AdminEditcodesCtrl', ['$scope', '$uiModalInstance', '$uiModal', 'type', 'size', 'business', '$timeout', function ($scope, $uiModalInstance, $uiModal, type, size, Business, $timeout) {
  $scope.type = type;
  $scope.size = size;
  $scope.predicate = 'activeStatus';
  $scope.check = {};
  $scope.reverse = false;
  $scope.windowSize = 15;
  $scope.pageNumber = 1;
  $scope.check.windowSize = $scope.windowSize;
  $scope.check.pageNumber = $scope.pageNumber;
  $scope.current = 0;

  var timer;

  var setupPaging = function() {
    clearTimeout(timer);
    $scope.current = $scope.windowSize * ($scope.pageNumber - 1);
    if (pagesMemoizeCache) {
      pagesMemoizeCache.cache = {};
    }
  }

  $scope.$watch('windowSize', function(newVal, oldVal){
    // console.log('Window Changed');
    // console.log('Window newVal', newVal);
    // console.log('Window oldVal', oldVal);
    if ($scope.windowSize > 50) {
      $scope.windowSize = 50;
      $scope.check.windowSize = 50;
    } else if ($scope.windowSize <= 0) {
      $scope.windowSize = 5;
      $scope.check.windowSize = 5;
    } else if (isNaN($scope.windowSize)) {
      $scope.windowSize = 5;
      $scope.check.windowSize = 5;
    } else {
      $scope.check.windowSize = newVal;
    }

    if ($scope.pageNumber > $scope.getMaxPage()) {
      $scope.pageNumber = $scope.getMaxPage();
    } else if ($scope.pageNumber <= 0) {
      $scope.pageNumber = 1;
      $scope.check.pageNumber = 1;
    } else if (isNaN($scope.pageNumber)) {
      $scope.pageNumber = 1;
      $scope.check.pageNumber = 1;
    }

    setupPaging();
    
  })
  $scope.$watch('pageNumber', function(newVal, oldVal){
    // console.log('page number Changed');
    // console.log('page newVal', newVal);
    // console.log('page oldVal', oldVal);
    if ($scope.pageNumber > $scope.getMaxPage()) {
      $scope.pageNumber = $scope.getMaxPage();
    } else if ($scope.pageNumber <= 0) {
      $scope.pageNumber = 1;
      $scope.check.pageNumber = 1;
    } else if (isNaN($scope.pageNumber)) {
      $scope.pageNumber = 1;
      $scope.check.pageNumber = 1;
    } else {
      $scope.check.pageNumber = newVal;
    }
    setupPaging();
    
  })

  $scope.applyWindow = function() {
    clearTimeout(timer)
    timer = setTimeout(function(){
      // console.log('scope.check.windowSize', $scope.check.windowSize);
      $scope.windowSize = $scope.check.windowSize;
      $scope.$apply();
    }, 500);
  }
  $scope.applyPage = function() {
    clearTimeout(timer)
    timer = setTimeout(function(){
      // console.log('scope.check.pageNumber', $scope.check.pageNumber);
      $scope.pageNumber = $scope.check.pageNumber;
      $scope.$apply();
    }, 500);
  }

  $scope.setPredicate = function(predicate, override){
    if (pagesMemoizeCache) {
      pagesMemoizeCache.cache = {};
    }
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  }

  $scope.getUniqueId = function() {
    return '' + $scope.type.type + $scope.current + $scope.windowSize + $scope.predicate + $scope.reverse;
  }

  $scope.next = function () {
    if (($scope.current + $scope.windowSize) <= $scope.type.codes.length) {
      $scope.pageNumber++;
    }
  }

  $scope.previous = function () {
    if (($scope.current - $scope.windowSize) >= 0) {
      $scope.pageNumber--;
    } 
  }

  $scope.getMaxPage = function(){
    return Math.ceil($scope.type.codes.length / $scope.windowSize);
  }


  $scope.ok = function () {
    $uiModalInstance.close('success');
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };




  $scope.changeActivity = function(code){
    var cont = confirm("You are about to change the active status of an Attribute (Enabled or disabled). Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      if (code.activeStatus === 'A') {
        $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
        Business.articleservice.deactivateCode(code.type).then(function(){
          $timeout(function(){
            $scope.getFilters(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getFilters(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
          }, 1000);
        })
      } else {
        $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
        Business.articleservice.activateFilter(code.type).then(function() {
          $timeout(function(){
            $scope.getFilters(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getFilters(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
          }, 1000);
        })
      }
    }
  }


  $scope.editCode = function(code){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/editcode.html',
      controller: 'AdminEditCodeCtrl',
      size: 'sm',
      resolve: {
        code: function () {
          return code;
        },
        size: function() {
          return 'sm';
        }
      }
    });

    modalInstance.result.then(function (result) {
      //do something
      triggerAlert('Your edits were saved', 'editUserProfile', 'body', 6000);
    }, function () {
      // cancled or failed
    });
  }








}]);

app.controller('AdminEditCodeCtrl', ['$scope', '$uiModalInstance', 'code', 'size', 'business', '$timeout', function ($scope, $uiModalInstance, code, size, Business, $timeout) {
  $scope.code = code;

  
  $scope.ok = function () {
    $uiModalInstance.close('success');
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);


app.filter('pageme', ['$timeout', function ($timeout) {
  pagesMemoizeCache = _.memoize(function(arr, current, distance, id) {
    // if we don't have an array to search return...
    if (!arr) { return; }
    
    // otherwise find our sub array
    var newArr = angular.copy(arr.slice(current, (current + distance)));
    return newArr;
  }, function(arr, current, distance, id){
    return id;
  });
  return pagesMemoizeCache;
}]);
