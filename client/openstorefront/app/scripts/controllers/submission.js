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

    $scope.config = {
      sources: [
      // {src: $sce.trustAsResourceUrl("http://familyhistorydatabase.org/tempfiles/Never Be Alone.mp3"), type: "audio/mp3"},
      {src: $sce.trustAsResourceUrl("http://static.videogular.com/assets/videos/videogular.mp4"), type: "video/mp4"},
      {src: $sce.trustAsResourceUrl("http://static.videogular.com/assets/videos/videogular.webm"), type: "video/webm"},
      {src: $sce.trustAsResourceUrl("http://static.videogular.com/assets/videos/videogular.ogg"), type: "video/ogg"}
      ],
      tracks: [
      {
        src: "http://www.videogular.com/assets/subs/pale-blue-dot.vtt",
        kind: "subtitles",
        srclang: "en",
        label: "English",
        default: ""
      }
      ],
      theme: "bower_components/bower_components/videogular-themes-default/videogular.css"
      // plugins: {
      //   poster: "http://www.videogular.com/assets/images/videogular.png"
      // }
    };


    $scope.test = 'This is a test';

    $scope.name;
    $scope.email;
    $scope.organization;
    $scope.current = 'top';


    $scope.component = {};
    $scope.component.attributes = [];
    
    $scope.component.metadata = [];
    $scope.metadataLength = 0;
    $scope.component.metadata[$scope.metadataLength] = {};
    
    $scope.component.tags = [];
    $scope.tagsLength = 0;
    $scope.component.tags[$scope.tagsLength] = {};

    $scope.contactForm = {};
    $scope.component.contacts = [];

    $scope.mediaForm = {};
    $scope.isFull = false;

    $scope.details = {};

    $scope.getStarted = function(){
      return $scope.name && $scope.email && $scope.organization;
    }

    $scope.checkAttributes = function(){
    // console.log('Compact list', _.compact($scope.component.attributes));
    // we need to compact the attributes list because it may have unused indexes.
    var list = angular.copy(_.compact($scope.component.attributes));
    var requiredAttributes = _.filter(list, {requiredFlg: true});
    if (requiredAttributes.length !== $scope.requiredAttributes.length) {
      return false;
    }

    // This is how we'll weed out the attributes we need for the submission
    /*
    var realAttributes = _.compact($scope.component.attributes);
    var attributes = [];
    _.each(realAttributes, function(attr){
      if (attr.constructor === Array){
        _.each(attr, function(item){
          attributes.push(item);
        })
      } else {
        attributes.push(attr);
      }
    })
    console.log('attributes', attributes);
    */
    return true;
  }

  $scope.getComponent = function(){
    return JSON.stringify($scope.component, null, 4);
  }

  $scope.removeMetadata = function(index){
    $scope.component.metadata.splice(index, 1);
    $scope.metadataLength--;
    $scope.component.metadata[$scope.metadataLength] = {};
  }
  $scope.addMetadata = function(){
    if ($scope.component.metadata[$scope.metadataLength].value && $scope.component.metadata[$scope.metadataLength].label) {
      $scope.metadataLength++;
      $scope.component.metadata[$scope.metadataLength] = {};
      $('#metadataLabel').focus();
    }
  }

  $scope.removeTag = function(index){
    $scope.component.tags.splice(index, 1);
    $scope.tagsLength--;
    $scope.component.tags[$scope.tagsLength] = {};
  }
  $scope.addTag = function(){
    if ( $scope.component.tags[$scope.tagsLength].text ) {
      $scope.tagsLength++;
      $scope.component.tags[$scope.tagsLength] = {};
      $('#tagLabel').focus();
    }
  }

  $scope.removeContact = function(index){
    $scope.component.contacts.splice(index, 1);
  }
  $scope.addContact = function(){
    console.log('$scope.contactForm', $scope.contactForm);
    
    if ( $scope.contactForm ) {
      $scope.component.contacts.push($scope.contactForm);
      $scope.contactForm = {};
      $('#contactType').focus();
    }
  }
  $scope.getContactType = function(type){
    var found = _.find($scope.contactTypes, {'code': type});
    return found? found.description: type;
  }

  $scope.vitalsCheck = function(log){
    return $scope.getStarted() && $scope.component && $scope.component.name && $scope.component.description && $scope.component.organization && $scope.checkAttributes();
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

  $scope.getAttributes = function (override) { 
    Business.getFilters(override, false).then(function (result) {
      $scope.allAttributes = result ? angular.copy(result) : [];
      $scope.requiredAttributes = _.filter($scope.allAttributes, {requiredFlg: true});
      console.log('required', $scope.requiredAttributes);
      _.remove($scope.requiredAttributes, function(attribute) {
        return attribute.attributeType === 'DI2ELEVEL' || attribute.attributeType === 'DI2EINTENT' || attribute.attributeType === 'DI2ESTATE';
      });
      $scope.attributes = _.filter($scope.allAttributes, {requiredFlg: false});
    });
  }; 
  $scope.getAttributes();
  
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

$scope.srcList = [];
$scope.queue = [];
  $scope.addMedia = function (file) { //
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
      //   loader: 'mediaFormLoader',
      //   entityName: 'media',
      //   entity: $scope.mediaForm,
      //   entityId: $scope.mediaForm.componentMediaId,
      //   formName: 'mediaForm',
      //   loadEntity: function () {
      //     $scope.loadMedia();
      //   }
      // });
    // } else {
      console.log('$scope.mediaUploader', $scope.mediaUploader);
      if (file._file){
        $scope.readFile(file._file, function(result){
          $scope.queue.push(result);
          if(!$scope.$$phase) {
            $scope.$apply();
          }
        });
      }
      // $scope.mediaUploader.uploadAll();
    // }
  };
  $scope.resetMediaInput = function(){
    $('#mediaUploadInput').val(null);
  }

  $scope.readFile = function(file, callback){
    var reader;
    if (file.type.match('image.*')) {
      var reader = new FileReader();
      alert('e.target.result');
      // Closure to capture the file information.
      reader.onload = (function(theFile) {
        return function(e) {
          // Render thumbnail.
          callback(
           ['<img class="thumb" src="', e.target.result,
           '" title="', escape(theFile.name), '" width="230"    height="270"/>'].join('')
           )
        };
      })(file);
    }else if (file.type.match('audio.*')){
      var reader = new FileReader();

      // Closure to capture the file information.
      reader.onload = (function(theFile) {
        return function(e) {
          // Render thumbnail.

          callback(
            ['<audio controls><source src="', e.target.result,'   "type="audio/ogg"><source src="', e.target.result,' "type="audio/mpeg"></audio>'].join('')
            )
        };
      })(file);
    }else if (file.type.match('video.*')){
      var URL = window.URL || window.webkitURL;
      var type = file.type;

      var videoNode = document.createElement('video');

      var canPlay = videoNode.canPlayType(type);

      canPlay = (canPlay === '' ? 'no' : canPlay);

      var message = 'Can play type "' + type + '": ' + canPlay;

      var isError = canPlay === 'no';

      console.log(message, isError);

      if (isError) {
        return;
      }

      var fileURL = URL.createObjectURL(file);
      console.log('fileURL', fileURL);
      
      if (!URL) {
        callback('<span>Your browser is not <a href="http://caniuse.com/bloburls">supported</a>!</span>')
      }   
      var srcs = [];
      console.log('$sce.trustAsResourceUrl', $sce.trustAsResourceUrl(fileURL));
      
      srcs.push({src: $sce.trustAsResourceUrl(fileURL).$$unwrapTrustedValue(), type: 'video/mp4'});             
      srcs.push({src: $sce.trustAsResourceUrl(fileURL).$$unwrapTrustedValue(), type: 'video/webm'});             
      srcs.push({src: $sce.trustAsResourceUrl(fileURL).$$unwrapTrustedValue(), type: 'video/ogg'});             
      callback(
        ['<videogular> <vg-media vg-src=\''+JSON.stringify(srcs)+'\'></vg-media>  <vg-controls vg-autohide="true" vg-autohide-time="2000"> <vg-play-pause-button></vg-play-pause-button> <vg-time-display>{{ currentTime | date:\'mm:ss\' }} </vg-time-display> <vg-scrub-bar><vg-scrub-bar-current-time></vg-scrub-bar-current-time> </vg-scrub-bar> <vg-time-display>{{ timeLeft | date:\'mm:ss\' }}</vg-time-display> <vg-volume> <vg-mute-button></vg-mute-button><vg-volume-bar></vg-volume-bar></vg-volume> <vg-fullscreen-button></vg-fullscreen-button> </vg-controls> </videogular>'].join('')
        )
    }
  }

  $scope.mediaUploader = new FileUploader({ //
    url: 'Media.action?UploadMedia',
    alias: 'file',
    queueLimit: 50,  
    removeAfterUpload: true,
    onAfterAddingFile: function(file){
      if (this.queue.length >= this.queueLimit) {
        $scope.isFull = true;
      }
      $scope.addMedia(file);
    },
    onBeforeUploadItem: function(item) {
      $scope.$emit('$TRIGGERLOAD', 'mediaFormLoader');

      item.formData.push({
        "componentMedia.componentId" : $scope.componentForm.componentId
      });
      item.formData.push({
        "componentMedia.mediaTypeCode" : $scope.mediaForm.mediaTypeCode
      });
      if ($scope.mediaForm.caption) {
        item.formData.push({
          "componentMedia.caption": $scope.mediaForm.caption
        });
      }
      if ($scope.mediaForm.componentMediaId) {
        item.formData.push({
          "componentMedia.componentMediaId": $scope.mediaForm.componentMediaId
        });
      }
    },
    onSuccessItem: function (item, response, status, headers) {
      $scope.$emit('$TRIGGERUNLOAD', 'mediaFormLoader');

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
      $scope.$emit('$TRIGGERUNLOAD', 'mediaFormLoader');
      triggerAlert('Unable to upload media. Failure communicating with server. ', 'saveMedia', 'componentWindowDiv', 6000);        
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

}])
.filter('makeattribute', function() {
  return function(input, attribute) {
    _.each(input, function(code){
      if (code) {
        code.requiredFlg = attribute.requiredFlg || false;
        code.attributeType = attribute.attributeType;
      }
    })
    return input;
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
      onChange: '&'
    },
    template: $templateCache.get('multiselect/select.tmp.html'),
    link: function(scope, elem, attrs) {
      scope.addToSelection = function(selection){
        console.log('selection', selection);
        
        scope.selected = scope.selected && scope.selected.constructor === Array? scope.selected: [];
        _.contains(scope.selected, selection) || !selection? '':scope.selected.push(selection);
        scope.onChange(true);
      }
      scope.removeItem = function(item){
        console.log('item', item);
        
        var index = _.find(scope.selected, {label: item.label});
        console.log('index', index);
        if (index) {
          index = _.indexOf(scope.selected, index);
          console.log('index', index);
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
  };
}]);