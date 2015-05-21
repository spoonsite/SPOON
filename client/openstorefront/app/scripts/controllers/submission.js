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

app.controller('SubmissionCtrl', ['$scope', 'localCache', 'business', '$filter', '$timeout', '$location', '$rootScope', '$q', '$route', '$anchorScroll', 'FileUploader', '$templateCache', '$uiModal', '$sce',
  function ($scope,  localCache, Business, $filter, $timeout, $location, $rootScope, $q, $route, $anchorScroll, FileUploader, $templateCache, $uiModal, $sce) { /*jshint unused: false*/


    $scope.test = 'This is a test';
    $scope.badgeFound = false;
    $scope.lastMediaFile = '';

    $scope.submitter = {};
    $scope.submitter.firstName;
    $scope.submitter.lastName;
    $scope.submitter.email;
    $scope.submitter.organization;
    $scope.current = 'top';
    $scope.optIn = false;


    $scope.componentId;
    $scope.component = {};
    $scope.backup = {};
    $scope.component.component = {};
    $scope.component.attributes = {};

    $scope.component.metadata = [];
    $scope.metadataForm = {};

    $scope.component.tags = [];
    $scope.tagsForm = {};


    $scope.contactForm = {};
    $scope.component.contacts = [];

    $scope.component.media = [];
    $scope.mediaForm = {};
    $scope.showMediaUpload = 'true';
    $scope.isFull = false;


    $scope.component.resources = [];
    $scope.resourceForm = {};
    $scope.showResourceUpload = 'false';
    $scope.isFullResource = false;


    $scope.dependencyForm = {};
    $scope.component.externalDependencies = [];

    $scope.details = {};

    $scope.formMedia;

    $scope.setEditable = function($event){
      var response = window.confirm("Are you sure you want to resume editing your submission? This action remove your submission from the admin's pending queue and you will have to re-submit it for approval.");
      if (response && $scope.vitalsCheck()){
        if ($scope.component.component) {
          $scope.component.component.approvalState = 'N';
          $scope.submit(true).then(function(){
            $scope.scrollTo('vitals', 'vitals', '', $event, 'componentName');
          }, function(){
            if($event) {
              $event.preventDefault();
              $event.stopPropagation();
            }
          })
        }
      } 
    }

    $scope.getMediaHTML = function(media){
      return utils.getMediaHTML(media, $sce);
    }

    $scope.getResourceHTML = function(resource){
      return utils.getResourceHTML(resource, $sce);
    }

    $scope.getSubmission = function(){
      var deferred = $q.defer();
      if ($scope.componentId !== null && $scope.componentId !== undefined) {
        Business.submissionservice.getSubmission($scope.componentId, true).then(function(result){
          $scope.$broadcast('$UNLOAD', 'submissionLoader');
          if (result && result.component && result.component.componentId){
            $scope.backup = angular.copy(result);
            $scope.backup.media = _.uniq($scope.backup.media, 'componentMediaId');              
            $scope.backup.resources = _.uniq($scope.backup.resources, 'resourceId');              
            $scope.componentId = result.component.componentId;
            $scope.component = angular.copy(result);
            $scope.component.media = _.uniq($scope.component.media, 'componentMediaId');
            $scope.component.resources = _.uniq($scope.component.resources, 'resourceId');
            $scope.component.attributes = $scope.setupAttributes($scope.component.attributes);
            if ($scope.component.component.approvalState !== 'N') {
              $scope.scrollTo('reviewAndSubmit', 'submit', '', null)
            }
          }
          deferred.resolve();
        }, function(result){
          $scope.$broadcast('$UNLOAD', 'submissionLoader');
          deferred.reject();
        });
}
return deferred.promise;
}

$scope.init = function(){
  if ($location.search() && $location.search().id){
    console.log('$location', $location.search());
    $scope.componentId = $location.search().id;
    $scope.$broadcast('$LOAD', 'submissionLoader');
    $scope.getSubmission().then(function(){
      var found = _.find($scope.component.contacts, {'contactType':'SUB'});
      if (found){
        $scope.submitter = found;
      }
      $scope.optIn = $scope.component.component.notifyOfApprovalEmail? true: false;
    }, function(){
      $scope.componentId = null;
    });
  } else {
    $scope.componentId = null;
  }
}

$scope.loadLookup = function(lookup, entity, loader){
  var deferred = $q.defer();
  Business.lookupservice.getLookupCodes(lookup, 'A').then(function (results) {
    deferred.resolve();
    if (results) {
      $scope[entity]= results;
    }        
  });      
  return deferred.promise;
};
$scope.getAttributes = function (override) { 
  var deferred = $q.defer();
  Business.getFilters(override, false).then(function (result) {
    deferred.resolve();
    $scope.allAttributes = result ? angular.copy(result) : [];
    $scope.requiredAttributes = _.filter($scope.allAttributes, {requiredFlg: true, hideOnSubmission: false});
      // console.log('required', $scope.requiredAttributes);

      $scope.attributes = _.filter($scope.allAttributes, {requiredFlg: false});
    });
  return deferred.promise;
}; 

    // WHERE WE CALL INIT()!
    (function(){
      $q.all($scope.getAttributes(),
      //
      $scope.loadLookup('ContactType', 'contactTypes', 'contactFormLoader'),
      $scope.loadLookup('MediaType', 'mediaTypes', 'submissionLoader'),
      $scope.loadLookup('ResourceType', 'resourceTypes', 'resourceFormLoader')).then(function(){
        $scope.init();
      })
    })()

    $scope.formFocused = function(form, reset){

      var keys = _.keys(form);
      if (!reset){
        for (var i = 0; i < keys.length; i++){
          if (keys[i][0] !== '$'){
            if (form[keys[i]].$focused){
              return true;
            }
          }
        }
        return false;
      } else {
        // console.log('form', form);
        // console.log('form', $scope);
        for (var i = 0; i < keys.length; i++){
          if (keys[i][0] !== '$'){
            form[keys[i]].$hasBeenFocused = false
          }
        }
      }
    }

    $scope.getMimeTypeClass = function(type){
      if (type) {

        if (type.match('video.*')) {
          return 'fa-file-video-o'
        } else if (type.match('audio.*')){
          return 'fa-file-audio-o'
        } else if (type.match('application.*')){
          return 'fa-file-code-o'
        } else if (type.match('text.*')){
          return 'fa-file-text-o'
        } else if (type.match('image.*')){
          return 'fa-file-image-o'
        } else {
          return 'fa-file-o'
        }
      } else {return ''};
    }

    $scope.setBadgeFound = function(attribute){
      if (attribute && attribute.badgeUrl) {
        $scope.badgeFound = true;
      }
    }

    $scope.getAttributeTypeDesc = function(type){
      var found = _.find($scope.allAttributes, {'attributeType': type});
      if (found) {
        return found.description;
      }
      return '';
    }

    $scope.getResourceTypeDesc = function(type){
      var found = _.find($scope.resourceTypes, {'code': type});
      if (found) {
        return found.description;
      }
      return '';
    }

    $scope.getMediaTypeDesc = function(type){
      var found = _.find($scope.mediaTypes, {'code': type});
      if (found) {
        return found.description;
      }
      return '';
    }

    $scope.initialSave = function($event){
      if ($scope.vitalsCheck()){
        $scope.submit(true).then(function(){
          $scope.scrollTo('details', 'details', '', $event, 'tagLabel');
        }, function(){
          if($event) {
            $event.preventDefault();
            $event.stopPropagation();
          }
        })
      } 
    }

    $scope.updateSave = function($event){
      if ($scope.vitalsCheck()){
        $scope.submit(false).then(function(){
          $scope.scrollTo('reviewAndSubmit', 'submit', '', $event)
          $scope.detailsDone = true;
        }, function(){
          if($event) {
            $event.preventDefault();
            $event.stopPropagation();
          }
        })
        $scope.mediaUploader.uploadAll();
        $scope.resourceUploader.uploadAll();
      } 
    }

    $scope.finalSave = function($event){
      if ($scope.vitalsCheck()){
        $scope.submitForAproval(false).then(function(){
          $scope.scrollTo('reviewAndSubmit', 'submit', '', $event)
          $scope.detailsDone = true;
        }, function(){
          if($event) {
            $event.preventDefault();
            $event.stopPropagation();
          }
        })
      } 
    }

    $scope.createInitialSubmit = function(){
      $scope.component.component = $scope.component.component || {};
      $scope.component.component.activeStatus = $scope.component.activeStatus || 'A';
      if ($scope.componentId) {
        $scope.component.component.componentId = $scope.componentId; 
      }
      var component = angular.copy($scope.component);
      component.attributes = $scope.getCompactAttributes(true);
      _.each($scope.allAttributes, function(attribute) {
        if (attribute.hideOnSubmission) {
          var found = _.find(attribute.codes, {'code':attribute.defaultAttributeCode});
          var exists = _.find(component.attributes, {'attributeType': attribute.attributeType});
          if (found && !exists) {
            component.attributes.push({
              componentAttributePk: {
                attributeCode: found.code,
                attributeType: attribute.attributeType
              }
            });
          }
        }
      });
      var submitter = angular.copy($scope.submitter);
      submitter.contactType = 'SUB';

      var found = _.find(component.contacts, {'contactType': 'SUB'});
      if (found) {
        var index = _.indexOf(component.contacts, found);
        component.contacts[index] = submitter
      } else {
        component.contacts.push(submitter);
      }
      

      var deferred = $q.defer();

      // console.log('$scope.component', $scope.component);
      deferred.resolve(component);

      return deferred.promise;
    }


    $scope.submit = function(initial){
      $scope.$broadcast('$LOAD', 'submissionLoader');
      var deferred = $q.defer();
      if (initial){
        $scope.createInitialSubmit().then(function(component){
          var compare = angular.copy(component);
          for(var i = 0; i < compare.attributes.length; i++){
            compare.attributes[i] = {
              componentAttributePk: compare.attributes[i].componentAttributePk
            };
          }
          if (_.diff(compare,$scope.backup)) {
            console.log('INIT Diff', compare);
            console.log('INIT Diff', $scope.backup);
            console.log('INIT Diff', _.diff(compare,$scope.backup));
            console.log('component', component);
            
            Business.submissionservice.createSubmission(component).then(function(result){
              $scope.$broadcast('$UNLOAD', 'submissionLoader');
              if (result && result.component && result.component.componentId){
                $scope.backup = angular.copy(result);
                $scope.backup.media = _.uniq($scope.backup.media, 'componentMediaId');              
                $scope.backup.resources = _.uniq($scope.backup.resources, 'resourceId');              
                $scope.componentId = result.component.componentId;
                $scope.component = angular.copy(result);
                $scope.component.media = _.uniq($scope.component.media, 'componentMediaId');              
                $scope.component.resources = _.uniq($scope.component.resources, 'resourceId');              
                $scope.component.attributes = $scope.setupAttributes($scope.component.attributes);
              }
              // console.log('Success result', $scope.component);
              deferred.resolve();
            }, function(result){
              $scope.$broadcast('$UNLOAD', 'submissionLoader');
              deferred.reject();
              // console.log('Fail result', result);
            });
} else {
  $scope.$broadcast('$UNLOAD', 'submissionLoader');
  console.log('The init diff didn happen');
  deferred.resolve();
}
})
      } else {//
        $scope.createInitialSubmit().then(function(component){
          component.attributes = $scope.getCompactAttributes(true);
          if ($scope.optIn) {
            console.log('we opted in');
            
            component.component.notifyOfApprovalEmail = $scope.submitter.email;
          }

          component.contacts = component.contacts || [];

          _.each(component.contacts, function(contact){
            if (contact.contactType && contact.contactType.code){
              contact.contactType = contact.contactType.code;
            } else if (contact.contactType) {
            }
          })

          // console.log('$scope.component', component);
          var compare = angular.copy(component);
          for(var i = 0; i < compare.attributes.length; i++){
            compare.attributes[i] = {
              componentAttributePk: compare.attributes[i].componentAttributePk
            };
          }

          if (_.diff(compare,$scope.backup)) {
            console.log('UPDATE Diff', compare);
            console.log('UPDATE Diff', $scope.backup);
            console.log('UPDATE Diff', _.diff(compare,$scope.backup));
            console.log('component', component);
            
            Business.submissionservice.updateSubmission(component).then(function(result){
              $scope.$broadcast('$UNLOAD', 'submissionLoader');
              if (result && result.component && result.component.componentId){
                $scope.backup = angular.copy(result);
                $scope.backup.media = _.uniq($scope.backup.media, 'componentMediaId');              
                $scope.backup.resources = _.uniq($scope.backup.resources, 'resourceId');              
                $scope.componentId = result.component.componentId;
                $scope.component = angular.copy(result);
                $scope.component.media = _.uniq($scope.component.media, 'componentMediaId');              
                $scope.component.resources = _.uniq($scope.component.resources, 'resourceId');              
                $scope.component.attributes = $scope.setupAttributes($scope.component.attributes);
              }
              deferred.resolve();
              // console.log('Success result', result);
            }, function(result){
              $scope.$broadcast('$UNLOAD', 'submissionLoader');
              deferred.reject();
              // console.log('Fail result', result);
            });
} else {
  $scope.$broadcast('$UNLOAD', 'submissionLoader');
  console.log('The component did not change', compare, $scope.backup);
  deferred.resolve();
}
})
      }//
      return deferred.promise;
    }

    $scope.submitForAproval = function(){
      var deferred = $q.defer();
      $scope.submit(false).then(function(){
        if ($scope.component && $scope.component.component && $scope.component.component.componentId) {
          console.log('component', $scope.component);
          
          Business.submissionservice.submit($scope.component.component.componentId).then(function(){
            $scope.backup.component.approvalState = 'P';              
            $scope.component.component.approvalState = 'P';
            triggerAlert('Your component has been successfully submitted!', 'submitAlert', 'body', 8000);
            deferred.resolve();
          }, function(result){
            deferred.reject();
          });
        } else {
          deferred.reject();
        }
      })
      return deferred.promise;
    }



    $scope.$watch('current', function(){
      $scope.badgeFound = false;
      if ($scope.current && $scope.current === 'submit') {
        $scope.badgeFound = false;
        _.each($scope.component.attributes, function(attribute){
          $scope.setBadgeFound(attribute);
        })
      }
    })

    $scope.makeThisHappen = function(canHappen, form, func){
      if (canHappen){
        func(form);
        $scope.formFocused(form, true);
      }
    }



    $scope.checkAttributes = function(){
      // console.log('Compact list', _.compact($scope.component.attributes));
      // we need to compact the attributes list because it may have unused indexes.
      var list = angular.copy($scope.component.attributes);
      
      var requiredAttributes = _.filter(list, function(n){
        return n.requiredFlg && !n.hideOnSubmission;
      });
      
      if (requiredAttributes.length !== $scope.requiredAttributes.length) {
        return false;
      }
      return true;
    }

    $scope.setDefaultAttribute = function(index, attribute, required){

      if (required && !$scope.component.attributes[index]) {
        var found = _.find($scope.requiredAttributes, {'attributeType': attribute.attributeType});
        if (attribute.defaultAttributeCode) {
          found = _.find(attribute.codes, {code: attribute.defaultAttributeCode});
          if (found) {
            $scope.component.attributes[index] = found;
          }
        }
      } else {
        var found = _.find($scope.attributes, {'attributeType': attribute.attributeType});
        if (attribute.defaultAttributeCode) {
          found = _.find(attribute.codes, {code: attribute.defaultAttributeCode});
          if (found) {
            $scope.component.attributes[index] = found;
          }
        }
      }
    }

    $scope.getCompactAttributes = function(attributePK){
      // This is how we'll weed out the attributes we need for the submission
      var realAttributes = $scope.component.attributes;
      var attributes = [];
      _.each(realAttributes, function(attr){
        if (attr.constructor === Array){
          _.each(attr, function(item){
            if (attributePK && !item.componentAttributePk) {
              item.componentAttributePk = {
                'attributeType': item.attributeType,
                'attributeCode': item.code,
              };
            }
            attributes.push(item);
          })
        } else {
          if (attributePK && !attr.componentAttributePk) {
            attr.componentAttributePk = {
              'attributeType': attr.attributeType,
              'attributeCode': attr.code,
            };
          }
          attributes.push(attr);
        }
      })

      
      return attributes;      
    }

    $scope.setupAttributes = function(attributes){
      var result = {};
      _.each(attributes, function(attribute){
        var foundAttr = _.find($scope.allAttributes, {'attributeType': attribute.componentAttributePk.attributeType});
        if (foundAttr) {
          var foundAttr = $filter('makeattribute')(foundAttr.codes, foundAttr);
          var found = _.find(foundAttr, {'code': attribute.componentAttributePk.attributeCode});
          var merged = _.merge(found, attribute);
          if (merged.requiredFlg) {
            result[attribute.componentAttributePk.attributeType] = merged;
          } else {
            if (!result[attribute.componentAttributePk.attributeType]) {
              result[attribute.componentAttributePk.attributeType] = []
            } 
            result[attribute.componentAttributePk.attributeType].push(merged);
          }
        }
      })
      return result;
    }

    $scope.getComponent = function(){
      return JSON.stringify($scope.component, null, 4);
    }

    // Metadata section
    $scope.removeMetadata = function(index){
      if (!($scope.component.component.approvalState && $scope.component.component.approvalState !== 'N')) {

        $scope.component.metadata.splice(index, 1);
      }
    }
    $scope.addMetadata = function(form){
      if ($scope.metadataForm.value && $scope.metadataForm.label) {
        $scope.component.metadata.push($scope.metadataForm);
        $scope.metadataForm = {};
        $scope.formFocused(form, true)
        $('#metadataLabel').focus();
      }
    }

    // tag section
    $scope.removeTag = function(index){
      if (!($scope.component.component.approvalState && $scope.component.component.approvalState !== 'N')) {
        $scope.component.tags.splice(index, 1);
      }
    }
    $scope.addTag = function(form){
      if ( $scope.tagsForm.text ) {
        var found = _.find($scope.component.tags, {'text':$scope.tagsForm.text});
        if (!found) {
          $scope.component.tags.push($scope.tagsForm);
          $scope.tagsForm = {};
          $scope.formFocused(form, true)
          $('#tagLabel').focus();
        }
      }
    }

  // contact section
  $scope.getContactTypeDesc = function(type){
    var found = _.find($scope.contactTypes, {'code': type});
    return  found? found.description : type;
  }
  $scope.removeContact = function(index){
    if (!($scope.component.component.approvalState && $scope.component.component.approvalState !== 'N')) {
      var originalLength = $scope.component.contacts.length;
      var afterLength = $filter('contactsfilter')($scope.component.contacts, 'contactType').length;
      index = index + (originalLength - afterLength);
      $scope.component.contacts.splice(index, 1);
    }
  }
  $scope.addContact = function(form){
    if ( $scope.contactForm ) {
      $scope.component.contacts.push($scope.contactForm);
      $scope.contactForm = {};
      $scope.formFocused(form, true)
      $('#contactType').focus();
    }
  }
  $scope.getContactType = function(type){
    var found = _.find($scope.contactTypes, {'code': type});
    return found? found.description: type;
  }


  // Media section
  $scope.oldMediaState = $scope.showMediaUpload;
  $scope.toggleShowMedia = function(val){
    if (val !== $scope.oldMediaState) {
      $scope.oldMediaState = val;
      $('#mediaUploadInput').val(null);
      $scope.mediaForm.mediaTypeCode = null;
      $scope.mediaForm.caption = null;
      $scope.mediaForm.link = null;
      $scope.lastMediaFile = '';
    }
  }


  $scope.resetMediaInput = function(){
    $('#mediaUploadInput').val(null);
    $scope.mediaForm.mediaTypeCode = null;
    $scope.mediaForm.caption = null;
    $scope.lastMediaFile = '';
  }

  $scope.addLinkToMedia = function(){
    $scope.component.media.push(angular.copy($scope.mediaForm));
    $('#mediaUploadInput').val(null);
    $scope.mediaForm.mediaTypeCode = null;
    $scope.mediaForm.link = null;
    $scope.mediaForm.caption = null;
    $scope.lastMediaFile = '';
  }  


  // Resource section
  $scope.oldResourceState = $scope.showResourceUpload;
  $scope.toggleShowResource = function(val){
    if (val !== $scope.oldResourceState) {
      $scope.oldResourceState = val;
      $('#resourceUploadInput').val(null);
      $scope.resourceForm.resourceType = null;
      $scope.resourceForm.description = null;
      $scope.resourceForm.link = null;
      $scope.lastResourceFile = '';
    }
  }


  $scope.resetResourceInput = function(){
    $('#resourceUploadInput').val(null);
    $scope.resourceForm.resourceType = null;
    $scope.resourceForm.description = null;
    $scope.lastResourceFile = '';
  }

  $scope.addLinkToResource = function(){
    $scope.component.resources.push(angular.copy($scope.resourceForm));
    $('#resourceUploadInput').val(null);
    $scope.resourceForm.resourceType = null;
    $scope.resourceForm.link = null;
    $scope.resourceForm.description = null;
    $scope.lastResourceFile = '';
  }

  // contact section
  $scope.removeDependency = function(index){
    if (!($scope.component.component.approvalState && $scope.component.component.approvalState !== 'N')) {
      $scope.component.externalDependencies.splice(index, 1);
    }
  }
  $scope.addDependency = function(form){
    // console.log('$scope.dependencyForm', $scope.dependencyForm);
    
    if ( $scope.dependencyForm ) {
      $scope.component.externalDependencies.push($scope.dependencyForm);
      $scope.formFocused(form, true)
      $scope.dependencyForm = {};
      $('#dependencyFormName').focus();
    }
  }

  // validation section
  $scope.getStarted = function(){
    // return true;
    return $scope.submitter.firstName && $scope.submitter.lastName && $scope.submitter.email && $scope.submitter.phone && $scope.submitter.organization;
  }

  $scope.vitalsCheck = function(log){
    // return true;
    if (false){
      console.log('getStarted', $scope.getStarted());
      console.log('component', $scope.component);
    }
    
    return $scope.getStarted() && $scope.component && $scope.component.component && $scope.component.component.name && $scope.component.component.description && $scope.component.component.organization && $scope.checkAttributes();
  }




  $scope.openInfo = function(attribute){
    var modalInstance = $uiModal.open({
      template: $templateCache.get('submission/attributesinfo.tpl.html'),
      controller: 'AttrsInfoCtrl',
      size: 'sm',
      resolve: {
        size: function() {
          return 'sm';
        },
        attribute: function() {
          return attribute;
        }
      }
    });

    modalInstance.result.then(function (result) {
    }, function () {
    });
  }

  
  $scope.getCodesForType = function(type){
    var foundType = _.find($scope.allAttributes, {attributeType: type});
    return foundType !== undefined ? foundType.codes : [];
  }; 





  // Media Handling functions
  $scope.srcList = []; //
  $scope.queue = [];
  $scope.resourceQueue = [];
  $scope.addMedia = function (file, queue, form, loader, caption, typeCode) { //
    // if ($scope.mediaForm.link || 
    //   $scope.mediaUploader.queue.length === 0) {

    //   if (!$scope.mediaForm.link) {          
    //     $scope.mediaForm.originalName = $scope.mediaForm.originalFileName;  
    //   } else {
    //     $scope.mediaForm.mimeType = '';
    //   }

      // $scope.saveEntity({
      //   alertId: 'saveMedia',
      //   alertDiv: 'componentWindowDiv',
      //   loader: 'submissionLoader',
      //   entityName: 'media',
      //   entity: $scope.mediaForm,
      //   entityId: $scope.mediaForm.componentMediaId,
      //   formName: 'mediaForm',
      //   loadEntity: function () {
      //     $scope.loadMedia();
      //   }
      // });
    // } else {
      file[typeCode] = $scope[form][typeCode];
      file[caption] = $scope[form][caption];
      file.mimeType = file._file? file._file.type: file.file.type;
      
      if (file._file){
        $scope.readFile(file._file, function(result){
          queue.push({file: file, dom:result});
          if(!$rootScope.$$phase) {
            $scope.$apply();
          }
          $scope.$emit('$TRIGGERUNLOAD', loader);
        });
      } else {
        queue.push({file: file, dom:'<span>No Link or Preview Available</span>'});
        if(!$rootScope.$$phase) {
          $scope.$apply();
        }
        $scope.$emit('$TRIGGERUNLOAD', loader);
      }
      // $scope.mediaUploader.uploadAll();
    // }
  };

  $scope.readFile = function(file, callback){

    var reader;
    if (file.type.match('image.*')) {
      // console.log('file ===  image', file);
      var reader = new FileReader();
      // Closure to capture the file information.
      reader.onload = (function(theFile, callback) {

        return function(e) {
          // Render thumbnail.
          callback(
           ['<img class="thumb" src="', e.target.result,'" title="', escape(theFile.name), '" width="230"    height="270"/>'].join('')
           )
        };
      })(file, callback);
      reader.readAsDataURL(file);
    }else if (file.type.match('audio.*')){
      var reader = new FileReader();
      // Closure to capture the file information.
      reader.onload = (function(theFile, callback) {
        return function(e) {
          // Render thumbnail.

          callback(
            ['<audio controls><source src="', e.target.result,'   "type="audio/ogg"><source src="', e.target.result,' "type="audio/mpeg"></audio>'].join('')
            )
        };
      })(file, callback);
      reader.readAsDataURL(file);
    }else if (file.type.match('video.*')){
      var URL = window.URL || window.webkitURL;
      var type = file.type;

      var videoNode = document.createElement('video');

      var canPlay = videoNode.canPlayType(type);

      canPlay = (canPlay === '' ? 'no' : canPlay);

      var message = 'Can play type "' + type + '": ' + canPlay;

      var isError = canPlay === 'no';

      // console.log(message, isError);

      if (isError) {
        return;
      }

      var fileURL = URL.createObjectURL(file);
      if (!URL) {
        callback('<span>Your browser is not <a href="http://caniuse.com/bloburls">supported</a>!</span>')
      }   
      var srcs = [];
      srcs.push({src: $sce.trustAsResourceUrl(fileURL).$$unwrapTrustedValue(), type: 'video/mp4'});             
      srcs.push({src: $sce.trustAsResourceUrl(fileURL).$$unwrapTrustedValue(), type: 'video/webm'});             
      srcs.push({src: $sce.trustAsResourceUrl(fileURL).$$unwrapTrustedValue(), type: 'video/ogg'});             
      callback(
        ['<videogular> <vg-media vg-src=\''+JSON.stringify(srcs)+'\' vg-preload="\'none\'" vg-native-controls="true"></vg-media></videogular>'].join('')
        )
    } else {
      callback('<span>No Link or Preview Available</span>')
    }
  }

  $scope.removeFromQueue = function(file){
    if (!($scope.component.component.approvalState && $scope.component.component.approvalState !== 'N')) {
      console.log('MediaUploader', $scope.mediaUploader);
      console.log('file', file);
      console.log('Queue', $scope.mediaUploader.queue);

      var found = _.find($scope.mediaUploader.queue, file.file);
      if (found){
        console.log('found', found);
        found = _.indexOf($scope.mediaUploader.queue, found);
        $scope.mediaUploader.queue.splice(found, 1);
        console.log('MediaUploader', $scope.mediaUploader);
      }
      found = _.find($scope.queue, file);
      if (found){
        console.log('found', found);
        found = _.indexOf($scope.queue, found);
        $scope.queue.splice(found, 1);
        console.log('QUEUE', $scope.queue);
      }
    }
  }

  $scope.hardDeleteEntity = function(options){
    var response = window.confirm("Are you sure you want to DELETE "+ options.entityName + "?");
    if (response && $scope.componentId) {
      $scope.$emit('$TRIGGERLOAD', options.loader);
      Business.submissionservice.forceRemoveEnity({
        componentId: $scope.componentId,
        entity: options.entityPath,
        entityId: options.entityId
      }).then(function (result) {          
        $scope.$emit('$TRIGGERUNLOAD', options.loader);
        options.loadEntity();
      });
    }
  };

  $scope.deleteMedia = function(media){
    if (!($scope.component.component.approvalState && $scope.component.component.approvalState !== 'N')) {

      if (media.componentMediaId) {
        $scope.hardDeleteEntity({
          loader: 'submissionLoader',
          entityName: 'Media',
          entityPath: 'media',
          entityId: media.componentMediaId,
          loadEntity: function(){
            $scope.getSubmission();
          }        
        }); 
      } else {
        var found = _.find($scope.component.media, media);
        if (found){
          console.log('found', found);
          found = _.indexOf($scope.component.media, found);
          $scope.component.media.splice(found, 1);
          console.log('MediaUploader', $scope.component.media);
        }
      }
    }; 
  }; 

  // media uploader
  $scope.mediaUploader = new FileUploader({ //
    url: 'Media.action?UploadMedia',
    alias: 'file',
    queueLimit: 50,  
    removeAfterUpload: true,
    onAfterAddingFile: function(file){
      // console.log('We loaded the loader!', file.file);
      console.dir(file.file);
      $scope.$emit('$TRIGGERLOAD', 'mediaPreviewLoader');
      if (this.queue.length >= this.queueLimit) {
        $scope.isFull = true;
      }
      if (file._file) {
        $scope.lastMediaFile = file._file.name;
      } else if (file.file) {
        $scope.lastMediaFile = file.file.name;
      }
      $scope.addMedia(file, $scope.queue, 'mediaForm', 'mediaPreviewLoader', 'caption', 'mediaTypeCode');
      file.componentId = $scope.componentId;
      file.mediaTypeCode = $scope.mediaForm.mediaTypeCode;
      file.caption = $scope.mediaForm.caption;
      file.componentMediaId = $scope.mediaForm.componentMediaId;
      $scope.resetMediaInput();
    },
    onBeforeUploadItem: function(item) {
      $scope.$emit('$TRIGGERLOAD', 'submissionLoader');

      item.formData.push({
        "componentMedia.componentId" : item.componentId
      });
      item.formData.push({
        "componentMedia.mediaTypeCode" : item.mediaTypeCode
      });
      if (item.description) {
        item.formData.push({
          "componentMedia.caption": item.caption
        });
      }
      if (item.componentMediaId) {
        item.formData.push({
          "componentMedia.componentMediaId": item.componentMediaId
        });
      }
    },
    onSuccessItem: function (item, response, status, headers) {
      $scope.$emit('$TRIGGERUNLOAD', 'submissionLoader');

      //check response for a fail ticket or a error model
      if (response.success) {
        triggerAlert('Uploaded successfully', 'saveResource', 'componentWindowDiv', 3000);          
        $scope.cancelMediaEdit();
        $scope.loadMedia();          
      } else {
        if (response.errors) {
          var uploadError = response.errors.file;
          var enityError = response.errors.componentMedia;
          var errorMessage = uploadError !== undefined ? uploadError : '  ' + enityError !== undefined ? enityError : '';
          triggerAlert('Unable to upload media. Message: <br> ' + errorMessage, 'saveMedia', 'componentWindowDiv', 6000);
        } else {
          triggerAlert('Unable to upload media. ', 'saveMedia', 'componentWindowDiv', 6000);
        }
      }
    },
    onErrorItem: function (item, response, status, headers) {
      $scope.$emit('$TRIGGERUNLOAD', 'submissionLoader');
      triggerAlert('Unable to upload media. Failure communicating with server. ', 'saveMedia', 'componentWindowDiv', 6000);        
    }      
  });     



$scope.removeFromResourceQueue = function(file){
  if (!($scope.component.component.approvalState && $scope.component.component.approvalState !== 'N')) {

    console.log('MediaUploader', $scope.resourceUploader);
    console.log('file', file);
    console.log('Queue', $scope.resourceUploader.queue);

    var found = _.find($scope.resourceUploader.queue, file.file);
    if (found){
      console.log('found', found);
      found = _.indexOf($scope.resourceUploader.queue, found);
      $scope.resourceUploader.queue.splice(found, 1);
      console.log('resourceUploader', $scope.resourceUploader);
    }
    found = _.find($scope.resourceQueue, file);
    if (found){
      console.log('found', found);
      found = _.indexOf($scope.resourceQueue, found);
      $scope.resourceQueue.splice(found, 1);
      console.log('QUEUE', $scope.resourceQueue);
    }
  }
}

  $scope.deleteResource = function(resource){//
    if (!($scope.component.component.approvalState && $scope.component.component.approvalState !== 'N')) {
      if (resource.resourceId) {
        $scope.hardDeleteEntity({
          loader: 'submissionLoader',
          entityName: 'Resource',
          entityPath: 'resources',
          entityId: resource.resourceId,
          loadEntity: function(){
            $scope.loadResources();
          }        
        }); 
      } else {
        var found = _.find($scope.component.resources, resource);
        if (found){
          console.log('found', found);
          found = _.indexOf($scope.component.resources, found);
          $scope.component.resources.splice(found, 1);
          console.log('MediaUploader', $scope.component.resources);
        }
      }
    };
  };

  // Resource uploader
  $scope.resourceUploader = new FileUploader({//
    url: 'Resource.action?UploadResource',
    alias: 'file',
    queueLimit: 50,  
    removeAfterUpload: true,
    onAfterAddingFile: function(file){
      // console.log('We loaded the loader!', file.file);
      // console.dir(file.file);
      $scope.$emit('$TRIGGERLOAD', 'resourcePreviewLoader');
      if (this.queue.length >= this.queueLimit) {
        $scope.isFull = true;
      }
      if (file._file) {
        $scope.lastResourceFile = file._file.name;
      } else if (file.file) {
        $scope.lastResourceFile = file.file.name;
      }
      $scope.addMedia(file, $scope.resourceQueue, 'resourceForm', 'resourcePreviewLoader', 'description', 'resourceType');
      file.componentId = $scope.componentId;
      file.resourceType = $scope.resourceForm.resourceType;
      file.description = $scope.resourceForm.description;
      file.resourceId = $scope.resourceForm.resourceId;
      $scope.resetResourceInput();
    },
    onBeforeUploadItem: function(item) {
      $scope.$emit('$TRIGGERLOAD', 'resourceFormLoader');

      item.formData.push({
        "componentResource.componentId" : item.componentId
      });
      item.formData.push({
        "componentResource.resourceType" : item.resourceType
      });
      item.formData.push({
        "componentResource.description" : item.description
      });
      item.formData.push({
        "componentResource.restricted" : item.restricted
      });        
      if (item.resourceId) {
        item.formData.push({
          "componentResource.resourceId" : item.resourceId
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

$scope.scrollTo = function(id, current, parent, $event, focusId) {
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
          $scope.resetToggles();
          
          var topScroll = $(document).height() - ($(window).scrollTop() + $(window).height()) ;
          var returnScroll = ($(window).scrollTop() - $('#'+id).offset().top);
          if (topScroll === 0 && returnScroll < 0 ) {
            returnScroll = offset - (returnScroll  * -1);
          } else {
            returnScroll = offset
          }
          window.scrollBy(0, -returnScroll);
          if (focusId) {
            $('#'+focusId).focus();
          }
        })
        $timeout(function(){
          $scope.resetToggles();
          $('li.active').removeClass('active');
          if (parent) {
            $('[data-target="#'+parent+'"]').addClass('active');
          }
          $('[data-target="#'+id+'"]').addClass('active');
        },100)
      })
    }) //
  };//


  $timeout(function(){
    if(!$rootScope.$$phase) {
      $scope.$apply(function(){
        $('body').scrollspy({ target: '#scrollSpy', offset: 100 });
        $scope.scrollTo('top', 'top');
      })
    }
  })

  $scope.resetToggles = function(){
    $timeout(function() {
      $('[data-toggle=\'tooltip\']').tooltip();
    }, 300);
  }
  $scope.resetToggles();

}])
.filter('makeattribute', function() {
  return function(input, attribute) {
    _.each(input, function(code){
      if (code) {
        code.requiredFlg = attribute.requiredFlg || false;
        code.hideOnSubmission = attribute.hideOnSubmission || false;
        code.attributeType = attribute.attributeType;
      }
    })
    return input;
  };
})
.filter('shownattribute', function() {
  return function(input) {
    var result = [];
    _.each(input, function(attribute){
      if (attribute && !attribute.hideOnSubmission) {
        result.push(attribute)
      }
    })
    return result;
  };
})
.filter('contactsfilter', function() {
  return function(input, key) {
    if (!input || !input.length || !key) {
      return input;
    }
    return _.reject(input, function(n){
      return n[key] === 'SUB';
    });
  };
})
.controller('AttrsInfoCtrl', ['$scope', '$uiModalInstance', 'size', 'attribute', 'notificationsFactory', '$timeout', function ($scope, $uiModalInstance, size, attribute, Factory, $timeout) {
  $scope.attribute = angular.copy(attribute);
  $scope.title = attribute.description;

  $scope.getDescription = function(code) {
    return code.description? '<p>'+code.description+'</p>': '<p style="font-style: italic;">There is no detailed description for this code.</p>';
  }

  $scope.ok = function (validity) {
    $uiModalInstance.close('success');
  }

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}])
.controller('myCtrl', function($scope) {
  $scope.options = [{
    'label': 'test 1',
    'code': 'CODE1'
  }, {
    'label': 'test 2',
    'code': 'CODE2'
  }]
})
.directive('multiselect', ['$templateCache', '$timeout', function($templateCache, $timeout) {
  return {
    restrict: 'E',
    replace: true,
    scope: {
      selected: '=',
      options: '@',
      list: '=',
      onChange: '&',
      ngDisabled: '='
    },
    template: $templateCache.get('multiselect/select.tmp.html'),
    link: function(scope, elem, attrs) {
      scope.addToSelection = function(selection){
        if (!scope.ngDisabled) {
          scope.selected = scope.selected && scope.selected.constructor === Array? scope.selected: [];
          _.contains(scope.selected, selection) || !selection? '':scope.selected.push(selection);
          scope.onChange(true);
        }
      }
      scope.removeItem = function(item){
        if (!scope.ngDisabled) {
          var index = _.find(scope.selected, {label: item.label});
          if (index) {
            index = _.indexOf(scope.selected, index);
            scope.selected.splice(index, 1);
          }
          if (scope.selected.length === 0){
            var elements = elem.find('select')[0].options;

            for(var i = 0; i < elements.length; i++){
              elements[i].selected = false;
            }
          }
        }
      }
    }
  };
}]);