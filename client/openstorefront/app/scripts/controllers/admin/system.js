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

app.controller('AdminSystemCtrl', ['$scope', 'business', '$rootScope', '$uiModal', function ($scope, Business, $rootScope, $uiModal) {
    
    $scope.recentChangesForm = {};
    $scope.recentChangesForm.lastRunDts = "";
    $scope.recentChangesForm.emailAddress = "";
    $scope.recentChangesStatus = {};
    $scope.errorTickets = {};
    $scope.maxPageNumber = 1;
    $scope.pageNumber = 1;
    
    $scope.EMAIL_REGEXP = /^[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*$/i;
    
    $scope.errorTicketsQueryFilter = {
       status: null,
       max: 100,
       sortField: null,
       sortOrder: null,
       offset: null,
       toQuery: function () {
         var queryParams = "";
         if (this.status !== null){
           queryParams += "status=" + this.status.code + "&";
         }
         if (this.max !== null){
           queryParams += "max=" + this.max + "&";
         }
         if (this.sortField !== null){
           queryParams += "sortField=" + this.sortField + "&";
         }
         if (this.sortOrder !== null){
           queryParams += "sortOrder=" + this.sortOrder + "&";
         }
         if (this.offset !== null){
           queryParams += "offset=" + this.offset + "&";
         }
         return queryParams;
       }
     }; 
     
     $scope.refreshTickets = function(){
      $scope.$emit('$TRIGGERLOAD', 'ticketLoader');       
      $scope.errorTicketsQueryFilter.offset = ($scope.pageNumber - 1) * $scope.errorTicketsQueryFilter.max;
      Business.systemservice.getErrorTickets($scope.errorTicketsQueryFilter).then(function (results) {
        if (results) {          
          $scope.errorTickets = results;
          
          _.forEach($scope.errorTickets.errorTickets, function(ticket){
            ticket.detailText="View Details";
          });
          
          $scope.maxPageNumber = Math.ceil($scope.errorTickets.totalNumber / $scope.errorTicketsQueryFilter.max);          
        }  
        $scope.$emit('$TRIGGERUNLOAD', 'ticketLoader');        
      });      
    };
    $scope.refreshTickets();
        
    $scope.firstPage = function(){
      $scope.pageNumber=1;
      $scope.refreshTickets();      
    };
    
    $scope.lastPage = function(){
      $scope.pageNumber=$scope.maxPageNumber;
      $scope.refreshTickets();
    };

    $scope.prevPage = function(){
      $scope.pageNumber=$scope.pageNumber-1;
      if ($scope.pageNumber < 1) {
        $scope.pageNumber = 1;
      }   
      $scope.refreshTickets();     
    };

    $scope.nextPage = function(){
      $scope.pageNumber=$scope.pageNumber+1;
      if ($scope.pageNumber > $scope.maxPageNumber) {
        $scope.pageNumber = $scope.maxPageNumber;
      }  
      $scope.refreshTickets(); 
    };    
    
    $scope.setPageSize = function(){
        $scope.pageNumber = 1;
        $scope.refreshTickets(); 
    };
    
    $scope.jumpPage = function(){
     if ($scope.pageNumber > $scope.maxPageNumber) {
      $scope.pageNumber = $scope.maxPageNumber;
     }
     $scope.refreshTickets(); 
    };
    
    $scope.showErrorDetails = function(ticketId){

      var ticket = _.find($scope.errorTickets.errorTickets, {'errorTicketId': ticketId});

      if (ticket.details && ticket.details === true){
         ticket.details = false;
         ticket.detailText = "View Details";
      } else {        
        ticket.details = true;
        ticket.detailText = "Hide Details";
      }

      if (!ticket.loadedDetails) {
         Business.systemservice.getErrorTicketInfo(ticketId).then(function (results) {
            ticket.loadedDetails = results;
         });
      }
    };    
    
    $scope.wrapString = function(data){
      if (data !== undefined && data !== null) {        
        var newString = "";
        for (var i=0; i<data.length; i++){
          newString += data.charAt(i);
          if (newString.length % 40 === 0) {
            newString += " ";
          }
        }
        return newString;
      }
      return data;
    };
    
    $scope.reindexListings = function(){
     var response = window.confirm("Are you sure you want to reset Indexer?");
     if (response){
       Business.systemservice.resetIndexer().then(function (results) {
         triggerAlert('Re-Index task submitted see jobs -> tasks for status', 'reIndexMessage', '', 5000, true);
       }); 
     }
    };
    
    $scope.recentChangeStatus = function(){
     Business.systemservice.getRecentChangeStatus().then(function (result){
        $scope.recentChangesStatus = result;
     }); 
    };
    $scope.recentChangeStatus();
  
    $scope.sendRecentChangesEmail = function(){
      var response = window.confirm("Are you sure you want to email recent changes?");
      if (response){
        Business.systemservice.sendRecentChangesEmail($scope.recentChangesForm.lastRunDts, $scope.recentChangesForm.emailAddress).then(function (result){
          triggerAlert('Emailing recent changes, see jobs -> tasks for status', 'recentChangesEmailMessage', '', 5000, true);
       });      
     }
    };
    
}]);

