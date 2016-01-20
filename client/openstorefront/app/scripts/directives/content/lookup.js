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

app.directive('lookup', ['$document', '$timeout', 'business', function($document, $timeout, Business) {
  return {
    restrict: 'E',
    templateUrl:'views/content/lookup.html',
    scope:{
      ngModel: '=',
      callback: '&',
      title: '@'
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

          if (scope.callback && typeof scope.callback === 'function') {
            scope.func = [scope.callback.call(null), ['', true]]; 
          } else {
            scope.func = null;
          }
          scope.typeahead = [];
          scope.search = {code: '', description: ''};
          scope.selectedRowsLookup = [];
          scope.pagination = {
            features: {'dates': false, 'max': false, 'activeState': true, 'approvalState': true, 'componentType': true, 'maxPP': false, 'clear': false, 'pages': false},
            control: {
              onRefresh: function(){}
            }
          };
          if (scope.pagination.control) {
            scope.pagination.control.onRefresh = function(){
              scope.selectedRowsLookup = [];
              iElem.find('[childselect]').find('input[type=checkbox]').prop('checked', false);
            }
          }
          scope.$watch('selectedRowsLookup', function(newval, oldval){
            if (!angular.equals(scope.ngModel, newval)){
              scope.ngModel = newval;
            }
          }, true)

          scope.$watch('ngModel', function(newval, oldval){
            if (!angular.equals(scope.selectedRowsLookup, newval)){
              scope.selectedRowsLookup = newval;
            }
          }, true)

          scope.clearSelection = function(){
            scope.selectedRowsLookup = [];
            iElem.find('[childselect]').find('input[type=checkbox]').prop('checked', false);
          }
          scope.getCallback = function(){
            return scope.func;
          }
          scope.getThing = function(id){
            var found = _.find(scope.typeahead, {'code': id});
            if (found){
              return found.description;
            } else {
              return 'Item ' + id;
            }
          }
        }
      }
    }
  };
}]);
