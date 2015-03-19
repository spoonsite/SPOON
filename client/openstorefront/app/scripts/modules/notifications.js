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

angular.module('notifications', ['ui.bootstrap','mgcrea.ngStrap'])
.directive('notifications', ['$templateCache', 'notificationsFactory', '$uiModal', '$timeout', function ($templateCache, Factory, $uiModal, $timeout) {
  return {
    restrict: 'E',
    scope: {},
    template: $templateCache.get('notifications/notifications.tpl.html'),
    link: function (scope, ele, attrs) {
      scope.getSize = function () {
        return Factory.get().length;
      }
      scope.$on('$NOTIFICATIONADDED', function (event, data) {
        $('.notificationsBox').stop(true, true).fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
        scope.getSize();
        var alert = scope.addAlert("Here is a new alert");
        setTimeout(function(alert){
          scope.closeAlert(alert);
          scope.$apply();
        },10000);
      });
      scope.$on('$NOTIFICATIONREMOVED', function (event, data) {
        $('.notificationsBox').stop(true, true).fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
        console.log('We\'re refreshing notifications');
        scope.getSize();
      });

      scope.alerts = [];

      scope.checkDanger = function() {
        return scope.getSize > 10;
      }

      scope.addAlert = function(message) {
        var id = new Date().getTime();
        var alert = {'type': 'success', 'msg': message, 'id':id};
        scope.alerts.push(alert);
        return alert;
      };

      scope.closeAlert = function(alert) {
        console.log('alert', alert);
        
        var index = _.find(scope.alerts, {'id': alert.id});
        if (index) {
          index = _.indexOf(scope.alerts, index);
          scope.alerts.splice(index, 1);
        }
      };

      scope.openModal = function(){
        var modalInstance = $uiModal.open({
          template: $templateCache.get('notifications/notificationsModal.tpl.html'),
          controller: 'notificationsModalCtrl',
          size: 'sm',
          resolve: {
            size: function() {
              return 'sm';
            }
          }
        });

        modalInstance.result.then(function (result) {
        }, function () {
        });

      }
    }
  };
}]).directive('divStick', ['$timeout', function($timeout) {
  // id needs to be unique across all implementations of the directive
  var stickyDivId = 1;
  return {
    restrict: 'E',
    scope:{
      fixedOffsetTop: '@',
      fixedOffsetLeft: '@',
      elementId:'@'
    },
    transclude: true,
    templateUrl: 'views/content/stickydiv.html',
    link: function postlink(scope, element, attrs) {
      scope.elementId = scope.elementId || 'stickydiv' + (stickyDivId++);
      scope.fillerId = scope.elementId? scope.elementId +'-filler' : 'stickydiv' + (stickyDivId++) +'-filler';
      scope.currentTop = element.position().top;
      scope.currentLeft = element.position().left;
      scope.notElementHeight = 0;
      $timeout(function(){
        scope.notElementHeight = element.find('#'+scope.elementId).outerHeight();
      })

      if (scope.fixedOffsetTop && isNaN(scope.fixedOffsetTop) && $(scope.fixedOffsetTop).length > 0){
        scope.topOffset = $(scope.fixedOffsetTop).outerHeight();
        scope.topOffsetScroll = scope.currentTop - scope.topOffset;
      } else if (!isNaN(scope.fixedOffsetTop)){
        scope.topOffset = scope.fixedOffsetTop;
        scope.topOffsetScroll = scope.currentTop - scope.topOffset;
      } else if (!scope.fixedOffsetTop){
        scope.topOffset = scope.currentTop;
        scope.topOffsetScroll = scope.currentTop;
      } else {
        scope.topOffset = 0;
        scope.topOffsetScroll = scope.currentTop;
      }

      if (scope.fixedOffsetLeft && isNaN(scope.fixedOffsetLeft) && $(scope.fixedOffsetLeft).length > 0){
        scope.leftOffset = $(scope.fixedOffsetLeft).outerHeight();
        scope.leftOffsetScroll = scope.currentLeft - scope.leftOffset;
      } else if (!isNaN(scope.fixedOffsetLeft)){
        scope.leftOffset = scope.fixedOffsetLeft;
        scope.leftOffsetScroll = scope.currentLeft - scope.leftOffset;
      } else if (!scope.fixedOffsetLeft){
        scope.leftOffset = scope.currentLeft;
        scope.leftOffsetScroll = scope.currentLeft;
      } else {
        scope.leftOffset = 0;
        scope.leftOffsetScroll = scope.currentLeft;
      }


      scope.setPositionValues = function () {
        $('.popover').hide();
        var winScrollTop = $(window).scrollTop();
        if (winScrollTop < 0 || winScrollTop + $(window).height() > $(document).height() || winScrollTop < scope.topOffsetScroll) {
          element.find('#'+scope.elementId).css({
            'position': 'static',
            'background': 'transparent',
            'width': 'auto'
          });
          element.find('#'+scope.fillerId).css({
            'min-height': '0px'
          })
          return;
        }
        element.find('#'+scope.elementId).css({
          'position': 'fixed',
          'background': 'url(/openstorefront/images/squared_metal.png)',
          'top': scope.topOffset,
          'width': 'calc(100% - '+ scope.currentLeft+'px)',
          'z-index': 1010
        });
        element.find('#'+scope.fillerId).css({
          'min-height': scope.notElementHeight
        })
      };

      scope.bind = function () {
        $(window).on('scroll', scope.setPositionValues);
        $(window).on('resize', scope.setPositionValues);
      };
      scope.bind();
    }
  };
}])
.factory('notificationsFactory', ['$rootScope', '$q', '$location', function ($rootScope, $q, $location) {
  // This variable will hold the state. (there is only 1 per factory -- singleton)  
  var notifications = {};

  var data = {};
  data.tasks = [];
  notifications.add = function (task) {
    data.tasks.push(task);
    $rootScope.$emit('$MAKEITHAPPEN', '$NOTIFICATIONADDED');
  };
  notifications.remove = function (task) {
    index = _.find(data.tasks, task);
    if (index) {
      index = _.indexOf(data.tasks, index);
      data.tasks.splice(index, 1);
      $rootScope.$emit('$MAKEITHAPPEN', '$NOTIFICATIONREMOVED');
    }

  };
  notifications.get = function () {
    return data.tasks;
  };

  return notifications;
}])
.controller('notificationsCtrl', ['$scope', 'notificationsFactory', function ($scope, Factory) {
  var num = 0;
  $scope.addToFactory = function () {
    Factory.add({
      'test': '' + num++
    });
  }
  // console.log("We've added a cool test", Factory.get());
  $scope.removeFromFactory = function () {
    if (Factory.get().length) {
      --num;
      Factory.remove({
        'test': '' + num
      });
    }
  }
  // console.log("We tried to remove one", Factory.get());
  $scope.name = 'Superhero';
}])
.controller('notificationsModalCtrl', ['$scope', '$uiModalInstance', 'size', 'notificationsFactory', '$timeout', function ($scope, $uiModalInstance, size, Factory, $timeout) {
  $scope.data = Factory.get();

  $scope.ok = function (validity) {
    $uiModalInstance.close('success');
  }

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);


(function (window, document, undefined) {


  // create the template cache for the directive htmls.
  angular.module('notifications').run([
    '$templateCache', '$rootScope', '$timeout',

    function ($templateCache, $rootScope, $timeout) {

      $rootScope.$on('$MAKEITHAPPEN', function(event, newEvent, infoArray){
        $rootScope.$broadcast(newEvent, infoArray);
      });



      $templateCache.put('notifications/notifications.tpl.html', '<div class="notificationsBox imitateLink" ng-click="openModal();" ng-class="checkDanger()? \'warning\':\'\'">{{getSize()}}</div><div-stick fixed-offset-top="100" style="position:fixed; top:65px; right: 20px; width: 300px;"><alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert(alert)">{{alert.msg}}</alert></div-stick>');
      $templateCache.put('notifications/notificationsModal.tpl.html', '<div class="modal-header"> <h3 class="modal-title">I\'m a modal!</h3> </div> <div class="modal-body"> <ul> <li ng-repeat="item in items"> <a ng-click="selected.item = item">{{ item }}</a> </li> </ul> Selected: <b>{{ selected.item }}</b> </div> <div class="modal-footer"> <button class="btn btn-primary" ng-click="ok()">OK</button> <button class="btn btn-warning" ng-click="cancel()">Cancel</button> </div>');
    }]);


})(window, document);
