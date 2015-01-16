/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

app.controller('AdminLookupCtrl', ['$scope', 'business', '$rootScope', '$uiModal', function ($scope, Business, $rootScope, $uiModal) {

    $scope.lookups = [];
    $scope.predicate = [];
    $scope.reverse = [];    

    $scope.refreshLookups = function () {
      $scope.$emit('$TRIGGERLOAD', 'lookupLoader');

      Business.lookupservice.getLookups().then(function (results) {
        if (results) {
          $scope.lookups = results;
        }
        $scope.$emit('$TRIGGERUNLOAD', 'lookupLoader');
      });
    };
    $scope.refreshLookups();

    $scope.setPredicate = function(predicate, table) {
      if ($scope.predicate[table] === predicate) {
        $scope.reverse[table] = !$scope.reverse[table];
      } else {
        $scope.predicate[table] = predicate;
        $scope.reverse[table] = false;
      }
    };

    $scope.editLookupCodes = function(lookupEntity) {
      var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/editLookupCodes.html',
        controller: 'AdminEditLookupEntityCtrl',
        size: 'lg',
        resolve: {
          lookupEntity: function () {
            return lookupEntity;
          }
        }
      });      
    };

}]);

app.controller('AdminEditLookupEntityCtrl', ['$scope', '$uiModalInstance', 'lookupEntity', 'business', '$uiModal', 'FileUploader',
  function ($scope, $uiModalInstance, lookupEntity, Business, $uiModal, FileUploader) {
  
    $scope.showInactive = false;
    $scope.statusFilterOptions = [
      {code: 'A', desc: 'Active'},
      {code: 'I', desc: 'Inactive'}
    ];
    $scope.filterstatus = {};
    $scope.filterstatus.status = $scope.statusFilterOptions[0];
    $scope.lookupCodes = [];
    $scope.predicate = [];
    $scope.reverse = [];    
    $scope.tablename = lookupEntity.code;   
    $scope.flags = {};
    $scope.flags.showUpload = false;
    
    $scope.uploader = new FileUploader({
      url: 'Upload.action?UploadLookup&entityName=' + lookupEntity.code,
      alias: 'uploadFile',
      queueLimit: 1,      
      filters: [{
        name: 'csv',    
        fn: function(item) {
            return true;
        }
      }],
      onBeforeUploadItem: function(item) {
        $scope.$emit('$TRIGGERLOAD', 'lookupCodeLoader');
      },
      onSuccessItem: function (item, response, status, headers) {
        $scope.$emit('$TRIGGERUNLOAD', 'lookupCodeLoader');
        $scope.uploader.clearQueue();
        
        //check response for a fail ticket or a error model
        if (response.success) {
          triggerAlert('Uploaded successfully', 'importCode', '#lookupWindowDiv', 3000);          
          $scope.flags.showUpload = false;
          $scope.refreshLookupCodes();
        } else {
          if (response.errors) {
            var uploadError = response.errors.uploadFile;
            var enityError = response.errors.entityName;
            var errorMessage = uploadError !== undefined ? uploadError : '  ' + enityError !== undefined ? enityError : '';
            triggerAlert('Unable to import codes. Message: <br> ' + errorMessage, 'importCode', '#lookupWindowDiv', 6000);
          } else {
            triggerAlert('Unable to import codes. ', 'importCode', '#lookupWindowDiv', 6000);
          }
        }
      },
      onErrorItem: function (item, response, status, headers) {
        $scope.$emit('$TRIGGERUNLOAD', 'lookupCodeLoader');
        triggerAlert('Unable to import codes. Failure communicating with server. ', 'importCode', '#lookupWindowDiv', 6000);
        $scope.uploader.clearQueue()
      }      
    });
    
    $scope.setPredicate = function(predicate, table) {
      if ($scope.predicate[table] === predicate) {
        $scope.reverse[table] = !$scope.reverse[table];
      } else {
        $scope.predicate[table] = predicate;
        $scope.reverse[table] = false;
      }
    };    
  
    $scope.refreshLookupCodes = function(){
      $scope.$emit('$TRIGGERLOAD', 'lookupCodeLoader');

      Business.lookupservice.getLookupCodes(lookupEntity.code, $scope.filterstatus.status.code).then(function (results) {
        if (results) {
          $scope.lookupCodes = results;
        }
        $scope.$emit('$TRIGGERUNLOAD', 'lookupCodeLoader');
      });      
    };
    $scope.refreshLookupCodes();
    
    $scope.$on('$REFRESH_CODES', function(){
        triggerAlert('Saved successfully', 'editCode', 'body', 3000);
        $scope.refreshLookupCodes();
    });   
    
    $scope.editLookupCode = function(lookupCode){
        var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/editLookupCodeForm.html',
        controller: 'AdminEditLookupCodeCtrl',
        size: 'lg',
        resolve: {
          lookupCode: function () {
            return lookupCode;
          },
          lookupEntity: function(){
            return lookupEntity.code;
          },
          editMode: function(){
            return true;
          }
        }
      });     
    };
    
    $scope.addLookupCode = function(){
        var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/editLookupCodeForm.html',
        controller: 'AdminEditLookupCodeCtrl',
        size: 'lg',
        resolve: {
          lookupCode: function () {
            return {};
          },
          lookupEntity: function(){
            return lookupEntity.code;
          },
          editMode: function(){
            return false;
          }
        }
      });       
    };
    
    $scope.close = function(){
      $uiModalInstance.dismiss('cancel');
    };
    
    $scope.changeActivity = function(code){
       
      if (code.activeStatus === 'A'){
        Business.lookupservice.deactivateLookupCode(lookupEntity.code, code.code).then(function (results) {
          $scope.refreshLookupCodes();
        });
      } else {
        Business.lookupservice.activateLookupCode(lookupEntity.code, code.code).then(function (results) {
          $scope.refreshLookupCodes();
        });        
      }      
    };
    
    $scope.export = function(){
      window.location.href = "api/v1/resource/lookuptypes/" + lookupEntity.code + "/export";
    };
        
}]);

app.controller('AdminEditLookupCodeCtrl', ['$scope', '$uiModalInstance', 'lookupCode', 'lookupEntity', 'business', '$uiModal', 'editMode',
  function ($scope, $uiModalInstance, lookupCode, lookupEntity, Business, $uiModal, editMode) {

    $scope.lookupCodeForm = angular.copy(lookupCode);
    $scope.lookupCodeName = lookupCode.code;
    $scope.tablename = lookupEntity; 
    $scope.editMode = editMode;
    $scope.editModeText = $scope.editMode ? 'Edit' : 'Add';

    $scope.save = function () {
      $scope.$emit('$TRIGGERLOAD', 'codeFormLoader'); 
      
      var formData = {
        code: $scope.lookupCodeForm.code,
        description: $scope.lookupCodeForm.description,
        detailedDecription: $scope.lookupCodeForm.detailedDecription,
        sortOrder: $scope.lookupCodeForm.sortOrder
      };
           
      
      Business.lookupservice.saveLookupCode($scope.editMode, lookupEntity, lookupCode.code, formData).then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', 'codeFormLoader');
        $scope.$emit('$TRIGGEREVENT', '$REFRESH_CODES');  
        $uiModalInstance.dismiss('success');
      }, function(){
          triggerAlert('Unable to save. ', 'editCode', '#editLookupWindowDiv', 3000);
          $scope.$emit('$TRIGGERUNLOAD', 'codeFormLoader');
      });
    };


    $scope.cancel = function () {
      $uiModalInstance.dismiss('cancel');
    };

  }]);