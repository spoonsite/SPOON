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

app.directive('question', ['business', function (Business) {
  return {
    template: '<div><br><b>Ask a Question</b><br><form ng-submit="submitQuestion($event)"><textarea cols="100" ng-model="content"></textarea><br><br><button class="btn btn-primary" type="submit">Post</button></form></div>',
    restrict: 'E',
    scope: {
      componentId: '='
    },
    link: function postLink(scope, element, attrs) {
      scope.content = '';
      scope.user = {};
      Business.userservice.getCurrentUserProfile().then(function(result){
        if (result) {
          scope.user.info = result;
        } 
      });
      scope.submitQuestion = function(event)
      {
        event.preventDefault();
        if (scope.user.info) {
          var post = {};
          post.question = scope.content;
          post.userType = scope.user.info.userTypeCode;
          post.organization = scope.user.info.organization;
          Business.componentservice.postQuestion(scope.componentId, post).then(function(result){
            if (result) {
              // console.log('result', result);
              scope.$emit('$TRIGGEREVENT', '$detailsUpdated', scope.componentId);
            }
          });
        } else {
          return false;
        }
      }

    }
  };
}]);
