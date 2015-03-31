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

app.factory('mediaservice', ['$http', '$q', 'localCache', function($http, $q, localCache) {
    
    var mediaservice = {};
    
    mediaservice.getGeneralMedia = function () {
      var deferred = $q.defer();

      $http({
        'method': 'GET',
        'url': 'api/v1/resource/generalmedia'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });

      return deferred.promise;
    }; 

    mediaservice.getGeneralMediaLookup = function () {
      var deferred = $q.defer();

      $http({
        'method': 'GET',
        'url': 'api/v1/resource/generalmedia/lookup'
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };

    mediaservice.removeMedia = function (name) {
      var deferred = $q.defer();

      $http({
        'method': 'DELETE',
        'url': 'api/v1/resource/generalmedia/' + encodeURIComponent(name)
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });

      return deferred.promise;
    };  
    
    return mediaservice;
}]);
