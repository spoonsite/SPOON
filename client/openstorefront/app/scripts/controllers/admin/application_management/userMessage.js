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

app.controller('AdminUserMessageCtrl', ['$scope', 'business', '$uiModal', function ($scope, Business, $uiModal) {
  
  $scope.userMessages = [];
  $scope.statusFilterOptions = [
  {code: 'A', desc: 'Active'},
  {code: 'I', desc: 'Archived'}
  ];
  $scope.queryFilter = angular.copy(utils.queryFilter);
  $scope.queryFilter.status = $scope.statusFilterOptions[0].code;
  $scope.predicate = [];
  $scope.reverse = [];  

  $scope.pagination = {};
  $scope.pagination.control;
  $scope.pagination.features = {'dates': true, 'max': true};  
  
  $scope.paginationEvent = {};
  $scope.paginationEvent.control;
  $scope.paginationEvent.features = {'dates': true, 'max': true};  
  
  
  $scope.setPredicate = function (predicate, table) {
    if ($scope.predicate[table] === predicate) {
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
    if (table === 'userM') {
      $scope.pagination.control.changeSortOrder(predicate);
    } else if (table === 'notEvent') {
      $scope.paginationEvent.control.changeSortOrder(predicate);
    }
  }; 
  
  $scope.showMessage = function(message){
     if (message.details) {
       message.details = !message.details;
     } else {
       message.details = true;
     }     
  };
  
  $scope.deleteUserMessage = function(message){     
    var response = window.confirm("Are you sure you want to delete this message for " + (message.username ? message.username : message.emailAddress) + " ?");
    if (message.userMessageId && response){
      Business.userservice.removeUserMessages(message.userMessageId).then(function(results){
        $scope.refreshData();          
      });
    }
  };  
  
  $scope.deleteNotificationEvent = function(event){   
    var response = window.confirm("Are you sure you want to delete this event for " + (event.username ? event.username : event.rolegroup ? event.rolegroup : 'All') + " ?");
    if (event.eventId && response){
      Business.userservice.removeNotificationEvent(event.eventId).then(function(results){
        $scope.refreshNotificationEventData();          
      });
    }    
  };
  
  $scope.refreshNotificationEventData = function() {  
    $scope.$emit('$TRIGGERLOAD', 'messageLoader');
    if ($scope.paginationEvent.control && $scope.paginationEvent.control.refresh) {
      $scope.paginationEvent.control.refresh().then(function(){
        $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
      });
    } else {
      $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
    }     
  };  
  
  $scope.refreshData = function() {  
    $scope.$emit('$TRIGGERLOAD', 'messageLoader');
    if ($scope.pagination.control && $scope.pagination.control.refresh) {
      $scope.pagination.control.refresh().then(function(){
        $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
      });
    } else {
      $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
    }     
  };
  
  $scope.processUserMessageNow = function() {  
    $scope.$emit('$TRIGGERLOAD', 'messageLoader'); 
    Business.userservice.processUserMessagesNow().then(function() {
      $scope.refreshData();
      $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
      triggerAlert('Processing User Messages', 'processUserMessages', '', 5000, true);
    });                
  };
  
  $scope.cleanoldUserMessagesNow = function() {  
    $scope.$emit('$TRIGGERLOAD', 'messageLoader'); 
    Business.userservice.cleanoldUserMessagesNow().then(function() {
      $scope.refreshData();
      $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
      triggerAlert('Cleaned old User Messages', 'cleanedUserMessages', '', 5000, true);
    });                
  };
  
  $scope.postAdminEvent = function(){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/application_management/postNotificationEvent.html',
      controller: 'AdminPostNotifcationCtrl',
      backdrop: 'static',
      size: 'sm',
      resolve: {        
      }
    });       
  };
  
    $scope.$on('$REFRESH_EVENTS', function(){
        triggerAlert('Posted successfully', 'postMessage', 'body', 3000);
        $scope.refreshData();
    });   
  
}]);

app.controller('AdminPostNotifcationCtrl', ['$scope', 'business', '$uiModal', '$uiModalInstance',  function ($scope, Business, $uiModal, $uiModalInstance) {
    
   $scope.notEventForm = {};
   $scope.users = [];
    
    $scope.loadUsers = function () {
      $scope.$emit('$TRIGGERLOAD', 'messageLoader');

      Business.userservice.getAllUserProfiles(false, false).then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
        if (results) {
            
            $scope.users = results.data;
            $scope.users.splice(0, 0, {username: null});
        } else {
          $scope.users = [];
        }
      }, function(){
        $scope.users = [];
      });
    };
    $scope.loadUsers();    
    
    $scope.postNotification = function()
    {
        $scope.$emit('$TRIGGERLOAD', 'messageLoader'); 


        Business.userservice.postNotificationEvent($scope.notEventForm).then(function (results) {
          $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
          $scope.$emit('$TRIGGEREVENT', '$REFRESH_EVENTS');  
          $uiModalInstance.dismiss('success');
        }, function(){
            triggerAlert('Unable to post message. ', 'postMessage', '#postWindowDiv', 3000);
            $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
        });        
      
    };
    
    $scope.close = function () {
      $uiModalInstance.dismiss('cancel');
    }; 
    
}]);
