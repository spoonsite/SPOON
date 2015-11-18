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

 app.factory('submissionservice', ['$http', '$q', 'localCache', function ($http, $q, localCache) {

  var minute = 60 * 1000;
  var submissionservice = {};

  var checkExpire = function (name, expire) {
    var result = localCache.get(name, 'object');
    var cacheTime = null;
    if (result) {
      cacheTime = localCache.get(name + '-time', 'date');
      var timeDiff = new Date() - cacheTime;
      if (timeDiff < expire) {
        return result;
      } else {
        return null;
      }
    }
    return null;
  };


  var save = function (name, value) {
    localCache.save(name, value);
    localCache.save(name + '-time', new Date());
  };


  var updateCache = function (name, value) {
    save(name, value);
  };

  submissionservice.getComponentDetails = function (id, override) {
    var result = $q.defer();
    if (id)
    {
      var url = 'api/v1/resource/components/' + encodeURIComponent(id) + '/detail';
      var value = null;
      value = checkExpire('component_' + id, minute * 2);
      if (value && !override) {
        result.resolve(value);
      } else {
        $http({
          method: 'GET',
          url: url
        })
        .success(function (data, status, headers, config) { /*jshint unused:false*/
          if (data && !isEmpty(data) && isNotRequestError(data)) {
            removeError();
            save('component_' + id, data);
            result.resolve(data);
          } else {
            removeError();
            triggerError(data);
            result.reject(false);
          }
        }).error(function (data, status, headers, config) {
          result.reject('There was an error');
        });
      }
    } else {
      result.reject('A unique ID is required to retrieve component details');
    }
    return result.promise;
  };

  submissionservice.getSubmission = function (id, override) {
    var result = $q.defer();
    if (id) {
      var url = 'api/v1/resource/componentsubmissions/' + encodeURIComponent(id);
      var value = checkExpire('componentSubmission_' + id, minute * 2);
      if (value && !override) {
        result.resolve(value);
      } else {
        $http({
          method: 'GET',
          url: url
        })
        .success(function (data, status, headers, config) { /*jshint unused:false*/
          if (data && !isEmpty(data) && isNotRequestError(data)) {
            removeError();
            save('componentSubmission_' + id, data);
            result.resolve(data);
          } else {
            removeError();
            triggerError(data);
            result.reject(false);
          }
        }).error(function (data, status, headers, config) {
          result.reject({data:data, status:status, headers:headers, config:config});
        });
      }
    } else {
      result.reject('A unique ID is required to retrieve component details');
    }
    return result.promise;
  };

  submissionservice.getSubmissions = function (id, override) {
    var result = $q.defer();
    if (id) {
      var url = 'api/v1/resource/componentsubmissions/';
      var value = checkExpire('componentSubmissions_' + id, minute * 2);
      if (value && !override) {
        result.resolve(value);
      } else {
        $http({
          method: 'GET',
          url: url
        })
        .success(function (data, status, headers, config) { /*jshint unused:false*/
          if (data && !isEmpty(data) && isNotRequestError(data)) {
            removeError();
            save('componentSubmissions_' + id, data);
            result.resolve(data);
          } else {
            removeError();
            triggerError(data);
            result.reject(false);
          }
        }).error(function (data, status, headers, config) {
          result.reject('There was an error');
        });
      }
    } else {
      result.reject('A unique ID is required to retrieve component details');
    }
    return result.promise;
  };


  submissionservice.forceRemoveEnity = function (options) {
    var deferred = $q.defer();

    $http({
      'method': 'DELETE',
      'url': 'api/v1/resource/componentsubmissions/' + encodeURIComponent(options.componentId) + '/' + encodeURIComponent(options.entity) + '/' + encodeURIComponent(options.entityId) + '/force'
    }).success(function (data, status, headers, config) { /*jshint unused:false*/
      if (data && isNotRequestError(data)) {
        removeError();
        deferred.resolve(data);
      } else {
        deferred.resolve(data);
      }
    }).error(function (data, status, headers, config) { /*jshint unused:false*/
      showServerError(data, 'body');
      deferred.reject('There was an error');
    });

    return deferred.promise;
  };    

  submissionservice.createSubmission = function (requiredForComponent) {
    var deferred = $q.defer();
    var method = 'POST';
    var url = 'api/v1/resource/componentsubmissions';
    if (requiredForComponent.component.componentId){
      method = 'PUT';
      url = 'api/v1/resource/componentsubmissions/'+encodeURIComponent(requiredForComponent.component.componentId);
    }
    $http({
      'method': method,
      'url': url,
      data: requiredForComponent
    }).success(function (data, status, headers, config) { /*jshint unused:false*/
      if (data && isNotRequestError(data)) {
        removeError();
        deferred.resolve(data);
      } else {
        deferred.resolve(data);
      }
    }).error(function (data, status, headers, config) { /*jshint unused:false*/
      showServerError(data, 'body');
      deferred.reject('There was an error');
    });

    return deferred.promise;
  }; 

  submissionservice.submit = function (componentId, reverse) {
    var deferred = $q.defer();
    if (componentId){
      var method = 'PUT';
      var url = 'api/v1/resource/componentsubmissions/'+encodeURIComponent(componentId);
      if (reverse){
        url = url + '/unsubmit';
      } else {
        url = url + '/submit';
      }
      $http({
        'method': method,
        'url': url
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          deferred.resolve(data);
        }
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  }; 

  submissionservice.deactivateSubmission = function (componentId) {
    var deferred = $q.defer();
    if (componentId){
      var method = 'PUT';
      var url = 'api/v1/resource/componentsubmissions/'+encodeURIComponent(componentId)+'/inactivate';
      $http({
        'method': method,
        'url': url
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          deferred.resolve(data);
        }
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  }; 
  
  submissionservice.copySubmission = function (componentId) {
    var deferred = $q.defer();
    if (componentId){
      var method = 'POST';
      var url = 'api/v1/resource/componentsubmissions/'+encodeURIComponent(componentId)+'/copy';
      $http({
        'method': method,
        'url': url
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          deferred.resolve(data);
        }
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  };   

  submissionservice.updateSubmission = function (requiredForComponent) {
    if (requiredForComponent.component.componentId){
      return submissionservice.createSubmission(requiredForComponent);  
    } else {
      var deferred = $q.defer();
      deferred.reject('The component id was missing');
      return deferred.promise;
    }
  };    

  submissionservice.setNotifyMe = function (email, submission) {
    var deferred = $q.defer();
    if (!submission || !submission.componentId){
      return deferred.reject('An email and submission is required');
    } else {
      var method = 'PUT';
      var url = 'api/v1/resource/componentsubmissions/'+encodeURIComponent(submission.componentId)+'/setNotifyMe';
      $http({
        'method': method,
        'url': url,
        'data': email
      }).success(function (data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          deferred.resolve(data);
        }
      }).error(function (data, status, headers, config) { /*jshint unused:false*/
        showServerError(data, 'body');
        deferred.reject('There was an error');
      });
    }
    return deferred.promise;
  };    




  return submissionservice;
}]);
