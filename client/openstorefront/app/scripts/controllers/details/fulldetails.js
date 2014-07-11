'use strict';

app.controller('DetailsFulldetailsCtrl', function ($scope) {
  $scope.expertise = [
    //
    {'value':'1', 'label': 'Less than 1 month'},
    {'value':'2', 'label': 'Less than 3 months'},
    {'value':'3', 'label': 'Less than 6 months'},
    {'value':'4', 'label': 'Less than 1 year'},
    {'value':'5', 'label': 'Less than 3 years'},
    {'value':'6', 'label': 'More than 3 years'}
  //
  ];
  
  $scope.userRoles = [
    //
    {'code':'ENDUSER', 'description': 'User'},
    {'code':'DEV', 'description': 'Developer'},
    {'code':'PM', 'description': 'Project Manager'}
  //
  ];

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
        'title': 'Comments',
        'include': 'views/details/comments.html'
      }
    //
    ]
  };


  $scope.detailResultsTabs = [
    //
    { title:'SUMMARY', content:'1', relpath:'views/details/summary.html' },
    { title:'DETAILS', content:'2', relpath:'views/details/details.html' },
    { title:'REVIEWS', content:'3', relpath:'views/details/reviews.html' },
    { title:'COMMENTS', content:'4', relpath:'views/details/comments.html' },
  //
  ];
  $scope.tab = $scope.detailResultsTabs[0];

  /***************************************************************
  * This function does the route redirection to the user profile path in order
  * to allow the user to view their watches.
  ***************************************************************/
  $scope.getEvaluationState = function () {
    if ($scope.details.details && $scope.details.evaluationLevel !== undefined) {
      var code = $scope.details.details.evaluationLevel[0].code;
      var stateFilter = _.where($scope.filters, {'key': 'evaluationLevel'})[0];
      var item = _.where(stateFilter.collection, {'code': code})[0];
      return item.type;
    }
    return '';
  };
  $scope.getEvaluationState();
  $scope.selectedTab = $scope.tabs[0];
});
