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
* A sneaky solution to the select all business.
*
* USE: add this attribute to the table tag where the ngModelObject is your
*      select items array:
*          selectall="ngModelObject"
*
*      add this attribute to the table header that will be the master control:
*          masterselect
*
*      add this to each of the children items that will be selectable where
*      ngModelItem will be the value they add to the selectedarray:
*          childselect="ngModelItem"
*
*/

app.directive('selectall', ['$timeout','$parse', function($timeout, $parse) {
  // id needs to be unique across all implementations of the directive
  var nextId2 = 1;
  return {
    scope: {
      selectall: '='
    },
    restrict: 'A',
    link: function postlink(scope, element, attrs) {
      var master;
      var children;
      scope.count = 0;
      scope.internalChange = false;
      scope.selectall = scope.selectall || [];

      function resolve(cur, ns) {
        var undef;
        ns = ns.split('.');
        while (cur && ns[0])
          cur = cur[ns.shift()] || undef;
        return cur;
      }

      var initTimeout = setTimeout(function(scope){
        // console.log('we hit the timeout');
        
        scope.init();
      }, 1000, scope)

      scope.$on('$RENDERAFTERREPEAT', function(event, data){
        if (data === attrs['selectall']) {
          clearTimeout(initTimeout);
          scope.init();
        }
      })
      
      scope.init = function(){
        master = element.find('*[masterselect]');
        children = element.find('*[childselect]');
        scope.reset();
        scope.render();
        scope.$watch('selectall', function(newval, oldval){
          if (!scope.internalChange) {
            scope.reset(newval, oldval)
          } else {
            scope.internalChange = false;
          }
        }, true)
      }

      scope.reset = function(newval, oldval){
        newval = newval || scope.selectall;
        $timeout(function(){
          scope.count = 0;
          children.each(function(e){
            var child = $(this);
            var found = child.find('input[type=checkbox]');
            if (!found.length){
              found = child.append('<input type="checkbox"/>').find('input[type=checkbox]');
            }
            if (_.contains(newval, resolve(angular.element(child).scope(), child.attr('childselect')))){
              found.prop('checked', true);
              scope.count++;
            }
          })
        })
      }

      scope.$watch('count', function(newval, oldval){
        // console.log('count', newval);
        // console.log('children', children.length);
        
        if (newval) {
          if (newval !== 0 && newval !== children.length) {
            master.find('input[type=checkbox]').prop("indeterminate", true);
          } else {
            master.find('input[type=checkbox]').prop("indeterminate", false);
            master.find('input[type=checkbox]').prop("checked", true);
          }
        } else if (master) {
          master.find('input[type=checkbox]').prop("indeterminate", false);
          master.find('input[type=checkbox]').prop("checked", false);
        }
      })

      scope.render = function(){
        var found = master.find('input[type=checkbox]');
        if (!found.length){
          var checkbox = master.append('<input type="checkbox"/>').find('input[type=checkbox]');
          checkbox.change(function(){
            this.checked ? 
            (function(that){
              that.each(function(e){
                var thing = $(this);
                var scopeItem = resolve(angular.element(thing).scope(), thing.attr('childselect'));
                if (scopeItem) {
                  thing.find('input[type=checkbox]').prop('checked', true);
                  var found = _.contains(scope.selectall, scopeItem);
                  if (!found) {
                    scope.internalChange = true;
                    scope.selectall.push(scopeItem);
                  }
                }
              })
              scope.count = children.length;
            })(children) 
            : 
            (function(that){
              that.each(function(e){
                var thing = $(this);
                var scopeItem = resolve(angular.element(thing).scope(), thing.attr('childselect'));
                if (scopeItem) {
                  thing.find('input[type=checkbox]').prop('checked', false);
                  var found = _.indexOf(scope.selectall, scopeItem);
                  if (found) {
                    scope.internalChange = true;
                    scope.selectall.splice(found, 1);
                  }
                }
              })
            })(children);
          });
        }
        children.each(function(e){
          var child = $(this);
          found = child.find('input[type=checkbox]');
          if (!found.length){
            var checkbox = child.append('<input type="checkbox"/>').find('input[type=checkbox]');
            checkbox.change(function(){
              this.checked ? 
              (function(that){
                // console.log('that', resolve(angular.element(that).scope(), that.attr('childselect')));
                scope.internalChange = true;
                scope.selectall.push(resolve(angular.element(that).scope(), that.attr('childselect')));
                scope.count++;
              })(child) 
              : 
              (function(that){
                var thing = resolve(angular.element(that).scope(), that.attr('childselect'));
                // console.log('thing', thing, scope.selectall);
                
                var found = _.contains(scope.selectall, thing);
                if (found) {
                  // console.log('it was found now we remove it');
                  var index = _.indexOf(scope.selectall, thing);
                  scope.internalChange = true;
                  scope.selectall.splice(index, 1);
                  scope.count--;
                }
              })(child);
            });
          } 
        })
      } //
    }
  };
}]);

app.directive('renderAfterRepeat', function ($timeout) {
  return {
    restrict: 'A',
    link: function (scope, element, attr) {
      if (scope.$last === true) {
        $timeout(function () {
          scope.$emit('$TRIGGEREVENT', '$RENDERAFTERREPEAT', attr['renderAfterRepeat']);
        });
      }
    }
  }
});
