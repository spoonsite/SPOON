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



(function (window, document, undefined) {
// create the template cache for the directive htmls.
var app;
try {
  app = angular.module('openstorefrontApp');
} catch(err) {
  try {
    app = angular.module('submissionApp');
  } catch(err) {
    //this will break some stuff because we're dependent on other portions of the openstorefront/submission apps.
    //I tried to keep it seperate, but theres a need here to use some of the neat directives we made for the openstorefront app 
    app = angular.module('notifications', ['ui.bootstrap','mgcrea.ngStrap'])
  }  
}
app.run(['$templateCache', '$rootScope', '$timeout', function ($templateCache, $rootScope, $timeout) {
  $rootScope.$on('$N-EVENT', function(event, newEvent, infoArray){
    $rootScope.$broadcast(newEvent, infoArray);
  });

  $templateCache.put('notifications/notifications.tpl.html', '<div class="notificationsBox imitateLink" ng-click="openModal();" ng-class="checkDanger()? \'warning\':\'\'">{{size}}</div><div-stick fixed-offset-top="100" style="position:fixed; top:65px; right: 20px; width: 300px;"><div ng-show="alerts.length && !types.length"><alert ng-repeat="alert in alerts track by alert.id" type="{{getAlertType(alert);}}" close="closeAlert(alert)"><span dynamichtml="alert.msg"></span></alert></div><div ng-show="types.length"><alert ng-repeat="alert in types track by alert.id" type="{{getAlertType(alert);}}" close="closeType(alert)"><span dynamichtml="alert.msg"></span></alert></div></div-stick>');
  $templateCache.put('notifications/notificationsModal.tpl.html', '<div class="modal-header"><h3 class="modal-title">Notifications <small>(Notifications time out after a week)</small></h3></div><div class="modal-body"><button class="btn btn-default" ng-click="refresh()"><i class="fa fa-refresh"></i>&nbsp;Refresh</button><div class="btn-group" data-toggle="buttons"><button type="button" class="btn btn-default" ng-class="unreadFlg" ng-click="setRFlg(false)">Unread</button><button type="button" class="btn btn-default" ng-class="readFlg" ng-click="setRFlg(true)">All</button></div><table class="table table-bordered table-striped admin-table"><tr><th><a href="" ng-click="setPredicate(\'entityType\');">Type&nbsp;<span ng-show="predicate === \'entityType\'"><i ng-show="!reverse" class="fa fa-sort-alpha-asc"></i><i ng-show="reverse" class="fa fa-sort-alpha-desc"></i></span></a></th><th><a href="" ng-click="setPredicate(\'message\');">Message&nbsp;<span ng-show="predicate === \'message\'"><i ng-show="!reverse" class="fa fa-sort-alpha-asc"></i><i ng-show="reverse" class="fa fa-sort-alpha-desc"></i></span></a></th><th style="padding: 8px 3px;">Actions</th></tr><tr ng-if="readFlg" ng-repeat="item in data| orderBy:predicate:reverse"><td style="padding: 0px !important; height:1px; vertical-align: inherit;"><div style="width: 7px; height:100%; min-height: 20px; margin-right:3px; border-right:1px solid darkgray; border-top:1px solid darkgray; border-bottom:1px solid darkgray; float:left;" class="imitateLink isRead" ng-click="toggleReadStatus(item)" ng-class="{\'unreadTableItem\':!item.readMessage}" data-id="{{item.eventId}}" data-html="true" data-toggle="tooltip" data-placement="right">&nbsp;</div><div style="padding: 5px !important;">{{getItemName(item)}}</div></td><td><span dynamichtml="getMessage(item)"></span></td><td style="padding: 0px 3px;"><button ng-show="user.username === item.username" type="button" title="Remove Old Task" class="btn btn-danger btn-sm" ng-click="deleteTask(item)"><i class="fa fa-trash fa-aw"></i></button></td></tr><tr ng-if="unreadFlg" ng-repeat="item in filteredItems = (data | hasProperty:\'readMessage\':false |  orderBy:predicate:reverse)"><td ng-show="!filteredItems.length" colspan="3"></td><td style="padding: 0px !important; height:1px; vertical-align: inherit;"><div style="width: 7px; height:100%; min-height: 20px; margin-right:3px; border-right:1px solid darkgray; border-top:1px solid darkgray; border-bottom:1px solid darkgray; float:left;" class="imitateLink isRead" ng-click="toggleReadStatus(item)" ng-class="{\'unreadTableItem\':!item.readMessage}" data-id="{{item.eventId}}" data-html="true" data-toggle="tooltip" data-placement="right">&nbsp;</div><div style="padding: 5px !important;">{{getItemName(item)}}</div></td><td><span dynamichtml="getMessage(item)"></span></td><td style="padding: 0px 3px;"><button ng-show="user.username === item.username" type="button" title="Remove Old Task" class="btn btn-danger btn-sm" ng-click="deleteTask(item)"><i class="fa fa-trash fa-aw"></i></button></td></tr></table><div ng-if="!filteredItems.length && unreadFlg">You have no unread notifications.</div></div><div class="modal-footer"><button class="btn btn-default" ng-click="cancel()"><i class="fa fa-close"></i>&nbsp;Close</button></div>');
}])
.directive('notifications', ['$templateCache', 'notificationsFactory', '$uiModal', '$timeout', 'socket', function ($templateCache, Factory, $uiModal, $timeout, socket) {
  return {
    restrict: 'E',
    scope: {},
    template: $templateCache.get('notifications/notifications.tpl.html'),
    link: function (scope, ele, attrs) {
      scope.size = 0;
      scope.types = [];
      var bumpIcon = function(){
        $('.notificationsBox').stop(true, true).fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
      }
      socket.on('connect', function () {
        // console.warn(this.socket.transport.name + ' contected');
      });
      socket.on('WATCH', function (args) {
        // console.log('this', args);
        var alert = {'type': args.entityMetaDataStatus? scope.getStatus(args.entityMetaDataStatus): 'watch', 'msg': args.message + '<i>View the changes <a href="single?id='+args.entityId+'"><strong>here</strong></a>.</i>', 'id': 'watch_'+ args.eventId};
        bumpIcon();
        scope.getSize();
        scope.addAlert(alert, 10000);
      });
      socket.on('IMPORT', function (args) {
        // console.log('this', args);
        var alert = {'type': args.entityMetaDataStatus? scope.getStatus(args.entityMetaDataStatus): 'import', 'msg': args.message, 'id': 'import_'+ args.eventId};
        bumpIcon();
        scope.getSize();
        scope.addAlert(alert, 10000);
      });
      socket.on('TASK', function (args) {
        // console.log('this', args);
        var alert = {'type': args.entityMetaDataStatus? scope.getStatus(args.entityMetaDataStatus): 'task', 'msg': args.message, 'id': 'task_'+ args.eventId};
        bumpIcon();
        scope.getSize();
        scope.addAlert(alert, 10000);
      });
      socket.on('REPORT', function (args) {
        // console.log('this', args);
        var alert = {'type': args.entityMetaDataStatus? scope.getStatus(args.entityMetaDataStatus): 'report', 'msg': args.message + '<i>View/Download the report <a href="tools?tool=Reports"><strong>here</strong></a></i>.', 'id': 'report_'+ args.eventId};
        bumpIcon();
        scope.getSize();
        scope.addAlert(alert, 10000);
      });
      socket.on('ADMIN', function (args) {
        // console.log('this', args);
        var alert = {'type': args.entityMetaDataStatus? scope.getStatus(args.entityMetaDataStatus): 'admin', 'msg': '<i class="fa fa-warning"></i>&nbsp;' + args.message, 'id': 'admin_'+ args.eventId};
        bumpIcon();
        scope.getSize();
        scope.addAlert(alert, 10000);
      });

      scope.$on('$REFRESHTASKS', function(){
        scope.getSize();
      })

      // Factory.get().then(function(result){
      //   _.each(result.data, function(args){
      //     if (!args.readMessage){
      //       var alert = '';
      //       switch(args.eventType){
      //         case 'WATCH':
      //         alert = {'type': 'watch', 'msg': args.message + '<i>View the changes <a href="single?id='+args.entityId+'"><strong>here</strong></a>.</i>', 'id': 'watch_'+ args.eventId};
      //         break;
      //         case 'REPORT':
      //         alert = {'type': 'report', 'msg': args.message + '<i>View/Download the report <a href="tools?tool=Reports"><strong>here</strong></a></i>.', 'id': 'report_'+ args.eventId};
      //         break;
      //         case 'ADMIN':
      //         alert = {'type': 'admin', 'msg': '<i class="fa fa-warning"></i>&nbsp;' + args.message, 'id': 'admin_'+ args.eventId};
      //         break;
      //         case 'TASK':
      //         alert = {'type': 'task', 'msg': args.message, 'id': 'task_'+ args.eventId};
      //         break;
      //         case 'IMPORT':
      //         alert = {'type': 'import', 'msg': args.message, 'id': 'import_'+ args.eventId};
      //         break;
      //         default:
      //         alert = {'type': 'task', 'msg': args.message, 'id': 'task_'+ args.eventId};
      //         break;
      //       }
      //       scope.addAlert(alert, 10000);
      //     }
      //   })
      //   bumpIcon();//
      //   scope.getAlertTypes();
      //   scope.getSize();
      // });

      scope.getSize = function () { //
        Factory.get().then(function(result){
          scope.size = _.countBy(result.data, function(n) {
            return n.readMessage;
          }).false || 0;
        })
      }
      
      scope.getAlertTypes = function(){
        Factory.get().then(function(result){
          var alerts = angular.copy(result.data);
          var count = _.countBy(alerts, function(n) {
            return !n.readMessage? n.eventType: 'ALREADY_READ';
          });
          _.each(count, function(item, type){
            var alert = '';
            switch(type){
              case 'ALREADY_READ':
              break;
              case 'ADMIN':
              // console.log('item', item);
              
              alert = item !== 1? {'type': 'admin', 'msg': 'There are '+item+' admin notifications for you to view.', 'id': 'admin_all'}: {'type': 'admin', 'msg': 'There is '+item+' admin notification for you to view.', 'id': 'admin_all'};
              break;
              case 'IMPORT':
              alert = item !== 1? {'type': 'import', 'msg': 'There are '+item+' import notifications for you to view.', 'id': 'import_all'}: {'type': 'import', 'msg': 'There is '+item+' import notification for you to view.', 'id': 'import_all'};
              break;
              case 'REPORT':
              alert = item !== 1? {'type': 'report', 'msg': 'There are '+item+' report notifications for you to view.', 'id': 'report_all'}: {'type': 'report', 'msg': 'There is '+item+' report notification for you to view.', 'id': 'report_all'};
              break;
              case 'TASK':
              alert = item !== 1? {'type': 'task', 'msg': 'There are '+item+' task notifications for you to view.', 'id': 'task_all'}: {'type': 'task', 'msg': 'There is '+item+' task notification for you to view.', 'id': 'task_all'};
              break;
              case 'WATCH':
              alert = item !== 1? {'type': 'watch', 'msg': 'There are '+item+' watch notifications for you to view.', 'id': 'watch_all'} : {'type': 'watch', 'msg': 'There is '+item+' watch notification for you to view.', 'id': 'watch_all'};
              break;
              default:
              break;
            }
            if (alert) {
              bumpIcon();
              scope.getSize();
              scope.addType(alert);
              setTimeout(closeTypeTrigger.bind(null, alert), 10000);
            }
          })
        });//
      }//
      scope.getAlertTypes();


      scope.getAlertType = function(alert){
        return alert.type;
      }

      scope.getStatus = function(status){
        return utils.getStatus(status);
      }
      
      var closeAlertTrigger = function(alert){
        // console.log('closing alert', alert);
        scope.closeAlert(alert);
        if(!scope.$$phase) {
          scope.$apply();
        }
      }

      var closeTypeTrigger = function(alert){
        // console.log('closing alert', alert);
        scope.closeType(alert);
        if(!scope.$$phase) {
          scope.$apply();
        }
      }

      scope.$on('$NOTIFICATIONADDED', function (event, alert) {
        $('.notificationsBox').stop(true, true).fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
        scope.getSize();
        scope.addAlert(alert, 7000);
      });
      scope.$on('$NOTIFICATIONREMOVED', function (event, alert) {
        $('.notificationsBox').stop(true, true).fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
        scope.getSize();
        scope.addAlert(alert, 7000);
      });

      scope.alerts = [];

      scope.checkDanger = function() {
        var found = _.find(Factory.get(true), {'status': 'FAILED'});
        if (found) {
          return true;
        } else {
          return false;
        }
      }

      var stack = [];
      var timer = null;
      scope.addAlert = function(alert, timeout) {
        scope.types = [];
        stack.push({'alert': alert, 'timeout': timeout});
        function addToStack() {
          // console.log('stack', scope.alerts);
          
          timer = setTimeout(function(){
            scope.$apply(function(){
              if (stack.length) {
                var thing = stack.shift();
                scope.alerts.push(thing.alert);
                setTimeout(closeAlertTrigger.bind(null, thing.alert), thing.timeout);
                addToStack();
              } else {
                timer = null;
              }
            })
          },1000);
        };
        if (timer === null){
          addToStack(alert, timeout);
        }
      };

      scope.closeAlert = function(alert) {
        // console.log('scope.alerts', scope.alerts);
        var index = _.find(scope.alerts, alert);
        if (index) {
          index = _.indexOf(scope.alerts, index);
          scope.alerts.splice(index, 1);
        }
      };

      scope.addType = function(alert) {
        scope.types.push(alert);
      };

      scope.closeType = function(alert) {
        // console.log('scope.alerts', scope.alerts);
        var index = _.find(scope.types, alert);
        if (index) {
          index = _.indexOf(scope.types, index);
          scope.types.splice(index, 1);
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
.factory('notificationsFactory', ['$rootScope', '$http', '$q', '$location', 'business', function ($rootScope, $http, $q, $location, Business) {
  // This variable will hold the state. (there is only 1 per factory -- singleton)  
  var notifications = {};

  var data = {};
  var notBlocking = true;
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
  notifications.get = function (cached) {
    // console.log('cached', cached);
    // console.log('data', data);
    // console.log('notBlo', notBlocking);
    
    if (cached && data.tasks && notBlocking) {
      return data.tasks; 
    } else if (cached && notBlocking){ 
      notBlocking = false;
      Business.notificationservice.getUserEvents().then(function(events){
        // console.log('event', events);
        
        data.tasks = events;
        notBlocking = true;
      }, function(){
        // console.log('event', events);
        data.tasks = [];
        notBlocking = true;
      })
    } else if (cached) {
      return [];
    } else {
      return Business.notificationservice.getUserEvents();
    }
  };


  notifications.deleteTask = function (url, taskId) {
    var deferred = $q.defer();

    $http({
      'method': 'DELETE',
      'url': url + taskId 
    }).success(function (data, status, headers, config) { /*jshint unused:false*/
      $rootScope.$emit('$N-EVENT', '$REFRESHTASKS', 100);
      deferred.resolve(data);
    }).error(function (data, status, headers, config) { /*jshint unused:false*/
      $rootScope.$emit('$N-EVENT', '$REFRESHTASKS', 100);
      deferred.reject('There was an error');
    });

    return deferred.promise;
  };     

  return notifications;
}])
.controller('notificationsModalCtrl', ['$scope', '$uiModalInstance', 'size', 'notificationsFactory', '$timeout', 'business', function ($scope, $uiModalInstance, size, Factory, $timeout, Business) {
  Factory.get().then(function(data){
    $scope.data = data.data;
    $scope.$emit('$N-EVENT', '$REFRESHTASKS', 100);
  });
  $scope.predicate = 'expireDts';
  $scope.reverse = false;
  $scope.user;
  $scope.readFlg = '';
  $scope.unreadFlg = 'active';
  $scope.setRFlg = function(bool){
    $timeout(function(){
      if (bool){
        $scope.readFlg = 'active';
        $scope.unreadFlg = '';
      } else {
        $scope.unreadFlg = 'active';
        $scope.readFlg = '';
      }
    },10)
  }
  Business.userservice.getCurrentUserProfile().then(function(profile){
    $scope.user = profile;
  })

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  };

  $scope.refresh = function(){
    Factory.get().then(function(data){
      $scope.data = data? data.data: [];
      $scope.$emit('$N-EVENT', '$REFRESHTASKS', 100);
      if(!$scope.$$phase) {
        $scope.$apply();
      }
    }, function(){
      $scope.data = [];
      //something broke the refresh...
    });
  }

  $scope.$on('$NOTIFICATIONREMOVED', function(event, task){
    $scope.refresh();
  })

  $scope.deleteTask = function(task){    
    // console.log('task', task);
    
    var response = window.confirm("Are you sure you want DELETE this " + task.eventTypeDescription + " notification? (This may take a few seconds to apply)");
    if (response) {
      Business.notificationservice.deleteEvent(task.eventId).then(function (results) {
        $timeout(function(){
          $scope.refresh();
        });
      }, function(){
        $timeout(function(){
          $scope.refresh();
        });
      });
    }
  }; 

  $scope.getStatus = function(status){
    return utils.getStatus(status);
  }

  $scope.getAlertType = function(alert){
    return alert.type;
  }

  $scope.getItemName = function(item){
    return item.entityName? item.entityName: item.eventTypeDescription;
  }

  $scope.getReadMessage = function(item){
    return item.readMessage? 'Mark as <i>Unread</i>': 'Mark as <i>Read</i>';
  }
  
  $scope.getMessage = function(item){
    switch(item.eventType){
      case 'WATCH':
      return item.message + '<i>View the changes <a href="single?id='+item.entityId+'"><strong>here</strong></a>.</i>';
      break;
      case 'REPORT':
      return item.message + '<i>View/Download the report <a href="tools?tool=Reports"><strong>here</strong></a></i>.';
      break;
      case 'ADMIN':
      return '<i class="fa fa-warning"></i>&nbsp;' + item.message;
      break;
      case 'TASK':
      case 'IMPORT':
      default:
      return item.message;
      break;
    }
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

  $scope.toggleReadStatus = function(item){
    if (item.readMessage) {
      Business.notificationservice.deleteNewEvent(item.eventId).then(function(){
      }, function(){
        item.readMessage = !item.readMessage;
        setupTooltips();
      });
    } else {
      Business.notificationservice.putNewEvent(item.eventId).then(function(){
        item.readMessage = !item.readMessage;
        setupTooltips();
      });
    }
  }

  $scope.timer = function() {
    _.each($scope.data, function(item){
      var result;
      var hours;
      var minutes;
      var seconds;
      var diff;
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

      var result = hours? hours + ":" + minutes + ":" + seconds : minutes + ":" + seconds; 

      item.countDown = (function(result){
        return result;
      }(result));
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

  var setupTooltips = function(){
    $timeout(function() {
      $('.isRead[data-toggle=\'tooltip\']').tooltip({
        title: function(){
          var that = $(this);
          var thing = _.find($scope.data, {'eventId': that.attr('data-id')});
          return thing? $scope.getReadMessage(thing): '';
        }
      });
    }, 300);
  }
  setupTooltips();

}])
.filter("hasProperty", function(){
  return function(input, key, value){
    if (!input) {
      return [];
    }
    var search = {};
    search[key] = value;
    var result = _.filter(input, search);
    return result;
  }
});
})(window, document);
