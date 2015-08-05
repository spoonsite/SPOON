// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function(config) {
  config.set({
    // base path, that will be used to resolve files and exclude
    basePath: '',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],

    // list of files / patterns to load in the browser
    files: [
      'app/bower_components/jquery/dist/jquery.js',
      'app/bower_components/angular/angular.js',
      'app/bower_components/json3/lib/json3.js',
      'app/bower_components/bootstrap/dist/js/bootstrap.js',
      'app/bower_components/angular-resource/angular-resource.js',
      'app/bower_components/angular-cookies/angular-cookies.js',
      'app/bower_components/angular-sanitize/angular-sanitize.js',
      'app/bower_components/angular-route/angular-route.js',
      'app/bower_components/angular-mocks/angular-mocks.js',
      'app/bower_components/angular-touch/angular-touch.js',
      'app/bower_components/angular-animate/angular-animate.js',
      'app/bower_components/angular-file-upload/angular-file-upload.js',
      'app/bower_components/ng-tags-input/ng-tags-input.min.js',
      'app/bower_components/moment/moment.js',
      'app/bower_components/angular-moment/angular-moment.js',
      'app/bower_components/ng-grid/build/ng-grid.js',
      'app/bower_components/angular-loading-bar/build/loading-bar.js',
      'app/bower_components/lodash/dist/lodash.compat.js',
      'app/bower_components/angular-carousel/dist/angular-carousel.js',
      'app/bower_components/jquery-waypoints/waypoints.js',
      'app/bower_components/SHA-1/sha1.js',
      'app/bower_components/angulartics/src/angulartics.js',
      'app/bower_components/angulartics/src/angulartics-adobe.js',
      'app/bower_components/angulartics/src/angulartics-chartbeat.js',
      'app/bower_components/angulartics/src/angulartics-flurry.js',
      'app/bower_components/angulartics/src/angulartics-ga-cordova.js',
      'app/bower_components/angulartics/src/angulartics-ga.js',
      'app/bower_components/angulartics/src/angulartics-gtm.js',
      'app/bower_components/angulartics/src/angulartics-kissmetrics.js',
      'app/bower_components/angulartics/src/angulartics-mixpanel.js',
      'app/bower_components/angulartics/src/angulartics-piwik.js',
      'app/bower_components/angulartics/src/angulartics-scroll.js',
      'app/bower_components/angulartics/src/angulartics-segmentio.js',
      'app/bower_components/angulartics/src/angulartics-splunk.js',
      'app/bower_components/angulartics/src/angulartics-woopra.js',
      'app/bower_components/angulartics/src/angulartics-marketo.js',
      'app/bower_components/ng-idle/angular-idle.js',
      'app/bower_components/angular-filter/dist/angular-filter.js',
      'app/bower_components/videogular/videogular.js',
      'app/bower_components/videogular-controls/vg-controls.js',
      'app/bower_components/videogular-buffering/vg-buffering.js',
      'app/bower_components/videogular-poster/vg-poster.js',
      'app/bower_components/videogular-overlay-play/vg-overlay-play.js',
      'app/bower_components/jquery-cycle2/build/jquery.cycle2.js',
      'app/bower_components/jQuery.dotdotdot/src/js/jquery.dotdotdot.js',
      'app/bower_components/angular-bindonce/bindonce.js',
      'app/bower_components/d3/d3.js',
      'app/scripts/common/ckeditor/ckeditor.js',
      'app/scripts/**/*.js',
      'test/spec/controllers/**/*.js',
      // 'test/spec/directives/**/*.js',
      'test/spec/filters/**/*.js',
      'test/spec/services/**/*.js',
      // 'test/spec/protractor/**/*.js' // e2e tests
    ],

    // list of files / patterns to exclude
    exclude: [],

    // web server port
    port: 8081,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: false,


    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: ['PhantomJS'],


    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false
  });
};
