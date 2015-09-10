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

  $scope.predicate = 'title';
  $scope.reverse = false;
  $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminArticlesEdit');
  $scope.selectedTypes = [];

  // /***************************************************************
  // * If we don't have a landing page, we're going to set up one for now so that
  // * there will always be one in the editor when we look, unless we click on a button
  // * that says 'add landing page'
  // ***************************************************************/


  $scope.clearSort = function() {
    $scope.predicate = 'title'; 
    $scope.reverse = false;
    if(!$scope.$$phase) {
      $scope.$apply();
    }
  }

  $scope.setPredicate = function (predicate) {
    if ($scope.predicate === predicate) {
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = false;
    }
  };

  $scope.export = function(){
    document.exportForm.submit();
  };

  $scope.editContent = function(type, code){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/data_management/editlandingform.html',
      controller: 'AdminEditLandingCtrl',
      size: 'lg',
      backdrop: 'static',
      resolve: {
        type: function () {
          return type;
        },
        code: function () {
          return code;
        }
      }
    });

    modalInstance.result.then(function (result) {
      $scope.getArticles(true);
    }, function (result) {
      $scope.getArticles(true);
    });
  }
  


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

  $scope.reports = {};
  $scope.reports.data = [];
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

app.controller('AdminEditLandingCtrl',['$scope', '$uiModalInstance', 'type', 'code', 'business', '$location', '$q', '$timeout', function ($scope, $uiModalInstance, type, code, Business, $location, $q, $timeout) {

  $scope.editor = {};
  $scope.editor.editorContent = '';
  $scope.editor.editorContentWatch;
  $scope.showEditor = false;

  
  var popupWin;
  $scope.editorOptions = getCkConfig();

  $scope.ok = function () {
    $scope.article.html = $scope.getEditorContent();
    $scope.article.attributeType = $scope.type.type;
    $scope.article.attributeCode = $scope.code.code;
    if ($scope.article && $scope.article.html && $scope.article.attributeType && $scope.article.attributeCode) {
      Business.articleservice.saveArticle($scope.article).then(function(result){
        $uiModalInstance.close();
      }, function(){
        triggerAlert('There was an error saving your article, please try again.', 'articleAlert', 'articleEditModal', 6000);
      })
    } else {
      triggerAlert('There is missing information required for saving the article, please try again.', 'articleAlert', 'articleEditModal', 6000);
    }
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };

}]);

