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

app.directive('question', ['business', '$timeout', function (Business, $timeout) {
  return {
    templateUrl: 'views/questionsResponse/questionTemplate.html',
    restrict: 'E',
    scope: {
      componentId: '=',
      questionId: '=',
    },
    link: function postLink(scope, element, attrs) {
      scope.user = {};
      scope.post = {};

      Business.userservice.getCurrentUserProfile().then(function(result){
        if (result) {
          scope.user.info = result;
          Business.lookupservice.getUserTypeCodes().then(function(result){
            if (result) {
              scope.userTypeCodes = result;
              if (attrs && attrs.question) {
                scope.post.question = attrs.question || '';
                scope.post.userTypeCode = _.find(scope.userTypeCodes, {'code': attrs.userTypeCode}) || scope.userTypeCodes[0];
                scope.post.organization = attrs.organization || '';
              } else {
                scope.post.userTypeCode = _.find(scope.userTypeCodes, {'code': scope.user.info.userTypeCode});
                scope.post.organization = scope.user.info.organization;
              }
            } else {
              $scope.userTypeCodes = [];
            }
          });
        }
      });

      scope.submitQuestion = function(event) {
        event.preventDefault();
        Business.userservice.getCurrentUserProfile().then(function(result){
          if (result) {
            scope.user.info = result;
            var request = angular.copy(scope.post);
            console.log('scope.user.info', scope.user.info);
            request.userTypeCode = request.userTypeCode.code;
            console.log('request', request);
            Business.componentservice.saveQuestion(scope.componentId, request, scope.questionId).then(function(result){
              if (result) {
                scope.$emit('$TRIGGEREVENT', '$detailsUpdated', scope.componentId);
              }
            });
          }
        });
      };
    } 
  };
}]);
