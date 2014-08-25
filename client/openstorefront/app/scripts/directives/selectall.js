'use strict';

app.directive('selectall', [ function () {
  return {
    replace: true,
    restrict: 'E',
    scope: {
      checkboxes: '=',
      allselected: '=allSelected',
      allclear: '=allClear',
      toggleCallback : '='
    },
    template: '<input type="checkbox" ng-model="master" ng-change="masterChange()">',
    link: function postLink(scope, element, attrs) { /*jshint unused:false*/
      scope.masterChange = function () {
        if (scope.master) {
          angular.forEach(scope.checkboxes, function (cb, index) { /*jshint unused:false*/
            cb.checked = true;
            scope.toggleCallback();
          });
        } else {
          angular.forEach(scope.checkboxes, function (cb, index) { /*jshint unused:false*/
            cb.checked = false;
            scope.toggleCallback();
          });
        }
      };

      scope.$watch('checkboxes', function () {
        var allSet = true,
        allClear = true;
        angular.forEach(scope.checkboxes, function (cb, index) { /*jshint unused:false*/
          if (cb.checked) {
            allClear = false;
          } else {
            allSet = false;
          }
        });

        if (scope.allselected !== undefined) {
          scope.allselected = allSet;
        }
        if (scope.allclear !== undefined) {
          scope.allclear = allClear;
        }

        element.prop('indeterminate', false);
        if (allSet) {
          scope.master = true;
        } else if (allClear) {
          scope.master = false;
        } else {
          scope.master = false;
          element.prop('indeterminate', true);
        }

      }, true);
    }
  };
}]);
