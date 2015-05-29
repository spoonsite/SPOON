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

app.directive('photo', ['$timeout', '$parse', '$sce', function($timeout, $parse, $sce) {
  // id needs to be unique across all implementations of the directive.
  var uniqueId= 0;
  return {
    restrict: 'E',
    scope:{
      files: '=',
      callback: '&',
      init: '='
    },
    templateUrl: 'views/content/photo.html',
    link: function postlink(scope, element, attrs) {
      scope.fx = attrs.fx;
      scope.doCallback = function(data){
        if (scope.callback){
          scope.callback(data);
        }
      }
      var item = 'item' + uniqueId++;
      element.find('#prev').attr('id', 'prev'+item);
      element.find('#next').attr('id', 'next'+item);

      $timeout(function(){

        var carousel = element.find('.carousel');
        var options = {
          slides:'> div.item',
          fx: attrs['fx'],

          timeout: 0,
          caption:'.caption',
          next:"#next"+item,
          prev:"#prev"+item,
          allowWrap: (attrs['wrap'] === 'true')? true: false,
          slideCount: scope.files.length || 0,
          _nextBoundry: scope.files.length || 0
        }
        if (attrs.fx === 'center'){
          options.fx = 'fade';
          options.centerHorz = true;
          options.centervert = true;
          scope.fullClass = true;
        }

        carousel.cycle(options);

        $timeout(function(){
          carousel.cycle('stop');
          if (scope.init && !isNaN(parseInt(scope.init))){
            console.log('scope.init', scope.init);
            carousel.cycle('goto', parseInt(scope.init));
          }
        })
      })

      scope.getWinHeight = function(video){
        var result = $(window).height() - 100;
        if (video) {
          return result - 50;
        }
        return result;
      }

      scope.getMimeTypeClass = function(type){
        return utils.getMimeTypeClass(type);
      }
      scope.isImage = function(file){
        if (utils.getBasicFileType(file) === 'image'){
          return true;
        }
        return false;
      }
      scope.isVideo = function(file){
        if (utils.getBasicFileType(file) === 'video'){
          return true;
        }
        return false;
      }
      scope.isAudio = function(file){
        if (utils.getBasicFileType(file) === 'audio'){
          return true;
        }
        return false;
      }
      scope.isOther = function(file){
        if (!scope.isAudio(file) && !scope.isVideo(file) && !scope.isImage(file))
          return true;
        return false;
      }
      _.each(scope.files, function(file){
        if (scope.isVideo(file)){
          file.srcs = [
            //
            {
              src:$sce.trustAsResourceUrl(file.link).$$unwrapTrustedValue(),
              type: file.mimeType
            }
          //
          ];
          console.log('file', file);
          
        }
      })
    }
  };
}]);
