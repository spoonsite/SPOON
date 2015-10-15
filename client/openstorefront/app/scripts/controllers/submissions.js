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

app.controller('UserSubmissionCtrl', ['$scope', 'business', '$rootScope', '$location', '$timeout', '$q', function($scope, Business, $rootScope, $location, $timeout, $q) {

  $scope.submissions;
  $scope.predicate = 'name';
  $scope.reverse = false;
  
  $scope.getUserInfo = function(){
    var deferred = $q.defer();
    Business.userservice.getCurrentUserProfile().then(function(result){
      if (result) {
        $scope.userInfo = result;
        deferred.resolve(result);
        // console.log('result', result);
      }
    });
    return deferred.promise;
  }

  $scope.loadLookup = function(lookup, entity, loader){
    var deferred = $q.defer();
    Business.lookupservice.getLookupCodes(lookup, 'A').then(function (results) {
      deferred.resolve();
      if (results) {
        $scope[entity]= results;
      }        
    });      
    return deferred.promise;
  };

  $scope.loadLookup('ApprovalStatus', 'approvals', 'submissionLoader'),

  $scope.getApprovalStatus = function(code){
    if ($scope.approvals.length) {
      var found = _.find($scope.approvals,{'code': code});
      if (found){
        return found.description;
      } else {
        return code;
      }
    }
    return code;
  }

  $scope.setNotifyMe = function(submission){
    $scope.getUserInfo().then(function(userInfo){
      if (!userInfo || !userInfo.email){
        return;
      } else {
        var email = userInfo.email;
        if (!submission.notifyMe) {
          email = null;
        }
        Business.submissionservice.setNotifyMe(email, submission).then(function(result){
          $scope.getSubmissions(true);
        }, function(){
          $scope.getSubmissions(true);
        });
      }
    });
  }

  $scope.submit = function(submission, reverse){
    reverse = reverse || false;
    if (submission.componentId) {
      Business.submissionservice.submit(submission.componentId, reverse).then(function(result){
        $scope.getSubmissions(true);
      }, function(){
        $scope.getSubmissions(true);
      });
    }
  }

  $scope.deactivateSubmission = function(submission){
    if (submission.componentId) {
      Business.submissionservice.deactivateSubmission(submission.componentId).then(function(result){
        $scope.getSubmissions(true);
      }, function(){
        $scope.getSubmissions(true);
      });
    }
  }

  $scope.setPredicate = function (predicate) {
    if ($scope.predicate === predicate) {
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = false;
    }
  };


  $scope.getSubmissions = function(override){
    var deferred = $q.defer();
    $scope.getUserInfo().then(function(userInfo){
      if (userInfo && userInfo.username){
        Business.submissionservice.getSubmissions(userInfo.username, override).then(function(result){
          // $scope.$emit('$TRIGGERUNLOAD', 'submissionLoader', 100000);
          $scope.submissions = result;
          _.each($scope.submissions, function(submission){
            if (submission.notifyOfApprovalEmail){
              submission.notifyMe = true;
            }
          })
          deferred.resolve();
        }, function(result){
          $scope.submissions = [];
          // $scope.$emit('$TRIGGERUNLOAD', 'submissionLoader', 100000);
          deferred.reject();
        });
      }//
    });
    return deferred.promise;//
  }

  $scope.$on('$UPDATED_SUBMISSIONS', function(){
    $scope.getSubmissions();
  })

  $scope.getSubmissions(true);
}]);

