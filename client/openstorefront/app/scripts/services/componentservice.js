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
      var url = 'api/v1/resource/components/'+id+'/reviews/'+reviewId+'/pro';
      $http({
        method: 'DELETE',
        url: url,
        data: "test"
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
      });
      url = 'api/v1/resource/components/'+id+'/reviews/'+reviewId+'/con';
      $http({
        method: 'DELETE',
        url: url,
        data: "test"
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          result.resolve(data);
          removeError();
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was an error');
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
      var url = 'api/v1/resource/components/'+id+'/questions/'+questionId;
      $http({
        method: 'DELETE',
        url: url,
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)){
          result.resolve(data);
          removeError();
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config) {
        result.reject('There was an error');
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
        url = 'api/v1/resource/components/'+id+'/questions/'+questionId;
      } else {
        methodString = 'POST';
        url = 'api/v1/resource/components/'+id+'/questions';
      }
      $http({
        method: methodString,
        url: url,
        data: post
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data) {
          result.resolve(data.questionId);
          removeError();
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was an error');
      });
    } else{
      result.reject('Either a unique ID or question object were missing, and the question was not saved');
    }
    return result.promise;
  }


  componentservice.deleteResponse = function(id, responseId, questionId) {
    var result = $q.defer();
    if (id && responseId && questionId)
    {
      var url = 'api/v1/resource/components/'+id+'/questions/'+questionId+'/responses/'+responseId;
      $http({
        method: 'DELETE',
        url: url,
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          result.resolve(data.responseId);
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was an error');
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
        url = 'api/v1/resource/components/'+id+'/questions/'+questionId+'/responses';
        methodString = 'POST';
      } else {
        url = 'api/v1/resource/components/'+id+'/questions/'+questionId+'/responses/'+responseId;
        methodString = 'PUT';
      }
      $http({
        method: methodString,
        url: url,
        data: post
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          result.resolve(data);
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was an error');
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
      var url = 'api/v1/resource/components/'+id+'/reviews/'+reviewId;
      $http({
        method: 'DELETE',
        url: url,
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          result.resolve(data);
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was an error');
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
        url = 'api/v1/resource/components/'+id+'/reviews';
        methodString = 'POST';
      } else {
        url = 'api/v1/resource/components/'+id+'/reviews/' + reviewId;
        methodString = 'PUT';
      }
      $http({
        method: methodString,
        url: url,
        data: review
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)){
          removeError();
          result.resolve(data);
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was an error');
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
      var url = 'api/v1/resource/components/'+id+'/reviews/'+reviewId+'/pros';
      $http({
        method: 'POST',
        url: url,
        data: pro
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          result.resolve(data);
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was an error');
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
      var url = 'api/v1/resource/components/'+id+'/reviews/'+reviewId + '/cons';
      $http({
        method: 'POST',
        url: url,
        data: con
      })
      .success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)){
          removeError();
          result.resolve(data);
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was an error');
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
          if (data && !isEmpty(data) && isNotRequestError(data)) {
            removeError();
            // console.log('data', data);
            save('component_'+id, data);
            result.resolve(data);
          } else {
            removeError();
            triggerError(data);
            result.reject(false);
          }
        }).error(function(data, status, headers, config){
          result.reject('There was an error');
        });
      }
    } else{
      result.reject('A unique ID is required to retrieve component details');
    }
    return result.promise;
  };

  componentservice.getComponentPrint = function(id, override) {
    var result = $q.defer();
    if (id)
    {
      var url = 'api/v1/resource/components/'+id+'/detail?type=print';
      var value = null;
      // if they don't give me an ID I send them back the whole list.
      value = checkExpire('component_print_'+id, minute * 2);
      if (value && !override) {
        result.resolve(value);
      } else {
        $http({
          method: 'GET',
          url: url
        })
        .success(function(data, status, headers, config) { /*jshint unused:false*/          
          if (data && !isEmpty(data) && isNotRequestError(data)) {
            removeError();
            // console.log('data', data);
            save('component_print_'+id, data);
            result.resolve(data);
          } else {
            removeError();
            triggerError(data);
            result.reject(false);
          }
        }).error(function(data, status, headers, config){
          result.reject('There was an error');
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
        if (data && !isEmpty(data) && isNotRequestError(data)) {
          removeError();
          var resultList = [];
          _.each(data, function(item){
            var temp = item;
            temp.description = getShortDescription(item.description);
            resultList.push(temp);
          })
          save('componentList', resultList);
          result.resolve(resultList);
        } else {
          removeError();
          triggerError(data);
          result.reject(false);
        }
      }).error(function(data, status, headers, config){
        result.reject('There was a server error');
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

  componentservice.doSearch = function(type, key, architecture) {
    var result            = $q.defer();
    var url               = 'api/v1/service/search/all';
    var value             = null;
    var cachedComponents  = null;
    var name;

    // use this code if you want the client to handle the search.    
    // if (type && key) {
    //   type  = type.toLowerCase();
    //   if (type !== 'attribute'){
    //     key   = key.toLowerCase();
    //     name  = type + '-' + key;
    //   } else {
    //     if (!key.key) {
    //       name = type + key.type.toLowerCase() + '-ALL';
    //     } else {
    //       name = type + key.type.toLowerCase() + '-' + key.key.toLowerCase();
    //     }
    //   }
    //   // if they don't give me an ID I send them back the whole list.
    //   value = checkExpire(name, minute * 10);
    //   cachedComponents = checkExpire('cachedComponents', minute * 1440); // a day
    //   if (value) {
    //     result.resolve(value);
    //   } else {
    //     if (cachedComponents) {
    //       var temp = search({'type': type, 'key': key}, cachedComponents, architecture);
    //       save(name, temp);
    //       result.resolve(temp);
    //     } else {
    //       $http({
    //         method: 'GET',
    //         url: url
    //       })
    //       .success(function(data, status, headers, config) { /*jshint unused:false*/
    //         if (data && !isEmpty(data) && isNotRequestError(data)) {
    //           removeError();
    //           save('cachedComponents', data);
    //           var temp = search({'type': type, 'key': key}, data, architecture);
    //           save(name, temp);
    //           result.resolve(temp);
    //         } else {
    //           removeError();
    //           triggerError(data);
    //           result.reject(false);
    //         }
    //       }).error(function(data, status, headers, config){
    //         result.reject('There was a server error');
    //       });
    //     }
    //   }
    // } else {
    //   result.reject('You must provide a type and key for the search');
    // }

    // use this code if you want solr to handle the search.    
    if (type && key) {
      if (type !== 'search') {
        name = key.type + key.key;
      } else {
        name = type + key;
      }
      // if they don't give me an ID I send them back the whole list.
      value = checkExpire(name, minute * 10);
      cachedComponents = checkExpire(name, minute * 1440); // a day
      if (value) {
        result.resolve(value);
      } else {
        var paramsObj = {};
        if (type !== 'search') {
          url = 'api/v1/service/search/attribute/'+key.type+'/'+key.key;
        } else {
          url = 'api/v1/service/search';
          if (key.toLowerCase() === 'all' ){
            paramsObj.query = "*";
          } else {
            paramsObj.query = key;
          }
        }
        $http({
          method: 'GET',
          url: url,
          params: paramsObj
        })
        .success(function(data, status, headers, config) {
          if (data && !isEmpty(data) && isNotRequestError(data)) {
            removeError();
            var temp = {};
            temp.data = data;
            save(name, temp);
            result.resolve(temp);
          } else {
            removeError();
            triggerError(data);
            result.reject(false);
          }
        }).error(function(data, status, headers, config){
          result.reject('There was a server error');
        });
      }
    } else {
      result.reject('You must provide a type and key for the search');
    }


    return result.promise;
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

  componentservice.saveTags = function(id, tags) {
    var deferred = $q.defer();
    $http.delete('api/v1/resource/components/'+id+'/tags');
    $http({
      method: 'POST',
      url: 'api/v1/resource/components/'+id+'/tags/list',
      data: tags
    }).success(function(data, status, headers, config){
      if (data && data !== 'false' && isNotRequestError(data)) {
        removeError();
        deferred.resolve(data);
      } else {
        removeError();
        triggerError(data);
        deferred.reject(false);
      }
    }).error(function(data, status, headers, config){
      deferred.resolve('There was an error saving the tags');
    });
    return deferred.promise;
  };

  componentservice.addTag = function(id, tag) {
    var deferred = $q.defer();
    if (id && tag) {
      $http({
        method: 'POST',
        url: 'api/v1/resource/components/'+id+'/tags',
        data: tag
      }).success(function(data, status, headers, config){
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config){
        deferred.resolve('There was an error saving the tags');
      });
    } else {
      deferred.reject('You need a component Id and a tag to save.')
    }
    return deferred.promise;
  };

  componentservice.removeTag = function(id, tag) {
    var deferred = $q.defer();
    if (id && tag) {
      $http({
        method: 'DELETE',
        url: 'api/v1/resource/components/'+id+'/tags/text',
        headers: {
          'Content-Type': 'application/json'
        },
        data: tag
      }).success(function(data, status, headers, config){
        if (data !== 'false' && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config){
        deferred.reject(false);
      });
    } else {
      deferred.reject(false);
    }
    return deferred.promise;
  }
  return componentservice;
}]);
