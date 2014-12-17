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

app.controller('AdminUserMessageCtrl', ['$scope', 'business', function ($scope, Business) {
  
  $scope.userMessages = [];
  $scope.statusFilterOptions = [
  {code: 'A', desc: 'Active'},
  {code: 'I', desc: 'Archived'}
  ];
  $scope.queryFilter = angular.copy(utils.queryFilter);
  $scope.queryFilter.status = $scope.statusFilterOptions[0];
  
  $scope.deleteUserMessage = function(username, userMessageId){     
    var response = window.confirm("Are you sure you want to delete this message for " + username + " ?");
    if (userMessageId && response){
      Business.userservice.removeUserMessages(userMessageId).then(function(results){
        $scope.refreshData();          
      });
    }
  };    
  
  $scope.refreshData = function() {  
    $scope.$emit('$TRIGGERLOAD', 'messageLoader'); 
    Business.userservice.getUserMessages($scope.queryFilter).then(function (results) {
      if (results) {
        $scope.userMessages = results;
      }  
      $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');        
    });                
  };
  
  $scope.refreshData();
  
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
  
}]);
