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

app.controller('AdminEditcodesCtrl', ['$scope', '$uiModalInstance', '$uiModal', 'type', 'size', 'business', '$timeout', '$filter', function ($scope, $uiModalInstance, $uiModal, type, size, Business, $timeout, $filter) {
  $scope.type = angular.copy(type);
  $scope.backup = angular.copy(type);
  $scope.size = size;
  $scope.predicate = 'sortOrder';
  $scope.check = {};
  $scope.reverse = false;
  $scope.windowSize = 15;
  $scope.pageNumber = 1;
  $scope.check.windowSize = $scope.windowSize;
  $scope.check.pageNumber = $scope.pageNumber;
  $scope.current = 0;
  $scope.dirty = false;
  $scope.cache = new Date().getTime();

  $scope.changed = false;

  var timer;


  $scope.refreshFilterCache = function() {
    Business.getFilters(true, false);
  };


  var setupPaging = function() {
    clearTimeout(timer);
    $scope.current = $scope.windowSize * ($scope.pageNumber - 1);
    if (pagesMemoizeCache) {
      pagesMemoizeCache.cache = {};
    }
  }

  $scope.clearCache = function() {
    if (pagesMemoizeCache) {
      pagesMemoizeCache.cache = {};
    } 
    $scope.cache = new Date().getTime();
  }

  $scope.getType = function() {
    Business.articleservice.getType(type.type, true, true).then(function(result){
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
      if (result) {
        console.log('----------------WE ARE GETTING THE NEW TYPE ==--------------------', result);
        $scope.type = result;
        $scope.clearCache();
      } else {
        $scope.type = {}
      }
    }, function() {
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
      $scope.type = {};
    })
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
    return '' + $scope.type.type + $scope.current + $scope.windowSize + $scope.predicate + $scope.reverse + $scope.cache;
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
    $uiModalInstance.close($scope.changed);
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss($scope.changed);
  };

  $scope.applySortOrder = function(remove){
    var temp = $filter('orderBy')($scope.type.codes, $scope.predicate, $scope.reverse);
    console.log('temp', temp[0]);
    var sortOrder = temp.length;
    for (var i = temp.length - 1; i >= 0; i--) {
      if (!remove){
        temp[i].sortOrder = sortOrder--;
      } else {
        delete temp[i].sortOrder;
      }
    };
    $scope.type.codes = angular.copy(temp);
    var oldPredicate = $scope.predicate;
    $scope.dirty = true;
    $scope.clearCache();
  }


  $scope.saveType = function(){
    $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
    var type = angular.copy($scope.type);
    type.attributeType = type.type;
    console.log('Type save', type);
    delete type.type;
    delete type.codes;
    Business.articleservice.saveType(type).then(function(result){
      if (result) {
        $scope.changed = true;
        $timeout(function(){
          $scope.getType();
          $scope.dirty = false;
          triggerAlert('Your edits were saved', 'editUserProfile', '#editTypeModalDiv', 6000);
        }, 500);
      }
      $scope.refreshFilterCache()
    }, function(){
      $scope.refreshFilterCache()
    })
  }

  $scope.saveSortOrder = function(){
    $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
    Business.articleservice.saveSortOrder($scope.type).then(function(result){
      if (result) {
        $timeout(function(){
          $scope.getType();
          $scope.dirty = false;
          triggerAlert('Your edits were saved', 'editUserProfile', '#editTypeModalDiv', 6000);
        }, 500);
      }      
    }, function(){
    })
  }

  $scope.cancelChanges = function(){
    $scope.dirty = false;
    $scope.clearCache();
    $scope.type = angular.copy($scope.backup);
  }

  $scope.changeActivity = function(code){
    var cont = confirm("You are about to change the active status of an Attribute (Enabled or disabled). Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      if (code.activeStatus === 'A') {
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
        Business.articleservice.deactivateCode($scope.type.type, code.code).then(function(){
          $timeout(function(){
            $scope.getType();
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            $scope.refreshFilterCache();
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getType();
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            $scope.refreshFilterCache();
          }, 1000);
        })
      } else {
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
        Business.articleservice.activateCode($scope.type.type, code.code).then(function() {
          $timeout(function(){
            $scope.getType();
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            $scope.refreshFilterCache();
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getType();
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            $scope.refreshFilterCache();
          }, 1000);
        })
      }
    }
  }

  $scope.getCodeDesc = function(desc){
    if (desc && desc !== undefined  && desc !== null && desc !== '') {
      return desc;
    }
    return ' ';
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
        type: function () {
          return type.type;
        },
        size: function() {
          return 'sm';
        }
      }
    });

    modalInstance.result.then(function (result) {
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
      $timeout(function(){
        $scope.getType();
        triggerAlert('Your edits were saved', 'editUserProfile', '#editTypeModalDiv', 6000);
      }, 500);
      $scope.refreshFilterCache()
    }, function () {
      console.log('The edit was canceled or failed');
      // cancled or failed
      $scope.refreshFilterCache()
    });


  }


  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);
}]);







app.controller('AdminEditCodeCtrl', ['$scope', '$uiModalInstance', 'code', 'type', 'size', 'business', '$timeout', function ($scope, $uiModalInstance, code, type, size, Business, $timeout) {
  $scope.code = angular.copy(code);
  console.log('===CODE===', code);
  
  $scope.editorContent = angular.copy($scope.code.description);



  $scope.ok = function () {
    //save the code change.
    $scope.code.detailUrl = $scope.code.fullTextLink;
    console.log('$scope.code', $scope.code);
    
    Business.articleservice.saveCode(type, code.code, $scope.code).then(function(result){
      if (result) {
        console.log('Code save result', result);
        console.log('$scope.code', $scope.code);
        $uiModalInstance.close('success');
      } else {
        console.log('The code failed but the call succeded');
        
      }
    }, function(){
      // the save failed
      console.log('The code save failed');
      
    })
  };

  $scope.cancel = function () {
    console.log('$scope.code', $scope.code);
    $uiModalInstance.dismiss('cancel');
  };
}]);


app.filter('pageme', ['$timeout', function ($timeout) {
  var pagesMemoizeCache = _.memoize(function(arr, current, distance, id) {
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
