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
app.factory('lookupservice', ['$http', '$q', function($http, $q) {

    //Page Cache (on reload of page I want to reload the data)
    var userTypeCodes = null;


    var getUserTypeCodes = function() {
      var deferred = $q.defer();
      if (userTypeCodes === null) {
        loadLookupTable('UserTypeCode', function(data, status, headers, config) { /*jshint unused:false*/
          userTypeCodes = data.data;
          deferred.resolve(userTypeCodes);
        });
      } else {
        deferred.resolve(userTypeCodes);
      }
      return deferred.promise;
    };

    /**
    * Load a lookup table
    * @param  entityName to lookup
    * @param  success function
    */
    var loadLookupTable = function(entityName, successFunc) {
      $http.get('/openstorefront-web/api/v1/resource/lookup/' + entityName).success(successFunc);
    };
    var getEvalLevels = function() {
      var deferred = $q.defer();
      deferred.resolve(_.find(MOCKDATA.filters, {'type':'DI2ELEVEL'}));
      return deferred.promise;
    };

    //Public API
    return {
      getUserTypeCodes: getUserTypeCodes,
      getEvalLevels: getEvalLevels
    };

  }]);
