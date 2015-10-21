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

/* global isEmpty, setupPopovers, openClick:true, setupResults,
fullClick, openFiltersToggle, buttonOpen, buttonClose, toggleclass, resetAnimations,
filtClick, setPageHeight*/

app.controller('ResultsCtrl', ['$scope', 'localCache', 'business', '$filter', '$timeout', '$location', '$rootScope', '$q', '$route', '$sce', function ($scope,  localCache, Business, $filter, $timeout, $location, $rootScope, $q, $route, $sce) { /*jshint unused: false*/

  //////////////////////////////////////////////////////////////////////////////
  // Here we put our variables...
  //////////////////////////////////////////////////////////////////////////////
  // set the page height so the loading masks look good.
  setPageHeight($('.page1'), 52);
  $scope.scrollTo = $rootScope.scrollTo;
  // start the loading masks
  $scope.$emit('$TRIGGERLOAD', 'mainLoader');
  // $scope.$emit('$TRIGGERLOAD', 'resultsLoad');
  $scope.$emit('$TRIGGERLOAD', 'filtersLoad');
  
  // set variables
  $scope._scopename         = 'results';
  $scope.orderProp          = '';
  $scope.query              = '';
  $scope.lastUsed           = new Date();
  $scope.modal              = {};
  $scope.details            = {};
  $scope.data               = {};
  $scope.isPage1            = true;
  $scope.showSearch         = false;
  $scope.showDetails        = false;
  $scope.showMessage        = false;
  $scope.modal.isLanding    = false;
  $scope.single             = false;
  $scope.isArticle          = false;
  $scope.searchCode         = null;
  $scope.filteredTotal      = null;
  $scope.searchTitle        = null;
  $scope.searchDescription  = null;
  $scope.details.details    = null;
  $scope.typeahead          = null;
  $scope.searchGroup        = null;
  $scope.searchKey          = null;
  $scope.filters            = null;
  $scope.resetFilters       = null;
  $scope.total              = null;
  $scope.ratingsFilter      = 0;
  $scope.rowsPerPage        = 200;
  $scope.pageNumber         = 1;
  $scope.maxPageNumber      = 1;
  $scope.noDataMessage      = $sce.trustAsHtml('<p>There are no results for your search</p> <p>&mdash; Or &mdash;</p> <p>You have filtered out all of the results.</p><button class="btn btn-default" ng-click="clearFilters()">Reset Filters</button>');
  $scope.showBreadCrumbs    = false;

  // grab what we need from the server.

  $scope.setupTagList = function() {
    Business.getTagsList(true).then(function(result) {
      if (result) {
        $scope.tagsList       = result;
        $scope.tagsList.sort();
      } else {
        $scope.tagsList       = null;
      }
    });
  }
  $scope.setupTagList();
  $scope.$on('$REFRESHTAGLIST', function(event) {
    $scope.setupTagList();
  })

  Business.getProsConsList().then(function(result) {
    if (result) {
      $scope.prosConsList   = result;
    } else {
      $scope.prosConsList   = null;
    }
  }, function(){
    $scope.prosConsList   = null;
  });
  Business.userservice.getWatches().then(function(result) {
    if (result) {
      
      $scope.watches        = result;
    } else {
      $scope.watches        = null;
    }
  }, function(result){
    
    $scope.watches        = null;
  });
  Business.lookupservice.getExpertise().then(function(result) {
    if (result) {
      $scope.expertise      = result;
    } else {
      $scope.expertise      = [];
    }
  }, function(){
    $scope.expertise      = [];
  });
  Business.lookupservice.getUserTypeCodes().then(function(result) {
    if (result) {
      $scope.userTypeCodes  = result;
    } else {
      $scope.userTypeCodes  = [];
    }
  }, function(){
    $scope.userTypeCodes  = [];
  });
  Business.componentservice.getComponentDetails().then(function(result) {
    Business.typeahead(result, 'name').then(function(value){
      if (value) {
        $scope.typeahead    = value;
      } else {
        $scope.typeahead    = [];
      }
    }, function(){
      $scope.typeahead    = [];
    });
  });

  //////////////////////////////////////////////////////////////////////////////
  // Here we put our Functions
  //////////////////////////////////////////////////////////////////////////////


  /***************************************************************
  * This function selects the initial tab.
  * params: tab -- The tab that is selected
  ***************************************************************/
  $scope.setSelectedTab = function(tab) {
    $scope.selectedTab = tab;
  };

  $scope.isItAnArticle = function(item){
    return item.listingType === 'Article' || item.componentType === 'ARTICLE';
  }

  $scope.getNumThings = function(article){
    if ($scope.data && $scope.data.data && $scope.data.data.length) {
      var count = 0;
      _.each($scope.data.data, function(item){
        if (article) {
          if ($scope.isItAnArticle(item)) {
            count++; 
          }
        } else if (!article) {
          if (!$scope.isItAnArticle(item))
            count++;
        }
      })
      return count;
    } else {
      return 0;
    }
  }


  /***************************************************************
  * Here we set the tab class
  * params: tab -- The tab to check to see if it is selected
  * returns: class -- The classes that the tab will have
  ***************************************************************/
  $scope.tabClass = function(tab) {
    if ($scope.selectedTab === tab) {
      return 'active';
    } else {
      return '';
    }
  };

  /***************************************************************
  * This function is looked at for auto suggestions for the tag list
  * if a ' ' is the user's entry, it will auto suggest the next 20 tags that
  * are not currently in the list of tags. Otherwise, it will look at the
  * string and do a substring search.
  * params: query -- The input that the user has typed so far
  * params: list -- The list of tags already tagged on the item
  * params: source -- The source of the tags options
  * returns: deferred.promise -- The promise that we will return a resolved tags list
  ***************************************************************/
  $scope.checkTagsList = function(query, list, source) {
    var deferred = $q.defer();
    var subList = null;
    if (query === ' ') {
      subList = _.reject(source, function(item) {
        return !!(_.where(list, {'text': item}).length);
      });
    } else {
      subList = _.filter(source, function(item) {
        return item.toLowerCase().indexOf(query.toLowerCase()) > -1;
      });
    }
    deferred.resolve(subList);
    return deferred.promise;
  };

  /***************************************************************
  * Description
  * params: param name -- param description
  * returns: Return name -- return description
  ***************************************************************/
  var getBody = function(route) {
    var deferred = $q.defer();
    $.get(route).then(function(responseData) {
      deferred.resolve(responseData);
    });
    return deferred.promise;
  };

  var setupFilters = function() {
    Business.getFilters(false, false).then(function(result) {
      
      if (result) {
        result = _.sortBy(result, function(item){
          return item.description;
        });
        $scope.filters = angular.copy(result);
      } else {
        $scope.filters = null;
      }
      setupResults();      
      var architecture = null;

      if ($scope.searchKey === 'attribute') {
        if ($scope.searchCode.type) {
          var filter = _.find($scope.filters, {'attributeType': $scope.searchCode.type});
          if (filter){
            architecture = filter.architectureFlg;
          }
        }
      }
      $scope.$emit('$TRIGGERUNLOAD', 'filtersLoad');
    });
  };
  /***************************************************************
  * This function is called once we have the search request from the business layer
  * The order and manner in which we do this call will most likely change once
  * we get the httpbackend fleshed out.
  ***************************************************************/
  $scope.reAdjust = function(key) {
    $scope.searchGroup        = key;
    $scope.searchKey          = $rootScope.searchKey;
    
    if (!isEmpty($scope.searchGroup)) {
      // grab all of the keys in the filters
      $scope.searchKey        = $scope.searchGroup[0].key;
      $scope.searchCode       = $scope.searchGroup[0].code;
    } else {
      $scope.searchKey        = 'search';
      $scope.searchCode       = '';
    }
    var architecture = false;
    Business.componentservice.doSearch($scope.searchKey, $scope.searchCode, architecture).then(function(result) {
      if (result && result.data && result.data.length > 0)
      {
        $scope.total = result.data || [];
      } else {
        $scope.total = [];
      }
      $scope.filteredTotal = $scope.total;

      /*Simulate wait for the filters*/
      /*This is simulating the wait time for building the data so that we get a loader*/
      $scope.data.data = $scope.total;
      _.each($scope.data.data, function(item){
        if (item.description !== null && item.description !== undefined && item.description !== '') {
          var desc = item.description.match(/^(.*?)[.?!]\s/);
          item.shortdescription = (desc && desc[0])? desc[0] + '.': item.description;
        } else {
          item.shortdescription = 'This is a temporary short description';
        }
      });
      $scope.setupData();
      setupFilters();
      adjustFilters();
      // var end = new Date().getTime();
      // var time = end - start;
      $scope.$emit('$TRIGGERUNLOAD', 'mainLoader');
      $scope.initializeData(key);
    }, function(result){
      if (result && result.data && result.data.length > 0)
      {
        $scope.total = result.data || [];
      } else {
        $scope.total = [];
      }
      $scope.data.data = $scope.total;
      $scope.$emit('$TRIGGERUNLOAD', 'mainLoader');
      $scope.initializeData(key);
      $scope.showMessage = true;
      $scope.setupData();
      setupFilters();
    });
  }; //


  /***************************************************************
  * This is used to initialize the scope title, key, and code. Once we have a 
  * database, this is most likely where we'll do the first pull for data.
  *
  * TODO:: Add query prameters capabilities for this page so that we don't have
  * to rely on the local/session storrage to pass us the search key
  *
  * TODO:: When we do start using actual transfered searches from the main page
  * we need to initialize checks on the filters that were sent to us from that
  * page (or we need to disable the filter all together)
  * 
  * This function is called by the reAdjustment function in order
  * to reinitialze all of the data if the list of items changes.
  * which usually would hinge on the key of the search
  * params: key -- The search object we use to initialize data with.
  ***************************************************************/
  $scope.initializeData = function(key) {

    if (!isEmpty($scope.searchGroup)) {
      // grab all of the keys in the filters
      $scope.searchKey          = $scope.searchGroup[0].key;
      $scope.searchCode         = $scope.searchGroup[0].code;
      var keys = _.pluck($scope.filters, 'attributeType');
      
      var foundFilter = null;
      var foundCollection = null;
      var type = '';
      


      // TODO: CLEAN UP THIS IF/ELSE switch!!!!!!!


      if (_.contains(keys, $scope.searchCode.type)) {
        $scope.showSearch         = true;
        
        foundFilter = _.find($scope.filters, {'attributeType': $scope.searchCode.type});
        foundCollection = _.find(foundFilter.codes, {'code': $scope.searchCode.key});
        
        // if the search group is based on one of those filters do this
        if ($scope.searchCode !== 'all' && foundFilter && foundCollection) {
          var index = _.indexOf($scope.filters, foundFilter);
          if (index){
            $scope.filters.splice(index, 1);
          }
          $scope.searchColItem      = foundCollection;
          $scope.searchTitle        = foundFilter.description + ', ' + foundCollection.label;
          $scope.modal.modalTitle   = foundFilter.description + ', ' + foundCollection.label;
          $scope.searchDescription  = getShortDescription(foundCollection.description) || 'The results on this page are restricted by an implied filter on the attribute: ' + $scope.searchTitle;

          if (foundCollection.landing !== undefined && foundCollection.landing !== null) {
            getBody(foundCollection.landing).then(function(result) {
              $scope.modal.modalBody = result;
              $scope.modal.isLanding = true;
            });
          } else {
            $scope.modal.modalBody = foundCollection.description || 'The results on this page are restricted by an implied filter on the attribute: ' + $scope.searchTitle;
            $scope.modal.isLanding = false;
          }

        } else {
          $scope.searchTitle        = $scope.searchType + ', All';
          $scope.modal.modalTitle   = $scope.searchType + ', All';
          $scope.searchDescription  = 'The results on this page are restricted by an implied filter on the attribute: ' + $scope.searchType;
          $scope.modal.modalBody    = 'This will eventually hold a description for this attribute type.';
          $scope.modal.isLanding    = false;
        }
      } else if ($scope.searchGroup[0].key === 'search') {
        // Otherwise check to see if it is a search
        $scope.searchKey          = 'DOALLSEARCH';
        $scope.showSearch         = true;
        $scope.searchTitle        = $scope.searchGroup[0].code;
        $scope.modal.modalTitle   = $scope.searchGroup[0].code;
        $scope.searchDescription  = 'Search results based on the search key: ' + $scope.searchGroup[0].code;
        $scope.modal.modalBody    = 'The results on this page are restricted by an implied filter on words similar to the search key \'' + $scope.searchGroup[0].code + '\'';
      } else {
        // In this case, our tempData object exists, but has no useable data
        $scope.searchKey          = 'DOALLSEARCH';
        $scope.showSearch         = true;
        $scope.searchTitle        = 'All';
        $scope.modal.modalTitle   = 'All';
        $scope.searchDescription  = 'Search all results';
        $scope.modal.modalBody    = 'The results found on this page are not restricted by any implied filters.';
      }
    } else {
      // In this case, our tempData doesn't exist
      $scope.searchKey          = 'DOALLSEARCH';
      $scope.showSearch         = true;
      $scope.searchTitle        = 'All';
      $scope.modal.modalTitle   = 'All';
      $scope.searchDescription  = 'Search all results';
      $scope.modal.modalBody    = 'The results found on this page are not restricted by any implied filters.';
    }

    $scope.applyFilters();
    $scope.$broadcast('dataloaded', !$scope.single);
  };

  /***************************************************************
  * This function grabs the search key and resets the page in order to update the search
  ***************************************************************/
  var callSearch = function(key) {
    $scope.$emit('$TRIGGERLOAD', 'mainLoader');

    var type = 'search';
    var code = 'all';
    var query = null;
    if (key === null || key === undefined) {
      if (!isEmpty($location.search())) {
        query = $location.search();
        if (query.type === 'attribute') {
          if (query.keyType && query.keyKey) {
            type = 'attribute';
            code = {
              'type': query.keyType,
              'key': query.keyKey
            };
          } 
        } else if (query.type && query.code) {
          type = query.type;
          code = query.code;
        } 
      }
    } else {
      if (!isEmpty($location.search())) {
        query = $location.search();
        if (query.type === 'attribute') {
          if (query.keyType && query.keyKey) {
            type = 'attribute';
            code = {
              'type': query.keyType,
              'key': query.keyKey
            };
          }
        } else if (query.type && query.code) {
          type = query.type;
          code = query.code;
        }
      }
    }
    $scope.reAdjust([{ 'key': type, 'code': code }]);
  };

  $scope.$on('$CHANGESEARCHRESULTTAGS', function(event, id, tags){
    $timeout(function() {
      var temp = _.find($scope.data.data, {'componentId': id});
      temp.tags = tags;
    })
  }); //

  $scope.resetSearch = function() {
    $scope.$emit('$TRIGGERLOAD', 'mainLoader');
    // $scope.$emit('$TRIGGERLOAD', 'resultsLoad');
    $scope.$emit('$TRIGGERLOAD', 'filtersLoad');
    var type = 'search';
    var code = 'all';
    $rootScope.searchKey = 'all';
    $location.search({
      'type': type,
      'code': code
    });
    $scope.reAdjust([{ 'key': type, 'code': code }]);
  }

  /***************************************************************
  * This function is used by the reviews section in the details to remove
  * and add the ellipsis
  ***************************************************************/
  $scope.toggleclass = function(id, className) {
    toggleclass(id, className);
  };

  /***************************************************************
  * This function removes the inherent filter (if you click on apps, types no longer applies etc)
  ***************************************************************/
  var adjustFilters = function() {
    if ($scope.searchGroup[0].key) {
      var temp = _.find($scope.filters, {'key': $scope.searchGroup[0].key});
      if (temp){
        var index = _.indexOf($scope.filters, temp);
        if (index){
          $scope.filters.splice(index, 1);
        }
      }
    }
    Business.getFilters(false, false).then(function(result) {
      $scope.resetFilters = JSON.parse(JSON.stringify(result));
    })
  };

  /***************************************************************
  * This funciton calls the global buttonOpen function that handles page 
  * flyout animations according to the state to open the details
  ***************************************************************/
  $scope.doButtonOpen = function() {
    buttonOpen();
  };

  /***************************************************************
  * This funciton calls the global buttonClose function that handles page 
  * flyout animations according to the state to close the details
  ***************************************************************/
  $scope.doButtonClose =  function() {
    buttonClose();
  };

  /***************************************************************
  * This function handles toggleing filter checks per filter heading click.
  ***************************************************************/
  $scope.toggleChecks = function(collection, override){
    $scope.applyFilters();
  };

  /***************************************************************
  * This function updates the details when a component title is clicked on
  ***************************************************************/
  $scope.updateDetails = function(id, article){
    // $scope.$emit('$TRIGGERLOAD', 'fullDetailsLoader');
    if (article && article.listingType === 'Article') {
      $scope.isArticle = true;
      localCache.save('type', article.articleAttributeType);
      localCache.save('code', article.articleAttributeCode);
      // $scope.$emit('$TRIGGERUNLOAD', 'fullDetailsLoader');
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERLANDING', false);
      $scope.showDetails = true;
      if (!openClick) {
        buttonOpen();
      }
      $timeout(function(){
        $('.page1').focus();
        // $scope.componentList.pauseLimit();
        $scope.componentList.dotdotdotFinished().then(function(){
          $scope.scrollTo('componentScroll'+article.attributes[0].type.replace(/\W/g, '')+article.attributes[0].code.replace(/\W/g, ''));
        });
        // $timeout(function(){
          // $scope.componentList.resumeLimit();
        // })
    }, 0);
    } else {
      $scope.isArticle = false;

      $('.page2').scrollTop(0);
      if (!openClick) {
        buttonOpen();
      }
      $scope.showDetails = false;
      
      Business.componentservice.getComponentDetails(id, true).then( function (result){
        if (result)
        {

          // grab the evaluation schedule.

          $scope.sendPageView(result.name);
          $scope.details.details = result;
          $scope.$emit('$TRIGGEREVENT', '$RESETCAROUSEL')
          // Code here will be linted with JSHint.
          /* jshint ignore:start */
          // Code here will be linted with ignored by JSHint.

          if ($scope.details.details.attributes[0] !== undefined) {
            var foundEvaluation = null;
            _.each($scope.details.details.attributes, function(attribute) {
              if (attribute.type === 'DI2ELEVEL') {
                foundEvaluation = attribute;
              }
            });
            $scope.details.details.evaluationAttribute = foundEvaluation;
          }
          if ($scope.details.details.lastActivityDts && $scope.details.details.lastViewedDts)
          {
            var update = new Date($scope.details.details.lastActivityDts);
            var view = new Date($scope.details.details.lastViewedDts);
            if (view < update) {
              showUpdateNotify();
            } else {
              resetUpdateNotify();
            }
          } else {
            resetUpdateNotify();
          }

          /* jshint ignore:end */
        }
        // $scope.$emit('$TRIGGERUNLOAD', 'fullDetailsLoader');
        $scope.showDetails = true;
        $timeout(function(){
          $('.page1').focus();
          // $scope.componentList.pauseLimit();
          $scope.componentList.dotdotdotFinished().then(function(){
            $scope.scrollTo('componentScroll'+$scope.details.details.componentId.replace(/\W/g, ''));
          });
          // $timeout(function(){
            // $scope.componentList.resumeLimit();
          // })
      }, 0);
      });
    } //
  }; //


  var popupWin;
  /***************************************************************
  * This function adds a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.goToFullPage = function(id){
    var url = $location.absUrl().substring(0, $location.absUrl().length - $location.url().length);
    url = url + '/single?id=' + id;
    popupWin = utils.openWindow(url, 'Component_' + id, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=840, height=840', popupWin);
  };

  /***************************************************************
  * This function adds a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.goToCompare = function(){
    var list = [];
    _.each($scope.data.data, function(item) {
      list.push(item.componentId);
    });
    
    Business.saveLocal('COMPARE', {'id': list});
//    $location.search({
//      'id': list
//    });    
    $location.path('/compare');
  };
  
  /***************************************************************
  * This function resets the filters in the results page in order to clear
  * the filters as quickly as possible
  ***************************************************************/
  $scope.clearFilters = function() {
    $scope.orderProp = '';
    $scope.ratingsFilter = null;
    $scope.tagsFilter = null;
    $scope.query = null;
    if ($scope.resetFilters) {
      $scope.filters = JSON.parse(JSON.stringify($scope.resetFilters));
    }
    $scope.applyFilters();
  };

  /***************************************************************
  * This function is used to watch filters in order to show the 'applied'
  * message so that they won't forget one of the filters is applied.
  ***************************************************************/
  $scope.checkFilters = function() {
    _.each($scope.filters, function(filter){
      filter.hasChecked = _.some(filter.codes, function(item){
        return item.checked;
      });
      if (!filter.hasChecked) {
        filter.checked = false;
      }
    });
    $scope.applyFilters();
  }

  /***************************************************************
  * This function applies the filters that have been given to us to filter the
  * data with
  ***************************************************************/
  //componentList will be overridden by the component list directive
  //it is the 'handler'
  $scope.componentList = {};
  $scope.componentList.resetLimit = function(){};
  $scope.applyFilters = function() {
    if ($scope.filteredTotal) {

      var results =
      // We must use recursive filtering or we will get incorrect results
      // the order DOES matter here.
      $filter('orderBy')
      ($filter('ratingFilter')
        ($filter('tagFilter')
          ($filter('componentFilter')
            ($filter('filter')
                                        //filter by the string
                                        ($scope.total, $scope.query),
                                                // filter the data by the filters
                                                $scope.filters),
                                        // filter the data by the tags
                                        $scope.tagsFilter),
                                // filter the data by the ratings
                                $scope.ratingsFilter),
                        // Then order-by the orderProp
                        $scope.orderProp);

      // make sure we reset the data and then copy over the results  
      $scope.filteredTotal = [''];
      $scope.filteredTotal = results;

      // Do the math required to assure that we have a valid page number and 
      // maxPageNumber
      $scope.maxPageNumber = Math.ceil($scope.filteredTotal.length / $scope.rowsPerPage);
      if (($scope.pageNumber - 1) * $scope.rowsPerPage >= $scope.filteredTotal.length) {
        $scope.pageNumber = 1;
      }

      // Set the data that will be displayed to the first 'n' results of the filtered data
      $scope.data.data = $scope.filteredTotal.slice((($scope.pageNumber - 1) * $scope.rowsPerPage), ($scope.pageNumber * $scope.rowsPerPage));
      if ($scope.data.data.length) {
        $scope.showMessage = false;
      } else {
        $scope.showMessage = true;
      }
      // after a slight wait, reapply the popovers for the results ratings.
      $timeout(function () {
        setupPopovers();                    
        $scope.componentList.resetLimit('.page1');
      }, 300);

    }
  };


  //////////////////////////////////////////////////////////////////////////////
  // Here we put our Event Watchers
  //////////////////////////////////////////////////////////////////////////////


  /***************************************************************
  * Event for callSearch caught here. This is triggered by the nav
  * search bar when you are already on the results page.
  ***************************************************************/
  // $scope.$on('$callSearch', function(event, data) {jshint unused: false
  //   callSearch(data);
  // });

  /***************************************************************
  * Event to trigger an update of the details that are shown
  ***************************************************************/
  $scope.$on('$detailsUpdated', function(event, id) {/*jshint unused: false*/
    if ($scope.details.details && $scope.details.details.componentId === id) {
      $timeout(function() {
        $scope.updateDetails($scope.details.details.componentId, $scope.details.details.listingType);
      });
    }
  });


  /***************************************************************
  * Catch the enter/select event here for typeahead
  ***************************************************************/
  $scope.$on('$typeahead.select', function(event, value, index) {/*jshint unused: false*/
    $scope.applyFilters();
    $scope.sendEvent('Filter Set', 'Text', $scope.query);
  });

  /*******************************************************************************
  * This function watches for the view content loaded event and runs a timeout 
  * function to handle the initial movement of the display buttons.
  *******************************************************************************/
  $scope.$on('$viewContentLoaded', function(){
    resetAnimations($('.page1'), $('.page2'), $('.filters'));
    $timeout(function() {
      if (fullClick === 0) {
        if ($(window).width() >= 768) {
          if (filtClick === 0) {
            openFiltersToggle();
          }
        }
      }
    }, 1000);
  });


  //////////////////////////////////////////////////////////////////////////////
  // Here we put our Scope Watchers
  //////////////////////////////////////////////////////////////////////////////

  /***************************************************************
  * This function is used to watch the pagenumber variable. When it changes
  * we need to readjust the pagination
  ***************************************************************/
  $scope.$watch('pageNumber',function(val, old){ /* jshint unused:false */
    $scope.pageNumber = parseInt(val);
    if ($scope.pageNumber < 1) {
      $scope.pageNumber = 1;
    }
    if ($scope.pageNumber > $scope.maxPageNumber) {
      $scope.pageNumber = $scope.maxPageNumber;
    }

    var page = $scope.pageNumber;
    if (page < 1 || page === '' || isNaN(page) || page === null){
      page = 1;
    }

    if ($scope.filteredTotal) {
      $scope.data.data = $scope.filteredTotal.slice(((page - 1) * $scope.rowsPerPage), (page * $scope.rowsPerPage));
    } else {
      $scope.data.data = [];
    }
    $scope.applyFilters();

  });

  /***************************************************************
  * This function is used to watch the rowsPerPage variable. When it changes
  * we need to adjust pagination
  ***************************************************************/
  $scope.$watch('rowsPerPage',function(val, old){ /* jshint unused:false */
    var rowPP = $scope.rowsPerPage;
    if (rowPP < 1 || rowPP === '' || isNaN(rowPP) || rowPP === null){
      rowPP = 1;
    }
    $scope.pageNumber = 1;
    if ($scope.filteredTotal) {
      $scope.maxPageNumber = Math.ceil($scope.filteredTotal.length / rowPP);
    }
    $scope.applyFilters();
  });

  /***************************************************************
  * This function is used to watch the orderProp variable. When it changes
  * re-filter the data
  ***************************************************************/
  $scope.$watch('orderProp',function(val, old){ /* jshint unused:false */
    $scope.applyFilters();
  });

  /***************************************************************
  * This function is used to watch the query variable. When it changes
  * re-filter the data
  ***************************************************************/
  $scope.$watch('query',function(val, old){ /* jshint unused:false */
    $scope.applyFilters();
  });

  /***************************************************************
  * This function is used to watch the query variable. When it changes
  * re-filter the data
  ***************************************************************/
  $scope.$watch('ratingsFilter',function(val, old){ /* jshint unused:false */
    $scope.applyFilters();
  });

  /***************************************************************
  * This function is a deep watch on the data variable to see if 
  * data.data changes. When it does, we need to see if the result set
  * for the search results is larger than the 'max' displayed
  ***************************************************************/
  $scope.setupData = function() {
    if ($scope.data && $scope.data.data) {
      // max needs to represent the total number of results you want to load
      // on the initial search.
      var max = 2000;
      // also, we'll probably check the total number of possible results that
      // could come back from the server here instead of the length of the
      // data we have already.
      if ($scope.data.data.length > max) {
        $scope.moreThan200 = true;
      } else {
        $scope.moreThan200 = false;
      }
    }
  }

  callSearch();
  
}]);

