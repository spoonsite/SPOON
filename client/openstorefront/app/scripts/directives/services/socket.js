'use strict';

/**
* @ngdoc directive
* @name openstorefrontApp.directive:services/socket
* @description
* # services/socket
*/
app.directive('socket', ['socket', function (socket) {
  return {
    template: '<div id="header"><h3>Atmosphere Chat. Default transport is WebSocket, fallback is long-polling</h3></div> <div id="detect"><h3>Detecting what the browser and server are supporting</h3></div> <div id="content"></div> <div> <span id="status">Connecting...</span> <input type="text" id="input" disabled="disabled"/> </div>',
    restrict: 'EA',
    link: function postLink(scope, element, attrs) {
      
      socket.on('connect', function () { //
        console.warn(this.socket.transport.name + ' contected');
      });
      socket.on('WATCH', function (args) { //
        console.log('this', args);
      });
      socket.on('IMPORT', function (args) { //
        console.log('this', args);
      });
      socket.on('TASK', function (args) { //
        console.log('this', args);
      });
      socket.on('REPORT', function (args) { //
        console.log('this', args);
      });
      socket.on('ADMIN', function (args) { //
        console.log('this', args);
      });

    }
  };
}]);