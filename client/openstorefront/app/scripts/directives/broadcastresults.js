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
          }, 0, false);
        } else {
          $timeout(function () { // You might need this timeout to be sure its run after DOM render.
            fullDetailsToggle();
          }, 0, false);
        }
      });
    }
  };
}]);
