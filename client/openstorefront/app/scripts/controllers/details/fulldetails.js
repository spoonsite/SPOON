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

/*global isEmpty*/

app.controller('DetailsFulldetailsCtrl', ['$rootScope', '$scope', 'business', '$location', function ($rootScope, $scope, Business, $location) {

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
    { title:'FAQ Q&A', content:'4', relpath:'views/details/comments.html', class:'' },
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
  * This function adds a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.addToWatches = function(id){
    var a = _.findWhere($scope.watches, {'id': id});
    if (a === undefined  || isEmpty(a)) {
      $scope.watches.push({'id': id, 'watched': true});
    }

    Business.setWatches($scope.watches);
    _.where($scope.data.data, {'id': id})[0].watched = true;
  };
  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.saveTags = function(id, tags){
    Business.componentservice.saveTags(id, tags);
    $scope.applyFilters();
  };

  /***************************************************************
  * This function removes a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.removeFromWatches = function(id){
    var a = _.findWhere($scope.watches, {'id': id});

    if (a !== undefined  && !isEmpty(a)) {
      $scope.watches.splice(_.indexOf($scope.watches, a), 1);
    }

    Business.setWatches($scope.watches);
    _.where($scope.data.data, {'id': id})[0].watched = false;
  };

  /***************************************************************
  * This function does the route redirection to the user profile path in order
  * to allow the user to view their watches.
  ***************************************************************/
  $scope.viewWatches = function () {
    $location.path('/userprofile');
  };

  
  $scope.getTimes = function(n){
    return new Array(parseInt(n));
  };



}]);
