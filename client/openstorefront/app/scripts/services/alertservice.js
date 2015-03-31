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

app.factory('alertservice', ['$http', '$q', 'localCache', function($http, $q, localCache) {
    
    var alertservice = {};
  
    alertservice.getAlerts = function (queryParamFilter) {
      var deferred = $q.defer();

      $http({
        'method': 'GET',
        'url': 'api/v1/resource/alerts?' + queryParamFilter.toQuery()
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/    
        deferred.reject('There was an error');
      });

      return deferred.promise;
    }; 
    
    alertservice.saveAlert = function (alert) {
      var deferred = $q.defer();
      
      if (alert.alertId) {
        $http({
          'method': 'PUT',
          'url': 'api/v1/resource/alerts/' + alert.alertId,
          data: alert
        }).success(function (data, status, headers, config) { /*jshint unused:false*/
          removeError();
          if (data && isNotRequestError(data)) {
            deferred.resolve(data);            
          } else {
            deferred.reject(data);
          }
        }).error(function (data, status, headers, config) { /*jshint unused:false*/
          deferred.reject('There was an error');
        });
      } else {
        $http({
          'method': 'POST',
          'url': 'api/v1/resource/alerts',
          data: alert
        }).success(function (data, status, headers, config) { /*jshint unused:false*/
          removeError();
          if (data && isNotRequestError(data)) {
            deferred.resolve(data);            
          } else {
            deferred.reject(data);
          }
        }).error(function (data, status, headers, config) { /*jshint unused:false*/
          deferred.reject('There was an error');
        });
      }

      return deferred.promise;
    };     
    
    
    alertservice.activateAlert = function (id) {
      var deferred = $q.defer();

      $http({
        'method': 'POST',
        'url': 'api/v1/resource/alerts/' + id + '/activate'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/    
        deferred.reject('There was an error');
      });

      return deferred.promise;
    }; 
    
    alertservice.inactivateAlert = function (id) {
      var deferred = $q.defer();

      $http({
        'method': 'DELETE',
        'url': 'api/v1/resource/alerts/' + id
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/    
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };     
    
    alertservice.removeAlert = function (id) {
      var deferred = $q.defer();

      $http({
        'method': 'DELETE',
        'url': 'api/v1/resource/alerts/' + id + '/force'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };      
  
    return alertservice;
}]);


