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

app.factory('articleservice', ['$http', '$q', 'localCache', function($http, $q, localCache) {
  // default to 60 second expire time.
  var minute = 60 * 1000;
  var article = {};

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

  article.getArticle = function(type, code, override) {
    var deferred = $q.defer();
    var result = checkExpire('article', minute * 30);
    if (result && !override) {
      deferred.resolve(result);
    } else if (type && code){
      $http({
        method: 'GET',
        url: 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(type)+'/attributecodes/'+encodeURIComponent(code)+'/article'
      }).success(function(data, status, headers, config){
        if (data && data !== 'false' && isNotRequestError(data)){
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
      deferred.reject('You must include a type and a code to retreive it\'s article');
    }
    return deferred.promise;
  };

  article.getArticles = function(override, all) {
    var deferred = $q.defer();
    var result = checkExpire('all-articles', minute * 30);
    if (result && !override) {
      deferred.resolve(result);
    } else {
      var params = {};
      if (all){
        params.all = 'true';
      }
      $http({
        method: 'GET',
        url: 'api/v1/resource/attributes/attributetypes/articlecodes',
        params: params
      }).success(function(data, status, headers, config){
        if (data && data !== 'false' && isNotRequestError(data)){
          removeError();
          save('all-articles', data);
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
    }
    return deferred.promise;
  };
  
  article.getArchitecture = function(type){
    var deferred = $q.defer();

    $http({
      method: 'GET',
      url: 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(type)+'/architecture'
    }).success(function(data, status, headers, config){        
      if (data && data !== 'false' && isNotRequestError(data)){
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

    return deferred.promise;
  };  
  
  article.getType = function(type, view, all){
    var deferred = $q.defer();
    if (type) {
      var method = 'GET';
      var url = 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(type);
      var params = {}
      if (view) {
        params = {
          'view': view,
          'all': all
        }
      }
      $http({
        method: method,
        url: url,
        params: params
      }).success(function(data, status, headers, config){        
        if (data && data !== 'false' && isNotRequestError(data)){
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
      deferred.reject(false);
    }

    return deferred.promise;
  };  

  article.getTypes = function(filterQueryObj){
    var deferred = $q.defer();
    if (filterQueryObj) {
      var method = 'GET';
      var url = 'api/v1/resource/attributes/attributetypes';
      var params = {}
      if (view) {
        params = filterQueryObj
      }
      $http({
        method: method,
        url: url,
        params: params
      }).success(function(data, status, headers, config){        
        if (data && data !== 'false' && isNotRequestError(data)){
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
      deferred.reject(false);
    }

    return deferred.promise;
  };

  article.getCodeViews = function(type, filter){
    var deferred = $q.defer();
    if (type) {
      var method = 'GET';
      var url = 'api/v1/resource/attributes/attributetypes/'+ encodeURIComponent(type) + '/attributecodeviews?'+filter;
      $http({
        method: method,
        url: url,
      }).success(function(data, status, headers, config){        
        if (data && data !== 'false' && isNotRequestError(data)){
          deferred.resolve(data);
        } else {
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config){
        deferred.reject(data);
      });
    } else {
      deferred.reject(false);
    }

    return deferred.promise;
  };
  article.getCodes = function(type, filter){
    var deferred = $q.defer();
    if (type) {
      var method = 'GET';
      var url = 'api/v1/resource/attributes/attributetypes/'+ encodeURIComponent(type) + '/attributecodes?'+filter;
      $http({
        method: method,
        url: url,
      }).success(function(data, status, headers, config){        
        if (data && data !== 'false' && isNotRequestError(data)){
          deferred.resolve(data);
        } else {
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config){
        deferred.reject(data);
      });
    } else {
      deferred.reject(false);
    }

    return deferred.promise;
  };

  article.getCode = function(type, code, override){
    var deferred = $q.defer();
    if (type && code) {
      var method = 'GET';
      var url = 'api/v1/resource/attributes/attributetypes/'+ encodeURIComponent(type) + '/attributecodes/' + encodeURIComponent(code);
      $http({
        method: method,
        url: url,
      }).success(function(data, status, headers, config){        
        if (data && data !== 'false' && isNotRequestError(data)){
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
      deferred.reject(false);
    }

    return deferred.promise;
  };



  article.deactivateFilter = function(type){
    var deferred = $q.defer();
    if (type) {
      $http({
        method: 'DELETE',
        url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(type)
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
  
  article.deactivateCode = function(type, code){
    var deferred = $q.defer();
    if (type) {
      $http({
        method: 'DELETE',
        url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(type) + '/attributecodes/' + encodeURIComponent(code)
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
  
  article.saveCode = function(type, code, data, newCode){
    var deferred = $q.defer();
    
    if (type && data) {
      var url;
      var method;
      if (newCode || code !== data.code || !code) {
        data.attributeCodePk = {};
        data.attributeCodePk.attributeCode = data.code;
        data.attributeCodePk.attributeType = type;
        url = 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(type)+'/attributecodes';
        method = 'POST';
      } else {
        method = 'PUT';
        url = 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(type)+'/attributecodes/'+encodeURIComponent(code);
      }
      $http({
        method: method,
        url: url,
        data: data
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

  article.saveType = function(type, newType){
    var deferred = $q.defer();
    if (type) {
      var url;
      var method;
      if (newType) {
        method = 'POST';
        url = 'api/v1/resource/attributes/attributetypes/';
      } else {
        method = 'PUT';
        url = 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(type.attributeType);
      }
      $http({
        method: method,
        url: url,
        data: type
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

  article.saveSortOrder = function(attributeTypeView){
    var deferred = $q.defer();
    if (attributeTypeView) {
      var url;
      var method;
      method = 'PUT';
      url = 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(attributeTypeView.type)+'/sortorder';
      $http({
        method: method,
        url: url,
        data: attributeTypeView
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


  article.saveArticle = function(article){
    var deferred = $q.defer();
    if (article && article.attributeType && article.attributeCode) {
      var url;
      var method;
      method = 'PUT';
      url = 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(article.attributeType)+'/attributecodes/'+encodeURIComponent(article.attributeCode)+'/article';
      $http({
        method: method,
        url: url,
        data: article
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

  article.deleteArticle = function(article){
    var deferred = $q.defer();
    if (article && article.attributeType && article.attributeCode) {
      var url;
      var method;
      method = 'DELETE';
      url = 'api/v1/resource/attributes/attributetypes/'+encodeURIComponent(article.attributeType)+'/attributecodes/'+encodeURIComponent(article.attributeCode)+'/article';
      $http({
        method: method,
        url: url,
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

  article.previewArticle = function(article){
    var deferred = $q.defer();
    if (article && article.attributeType && article.attributeCode) {
      $http({
        method: 'POST',
        url: 'Article.action?Preview&attributeType='+encodeURIComponent(article.attributeType)+'&attributeCode='+encodeURIComponent(article.attributeCode),
        data: $.param({html: article.html}),
        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
      }).success(function(data, status, headers, config){
        deferred.resolve(data);
      }).error(function(data, status, headers, config){
        deferred.reject(false);
      })
    }
    return deferred.promise;
  }

  article.activateFilter = function(type) {
    var deferred = $q.defer();
    if (type) {
      $http({
        method: 'POST',
        url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(type)
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

  article.activateCode = function(type, code) {
    var deferred = $q.defer();
    if (type) {
      $http({
        method: 'POST',
        url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(type) + '/attributecodes/' + encodeURIComponent(code)
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

  return article;
}]);
