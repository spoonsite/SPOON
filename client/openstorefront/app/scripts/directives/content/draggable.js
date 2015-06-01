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

app.directive('draggable', ['$document', '$timeout',  function($document, $timeout) {
  return {
    transclude: true,
    templateUrl: 'views/content/draggable.html',
    link: function(scope, element, attr) {
      var startX = 0, startY = 0, x = 0, y = 0;

      element.css({
        position: 'relative',
        cursor: 'move',
      });
      
      scope.small = false;
      scope.lock = 0;
      
      element.on('mousedown', function(event) {
        // Prevent default dragging of selected content
        x = element.position().left || x;
        y = element.position().top || y;
        event.preventDefault();
        event.preventDefault();
        startX = event.pageX - x;
        startY = event.pageY - y;
        $document.on('mousemove', mousemove);
        $document.on('mouseup', mouseup);
      });

      function toggle(){
        console.log('we toggled');
        if (scope.lock < 2){
          scope.small = !scope.small;
          if (scope.small) {
            console.log('scope.small', scope.small);
            element.css({
              minWidth: '50px',
              maxWidth: '50px',
              minHeight: '50px',
              maxHeight: '50px',
            })
          } else {
            console.log('scope.small', scope.small);
            element.css({
              maxWidth: '',
              minWidth: '300px',
              maxHeight: '',
              minHeight: '300px',
            })
          }
        }
        scope.lock = 0;
      }

      scope.toggle = function(){
        toggle()
      }

      element.on('dblclick', function(){
        toggle()
      })

      function mousemove(event) {
        scope.lock = 1;
        y = event.pageY - startY;
        x = event.pageX - startX;
        x = x < 0? 0:x;
        y = y < 0? 0:y;
        x = (x + element.width()) > element.parent().width()? element.parent().width() - element.width(): x;
        y = (y + element.height()) > element.parent().height()? element.parent().height() - element.height(): y;
        element.css({
          top: y + 'px',
          left:  x + 'px'
        });
      }

      function mouseup() {
        if (scope.lock === 1) {
          scope.lock = 2;
        } else {
          scope.lock = 0;
        }
        $document.off('mousemove', mousemove);
        $document.off('mouseup', mouseup);
      }
    }
  };
}]);