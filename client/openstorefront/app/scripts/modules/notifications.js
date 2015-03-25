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

      scope.history = [];
      scope.interval = null;
      scope.getTasks = function(){
        clearInterval(scope.interval);
        scope.interval = setInterval(function(){
          Factory.get('api/v1/service/jobs/tasks/status').then(function(result){
            if (result) {

              var total = angular.copy(scope.history);
              _.each(result.tasks, function(item){
                var found = _.find(total, {'taskId': item.taskId});
                // console.log('total', total);
                // console.log('item', item);
                
                var message = '';
                var type = '';
                var id = '';
                if (found && found.status !== item.status) {
                  message = 'Task "' + item.taskName + '"\'s status has changed and is now: "' + item.status +'"';
                  type = utils.getStatus(item.status);
                  id = item.taskId + type;
                  Factory.update(item, type, message);
                  var index = _.find(scope.history, {'taskId': item.taskId});
                  if (index) {
                    index = _.indexOf(scope.history, index);
                    scope.history[index] = item;
                  } 
                  index = _.indexOf(total, found);
                  total.splice(index, 1);
                } else if (!found && item.status !== 'DONE' && item.status !== 'CANCELLED' && item.status !== 'FAILED') {
                  if (item.status === 'QUEUED') {
                    message = 'Task "' + item.taskName + '" has been queued';
                  } else {
                    message = 'Task "' + item.taskName + '" has started';
                  }
                  type = utils.getStatus(item.status);
                  Factory.add(item, type, message);
                  scope.history.push(item);
                } else if (!found && item.status !== 'QUEUED' && item.status !== 'WORKING') {
                  if (item.status === 'DONE') {
                    message = 'Task "' + item.taskName + '" was started and is now complete';
                  } else if (item.status === 'CANCELLED') {
                    message = 'Task "' + item.taskName + '" was started and has been cancelled';
                  } else {
                    message = 'Task "' + item.taskName + '" was started and has failed';
                  }
                  type = utils.getStatus(item.status);
                  Factory.add(item, type, message);
                  scope.history.push(item);
                } else if (found) {
                  var index = _.indexOf(total, found);
                  total.splice(index, 1);
                }
              });
              _.each(total, function(item){ //
                Factory.remove(item)
                var alert = {'type': 'warning', 'msg': 'Task "' + item.taskName + '" has been removed from the task queue.', 'id': item.taskId + 'removed'};
                scope.addAlert(alert);
                setTimeout(closeAlertTrigger.bind(null, alert), 7000);
                var index = _.find(scope.history, {'taskId': item.taskId});
                if (index) {
                  index = _.indexOf(scope.history, index);
                  scope.history.splice(index, 1);
                } 
              })
            } 
          }, function(){
            // console.log('There was an error getting the status');
          })
        }, 15000); //
      } //

      scope.getTasks();
      scope.$on('$REFRESHTASKS', function(event, data){
        $timeout(function(){
          scope.getTasks();
        }, 2000);
      })

      scope.getSize = function () {
        return Factory.get().length;
      }

      var closeAlertTrigger = function(alert){
        // console.log('closing alert', alert);
        scope.closeAlert(alert);
        scope.$apply();
      }

      scope.$on('$NOTIFICATIONADDED', function (event, alert) {
        $('.notificationsBox').stop(true, true).fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
        scope.getSize();
        scope.addAlert(alert);
        // console.log('alert', alert);
        setTimeout(closeAlertTrigger.bind(null, alert), 7000);
      });
      scope.$on('$NOTIFICATIONREMOVED', function (event, alert) {
        $('.notificationsBox').stop(true, true).fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
        // console.log('We\'re refreshing notifications');
        scope.getSize();
        scope.addAlert(alert);
        // console.log('alert', alert);
        setTimeout(closeAlertTrigger.bind(null, alert), 7000);
      });

      scope.alerts = [];

      scope.checkDanger = function() {
        var found = _.find(Factory.get(), {'status': 'FAILED'});
        if (found) {
          return true;
        } else {
          return false;
        }
      }

      scope.addAlert = function(alert) {
        scope.alerts.push(alert);
      };

      scope.closeAlert = function(alert) {
        // console.log('scope.alerts', scope.alerts);
        var index = _.find(scope.alerts, alert);
        if (index) {
          index = _.indexOf(scope.alerts, index);
          scope.alerts.splice(index, 1);
        }
      };

      scope.openModal = function(){
        var modalInstance = $uiModal.open({
          template: $templateCache.get('notifications/notificationsModal.tpl.html'),
          controller: 'notificationsModalCtrl',
          size: 'lg',
          resolve: {
            size: function() {
              return 'lg';
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
.factory('notificationsFactory', ['$rootScope', '$http', '$q', '$location', function ($rootScope, $http, $q, $location) {
  // This variable will hold the state. (there is only 1 per factory -- singleton)  
  var notifications = {};

  var data = {};
  data.tasks = [];
  notifications.update = function (task, type, message) {
    // console.log('updating a task');
    var found = _.find(data.tasks, {'taskId': task.taskId});
    if (found) {
      var index = _.indexOf(data.tasks, found);
      var alert = angular.copy(task);
      data.tasks[index] = angular.copy(task);
      alert.msg = message;
      alert.type = type;
      alert.id = alert.id + type;
      $rootScope.$emit('$N-EVENT', '$NOTIFICATIONADDED', alert);
    }
  };
  notifications.add = function (task, type, message) {
    // console.log('adding a task');
    
    var found = _.find(data.tasks, {'taskId': task.taskId});
    if (!found) {
      data.tasks.push(angular.copy(task));
      task.msg = message;
      task.type = type;
      task.id = task.id + type;
      $rootScope.$emit('$N-EVENT', '$NOTIFICATIONADDED', task);
    }
  };
  notifications.remove = function (task, type, message) {
    // console.log('removing a task');
    index = _.find(data.tasks, {'taskId': task.taskId});
    if (index) {
      index = _.indexOf(data.tasks, index);
      data.tasks.splice(index, 1);
      if (type && message) {
        task = angular.copy(task);
        task.msg = message;
        task.type = type;
        task.id = task.id + type;
        $rootScope.$emit('$N-EVENT', '$NOTIFICATIONREMOVED', task);
      }
    }

  };
  notifications.get = function (url) {
    if (!url) {
      return data.tasks;
    } else {
      var deferred = $q.defer();
      $http({
        'method': 'GET',
        'url': url
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
      return deferred.promise;
    }
  };


  notifications.deleteTask = function (url, taskId) {
    var deferred = $q.defer();

    $http({
      'method': 'DELETE',
      'url': url + taskId 
    }).success(function (data, status, headers, config) { /*jshint unused:false*/
      deferred.resolve(data);
    }).error(function (data, status, headers, config) { /*jshint unused:false*/
      deferred.reject('There was an error');
    });

    return deferred.promise;
  };     

  return notifications;
}])
.controller('notificationsModalCtrl', ['$scope', '$uiModalInstance', 'size', 'notificationsFactory', '$timeout', function ($scope, $uiModalInstance, size, Factory, $timeout) {
  $scope.data = Factory.get();

  $scope.predicate = 'expireDts';
  $scope.reverse = false;

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  };

  $scope.refresh = function(){
    Factory.get('api/v1/service/jobs/tasks/status');
  }

  $scope.deleteTask = function(task){    
    var response = window.confirm("Are you sure you want DELETE " + task.taskName + "? (This may take a few seconds to apply)");
    if (response) {
      Factory.deleteTask('api/v1/service/jobs/tasks/', task.taskId).then(function (results) {
        $scope.refresh();
      });
    }
  }; 

  $scope.getStatus = function(status){
    return utils.getStatus(status);
  }

  $scope.checkStatus = function(val){
    switch(val){
      case 'DONE':
      case 'CANCELLED':
      case 'FAILED':
      return false
      break;
      case 'QUEUED':
      case 'WORKING':
      default:
      return true
      break;
    }
  }

  $scope.timer = function() {
    var result;
    var hours;
    var minutes;
    var seconds;
    var diff;
    _.each($scope.data, function(item){
      var start = new Date(item.expireDts);
      diff = ((start - Date.now()) / 1000) | 0;
      if (!diff || diff <= 0) {
        item.countDown = '00:00';
      }

      // does the same job as parseInt truncates the float
      minutes = (diff / 60) | 0;
      if (minutes > 60) {
        hours = minutes / 60 | 0;
        minutes = minutes % 60 | 0;
      }
      seconds = (diff % 60) | 0;

      minutes = minutes < 10 ? "0" + minutes : minutes;
      seconds = (seconds > 0 && seconds < 10)? "0" + seconds : (seconds > 0)? seconds : "00";

      item.countDown = hours? hours + ":" + minutes + ":" + seconds : minutes + ":" + seconds; 
    })
    if(!$scope.$$phase) {
      $scope.$apply();
    }
  };
  $scope.timer();
  // we don't want to wait a full second before the timer starts
  setInterval($scope.timer, 1000);

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

      $rootScope.$on('$N-EVENT', function(event, newEvent, infoArray){
        $rootScope.$broadcast(newEvent, infoArray);
      });

      $templateCache.put('notifications/notifications.tpl.html', '<div class="notificationsBox imitateLink" ng-click="openModal();" ng-class="checkDanger()? \'warning\':\'\'">{{getSize()}}</div><div-stick fixed-offset-top="100" style="position:fixed; top:65px; right: 20px; width: 300px;"><alert ng-repeat="alert in alerts track by alert.id" type="{{alert.type}}" close="closeAlert(alert)">{{alert.msg}}</alert></div-stick>');
      $templateCache.put('notifications/notificationsModal.tpl.html', '<div class="modal-header"><h3 class="modal-title">Tasks Queue</h3> </div> <div class="modal-body"> <button class="btn btn-default" ng-click="refresh()"><i class="fa fa-refresh"></i>&nbsp;Refresh</button> <table class="table table-bordered table-striped admin-table"> <tr> <th><a href="" ng-click="setPredicate(\'taskName\');">Name&nbsp;<span ng-show="predicate === \'taskName\'"><i ng-show="!reverse" class="fa fa-sort-alpha-asc"></i><i ng-show="reverse" class="fa fa-sort-alpha-desc"></i></span></a></th> <th><a href="" ng-click="setPredicate(\'details\');">Details&nbsp;<span ng-show="predicate === \'details\'"><i ng-show="!reverse" class="fa fa-sort-alpha-asc"></i><i ng-show="reverse" class="fa fa-sort-alpha-desc"></i></span></a></th> <th><a href="" ng-click="setPredicate(\'status\', false);">Status&nbsp;<span ng-show="predicate === \'status\'"><i ng-show="!reverse" class="fa fa-sort-alpha-asc"></i><i ng-show="reverse" class="fa fa-sort-alpha-desc"></i></span></a></th> <th><a href="" ng-click="setPredicate(\'expireDts\', false);">Expires In&nbsp;<span ng-show="predicate === \'expireDts\'"><i ng-show="!reverse" class="fa fa-sort-alpha-asc"></i><i ng-show="reverse" class="fa fa-sort-alpha-desc"></i></span></a></th> <th style="padding: 8px 3px;">Actions</th> </tr> <tr ng-repeat="item in data| orderBy:predicate:reverse"> <td>{{item.taskName}}</td> <td>{{item.details}}</td> <td ng-class="getStatus(item.status)">{{item.status}}</td> <td ng-class="">{{item.countDown}}</td> <td style="padding: 0px 3px;"> <button type="button" title="Remove Old Task" class="btn btn-default btn-sm" ng-click="deleteTask(item)" ng-disabled="checkStatus(item.status)"><i class="fa fa-trash fa-aw"></i></button> </td> </tr> </table> </div> <div class="modal-footer"> <button class="btn btn-warning" ng-click="cancel()">Close</button> </div>');
    }]);


})(window, document);
