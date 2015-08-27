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


/*
* For use of this directive, the target callback must accept a string as its first
* argument. This string will be what the typeahead works off of.
*
*
* Currently this typeahead only works with strings. We'll need to improve the 
* compare and a few other things to make it work for richer data types.
*/

app.directive('typeAhead', ['$document', '$timeout', 'business', function($document, $timeout, Business) {
  return {
    restrict: 'E',
    template:'<input type="text" class="form-control searchBar" ng-model="ngModelTemp" placeholder="Search" typeahead="{{statement}}">',
    scope:{
      ngModel: '=',
      callback: '&',
    },
    compile: function(tElem, tAttrs){
      return {
        pre: function(scope, element, attrs){
          function camel2dashed($className) {
            return $className.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase();
          }
          if (attrs['statement']) {
            scope.statement = attrs['statement']
          } else {
            scope.statement = 'key.description as key.description for key in typeahead | looseFilter:$viewValue | limitTo:8';
          }

          // transfer applicable attributes to the input.
          // these need to be watched because they're variable
          scope.$parent.$watch(attrs.ngDisabled, function(newVal){
            element.find('input').prop('disabled', newVal);
          });
          scope.$parent.$watch(attrs.ngMinlength, function(newVal){
            element.find('input').prop('minlength', newVal);
          });
          scope.$parent.$watch(attrs.ngMaxlength, function(newVal){
            element.find('input').prop('maxlength', newVal);
          });
          scope.$parent.$watch(attrs.ngRequired, function(newVal){
            element.find('input').prop('required', newVal);
          });
          scope.$parent.$watch(attrs.ngPattern, function(newVal){
            element.find('input').prop('ng-pattern', newVal);
          });
          scope.$parent.$watch(attrs.ngChange, function(newVal){
            element.find('input').prop('ng-change', newVal);
          });
          scope.$parent.$watch(attrs.ngTrim, function(newVal){
            element.find('input').prop('ng-trim', newVal);
          });

          // these just need to be moved from parent to child.
          var newVal = element.attr('id');
          element.attr('id', 'typeahead_' + newVal);
          element.find('input').attr('id', newVal);
          newVal = element.attr('name');
          element.attr('name', 'typeahead_' + newVal);
          element.find('input').attr('name', newVal);
          element.attr('placeholder', 'typeahead_' + newVal);
          element.find('input').attr('placeholder', newVal);
        }, //
        post: function(scope, iElem, iAttrs){
          scope.ngModelTemp = scope.ngModel;

          // get the typeahead
          scope.getTypeahead = function(){
            if (scope.callback && scope.ngModelTemp) {
              scope.callback.call(null).apply(null, [scope.ngModelTemp]).then(function(result){
                scope.typeahead = result;
              }, function(){
                scope.typeahead = [];
              })
            } else {
              scope.typeahead = [];
            } 
          }

          // if the selection/userinput changes, update the model.
          scope.$watch('ngModelTemp', function(newval, oldval){
            if (newval) {
              scope.getTypeahead();
            }

            // Only update the external model if things changed.
            if (newval !== scope.ngModel){
              scope.ngModel = newval
            }
          })

          // if the external model changed, make the change in our directive model
          scope.$watch('ngModel', function(newval, oldval){
            scope.ngModelTemp = newval;
          })
        }
      }
    }
  };
}]);
