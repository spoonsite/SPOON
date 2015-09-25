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

 app.controller('AdminReportCtrl', ['$scope', 'business', '$rootScope', '$uiModal', function ($scope, Business, $rootScope, $uiModal) {

  $scope.predicate = [];
  $scope.reverse = [];
  $scope.selectedRows = [];
  $scope.business = Business;

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

}]);

app.controller('AdminEditReportCtrl', ['$scope', '$uiModalInstance', 'report', 'business', '$uiModal', '$filter', '$timeout', 'sheduleFlag',
  function ($scope, $uiModalInstance, report, Business, $uiModal, $filter, $timeout, sheduleFlag) {

    $scope.reportForm = angular.copy(report);
    $scope.actionText = 'Generate';
    $scope.email = {};    
    $scope.flag = {};
    $scope.flag.schedule = sheduleFlag || false;    
    $scope.options = {};   
    if (!report) {
      $scope.reportForm = {};
    }

    $scope.daysOptions = [];
    for (var i=1; i<29; i++){
      $scope.daysOptions.push({
        code: i,
        desc: i
      });
    }

    $scope.intervalOptions = [
    {code: 1, desc: 'Daily'},
    {code: 7, desc: 'Weekly'},
    {code: 28, desc: 'Monthly'}
    ];
    $scope.email.type = 'users';

    $scope.loadLookup = function (lookup, entity, loader) {
      $scope.$emit('$TRIGGERLOAD', loader);

      Business.lookupservice.getLookupCodes(lookup, 'A').then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', loader);
        if (results) {
          $scope[entity] = results;
        }
      });
    };
    $scope.loadLookup('ReportType', 'allReportTypes', 'reportFormLoader');
    

    $scope.loadReportTypes = function () {
      $scope.$emit('$TRIGGERLOAD', 'reportFormLoader');

      Business.reportservice.getReportTypes().then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
        if (results) {
          $scope.reportTypes = results;
        } else {
          $scope.reportTypes = [];
        }
      }, function(){
        $scope.reportTypes = [];
      });
    };
    $scope.loadReportTypes();

    $scope.getReportDesc = function(type){
      var found = _.find($scope.reportTypes, {'code': type});
      if (found) {
        return found.detailedDescription;
      } else {
        return type;
      }
    }

    $scope.loadReportFormat = function(reportTypeField){
      $scope.$emit('$TRIGGERLOAD', 'reportFormLoader');

      Business.reportservice.getReportFormats(reportTypeField.$viewValue).then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
        if (results) {
          $scope.reportFormats = results;
          if (results.length === 1){
            $scope.reportForm.reportFormat = results[0].code;
          }
        }
      }, function(){
        $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
      });
    };

    $scope.getCategories = function () {
      var filterQueryObj = angular.copy(utils.queryFilter);
      filterQueryObj.status = 'A';
      $scope.$emit('$TRIGGERLOAD', 'reportFormLoader');
      Business.articleservice.getTypes(filterQueryObj, true).then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
        if (results) {
          
          $scope.categories = results;
        }
      }, function(){
        $scope.categories = [];
        $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
      });
    };
    $scope.getCategories();

    $scope.showOptions = function(report){
      var found = _.find($scope.reportTypes, {'code': report.$viewValue});
      var option = {
        '$viewValue': report.$viewValue
      }
      console.log('found', found, option);
      
      if (option.$viewValue === 'USAGE') {
        $scope.options.useage=true;
        $scope.options.link=false;
        $scope.options.submission=false;
        $scope.options.category=false;
        $scope.options.ids = found? found.componentReport: false;
      }else if (option.$viewValue === 'SUBMISSION') {
        $scope.options.submission=true;
        $scope.options.useage=false;
        $scope.options.link=false;
        $scope.options.category=false;
        $scope.options.ids = found? found.componentReport: false;
      } else if (option.$viewValue === 'LINKVALID') {
        $scope.options.submission=false;
        $scope.options.useage=false;
        $scope.options.link=true;
        $scope.options.category=false;
        $scope.options.ids = found? found.componentReport: false;
      } else if (option.$viewValue === 'CATCOMP') {
        $scope.options.submission=false;
        $scope.options.useage=false;
        $scope.options.link=false;
        $scope.options.category=true;
        $scope.options.ids = found? found.componentReport: false;
      } else {
        $scope.options.submission=false;
        $scope.options.useage=false;
        $scope.options.link=false;
        $scope.options.category=false;
        $scope.options.ids = found? found.componentReport: false;
      }      
    };  
    
    $scope.checkWaitValue = function (value, form) {

      form.$invalid = false;
      form.$valid = true;
      removeError();
      if (value.$viewValue) {     
        if (value.$viewValue < 1) {
          form.$invalid = true;
          form.$valid = false;
          triggerError({
            errors: {
              entry: [{
                key: 'maxWaitSeconds',
                value: 'Value is out of range. (1-300)'
              }]
            },
            success: false
          }, true);                    
        } else if (value.$viewValue > 300) {
          form.$invalid = true;
          form.$valid = false;            
          triggerError({
            errors: {
              entry: [{
                key: 'maxWaitSeconds',
                value: 'Value is out of range. (1-300)'
              }]
            },
            success: false
          }, true);                        
        }
      }
    };
    
    $scope.setSchedule = function(){
      if ($scope.flag.schedule) {
        if (report) {
          $scope.actionText = 'Update Schedule';
        } else {
          $scope.actionText = 'Schedule';
        }
      } else {
        $scope.actionText = 'Generate';
      }
    };
    
    $scope.generateReport = function(){

      //custom validation
      if ($scope.flag.schedule) {
        if (!($scope.reportForm.scheduleIntervalDays)) {
          triggerAlert('Interval is required on a scheduled report', 'reportId', 'body', 3000);
          return;
        }
      }
      
      //check report options 
      if ($scope.reportForm.reportType === 'USAGE') {
        if ( (!($scope.reportForm.reportOption.startDts) && !($scope.reportForm.reportOption.endDts))
          &&  !($scope.reportForm.reportOption.previousDays)) {
          triggerAlert('A Start and End date or Previous Days must be set on this report.', 'reportId', 'body', 3000);
        return;
      }         
    }    


    $scope.$emit('$TRIGGERLOAD', 'reportFormLoader');

      //unpack emails
      if ($scope.reportForm.emailAddressMulti) {
        var emails = $scope.reportForm.emailAddressMulti.split(";");
        $scope.reportForm.emailAddresses = [];
        _.forEach(emails, function (email) {
          email = email.trim();
          if (email !== "") {
            $scope.reportForm.emailAddresses.push({
              email: email
            });
          }
        });
      }
      
      if ($scope.reportForm.reportOption) {
        if ($scope.reportForm.reportOption.startDts) {
          $scope.reportForm.reportOption.startDts = $filter('date')($scope.reportForm.reportOption.startDts, "yyyy-MM-dd'T'HH:mm:ss.sss");
        }
        if ($scope.reportForm.reportOption.endDts) {
          $scope.reportForm.reportOption.endDts = $filter('date')($scope.reportForm.reportOption.endDts, "yyyy-MM-dd'T'HH:mm:ss.sss");
        }
      }
      
      //Clear bad data
      delete $scope.reportForm.type;
      
      if ($scope.flag.schedule) {
        Business.reportservice.saveScheduledReport($scope.reportForm).then(function(results) {      
          $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
          if (results) {
            triggerAlert('Scheduled  Report', 'reportId', 'body', 3000);
            $scope.$emit('$TRIGGEREVENT', '$REFRESH_REPORTS');            
            $uiModalInstance.dismiss('success');
          }          
        }, function(failData) {
          $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
          triggerAlert('Validation Error: <br> Make sure Email(s) are valid', 'alertId', 'body', 3000);
        });        
      } else {
        console.log('$scope.reportForm', $scope.reportForm);
        
        var reportDataIds = [];
        _.forEach($scope.reportForm.ids, function(id){
          reportDataIds.push({
            id: id
          });
        });
        
        
        var reportView = {
          report: $scope.reportForm,
          reportDataId: reportDataIds
        };
        delete reportView.report.ids;
                
        Business.reportservice.generateReport(reportView).then(function(results) {      
          $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
          
          if (results) {
            triggerAlert('Generating Report', 'alertId', 'body', 3000);
            $scope.$emit('$TRIGGEREVENT', '$REFRESH_REPORTS');            
            $uiModalInstance.dismiss('success');
          }
          
        }, function(failData) {
          $scope.$emit('$TRIGGERUNLOAD', 'reportFormLoader');
          triggerAlert('Validation Error: <br> Make sure Email(s) are valid', 'alertId', 'body', 3000);
        });        
      }      
    };    
    
    $scope.close = function () {
      $uiModalInstance.dismiss('cancel');
    }; 
    
    
    if (report) {
      $scope.title = "Edit Scheduled Report";
      $scope.actionText = 'Update Schedule';
      $scope.flag.schedule = true;
      
      //Pack email 
      $scope.reportForm.emailAddressMulti = "";
      _.forEach($scope.reportForm.emailAddresses, function(email){
        $scope.reportForm.emailAddressMulti = $scope.reportForm.emailAddressMulti + email.email + "; ";          
      });

      $scope.loadReportFormat({
        '$viewValue' : $scope.reportForm.reportType
      });       

      $timeout(function(){      
       $scope.showOptions({
         '$viewValue' : $scope.reportForm.reportType
       });
     }, 250);             
    } else {
      $scope.title = "New Report";
    }


    
  }]);

