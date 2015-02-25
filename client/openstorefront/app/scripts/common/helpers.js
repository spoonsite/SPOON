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
  utils.getDate = function(date, toString, morning, monthYear){
    toString = toString || false;
    if (date && !toString)
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
    } else if (date){
      var d = new Date(date);

      var year = d.getFullYear();

      var month = d.getMonth() + 1;
      if(month <= 9) {
        month = '0'+month;
      }

      var day= d.getDate();
      if(day <= 9) {
        day = '0'+day;
      }

      var currDate = d.getFullYear() + '-' + month + '-' + day + 'T';
      
      if (morning){
        currDate += '00:00:01.000';
      } else {
        currDate += '23:59:59.999'
      }
      return currDate;
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

})(window);