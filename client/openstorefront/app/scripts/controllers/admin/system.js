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

app.controller('AdminSystemCtrl', ['$scope', 'business', '$rootScope', '$uiModal', '$timeout', function ($scope, Business, $rootScope, $uiModal, $timeout) {

  $scope.recentChangesForm = {};
  $scope.recentChangesForm.lastRunDts = "";
  $scope.recentChangesForm.emailAddress = "";
  $scope.recentChangesStatus = {};
  $scope.errorTickets = {};
  $scope.maxPageNumber = 1;
  $scope.pageNumber = 1;
  $scope.EMAIL_REGEXP = utils.EMAIL_REGEXP;
  $scope.queryFilter = angular.copy(utils.queryFilter);
  $scope.queryFilter.max = 100;
  $scope.untilDate = new Date();
  $scope.appProperties = [];
  $scope.configProperties = [];
  $scope.loggers = [];
  $scope.appStatus = {};
  $scope.threads = [];
  $scope.predicate = [];
  $scope.reverse = [];
  $scope.tabs = {};
  $scope.tabs.general = true;
  
  $scope.selectTab = function(tab){
    _.forIn($scope.tabs, function(value, key){
      $scope.tabs[key] = false;
    });
    
    $scope.tabs[tab] = true;    
  };

  $scope.pagination = {};
  $scope.pagination.control;
  $scope.pagination.features = {'dates': false, 'max': true};  

  $scope.refreshTickets = function(){
    $scope.$emit('$TRIGGERLOAD', 'ticketLoader');
    if ($scope.pagination.control && $scope.pagination.control.refresh) {
      $scope.pagination.control.refresh().then(function(){
        $scope.$emit('$TRIGGERUNLOAD', 'ticketLoader');
      });
    } else {
      $scope.$emit('$TRIGGERUNLOAD', 'ticketLoader');
    }    
  };

  $scope.getPercent = function(value,max) {
    return Math.round((value / max) * 100);
  };
  
  $scope.getPercentColor = function(value) {
    if (value < 65) {
      return 'success';
    } else  if (value >= 65 && value <= 80) {
      return 'warning';
    } else {
      return 'danger';
    }
  };

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
  
  $scope.setPredicate = function(predicate, table){
    if ($scope.predicate[table] === predicate){
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
    if (table === 'error') {
      $scope.pagination.control.changeSortOrder(predicate);
    }    
  };
  
  var stickThatTable = function(){
    var offset = $('.top').outerHeight() + $('#errorTicketToolbar').outerHeight();
    $(".stickytable").stickyTableHeaders({
      fixedOffset: offset + 30
    });
  };

  $(window).resize(stickThatTable);
  $timeout(stickThatTable, 100);  

  $scope.showErrorDetails = function(ticket){

   // var ticket = _.find($scope.errorTickets.errorTickets, {'errorTicketId': ticketId});

    if (ticket.details && ticket.details === true){
      ticket.details = false;
      ticket.detailText = "View Details";
    } else {        
      ticket.details = true;
      ticket.detailText = "Hide Details";
    }

    if (!ticket.loadedDetails) {
      Business.systemservice.getErrorTicketInfo(ticket.errorTicketId).then(function (results) {
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

  $scope.refreshAppProperties = function(){
    $scope.$emit('$TRIGGERLOAD', 'appPropLoader');       
   
    Business.systemservice.getAppProperties().then(function (results) {
      if (results) {          
        $scope.appProperties = results;
      }  
      $scope.$emit('$TRIGGERUNLOAD', 'appPropLoader');        
    });      
  };
  $scope.refreshAppProperties();

 $scope.refreshConfigProps = function(){
    $scope.$emit('$TRIGGERLOAD', 'configPropLoader');       
   
    Business.systemservice.getConfigProperties().then(function (results) {
      if (results) {          
        $scope.configProperties = results;
      }  
      $scope.$emit('$TRIGGERUNLOAD', 'configPropLoader');        
    });      
  };
  $scope.refreshConfigProps();

 $scope.refreshLoggers = function(){
    $scope.$emit('$TRIGGERLOAD', 'loggingLoader');       
   
    Business.systemservice.getLoggers().then(function (results) {
      if (results) {          
        $scope.loggers = results;
      }  
      $scope.$emit('$TRIGGERUNLOAD', 'loggingLoader');        
    });      
  };
  $scope.refreshLoggers();
  
 $scope.refreshAppStatus = function(){
    $scope.$emit('$TRIGGERLOAD', 'loggingLoader');       
   
    Business.systemservice.getAppStatus().then(function (results) {
      if (results) {          
        $scope.appStatus = results;
      }  
      $scope.$emit('$TRIGGERUNLOAD', 'loggingLoader');        
    });      
  };
  $scope.refreshAppStatus();  
  
 $scope.refreshThreads = function(){
    $scope.$emit('$TRIGGERLOAD', 'loggingLoader');       
   
    Business.systemservice.getThreads().then(function (results) {
      if (results) {          
        $scope.threads = results;
      }  
      $scope.$emit('$TRIGGERUNLOAD', 'loggingLoader');        
    });      
  };
  $scope.refreshThreads();   

  $scope.editAppProperty = function(property){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/editAppProperty.html',
      controller: 'adminEditAppPropertyCtrl',
      size: 'lg',
      resolve: {
        property: function () {
          return property;
        }
      }
    }); 

  };
  $scope.$on('$REFRESH_APP_PROPS', function(){
    triggerAlert('Saved successfully', 'editAppProperty', 'body', 3000);
    $scope.refreshAppProperties();
  });

  $scope.editLogger = function(logger){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/editLogger.html',
      controller: 'adminEditLoggingCtrl',
      size: 'lg',
      resolve: {
        logger: function () {
          return logger;
        }
      }
    }); 
  };
  
  $scope.$on('$REFRESH_LOGGERS', function(){
      triggerAlert('Saved successfully', 'editLogger', 'body', 3000);
      $scope.refreshLoggers();
  });
  
  $scope.viewLog =function(){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/viewLog.html',
      controller: 'adminViewLogCtrl',      
      size: 'lg',
      resolve: {        
      }
    });    
  };

}]);

app.controller('adminEditAppPropertyCtrl', ['$scope', '$uiModalInstance', 'property', 'business', function ($scope, $uiModalInstance, property, Business) {
    $scope.propetyForm = angular.copy(property);
    
    
    $scope.cancel = function(){
     $uiModalInstance.dismiss('cancel');
    };
    
    $scope.save = function(){
      $scope.$emit('$TRIGGERLOAD', 'formLoader'); 
      Business.systemservice.updateAppProperty($scope.propetyForm.key, $scope.propetyForm.value).then(function (results) {      
       $scope.$emit('$TRIGGERUNLOAD', 'formLoader');
       $scope.$emit('$TRIGGEREVENT', '$REFRESH_APP_PROPS');       
       $uiModalInstance.dismiss('success');
      }, function(){
        triggerAlert('Unable to save. ', 'editLogger', 'body', 3000);
        $scope.$emit('$TRIGGERUNLOAD', 'formLoader'); 
      });
    };
    
}]);

app.controller('adminEditLoggingCtrl', ['$scope', '$uiModalInstance', 'logger', 'business', function ($scope, $uiModalInstance, logger, Business) {
    $scope.logger = angular.copy(logger);
    $scope.logLevels = [];
    
    $scope.loadLevels = function(){
      $scope.$emit('$TRIGGERLOAD', 'formLoader');       

      Business.systemservice.getLogLevels().then(function (results) {
        if (results) {          
          $scope.logLevels = results;          
          $scope.logger.levelSelect = _.find($scope.logLevels, {'code': $scope.logger.level});
          if ($scope.logger.levelSelect === undefined) {
            $scope.logger.useParentLevel=true;
          }
        }  
        $scope.$emit('$TRIGGERUNLOAD', 'formLoader');        
      });      
    };
    $scope.loadLevels();    
        
    $scope.cancel = function(){
     $uiModalInstance.dismiss('cancel');
    };
    
    $scope.save = function(){
      $scope.$emit('$TRIGGERLOAD', 'formLoader'); 
      if ($scope.logger.useParentLevel) {
        $scope.logger.level = null;
      } else {
        $scope.logger.level = $scope.logger.levelSelect.code;
      }
      
      Business.systemservice.updateLogLevel($scope.logger.name, $scope.logger.level).then(function (results) {      
       $scope.$emit('$TRIGGERUNLOAD', 'formLoader'); 
       $scope.$emit('$TRIGGEREVENT', '$REFRESH_LOGGERS');  
       $uiModalInstance.dismiss('success');
      }, function(){
        triggerAlert('Unable to save. ', 'editLogger', 'body', 3000);
        $scope.$emit('$TRIGGERUNLOAD', 'formLoader'); 
      });
    };
    
}]);

app.controller('adminViewLogCtrl', ['$scope', '$uiModalInstance', 'business', '$sce', function ($scope, $uiModalInstance, Business, $sce) {
    
    $scope.logRecords = [];
    
    $scope.predicate = [];
    $scope.reverse = [];  

    $scope.pagination = {};
    $scope.pagination.control;
    $scope.pagination.features = {'dates': true, 'max': true};  

    $scope.setPredicate = function (predicate, table) {
      if ($scope.predicate[table] === predicate) {
        $scope.reverse[table] = !$scope.reverse[table];
      } else {
        $scope.predicate[table] = predicate;
        $scope.reverse[table] = false;
      }
      if (table === 'logs') {
        $scope.pagination.control.changeSortOrder(predicate);
      }
    };    
    
    $scope.refreshData = function() {  
      $scope.$emit('$TRIGGERLOAD', 'logLoader');
      if ($scope.pagination.control && $scope.pagination.control.refresh) {
        $scope.pagination.control.refresh().then(function(){
          $scope.$emit('$TRIGGERUNLOAD', 'logLoader');
        });
      } else {
        $scope.$emit('$TRIGGERUNLOAD', 'logLoader');
      }     
    };   
    
    $scope.showDetails = function(record){
       if (record.details) {
         record.details = !record.details;
       } else {
         record.details = true;
       }     
    };  
    
    $scope.clearAll = function () {
      var response = window.confirm("Clear all DB log records?");
      if (response) {
        Business.systemservice.clearAllLogRecords().then(function (result) {
          $scope.refreshData();
        });
      }
    };
       
    $scope.getLogContent = function(record) {
      return $sce.trustAsHtml(record.stackTrace);
    };    
    
    $scope.cancel = function(){
     $uiModalInstance.dismiss('cancel');
    };
    
}]);