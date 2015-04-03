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

app.factory('highlightservice', [ 'localCache', '$http', '$q',function ( localCache, $http, $q) {

  // 60 seconds until expiration
  var minute = 60 * 1000;

  /***************************************************************
  * This function is used to check the localCache for the existance of a result
  * object that hasn't yet expired
  * params: name -- The unique identifier for the entry in the local cache (usually a string)
  * params: expire -- The ammount of time in ms that it will take for the object to expire
  * returns: result -- The value of the object if it has not yet expired, and null for
  *                    result objects that are no longer valid
  ***************************************************************/
  var checkExpire = function(name, expire) {
    var result = localCache.get(name, 'object');
    var cacheTime = null;
    if (result) {
      cacheTime = localCache.get(name+'-time', 'date');
      var timeDiff = new Date() - cacheTime;
      if (timeDiff < expire) {
        return result;
      } else {
        return null;
      }
    }
    return null;
  };

  /***************************************************************
  * We use this function in conjunction with the checkExpire function.
  * Use this function to save the value in the local cache (it will also save
  * an expire time that it can use later to check validity of an entry)
  * params: name -- The unique identifier for the entry in the local cache (usually a string)
  * params: value -- The value of the data that you will be storing
  ***************************************************************/
  var save = function(name, value) {
    localCache.save(name, value);
    localCache.save(name+'-time', new Date());
  };

  var updateCache = function(name, value) {
    save(name, value);
  };

  var highlights = {};

  highlights.getHighlights = function(all, override) {
    var deferred = $q.defer();
    var highlights = checkExpire('highlights', minute * 1440);
    if (highlights && !override) {
      deferred.resolve(highlights);
    } else {
      var params = {}
      if (all){
        params.all = true;
      }
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/highlights',
        'params': params
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          save('highlights', data);
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  }

  highlights.saveHighlight = function(highlight) {
    var deferred = $q.defer();
    if (highlight) {

      var method = highlight.highlightId? 'PUT': 'POST';
      var url = highlight.highlightId? 'api/v1/resource/highlights/' + encodeURIComponent(highlight.highlightId) : 'api/v1/resource/highlights';
      $http({
        'method': method,
        'url': url,
        'data': highlight
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data !== 'false' && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });
    } else {
      deferred.reject(false);
    }
    return deferred.promise;
  }

  highlights.getRecentlyAdded = function() {
    var deferred = $q.defer();
    var recentlyAdded = checkExpire('recentlyAdded', minute * 1440);
    if (recentlyAdded) {
      deferred.resolve(recentlyAdded);
    } else {
      $http({
        'method': 'GET',
        'url': 'api/v1/service/search/recent'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          save('recentlyAdded', data);
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  }

  highlights.activateHighlight = function(id) {
    var deferred = $q.defer();
    if (id) {
      $http({
        method: 'PUT',
        url: 'api/v1/resource/highlights/' + encodeURIComponent(id) + '/activate'
      }).success(function(data, status, headers, config){        
        if (isNotRequestError(data)){
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config){
        showServerError(data, 'body');
        deferred.reject(data);
      });
    } else {
      deferred.reject('There was no type...');
    }

    return deferred.promise;
  }

  highlights.deactivateHighlight = function(id) {
    var deferred = $q.defer();
    if (id) {
      $http({
        method: 'DELETE',
        url: 'api/v1/resource/highlights/' + encodeURIComponent(id) + '/deactivate'
      }).success(function(data, status, headers, config){        
        if (isNotRequestError(data)){
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config){
        showServerError(data, 'body');
        deferred.reject(data);
      });
    } else {
      deferred.reject('There was no type...');
    }

    return deferred.promise;
  }

  highlights.deleteHighlight = function(id) {
    var deferred = $q.defer();
    if (id) {
      $http({
        method: 'DELETE',
        url: 'api/v1/resource/highlights/' + encodeURIComponent(id) + '/delete'
      }).success(function(data, status, headers, config){        
        if (isNotRequestError(data)){
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config){
        showServerError(data, 'body');
        deferred.reject(data);
      });
    } else {
      deferred.reject('There was no type...');
    }

    return deferred.promise;
  }

  return highlights;
}]);
