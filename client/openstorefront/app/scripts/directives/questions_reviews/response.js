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

app.directive('response', ['business', '$timeout', function (Business, $timeout) {
  var responseId = 1;
  return {
    templateUrl: 'views/questionsResponse/responseTemplate.html',
    restrict: 'E',
    scope: {
      componentId: '=',
      questionId: '=',
      responseId: '=',
    },
    link: function postLink(scope, element, attrs) {
      scope.user = {};
      scope.post = {};
      element.find('textarea').attr('id', responseId+'response')
      element.find('.giveMeARole').attr('id', responseId+'role')
      element.find('.giveMeAnOrganization').attr('id', responseId+'org')
      scope.id = responseId;
      responseId++;
      Business.userservice.getCurrentUserProfile().then(function(result){
        if (result) {
          scope.user.info = result;
          Business.lookupservice.getUserTypeCodes().then(function(result){
            if (result) {
              scope.userTypeCodes = result;
              if (attrs && attrs.response) {
                scope.post.response = attrs.response || '';
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

      scope.submitResponse = function(event) {


        event.preventDefault();
        
        var error = false;
        var errorObjt = {};
        errorObjt.errors = {};
        errorObjt.errors.entry = [];

        if (!scope.post.response){
          errorObjt.errors.entry.push({'key': scope.id+'response', 'value':'A response is required.'});
          error = true;
        } else if (scope.post.response.length > 1024){
          errorObjt.errors.entry.push({'key':scope.id+'response', 'value':'Your response has exceeded the accepted input length'});
          error = true;
        }        
        if (!scope.post.organization){
          errorObjt.errors.entry.push({'key': scope.id+'org', 'value':'An organization is required.'});
          error = true;
        } else if (scope.post.organization.length > 120){
          errorObjt.errors.entry.push({'key':scope.id+'org', 'value':'Your organization has exceeded the accepted input length'});
          error = true;
        }


        if (error) {
          errorObjt.success = false;
          triggerError(errorObjt);
          return false;
        }


        Business.userservice.getCurrentUserProfile().then(function(result){
          if (result) {
            scope.user.info = result;
            if (scope.responseId){
              scope.post.questionId = scope.questionId;
            }
            var request = angular.copy(scope.post);
            request.userTypeCode = request.userTypeCode.code;
            if (!request.userTypeCode){
              errorObjt.errors.entry.push({'key': scope.id+'role', 'value':'A valid user type code is required.'});
              error = true;
            }
            if (error) {
              errorObjt.success = false;
              triggerError(errorObjt);
              return false;
            }
            Business.componentservice.saveResponse(scope.componentId, scope.questionId, request, scope.responseId).then(function(result){
              if (result) {
                scope.$emit('$TRIGGEREVENT', '$detailsUpdated', scope.componentId);
              }
            });
          } 
        });
      }
    }
  };
}]);
