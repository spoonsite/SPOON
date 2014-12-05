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


// <message>
//     <img src='img/content/car.png'/>
// </message>

app.directive('message', function () {
  return {
    transclude: true,
    restrict: 'EA',
    template: '<a ng-click='open()' ng-transclude></a>',
    scope: {
      useCtrl: '@',
      email: '@'
    },
    link: function(scope, element, attrs) {
      scope.open = function(){

        var modalInstance = $modal.open({
          templateUrl: templateDir+attrs.instanceTemplate +'.tpl.html',
          controller:  scope.useCtrl,
          size: 'lg',
          windowClass: 'app-modal-window',
          backdrop: true,
          resolve: {
            custEmail: function(){
              return {email: scope.email};
            }
          }
        });
        modalInstance.result.then(function(){
          console.log('Finished');
        }, function(){
          console.log('Modal dismissed at : ' + new Date());
        });
      };
    }
  };
});

