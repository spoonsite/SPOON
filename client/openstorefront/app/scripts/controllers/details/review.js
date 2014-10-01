'use strict';

app.controller('DetailsReviewCtrl', ['$scope', 'business', '$rootScope', function ($scope, Business, $rootScope) {

  $scope.review;
  $scope.rating = 0;
  $scope.timeCode;
  $scope.role;

  $scope.$watch('review', function() {
    if ($scope.review) {
      $scope.rating = $scope.review.rating;
    }
    if ($scope.review && $scope.review.usedTimeCode) {
      if (!$scope.timeCode) {
        $scope.timeCode = _.find($scope.expertise, {'description': $scope.review.usedTimeCode});
      }
    }
    if ($scope.review && $scope.review.userType) {
      if (!$scope.role) {
        $scope.role = _.find($scope.userTypeCodes, {'description': $scope.review.userType});
      }
    }
  }, true);

  $scope.$watch('rating', function() {
    if ($scope.review) {
      $scope.review.rating = $scope.rating;
    }
  });
  $scope.$watch('timeCode', function() {
    if ($scope.review) {
      $scope.review.usedTimeCode = $scope.timeCode.description;
      $scope.review.timeCode = $scope.timeCode;
    }
  });
  $scope.$watch('role', function() {
    if ($scope.review) {
      $scope.review.userType = $scope.role.description;
      $scope.review.role = $scope.role;
    }
  });


  Business.lookupservice.getExpertise().then(function(result){
    if (result) {
      $scope.expertise = result;
    } else {
      $scope.expertise = [];
    }
  });
  Business.lookupservice.getUserTypeCodes().then(function(result){
    if (result) {
      $scope.userTypeCodes = result;
    } else {
      $scope.userTypeCodes = [];
    }
  });
  Business.getProsConsList().then(function(result) {
    if (result) {
      $scope.prosConsList = result;
    } else {
      $scope.prosConsList = null;
    }
  });

  /***************************************************************
  * This function saves the profile changes in the scope by copying them from
  * the user variable into the backup variable (this function would be where
  * you send the saved data to the database to store it)
  ***************************************************************/ //
  $scope.submitReview = function(event, review, revs) {
    var body = {};
    body.userTypeCode = review.role.code;
    body.comment = review.comment? review.comment : '';
    body.title = review.title;
    body.rating = review.rating? review.rating: 0;
    body.lastUsed = new Date(review.lastUsed).toISOString();
    body.recommend = review.recommend? true: false;
    body.organization = review.organization;
    body.userTimeCode = review.timeCode.code;
    // console.log('body', body);
    event.preventDefault();
    // console.log('body', body);
    
    var componentId = $rootScope.getComponentId();
    var reviewId = null;
    if (revs) {
      reviewId = revs.reviewId;
      componentId = revs.componentId;
    }
    Business.componentservice.saveReview(componentId, body, reviewId).then(function(result){
      // console.log('result', result);
      if (result && result.componentReviewId)
      {
        var reviewId = result.componentReviewId;
        _.each(review.pros, function(pro){
          Business.componentservice.saveReviewPros(componentId, reviewId, pro.text).then(function(result){
            // console.log('result', result);
          })
        });
        _.each(review.cons, function(con){
          Business.componentservice.saveReviewCons(componentId, reviewId, con.text).then(function(result){
            // console.log('result', result);
          })
        });
        if (!revs) {
          $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', componentId);
          $scope.$emit('$TRIGGEREVENT', '$newReview');
          $scope.$emit('$hideModal', 'descModal');
        } else {
          $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', componentId);
          revs.edit = false;
        }
        $rootScope.refId = null;
      }
    });
    return false;
  };


}]);
