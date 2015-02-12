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

app.directive('architecture',['business', '$timeout', function (Business, $timeout) {
  return {
    template: '<div class="architectureSearch"><div class="archBody"> <div class="archList">  </div> <div class="rightSide"> <h2>Description</h2>  <div>description content</div> </div> <div class="rightSide"> <h2>Results</h2>  <div>results content</div> </div> </div></div>',
    restrict: 'E',
    link: function postLink(scope, element, attrs) {
      scope.tree;
      element.text('this is the component/architecture directive');
      Business.articleservice.getArchitecture().then(function(result){
        scope.tree = result? result: [];
      }, function(){
        scope.tree = [];
      });
    }
  };
}]);
