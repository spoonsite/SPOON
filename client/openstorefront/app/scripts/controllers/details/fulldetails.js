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

/*global MOCKDATA2*/

app.controller('DetailsFulldetailsCtrl', ['$rootScope', '$scope', 'business', '$location', 'Lightbox', '$timeout', function ($rootScope, $scope, Business, $location, Lightbox, $timeout) { /*jshint unused:false*/

  $scope.scoreCard                     = Business.componentservice.getScoreCard();
  $scope.externalDepend                = Business.componentservice.getExternalDepend();
  $scope.localAssetArtifacts           = Business.componentservice.getLocalAssetArtifacts();
  $scope.componentVitals               = Business.componentservice.getComponentVitals();
  $scope.pointsContact                 = Business.componentservice.getPointsContact();
  $scope.componentSummary              = Business.componentservice.getComponentSummary();
  $scope.componentEvalProgressBar      = Business.componentservice.getComponentEvalProgressBar();
  $scope.componentEvalProgressBarDates = Business.componentservice.getComponentEvalProgressBarDates();
  $scope.componentState                = Business.componentservice.getComponentState();
  $scope.resultsComments               = Business.componentservice.getResultsComments();
  $scope.watches                       = Business.getWatches();

  Business.lookupservice.getEvalLevels().then(function(result){
    $scope.evalLevels = result;
  });


  $scope.tabs = {
    'current': null,
    'bars': [
      //
      {
        'title': 'Summary',
        'include': 'views/details/summary.html'
      },
      {
        'title': 'Details',
        'include': 'views/details/details.html'
      },
      {
        'title': 'Reviews',
        'include': 'views/details/reviews.html'
      },
      {
        'title': 'Questions And Answers',
        'include': 'views/details/comments.html'
      }
    //
    ]
  };

  if ($scope.modal) {
    $scope.$watch('modal.modalBody', function() {
      $scope.modalBody = $scope.modal.modalBody;
    });
  }

  $scope.detailResultsTabs = [
    //
    { title:'SUMMARY', content:'1', relpath:'views/details/summary.html', class:'' },
    { title:'DETAILS', content:'2', relpath:'views/details/details.html', class:'' },
    { title:'REVIEWS', content:'3', relpath:'views/details/reviews.html', class:'' },
    { title:'Q&A', content:'4', relpath:'views/details/comments.html', class:'' }
    // { title:'QUESTIONS & ANSWERS', content:'4', relpath:'views/details/comments.html', class:"questionandanswer" },
  //
  ];

  $scope.tab = $scope.detailResultsTabs[0];

  /***************************************************************
  * This function does the route redirection to the user profile path in order
  * to allow the user to view their watches.
  ***************************************************************/
  $scope.getEvaluationState = function () {
    if ($scope.details && $scope.details.details && $scope.details.evaluationLevel !== undefined) {
      var code = $scope.details.details.evaluationLevel[0].code;
      var stateFilter = _.where($scope.filters, {'key': 'evaluationLevel'})[0];
      var item = _.where(stateFilter.collection, {'code': code})[0];
      return item.type;
    }
    return '';
  };
  $scope.getEvaluationState();
  $scope.selectedTab = $scope.tabs[0];

  /***************************************************************
  * This function is triggered by the '$descModal' event.
  ***************************************************************/
  $scope.$on('$descModal', function(event) { /*jshint unused: false*/
    // re-initialize the modal content here if we must
    if ($scope.modal.nav !== undefined && $scope.modal.nav !== null) {

      if ($rootScope.current) {
        $scope.modal.nav.current = $rootScope.current;
      } else {
        $scope.modal.nav.current = 'Write a Review';
      }
    }
  });

  /***************************************************************
  * This function is used by our image modal.
  ***************************************************************/
  $scope.openLightboxModal = function (index, imageArray) {
    Lightbox.openModal(imageArray, index);
  };

  /***************************************************************
  * This function watches the details object for changes
  ***************************************************************/
  $scope.$watch('details', function () {
    if ($scope.details){
      if ($scope.details.details) {
        var found = _.find($scope.watches, {'componentId': $scope.details.details.componentId});
        if (found) {
          $scope.details.details.watched = true;
        }
        if ($scope.details.details.reviews) {
          if ($scope.details.details.reviews[0] !== undefined) {
            $scope.setupReviewSummary();
          } else {
            $scope.reviewSummary = null;
          }
        }
      }
    }
  }, true);

  /***************************************************************
  * This function take the reviews array from the component details and sets up
  * all the information that is required for the summary. It is triggered by a change
  * in the details.details.reviews object. So it should be fairly resposive if a user
  * adds a review to the details. (which currently isn't implemented)
  ***************************************************************/
  $scope.setupReviewSummary = function(){
    var total = [];
    var pros = {};
    var cons = {};
    var prosList = [];
    var consList = [];
    var recommend = 0;

    //count the stuff
    _.each($scope.details.details.reviews, function(review){
      total[review.rating] = total[review.rating]? total[review.rating] + 1: 1;
      if (review.recommend) {
        recommend = recommend + 1;
      }
      if (review.pros[0] !== undefined) {
        _.each(review.pros, function(pro){
          prosList.push(pro);
        });
      }
      if (review.cons[0] !== undefined) {
        _.each(review.cons, function(con){
          consList.push(con);
        });
      }
    });

    //Grab all of the different pros and cons
    if (prosList.length){
      prosList.map( function (a) {
        if (a in pros) {
          pros[a] ++;
        } else {
          pros[a] = 1;
        }
      });
    } else {
      pros = null;
    }
    if (consList.length){
      consList.map( function (a) {
        if (a in cons) {
          cons[a] ++;
        } else {
          cons[a] = 1;
        }
      });
    } else {
      cons = null;
    }

    // create our result object
    var result = {
      'total': 0,
      'count': 0
    };
    for (var key in total) {
      result.total = result.total + (parseInt(key) * total[key]);
      result.count = result.count + 1;
    }
    result.score = result.total / result.count;
    result.recommend = recommend;
    result.recommendPercent = recommend / result.count * 100;

    //set up our review summary
    $scope.reviewSummary = {
      'ratings': total,
      'total': result,
      'pros': pros,
      'cons': cons
    };
  };

  /***************************************************************
  * This function adds a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.addToWatches = function(id){
    var a = _.find($scope.watches, {'componentId': id});
    var component = _.find(MOCKDATA2.componentList, {'componentId': id});
    
    if (!a) {
      $scope.watches.push(
      {
          'watchId' : $scope.watches[$scope.watches.length - 1].watchId + 1,
          'lastUpdateDts' : new Date().getTime(),
          'lastViewDts' : new Date().getTime(),
          'createDts' : new Date().getTime(),
          'componentName' : component.name,
          'componentId' : id,
          'notifyFlag' : true
      });
    }

    console.log('$scope.watches', $scope.watches);
    
    Business.setWatches($scope.watches);
    $scope.details.details.watched = true;
    _.where(MOCKDATA2.componentList, {'componentId': id})[0].watched = true;
    Business.updateCache('component_'+id, _.where(MOCKDATA2.componentList, {'componentId': id})[0]);
  };
  
  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.saveTags = function(id, tags){
    Business.componentservice.saveTags(id, tags);
    $scope.applyFilters();
  };

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.grabEvaluationMessage = function(statusCode, actual, estimated){
    var result = '';
    switch(statusCode){
      case 'C':
      result = 'COMPLETED ' + actual;
      break;
      case 'H':
      result = 'HAULTED ' + actual;
      break;
      case 'P':
      result = 'IN PROGRESS (estimated complete ' + estimated + ')';
      break;
      default:
      result = 'NOT STARTED (estimated complete ' + estimated + ')';
      break;
    }
    return result;
  };

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.getEval = function(levelCode){
    var level = _.find($scope.evalLevels.collection, {'code': levelCode});
    return level;
  };

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.checkForImportants = function(array){
    return _.some(array, function(item){
      return item.important;
    });
  };
  
  /***************************************************************
  * This function creates the image array required by the gallery
  ***************************************************************/
  $scope.getImages = function(imageArray){
    _.each(imageArray, function(image){
      var img = new Image();
      img.onload = function() {
        image.width = this.width;
        image.height = this.height;
      };
      img.src = image.link;
    });
    return imageArray;
  };


  var mNames = new Array('January', 'February', 'March',
    'April', 'May', 'June', 'July', 'August', 'September',
    'October', 'November', 'December');

  /***************************************************************
  * This function converts a timestamp to a displayable date
  ***************************************************************/
  $scope.getDate = function(date){
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
  * This function saves a component's tags
  ***************************************************************/
  $scope.toggleTags = function(id){
    $('#data-collapse-tags').toggleClass('collapsed');
    $(id).collapse('toggle');
  };

  /***************************************************************
  * This function removes a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.removeFromWatches = function(id){
    var a = _.find($scope.watches, {'componentId': id});

    if (a) {
      $scope.watches.splice(_.indexOf($scope.watches, a), 1);
    }

    console.log('$scope.watches', $scope.watches);


    Business.setWatches($scope.watches);
    $scope.details.details.watched = false;
    _.where(MOCKDATA2.componentList, {'componentId': id})[0].watched = false;
    Business.updateCache('component_'+id, _.where(MOCKDATA2.componentList, {'componentId': id})[0]);
  };

  
  $scope.getTimes = function(n){
    return new Array(parseInt(n));
  };



}]);
