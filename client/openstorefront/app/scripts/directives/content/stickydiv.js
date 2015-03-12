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

app.directive('stickydiv', ['$timeout', function($timeout) {
  // id needs to be unique across all implementations of the directive
  var stickyDivId = 1;
  return {
    restrict: 'E',
    scope:{
      fixedOffsetTop: '@',
      fixedOffsetLeft: '@',
      elementId:'@'
    },
    transclude: true,
    templateUrl: 'views/content/stickydiv.html',
    link: function postlink(scope, element, attrs) {
      scope.elementId = scope.elementId || 'stickydiv' + (stickyDivId++);
      scope.fillerId = scope.elementId? scope.elementId +'-filler' : 'stickydiv' + (stickyDivId++) +'-filler';
      scope.currentTop = element.position().top;
      scope.currentLeft = element.position().left;
      scope.notElementHeight = 0;
      $timeout(function(){
        scope.notElementHeight = element.find('#'+scope.elementId).outerHeight();
      })

      if (scope.fixedOffsetTop && isNaN(scope.fixedOffsetTop) && $(scope.fixedOffsetTop).length > 0){
        scope.topOffset = $(scope.fixedOffsetTop).outerHeight();
        scope.topOffsetScroll = scope.currentTop - scope.topOffset;
      } else if (!isNaN(scope.fixedOffsetTop)){
        scope.topOffset = scope.fixedOffsetTop;
        scope.topOffsetScroll = scope.currentTop - scope.topOffset;
      } else if (!scope.fixedOffsetTop){
        scope.topOffset = scope.currentTop;
        scope.topOffsetScroll = scope.currentTop;
      } else {
        scope.topOffset = 0;
        scope.topOffsetScroll = scope.currentTop;
      }

      if (scope.fixedOffsetLeft && isNaN(scope.fixedOffsetLeft) && $(scope.fixedOffsetLeft).length > 0){
        scope.leftOffset = $(scope.fixedOffsetLeft).outerHeight();
        scope.leftOffsetScroll = scope.currentLeft - scope.leftOffset;
      } else if (!isNaN(scope.fixedOffsetLeft)){
        scope.leftOffset = scope.fixedOffsetLeft;
        scope.leftOffsetScroll = scope.currentLeft - scope.leftOffset;
      } else if (!scope.fixedOffsetLeft){
        scope.leftOffset = scope.currentLeft;
        scope.leftOffsetScroll = scope.currentLeft;
      } else {
        scope.leftOffset = 0;
        scope.leftOffsetScroll = scope.currentLeft;
      }


      scope.setPositionValues = function () {
        $('.popover').hide();
        var winScrollTop = $(window).scrollTop();
        if (winScrollTop < 0 || winScrollTop + $(window).height() > $(document).height() || winScrollTop < scope.topOffsetScroll) {
          element.find('#'+scope.elementId).css({
            'position': 'static',
            'background': 'transparent',
            'width': 'auto'
          });
          element.find('#'+scope.fillerId).css({
            'min-height': '0px'
          })
          return;
        }
        element.find('#'+scope.elementId).css({
          'position': 'fixed',
          'background': 'url(/openstorefront/images/squared_metal.png)',
          'top': scope.topOffset,
          'width': 'calc(100% - '+ scope.currentLeft+'px)',
          'z-index': 1010
        });
        element.find('#'+scope.fillerId).css({
          'min-height': scope.notElementHeight
        })
      };

      scope.bind = function () {
        $(window).on('scroll', scope.setPositionValues);
        $(window).on('resize', scope.setPositionValues);
      };
      scope.bind();
    }
  };
}]);
