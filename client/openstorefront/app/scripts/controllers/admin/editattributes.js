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

app.controller('AdminEditattributesCtrl',['$scope','business', '$uiModal', '$timeout', 'FileUploader', function ($scope, Business, $uiModal, $timeout, FileUploader) {
  $scope.predicate = 'description';
  $scope.reverse = false;
  $scope.flags = {};
  $scope.flags.showUpload = false;
  $scope.data = {};
  $scope.data.allTypes = {};
  $scope.pagination = {};
  $scope.pagination.control = {};
  $scope.pagination.features = {'dates': false, 'max': false};

  $scope.getFilters = function (override, all) {
    $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
    if ($scope.pagination.control && $scope.pagination.control.refresh) {
      $scope.pagination.control.refresh().then(function(){
        $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
      });
    } else {
      $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
    }
  };
  $timeout(function(){
    $scope.getFilters();
  },10)

  $scope.refreshFilterCache = function() {
    $scope.getFilters(true, false);
  };

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
    $scope.pagination.control.changeSortOrder(predicate);
  };

  $scope.pagination.control.setPredicate = function(val){
    $scope.setPredicate(val, false);
  };

  $scope.editLanding = function(type, code) {
    $scope.$parent.editLanding(type, code);
  };

  $scope.deleteAttribute = function(filter){
    // console.log('Deleted filter', filter);
  };

  $scope.deleteFilter = function(filter){
    var cont = confirm("You are about to permanently remove an attribute from the system. This will affect all related components.  Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
      Business.articleservice.deleteFilter(filter.attributeType).then(function(){
        $timeout(function(){
          triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusAttributes', 'body', 3000);    
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

  $scope.changeActivity = function(filter){
    var cont = confirm("You are about to change the active status of an Attribute (Enabled or disabled). This will affect all related component attributes.  Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      if (filter.activeStatus === 'A') {
        $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
        Business.articleservice.deactivateFilter(filter.attributeType).then(function(){
          $timeout(function(){
            triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusAttributes', 'body', 3000);    
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
        Business.articleservice.activateFilter(filter.attributeType).then(function() {
          $timeout(function(){
            triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusAttributes', 'body', 3000);    
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

  $scope.editType = function(type){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/editcodes.html',
      controller: 'AdminEditcodesCtrl',
      size: 'lg',
      backdrop: 'static',
      resolve: {
        type: function () {
          return type;
        },
        size: function() {
          return 'lg';
        }
      }
    });

    modalInstance.result.then(function (result) {
      $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
      $scope.getFilters(true);
    }, function (result) {
      if (result.type && result.code) {
        $scope.editLanding(result.type, result.code);
      } else {
        $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
        $scope.getFilters(true);
      }  
    });
  }

  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);
  
  $scope.export = function(){
    window.location.href = "api/v1/resource/attributes/export"; 
  };

  $scope.confirmAttributeUpload = function(isAttributeUploader){
    var cont = false;
    if (isAttributeUploader){
      cont = confirm('Please verify that this file is the allattributes.csv file with a header similiar to this: (order and letter case matters)\nAttribute Type, Description, Architecture Flag, Visible Flag, Important Flag, Required Flag, Code, Code Label, Code Description, External Link, Group, Sort Order, Architecture Code, Badge Url');
      if (cont){
        $scope.attributeUploader.uploadAll();
        document.getElementById('attributeUploadFile').value = null;
      }
    } else {
      cont = confirm('Please verify that this file is the svcv-4_export.csv file with a header similiar to this: (order and letter case matters)\nTagValue_UID, TagValue_Number, TagValue_Service Name, TagNotes_Service Definition, TagNotes_Service Description, TagValue_JCA Alignment, TagNotes_JCSFL Alignment, TagValue_JARM/ESL Alignment, TagNotes_Comments');
      if (cont){
        $scope.svcv4uploader.uploadAll();
        document.resourceUIForm.uploadFile.value = null;
        document.getElementById('svcv4UploadFile').value = null;
      }
    }
  };

  $scope.attributeUploader = new FileUploader({
    url: 'Upload.action?UploadAttributes',
    alias: 'uploadFile',
    queueLimit: 1, 
    removeAfterUpload: true,
    filters: [{
      name: 'csv',    
      fn: function(item) {
        return true;
      }
    }],
    onBeforeUploadItem: function(item) {
      $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
    },
    onSuccessItem: function (item, response, status, headers) {
      $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');

      //check response for a fail ticket or a error model
      if (response.success) {
        triggerAlert('Uploaded successfully.  Watch Job-Tasks for completion of processing.', 'importAttributes', 'body', 3000);          
        $scope.flags.showUpload = false;
        $scope.getFilters(true);
      } else {
        if (response.errors) {
          var uploadError = response.errors.uploadFile;  
          var errorMessage = '';
          if (uploadError){
            errorMessage = uploadError;
          }          
          triggerAlert('Unable to import attributes. Message: <br> ' + errorMessage, 'importAttributes', 'body', 6000);
        } else {
          triggerAlert('Unable to import attributes. ', 'importAttributes', 'body', 6000);
        }
      }
    },
    onErrorItem: function (item, response, status, headers) {
      $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
      triggerAlert('Unable to import attributes. Failure communicating with server. ', 'importAttributes', 'body', 6000);    
    }      
  });  

$scope.svcv4uploader = new FileUploader({
  url: 'Upload.action?UploadSvcv4',
  alias: 'uploadFile',
  queueLimit: 1,
  removeAfterUpload: true,
  filters: [{
    name: 'csv',    
    fn: function(item) {
      return true;
    }
  }],
  onBeforeUploadItem: function(item) {
    $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
  },
  onSuccessItem: function (item, response, status, headers) {
    $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');

      //check response for a fail ticket or a error model
      if (response.success) {
        triggerAlert('Uploaded successfully.  Watch Job-Tasks for completion of processing', 'importAttributes', 'body', 3000);          
        $scope.flags.showUpload = false;
        $scope.getFilters(true);
      } else {
        if (response.errors) {
          var uploadError = response.errors.uploadFile;  
          var errorMessage = '';
          if (uploadError){
            errorMessage = uploadError;
          }          
          triggerAlert('Unable to import svcv4 data. Message: <br> ' + errorMessage, 'importAttributes', 'body', 6000);
        } else {
          triggerAlert('Unable to import  svcv4 data. ', 'importAttributes', 'body', 6000);
        }
      }
    },
    onErrorItem: function (item, response, status, headers) {
      $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
      triggerAlert('Unable to import  svcv4 data. Failure communicating with server. ', 'importAttributes', 'body', 6000);    
    }    
  });  

var stickThatTable = function(){
  var offset = $('.top').outerHeight() + $('#editAttributesToolbar').outerHeight();
  $(".stickytable").stickyTableHeaders({
    fixedOffset: offset
  });
}

$(window).resize(stickThatTable);
$timeout(stickThatTable, 100);

}]);