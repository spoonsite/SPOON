$(function () {
    "use strict";

    var detect = $('#detect');
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
   // var socket = io.connect('', {'resource':loginurl + 'chat'});
    var socket = io.connect('', {
      'resource':'openstorefront/event', 
      query: 'id=admin'
    });

    socket.on('connect', function () {
        content.html($('<p>', { text: 'Atmosphere connected using ' + this.socket.transport.name}));
        input.removeAttr('disabled').focus();
        status.text('Choose name:');

        $.each(this.socket.transports, function(index, item) {
            $("#transport").append(new Option(item, item));
        });
    });

    socket.on('ADMIN', message);
    socket.on('TASK', message);
    socket.on('REPORT', message);
    socket.on('WATCH', message);
    socket.on('IMPORT', message);

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

            socket.emit('notification', $.stringifyJSON({ author: author, message: msg }));
            $(this).val('');

            input.attr('disabled', 'disabled');
            if (myName === false) {
                myName = msg;
            }
        }
    });

    function message(msg) {
//        try {
//            var json = jQuery.parseJSON(msg);
//        } catch (e) {
//            console.log('This doesn\'t look like a valid JSON: ', message.data);
//            return;
//        }

//        if (!logged) {
//            logged = true;
//            status.text(myName + ': ').css('color', 'blue');
//            input.removeAttr('disabled').focus();
//        } else {
            input.removeAttr('disabled');

            addMessage('', msg,  'blue', new Date());
//        }
    }

    function addMessage(author, message, color, datetime) {
        content.append('<p><span style="color:' + color + '"> Server: </span> @ ' 
            + message.message + '</p>');
    }
});

