exports.config = {
  //seleniumAddress: 'http://localhost:4444/wd/hub',
  specs: [
    'spec/protractor/*_spec.js'
  ],
  onPrepare: function() {
   // browser.driver.manage().window().maximize();
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
    allScriptsTimeout: 15000
  }
};
