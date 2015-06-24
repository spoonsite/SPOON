/* ng-infinite-scroll - v1.0.0 - 2013-02-23 */
/* modified for use within the storefront project */

var mod;

mod = angular.module('infinite-scroll', []);

mod.directive('infiniteScroll', [
  '$rootScope', '$window', '$timeout', function($rootScope, $window, $timeout) {
    return {
      link: function(scope, elem, attrs) {
        var checkWhenEnabled, handler, scrollDistance, scrollEnabled;
        var windowElement = attrs.windowElement;
        if (windowElement) {
          if ($('#'+windowElement).length) { 
            $window =  $($('#'+windowElement)[0]);
          } else if ($('.'+windowElement).length) {
            $window = $($('.'+windowElement)[0]);
          } else {
            $window = angular.element($window);
          }
        }
        var top;
        $timeout(function(){
          top = elem.offset().top;
          // console.log('top', top);
        })
        scrollDistance = 0;
        if (attrs.infiniteScrollDistance != null) {
          scope.$watch(attrs.infiniteScrollDistance, function(value) {
            return scrollDistance = parseFloat(value, 10);
          });
        }
        scrollEnabled = true;
        checkWhenEnabled = false;
        if (attrs.infiniteScrollDisabled != null) {
          scope.$watch(attrs.infiniteScrollDisabled, function(value) {
            scrollEnabled = !value;
            if (scrollEnabled && checkWhenEnabled) {
              checkWhenEnabled = false;
              return handler();
            }
          });
        }
        handler = function() {
          var elementBottom, remaining, shouldScroll, windowBottom;
          // windowBottom = $window.height() + $window.scrollTop(); // the distance from the top to the bottom of the window
          // elementBottom = elem.offset().top + elem.height() - top; // the distance from the top of the element to the bottom of the element
          // remaining = elementBottom - windowBottom; // subtract the distances to see what remains of the element.
          // console.log('remainingHeight', elem.height() - $window.height() + (elem.offset().top));
          // console.log('scrollDistance', scrollDistance);
          // console.log('scrollDistance', $window.height() * scrollDistance);
          shouldScroll = (elem.height() - $window.height() + (elem.offset().top)) <= ($window.height() * scrollDistance); // if the scrolldistance times the height of the window (431px) is greater than the remaining element, add more...
          // console.log('elem.offset().top', elem.offset().top - top);
          // console.log('elem.height', elem.height());
          // console.log('$window.height()', $window.height());
          // console.log('windowBottom', windowBottom);
          // console.log('elementBottom', elementBottom);
          // console.log('remaining', remaining);
          // console.log('shouldScroll', shouldScroll);
          // console.log('scrollDistance', scrollDistance);
          // console.log('$window.height()', $window.height());

          if (shouldScroll && scrollEnabled) {
            if ($rootScope.$$phase) {
              return scope.$eval(attrs.infiniteScroll);
            } else {
              return scope.$apply(attrs.infiniteScroll);
            }
          } else if (shouldScroll) {
            return checkWhenEnabled = true;
          }
        };
        $window.on('scroll', handler);
        scope.$on('$destroy', function() {
          return $window.off('scroll', handler);
        });
        return $timeout((function() {
          if (attrs.infiniteScrollImmediateCheck) {
            if (scope.$eval(attrs.infiniteScrollImmediateCheck)) {
              return handler();
            }
          } else {
            return handler();
          }
        }), 0);
      }
    };
  }
  ]);
