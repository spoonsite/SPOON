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

/*global setupResults, fullDetailsToggle*/

app.directive('broadcastResults', ['$timeout', function ($timeout) {
  return {
    template: '<div></div>',
    restrict: 'AE',
    link: function postLink(scope, element, attrs) { /*jshint unused:false*/
      scope.$on('dataloaded', function (event, input) { /*jshint unused:false*/
        if (input) {
          $timeout(function () { // You might need this timeout to be sure its run after DOM render.
            setupResults();
          });
        } else {
          $timeout(function () { // You might need this timeout to be sure its run after DOM render.
            fullDetailsToggle();
          });
        }
      });
    }
  };
}]);
