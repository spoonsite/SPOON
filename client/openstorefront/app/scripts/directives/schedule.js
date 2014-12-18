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

app.directive('schedule', ['business', '$timeout', function (Business, $timeout) {
  return {
    templateUrl: 'views/details/schedule.html',
    restrict: 'EA',
    scope:{
      ngModel: '='
    },
    link: function postLink(scope, element, attrs) {
      scope.getDate = function(date){
        return utils.getDate(date);
      };

      var updateColumns = function(){
        if (element.width() <= 693) {
          element.find('#scheduleUl').css('width', '100%');
          element.find('#scheduleDiv').css('margin-left', '0px');
        } else {
          element.find('#scheduleUl').css('width', '335px');
          element.find('#scheduleDiv').css('margin-left', '315px');
        }
      }


      scope.$on('$UPDATESCHEDULECOLUMNS', function() {
        $timeout(function(){
          updateColumns();
        })
      })

      element.find('#scheduleHolder').on('resize', function() {
        updateColumns();
      })
      $(window).resize(function() {
        updateColumns();
      })
      updateColumns();

      if (scope.ngModel && scope.ngModel.code) {
        Business.lookupservice.getEvalLevels().then(function(result){
          scope.levels = result? result: [];
          scope.display = _.find(scope.levels, function(item){
            if (!item) {
              return false
            }
            if (item.attributeCodePk) {
              if (item.attributeCodePk.attributeCode === scope.ngModel.code) {
                item.current = true;
                scope.group = item.groupCode;
                return true;
              } else {
                item.current = false;
                return false;
              }
            } else {
              item.current = false;
              return false;
            }
          });
          for( var i = 0; i < scope.levels.length; i++) {
            if (scope.levels[i].current){
              break;
            } else {
              scope.levels[i].finished = true;
            }
          }
        });
      }
    }
  };
}]);
