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

app.factory('systemservice', ['$http', '$q', 'localCache', function($http, $q, localCache) {
    
  var getErrorTickets = function(queryParamFilter) {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/errortickets?' + queryParamFilter.toQuery()
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  }; 
  
  var getHelp = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/help'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };   
  
  var getShowFeedback = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/service/application/showfeedback'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };     
  
  var getLogRecords = function(queryParamFilter) {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/service/application/logrecords?' + queryParamFilter.toQuery()
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };   
  
  var clearAllLogRecords = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'DELETE',
        'url': 'api/v1/service/application/logrecords'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };   
  
  var resetIndexer = function () {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/service/search/resetSolr'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
  };
  
  var getErrorTicketInfo = function(ticketId) {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/errortickets/' + encodeURIComponent(ticketId) + "/ticket" 
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  }; 
  
  var sendRecentChangesEmail = function (lastRunDts, emailAddress) {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/service/notification/recent-changes?lastRunDts=' + encodeURIComponent(lastRunDts) +"&emailAddress=" + encodeURIComponent(emailAddress)
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });

      return deferred.promise;
  }; 
  
  var getRecentChangeStatus = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/service/notification/recent-changes/status'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };  
  
  var getAppVersion = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'System.action?AppVersion'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };
  
  var getAppStatus = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/service/application/status'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  }; 
  
  var getConfigProperties = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/service/application/configproperties'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  }; 
  
  var getLogLevels = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/service/application/loglevels'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };
  
  var getLoggers = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/service/application/loggers'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };    
  
  var getThreads = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/service/application/threads'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };   
    
  var updateLogLevel = function(loggerName, level) {
    var deferred = $q.defer();
    
      $http({
        'method': 'PUT',
        'url': 'api/v1/service/application/logger/' + encodeURIComponent(loggerName) + '/level',
        data: level
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };
  
  var useDBLogging = function(use) {
    var deferred = $q.defer();
    
      $http({
        'method': 'PUT',
        'url': 'api/v1/service/application/dblogger/' + encodeURIComponent(use)      
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };  
  
  var getAppProperties = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/applicationproperties'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  }; 
  
  var updateAppProperty = function(key, value) {
    var deferred = $q.defer();
    
      $http({
        'method': 'PUT',
        'url': 'api/v1/resource/applicationproperties/' + encodeURIComponent(key),
        data: value
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };   
  
  var saveConfigProperty = function(data) {
    var deferred = $q.defer();
    
      $http({
        'method': 'POST',
        'url': 'api/v1/service/application/configproperties',
        data: data
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };  
  
  var removeConfigProperty = function(key) {
    var deferred = $q.defer();
    
      $http({
        'method': 'DELETE',
        'url': 'api/v1/service/application/configproperties/' + encodeURIComponent(key)
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };   
  
  //plugins 
  
  var getPlugins = function() {
    var deferred = $q.defer();
    
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/plugins' 
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };   
  
  var startPlugin = function(pluginId) {
    var deferred = $q.defer();
    
      $http({
        'method': 'POST',
        'url': 'api/v1/resource/plugins/'+pluginId +"/start" 
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };  
  
  var stopPlugin = function(pluginId) {
    var deferred = $q.defer();
    
      $http({
        'method': 'POST',
        'url': 'api/v1/resource/plugins/'+pluginId +"/stop" 
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };  
  
  var uninstallPlugin = function(pluginId) {
    var deferred = $q.defer();
    
      $http({
        'method': 'DELETE',
        'url': 'api/v1/resource/plugins/'+pluginId 
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.resolve(data);       
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    
    return deferred.promise;
  };    
    
  return {
      getErrorTickets: getErrorTickets,
      resetIndexer: resetIndexer,
      getErrorTicketInfo: getErrorTicketInfo,
      sendRecentChangesEmail: sendRecentChangesEmail,
      getRecentChangeStatus: getRecentChangeStatus,
      getAppVersion: getAppVersion,
      getAppStatus: getAppStatus,
      getConfigProperties: getConfigProperties,
      getLogLevels: getLogLevels,
      getThreads: getThreads,
      updateLogLevel: updateLogLevel,
      getAppProperties: getAppProperties,
      updateAppProperty: updateAppProperty,
      getLoggers: getLoggers,
      clearAllLogRecords: clearAllLogRecords,
      getHelp: getHelp,
      getShowFeedback: getShowFeedback,
      getPlugins: getPlugins,
      startPlugin: startPlugin,
      stopPlugin: stopPlugin,
      uninstallPlugin: uninstallPlugin,
	 saveConfigProperty: saveConfigProperty,
	 removeConfigProperty: removeConfigProperty,
	 useDBLogging: useDBLogging
  };
    
}]);


