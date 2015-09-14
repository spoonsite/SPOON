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

app.controller('userManageReports',['$scope','business', '$uiModal', '$timeout', '$q', function ($scope, Business, $uiModal, $timeout, $q) {

  $scope.predicate = [];
  $scope.reverse = [];
  $scope.selectedRows = [];


  $scope.setPredicate = function (predicate, table) {
    if ($scope.predicate[table] === predicate) {
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
    if (table === 'report') {
      $scope.pagination.control.changeSortOrder(predicate);
    }
  };  

  $scope.statusFilterOptions = [
  {code: 'A', desc: 'Active'},
  {code: 'I', desc: 'Inactive'}
  ];

  $scope.reportFilter = angular.copy(utils.queryFilter);   
  $scope.reportFilter.status = $scope.statusFilterOptions[0].code;
  $scope.reportFilter.sortField = 'createDts';
  $scope.reportFilter.sortOrder = "ASC";

  $scope.reportScheduledFilter = angular.copy(utils.queryFilter);   
  $scope.reportScheduledFilter.status = $scope.statusFilterOptions[0].code;           

  $scope.data = {};
  $scope.data.reports = {};
  $scope.data.reports.data = [];

  $scope.scheduledReports = [];
  $scope.pagination = {};
  $scope.pagination.control;
  $scope.pagination.features = {'dates': true, 'max': true};

  $scope.refreshReports = function(){
    if ($scope.pagination.control && $scope.pagination.control.refresh) {
      $scope.$emit('$TRIGGERLOAD', 'reportLoader');
      $scope.pagination.control.refresh().then(function(){
        $scope.$emit('$TRIGGERUNLOAD', 'reportLoader');
      });
    }
    $scope.selectedRows = [];
  };

  $scope.refreshScheduledReports = function(){
    $scope.$emit('$TRIGGERLOAD', 'reportLoader');

    Business.reportservice.getScheduledReports($scope.reportScheduledFilter).then(function (results) {
      if (results) {
        $scope.scheduledReports = results;
      }
      $scope.$emit('$TRIGGERUNLOAD', 'reportLoader');
    });        
  };
  $scope.refreshScheduledReports();

  $scope.$on('$REFRESH_REPORTS', function(){ 
    $scope.refreshReports();
    $scope.refreshScheduledReports();
  });     

  $scope.deleteReport = function(report){
    var response = window.confirm("Are you sure you want DELETE "+ report.reportTypeDescription + " report?");
    if (response) {
      $scope.$emit('$TRIGGERLOAD', 'reportLoader');
      Business.reportservice.removeReport(report.reportId).then(function (result) {          
        $scope.$emit('$TRIGGERUNLOAD', 'reportLoader');
        $scope.refreshReports();
      });
    }       
  };

  $scope.deleteMultiple = function(){
    var response = window.confirm("Are you sure you want DELETE these reports?");
    if (response) {
      $scope.$emit('$TRIGGERLOAD', 'reportLoader');
      Business.reportservice.removeReports($scope.selectedRows).then(function (result) {          
        $scope.$emit('$TRIGGERUNLOAD', 'reportLoader');
        $scope.refreshReports();
      });
    } 
  };


  $scope.sheduleMode = function(mode){
    $scope.sheduleFlag = mode;
  };

  $scope.newReport = function(){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/application_management/editReport.html',
      controller: 'AdminEditReportCtrl',
      backdrop: 'static',
      size: 'sm',
      resolve: {
        report: function () {
          return null;
        },
        sheduleFlag: function () {
          return $scope.sheduleFlag;
        }
      }
    });       
  };

  $scope.editScheduledReport = function(report){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/application_management/editReport.html',
      controller: 'AdminEditReportCtrl',
      backdrop: 'static',
      size: 'sm',
      resolve: {
        report: function () {
          return report;
        },
        sheduleFlag: function () {
          return $scope.sheduleFlag;
        }
      }
    });         
  };

  $scope.toggleScheduledStatus = function(report){
    $scope.$emit('$TRIGGERLOAD', 'reportLoader');
    if (report.activeStatus === 'A') {
      Business.reportservice.inactivateScheduledReport(report.scheduleReportId).then(function (results) {
        $scope.refreshScheduledReports();
        $scope.$emit('$TRIGGERUNLOAD', 'reportLoader');
      });        
    } else {
      Business.reportservice.activateScheduledReport(report.scheduleReportId).then(function (results) {
        $scope.refreshScheduledReports();
        $scope.$emit('$TRIGGERUNLOAD', 'reportLoader');
      });        
    }      
  };

  $scope.deleteScheduledReport = function(report){
    var response = window.confirm("Are you sure you want DELETE "+ report.reportTypeDescription + " scheduled report?");
    if (response) {
      $scope.$emit('$TRIGGERLOAD', 'reportLoader');
      Business.reportservice.removeScheduledReport(report.scheduleReportId).then(function (result) {          
        $scope.$emit('$TRIGGERUNLOAD', 'reportLoader');
        $scope.refreshScheduledReports();
      });
    }        
  };

  $scope.download = function(report){
    if (report.runStatus === 'C') {
      window.location.href = "api/v1/resource/reports/" + report.reportId + "/report";
    } else {
      triggerAlert('Unable to download.  Report has not been completed or has failed.', 'reportId', 'body', 6000);
    }
  };

  $scope.translateTimePeriod = function(days) {
    if (days === 1) {
      return "Daily";
    } else if (days === 7) {
      return "Weekly";
    } else if (days === 28) {
      return "Monthly";
    } else {
      return days;
    }
  };



  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);

  var stickThatTable = function(){
    var offset = $('.top').outerHeight() + $('#editArticlesToolbar').outerHeight();
    $(".stickytable").stickyTableHeaders({
      fixedOffset: offset
    });
  }

  $(window).resize(stickThatTable);
  $timeout(stickThatTable, 100);

}]);

