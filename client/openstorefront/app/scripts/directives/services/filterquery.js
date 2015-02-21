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

app.directive('filterquery',['business', function (Business) {
  var returnFilterQuerySource = function(){
    return 'views/services/default.html';
  }
  return {
    templateUrl: returnFilterQuerySource(),
    restrict: 'E',
    scope:{
      url: '@'
    },
    link: function postLink(scope, element, attrs) {
      scope.today = new Date();
      /*
      utils.queryFilter = {
        status: null,
        max: null,
        sortField: null,
        sortOrder: null,
        offset: null,
        all: false,
        toQuery: function () {
          return utils.toParamString(this);
        }
      };

      {
        active status
        client ip
        component id
        component tracking id
        create dts
        create user
        event dts
        storage version
        track event type code
        update dts
        update user
      }
      */
      scope.query = {};
      scope.query.filterObj = angular.copy(utils.queryFilter);
      scope.query.url = scope.url || '';


      scope.sendRequest = function(){
        scope.query.filterObj.start = utils.getDate(scope.query.filterObj.start, true, true);
        scope.query.filterObj.end = utils.getDate(scope.query.filterObj.end, true, false);
        console.log('We sent the request', scope.query);
        Business.trackingservice.get(scope.query).then(function(result){
          console.log('result', result);
          scope.data = result? result.componentTrackings: [];
        }, function(){
          scope.data = [];
          console.log('The request failed');
        });
      }
    }
  };
}]);
