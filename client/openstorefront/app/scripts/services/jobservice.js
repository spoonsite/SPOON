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

app.factory('jobservice', ['$http', '$q', 'localCache', function($http, $q, localCache) {
    

    var getJobStatus = function () {
      var deferred = $q.defer();

      $http({
        'method': 'GET',
        'url': 'api/v1/service/jobs/status'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    }; 
    
    var getJobs = function () {
      var deferred = $q.defer();

      $http({
        'method': 'GET',
        'url': 'api/v1/service/jobs'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };    
    
    var getJob = function (jobName) {
      var deferred = $q.defer();

      $http({
        'method': 'GET',
        'url': 'api/v1/service/jobs/' + jobName
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };    
    
    var pauseScheduler = function () {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/service/jobs/pause'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };     
    
    var resumeScheduler = function () {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/service/jobs/resume'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };     

    var pauseJob = function (jobName) {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/service/jobs/' + jobName + '/pause'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    }; 
   
    var resumeJob = function (jobName) {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/service/jobs/' + jobName + '/resume'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };     
    
    var runJobNow = function (jobName, groupName) {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/service/jobs/' + jobName + '/' + groupName + '/runnow'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };     
    
    
    var getTasksAndStatus = function () {
      var deferred = $q.defer();

      $http({
        'method': 'GET',
        'url': 'api/v1/service/jobs/tasks/status'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };    
        
    var cancelTask = function (taskId) {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/service/jobs/tasks/' + taskId + '/cancel' 
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
    }; 
    
    
    return {
      getJobStatus: getJobStatus,
      getJobs: getJobs,
      getJob: getJob,
      pauseScheduler: pauseScheduler,
      resumeScheduler: resumeScheduler,
      pauseJob: pauseJob,
      resumeJob: resumeJob,
      runJobNow: runJobNow,
      getTasksAndStatus: getTasksAndStatus,
      cancelTask: cancelTask      
    };
    
}]);


