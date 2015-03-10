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

app.directive('panel', ['$timeout', function($timeout) {
  // id needs to be unique across all implementations of the directive
  var nextId2 = 1;
  return {
    restrict: 'E',
    scope:{
      title: '@',
      openState: '@'
    },
    transclude: true,
    templateUrl: 'views/content/panel.html',
    link: function postlink(scope, element, attrs) {
      console.log('scope.open', scope.openState);
      console.log('attrs', attrs);
      
      scope.open = (scope.openState !== 'false' && scope.openState !== '0' && scope.openState !== '' && scope.openState !== undefined && scope.openState !== null)? true: false;
      console.log('scope.open', scope.open);
      
      // set up the uniqueID for the panel
      scope.panelId = nextId2++;
      $timeout(function(){
        console.log('scope.open', scope.open);

        if (scope.open) {
          $('#collapseThis'+scope.panelId).collapse('show');
        }
        $('#collapseThis'+scope.panelId).on('hidden.bs.collapse', function () {
          scope.open = false;
          scope.$apply();
        })
        $('#collapseThis'+scope.panelId).on('shown.bs.collapse', function () {
          scope.open = true;
          scope.$apply();
        })
      })
    }
  };
}]);
