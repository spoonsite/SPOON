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
  
  //Constant
  var CURRENT_USER = "CURRENTUSER";
  
 var currentUserProfile = null;
    
  /**
   *  Loads the current user
   */
  var getCurrentUserProfile = function(){
      var deferred = $q.defer();
      if (currentUserProfile === null){
        loadProfile(CURRENT_USER, function(data, status, headers, config){
          currentUserProfile = data;
          deferred.resolve(currentUserProfile);
        });        
      } else  {
        deferred.resolve(currentUserProfile);
      }
      return deferred.promise;      
  };
  
  /**
   * 
   * @param boolean currentUser
   * @returns {undefined}
   */
 var loadProfile = function(username, successFunc){
   var deferred = $q.defer();
   $http.get('openstorefront-web/api/v1/resource/userprofiles/' + username).success(successFunc);
  return deferred.promise;        
 };
 
 var saveCurrentUserProfile = function(userProfile) {
   saveProfile(CURRENT_USER, userProfile);
 };
 
   /**
   * 
   * @param boolean currentUser
   * @returns {undefined}
   */
 var saveProfile = function(username, userProfile){
    var deferred = $q.defer();
  
  
   return deferred.promise;       
 };
  
  //Public API
   return {
        getCurrentUserProfile: getCurrentUserProfile,        
        saveCurrentUserProfile: saveCurrentUserProfile
    };
  
}]);