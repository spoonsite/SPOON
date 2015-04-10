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

/* global resetAnimGlobals, initiateClick */
/* exported app */

/***************************************************************
* This is where THE app is configured
***************************************************************/
var app = angular
// Here we add the dependancies for the app
.module('submissionApp',
  [
    // dependency injection list
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ui.bootstrap',
    'mgcrea.ngStrap',
    'ngTagsInput',
    'ngAnimate',
    'ngCkeditor',    
    'ngGrid',
    'bootstrapLightbox',
    'angular-carousel',
    'angulartics.google.analytics',
    'angularFileUpload',
    'ngIdle',    
    'multi-select',
    'angular.filter',
    'notifications'
  // end of dependency injections
  ]
// end of the module creation
)
// Here we configure the route provider
.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {

  $locationProvider
  .html5Mode(true);


//   //disable IE ajax request caching
//   //initialize get if not there
//   if (!$httpProvider.defaults.headers.get) {
//     $httpProvider.defaults.headers.get = {};    
//   }
//   $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
//   $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
//   $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';


//   // add the errorhandling interceptor
//   $httpProvider.interceptors.push('httpStatusCodeInterceptorFactory');

//  //Activate your interceptor 
// // $httpProvider.responseInterceptors.push(httpStatusCodeInterceptorFactory);

//   // Here we adjust the tags module
//   tagsInputConfigProvider
//   .setDefaults('tagsInput', {
//     placeholder: 'Add a tag (single space for suggestions)'
//     // Use this to disable the addition of tags outside the tag cloud:
//     // addOnEnter: false
//   })
//   .setDefaults('autoComplete', {
//     maxResultsToShow: 15
//     // debounceDelay: 1000
//   })
//   .setActiveInterpolation('tagsInput', {
//     placeholder: true,
//     addOnEnter: true,
//     removeTagSymbol: true
//   });

//   // Angular Lightbox setup
//   // set a custom template
//   LightboxProvider.templateUrl = 'views/lightbox/lightbox.html';
//   *
//   * Calculate the max and min limits to the width and height of the displayed
//   *   image (all are optional). The max dimensions override the min
//   *   dimensions if they conflict.
//   * @param  {Object} dimensions Contains the properties windowWidth,
//   *   windowHeight, imageWidth, imageHeight.
//   * @return {Object} May optionally contain the properties minWidth,
//   *   minHeight, maxWidth, maxHeight.

//   LightboxProvider.calculateImageDimensionLimits = function (dimensions) {
//     return {
//       'minWidth': 100,
//       'minHeight': 100,
//       'maxWidth': dimensions.windowWidth - 102,
//       'maxHeight': dimensions.windowHeight - 136
//     };
//   };

//   /**
//   * Calculate the width and height of the modal. This method gets called
//   *   after the width and height of the image, as displayed inside the modal,
//   *   are calculated. See the default method for cases where the width or
//   *   height are 'auto'.
//   * @param  {Object} dimensions Contains the properties windowWidth,
//   *   windowHeight, imageDisplayWidth, imageDisplayHeight.
//   * @return {Object} Must contain the properties width and height.
//   */
//   LightboxProvider.calculateModalDimensions = function (dimensions) {
//     return {
//       'width': Math.max(500, dimensions.imageDisplayWidth + 42),
//       'height': Math.max(500, dimensions.imageDisplayHeight + 76)
//     };
//   };

//   // set up the idleProvider and keepaliveProvider
//   $idleProvider.idleDuration(30 * 60);
//   $idleProvider.warningDuration(60);
//   $keepaliveProvider.interval(14 * 60);


}])

// here we add the .run function for intial setup and other useful functions
.run(
  [
    // dependency list
    '$rootScope',
    'localCache',
    'business',
    '$location',
    '$route',
    '$timeout',
    '$q',
    '$anchorScroll',
    '$routeParams',
    '$analytics',
    '$idle',
    '$keepalive',
    '$uiModal',

  function ($rootScope, localCache, Business, $location, $route, $timeout, $q, $anchorScroll, $routeParams, $analytics, $idle, $keepalive, $uiModal) {/* jshint unused: false*/ //
    console.log('This is a test');
    $rootScope.current = 'top';
    $anchorScroll.yOffset = 100;   // always scroll by 50 extra pixels
    
  } // end of run function
] // end of injected dependencies for .run
).directive('a',[ function() {
  return {
    restrict: 'E',
    link: function(scope, elem, attrs) {
      if(attrs.ngClick || attrs.href === '' || attrs.href === '#'){
        elem.on('click', function(e){
          e.preventDefault();
        });
      }
    }
  };
}]);