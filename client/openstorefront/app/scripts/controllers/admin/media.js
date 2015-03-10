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

 app.controller('AdminMediaCtrl', ['$scope', 'business', '$rootScope', '$uiModal', function ($scope, Business, $rootScope, $uiModal) {

  $scope.predicate = [];
  $scope.reverse = [];   

  $scope.setPredicate = function(predicate, table) {
    if ($scope.predicate[table] === predicate) {
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
  };    

  $scope.refreshMedia = function(){
    $scope.$emit('$TRIGGERLOAD', 'mediaLoader');

    Business.mediaservice.getGeneralMedia().then(function (results) {
      if (results) {
        $scope.media = results;
      }
      $scope.$emit('$TRIGGERUNLOAD', 'mediaLoader');
    });      
  };
  $scope.refreshMedia();

  $scope.$on('$REFRESH_MEDIA', function(){        
    $scope.refreshMedia();
  });    

  $scope.removeMedia = function(media){
    var response = window.confirm("Are you sure you want DELETE " + media.name + "?");
    if (response) {

      $scope.$emit('$TRIGGERLOAD', 'mediaLoader');
      Business.mediaservice.removeMedia(media.name).then(function (results) {
        $scope.refreshMedia();          
        $scope.$emit('$TRIGGERUNLOAD', 'mediaLoader');
      });
    }
  };

  $scope.addMedia = function(){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/addGeneralMedia.html',
      controller: 'AdminAddMediaCtrl',
      backdrop: 'static',
      size: 'sm',
      resolve: {
        title: function () {
          return 'Import Media File';
        },
        url: function () {
          return 'Media.action?UploadGeneralMedia';
        },
        single: function () {
          return false;
        },
        alias: function () {
          return 'file';
        }
      }
    });       
  };

}]);

app.controller('AdminAddMediaCtrl', ['$scope', '$uiModalInstance', 'title', 'url', 'alias', 'single', 'business', '$uiModal', 'FileUploader',
  function ($scope, $uiModalInstance, title, url, alias, single, Business, $uiModal, FileUploader) {
    $scope.title = title;
    $scope.url = url;
    $scope.single = single;
    $scope.alias = alias;
    $scope.mediaForm = {};
    
    $scope.saveMedia = function(mediaUIForm){
      $scope.mediaUIForm = mediaUIForm;
      $scope.mediaUploader.uploadAll();
      document.mediaUIForm.uploadFile.value = null;
    };
    
    var getNewFileUpload = function () {
      return new FileUploader({
        url: $scope.url,
        alias: $scope.alias,
        queueLimit: 1,
        onBeforeUploadItem: function (item) {
          $scope.$emit('$TRIGGERLOAD', 'mediaFormLoader');
          if (!$scope.single) {
            item.formData.push({
              "generalMedia.name": $scope.mediaForm.name
            });
          }
        },
        onSuccessItem: function (item, response, status, headers) {
          $scope.$emit('$TRIGGERUNLOAD', 'mediaFormLoader');

          //check response for a fail ticket or a error model
          if (response.success) {  
            $scope.mediaUploader.clearQueue();
            $scope.mediaUploader.cancelAll();
            triggerAlert('Uploaded successfully', 'saveMedia', 'body', 3000);
            $scope.close();
            $scope.$emit('$TRIGGEREVENT', '$REFRESH_MEDIA');
          } else {
            if (response.errors) {              
              var uploadError = response.errors.file;
              var enityError = response.errors.generalMedia;
              var errorMessage = uploadError !== undefined ? uploadError : '  ' + enityError !== undefined ? enityError : '';
              if (!$scope.single) {
                $scope.mediaUIForm.name.$error.required = true;
              }
              $scope.mediaUploader.clearQueue();
              $scope.mediaUploader.cancelAll();               
              triggerAlert('Unable to upload media. Message: <br> ' + errorMessage, 'saveMedia', 'body', 30000);
            } else {
              triggerAlert('Unable to upload media. ', 'saveMedia', 'body', 6000);
            }
          }
        },
        onErrorItem: function (item, response, status, headers) {
          $scope.$emit('$TRIGGERUNLOAD', 'mediaFormLoader');
          triggerAlert('Unable to upload media. Failure communicating with server. ', 'saveMedia', 'body', 6000);          
          $scope.mediaUploader.clearQueue();
          $scope.mediaUploader.cancelAll();
        }
      });
};

$scope.mediaUploader = getNewFileUpload();


$scope.close = function(){
  $uiModalInstance.dismiss('cancel');
};

}]);