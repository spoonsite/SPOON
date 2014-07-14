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
app.factory('userservice', ['localCache', '$http',  '$q', function (localCache, $http, $q) {
  
  //Constants
  var CURRENT_USER = "CURRENTUSER";
  var MAX_USER_CACHE_TIME = (60*1000)*1440; //1 day
    
  /**
   *  Loads the current user
   */
  var getCurrentUserProfile = function(forceReload){
      var deferred = $q.defer();
      var currentUserProfile = localCache.get('currentUserProfile', 'object');
      var loadProfileFlag = false;
      
      if (forceReload) {
        loadProfileFlag = true;
      } else {      
        if (currentUserProfile) {
            //check for expired
            var cacheTime = localCache.get('currentUserProfile-time', 'date');
            var timeDiff = new Date() - cacheTime;
            if (timeDiff < MAX_USER_CACHE_TIME){
              deferred.resolve(currentUserProfile);
            }
            else {
              loadProfileFlag = true;
            }
        } else {
          loadProfileFlag = true;
        }        
      }
     
      if (loadProfileFlag){
        loadProfile(CURRENT_USER, function(data, status, headers, config){       
           localCache.save('currentUserProfile', data);
           localCache.save('currentUserProfile-time', new Date());                    
           deferred.resolve(currentUserProfile);          
        });        
      }
      return deferred.promise;      
  };
  
  /**
   *  Load profile from the server
   * @param string username
   * @returns {undefined}
   */
 var loadProfile = function(username, successFunc){
   $http.get('/openstorefront-web/api/v1/resource/userprofiles/' + username).success(successFunc);  
 };
 
 var saveCurrentUserProfile = function(userProfile, success, failure) {
   saveProfile(CURRENT_USER, userProfile, success, failure);
 };
 
   /**
   *  Save profile to the service and on success it reloads the profile
   * @param string usernamer
   * @returns {undefined}
   */
 var saveProfile = function(username, userProfile, success, failure){
    var deferred = $q.defer();
  
  
   return deferred.promise;       
 };
  
  //Public API
   return {
        getCurrentUserProfile: getCurrentUserProfile,        
        saveCurrentUserProfile: saveCurrentUserProfile
    };
  
}]);