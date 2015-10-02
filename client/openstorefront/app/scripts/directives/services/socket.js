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
      // Socket listeners
      // ================
      // socket.on('init', function (data) {
      //   $scope.name = data.name;
      //   $scope.users = data.users;
      // });

      // socket.on('send:message', function (message) {
      //   $scope.messages.push(message);
      // });

      // socket.on('change:name', function (data) {
      //   changeName(data.oldName, data.newName);
      // });

      // socket.on('user:join', function (data) {
      //   $scope.messages.push({
      //     user: 'chatroom',
      //     text: 'User ' + data.name + ' has joined.'
      //   });
      //   $scope.users.push(data.name);
      // });

      // // add a message to the conversation when a user disconnects or leaves the room
      // socket.on('user:left', function (data) {
      //   $scope.messages.push({
      //     user: 'chatroom',
      //     text: 'User ' + data.name + ' has left.'
      //   });
      //   var i, user;
      //   for (i = 0; i < $scope.users.length; i++) {
      //     user = $scope.users[i];
      //     if (user === data.name) {
      //       $scope.users.splice(i, 1);
      //       break;
      //     }
      //   }
      // });
      var detect = $('#detect');//
      var header = $('#header');
      var content = $('#content');
      var input = $('#input');
      var status = $('#status');
      var myName = false;
      var author = null;
      var logged = false;
      var loginurl = "";
      var pathname = document.location.pathname;
      var lastdot = pathname.lastIndexOf("/");
      if (lastdot > 1) {
        loginurl = pathname.substr(1, lastdot);
      }
      
      socket.on('connect', function () { //
        console.log('this', this);
        
        content.html($('<p>', { text: 'Atmosphere connected using ' + this.socket.transport.name}));
        input.removeAttr('disabled').focus();
        status.text('Choose name:');

        $.each(this.socket.transports, function(index, item) {
          $("#transport").append(new Option(item, item));
        });
      });

      socket.on('chat message', message);

      socket.on('error', function (e) {
        content.html($('<p>', { text: 'Sorry, but there\'s some problem with your '
          + 'socket or the server is down' }));
      });

      input.keydown(function(e) {
        if (e.keyCode === 13) {
          var msg = $(this).val();

          // First message is always the author's name
          if (author == null) {
            author = msg;
          }

          socket.emit('chat message', JSON.stringify({ author: author, message: msg }));
          $(this).val('');

          input.attr('disabled', 'disabled');
          if (myName === false) {
            myName = msg;
          }
        }
      });

      function message(msg) {
        try {
          var json = jQuery.parseJSON(msg);
        } catch (e) {
          console.log('This doesn\'t look like a valid JSON: ', message.data);
          return;
        }

        if (!logged) {
          logged = true;
          status.text(myName + ': ').css('color', 'blue');
          input.removeAttr('disabled').focus();
        } else {
          input.removeAttr('disabled');

          var me = json.author == author;
          var date = typeof(json.time) == 'string' ? parseInt(json.time) : json.time;
          addMessage(json.author, json.text, me ? 'blue' : 'black', new Date(date));
        }
      }

      function addMessage(author, message, color, datetime) {
        content.append('<p><span style="color:' + color + '">' + author + '</span> @ ' +
          + (datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()) + ':'
          + (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes())
          + ': ' + message + '</p>');
      }

      // Private helpers
      // ===============

      // var changeName = function (oldName, newName) {
      //   // rename user in list of users
      //   var i;
      //   for (i = 0; i < $scope.users.length; i++) {
      //     if ($scope.users[i] === oldName) {
      //       $scope.users[i] = newName;
      //     }
      //   }

      //   $scope.messages.push({
      //     user: 'chatroom',
      //     text: 'User ' + oldName + ' is now known as ' + newName + '.'
      //   });
      // }

      // // Methods published to the scope
      // // ==============================

      // $scope.changeName = function () {
      //   socket.emit('change:name', {
      //     name: $scope.newName
      //   }, function (result) {
      //     if (!result) {
      //       alert('There was an error changing your name');
      //     } else {

      //       changeName($scope.name, $scope.newName);

      //       $scope.name = $scope.newName;
      //       $scope.newName = '';
      //     }
      //   });
      // };

      // $scope.sendMessage = function () {
      //   socket.emit('send:message', {
      //     message: $scope.message
      //   });

      //   // add the message to our model locally
      //   $scope.messages.push({
      //     user: $scope.name,
      //     text: $scope.message
      //   });

      //   // clear message box
      //   $scope.message = '';
      // };
    }
  };
}]);