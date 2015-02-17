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
* This directive is used to inject html into an element through binding
* just like an 'ng-bind-html'. The only difference is that it will compile
* the contents so that it works with it's parent scope.
*
* The scope variable must be sent through '$sce' before being compiled here:
* 
* Scope Controller:
* ~~~~~
* $scope.scopevariable = $sce.trustAsHtml("<button>Hello World</button>");
* 
* HTML:
* ~~~~~
* <div dynamichtml='scopevariable'></div>
***************************************************************/
app.directive('dynamichtml', ['$compile', function ($compile) {
  return {
    restrict: 'A',
    replace: true,
    link: function (scope, ele, attrs) {
      scope.$watch(attrs.dynamichtml, function(html) {
        if (html) {
          ele.html(html.toString());
          $compile(ele.contents())(scope);
        }
      });
    }
  };
}]);
