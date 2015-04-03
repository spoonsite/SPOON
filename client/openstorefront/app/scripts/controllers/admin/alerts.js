/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

app.controller('AdminAlertCtrl', ['$scope', 'business', '$rootScope', '$uiModal', function ($scope, Business, $rootScope, $uiModal) {
  
  $scope.alerts = [];
  $scope.predicate = [];
  $scope.reverse = [];
  
  $scope.setPredicate = function (predicate, table) {
    if ($scope.predicate[table] === predicate) {
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
  };  
  
    $scope.statusFilterOptions = [
      {code: 'A', desc: 'Active'},
      {code: 'I', desc: 'Inactive'}
    ];
    
    $scope.alertFilter = angular.copy(utils.queryFilter);   
    $scope.alertFilter.status = $scope.statusFilterOptions[0].code;     
  
  $scope.refreshAlerts = function(){
      $scope.$emit('$TRIGGERLOAD', 'alertLoader');

      Business.alertservice.getAlerts($scope.alertFilter).then(function (results) {
        if (results) {
          $scope.alerts = results;
        }
        $scope.$emit('$TRIGGERUNLOAD', 'alertLoader');
      });    
  };
  $scope.refreshAlerts();
  
  $scope.$on('$REFRESH_ALERTS', function(){     
      $scope.refreshAlerts();
  });    
  
  $scope.addAlert = function(){
      var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/editAlert.html',
        controller: 'AdminEditAlertCtrl',
        backdrop: 'static',
        size: 'sm',
        resolve: {
          alert: function () {
            return null;
          }
        }
      });    
  };
  
  $scope.editAlert = function(alert){
      var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/editAlert.html',
        controller: 'AdminEditAlertCtrl',
        backdrop: 'static',
        size: 'sm',
        resolve: {
          alert: function () {
            return alert;
          }
        }
      });    
  };  
  
  $scope.toggleStatus = function(alert){
      $scope.$emit('$TRIGGERLOAD', 'alertLoader');
      if (alert.activeStatus === 'A') {
        Business.alertservice.inactivateAlert(alert.alertId).then(function (results) {
          $scope.refreshAlerts();
          $scope.$emit('$TRIGGERUNLOAD', 'alertLoader');
        });        
      } else {
        Business.alertservice.activateAlert(alert.alertId).then(function (results) {
          $scope.refreshAlerts();
          $scope.$emit('$TRIGGERUNLOAD', 'alertLoader');
        });        
      }    
  };  
   
  $scope.deleteAlert = function(alert){
      var response = window.confirm("Are you sure you want DELETE "+ alert.name + "?");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', 'alertLoader');
        Business.alertservice.removeAlert(alert.alertId).then(function (result) {          
          $scope.$emit('$TRIGGERUNLOAD', 'alertLoader');
          $scope.refreshAlerts();
        });
      }    
  };     
    
}]);

app.controller('AdminEditAlertCtrl', ['$scope', '$uiModalInstance', 'alert', 'business', '$uiModal', '$timeout',
  function ($scope, $uiModalInstance, alert, Business, $uiModal, $timeout) {

    $scope.alertForm = angular.copy(alert);
    $scope.email = {};    
    $scope.email.type = 'users';
    if (alert) {
      $scope.title = "Edit Alert";
      
      //Pack email 
      $scope.alertForm.emailAddressMulti = "";
      _.forEach($scope.alertForm.emailAddresses, function(email){
        $scope.alertForm.emailAddressMulti = $scope.alertForm.emailAddressMulti + email.email + "; ";          
       });
       
       $timeout(function(){
         $scope.showOptions({
           '$viewValue': $scope.alertForm.alertType
         });
       }, 250);             
    } else {
      $scope.title = "Add Alert";
       $scope.alertForm = {};
    }
    $scope.options = {};
    

    $scope.loadAlertTypes = function (lookup, entity, loader) {
      $scope.$emit('$TRIGGERLOAD', loader);

      Business.lookupservice.getLookupCodes(lookup, 'A').then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', loader);
        if (results) {
          $scope[entity] = results;
        }
      });
    };
    $scope.loadAlertTypes('AlertType', 'alertTypes', 'alertLoader');

    $scope.showOptions = function(option){
      if (option.$viewValue === 'USERD') {
        $scope.options.system=false;
        $scope.options.user=true;
      } else if (option.$viewValue === 'SYSERROR') {
        $scope.options.system=true;
        $scope.options.user=false;        
      } else {
        $scope.options.system=false;
        $scope.options.user=false;        
      }      
    };

    $scope.saveAlert = function() {
      
      $scope.$emit('$TRIGGERLOAD', 'alertLoader');

      //unpack emails
      var emails = $scope.alertForm.emailAddressMulti.split(";");
      $scope.alertForm.emailAddresses = [];
      _.forEach(emails, function(email){        
          email = email.trim();
          if (email !== "") {
            $scope.alertForm.emailAddresses.push({
              email: email
           });
         }
      });
      
      //Clear bad data
      delete $scope.alertForm.type;      
      if ($scope.alertForm.alertType === 'USERD') {
        delete $scope.alertForm.systemErrorAlertOption;
      } else if ($scope.alertForm.alertType === 'SYSERROR') {
        delete $scope.alertForm.userDataAlertOption;
      }

      Business.alertservice.saveAlert($scope.alertForm).then(function(results) {
        $scope.$emit('$TRIGGERUNLOAD', 'alertLoader');
        if (results) {
          triggerAlert('Saved successfully', 'alertId', 'body', 3000);
          $scope.$emit('$TRIGGEREVENT', '$REFRESH_ALERTS');            
          $uiModalInstance.dismiss('success');
        }
      }, function(failData){
        $scope.$emit('$TRIGGERUNLOAD', 'alertLoader');
        triggerAlert('Validation Error: <br> Make sure Email(s) are valid.', 'alertId', 'body', 3000);        
      }); 
    };

    $scope.close = function () {
      $uiModalInstance.dismiss('cancel');
    };

  }]);
