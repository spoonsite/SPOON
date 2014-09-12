'use strict';

app.controller('DetailsReviewCtrl', ['$scope', 'business', '$rootScope', function ($scope, Business, $rootScope) {

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
    loadUserProfile();
  });
  Business.getProsConsList().then(function(result) {
    if (result) {
      $scope.prosConsList = result;
    } else {
      $scope.prosConsList = null;
    }
  });

  console.log('$scope.getComponentId', $rootScope.getComponentId());

  /***************************************************************
  * This function saves the profile changes in the scope by copying them from
  * the user variable into the backup variable (this function would be where
  * you send the saved data to the database to store it)
  ***************************************************************/ //
  $scope.submitReview = function(event, review, revs) {
    var body = {};
    body.userTypeCode = review.userRole.code;
    body.comment = review.comment;
    body.title = review.title;
    body.rating = review.rating? review.rating: 0;
    body.lastUsed = new Date(review.lastUsed).toISOString();
    body.recommend = review.recommend;
    body.organization = review.organization;
    body.userTimeCode = review.usedTimeCode.code;
    console.log('body', body);
    console.log('review', review);
    console.log('revs', revs);
    event.preventDefault();
    
    var componentId = $rootScope.getComponentId();
    if (!revs) {
      Business.componentservice.saveReview(componentId, body).then(function(result){
        console.log('result', result);
        if (result && result.componentReviewId)
        {
          var reviewId = result.componentReviewId;
          _.each(review.pros, function(pro){
            Business.componentservice.saveReviewPros(componentId, reviewId, pro.text).then(function(result){
              console.log('result', result);
            })
          });
          _.each(review.cons, function(con){
            Business.componentservice.saveReviewCons(componentId, reviewId, con.text).then(function(result){
              console.log('result', result);
            })
          });
          $scope.$emit('$hideModal', 'descModal');
        }
      });
    } else {
      // Business.componentservice.updateReview(componentId, body).then(function(result){
      //   console.log('result', result);
      //   if (result && result.componentReviewId)
      //   {
      //     var reviewId = result.componentReviewId;
      //     Business.componentservice.deleteProsandCons(componentId, reviewId);
      //     _.each(review.pros, function(pro){
      //       Business.componentservice.saveReviewPros(componentId, reviewId, pro.text).then(function(result){
      //         console.log('result', result);
      //       })
      //     });
      //     _.each(review.cons, function(con){
      //       Business.componentservice.saveReviewCons(componentId, reviewId, con.text).then(function(result){
      //         console.log('result', result);
      //       })
      //     });
      //   }
      // });
    }
    return false;
  };


}]);
