'use strict';

app.controller('DetailsReviewCtrl', ['$scope', 'business', function ($scope, Business) {
  
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




  /***************************************************************
  * This function saves the profile changes in the scope by copying them from
  * the user variable into the backup variable (this function would be where
  * you send the saved data to the database to store it)
  ***************************************************************/ //
  $scope.submitReview = function(event, review, revs) {
    event.preventDefault();
    review.lastUsed = new Date(review.lastUsed).toISOString();
    console.log('review', review);
    console.log('revs', revs);
    return false;
  };


}]);
