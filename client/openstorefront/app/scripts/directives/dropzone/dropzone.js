/**
* An AngularJS directive for Dropzone.js, http://www.dropzonejs.com/
* 
* Usage:
* 
* <div ng-app="app" ng-controller="SomeCtrl">
*   <button dropzone="dropzoneConfig">
*     Drag and drop files here or click to upload
*   </button>
* </div>
* 
*  ...
*
*  Option
*  url --- Has to be specified on elements other than form (or when the form doesn't have an action attribute)
*  method --- Defaults to "post" and can be changed to "put" if necessary.
*  parallelUploads --- How many file uploads to process in parallel (See the Enqueuing file uploads section for more info)
*  maxFilesize --- in MB
*  paramName --- The name of the file param that gets transferred. Defaults to file. NOTE: If you have the option uploadMultiple set to true, then Dropzone will append [] to the name.
*  uploadMultiple --- Whether Dropzone should send multiple files in one request. If this it set to true, then the fallback file input element will have the multiple attribute as well. This option will also trigger additional events (like processingmultiple). See the events section for more information.
*  headers --- An object to send additional headers to the server. Eg: headers: { "My-Awesome-Header": "header value" }
*  addRemoveLinks --- This will add a link to every file preview to remove or cancel (if already uploading) the file. The dictCancelUpload, dictCancelUploadConfirmation and dictRemoveFile options are used for the wording.
*  previewsContainer --- defines where to display the file previews – if null the Dropzone element is used. Can be a plain HTMLElement or a CSS selector. The element should have the dropzone-previews class so the previews are displayed properly.
*  clickable --- If true, the dropzone element itself will be clickable, if false nothing will be clickable. Otherwise you can pass an HTML element, a CSS selector (for multiple elements) or an array of those.
*  createImageThumbnails 
*  maxThumbnailFilesize --- in MB. When the filename exceeds this limit, the thumbnail will not be generated
*  thumbnailWidth --- if null, the ratio of the image will be used to calculate it.
*  thumbnailHeight --- the same as thumbnailWidth. If both are null, images will not be resized.
*  maxFiles --- if not null defines how many files this Dropzone handles. If it exceeds, the event maxfilesexceeded will be called. The dropzone element gets the class dz-max-files-reached accordingly so you can provided visual feedback.
*  resize --- is the function that gets called to create the resize information. It gets the file as first parameter and must return an object with srcX, srcY, srcWidth and srcHeight and the same for trg*. Those values are going to be used by ctx.drawImage().
*  init --- is a function that gets called when Dropzone is initialized. You can setup event listeners inside this function.
*  acceptedMimeTypes --- Deprecated in favor of acceptedFiles
*  acceptedFiles --- The default implementation of accept checks the file's mime type or extension against this list. This is a comma separated list of mime types or file extensions. Eg.: image/*,application/pdf,.psd. If the Dropzone is clickable this option will be used as accept parameter on the hidden file input as well.
*  accept --- is a function that gets a file and a done function as parameter. If the done function is invoked without a parameter, the file will be processed. If you pass an error message it will be displayed and the file will not be uploaded. This function will not be called if the file is too big or doesn't match the mime types.
*  enqueueForUpload --- Deprecated in favor of autoProcessQueue.
*  autoProcessQueue --- When set to false you have to call myDropzone.processQueue() yourself in order to upload the dropped files. See below for more information on handling queues.
*  previewTemplate --- is a string that contains the template used for each dropped image. Change it to fulfill your needs but make sure to properly provide all elements.
*  forceFallback --- defaults to false. If true the fallback will be forced. This is very useful to test your server implementations first and make sure that everything works as expected without dropzone if you experience problems, and to test how your fallbacks will look.
*  fallback --- is a function that gets called when the browser is not supported. The default implementation shows the fallback input field and adds a text.
*  
*  
*  to translate dropzone, you can also provide these options:
*  
*  Option --- Description
*  dictDefaultMessage --- The message that gets displayed before any files are dropped. This is normally replaced by an image but defaults to "Drop files here to upload"
*  dictFallbackMessage --- If the browser is not supported, the default message will be replaced with this text. Defaults to "Your browser does not support drag'n'drop file uploads."
*  dictFallbackText --- This will be added before the file input files. If you provide a fallback element yourself, or if this option is null this will be ignored. Defaults to "Please use the fallback form below to upload your files like in the olden days."
*  dictInvalidFileType --- Shown as error message if the file doesn't match the file type.
*  dictFileTooBig --- Shown when the file is too big. and will be replaced.
*  dictResponseError --- Shown as error message if the server response was invalid. `` will be replaced with the servers status code.
*  dictCancelUpload --- If addRemoveLinks is true, the text to be used for the cancel upload link.
*  dictCancelUploadConfirmation --- If addRemoveLinks is true, the text to be used for confirmation when cancelling upload.
*  dictRemoveFile --- If addRemoveLinks is true, the text to be used to remove a file.
*  dictMaxFilesExceeded --- If maxFiles is set, this will be the error message when it's exceeded.
*
*  ...
*
* angular.module('app').controller('SomeCtrl', function ($scope) {
*   $scope.dropzoneConfig = {
*     'options': { // passed into the Dropzone constructor
*       'url': 'upload.php'
*     },
*     'eventHandlers': {
*       'sending': function (file, xhr, formData) {  // Called just before each file is sent. Gets the xhr object and the formData objects as second and third parameters, so you can modify them (for example to add a CSRF token) or add additional data.
*       },
*       'success': function (file, response) { // The file has been uploaded successfully. Gets the server response as second argument. (This event was called finished previously)
*       },
*       'maxfilesreached': function(file) {  // maxfilesreached  Called when the number of files accepted reached the maxFiles limit.
*       },
*       'maxfilesexceeded': function(file) {  // Called for each file that has been rejected because the number of files exceeds the maxFiles limit.
*       },
*       'uploadprogress': function(file) {  // Gets called periodically whenever the file upload progress changes. Gets the progress parameter as second parameter which is a percentage (0-100) and the bytesSent parameter as third which is the number of the bytes that have been sent to the server. When an upload finishes dropzone ensures that uploadprogress will be called with a percentage of 100 at least once. Warning: This function can potentially be called with the same progress multiple times.
*       },
*       'addedfile': function(file) {  // Gets called when a file is added
*       },
*       'removedfile': function(file) {  // Called whenever a file is removed from the list.
*       }
*     }
*   };
* });
*/

'use strict';

app.directive('dropzone', ['$http', '$compile', 'business', '$timeout', function ($http, $compile, Business, $timeout) {
  return {
    template: '<button class="btn btn-primary saveAll" ng-click="saveAll();">Save All <i class="fa fa-save"></i> </button><form action="" method="post" enctype="multipart/form-data" onsubmit="return false"><div class="dropzone"><div class="dz-default dz-message"><span>Drop files here to upload</span></div></div></form>',
    restrict: 'E',
    scope: {
      config: '='
    },
    link: function postLink(scope, element, attrs) {
      // the index that allows us to have an infinate number of form information 
      // objects.
      scope.$index = 0;

      // here we need to make sure we grab the typeahead function... it doesn't
      // persist.
      scope.getTypeahead = scope.$parent.getTypeahead;      
      scope.getTagTypeahead = scope.$parent.getTagTypeahead;

      // console.log('scope.config', scope.config);
      var config, dropzone;
      scope.config.options.parallelUploads = 5;
      config = scope.config;

      // we could allow the user to pass in their own event handlers, but since
      // this is a directive, we will just keep a hold of it 
      config.eventHandlers = {};

      var dataURItoBlob = function(dataURI) {
        // convert base64/URLEncoded data component to raw binary data held in a string
        var byteString;
        if (dataURI.split(',')[0].indexOf('base64') >= 0)
          byteString = atob(dataURI.split(',')[1]);
        else
          byteString = unescape(dataURI.split(',')[1]);

        // separate out the mime component
        var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]

        // write the bytes of the string to a typed array
        var ia = new Uint8Array(byteString.length);
        for (var i = 0; i < byteString.length; i++) {
          ia[i] = byteString.charCodeAt(i);
        }

        return new Blob([ia], {type:mimeString});
      }

      // now we configure the functions for each of the uploads.
      var setupModels = function(index, element) {
        var tags = element.find('[data-ngModel]')
        _.each(tags, function(tag){
          var element = $(tag);
          element.attr('ng-model', index +'.'+ element.attr('data-ngModel'));
        });
        var tags = element.find('[data-ngClick]')
        _.each(tags, function(tag){
          var element = $(tag);
          element.attr('ng-click', index +'.'+ element.attr('data-ngClick'));
        });
        var tags = element.find('[data-ngClass]')
        _.each(tags, function(tag){
          var element = $(tag);
          element.attr('ng-class', index +'.'+ element.attr('data-ngClass'));
        });
        var tags = element.find('[data-ngId]')
        _.each(tags, function(tag){
          var element = $(tag);
          element.attr('id', index + element.attr('data-ngId'));
        });
      }

      // Here we hendle adding extra functionality to each file as it is loaded
      // when a file is added, handle it.
      config.eventHandlers.addedfile = function(file) {
        var dropzone = this;
        // set up the elements with an ng-model found in the template looking
        // like this: data-ngModel=""
        setupModels('info'+scope.$index, $(file.previewElement));
        // add it to the scope
        $compile(file.previewElement)(scope);
        // when they click 'processMe', send the file to the server
        $(file.previewElement).find('.processMe').on("click", function() {
          dropzone.processFile(file);
        });
        $timeout(function() {
          //Press Enter in INPUT moves cursor to next INPUT
          $(file.previewElement).find('input').keypress(function(e){
            if ( e.which == 13 ) // Enter key = keycode 13
            {
              $(this).next().focus();  //Use whatever selector necessary to focus the 'next' input
              return false;
            }
          });
          $(file.previewElement).find('.tags').keypress(function(e){
            if ( e.which == 13 ) // Enter key = keycode 13
            {
              $(this).next().focus();  //Use whatever selector necessary to focus the 'next' input
              return false;
            }
          });
        })
        // increase the index so that we don't overwrite ourselves
        // we don't really care about overflow or underflow here, but maybe
        // we want to take care of that later? 
        scope['info'+scope.$index] = {};
        scope['info'+scope.$index].docType = 'image';
        scope.$index++;
      };

      // when a file is about to send do this with the formData
      config.eventHandlers.sending = function(file, xhr, formData) {
        var dropzone = this;
        var ngModel = $(file.previewElement).find('.modelNumber').attr('ng-model').split('.')[0];
        var upFile = {};
        if (!scope[ngModel].newName || scope[ngModel].newName === '') {
          dropzone.cancelUpload(file)
        } else {
          // console.log('scopeNgModel', scope[ngModel]);
        }
        upFile.height = file.height;
        upFile.width = file.width;
        upFile.size = file.size;
        upFile.type = file.type;
        upFile.name = file.name;
        scope[ngModel].scopeId = ngModel;
        scope[ngModel].fileInfo = upFile;
        scope[ngModel].new = true;
        console.log('STUFF', $('#'+ngModel+'image'));
        
        // var blob = dataURItoBlob($('#'+ngModel+'image').attr('src'));
        // formData.append('thumbnail', blob);
        formData.append('info', JSON.stringify(scope[ngModel]));
      };

      // when the upload succeeds, make sure to handle the response
      config.eventHandlers.success =  function (file, response) {
        if (response && response.scopeId) {
          // clean up the scope after the file is saved so that we don't have to
          // worry about the scope getting HUGE
          delete scope[response.scopeId];
        }
        console.log('file', file);
        console.log('response', response);
        // var dropzone = this;
        // dropzone.processQueue.bind(dropzone);
      }

      // when a file is done uploading do this.
      config.eventHandlers.complete = function(file) {
        var dropzone = this;
        if (file.status === Dropzone.CANCELED) {
          file.status = Dropzone.QUEUED;
        } else {
          this.removeFile(file);
          if (dropzone.autoProcessQueue) {
            return dropzone.processQueue();
          }
        }
      };

      // when a file is done uploading do this.
      config.eventHandlers.canceled = function(file) {
        console.log('file', file.status);
      };

      // here we set up the actual dropzone.
      if (config.options && config.options.previewTemplateUrl) {
        // if they've given us the location of a template go grab it 
        $http.get(config.options.previewTemplateUrl).then(function(response) {
          if(response.status == 200){
            config.options.previewTemplate = response.data;
            // clear out the extra option just in case.
            delete config.options.previewTemplateUrl
            // create a Dropzone for the element with the given options
            dropzone = new Dropzone($(element).find('div')[0], config.options);
            // bind the given event handlers
            angular.forEach(config.eventHandlers, function (handler, event) {
              dropzone.on(event, handler);
            });
            scope.saveAll = function(){
              dropzone.autoProcessQueue = true;
              dropzone.processQueue();
            }
          }
        });
        } else { //

        // create a Dropzone for the element with the given options
        dropzone = new Dropzone($(element).find('div')[0], config.options);
        
        // bind the given event handlers
        angular.forEach(config.eventHandlers, function (handler, event) {
          dropzone.on(event, handler);
        });
        scope.saveAll = function(){
          dropzone.autoProcessQueue = true;
          dropzone.processQueue();
        }

      }

      // this is necessary for the typeahead selects for tags.
      scope.onSelect = function(item, model, something) {
        if (typeof scope.searchKey === 'object' && scope.searchKey){
          Business.individual.getIndData(scope.searchKey.id).then(function(result) {
            // console.log('Typeahead Item Found: ', $scope.searchKey);
            // console.log('Individual: ', result);
          });
        } else {
          // console.log('searchKey', $scope.searchKey);
        }
      };
    }
  };
}]);
