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

app.directive('draggable', ['$document', '$timeout', '$draggableStack',  function($document, $timeout, $draggableStack) {
  return {
    restrict: 'EA',
    scope: {
      index: '@',
      animate: '='
    },
    replace: true,
    transclude: true,
    templateUrl: function(tElement, tAttrs) {
      return tAttrs.templateUrl || 'views/content/draggable.html';
    },
    link: function(scope, element, attr) {
      var startX = 0, startY = 0, x = 0, y = 0;

      
      scope.small = false;
      scope.lock = 0;
      scope.targetFound = false;
      
      var draggableElement = element;
      var closeElement = element;
      $timeout(function(){
        if (attr.movetarget){
          draggableElement = element.find('#' + attr.movetarget);
          if (!draggableElement.length) {
            draggableElement = element.find('.' + attr.movetarget);
            if (!draggableElement.length) {
              draggableElement = element.find(attr.movetarget);
              if (!draggableElement.length && $('#draggableTarget').length) {
                draggableElement = $('#draggableTarget');
              } else {
                draggableElement = element;
              }
            } 
          }
        } else if ($('#draggableTarget').length) {
          draggableElement = $('#draggableTarget');
        }
        if (draggableElement.length) {
          draggableElement = $(draggableElement[0]);
        }

        draggableElement.css({
          position: 'relative',
          cursor: 'move',
        });

        draggableElement.on('mousedown', function(event) {
          // Prevent default dragging of selected content
          x = element.position().left || x;
          y = element.position().top || y;
          event.preventDefault();
          event.preventDefault();
          startX = event.pageX - x;
          startY = event.pageY - y;
          $document.on('mousemove', mousemove);
          $document.on('mouseup', mouseup);
        });
        
        closeElement = draggableElement;

        if (attr.closetarget){
          closeElement = element.find('#' + attr.closetarget);
          if (!closeElement.length) {
            closeElement = element.find('.' + attr.closetarget);
            if (!closeElement.length) {
              closeElement = element.find(attr.closetarget);
              if (!closeElement.length && $('#closeTarget').length) {
                closeElement = $('#closeTarget');
              } else {
                closeElement = draggableElement;
              }
            } 
          }
        } else if ($('#closeTarget').length) {
          closeElement = $('#closeTarget');
        }
        if (closeElement.length) {
          closeElement = $(closeElement[0]);
        }
        
        

        if (closeElement === draggableElement) {
          closeElement.on('dblclick', function(e){
            scope.close(e);
          })
        } else {
          closeElement.on('click', function(e){
            scope.close(e);
          })
        }


      })

      function mousemove(event) { //
        y = event.pageY - startY;
        x = event.pageX - startX;
        x = x < 0? 0:x;
        y = y < 0? 0:y;
        x = (x + element.width()) > element.parent().width()? element.parent().width() - element.width(): x;
        y = (y + element.height()) > element.parent().height()? element.parent().height() - element.height(): y;
        element.css({
          top: y + 'px',
          left:  x + 'px'
        });
      }

      function mouseup() {
        $document.off('mousemove', mousemove);
        $document.off('mouseup', mouseup);
      }

      element.addClass(attr.windowClass || '');
      scope.size = attr.size;

      $timeout(function () {
        // trigger CSS transitions
        scope.animate = true;
        // focus a freshly-opened modal
        element[0].focus();
      });

      scope.close = function (evt) {
        
        var draggable = $draggableStack.getTop();
        if (draggable && (evt.target === evt.currentTarget)) {
          evt.preventDefault();
          evt.stopPropagation();
          $draggableStack.dismiss(draggable.key, 'backdrop click');
        }
      };
    }
  };
}])
.factory('$draggableStack', ['$transition', '$timeout', '$document', '$compile', '$rootScope', '$$stackedMap', function ($transition, $timeout, $document, $compile, $rootScope, $$stackedMap) {


  var OPENED_DRAGGABLE_CLASS = 'draggable-open';

  var openedWindows = $$stackedMap.createNew();
  var $draggableStack = {};

  function removeDraggableWindow(draggableInstance) {

    var body = $document.find('body').eq(0);
    var draggableWindow = openedWindows.get(draggableInstance).value;

    openedWindows.remove(draggableInstance);

    removeAfterAnimate(draggableWindow.draggableDomEl, draggableWindow.draggableScope, 300, function() {
      draggableWindow.draggableScope.$destroy();
      body.toggleClass(OPENED_DRAGGABLE_CLASS, openedWindows.length() > 0);
    });
  }

  function removeAfterAnimate(domEl, scope, emulateTime, done) {
    // Closing animation
    scope.animate = false;

    var transitionEndEventName = $transition.transitionEndEventName;
    if (transitionEndEventName) {
      // transition out
      var timeout = $timeout(afterAnimating, emulateTime);

      domEl.bind(transitionEndEventName, function () {
        $timeout.cancel(timeout);
        afterAnimating();
        scope.$apply();
      });
    } else {
      // Ensure this call is async
      $timeout(afterAnimating, 0);
    }

    function afterAnimating() {
      if (afterAnimating.done) {
        return;
      }
      afterAnimating.done = true;

      domEl.remove();
      if (done) {
        done();
      }
    }
  }

  $draggableStack.open = function (draggableInstance, draggable) {

    var openable = true;
    if (!draggable.allowMultiples) {
      var openKeys = openedWindows.keys();
      _.each(openKeys, function(key){
        var that = openedWindows.get(key);
        if (that.value.draggableId === draggable.id) {
          openable = false;
        }
      })
    }
    
    if (!openable){
      // we could throw an error here, but this is just to warn the coders...
      // throw new Error('A draggable element by that id already exists.');
    }
    else {
      openedWindows.add(draggableInstance, {
        deferred: draggable.deferred,
        draggableScope: draggable.scope,
        draggableId: draggable.id
      });

      var body = $document.find('body').eq(0);

      var angularDomEl = angular.element('<div draggable></div>');
      angularDomEl.attr({
        'template-url': draggable.windowTemplateUrl,
        'window-class': draggable.windowClass,
        'size': draggable.size,
        'index': openedWindows.length() - 1,
        'animate': 'animate',
        'moveTarget': draggable.moveTarget,
        'closeTarget': draggable.closeTarget,
      }).html(draggable.content);
      if (draggable.alwaysontop){
        angularDomEl.attr({
          'alwaysontop': ''
        })
      }
      angularDomEl.css({
        top: (draggable.top || 0) + 'px',
        left: (draggable.left || 0) + 'px',
        maxWidth: draggable.maxWidth? draggable.maxWidth + px : 'auto',
        maxHeight: draggable.maxHeight? draggable.maxHeight + px : 'auto',
      })
      var draggableDomEl = $compile(angularDomEl)(draggable.scope);
      openedWindows.top().value.draggableDomEl = draggableDomEl;
      body.append(draggableDomEl);
      body.addClass(OPENED_DRAGGABLE_CLASS);
    }
  };


  $draggableStack.close = function (draggableInstance, result) {
    var draggableWindow = openedWindows.get(draggableInstance).value;
    if (draggableWindow) {
      draggableWindow.deferred.resolve(result);
      removeDraggableWindow(draggableInstance);
    }
  };

  $draggableStack.dismiss = function (draggableInstance, reason) {
    if (openedWindows.get(draggableInstance)){
      var draggableWindow = openedWindows.get(draggableInstance).value;
      if (draggableWindow) {
        draggableWindow.deferred.reject(reason);
        removeDraggableWindow(draggableInstance);
      }
    }
  };
  $draggableStack.getTop = function () {
    return openedWindows.top();
  };

  return $draggableStack;

}])
.provider('$draggable', function () {

  var $draggableProvider = {
    options: {},
    $get: ['$injector', '$rootScope', '$q', '$http', '$templateCache', '$controller', '$draggableStack', function ($injector, $rootScope, $q, $http, $templateCache, $controller, $draggableStack) {

      var $draggable = {};

      function getTemplatePromise(options) {
        return options.template ? $q.when(options.template) :
        $http.get(options.templateUrl, {cache: $templateCache}).then(function (result) {
          return result.data;
        });
      }

      function getResolvePromises(resolves) {
        var promisesArr = [];
        angular.forEach(resolves, function (value, key) {
          if (angular.isFunction(value) || angular.isArray(value)) {
            promisesArr.push($q.when($injector.invoke(value)));
          }
        });
        return promisesArr;
      }

      $draggable.open = function (draggableOptions) {

        var draggableResultDeferred = $q.defer();
        var draggableOpenedDeferred = $q.defer();

        var draggableInstance = {
          result: draggableResultDeferred.promise,
          opened: draggableOpenedDeferred.promise,
          close: function (result) {
            $draggableStack.close(draggableInstance, result);
          },
          dismiss: function (reason) {
            $draggableStack.dismiss(draggableInstance, reason);
          }
        };

        draggableOptions = angular.extend({}, $draggableProvider.options, draggableOptions);
        draggableOptions.resolve = draggableOptions.resolve || {};

        if (!draggableOptions.template && !draggableOptions.templateUrl) {
          throw new Error('One of template or templateUrl options is required.');
        }
        if (!draggableOptions.id) {
          throw new Error('An ID is required.');
        }

        var templateAndResolvePromise =
        $q.all([getTemplatePromise(draggableOptions)].concat(getResolvePromises(draggableOptions.resolve)));


        templateAndResolvePromise.then(function resolveSuccess(tplAndVars) {

          var draggableScope = (draggableOptions.scope || $rootScope).$new();
          draggableScope.$close = draggableInstance.close;
          draggableScope.$dismiss = draggableInstance.dismiss;

          var ctrlInstance, ctrlLocals = {};
          var resolveIter = 1;

          if (draggableOptions.controller) {
            ctrlLocals.$scope = draggableScope;
            ctrlLocals.$draggableInstance = draggableInstance;
            angular.forEach(draggableOptions.resolve, function (value, key) {
              ctrlLocals[key] = tplAndVars[resolveIter++];
            });

            ctrlInstance = $controller(draggableOptions.controller, ctrlLocals);
          }

          $draggableStack.open(draggableInstance, {
            id: draggableOptions.id,
            scope: draggableScope,
            deferred: draggableResultDeferred,
            content: tplAndVars[0],
            animation: draggableOptions.animation,
            windowClass: draggableOptions.windowClass,
            windowTemplateUrl: draggableOptions.windowTemplateUrl,
            size: draggableOptions.size,
            alwaysontop: draggableOptions.alwaysontop,
            top: draggableOptions.top,
            left: draggableOptions.left,
            maxWidth: draggableOptions.maxWidth,
            maxHeight: draggableOptions.maxHeight,
            moveTarget: draggableOptions.moveTarget,
            closeTarget: draggableOptions.closeTarget,
            allowMultiples: draggableOptions.allowMultiples || false,
          });

        }, function resolveError(reason) {
          draggableResultDeferred.reject(reason);
        });

        templateAndResolvePromise.then(function () {//
          draggableOpenedDeferred.resolve(true);
        }, function () {
          draggableOpenedDeferred.reject(false);
        });

        return draggableInstance;
      };

      return $draggable;
    }]
  };

  return $draggableProvider;
});