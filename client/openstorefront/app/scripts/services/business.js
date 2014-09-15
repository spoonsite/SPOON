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

app.factory('business', ['$rootScope','localCache', '$http', '$q', 'userservice', 'lookupservice', 'componentservice', 'highlightservice', function($rootScope, localCache, $http, $q, userservice, lookupservice, componentservice, highlightservice) { /*jshint unused: false*/

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

  var updateCache = function(name, value) {
    save(name, value);
  };

  //Services
  business.userservice = userservice;
  business.lookupservice = lookupservice;
  business.componentservice = componentservice;
  business.highlightservice = highlightservice;

  business.updateCache = function(name, value) {
    var deferred = $q.defer();
    updateCache(name, value);
    deferred.resolve(true);
    return deferred.promise;
  };

  business.getFilters = function() {
    var deferred = $q.defer();
    var filters = checkExpire('filters', minute * 1440);
    if (filters) {
      deferred.resolve(filters);
    } else {
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/attributes'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false') {
          save('filters', data);
          deferred.resolve(data);
        } else {
          deferred.reject('There was an error grabbing the filters');
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
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
        if (data && data !== 'false') {
          var tags = convertComponentTagsToTags(data);
          // console.log('tags', tags);
          save('tagsList', tags);
          deferred.resolve(tags);
        } else {
          deferred.reject('There was an error grabbing the tags list');
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
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
          if (collection) {
            save('typeahead', collection);
            deferred.resolve(collection);
          } else {
            deferred.reject('We need a new target in order to refresh the data');
          }
        } else {
          save('typeahead', collection);
          deferred.resolve(collection);
        }
      }
    }
    return deferred.promise;
  };




  return business;

}]);
