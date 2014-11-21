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

app.directive('compareview', function () {
  return {
    templateUrl: 'views/details/compare.html',
    restrict: 'E',
    scope: {
      details: '=data'
    },
    link: function postLink(scope, element, attrs) {
      console.log('scope.details', scope.details);
      scope.getDate = function(date){
        if (date)
        {
          var d = new Date(date);
          var currDate = d.getDate();
          var currMonth = d.getMonth();
          var currYear = d.getFullYear();
          return ((currMonth + 1) + '/' + currDate + '/' + currYear);
        }
        return null;
      };
      scope.formatTags = function(tags) {
        var result = '';
        _.each(tags, function(tag){
          if (result.length > 0) {
            result = result + ", " + tag.text;
          } else {
            result = tag.text;
          }
        });
        return result;
      }

      scope.isEvaluationPresent = function() {
        if (scope.details) {
          var details = scope.details;
          return details.evaluation.checked && details.evaluation.data && details.evaluation.data.evaluationSections && details.evaluation.data.evaluationSections.length;
        } else {
          return false;
        }
      }

      scope.getFullRating = function(num) {
        return new Array(num);   
      }
      scope.getEmptyRating = function(num) {
        var length = ((5 - num) > 0)? (5 - num):0; 
        return new Array(length);
      }

      scope.getTimes = function(n){
        return new Array(parseInt(n));
      };
    }
  };
});
