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
    jasmine.getEnv().addReporter(new HtmlReporter({ baseDirectory: 'screenshots', pathBuilder: function pathbuilder (
        spec, descriptions, results, capabilities) {
        var rightNow = new Date();
        //console.log(rightNow);

        var theYear = rightNow.getFullYear();
        var theMonth = rightNow.getMonth() + 1;
        var theDay = rightNow.getDay();
        var dtString = theYear + '-' + theMonth + '-' + theDay;

        return path.join(dtString, capabilities.caps_.browserName, descriptions.join('-'));

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
