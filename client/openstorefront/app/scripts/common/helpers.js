'use strict';
(function (window) {

  // attach myLibrary as a property of window
  var utils = window.utils || (window.utils = {});

  // BEGIN API

  // function to convert letters for the job status into human readable form
  // (might think about moving this to the server so it doesn't require a code change)
  utils.calcStatus = function(val) {
    switch(val){
      case 'C':
      return 'Complete'
      break;
      case 'E':
      return 'Error'
      break;
      case 'W':
      return 'Working'
      break;
      default:
      return 'Error'
      break;
    }
  }

  utils.getStatus = function(val){
    switch(val){
      case 'DONE':
      return 'info'
      break;
      case 'CANCELLED':
      return 'warning'
      break;
      case 'FAILED':
      return 'danger'
      break;
      case 'QUEUED':
      case 'WORKING':
      default:
      return 'success'
      break;
    }
  }

  // function to convert the eval status code to a string. 
  // (might think about moving this to the server so it doesn't require a code change)
  utils.calcEvalStatus = function(statusCode, actual, estimated){
    switch(statusCode){
      case 'C':
      if (actual && actual !== 'null') {
        result = 'COMPLETED ' + actual;
      } else {
        result = 'COMPLETED';
      }
      break;
      case 'H':
      result = 'HALTED ' + actual;
      if (actual && actual !== 'null') {
        result = 'HALTED ' + actual;
      } else {
        result = 'HALTED';
      }
      break;
      case 'P':
      if (estimated && estimated !== 'null') {
        // result = 'IN PROGRESS (estimated complete ' + estimated + ')';
        result = 'IN PROGRESS';
      } else {
        result = 'IN PROGRESS';
      }
      break;
      default:
      if (estimated && estimated !== 'null') {
        // result = 'NOT STARTED (estimated complete ' + estimated + ')';
        result = 'NOT STARTED';
      } else {
        result = 'NOT STARTED';
      }
      break;
    }
    return result;
  }

  // converts a string into a Date object and then into a readable string.
  utils.getDate = function(date, monthYear){
    if (date)
    {
      var d = new Date(date);
      var currDate = d.getDate();
      var currMonth = d.getMonth();
      var currYear = d.getFullYear();
      if (!monthYear) {
        return ((currMonth + 1) + '/' + currDate + '/' + currYear);
      } else {
        return ((currMonth + 1) + '/' + currYear);
      }
    }
    return null;
  };

  // function to convert an object with parameters that translate to strings
  utils.toParamString = function(obj){
    var queryParams = "";
    for (var key in obj) {
      if (obj.hasOwnProperty(key)) {
        var val = obj[key];
        // if the value is a clean string and has a value, we know we want it.
        if (val !== null && (typeof val === 'string' || typeof val === 'number')){
          if (!queryParams.length) {
            queryParams += key + '=' + encodeURIComponent(val);
          } else{
            queryParams += '&' + key + '=' + encodeURIComponent(val);
          }
        }
      }
    }
    return queryParams;
  }

  // END API
  utils.RE = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  utils.EMAIL_REGEXP = /^[a-z0-9!#$%&'*+\/=?^_`{|}~.-]+@[a-z0-9]([a-z0-9-]*[a-z0-9])?(\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$/i;

  utils.URL_REGEX = new RegExp(
    //an empty string
    "^$|" +
    // OR
    "^" +
    // protocol identifier
    "(?:(?:https?|ftp)://)" +
    // user:pass authentication
    "(?:\\S+(?::\\S*)?@)?" +
    "(?:" +
      // IP address exclusion
      // private & local networks
      "(?!(?:10|127)(?:\\.\\d{1,3}){3})" +
      "(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})" +
      "(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})" +
      // IP address dotted notation octets
      // excludes loopback network 0.0.0.0
      // excludes reserved space >= 224.0.0.0
      // excludes network & broacast addresses
      // (first & last IP address of each class)
      "(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])" +
      "(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}" +
      "(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))" +
      "|" +
      // host name
      "(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)" +
      // domain name
      "(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*" +
      // TLD identifier
      "(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))" +
      ")" +
    // port number
    "(?::\\d{2,5})?" +
    // resource path
    "(?:/\\S*)?" +
    "$", "i"
    );
  //
  utils.MONTHS = new Array('January', 'February', 'March',
    'April', 'May', 'June', 'July', 'August', 'September',
    'October', 'November', 'December');

  utils.queryFilter = {
    start: null,
    end: null,
    status: null,
    max: null,
    sortField: null,
    sortOrder: null,
    offset: null,
    all: false,
    toQuery: function () {
      return utils.toParamString(this);
    }
  };

  utils.errorObj = {
    errors:{
      entry: []
    },
    success: false,
    add: function(key, value){
      this.errors.entry.push({'key': key, 'value':value});
    }
  };

  utils.openWindow  = function(url, name, args, popupWin, errorBody) {

    if (typeof(popupWin) != "object"){
      popupWin = window.open(url,name,args);
    } else {
      if (!popupWin.closed){ 
        popupWin.close();
        popupWin = window.open(url, name,args);
      } else {
        popupWin = window.open(url, name,args);
      }
    }
    if (!popupWin) {
      popupWin = false;
      errorBody = errorBody || 'body';
      triggerAlert('A popup blocker is stopping this site from opening a new window.<br><br>To allow the site to open the new window, please turn off popublockers or add this site as an exception.', 'popupblocker', errorBody, 10000)
    } else {
      popupWin.focus();
    }
    return popupWin;
  }

})(window);