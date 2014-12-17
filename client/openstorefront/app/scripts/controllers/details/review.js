'use strict';

app.controller('DetailsReviewCtrl', ['$scope', 'business', '$rootScope', '$timeout','$q', function ($scope, Business, $rootScope, $timeout, $q) {

  $scope.review = {};
  $scope.user = {};
  $scope.backup = {};

  var setupReview = function() {
    $scope.rating = $scope.review.rating;
    resetVars().then(function() {
      $timeout(function() {
        if ($scope.review.usedTimeCode) {
          $scope.review.timeCode = _.find($scope.expertise, {'description': $scope.review.usedTimeCode});
        } else {
          $scope.review.timeCode = $scope.expertise[0];
        }
        if ($scope.review.userType) {
          $scope.review.role = _.find($scope.userTypeCodes, {'description': $scope.review.userType});
        } else {
          $scope.review.role = _.find($scope.userTypeCodes, {'code': $scope.user.info.userTypeCode});
        }
        if (!$scope.review.organization) {
          $scope.review.organization = $scope.user.info.organization;
        }
      });
    });
  }

  var resetVars = function() {
    var deferred = $q.defer();
    Business.userservice.getCurrentUserProfile().then(function(profile) {
      if (profile) {
        $scope.user.info = profile;
        Business.lookupservice.getExpertise().then(function(result){
          if (result) {
            $scope.expertise = result;
            Business.lookupservice.getUserTypeCodes().then(function(result){
              if (result) {
                $scope.userTypeCodes = result;
                deferred.resolve();
              } else {
                $scope.userTypeCodes = [];
                deferred.reject();
              }
            }, function() {
              $scope.userTypeCodes = [];
              deferred.reject();
            });
          } else {
            deferred.reject();
            $scope.userTypeCodes = [];
            $scope.expertise = [];
          }
        }, function() {
          deferred.reject();
          $scope.userTypeCodes = [];
          $scope.expertise = [];
        });
        Business.getProsConsList().then(function(result) {
          if (result) {
            $scope.prosConsList = result;
          } else {
            $scope.prosConsList = null;
          }
        });
      }
    });
    return deferred.promise; //
  }

  $scope.$on('$RESETREVIEWEDIT', function (event, review) {
    $scope.review.timeCode = null;
    $scope.review.role = null;
    $scope.rating = 0;
    $scope.review = angular.copy(review);
    $scope.backup = angular.copy(review);
    setupReview();
  });

  (resetVars().then(function() {
    $scope.review.timeCode = $scope.expertise[0];
    $scope.review.role = _.find($scope.userTypeCodes, {'code': $scope.user.info.userTypeCode});
    $scope.review.organization = $scope.user.info.organization;
    $scope.rating = 0;
  }));


  /***************************************************************
  * This function saves the profile changes in the scope by copying them from
  * the user variable into the backup variable (this function would be where
  * you send the saved data to the database to store it)
  ***************************************************************/ //
  $scope.submitReview = function(event, review, revs) {
    removeError();
    var body = {};
    var error = false;
    var errorObjt = angular.copy(utils.errorObj);
    if (review.role && !review.role.code) {
      error = true;
      errorObjt.add('userTypeCode', 'You must select a user role.');
    } else {
      body.userTypeCode = review.role.code;
    }
    if (!review.comment) {
      error = true;
      errorObjt.add('comment', 'A comment is required.');
    } else if (review.comment.length > 4096) {
      error = true;
      errorObjt.add('comment', 'Your comment has exceeded the accepted length.');
    } else {
      body.comment = review.comment? review.comment : '';
    }
    if (!review.title) {
      error = true;
      errorObjt.add('title', 'A title is required.');
    } else if (review.title.length > 255) {
      error = true;
      errorObjt.add('title', 'Your title has exceeded the accepted length.');
    } else {
      body.title = review.title;
    }
    body.rating = $scope.rating? $scope.rating: 0;
    if (!review.lastUsed) {
      error = true;
      errorObjt.add('lastUsed', 'You must included the last time you used this component.');
    } else {
      body.lastUsed = new Date(review.lastUsed).toISOString();
    }
    body.recommend = review.recommend? true: false;
    if (!review.organization) {
      error = true;
      errorObjt.add('organization', 'You must included your organization');
    } else {
      body.organization = review.organization;
    }
    if (!review.timeCode) {
      error = true;
      errorObjt.add('userTimeCode', 'You must included your how long you\'ve used this component.');
    } else {
      body.userTimeCode = review.timeCode.code;
    }
    // console.log('body', body);
    event.preventDefault();
    // console.log('body', body);
    
    var componentId = $rootScope.getComponentId();
    var reviewId = null;
    if (error) {
      // console.log('triggering errors');
      triggerError(errorObjt);
      return false;
    }
    // console.log('Saving Review.');

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
