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

/* global resetAnimGlobals */
/* exported app */

/***************************************************************
* This is where THE app is configured
***************************************************************/
var app = angular
// Here we add the dependancies for the app
.module('openstorefrontApp', ['ngCookies', 'ngResource', 'ngSanitize', 'ngRoute', 'ui.bootstrap', 'mgcrea.ngStrap', 'ngTagsInput', 'ngAnimate', 'ngCkeditor', 'ngGrid' , 'ngMockE2E', 'bootstrapLightbox' ])
// Here we configure the route provider
.config(function ($routeProvider, tagsInputConfigProvider, LightboxProvider) {
  $routeProvider
  .when('/', {
    templateUrl: 'views/main.html',
    controller: 'MainCtrl'
  })
  .when('/userprofile', {
    templateUrl: 'views/userprofile.html',
    controller: 'UserProfileCtrl'
  })
  .when('/results', {
    templateUrl: 'views/results.html',
    controller: 'ResultsCtrl'
  })
  .when('/admin', {
    templateUrl: 'views/admin.html',
    controller: 'AdminCtrl'
  })
  .when('/landing', {
    templateUrl: 'views/landing.html',
    controller: 'LandingCtrl'
  })
  .when('/single', {
    templateUrl: 'views/single.html',
    controller: 'SingleCtrl'
  })
  .when('/login', {
    templateUrl: 'views/login.html',
    controller: 'LoginCtrl'
  })
  .when('/register', {
    templateUrl: 'views/login.html',
    controller: 'LoginCtrl'
  })
  .otherwise({
    redirectTo: '/'
  });
  
  // /**
  // * Global errore
  // */
  // $httpProvider.interceptors.push(function($q) {
  //   return {
  //     'responseError': function(response) {
  //       //Handle the error (Mainly unexpected other may need different handling)

  //       //TODO: Add handling

  //       // if (canRecover(rejection)) {
  //         return response || $q.when(response);
  //       // }
  //       // return $q.reject(rejection);
  //       }
  //   };
  // });

  tagsInputConfigProvider
  .setDefaults('tagsInput', {
    placeholder: 'Add a tag (single space for suggestions)'
    // Use this to disable the addition of tags outside the tag cloud:
    // addOnEnter: false
  })
  .setDefaults('autoComplete', {
    maxResultsToShow: 15
    // debounceDelay: 1000
  })
  .setActiveInterpolation('tagsInput', {
    placeholder: true,
    addOnEnter: true,
    removeTagSymbol: true
  });

  // set a custom template
  LightboxProvider.templateUrl = 'views/lightbox/lightbox.html';

  /**
   * Calculate the max and min limits to the width and height of the displayed
   *   image (all are optional). The max dimensions override the min
   *   dimensions if they conflict.
   * @param  {Object} dimensions Contains the properties windowWidth,
   *   windowHeight, imageWidth, imageHeight.
   * @return {Object} May optionally contain the properties minWidth,
   *   minHeight, maxWidth, maxHeight.
   */
  LightboxProvider.calculateImageDimensionLimits = function (dimensions) {
    return {
      'minWidth': 100,
      'minHeight': 100,
      'maxWidth': dimensions.windowWidth - 102,
      'maxHeight': dimensions.windowHeight - 136
    };
  };

  /**
   * Calculate the width and height of the modal. This method gets called
   *   after the width and height of the image, as displayed inside the modal,
   *   are calculated. See the default method for cases where the width or
   *   height are 'auto'.
   * @param  {Object} dimensions Contains the properties windowWidth,
   *   windowHeight, imageDisplayWidth, imageDisplayHeight.
   * @return {Object} Must contain the properties width and height.
   */
  LightboxProvider.calculateModalDimensions = function (dimensions) {
    return {
      'width': Math.max(500, dimensions.imageDisplayWidth + 42),
      'height': Math.max(500, dimensions.imageDisplayHeight + 76)
    };
  };
})
// here we add the .run function for intial setup and other useful functions

.run(['$rootScope', 'localCache', 'business',  '$location', '$route', '$timeout', '$httpBackend','$q', function ($rootScope, localCache, Business, $location, $route, $timeout, $httpBackend, $q) {/* jshint unused: false*/

  $rootScope._scopename = 'root';

  //We must initialize global scope variables.
  $rootScope.Current = null;


  /***************************************************************
  * This function watches for a route change start and does a few things
  * params: event -- keeps track of which event is happening
  * params: next -- The next route we're headed for
  * params: current -- The current route we're at
  * 
  *
  * This is where we re-apply some functions on route-change
  *
  * This might also be where we do our 'is-logged-in' check
  ***************************************************************/
  $rootScope.$on('$routeChangeStart', function (event, next, current) {/* jshint unused:false */
    if (current && current.loadedTemplateUrl === 'views/results.html') {
      resetAnimGlobals();
    }

    setTimeout(function () {
      $('.searchBar:input[type=\'text\']').on('click', function () {
        $(this).select();
      });
    }, 500);
  });

  /***************************************************************
  * This funciton resets the search query when we don't want to be showing it
  ***************************************************************/
  $rootScope.$on('$locationChangeStart', function (event, next, current) {
    // console.log('path', $location.path());
    // console.log($location.path() === '/');
    // console.log('next', next);
    // console.log('current', current);
    // console.log('path', $location.path());
    if (!$location.path() || ($location.path() !== '/results' && $location.path() !== '/single')) {
      $location.search({});
    }
    if (!$location.path() || $location.path() === '/') {
      // console.log('Broadcasting');
      $rootScope.$broadcast('$changenav', 'views/nav/nav_main.html');
    } else {
      $rootScope.$broadcast('$changenav', 'views/nav/nav.html');
    }
  });


  /***************************************************************
  * This function is what is called when the view has finally been loaded
  ***************************************************************/
  $rootScope.$on('$viewContentLoaded', function() {
    $rootScope.typeahead = Business.typeahead();
    
    // setupParallax();

    $timeout(function() {
      $('[data-toggle="tooltip"').tooltip();
      if (!$location.path() || $location.path() === '/') {
        // console.log('Broadcasting');
        $rootScope.$broadcast('$changenav', 'views/nav/nav_main.html');
      } else {
        $rootScope.$broadcast('$changenav', 'views/nav/nav.html');
      }
    }, 300);
  });

  /***************************************************************
  * This function is what is called when the modal event is fired
  ***************************************************************/
  $rootScope.$on('$viewModal', function(event, id) {
    $('#' + id).modal('show');
  });

  /***************************************************************
  * These functions trigger and untrigger loading masks
  ***************************************************************/
  $rootScope.$on('$TRIGGERLOAD', function(event, value){
    $timeout(function() {
      $rootScope.$broadcast('$LOAD', value);
    }, 10);
  });
  $rootScope.$on('$TRIGGERUNLOAD', function(event, value){
    $timeout(function() {
      $rootScope.$broadcast('$UNLOAD', value);
    }, 10);
  });

  $rootScope.openModal = function(id, current) {
    $rootScope.current = current;
    $rootScope.$broadcast('$' + id);
    $rootScope.$broadcast('updateBody');
    $rootScope.$broadcast('$viewModal', id);
  };

  $rootScope.setNav = function() {
    // once the content is loaded, make sure we have the right navigation!
    if (!$location.path() || $location.path() === '/') {
      $rootScope.$broadcast('$changenav', 'views/nav/nav_main.html');
    } else {
      $rootScope.$broadcast('$changenav', 'views/nav/nav.html');
    }
  };

  $rootScope.setNav();
  
  
  //Mock Back End  (use passthough to route to server)
  $httpBackend.whenGET(/views.*/).passThrough();
  
  $httpBackend.whenGET('/openstorefront-web/api/v1/resource/userprofiles/CURRENTUSER').respond(MOCKDATA.userProfile);
  $httpBackend.whenGET('/openstorefront-web/api/v1/resource/lookup/UserTypeCode').respond(MOCKDATA.userTypeCodes);
  
  ////////////////////////////////////////////////////////////////////////

  $rootScope.setupModal = function(modal, classNames) {
    var deferred = $q.defer();
    if (classNames !== '') {
      modal.classes = classNames;
      modal.nav = {
        'current': 'Write a Review',
        'bars': [
          //
          {
            'title': 'Write a Review',
            'include': 'views/reviews/newfeedback.html'
          }
        //  
        ]
      };
      deferred.resolve();
    } else {
      modal.nav = '';
      deferred.resolve();
    }

    if (classNames === '' && modal.isLanding) {
      modal.classes = 'fullWidthModal';
    } else if (classNames === '') {
      modal.classes = '';
    }
    return deferred.promise;
  };

}]);

