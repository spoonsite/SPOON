/* 
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
app.factory('lookupservice', ['$http', '$q', 'localCache', function($http, $q, localCache) {

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

  // var updateCache = function(name, value) {
  //   save(name, value);
  // };

  //Page Cache (on reload of page I want to reload the data)
  var refreshData = [];
  refreshData.userTypeCodes = true;

  /**
  * Load a lookup table
  * @param  entityName to lookup
  * @param  success function
  */
  var loadLookupTable = function(entityName, successFunc, errorFunc) {
    $http.get('api/v1/resource/lookuptypes/' + entityName + '/view').success(successFunc).error(errorFunc);
  };


  var getUserTypeCodes = function() {
    var deferred = $q.defer();

    if (refreshData.userTypeCodes) {
      localCache.clear('UserTypeCode');
      refreshData.userTypeCodes = false;
    }

    var userTypeCodes = checkExpire('UserTypeCode', minute * 1440);
    if (userTypeCodes) {
      deferred.resolve(userTypeCodes);
    } else {
      loadLookupTable('UserTypeCode', function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          save('UserTypeCode', data);
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }, function(data, status, headers, config){
        deferred.reject('There was an error');
      });
    }

    return deferred.promise;
  };


  
  var getEvalLevels = function() {
    var deferred = $q.defer();

    var evalLevels = checkExpire('evalLevels', minute * 0.5);
    if (evalLevels) {
      deferred.resolve(evalLevels);
    } else {
      $http.get('api/v1/resource/attributes/attributetypes/DI2ELEVEL/attributecodes').success(function(data, status, headers, config){
        if (data && data != "false" && isNotRequestError(data)) {
          removeError();
          save('evalLevels', data);
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config){
        deferred.reject('There was an error');
      });
    }

    return deferred.promise;
  };

  var getExpertise = function() {
    var deferred = $q.defer();
    var expertise = checkExpire('ExperienceTimeType', minute * 1440);
    if (expertise) {
      deferred.resolve(expertise);
    } else {
      loadLookupTable('ExperienceTimeType', function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          save('ExperienceTimeType', data);
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }, function(data, status, headers, config){
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  };

  var getReviewConList = function() {
    var deferred = $q.defer();
    var reviewCon = checkExpire('ReviewCon', minute * 1440);
    if (reviewCon) {
      deferred.resolve(reviewCon);
    } else {
      loadLookupTable('ReviewCon', function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          save('ReviewCon', data);
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }, function(data, status, headers, config){
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  };
  
  var getReviewProList = function() {
    var deferred = $q.defer();
    var reviewPro = checkExpire('ReviewPro', minute * 1440);
    if (reviewPro) {
      deferred.resolve(reviewPro);
    } else {
      loadLookupTable('ReviewPro', function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          save('ReviewPro', data);
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }, function(data, status, headers, config){
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  };

  //Public API
  return {
    getUserTypeCodes: getUserTypeCodes,
    getEvalLevels: getEvalLevels,
    getExpertise: getExpertise,
    getReviewConList: getReviewConList,
    getReviewProList: getReviewProList
  };

}]);
