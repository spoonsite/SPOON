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


// WARNING: This overrides the angular ng-focus directive
app.directive('ngFocus', ['$parse', '$rootScope', function($parse, $rootScope) {
  var FOCUS_CLASS = 'ng-focused';
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, element, attrs, ctrl) {
      ctrl.$focused = false;
      var fn = $parse(attrs['ngFocus']);
      element.bind('focus', function(evt) { /*jshint unused:false*/
        if (!$rootScope.$$phase){
          scope.$apply(function() {
            element.addClass(FOCUS_CLASS);
            ctrl.$focused = true;
            fn(scope, {$event:event});
          });
        } else {
          element.addClass(FOCUS_CLASS);
          ctrl.$focused = true;
          fn(scope, {$event:event});
        }
      }).bind('blur', function(evt) { /*jshint unused:false*/
        if (!$rootScope.$$phase){
          scope.$apply(function() {
            element.removeClass(FOCUS_CLASS);
            ctrl.$focused = false;
          });
        } else {
          element.removeClass(FOCUS_CLASS);
          ctrl.$focused = false;
        }
      });
    }
  };
}]);

/*['$parse', function($parse) {
  return function(scope, element, attr) {
    var fn = $parse(attr['ngFocus']);
    element.bind('focus', function(event) {
      scope.$apply(function() {
        fn(scope, {$event:event});
      });
    });
  }
}]);*/