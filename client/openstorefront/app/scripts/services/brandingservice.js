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

app.factory('brandingservice', [ 'localCache', '$http', '$q',function ( localCache, $http, $q) {

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

  var branding = {};
  var base = 'api/v1/resource/branding/';


  branding.getBrandingView = function(id, override) {
      
    console.log("Loading data:",id);
    var deferred = $q.defer();
    if (id) { 
      var url = base; 
      var found = checkExpire('brandingView_'+id, 2 * minute);
      if (found && !override){
        deferred.resolve(found);
      } else {
        $http({
          'method': 'GET',
          'url': url,
        }).success(function(data, status, headers, config) { /*jshint unused:false*/
            
          if (data && data !== 'false' && isNotRequestError(data)) {
            removeError();
            save('brandingView_'+id, data);
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
    } else {
      deferred.reject(false);
    }
    return deferred.promise;
  };

  branding.getTopicSearchItems = function(brandingId, override) {
    var deferred = $q.defer();
    if (brandingId) { 
      var url = base; 
      var found = checkExpire('topicSearchItems_' + brandingId, 2 * minute);
      if (found && !override){
        deferred.resolve(found.topicSearchViews);
      } else {
        $http({
          'method': 'GET',
          'url': url
        }).success(function(data, status, headers, config) { /*jshint unused:false*/
          if (data && data !== 'false' && isNotRequestError(data)) {
            removeError();
            save('topicSearchItems_' + brandingId, data);
            deferred.resolve(data.topicSearchViews);
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
    } else {
      deferred.reject(false);
    }
    return deferred.promise;
  };


  branding.getAllTopicSearchItems = function(override) {
    var deferred = $q.defer();
    var url = base; 
    var found = checkExpire('allTopicSearchItems', 2 * minute);
    if (found && !override){
      deferred.resolve(found);
    } else {
      $http({
        'method': 'GET',
        'url': url
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          save('allTopicSearchItems', data);
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
  };


  branding.getTopicSearchItemById = function(entityId, override) {
    var deferred = $q.defer();
    if (entityId) { 
      var url = base + 'topicSearchItems/' + encodeURIComponent(entityId); 
      var found = checkExpire('topicSearchItem_' + entityId, 2 * minute);
      if (found && !override){
        deferred.resolve(found);
      } else {
        $http({
          'method': 'GET',
          'url': url,
        }).success(function(data, status, headers, config) { /*jshint unused:false*/
          if (data && data !== 'false' && isNotRequestError(data)) {
            removeError();
            save('topicSearchItem_' + entityId, data);
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
    } else {
      deferred.reject(false);
    }
    return deferred.promise;
  }


  branding.addTopicSearchItem = function(items, brandingId, override) {
    var deferred = $q.defer();
    if (items && items.length) { 
      var url = base + encodeURIComponent(brandingId) + '/topicSearchItems' ; 
      $http({
        'method': 'POST',
        'url': url,
        'data': {'brandingId': brandingId, 'topicSearchItems': items}
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
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
  };


  branding.deleteTopicSearchItems = function(brandingId, override) {
    var deferred = $q.defer();
    if (items && items.length) { 
      var url = base + encodeURIComponent(brandingId) + '/topicSearchItems' ; 
      $http({
        'method': 'DELETE',
        'url': url,
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
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

  branding.deleteTopicSearchItem = function(entityId, override) {
    var deferred = $q.defer();
    if (items && items.length) { 
      var url = base + '/topicSearchItems' + encodeURIComponent(entityId); 
      $http({
        'method': 'DELETE',
        'url': url,
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
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

  return branding;
}]);
