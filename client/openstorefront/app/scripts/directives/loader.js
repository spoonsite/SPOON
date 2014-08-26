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


/***************************************************************
* Usage:: <loader trigger="triggerId"></loader>
*
* ~~~
*
* $scope.$emit('$TRIGGERLOAD', 'triggerId');
*
* ~load stuff~
*
* $scope.$emit('$TRIGGERUNLOAD', 'triggerId');
***************************************************************/
app.directive('loader', function () {
  return {
    template: '<div class="loader-holder modal-backdrop" ng-show="loading"><div class="loader"><!--[if lt IE 10]><span>...Loading...</span><![endif]--></div></div>',
    restrict: 'E',
    scope: {
      trigger: '@trigger'
    },
    link: function postLink(scope, element, attrs) { /*jshint unused:false*/
      scope.loading = false;
      scope.$on('$LOAD', function(event, value) {
        if (value === scope.trigger) {
          scope.loading = true;
        }
      });
      scope.$on('$UNLOAD', function(event, value){
        if (value === scope.trigger) {
          scope.loading = false;
        }
      });
    }
  };
});
