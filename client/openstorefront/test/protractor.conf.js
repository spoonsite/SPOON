// For the HTML reporter
var HtmlReporter = require('protractor-html-screenshot-reporter');
var path = require('path');

exports.config = {
  //seleniumAddress: 'http://localhost:4444/wd/hub',
  specs: [
    'spec/protractor/*_spec.js'
  ],
  onPrepare: function() {
    browser.driver.manage().window().setSize(1180,1180);
    browser.driver.manage().window().setPosition(400,0);  // Get off of left corner where IDE usually is

    // For the HTML Reporter
    require('../node_modules/jasmine-reporters');

    // Add a reporter and store xml to 'reports'
    jasmine.getEnv().addReporter(new jasmine.JUnitXmlReporter({ baseDirectory: 'reports' }));

    // Add a reporter and store screenshots to a directory
    // See http://qainsight.blogspot.com/2014/03/adding-html-reporter-in-protractor-js.html
      jasmine.getEnv().addReporter(new HtmlReporter({
          baseDirectory: 'automation_logs', pathBuilder: function pathbuilder (spec, descriptions, results, capabilities)
          {
            // Build this format:  YYYY-MMM-DD_HH-MM-SS;  2014-Aug-28
            var dt = new Date().toDateString().split(' ');
            var tm = new Date().toTimeString().split(':');
            myDateFormat = dt[3] + '-' + dt[1] + '-' + dt[2];
            myTimeFormat = tm[0] + '-' + tm[1] + '_';
            return path.join(myDateFormat, capabilities.caps_.browserName, myTimeFormat + descriptions.join('-'));
          }
      }));


  },

    jasmineNodeOpts: {
    onComplete: null,
    isVerbose: true,
    showColors: true,
    includeStackTrace: true,
    defaultTimeoutInterval: 61000,
    allScriptsTimeout: 18000
  },

    multiCapabilities: [
        // Will run up to 3 or 4 at a time on my machine
        // Options:  'chrome', 'firefox', 'internet explorer', 'opera', 'safari'
        //{browserName: 'firefox'},
        {browserName: 'chrome'}
    ]
        // 8 Aug '14 Firefox QUIT WORKING
        //browserName: 'internet explorer',
        //version: '11'

    /*  Changed to "multiCapabilities" 8/30/14
     capabilities : {
     browserName : 'chrome',
     'chromeOptions': {
     args: ['--test-type']
     }
     },
     */

};
