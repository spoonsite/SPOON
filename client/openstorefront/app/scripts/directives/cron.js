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

app.directive('cron', ['$timeout', function ($timeout) {
  return {
    template: '<div></div>',
    restrict: 'E',
    scope: {
      ngModel: '=',
      callback: '&'
    },
    link: function postLink(scope, element, attrs) {
      $(function () {
        var cron = $(element).cronGen();
        cron[0].addEventListener("cronChange", function(data){
          console.log('cronChange', data);
          scope.ngModel = data.detail.cron;
          scope.$apply(function() {
            $timeout(function() {
              scope.callback();
            })
          });
        }, false);
      });
    }
  };
}]);
