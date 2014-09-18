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



  componentservice.deleteProsandCons = function(id, reviewId) {
    var result = $q.defer();
    if (id && reviewId && pro)
    {
      var url = 'api/v1/resource/components/'+id+'/review/'+reviewId+'/pro';
      $http({
        method: 'DELETE',
        url: url,
        data: "test"
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
      });
      url = 'api/v1/resource/components/'+id+'/review/'+reviewId+'/con';
      $http({
        method: 'DELETE',
        url: url,
        data: "test"
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
      });
    } else{
      result.reject('A unique ID and pro object is required to save a component pro');
    }
    return result.promise;
  }

  componentservice.deleteQuestion = function(id, questionId) {
    var result = $q.defer();
    if (id && questionId)
    {
      var url = 'api/v1/resource/components/'+id+'/question/'+questionId;
      $http({
        method: 'DELETE',
        url: url,
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        result.resolve(data);
      });
    } else{
      result.reject('Either a unique ID or question object were missing, and the question was not saved');
    }
    return result.promise;
  }

  componentservice.saveQuestion = function(id, post, questionId) {
    var result = $q.defer();
    if (id && post)
    {
      var url;
      var methodString; 
      if (questionId){
        methodString = 'PUT';
        url = 'api/v1/resource/components/'+id+'/question/'+questionId;
      } else {
        methodString = 'POST';
        url = 'api/v1/resource/components/'+id+'/question';
      }
      $http({
        method: methodString,
        url: url,
        data: post
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        result.resolve(data.questionId);
      });
    } else{
      result.reject('Either a unique ID or question object were missing, and the question was not saved');
    }
    return result.promise;
  }


  componentservice.deleteResponse = function(id, responseId) {
    var result = $q.defer();
    if (id && responseId)
    {
      var url = 'api/v1/resource/components/'+id+'/response/'+responseId;
      $http({
        method: 'DELETE',
        url: url,
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        result.resolve(data.responseId);
      });
    } else{
      result.reject('Either a unique ID or question object were missing, and the question was not saved');
    }
    return result.promise;
  }

  componentservice.saveResponse = function(id, questionId, post, responseId) {
    var result = $q.defer();
    if (id && questionId && post)
    {
      var url;
      var methodString;
      if (!responseId) {
        url = 'api/v1/resource/components/'+id+'/response/'+questionId;
        methodString = 'POST';
      } else {
        url = 'api/v1/resource/components/'+id+'/response/'+responseId;
        methodString = 'PUT';
      }
      $http({
        method: methodString,
        url: url,
        data: post
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        result.resolve(data);
      }).error(function(data, status, headers, config){
        console.log('data', data);
        console.log('headers', headers);
      });
    } else{
      result.reject('Either a unique ID, a quesitonID or a response object were missing, and the question was not saved');
    }
    return result.promise;
  }

  componentservice.deleteReview = function(id, reviewId) {
    var result = $q.defer();
    if (id && reviewId)
    {
      var url = 'api/v1/resource/components/'+id+'/review/'+reviewId;
      $http({
        method: 'DELETE',
        url: url,
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        result.resolve(data);
      });
    } else{
      result.reject('Either a unique ID or question object were missing, and the question was not saved');
    }
    return result.promise;
  }

  componentservice.saveReview = function(id, review, reviewId) {
    // console.log('id', id);
    // console.log('review', review);
    // console.log('review', reviewId);

    var result = $q.defer();
    if (id && review)
    {
      var url;
      var methodString;
      if (!reviewId) {
        url = 'api/v1/resource/components/'+id+'/review';
        methodString = 'POST';
      } else {
        url = 'api/v1/resource/components/'+id+'/review/' + reviewId;
        methodString = 'PUT';
      }
      console.log('url', url);
      console.log('method', methodString);
      $http({
        method: methodString,
        url: url,
        data: review
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        console.log('Success data', data);
        result.resolve(data);
      }).error(function(data, status, headers, config){
        console.log('Failure headers', headers);
        console.log('data', data);
        result.resolve(data);
      });
    } else{
      result.reject('A unique ID and review object is required to save a component review');
    }
    return result.promise;
  }

  componentservice.saveReviewPros = function(id, reviewId, pro) {
    var result = $q.defer();
    if (id && reviewId && pro)
    {
      var url = 'api/v1/resource/components/'+id+'/review/'+reviewId+'/pro';
      $http({
        method: 'POST',
        url: url,
        data: pro
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        result.resolve(data);
      });
    } else{
      result.reject('A unique ID and pro object is required to save a component pro');
    }
    return result.promise;
  }

  componentservice.saveReviewCons = function(id, reviewId, con) {
    var result = $q.defer();
    if (id && reviewId && con)
    {
      var url = 'api/v1/resource/components/'+id+'/review/'+reviewId + '/con';
      $http({
        method: 'POST',
        url: url,
        data: con
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        result.resolve(data);
      });
    } else{
      result.reject('A unique ID and con object is required to save a component con');
    }
    return result.promise;
  }


  componentservice.getComponentDetails = function(id, override) {
    var result = $q.defer();
    if (id)
    {
      var url = 'api/v1/resource/components/'+id+'/detail';
      var value = null;
      // if they don't give me an ID I send them back the whole list.
      value = checkExpire('component_'+id, minute * 2);
      if (value && !override) {
        result.resolve(value);
      } else {
        $http({
          method: 'GET',
          url: url
        })
        .success(function(data, status, headers, config) { /*jshint unused:false*/
          console.log('data', data);
          
          if (data && !isEmpty(data)) {
            save('component_'+id, data);
          }
          result.resolve(data);
        });
      }
    } else{
      result.reject('A unique ID is required to retrieve component details');
    }
    return result.promise;
  };

  componentservice.getComponentList = function() {
    var result = $q.defer();
    var url = 'api/v1/resource/components';
    var value = null;
    // if they don't give me an ID I send them back the whole list.
    value = checkExpire('componentList', minute * 10);
    if (value) {
      result.resolve(value);
    } else {
      $http({
        method: 'GET',
        url: url
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && !isEmpty(data)) {
          save('componentList', data);
        }
        result.resolve(data);
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
      // $http.get('api/v1/resource/component/search/?type=' + type + '&key=' + key ).success(function(data, status, headers, config) { /*jshint unused:false*/
        $http.get('api/v1/resource/components').success(function(data, status, headers, config) { /*jshint unused:false*/
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


    // componentservice.getComponentDetailsByType = function(input) {
    //   return _.filter(MOCKDATA.assets.assets, function(item) {
    //     return item.type === input;
    //   });
    // };
    // componentservice.getScoreCard = function() {
    //   return MOCKDATA.scoreTable;
    // };
    // componentservice.getExternalDepend = function() {
    //   return MOCKDATA.externalDepend;
    // };
    // componentservice.getLocalAssetArtifacts = function() {
    //   return MOCKDATA.localAssetArtifacts;
    // };
    // componentservice.getComponentVitals = function() {
    //   return MOCKDATA.componentVitals;
    // };
    // componentservice.getPointsContact = function() {
    //   return MOCKDATA.pointsContact;
    // };
    // componentservice.getComponentSummary = function() {
    //   return MOCKDATA.componentSummary;
    // };
    // componentservice.getComponentEvalProgressBar = function() {
    //   return MOCKDATA.componentEvalProgressBar;
    // };
    // componentservice.getComponentState = function() {
    //   return MOCKDATA.componentState;
    // };
    // componentservice.getComponentEvalProgressBarDates = function() {
    //   return MOCKDATA.componentEvalProgressBarDates;
    // };
    // componentservice.getResultsComments = function() {
    //   return MOCKDATA.resultsComments;
    // };


    componentservice.saveTags = function(id, tags) {
      var deferred = $q.defer();
      $http.delete('api/v1/resource/components/'+id+'/tags');
      $http({
        method: 'POST',
        url: 'api/v1/resource/components/'+id+'/tags',
        data: tags
      }).success(function(data, status, headers, config){
        if (data && data !== 'false') {
          deferred.resolve(data);
        }
      }).error(function(data, status, headers, config){
        deferred.resolve('There was an error saving the tags');
      });
      return deferred.promise;
    };

    return componentservice;
  }]);
