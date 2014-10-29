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
      ngModel: '='
    },
    link: function postLink(scope, element, attrs) {
      var flag = false;

      if (!scope.ngModel) {
        scope.ngModel = '0 0 * * *';
      }

      var cleanNgModel = function(value) {
        var check = value.trim().split(' ');
        if (check.length === 6) {
          check.shift();
          return check.join(' ');
        } else if (check.length === 5) {
          return check.join(' ');
        } else {
          return '0 0 * * *';
        }
      }

      $timeout(function() {
        var c = $(element).cron({
          initial: cleanNgModel(scope.ngModel), // Initial value. default = "* * * * *"
          onChange: function() {
            // console.log('cron value', $(this).cron('value'));
            scope.ngModel = $(this).cron('value');
            flag = true;
          }
        });
        scope.$watch('ngModel', function(value){
          if (value && !flag){
            $(element).cron('value', cleanNgModel(value));
          }
          flag = false;
        })
      })

    }
  };
}]);
