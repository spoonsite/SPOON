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

app.controller('AdminEditcodesCtrl', ['$scope', '$uiModalInstance', '$uiModal', 'type', 'size', 'business', '$timeout', '$filter', function ($scope, $uiModalInstance, $uiModal, type, size, Business, $timeout, $filter) {
  $scope.type = angular.copy(type);
  $scope.backup = angular.copy(type);
  $scope.size = size;
  $scope.predicate = 'sortOrder';
  $scope.check = {};
  $scope.reverse = false;
  $scope.current = 0;
  $scope.dirty = false;
  $scope.addTypeFlg = false;
  $scope.changed = false;

  $scope.data = {};
  $scope.data.allCodes = {};
  $scope.pagination = {};
  $scope.pagination.control = {};
  $scope.pagination.features = {'dates': false, 'max': false};


  $scope.$watch('data', function(){
    if ($scope.data.allCodes) {
      $scope.type.codes = $scope.data.allCodes.data || [];
    }
  }, true)

  $scope.getCodes = function (override, all) {
    console.log('we\'re getting types');
    $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
    if ($scope.pagination.control && $scope.pagination.control.refresh) {
      $scope.pagination.control.refresh().then(function(){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
      });
    } else {
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
    }
  };
  $timeout(function(){
    $scope.getCodes();
  },10)


  $scope.setFeatures = {
    'dates': false,
    'max': false
  }

  if (!$scope.type) {
    $scope.addTypeFlg = true;
    $scope.type = {};
  }

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
    $scope.pagination.control.changeSortOrder(predicate);
  };


  $scope.ok = function (type, code) {
    console.log('we closed the edit code');
    
    var result = {};
    $uiModalInstance.close(result);
  };

  $scope.cancel = function (type, code) {
    console.log('we closed the edit code');
    
    var result = {};
    result.type = type;
    result.code = code;
    $uiModalInstance.dismiss(result);
  };


  $scope.saveType = function(validity){
    if (validity) {

      var cont = true;
      if ($scope.addTypeFlg) {
        cont = confirm("Once this form is saved, the type field will be fixed. Continue?");
      }
      if ($scope.type && $scope.type.attributeType && cont) {
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

        Business.articleservice.getType($scope.type.attributeType, false, true).then(function(result){
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
                  $scope.getCodes();
                  $scope.dirty = false;
                  triggerAlert('Your edits were saved', 'editUserProfile', '#editTypeModalDiv', 6000);
                }, 500);
              }
            }, function(){
              $scope.getCodes();
            })
          }
        }, function(){
          Business.articleservice.saveType(type, $scope.addTypeFlg).then(function(result){
            if (result) {
              $scope.addTypeFlg = false;
              $scope.changed = true;
              $timeout(function(){
                $scope.getCodes();
                $scope.dirty = false;
                triggerAlert('Your edits were saved', 'editUserProfile', '#editTypeModalDiv', 6000);
              }, 500);
            }
          }, function(){
            $scope.getCodes();
          })
        })
      } //
    }
  }

  $scope.deleteCode = function(code){
    var cont = confirm("You are about to permanently remove an attribute code from the system. This will affect all related components. Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
      Business.articleservice.deleteCode($scope.type.attributeType, code.code).then(function(){
        $timeout(function(){
          $scope.getCodes();
          $scope.deactivateButtons = false;
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
        }, 1000);
      }, function(){
        $timeout(function(){
          $scope.getCodes();
          $scope.deactivateButtons = false;
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
        }, 1000);
      })
    }
  }

  $scope.changeActivity = function(code){
    if ($scope.type && $scope.type.attributeType && code && code.code) {
      var cont = confirm("You are about to change the active status of an Attribute (Enabled or disabled). Continue?");
      if (cont) {
        $scope.deactivateButtons = true;
        if (code.activeStatus === 'A') {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
          Business.articleservice.deactivateCode($scope.type.attributeType, code.code).then(function(){
            $timeout(function(){
              $scope.getCodes();
              $scope.deactivateButtons = false;
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            }, 1000);
          }, function(){
            $timeout(function(){
              $scope.getCodes();
              $scope.deactivateButtons = false;
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            }, 1000);
          })
        } else {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
          Business.articleservice.activateCode($scope.type.attributeType, code.code).then(function() {
            $timeout(function(){
              $scope.getCodes();
              $scope.deactivateButtons = false;
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminTypeRefresh');
            }, 1000);
          }, function(){
            $timeout(function(){
              $scope.getCodes();
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
    if ($scope.type && $scope.type.attributeType) {

      var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/editcode.html',
        controller: 'AdminEditCodeCtrl',
        size: 'lg',
        backdrop: 'static',
        resolve: {
          code: function () {
            return code;
          },
          type: function () {
            return $scope.type.attributeType;
          },
          size: function() {
            return 'lg';
          }
        }
      });

      modalInstance.result.then(function (result) {
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminTypeRefresh');
        $timeout(function(){
          $scope.getCodes();
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
  $scope.highlights = ['success', 'info', 'warning', 'danger', 'inverse'];

  $scope.getMaxCode = function() {
    return $scope.code? $scope.code.length? $scope.code.length: 100 : 100;
  };
  
  $scope.urlPattern = utils.URL_REGEX;

  $scope.ok = function (validity) {

    if (validity) {
      var cont = true;
      if ($scope.addCodeFlg) {
        cont = confirm("Once this form is saved, the code field will be fixed. Continue?");
      }
      if ($scope.code && $scope.code.code && cont) {
        //save the code change.
        // if (utils.URL_REGEX.test($scope.code.fullTextLink)) {
        //   $scope.code.detailUrl = $scope.code.fullTextLink;
        // } else {
        //   $scope.myForm.url.$setValidity($scope.code.fullTextLink, false);
        //   return false;
        // } // this is so that we can check against an actual url... but sometimes we use relative paths...
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
                // console.log('$scope.code', $scope.code);
                $uiModalInstance.close('success');
              } else {
                // console.log('The code failed but the call succeded');
              }
            }, function(){
              // the save failed
            })
          }
        }, function(){
          Business.articleservice.saveCode(type, code.code, $scope.code, $scope.addCodeFlg).then(function(result){
            if (result) {
              $scope.addCodeFlg = false;
              // console.log('$scope.code', $scope.code);
              $uiModalInstance.close('success');
            } else {
              // console.log('The code failed but the call succeded');
            }
          }, function(){
            // the save failed
          })
          //unable to finish code check;
        })
      } //
    } else {
      triggerAlert('There was an issue with your form data. Please verify that all of the data is correct, then try again.', 'formResult', 'editAttributeCodeForm', 7000);
    }
  };

  $scope.cancel = function () {
    // console.log('$scope.code', $scope.code);
    $uiModalInstance.dismiss('cancel');
  };
}]);


app.filter('offset', function() {
  return function(input, start) {
    start = parseInt(start, 10);
    return input.slice(start);
  };
});