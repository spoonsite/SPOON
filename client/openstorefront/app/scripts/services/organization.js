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

app.factory('organizationservice', ['$http', '$q', 'localCache', function($http, $q, localCache) {
  // default to 60 second expire time.
  var minute = 60 * 1000;
  var organization = {};
  var basepath = 'api/v1/resource/organizations/';

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

  // Gets all organization records.
  // @GET
  organization.getOrganizations = function(override) {
    var deferred = $q.defer();
    var result = checkExpire('all-organizations', minute * 30);
    if (result && !override) {
      deferred.resolve(result);
    } else {
      $http({
        method: 'GET',
        url: basepath
      }).success(function(data, status, headers, config){
        if (data && data !== 'false' && isNotRequestError(data)){
          removeError();
          save('all-organizations', data);
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
  
  // Gets an organization record.
  // @GET
  // @Path("/{id}")
  organization.getOrganization = function(id, override) {
    var deferred = $q.defer();
    var result = checkExpire('organization_'+id, minute * 30);
    if (result && !override) {
      deferred.resolve(result);
    } else if (id){
      $http({
        method: 'GET',
        url: basepath + encodeURIComponent(id)
      }).success(function(data, status, headers, config){
        if (data && data !== 'false' && isNotRequestError(data)){
          removeError();
          save('organization_'+id, data);
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
      deferred.reject('You must include a type and a code to retreive it\'s organization');
    }
    return deferred.promise;
  };

  // Gets an organization references.
  // @GET
  // @Path("/{id}/references")
  organization.getReferences = function(id, override) {
    var deferred = $q.defer();
    var result = checkExpire('organization_refs_'+id, minute * 30);
    if (result && !override) {
      deferred.resolve(result);
    } else if (id){
      $http({
        method: 'GET',
        url: basepath + encodeURIComponent(id) + '/references'
      }).success(function(data, status, headers, config){
        if (data && data !== 'false' && isNotRequestError(data)){
          removeError();
          save('organization_refs_'+id, data);
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
      deferred.reject('You must include a type and a code to retreive it\'s organization');
    }
    return deferred.promise;
  };

  // Gets references that do not have organization.
  // @GET
  // @Path("/references")
  organization.getReferencesNoOrg = function(override) {
    var deferred = $q.defer();
    var result = checkExpire('organization_refs', minute * 30);
    if (result && !override) {
      deferred.resolve(result);
    } else{
      $http({
        method: 'GET',
        url: basepath + 'references'
      }).success(function(data, status, headers, config){
        if (data && data !== 'false' && isNotRequestError(data)){
          removeError();
          save('organization_refs', data);
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

  // Creates an organization
  // @POST
  //
  // Updates an organization
  // @PUT
  // @Path("/{id}")
  organization.saveOrganization = function(organization){
    var deferred = $q.defer();
    if (organization) {
      var url = basepath;
      var method = 'POST';
      if (organization.organizationId) {
        method = 'PUT';
        url = url + encodeURIComponent(organization.organizationId);
      }
      $http({
        method: method,
        url: url,
        data: organization
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


  // Merges one organization with another
  // @POST
  // @Path("/{targetId}/merge/{mergeId}")
  organization.mergeOrganizations = function(target, merge){
    var deferred = $q.defer();
    if (target && merge) {
      $http({
        method: 'POST',
        url: basepath + encodeURIComponent(target) + '/merge/' + encodeURIComponent(merge),
        data: organization
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

  // Extract organizations from the data
  // @POST
  // @Path("/extract")
  organization.extract = function(){
    var deferred = $q.defer();
    $http({
      method: 'POST',
      url: basepath + 'extract',
      data: organization
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
    return deferred.promise;
  }

  // Deletes an organization
  // @DELETE
  // @Path("/{id}")
  organization.deleteOrganization = function(id){
    var deferred = $q.defer();
    if (id) {
      $http({
        method: 'DELETE',
        url: basepath + encodeURIComponent(id)
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
      deferred.reject('There was no id...');
    }
    return deferred.promise;
  }
  

  return organization;
}]);
