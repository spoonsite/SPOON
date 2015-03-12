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

app.factory('business', ['$rootScope','localCache', '$http', '$q', 'userservice', 'lookupservice', 'componentservice', 'highlightservice', 'articleservice', 'configurationservice', 'jobservice', 'systemservice', 'mediaservice', 'trackingservice',
  function($rootScope, localCache, $http, $q, userservice, lookupservice, componentservice, highlightservice, articleservice, configurationservice, jobservice, systemservice, mediaservice, trackingservice) { /*jshint unused: false*/

  // 60 seconds until expiration
  var minute = 60 * 1000;
  var business = {};

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

  var get = function(key) {
    return localCache.get(key, 'object');
  };

  var updateCache = function(name, value) {
    save(name, value);
  };

  //Services
  business.userservice = userservice;
  business.lookupservice = lookupservice;
  business.componentservice = componentservice;
  business.highlightservice = highlightservice;
  business.articleservice = articleservice;
  business.configurationservice = configurationservice;
  business.jobservice = jobservice;
  business.systemservice = systemservice;
  business.mediaservice = mediaservice;
  business.trackingservice = trackingservice;


  business.updateCache = function(name, value) {
    var deferred = $q.defer();
    updateCache(name, value);
    deferred.resolve(true);
    return deferred.promise;
  };

  business.getFilters = function(override, getAll) {
    var deferred = $q.defer();
    var filters;
    if (getAll) {
      filters = checkExpire('All-filters', minute * 1440);
    } else {
      filters = checkExpire('filters', minute * 1440);
    }
    if (filters && !override) {
      deferred.resolve(filters);
    } else {
      var params = {'all': !!getAll};  
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/attributes',
        'params': params
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          if (getAll) {
            save('All-filters', data);
          } else {
            save('filters', data);
          }
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject('There was an error grabbing the filters');
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error grabbing the filters');
      });
    }
    return deferred.promise;
  };


  var convertComponentTagsToTags = function(tags){
    var result = [];
    _.each(tags, function(tag){
      result.push(tag.text);
    })
    return result;
  }


  business.getTagsList = function(override) {
    var deferred = $q.defer();
    var tagsList = checkExpire('tagsList', minute * 0.5);
    if (tagsList && !override) {
      deferred.resolve(tagsList);
    } else {
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/components/tags'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          var tags = convertComponentTagsToTags(data);
          // console.log('tags', tags);
          save('tagsList', tags);
          deferred.resolve(tags);
        } else {
          removeError();
          triggerError(data);
          deferred.reject('There was an error grabbing the tags list');
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error grabbing the tags list');
      });
    }

    return deferred.promise;
  };



  business.getProsConsList = function() {
    var deferred = $q.defer();
    var result = {};

    business.lookupservice.getReviewConList().then(function(cons){
      business.lookupservice.getReviewProList().then(function(pros){
        result.pros = [];
        result.cons = [];
        _.each(pros, function(pro){
          result.pros.push(pro.description);
        })
        _.each(cons, function(con){
          result.cons.push(con.description);
        })
        deferred.resolve(result);
      })
    })
    return deferred.promise;
  };



  business.landingPage = function(key, value, wait) {
    var deferred = $q.defer();
    if (key && value) {
      updateCache('landingRoute', {'key': key, 'value': value});
    }
    var landingRoute = checkExpire('landingRoute', minute * 1440);
    if (landingRoute) {
      deferred.resolve(landingRoute);
    } else {
      if (!key && !value) {
        deferred.reject('You must include a key and/or value');
      } else{
        save('landingRoute', {'key': key, 'value': value});
        landingRoute = key;
        deferred.resolve(landingRoute);
      }
    }
    if (wait) {
      return deferred.promise;
    }
    return landingRoute;
  };

  // This function builds the typeahead options.
  business.typeahead = function(collection, pluckItem) {
    var deferred = $q.defer();
    // lets refresh the typeahead every 15 min until we actually get this
    // working with a http request upon user interaction.
    var result = checkExpire('typeahead', minute * 15);
    if (result) {
      deferred.resolve(result);
    } else{
      if (!collection) {
        deferred.reject('We broke it!');
      } else {
        if (pluckItem !== undefined && pluckItem !== null) {
          collection = _.pluck(collection, pluckItem);
          if (collection && isNotRequestError(collection)) {
            removeError();
            save('typeahead', collection);
            deferred.resolve(collection);
          } else {
            removeError();
            triggerError(collection);
            deferred.reject('We need a new target in order to refresh the data');
          }
        } else if (isNotRequestError(collection)) {
          save('typeahead', collection);
          deferred.resolve(collection);
        }
      }
    }
    return deferred.promise;
  };


  var checkVersion = function (userAgent){
    if (userAgent && userAgent.family === 'IE'){
      if (userAgent.versionNumber && userAgent.versionNumber.major) {
        // console.log('version number', userAgent.versionNumber.major);
        if (parseInt(userAgent.versionNumber.major) < 9){
          return true;
        }
      }
    }
    return false;    
  }

  // This function builds the typeahead options.
  business.ieCheck = function() {
    var deferred = $q.defer();
    $http.get('System.action?UserAgent').success(function(data, status, headers, config){
      deferred.resolve(checkVersion(data));
    }).error(function(){
      deferred.resolve(false);
    });

    return deferred.promise;
  };


  business.saveLocal = function(key, value){
    save(key, value);
  }

  business.getLocal = function(key){
    if (key) {
      var result = get(key);
      return result? result: null;
    } else {
      return null;
    }
  }
  business.get = function(query, override) {
    var deferred = $q.defer();
    if (query) { 
      var url = query.url + '?' + query.filterObj.toQuery();
      $http({
        'method': 'GET',
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




  return business;

}]);
