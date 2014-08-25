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

/*global isEmpty, MOCKDATA2*/

app.factory('componentservice', ['$http', '$q', 'localCache', function($http, $q, localCache) {

  // default to 60 second expire time.
  var minute = 60 * 1000;
  var componentservice = {};

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

  componentservice.getComponentDetails = function(id) {
    var result = $q.defer();
    var url = '/api/v1/resource/component/';
    var value = null;
    // if they don't give me an ID I send them back the whole list.
    if (id) {
      url = url + id + '/';
      value = checkExpire('component_'+id, minute * 2);
    } else {
      value = checkExpire('componentList', minute * 2);
    }
    if (value) {
      result.resolve(value);
    } else {
      $http({
        method: 'GET',
        url: url
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        data.then(function(resultObj) {
          if (resultObj && !isEmpty(resultObj)) {
            if (id) {
              save('component_'+id, resultObj);
            } else {
              updateCache('componentList', resultObj);
            }
          }
          result.resolve(resultObj);
        });
      });
    }
    return result.promise;
  };

  componentservice.batchGetComponentDetails = function(list) {
    var result = $q.defer();
    // var url = '/api/v1/resource/component/list=?'
    var total = _.filter(MOCKDATA2.componentList, function(item){
      return _.some(list, function(id){
        return parseInt(id) === item.componentId;
      });
    });
    result.resolve(total);
    return result.promise;
  };

  componentservice.doSearch = function(type, key) {
    var deferred = $q.defer();
    if (type && key) {
      $http.get('/api/v1/resource/component/search/?type=' + type + '&key=' + key ).success(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.resolve(data);
      });
    }
    return deferred.promise;
  };


  componentservice.search = function(type, key, wait) {
    var deferred = $q.defer();
    var searchKey = checkExpire('searchKey', minute * 1440);
    if (searchKey) {
      deferred.resolve(searchKey);
    } else {
      if (!type && key) {
        save('searchKey', [{'key': 'search', 'code': key}]);
        searchKey = key;
        deferred.resolve(searchKey);
      } else if (type && key) {
        save('searchKey', [{'key': type, 'code': key}]);
        searchKey = key;
        deferred.resolve(searchKey);
      } else {
        deferred.reject('A key is required for a search!');
        searchKey = null;
      }
    }
    if (wait) {
      return deferred.promise;
    }
    return searchKey;
  };


  componentservice.getComponentDetailsByType = function(input) {
    return _.filter(MOCKDATA.assets.assets, function(item) {
      return item.type === input;
    });
  };
  componentservice.getScoreCard = function() {
    return MOCKDATA.scoreTable;
  };
  componentservice.getExternalDepend = function() {
    return MOCKDATA.externalDepend;
  };
  componentservice.getLocalAssetArtifacts = function() {
    return MOCKDATA.localAssetArtifacts;
  };
  componentservice.getComponentVitals = function() {
    return MOCKDATA.componentVitals;
  };
  componentservice.getPointsContact = function() {
    return MOCKDATA.pointsContact;
  };
  componentservice.getComponentSummary = function() {
    return MOCKDATA.componentSummary;
  };
  componentservice.getComponentEvalProgressBar = function() {
    return MOCKDATA.componentEvalProgressBar;
  };
  componentservice.getComponentState = function() {
    return MOCKDATA.componentState;
  };
  componentservice.getComponentEvalProgressBarDates = function() {
    return MOCKDATA.componentEvalProgressBarDates;
  };
  componentservice.getResultsComments = function() {
    return MOCKDATA.resultsComments;
  };
  componentservice.saveTags = function(id, tags) {
    var a = _.find(MOCKDATA2.componentList, {'componentId': id});
    componentservice.updateTagCloud(tags);
    if (a) {
      a.tags = tags;
    }
    return true;
  };
  componentservice.updateTagCloud = function(tags) {
    _.each(tags, function(tag) {
      if (!_.contains(MOCKDATA.tagsList, tag.text)) {
        MOCKDATA.tagsList.push(tag.text);
      }
    });
    MOCKDATA.tagsList.sort();
  };





  return componentservice;
}]);
