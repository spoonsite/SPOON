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

app.controller('AdminJobsCtrl', ['$scope', 'business', function ($scope, Business) {
  
  $scope.jobs = {};
  $scope.schedulerStatus = {};
  $scope.tasks = {};
  $scope.showIntegrationJobs = false;
  $scope.predicate = [];
  $scope.reverse = [];
  
  $scope.setPredicate = function(predicate, table){
    if ($scope.predicate[table] === predicate){
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
  };  
  
  $scope.refreshJobs = function(showIntegration){
    $scope.$emit('$TRIGGERLOAD', 'jobLoader');       
    Business.jobservice.getJobs(showIntegration).then(function (results) {
      if (results) {          
        if (showIntegration === true) {
          $scope.jobs=results;                 
        } else {
          $scope.jobs = _.filter(results, function(item){
            return (item.jobName.indexOf('ComponentJob') !== 0);
          }); 
        }
      }  
      $scope.$emit('$TRIGGERUNLOAD', 'jobLoader');        
    });      
  };
  $scope.refreshJobs($scope.showIntegrationJobs);
  
  $scope.refreshSchedulerStatus = function(){
    Business.jobservice.getJobStatus().then(function (results) {
      $scope.schedulerStatus=results;
    });
  };
  $scope.refreshSchedulerStatus();
  
  $scope.refreshTasks = function(){
    $scope.$emit('$TRIGGERLOAD', 'taskLoader');       
    Business.jobservice.getTasksAndStatus().then(function (results) {
      if (results) {   
        $scope.tasks=results;
      }  
      $scope.$emit('$TRIGGERUNLOAD', 'taskLoader');        
    });         
  };
  $scope.refreshTasks();
  
  $scope.pauseResumeScheduler = function(pause){
    if (pause === true){      
      Business.jobservice.pauseScheduler().then(function (results) {
        $scope.refreshSchedulerStatus();
      });        
    } else {
      Business.jobservice.resumeScheduler().then(function (results) {
        $scope.refreshSchedulerStatus();
      });                
    }
  };
  
  $scope.pauseResumeJob = function(pause, jobName){
    if (pause === true){      
      Business.jobservice.pauseJob(jobName).then(function (results) {
        $scope.refreshJobs($scope.showIntegrationJobs);
      });        
    } else {
      Business.jobservice.resumeJob(jobName).then(function (results) {
        $scope.refreshJobs($scope.showIntegrationJobs);
      });                
    }      
  };
  
  $scope.runJobNow = function(jobname, groupname){
    Business.jobservice.runJobNow(jobname, groupname).then(function (results) {
      $scope.refreshJobs($scope.showIntegrationJobs);
    });
  };
  
  
    $scope.cancelTask = function (task) {
      var response = window.confirm("Are you sure you want CANCEL " + task.taskName + "?");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', 'taskLoader');
        Business.jobservice.cancelTask(task.taskId).then(function (results) {
          $scope.$emit('$TRIGGERUNLOAD', 'taskLoader');  
          $scope.refreshTasks();
        });        
      }
    };
  
  $scope.deleteTask = function (task){    
      var response = window.confirm("Are you sure you want DELETE " + task.taskName + "?");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', 'taskLoader');
        Business.jobservice.deleteTask(task.taskId).then(function (results) {
          $scope.$emit('$TRIGGERUNLOAD', 'taskLoader');
          $scope.refreshTasks();
        });
      }
  };  
  
  $scope.showError = function(task){
     if (task.showDetails) {
       task.showDetails = !task.showDetails;
     } else {
       task.showDetails = true;
     }     
  };  
  
}]);
