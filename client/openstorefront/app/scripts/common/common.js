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


/* exported setupPopovers, setupTypeahead, isEmpty, toggleclass, setUpDropdown,
setupParallax, triggerAlert, triggerError, removeError, initiateClick*/

/*****************************
* This function sets up the popovers for the results page, but could be 
* re-used anywhere that you use the data-toggle="popover" attribute on a div
* It has been configured to give the last popover a different direction in order
* to avoid page overflow.
*****************************/
var setupPopovers = function() {
  if ($('[data-toggle=\'popover\']').length > 2) {
    $('.lastPopover[data-toggle=\'popover\']').last().popover({trigger: 'hover','placement': 'top', 'html': true});
  }
  $('[data-toggle=\'popover\']').popover({trigger: 'hover','placement': 'bottom', 'html': true});
};

/***************************************************************
* This function toggles a class name for a specified id
***************************************************************/
var toggleclass = function(id, className) {
  $('#'+id).toggleClass(className);
};

/***************************************************************
* This function makes a dropdown menu work on first click.
***************************************************************/
var setUpDropdown= function(id) {
  $('#' + id).on('click', function(e) {
    e.stopPropagation();

    //The dropdown has to be somewhere inside the element (not in the element itself)
    $(this).next('.dropdown').find('[data-toggle=dropdown]').dropdown('toggle');
  });
};


var resetUpdateNotify = function() {
  $('#updateNotify').stop(true, true).animate({height: '0px'}, 0, function(){});
}

var showUpdateNotify = function() {
  resetUpdateNotify();
  $('#updateNotify').stop(true, true).animate({
    height: '50px'
  }, 300, function() {
    setTimeout(function() {
      $('#updateNotify').stop(true, true).animate({
        height: '0px'
      }, 300, function(){
        // both animations complete.
      })
    }, 12000);
  })
}

/***************************************************************
* Speed up calls to hasOwnProperty (somewhat of an hasOwnPropert override)
***************************************************************/
var hasOwnProperty2 = Object.prototype.hasOwnProperty;

/***************************************************************
* This function checks to see if the object is empty
* params: obj -- the object to check
* returns: boolean -- a true or false value of whether the object is empty or not
***************************************************************/
function isEmpty(obj) {
  // null and undefined are 'empty'
  if (obj === null || obj === undefined) {
    return true;
  }

  // Assume if it has a length property with a non-zero value
  // that that property is correct.
  if (obj.length > 0) {
    return false;
  }

  if (obj.length === 0) {
    return true;
  }

  // Otherwise, does it have any properties of its own?
  // Note that this doesn't handle
  // toString and valueOf enumeration bugs in IE < 9
  for (var key in obj) {
    if (hasOwnProperty2.call(obj, key)) {
      return false;
    }
  }

  return true;
}

/***************************************************************
* Hide an alert
* params: uid -- the unique id of the alert box
* params: delay -- How fast you want the alert to fade out
***************************************************************/
var hideAlert = function(uid, delay) {
  $('#alert_holder_'+uid).css('visiblility', 'hidden');
  $('#alert_holder_'+uid).stop(true, true).fadeOut(delay, function() {
    $('#alert_holder_'+uid).remove();
  });
};


/***************************************************************
* Trigger an alert
* params: text -- The text that will fill the alert box
* params: uid -- the unique id for the alert box
* params: id -- the id of the element to attach the alert box to
* params: delay -- how long you want the alert to stay
***************************************************************/
var triggerAlert = function(text, uid, id, delay) {
  delay = delay || 5000;
  if (!text || !uid){
    console.error('TRIGGER-ALERT Failed because the text or uid fields were not set');
    return;
  }
  if (text !== 'fail') {
    if ($(id).length === 0) {
      id = 'body';
    }
    $('#alert_holder_'+uid).remove();
    $(id).prepend('<div class="alert ng-scope centerAlert am-fade alert-customDI2E" id="alert_holder_'+uid+'"><button type="button" class="close" id="close_alert_'+uid+'" onclick="hideAlert(\''+uid+'\', 300)">×</button><span id="alert_holder_'+uid+'_span">'+text+'</span></div>');
    
    // this will hide the alert on any action outside the alert box.
    // $(document).on('click keypress', function(event) {
    //   //this condition makes it so that if you click on the span, it won't close
    //   //the alert. If you want it to close, we need to set it to false.
    //   if ($(event.target).attr('id') !== 'alert_holder_'+uid && $(event.target).attr('id') !== 'alert_holder_'+uid+'_span' ) {
    //     if ($('#alert_holder_'+uid).is(':visible')) {
    //       if ( event.which === 13 ) {
    //         event.preventDefault();
    //       }
    //       hideAlert(uid, 300);
    //     }
    //   }
    // });
    //
    setTimeout(function() {
      hideAlert(uid, 1000);
    }, delay);
  }
  // console.log($('#alert_holder_'+uid));
  
};


var getShortDescription = function(str){
  var html = $.parseHTML(str);
  var log = [];
  // Gather the parsed HTML's node names
  var count = 300;
  var total = 0;
  for(var i = 0; i < html.length && total < count; i++) {
    var el = html[i];
    var length;
    if (el.nodeName === 'A') {
      length = $(el).html().length;
      if (!((total + length) > count)) {
        total = total + length;
        log.push($(el)[0].outerHTML);
      } else {
        total = total + length;
        log.push('...');
      }
    } else if (el.nodeName === '#text'){
      if ((total+el.length) > count){
        var temp = $(el)[0].textContent.split(' ');
        var j = 0;
        while(total < count){
          total = total + temp[j].length + 1;
          log.push(temp[j]+" ");
          j++;
        }
        log.push('...');
      } else {
        total = total + el.length;
        log.push($(el)[0].textContent);
      }
    } else if (el.nodeName != 'BR'){
      log.push($(el)[0].outerHTML);
    }
  };
  return log.join(' ');
}


/***************************************************************
* This funciton gets rid of input error styling
* params: id -- the id of the element that should be cleaned up
***************************************************************/
var removeError = function() {
  $('.errorOnInput').tooltip('destroy');
  $('.errorOnInput').removeClass('errorOnInput');
};

var showServerError = function(errorObj, id){
  // console.log('errorO', errorObj);
  
  var message = 'There was a server error. Contact a System Admin or try again';
  //message, potential resolution, ticketNumber, contact;
  if (errorObj) {
    if (errorObj.message) {
      message = message + ': <div class="leftIndent">Message:&nbsp;<span>' + errorObj.message + '</span></div>';
    }if (errorObj.errorTicketNumber) {
      message = message + '<div class="leftIndent">Error Ticket Number:&nbsp;<span>' + errorObj.errorTicketNumber + '</span></div>';
    }if (errorObj.potentialResolution) {
      message = message + '<div class="leftIndent">Potential resolution:&nbsp;<span>' + errorObj.potentialResolution + '</span></div>';
    }
  }
  triggerAlert(message, 'serverError', id, 6000);
}

/***************************************************************
* This function adds a tooltip and styling to an input element
* when the serve responds with an error. This expects an error object
* params: errorObj -- the object that contains an errors array
*  {
*    'success': false,
*    'errors': [
*      {
*        'mainSearchBar' : 'Your input was invalid. Please try again.'
*      },
*      {
*        'element_id' : 'Error message to be displayed in the tooltip'
*      }
*    ]
*  };
***************************************************************/
var triggerError = function(errorObj) {
  // console.log('errorObject', errorObj);
  
  if (isRequestError(errorObj)) {
    var errors = errorObj.errors;
    _.each(errors.entry, function(item) {
      var i = item.key;
      $('#'+i).addClass('errorOnInput');
      $('#'+i).tooltip({
        // container: 'body',
        html: 'true',
        placement: 'top',
        trigger: 'focus',
        title: item.value
      });
    });
  }
};


/***************************************************************
* Close the navbar-collapse
***************************************************************/
var initiateClick = function(id) {
  if($('#' + id).css('display') !== 'none'){
    $('#' + id).trigger( 'click' );
  }
};

var originalLeave = $.fn.popover.Constructor.prototype.leave;
$.fn.popover.Constructor.prototype.leave = function(obj){
  var self = obj instanceof this.constructor ?
  obj : $(obj.currentTarget)[this.type](this.getDelegateOptions()).data('bs.' + this.type);
  var popovers, name, container, timeout;

  originalLeave.call(this, obj);

  if(obj.currentTarget) {
    popovers = $('.popover');
    name = $(obj.currentTarget).attr('data-original-title');
    container = _.find(popovers, function(item){
      return $(item).find('.popover-title').html() === name;
    });
    
    timeout = self.timeout;
    $(container).on('mouseenter', function(){
      //We entered the actual popover – call off the dogs
      clearTimeout(timeout);
      //Let's monitor popover content instead
      $(container).on('mouseleave', function(){
        $.fn.popover.Constructor.prototype.leave.call(self, self);
      });
    });
  }
};

/***************************************************************
* This function translates an sqlDate (yyyy-mm-ddThh:mm:ss.ms)
* to a javascript date.
***************************************************************/
var sqlToJsDate = function(sqlDate){
  if (sqlDate) {
    //sqlDate in SQL DATETIME format ("yyyy-mm-ddThh:mm:ss.ms")
    var sqlDateArr1 = sqlDate.split('-');
    //format of sqlDateArr1[] = ['yyyy','mm','dd hh:mm:ms']
    var sYear = sqlDateArr1[0];
    var sMonth = (Number(sqlDateArr1[1]) - 1).toString();
    var sqlDateArr2 = sqlDateArr1[2].split('T');
    //format of sqlDateArr2[] = ['dd', 'hh:mm:ss.ms']
    var sDay = sqlDateArr2[0];
    var sqlDateArr3 = sqlDateArr2[1].split(':');
    //format of sqlDateArr3[] = ['hh','mm','ss.ms']
    var sHour = sqlDateArr3[0];
    var sMinute = sqlDateArr3[1];
    var sqlDateArr4 = sqlDateArr3[2].split('.');
    //format of sqlDateArr4[] = ['ss','ms']
    var sSecond = sqlDateArr4[0];
    var sMillisecond = sqlDateArr4[1];

    return new Date(sYear,sMonth,sDay,sHour,sMinute,sSecond,sMillisecond);
  } else {
    return null;
  }
}

var isRequestError = function(response) {
  return !isNotRequestError(response);
}

var isNotRequestError = function(response){
  if (response && response !== 'false'){
    // console.log('response', response);
    if (typeof response === 'object' && (response.success === false || response.success === 'false')) {
      return false;
    } else {
      return true;
    }
  } else {
    return true;
  }
}

var notInCollection = function(collection, item){
  return _.contains(collection, item)? false: true;
}

// Base.esapi.properties.logging.ApplicationLogger = {
//   Level: org.owasp.esapi.Logger.ALL,
//   Appenders: [ new Log4js.ConsoleAppender() ],
//   LogUrl: true,
//   LogApplicationName: true,
//   EncodingRequired: true
// };

// Base.esapi.properties.application.Name = 'My Application v1.0';
// org.owasp.esapi.ESAPI.initialize();
// $ESAPI.logger('ApplicationLogger').info(org.owasp.esapi.Logger.EventType.EVENT_SUCCESS, 'This is a test message');
// document.writeln( $ESAPI.encoder().encodeForURL( "owasp-esapi-js.googlecode.com/this is awesome!\") );

// Using the validator
// var validateCreditCard = function() {
  // return $ESAPI.validator().isValidCreditCard( $('CreditCard').value );
// }

// $ESAPI.validator().isValidDate('DateRangeValidator', '07-07-2014', DateFormat(''), false)
// console.log("Valid Date: ", $ESAPI.validator().getValidDate( "Jun, 26 2014", "dateTest1", DateFormat.getDateInstance(), false) );
