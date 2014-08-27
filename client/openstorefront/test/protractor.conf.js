// For the HTML reporter
var HtmlReporter = require('protractor-html-screenshot-reporter');
var path = require('path');

exports.config = {
  //seleniumAddress: 'http://localhost:4444/wd/hub',
  specs: [
    'spec/protractor/*_spec.js'
  ],
  onPrepare: function() {
    //browser.driver.manage().window().maximize();
    browser.driver.manage().window().setSize(1180,1180);

    // For the HTML Reporter
    require('../node_modules/jasmine-reporters');

    // Add a reporter and store xml to 'reports'
    jasmine.getEnv().addReporter(new jasmine.JUnitXmlReporter({ baseDirectory: 'reports' }));

    // Add a reporter and store screenshots to a directory
    // See http://qainsight.blogspot.com/2014/03/adding-html-reporter-in-protractor-js.html
      jasmine.getEnv().addReporter(new HtmlReporter({
          baseDirectory: 'automation_logs', pathBuilder: function pathbuilder (spec, descriptions, results, capabilities)
          {
            // Build this format:  YYYY-MMM-DD_HH-MM-SS;  2014-Aug-28_16-35-56 GMT -0600 (Mountain Daylight Time)
            var dt = new Date().toDateString().split(' ');
            var tm = new Date().toTimeString().split(':');
            myDateFormat = dt[3] + '-' + dt[1] + '-' + dt[2];
            myTimeFormat = tm[0] + '-' + tm[1] + '_';
            return path.join(myDateFormat, capabilities.caps_.browserName, myTimeFormat + descriptions.join('-'));
          }
      }));


  },
  
  
    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    
  capabilities : {
    browserName : 'chrome',
    'chromeOptions': {
        args: ['--test-type']
    }
  },
  jasmineNodeOpts: {
    onComplete: null,
    isVerbose: true,
    showColors: true,
    includeStackTrace: true,
    defaultTimeoutInterval: 30000,
    allScriptsTimeout: 18000
  }
};
