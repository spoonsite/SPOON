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
  $scope.addTypeFlg = false;

  $scope.changed = false;

  var timer;

  if (!$scope.type) {
    $scope.addTypeFlg = true;
    $scope.type = {};
  }

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
    if ($scope.type && $scope.type.type) {
      Business.articleservice.getType($scope.type.type, true, true).then(function(result){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
        if (result) {
          $scope.type = result;
          $scope.clearCache();
        } else {
          $scope.type = null;
        }
      }, function() {
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
        $scope.type = null;
      })
    } else {
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
      $scope.type = null;
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
    if ($scope.type && $scope.type.type) {
      return '' + $scope.type.type + $scope.current + $scope.windowSize + $scope.predicate + $scope.reverse + $scope.cache;
    } else {
      return 'noType' + $scope.cache;
    }
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
    if ($scope.type && $scope.type.codes && $scope.type.codes.length) {
      return Math.ceil($scope.type.codes.length / $scope.windowSize);
    } else {
      return 1;
    }
  }


  $scope.ok = function (type, code) {
    var result = {};
    result.refresh = $scope.changed;
    result.type = type;
    result.code = code;
    $uiModalInstance.close(result);
  };

  $scope.cancel = function (type, code) {
    var result = {};
    result.refresh = $scope.changed;
    result.type = type;
    result.code = code;
    $uiModalInstance.dismiss(result);
  };

  $scope.applySortOrder = function(remove){
    if ($scope.type && $scope.type.codes) {
      var temp = $filter('orderBy')($scope.type.codes, $scope.predicate, $scope.reverse);
      // console.log('temp', temp[0]);
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
  }


  $scope.saveType = function(validity){
    if (validity) {

      console.log('type', $scope.type);
      console.log('type.type', $scope.type.type);
      var cont = true;
      if ($scope.addTypeFlg) {
        cont = confirm("Once this form is saved, the type field will be fixed. Continue?");
      }
      if ($scope.type && $scope.type.type && cont) {
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
        var type = angular.copy($scope.type);
        type.attributeType = type.type;
        type.description = type.description || '';
        type.visibleFlg = type.visibleFlg || false;
        type.requiredFlg = type.requiredFlg || false;
        type.architectureFlg = type.architectureFlg || false;
        type.allowMultipleFlg = type.allowMultipleFlg || false;
        type.importantFlg = type.importantFlg || false;


        // console.log('Type save', type);
        delete type.type;
        delete type.codes;

        Business.articleservice.getType($scope.type.type, false, true).then(function(result){
          console.log('result', result);

          var cont = true;
          if (result && $scope.addTypeFlg) {
            cont = confirm('Warning: You will be overriding a previously saved type by saving this form. Continue?');
          }
          if (cont) {
            Business.articleservice.saveType(type, $scope.addTypeFlg).then(function(result){
              if (result) {
                $scope.addTypeFlg = false;
                $scope.changed = true;
                $timeout(function(){
                  $scope.getType();
                  $scope.dirty = false;
                  triggerAlert('Your edits were saved', 'editUserProfile', '#editTypeModalDiv', 6000);
                }, 500);
              }
            }, function(){
              $scope.getType();
            })
          }
        }, function(){
          Business.articleservice.saveType(type, $scope.addTypeFlg).then(function(result){
            if (result) {
              $scope.addTypeFlg = false;
              $scope.changed = true;
              $timeout(function(){
                $scope.getType();
                $scope.dirty = false;
                triggerAlert('Your edits were saved', 'editUserProfile', '#editTypeModalDiv', 6000);
              }, 500);
            }
          }, function(){
            $scope.getType();
          })
        })
      } //
    }
  }

  $scope.saveSortOrder = function(){
    if ($scope.type) {
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
  }

  $scope.cancelChanges = function(){
    $scope.dirty = false;
    $scope.clearCache();
    $scope.type = angular.copy($scope.backup);
  }

  $scope.changeActivity = function(code){
    if ($scope.type && $scope.type.type && code && code.code) {
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
            }, 1000);
          }, function(){
            $timeout(function(){
              $scope.getType();
              $scope.deactivateButtons = false;
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            }, 1000);
          })
        } else {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
          Business.articleservice.activateCode($scope.type.type, code.code).then(function() {
            $timeout(function(){
              $scope.getType();
              $scope.deactivateButtons = false;
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            }, 1000);
          }, function(){
            $timeout(function(){
              $scope.getType();
              $scope.deactivateButtons = false;
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            }, 1000);
          })
        }
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
    if ($scope.type && $scope.type.type) {

      var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/editcode.html',
        controller: 'AdminEditCodeCtrl',
        size: 'sm',
        resolve: {
          code: function () {
            return code;
          },
          type: function () {
            return $scope.type.type;
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
      }, function () {
        // console.log('The edit was canceled or failed');
        // cancled or failed
      });
    }
  }


  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);
}]);







app.controller('AdminEditCodeCtrl', ['$scope', '$uiModalInstance', 'code', 'type', 'size', 'business', '$timeout', function ($scope, $uiModalInstance, code, type, size, Business, $timeout) {
  $scope.code = angular.copy(code);
  $scope.addCodeFlg = false;
  // console.log('===CODE===', code);
  
  $scope.editorContent = '';
  if ($scope.code && $scope.code.description) {
    $scope.editorContent = angular.copy($scope.code.description);
  } 

  if (!$scope.code) {
    $scope.addCodeFlg = true;
    $scope.code = {};
  }

  $scope.getMaxCode = function() {
    return $scope.code? $scope.code.length? $scope.code.length: 100 : 100;
  };
  
  $scope.ok = function (validity) {
    if (validity) {

      console.log('$scope.code', $scope.code);
      console.log('type', type);
      console.log('type', type);
      var cont = true;
      if ($scope.addCodeFlg) {
        cont = confirm("Once this form is saved, the code field will be fixed. Continue?");
      }
      if ($scope.code && $scope.code.code && cont) {
        //save the code change.
        $scope.code.detailUrl = $scope.code.fullTextLink;
        // console.log('$scope.code', $scope.code);
        $scope.code.label = $scope.code.label || '';
        $scope.code.description = $scope.code.description || '';
        $scope.code.articleFilename = $scope.code.articleFilename || '';
        $scope.code.detailUrl = $scope.code.detailUrl || '';
        $scope.code.groupCode = $scope.code.groupCode || '';
        $scope.code.sortOrder = $scope.code.sortOrder || null;
        if (!$scope.code.sortOrder) {
          delete $scope.code.sortOrder;
        }
        if (!code && !code.code) {
          code = {};
          code.code = '';
        }
        Business.articleservice.getCode(type, $scope.code.code, true).then(function(result){

          var cont = true;
          if (result && $scope.addCodeFlg) {
            cont = confirm('Warning: You will be overriding a previously saved code by saving this form. Continue?');
          }
          if (cont) {
            Business.articleservice.saveCode(type, code.code, $scope.code, $scope.addCodeFlg).then(function(result){
              if (result) {
                $scope.addCodeFlg = false;
                // console.log('Code save result', result);
                // console.log('$scope.code', $scope.code);
                $uiModalInstance.close('success');
              } else {
                // console.log('The code failed but the call succeded');
              }
            }, function(){
              // the save failed
              // console.log('The code save failed');
            })
          }
        }, function(){
          Business.articleservice.saveCode(type, code.code, $scope.code, $scope.addCodeFlg).then(function(result){
            if (result) {
              $scope.addCodeFlg = false;
              // console.log('Code save result', result);
              // console.log('$scope.code', $scope.code);
              $uiModalInstance.close('success');
            } else {
              // console.log('The code failed but the call succeded');
            }
          }, function(){
            // the save failed
            // console.log('The code save failed');
          })
          //unable to finish code check;
        })
      } //
    }
  };

  $scope.cancel = function () {
    // console.log('$scope.code', $scope.code);
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
