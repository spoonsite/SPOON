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
app.factory('userservice', ['localCache', '$http', '$q', function(localCache, $http, $q) {

  //Constants
  var CURRENT_USER = 'currentuser';
  var minute = 60 * 1000;
  var day = minute * 1440; //1 day
  var MAX_USER_CACHE_TIME = day; /*jshint unused:false*/


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


  /**
  *  Loads the current user
  */
  var sendAdminMessage = function(messageObj) {
    var deferred = $q.defer();
    // getting rid of caching here
    // if we want to bring it back for the user profile delete this line
    if (messageObj) {
      $http({
        'method': 'POST',
        'url': 'api/v1/service/notification/admin-message',
        'data': messageObj
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (isNotRequestError(data)) {
          deferred.resolve('The message was sent');
        } else {
          deferred.reject(data);
        }
      }, function(data, status, headers, config){
        deferred.reject(false);
      });
    } else {
      deferred.reject(false);
    }

    return deferred.promise;
  };


  /**
  *  Loads the current user
  */
  var getAllUserProfiles = function(all, forceReload) {
    var deferred = $q.defer();
    // getting rid of caching here
    // if we want to bring it back for the user profile delete this line
    var allUserProfiles;
    if (all) {
      allUserProfiles = checkExpire('all-allUserProfile', minute * 5);
    } else {
      allUserProfiles = checkExpire('allUserProfile', minute * 5);
    }
    if (allUserProfiles && !forceReload) {
      deferred.resolve(allUserProfiles);
    } else {
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/userprofiles',
        'params': {
          'all': all
        }
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          if (all) {
            save('all-allUserProfile', data);
          } else {
            save('allUserProfile', data);
          }
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }, function(data, status, headers, config){
        deferred.reject('There was an error');
      });
    }

    return deferred.promise;
  };

  /**
  *  Loads the current user
  */
  var deactivateUser = function(userId) {
    var deferred = $q.defer();
    // getting rid of caching here
    // if we want to bring it back for the user profile delete this line
    if (userId) {
      $http({
        'method': 'DELETE',
        'url': 'api/v1/resource/userprofiles/'+encodeURIComponent(userId),
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }, function(data, status, headers, config){
        deferred.reject('There was an error');
      });
    } else {
      deferred.reject('There was no userId')
    }

    return deferred.promise;
  };

  /**
  *  Loads the current user
  */
  var activateUser = function(userId) {
    var deferred = $q.defer();
    // getting rid of caching here
    // if we want to bring it back for the user profile delete this line
    if (userId) {
      $http({
        'method': 'PUT',
        'url': 'api/v1/resource/userprofiles/'+encodeURIComponent(userId)+'/reactivate',
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        
        if (data && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }, function(data, status, headers, config){
        
        deferred.reject('There was an error');
      });
    } else {
      deferred.reject('There was no userId')
    }

    return deferred.promise;
  };

  /**
  *  Loads the current user
  */
  var getCurrentUserProfile = function(forceReload) {
    var deferred = $q.defer();
    // getting rid of caching here
    // if we want to bring it back for the user profile delete this line
    forceReload = true;
    var currentUser = checkExpire('currentUserProfile');
    if (currentUser && !forceReload) {
      deferred.resolve(currentUser);
    } else {
      loadProfile(CURRENT_USER, function(data, status, headers, config) { /*jshint unused:false*/
        if (data && isNotRequestError(data)) {
          removeError();
          save('currentUserProfile', data);
          deferred.resolve(data);
        } else {
          triggerError(data);
          deferred.reject(false);
        }
      }, function(data, status, headers, config){
        deferred.reject('There was an error');
      });
    }

    return deferred.promise;
  };


  /**
  *  Initialize the current user
  */
  var initializeUser = function() {
    var deferred = $q.defer();
    loadProfile(CURRENT_USER, function(data, status, headers, config) { /*jshint unused:false*/
      if (data && isNotRequestError(data)) {
        removeError();
        if (data.username){
          CURRENT_USER = data.username;
        }
        save('currentUserProfile', data);
        deferred.resolve(data);
      } else {
        triggerError(data);
        deferred.reject(false);
      }
    }, function(data, status, headers, config){
      deferred.reject('There was an error');
    });

    return deferred.promise;
  };


  var sendTestEmail = function(userId) {
    var deferred = $q.defer();
    if (userId) {
      $http({
        'method': 'POST',
        'url': 'api/v1/resource/userprofiles/'+ encodeURIComponent(userId) + '/test-email'
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        // console.log('data', data);
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    } else {
      deferred.reject('It Failed');
    }
    return deferred.promise;
  };


  /**
  *  Load profile from the server
  * @param string username
  * @returns {undefined}
  */
  var loadProfile = function(username, successFunc, errorFunc) {
    $http.get('api/v1/resource/userprofiles/'+encodeURIComponent(username)).success(successFunc).error(errorFunc);
  };

  var saveCurrentUserProfile = function(userProfile) { /*jshint unused:false*/
    return saveProfile(CURRENT_USER, userProfile);
  };


  var saveThisProfile = function(profile){
    return saveProfile(profile.username, profile);
  }

  /**
  *  Save profile to the service and on success it reloads the profile
  * @param string usernamer
  * @returns {undefined}
  */
  var saveProfile = function(username, userProfile) { /*jshint unused:false*/
    var deferred = $q.defer();
    if (true) {
      $http({
        'method': 'PUT',
        'url': 'api/v1/resource/userprofiles/'+ encodeURIComponent(username),
        'data': userProfile
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        // console.log('data', data);
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          updateCache('currentUserProfile', data);
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject(false);
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    } else {
      deferred.reject('It Failed');
    }
    return deferred.promise;
  };


  var getWatches = function(userId, override) {
    var deferred = $q.defer();
    if (userId) {
      // console.log('userId', userId);
      
      var watches = checkExpire('watches', minute * 0.5);
      if (watches && !override) {
        deferred.resolve(watches);
      } else {
        var url = 'api/v1/resource/userprofiles/'+encodeURIComponent(userId)+'/watches';
        $http({
          'method': 'GET',
          'url': url
        }).success(function(data, status, headers, config) { /*jshint unused:false*/
          if (data && data !== 'false' && isNotRequestError(data)) {
            removeError();
            save('watches', data);
            deferred.resolve(data);
          } else {
            removeError();
            triggerError(data);
            deferred.reject(false);
          }
        }).error(function(data, status, headers, config) { /*jshint unused:false*/
          deferred.reject('There was an error');
        });
      }
    } else {
      deferred.reject('You didn\'t provide a username');
    }

    return deferred.promise;
  };

  var setWatches = function(watches) {
    var deferred = $q.defer();

    $http({
      'method': 'POST',
      'url': 'api/v1/resource/lookup/watches',
      'data': watches
    }).success(function(data, status, headers, config) { /*jshint unused:false*/
      if (data && data !== 'false' && isNotRequestError(data)) {
        removeError();
        updateCache('watches', data);
        deferred.resolve(data);
      } else {
        removeError();
        triggerError(data);
        deferred.result(false);
      }
    }).error(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.reject('There was an error');
    });

    return deferred.promise;
  };

  var saveWatch = function(userId, watch, watchId) {
    // console.log('userId', userId);
    // console.log('watch', watch);
    // console.log('watchId', watchId);
    
    var deferred = $q.defer();
    if (userId && watch) {
      var url;
      var methodString;
      if (!watchId) {
        url = 'api/v1/resource/userprofiles/'+encodeURIComponent(userId)+'/watches';
        methodString = 'POST';
      } else {
        url = 'api/v1/resource/userprofiles/'+encodeURIComponent(userId)+'/watches/'+encodeURIComponent(watchId);
        methodString = 'PUT';
      }
      $http({
        'method': methodString,
        'url': url,
        'data': watch
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject('There was an error saving the watch');
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    } else {
      deferred.reject('You did not save the watch');
    }

    return deferred.promise;
  };


  var removeWatch = function(userId, watchId) {
    var deferred = $q.defer();
    if (userId && watchId) {
      var url = 'api/v1/resource/userprofiles/'+encodeURIComponent(userId)+'/watches/'+encodeURIComponent(watchId);
      $http({
        'method': 'DELETE',
        'url': url
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject('There was an error grabbing the watches');
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    } else {
      deferred.reject('You must include a username and watch id to remove a watch');
    }

    return deferred.promise;
  }

  /**
  *
  */
  var getReviews = function(username, override) {
    var deferred = $q.defer();
    var reviews = checkExpire('reviews'+username, minute * 0.5);
    if (reviews && !override) {
      deferred.resolve(reviews);
    } else {
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/components/reviews/'+encodeURIComponent(username)
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          save('reviews'+username, data);
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject('There was an error grabbing the reviews');
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    }

    return deferred.promise;
  };
  
  var getUserMessages = function(queryParamFilter) {
    var deferred = $q.defer();
    
    $http({
      'method': 'GET',
      'url': 'api/v1/resource/usermessages?' + queryParamFilter.toQuery()
    }).success(function(data, status, headers, config) { /*jshint unused:false*/
      if (data && data !== 'false' && isNotRequestError(data)) {
        removeError();          
        deferred.resolve(data);
      } else {
        removeError();
        triggerError(data);
        deferred.reject('There was an error grabbing the usemessages');
      }
    }).error(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.reject('There was an error');
    });
    
    return deferred.promise;
  }; 
  
  var processUserMessagesNow = function() {
    var deferred = $q.defer();
    
    $http({
      'method': 'POST',
      'url': 'api/v1/resource/usermessages/processnow'
    }).success(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.resolve(data);       
    }).error(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.reject('There was an error');
    });
    
    return deferred.promise;
  }; 
  
  var cleanoldUserMessagesNow = function() {
    var deferred = $q.defer();
    
    $http({
      'method': 'POST',
      'url': 'api/v1/resource/usermessages/cleanold'
    }).success(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.resolve(data);        
    }).error(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.reject('There was an error');
    });
    
    return deferred.promise;
  };   
  
  var removeUserMessages = function(id) {
    var deferred = $q.defer();
    
    $http({
      'method': 'DELETE',
      'url': 'api/v1/resource/usermessages/' + encodeURIComponent(id)
    }).success(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.resolve(data);
    }).error(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.reject('There was an error');
    });
    
    return deferred.promise;
  };  
  
  var removeNotificationEvent = function(id) {
    var deferred = $q.defer();
    
    $http({
      'method': 'DELETE',
      'url': 'api/v1/resource/notificationevent/' + encodeURIComponent(id)
    }).success(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.resolve(data);
    }).error(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.reject('There was an error');
    });
    
    return deferred.promise;
  }; 
  
  var postNotificationEvent = function(notificationEvent) {
    var deferred = $q.defer();
    
    $http({
      'method': 'POST',
      'url': 'api/v1/resource/notificationevent',
      'data': notificationEvent
    }).success(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.resolve(data);        
    }).error(function(data, status, headers, config) { /*jshint unused:false*/
      deferred.reject('There was an error');
    });
    
    return deferred.promise;
  };   
  
  var getUserByUsername = function(username){
    // console.log('user requested', username);
    
    var deferred = $q.defer();
    var userInfo = checkExpire(username, minute * 1440);
    if (userInfo) {
      deferred.resolve(userInfo);
    } else {
      $http({
        'method': 'GET',
        'url': 'api/v1/resource/userprofiles/' + encodeURIComponent(username)
      }).success(function(data, status, headers, config) { /*jshint unused:false*/
        if (data && data !== 'false' && isNotRequestError(data)) {
          removeError();
          save(username, data);
          deferred.resolve(data);
        } else {
          removeError();
          triggerError(data);
          deferred.reject('There was an error grabbing the user profile');
        }
      }).error(function(data, status, headers, config) { /*jshint unused:false*/
        deferred.reject('There was an error');
      });
    }

    return deferred.promise;
  }

  //Public API
  return {
    deactivateUser:deactivateUser,
    activateUser:activateUser,
    getCurrentUserProfile: getCurrentUserProfile,
    getUserByUsername:getUserByUsername,
    getAllUserProfiles: getAllUserProfiles,
    getReviews: getReviews,
    getWatches: getWatches,
    initializeUser: initializeUser,
    removeWatch: removeWatch,
    saveCurrentUserProfile: saveCurrentUserProfile,
    saveThisProfile: saveThisProfile,
    saveWatch: saveWatch,
    sendAdminMessage:sendAdminMessage,
    sendTestEmail: sendTestEmail,
    setWatches: setWatches,
    getUserMessages: getUserMessages,
    processUserMessagesNow: processUserMessagesNow,
    cleanoldUserMessagesNow: cleanoldUserMessagesNow,
    removeUserMessages: removeUserMessages,
    removeNotificationEvent: removeNotificationEvent,
    postNotificationEvent: postNotificationEvent
  };

}]);