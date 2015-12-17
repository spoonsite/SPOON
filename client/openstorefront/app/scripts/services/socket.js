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

/**
* @ngdoc service
* @name openstorefrontApp.socket
* @description
* # socket
* Factory in the openstorefrontApp.
*/
app.factory('socket', ['$rootScope', 'business', '$q',  function ($rootScope, Business, $q) {
  var socket;
  var deferred = $q.defer();
  
  Business.userservice.getCurrentUserProfile().then(function(result){
    if (result){
      socket = io.connect('', {'resource':'openstorefront/event', 'query': 'id='+result.username+'', 
					transports: [
					'websocket',
					'polling'
				   ]});
    }
    deferred.resolve();
  }, function(){
    deferred.resolve();
  });

  return {
    on: function(eventName, callback) {
      var thing = function (eventName, callback, $rootScope, socket) {
        socket.on(eventName, function () {  
          var args = arguments;
          $rootScope.$apply(function () {
            callback.apply(socket, args);
          });
        })
      };
      var temp = thing.bind(null, eventName, callback, $rootScope);
      deferred.promise.then(function(){
        temp(socket);
      });
    },
    emit: function(eventName, data, callback){
      var thing = function (eventName, data, callback, $rootScope, socket) {
        socket.emit(eventName, data, function () {
          var args = arguments;
          $rootScope.$apply(function () {
            if (callback) {
              callback.apply(socket, args);
            }
          });
        })
      };
      var temp = thing.bind(null, eventName, data, callback, $rootScope);
      deferred.promise.then(function(){
        temp(socket);
      });
    }
  }
}]);