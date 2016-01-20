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

 app.factory('notificationservice', ['$http', '$q', 'localCache', 'userservice', function($http, $q, localCache, userservice) {

  var notification = {};
  
  var dbase = 'api/v1/resource/notificationevent';
  var base = 'api/v1/resource/notificationevent/';


  notification.getUserEvents = function (queryParamFilter) {
    var deferred = $q.defer();
    queryParamFilter = queryParamFilter || angular.copy(utils.queryFilter);
    if (queryParamFilter){
      $http({
        'method': 'GET',
        'url': dbase +'?'+ queryParamFilter.toQuery()
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          deferred.resolve(data);            
        } else {
          deferred.reject(data);
        }
      }).error(function (data, status, headers, config) { /*jshint unused:false*/    
        deferred.reject(false);
      });
    } else {
      deferred.reject(false);
    }

    return deferred.promise;
  }; 

  notification.putNewEvent = function (eventId) {
    var deferred = $q.defer();
    userservice.getCurrentUserProfile().then(function(user){
      var notificationEventReadStatus = {
        'eventId': eventId,
        'username': user.username   
      }

      if (notificationEventReadStatus.eventId) {
        $http({
          'method': 'PUT',
          'url': base + encodeURIComponent(notificationEventReadStatus.eventId),
          'data': notificationEventReadStatus
        }).success(function (data, status, headers, config) { /*jshint unused:false*/
          if (data && isNotRequestError(data)) {
            deferred.resolve(data);            
          } else {
            deferred.reject(data);
          }
        }).error(function (data, status, headers, config) { /*jshint unused:false*/
          deferred.reject(false);
        });
      } else {
        deferred.reject(false);
      }
    }, function(){
      deferred.reject(false);
    })
    return deferred.promise;
  }; 

  notification.deleteNewEvent = function (eventId) {
    var deferred = $q.defer();
    userservice.getCurrentUserProfile().then(function(user){
      if (eventId && user && user.username) {
        $http({
          'method': 'DELETE',
          'url': base + encodeURIComponent(eventId) + '/' + encodeURIComponent(user.username)
        }).success(function (data, status, headers, config) { /*jshint unused:false*/
          if (data && isNotRequestError(data)) {
            deferred.resolve(data);            
          } else {
            deferred.reject(data);
          }
        }).error(function (data, status, headers, config) { /*jshint unused:false*/
          deferred.reject(false);
        });
      } else {
        deferred.reject(false);
      }
    }, function(){
      deferred.reject(false);
    })
    return deferred.promise;
  };   

  notification.deleteEvent = function (eventId) {
    var deferred = $q.defer();
    if (eventId) {
      $http({
        'method': 'DELETE',
        'url': base + encodeURIComponent(eventId)
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          deferred.resolve(data);            
        } else {
          deferred.reject(data);
        }
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        if (status === 204) {
          deferred.resolve(true);
        } else {
          deferred.reject(false);
        }
      });
    } else {
      deferred.reject(false);
    }
    return deferred.promise;
  };          

  return notification;
}]);


