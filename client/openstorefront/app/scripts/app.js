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

/* global resetAnimGlobals, initiateClick, MOCKDATA2 */
/* exported app */

/***************************************************************
* This is where THE app is configured
***************************************************************/
var app = angular
// Here we add the dependancies for the app
.module('openstorefrontApp',
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
    'ngMockE2E',
    'bootstrapLightbox',
    'angular-carousel',
    'angulartics.google.analytics',
    'ngIdle'
  // end of dependency injections
  ]
// end of the module creation
)
// Here we configure the route provider
.config(['$routeProvider', 'tagsInputConfigProvider', 'LightboxProvider', '$keepaliveProvider', '$idleProvider', function ($routeProvider, tagsInputConfigProvider, LightboxProvider, $keepaliveProvider, $idleProvider) {
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
  .when('/compare', {
    templateUrl: 'views/compare.html',
    controller: 'CompareCtrl'
  })
  .otherwise({
    redirectTo: '/'
  });
  // /**
  // * Global error handling
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

  // Here we adjust the tags module
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

  // Angular Lightbox setup
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

  // set up the idleProvider and keepaliveProvider
  $idleProvider.idleDuration(30 * 60);
  $idleProvider.warningDuration(60);
  $keepaliveProvider.interval(20 * 60);


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
    '$httpBackend',
    '$q',
    'auth',
    '$anchorScroll',
    '$routeParams',
    '$analytics',
    '$idle',
    '$keepalive',
    '$uiModal',
    function ($rootScope, localCache, Business, $location, $route, $timeout, $httpBackend, $q, Auth, $anchorScroll, $routeParams, $analytics, $idle, $keepalive, $uiModal) {/* jshint unused: false*/

      // initialization stuff.
      $rootScope.ieVersionCheck = false;
      $rootScope.loaded = false;

      $timeout(function() {
        // this is called only on first view of the '/' route (login)
        localCache.clearAll();

        // grab the 'current user'
        Business.userservice.initializeUser().then(function(result){
          if (result) {
            // Google Analytics Tracking Code
            (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
              (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
              m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
            if (result.username) {
              ga('create', 'UA-48252919-8', { 'userId': result.username });
            } else {
              ga('create', 'UA-48252919-8', 'auto');
            }
            ga('require', 'displayfeatures');
            $rootScope.$broadcast('$LOGGEDIN', result);
            $rootScope.$broadcast('$beforeLogin', $location.path(), $location.search());
          }
        });
        Business.ieCheck().then(function(result){
          $rootScope.ieVersionCheck = result;
          $rootScope.loaded = true;
        }, function(){
          $rootScope.ieVersionCheck = false;
          $rootScope.loaded = true;
        })
      }, 10);


      //////////////////////////////////////////////////////////////////////////////
      // Variables
      //////////////////////////////////////////////////////////////////////////////
      $rootScope._scopename = 'root';
      $rootScope.Current    = null;


      $rootScope.$on('$triggerEvent', function(event, newEvent, infoArray){
        $rootScope.$broadcast(newEvent, infoArray);
      });


      //////////////////////////////////////////////////////////////////////////////
      // Event Handlers
      //////////////////////////////////////////////////////////////////////////////
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
        $rootScope.$broadcast('$LOAD', 'bodyLoad');
      });

      $rootScope.$on('$routeChangeSuccess', function (event, next, current){
        $rootScope.$broadcast('$UNLOAD', 'bodyLoad');
      });



      /***************************************************************
      * This funciton resets the search query when we don't want to be showing it
      ***************************************************************/
      $rootScope.$on('$locationChangeStart', function (event, next, current) {
        if (!$location.path() || ($location.path() !== '/results' && $location.path() !== '/single' && $location.path() !== '/landing' && $location.path() !== '/compare')) {
          $location.search({});
        }
      });

      /***************************************************************
      * This function is what is called when the view has finally been loaded
      ***************************************************************/
      $rootScope.$on('$viewContentLoaded', function() {
        Business.componentservice.getComponentDetails().then(function(result) {
          Business.typeahead(result, null).then(function(value){
            $rootScope.typeahead = value;
          });
        });
        
        $timeout(function() {
          $('[data-toggle=\'tooltip\']').tooltip();
        }, 300);
        $timeout(function() {
          $rootScope.$broadcast('$UNLOAD', 'bodyLoad');
        });
      });

      /***************************************************************
      * This function is what is called when the modal event is fired
      ***************************************************************/
      $rootScope.$on('$viewModal', function(event, id) {
        $('#' + id).modal('show');
      });

      /***************************************************************
      * This function is what is called when the modal event is fired
      ***************************************************************/
      $rootScope.$on('$hideModal', function(event, id) {
        $('#' + id).modal('hide');
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
      $rootScope.$on('$TRIGGEREVENT', function(event, trigger, data, data2){
        $timeout(function() {
          $rootScope.$broadcast(trigger, data, data2);
        }, 10);
      });


      //////////////////////////////////////////////////////////////////////////////
      // Functions
      //////////////////////////////////////////////////////////////////////////////
      $rootScope.sendPageView = function(view) {
        // console.log('we got a page view', view);
        
        $analytics.pageTrack($location.url()+'/'+view);
      };

      // All three arguments are required 
      $rootScope.sendEvent = function(name, category, label) {
        if (name === 'Filter Checked'){
          label = label? 'checked': 'unChecked';
        }
        
        // console.log('we got an event', name, category, label);
        $analytics.eventTrack(name,{'category': category, 'label': label});
      };


      $rootScope.openModal = function(id, current) {
        $rootScope.current = current;
        $rootScope.$broadcast('$' + id);
        $rootScope.$broadcast('updateBody');
        $rootScope.$broadcast('$viewModal', id);
        $rootScope.sendPageView('Modal-'+id+'-'+current);
      }; 

      $rootScope.setComponentId = function(id) {
        // console.log('We set the id');
        
        $rootScope.refId = id;
      };

      $rootScope.getComponentId = function() {
        // console.log('We got the id');
        return $rootScope.refId;
      };

      $rootScope.scrollTo = function(id) {
        var old = $location.hash();
        $location.hash(id);
        $anchorScroll();
        //reset to old to keep any additional routing logic from kicking in
        $location.hash(old);
      };

      /***************************************************************
      * This function sends the route to whatever path and search are passed in.
      ***************************************************************/
      $rootScope.goTo = function(path, search) {
        $location.search(search);
        $location.path(path);
      };

      /***************************************************************
      * This function will imitate a click on the provided id
      * In this case we're specifically targeting links in the navigation
      * because the click isn't propegated as it should be.
      ***************************************************************/
      $rootScope.closeNavbarItem = function(id) {
        initiateClick(id);
      };

      $rootScope.log = function(message, item) {
        console.log(message, item);
      }

      /***************************************************************
      * This function assists the modal setup when content is changed.
      ***************************************************************/
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

      /***************************************************************
      * This function converts a timestamp to a displayable date
      ***************************************************************/
      $rootScope.getDate = function(date){
        if (date)
        {
          var d = new Date(date);
          var currDate = d.getDate();
          var currMonth = d.getMonth();
          var currYear = d.getFullYear();
          return ((currMonth + 1) + '/' + currDate + '/' + currYear);
        }
        return null;
      };

      /***************************************************************
      * This is a local function used in the httpBackend functions
      ***************************************************************/
      var getParams = function(url) {
        var match,
        pl     = /\+/g,  // Regex for replacing addition symbol with a space
        search = /([^&=]+)=?([^&]*)/g,
        decode = function (s) { return decodeURIComponent(s.replace(pl, ' ')); },
        query  = url.split('?')[1],
        urlParams = {};

        match = search.exec(query);
        while (match) {
          urlParams[decode(match[1])] = decode(match[2]);
          match = search.exec(query);
        }
        return urlParams;
      };


      //////////////////////////////////////////////////////////////////////////////
      // HttpBackend
      //////////////////////////////////////////////////////////////////////////////
      //Mock Back End  (use passThrough to route to server)
      $httpBackend.whenGET(/views.*/).passThrough();
      
      $httpBackend.whenGET(/api\/v1\/resource\/userprofiles\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenGET('System.action?UserAgent').passThrough();
      $httpBackend.whenPUT(/api\/v1\/resource\/userprofiles\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenGET('api/v1/resource/lookup/UserTypeCodes').respond(MOCKDATA.userTypeCodes);
      $httpBackend.whenGET(/api\/v1\/resource\/component\/search\/\?.*/).respond(function(method, url, data) {
        // var query = getParams(url);
        // var result = null;
        // // console.log('query Parameters', query);
        // // console.log('Key', query.key);
        // if (query.type === 'search' && (query.key === 'all' || query.key === 'All'))
        // {
        //   query.key = '';
        // }
        // if (query.key !== '' && query.type === 'search') {
        //   result = _.filter(MOCKDATA2.resultsList, function(item) {
        //     return _.contains(item.name, query.key) || _.contains(item.description, query.key) || _.contains(item.owner, query.key);
        //   });
        // } else if (query.type && query.type === 'search'){
        //   result = MOCKDATA2.resultsList;
        // } else if (query.type){
        //   result = _.filter(MOCKDATA2.resultsList, function(item){
        //     return _.some(item.attributes, function(code) {
        //       if (code.type === query.type) {
        //         return code.code === query.key;
        //       } else {
        //         return false;
        //       }
        //     });
        //   });
        // }
        return [200, result, {}];
      });
      //
      $httpBackend.whenGET('api/v1/resource/components').passThrough();
      $httpBackend.whenGET('api/v1/resource/highlights').passThrough();
      $httpBackend.whenGET('api/v1/service/search/recent').passThrough();
      $httpBackend.whenGET('api/v1/service/search/all').passThrough();
      $httpBackend.whenGET(/api\/v1\/resource\/components\/[^\/][^\/]*\/?detail/).passThrough();
      $httpBackend.whenGET('api/v1/resource/components/tags').passThrough();
      $httpBackend.whenGET(/api\/v1\/resource\/components\/[^\/][^\/]*\/?tags/).passThrough();
      $httpBackend.whenPOST(/api\/v1\/resource\/components\/[^\/][^\/]*\/?tags/).passThrough();
      $httpBackend.whenDELETE(/api\/v1\/resource\/components\/[^\/][^\/]*\/?tags\/text/).passThrough();
      $httpBackend.whenDELETE(/api\/v1\/resource\/components\/[^\/][^\/]*\/?tags\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenPOST(/api\/v1\/resource\/components\/[^\/][^\/]*\/?tags\/list/).passThrough();
      $httpBackend.whenPOST(/api\/v1\/resource\/components\/[^\/][^\/]*\/?questions/).passThrough();
      $httpBackend.whenPUT(/api\/v1\/resource\/components\/[^\/][^\/]*\/?questions\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenDELETE(/api\/v1\/resource\/components\/[^\/][^\/]*\/?questions\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenDELETE(/api\/v1\/resource\/components\/[^\/][^\/]*\/?questions\/[^\/][^\/]*\/?responses\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenPOST(/api\/v1\/resource\/components\/[^\/][^\/]*\/?questions\/[^\/][^\/]*\/?responses\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenPUT(/api\/v1\/resource\/userprofiles\/[^\/][^\/]*\/?watches\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenDELETE(/api\/v1\/resource\/userprofiles\/[^\/][^\/]*\/?watches\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenPOST(/api\/v1\/resource\/userprofiles\/[^\/][^\/]*\/?watches/).passThrough();
      $httpBackend.whenGET(/api\/v1\/resource\/userprofiles\/[^\/][^\/]*\/?watches/).passThrough();
      $httpBackend.whenPUT(/api\/v1\/resource\/components\/[^\/][^\/]*\/?response\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenPOST(/api\/v1\/resource\/components\/[^\/][^\/]*\/?reviews/).passThrough();
      $httpBackend.whenPUT(/api\/v1\/resource\/components\/[^\/][^\/]*\/?reviews\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenDELETE(/api\/v1\/resource\/components\/[^\/][^\/]*\/?reviews\/[^\/][^\/]*\/?/).passThrough();
      $httpBackend.whenPOST('api/v1/resource/components/reviews/ANONYMOUS').passThrough();
      $httpBackend.whenPOST(/api\/v1\/resource\/components\/[^\/][^\/]*\/?reviews\/[^\/][^\/]*\/?pro/).passThrough();
      $httpBackend.whenPOST(/api\/v1\/resource\/components\/[^\/][^\/]*\/?reviews\/[^\/][^\/]*\/?con/).passThrough();
      $httpBackend.whenGET(/api\/v1\/resource\/attributes\/attributetypes\/[^\/][^\/]*\/?attributecodes\/[^\/][^\/]*\/?article/).passThrough();
      $httpBackend.whenGET(/api\/v1\/resource\/attributes\/attributetypes\/[^\/][^\/]*\/?architecture/).passThrough();
      // $httpBackend.whenGET(/api\/v1\/resource\/components\/[^\/][^\/]*\/?detail/).respond(function(method, url, data) {
      //   // grab the url (needed for what the backend will simulate)
      //   // parse it into an array
      //   var urlSplit = url.split('/');
      //   var i = 0;
      //   // go until we find our resource
      //   while (urlSplit[i++] !== 'component'){}
      //     // if there is an id, grab it for our use.
      //   var id = urlSplit[i]? parseInt(urlSplit[i]) : null;

      //   var result = $q.defer();
      //   $timeout(function() {
      //     if (id && id !== '') {
      //       var temp = _.find(MOCKDATA2.componentList, {'componentId': id});
      //       result.resolve(temp);
      //     } else {
      //       result.resolve(MOCKDATA2.componentList);
      //     }
      //   }, 1000);
      //   return [200, result.promise, {}];
      // });

$httpBackend.whenGET('api/v1/resource/attributes').passThrough();
      // .respond(function(method, url, data) {
      //   return [200, MOCKDATA.filters, {}];
      // });

      // respond(function(method, url, data) {
      //   return [200, MOCKDATA.tagsList, {}];
      // });

$httpBackend.whenGET('api/v1/resource/pros').respond(function(method, url, data) {
  return [200, MOCKDATA.prosConsList, {}];
});

$httpBackend.whenGET('api/v1/resource/attributes/attributetypes/DI2ELEVEL/attributecodes').passThrough();
      // respond(function(method, url, data) {
      //   var result = _.find(MOCKDATA.filters, {'type':'DI2ELEVEL'});
      //   return [200, result, {}];
      // });

$httpBackend.whenGET(/api\/v1\/resource\/lookuptypes\/[^\/][^\/]*\/?view/).passThrough();
      // respond(function(method, url, data) {
      //   var result = [
      //     //
      //     {'value':'1', 'label': 'Less than 1 month'},
      //     {'value':'2', 'label': 'Less than 3 months'},
      //     {'value':'3', 'label': 'Less than 6 months'},
      //     {'value':'4', 'label': 'Less than 1 year'},
      //     {'value':'5', 'label': 'Less than 3 years'},
      //     {'value':'6', 'label': 'More than 3 years'}
      //   //
      //   ];
      //   return [200, result, {}];
      // });

$httpBackend.whenGET('api/v1/resource/lookup/watches').respond(function(method, url, data) {
  return [200, MOCKDATA.watches, {}];
});

$httpBackend.whenPOST('api/v1/resource/lookup/watches').respond(function(method, url, data) {
  MOCKDATA.watches = data;
  return [200, angular.fromJson(data), {}];
});


      $rootScope.started = false;

      $rootScope.closeModals = function() {
        if ($rootScope.warning) {
          $rootScope.warning.close();
          $rootScope.warning = null;
        }

        if ($rootScope.timedout) {
          $rootScope.timedout.close();
          $rootScope.timedout = null;
        }
      }

      $rootScope.logout = function() {
        window.location.replace('/openstorefront/Login.action?Logout');
      }

      $rootScope.$on('$idleStart', function() {
        $rootScope.closeModals();

        $rootScope.warning = $uiModal.open({
          templateUrl: 'views/timeout/warning-dialog.html',
          windowClass: 'modal-danger'
        });
      });

      $rootScope.$on('$idleEnd', function() {
        $rootScope.closeModals();
        // no need to do anything unless you want to here.
      });

      $rootScope.$on('$keepalive', function() {
        // do something to keep the user's session alive
        Business.userservice.getCurrentUserProfile(true);
      });

      $rootScope.$on('$idleTimeout', function() {
        //log them out here
        $rootScope.closeModals();
        $rootScope.logout();
      });

      // $rootScope.start = function() {
      //   closeModals();
      //   $idle.watch();
      //   $rootScope.started = true;
      // };

      // $rootScope.stop = function() {
      //   closeModals();
      //   $idle.unwatch();
      //   $rootScope.started = false;
      // };
      
      $idle.watch();


    } // end of run function
  ] // end of injected dependencies for .run
); // end of app module