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

app.directive('draggableCollapse', ['$timeout', function($timeout) {
  // id needs to be unique across all implementations of the directive
  var draggableCollapseCount = 1;
  return {
    restrict: 'A',
    scope:{
      title: '@',
      openState: '@',
      classList: '@',
      isDraggable: '@',
      isCloseable: '@',
    },
    transclude: true,
    templateUrl: 'views/content/collapse.html',
    link: function postlink(scope, element, attrs) {
      
      scope.dragTarget = (scope.isDraggable !=='true' && scope.isDraggable !== 'false')? scope.isDraggable || 'draggableTarget':'draggableTarget';
      scope.closeTarget = (scope.isCloseable !=='true' && scope.isCloseable !== 'false')? scope.isCloseable || 'closeTarget':'closeTarget';

      scope.open = (scope.openState !== 'false' && scope.openState !== '0' && scope.openState !== '' && scope.openState !== undefined && scope.openState !== null)? true: false;
      
      // set up the uniqueID for the panel
      scope.panelId = draggableCollapseCount++;
      $timeout(function(){

        if (scope.open) {
          $('#collapseThat'+scope.panelId).collapse('show');
        }
        $('#collapseThat'+scope.panelId).on('hidden.bs.collapse', function () {
          scope.open = false;
          scope.$apply();
        })
        $('#collapseThat'+scope.panelId).on('shown.bs.collapse', function () {
          scope.open = true;
          scope.$apply();
        })
      })
    }
  };
}]);
