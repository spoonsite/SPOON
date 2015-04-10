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

/*global isEmpty*/

app.controller('SubmissionCtrl', ['$scope', 'localCache', 'business', '$filter', '$timeout', '$location', '$rootScope', '$q', '$route', '$anchorScroll', 'FileUploader',  function ($scope,  localCache, Business, $filter, $timeout, $location, $rootScope, $q, $route, $anchorScroll, FileUploader) { /*jshint unused: false*/

  $scope.test = 'This is a test';

  $scope.name;
  $scope.email;
  $scope.organization;
  $scope.current = 'top';

  $scope.vitals = {};
  $scope.vitals.name;
  $scope.vitals.description;
  $scope.vitals.organization;
  $scope.vitals.requiredAttributes = [];

  $scope.details = {};

  $scope.getStarted = function(){
    return $scope.name && $scope.email && $scope.organization;
  }

  $scope.vitalsCheck = function(){
    return $scope.getStarted() && $scope.vitals && $scope.vitals.name && $scope.vitals.description && $scope.vitals.organization && $scope.type;
  }

  $scope.loadLookup = function(lookup, entity, loader){
    $scope.$emit('$TRIGGERLOAD', loader);

    Business.lookupservice.getLookupCodes(lookup, 'A').then(function (results) {
      $scope.$emit('$TRIGGERUNLOAD', loader);
      if (results) {
        $scope[entity]= results;
      }        
    });      
  };

  $scope.getAttributes = function (override) { 
    Business.getFilters(override, false).then(function (result) {
      $scope.allAttributes = result ? angular.copy(result) : [];
      $scope.requiredAttributes = _.filter($scope.allAttributes, {requiredFlg: true});
    });
  }; 
  
  $scope.getCodesForType = function(type){
    var foundType = _.find($scope.allAttributes, {attributeType: type});
    return foundType !== undefined ? foundType.codes : [];
  }; 

  $scope.loadLookup('ResourceType', 'resourceTypes', 'resourceFormLoader');  
  $scope.loadLookup('ContactType', 'contactTypes', 'contactFormLoader'); 

  $scope.resourceUploader = new FileUploader({
    url: 'Resource.action?UploadResource',
    alias: 'file',
    queueLimit: 1,  
    removeAfterUpload: true,
    onBeforeUploadItem: function(item) {
      $scope.$emit('$TRIGGERLOAD', 'resourceFormLoader');

      item.formData.push({
        "componentResource.componentId" : $scope.componentForm.componentId
      });
      item.formData.push({
        "componentResource.resourceType" : $scope.resourceForm.resourceType
      });
      item.formData.push({
        "componentResource.description" : $scope.resourceForm.description
      });
      item.formData.push({
        "componentResource.restricted" : $scope.resourceForm.restricted
      });        
      if ($scope.resourceForm.resourceId) {
        item.formData.push({
          "componentResource.resourceId" : $scope.resourceForm.resourceId
        });
      }
    },
    onSuccessItem: function (item, response, status, headers) {
      $scope.$emit('$TRIGGERUNLOAD', 'resourceFormLoader');

        //check response for a fail ticket or a error model
        if (response.success) {
          triggerAlert('Uploaded successfully', 'saveResource', 'componentWindowDiv', 3000); 
          $scope.cancelResourceEdit();
          $scope.loadResources();
        } else {
          if (response.errors) {
            var uploadError = response.errors.file;
            var enityError = response.errors.componentResource;
            var errorMessage = uploadError !== undefined ? uploadError : '  ' + enityError !== undefined ? enityError : '';
            triggerAlert('Unable to upload resource. Message: <br> ' + errorMessage, 'saveResource', 'componentWindowDiv', 6000);
          } else {
            triggerAlert('Unable to upload resource. ', 'saveResource', 'componentWindowDiv', 6000);
          }
        }
      },
      onErrorItem: function (item, response, status, headers) {
        $scope.$emit('$TRIGGERUNLOAD', 'resourceFormLoader');
        triggerAlert('Unable to upload resource. Failure communicating with server. ', 'saveResource', 'componentWindowDiv', 6000);      
      }      
    });     



    $scope.loadLookup('MediaType', 'mediaTypes', 'mediaFormLoader'); //


    $scope.scrollTo = function(id, current, parent, $event) {
      var offset = 120;
      if($event) {
        $event.preventDefault();
        $event.stopPropagation();
      }
      $('li a:focus').blur();
      $scope.current = current;
      $timeout(function(){
        $('[data-spy="scroll"]').each(function () {
          var $spy = $(this).scrollspy('refresh')
        })

        $timeout(function(){
          if ($location.hash() !== id) {
          // set the $location.hash to `newHash` and
          // $anchorScroll will automatically scroll to it
          $location.hash(id);
        } else {
          // call $anchorScroll() explicitly,
          // since $location.hash hasn't changed
          $anchorScroll();
        }
        $timeout(function(){
          var topScroll = $(document).height() - ($(window).scrollTop() + $(window).height()) ;
          var returnScroll = ($(window).scrollTop() - $('#'+id).offset().top);
          if (topScroll === 0 && returnScroll < 0 ) {
            returnScroll = offset - (returnScroll  * -1);
          } else {
            returnScroll = offset
          }
          window.scrollBy(0, -returnScroll);
        })
        $timeout(function(){
          $('li.active').removeClass('active');
          $('[data-target="#'+parent+'"').addClass('active');
          $('[data-target="#'+id+'"').addClass('active');
        },100)
      })
    }) //
  };//


  $timeout(function(){
    $scope.$apply(function(){
      $('body').scrollspy({ target: '#scrollSpy', offset: 100 });
      $scope.scrollTo('top', 'top');
    })
  })

}]);
