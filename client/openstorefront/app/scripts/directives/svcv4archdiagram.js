/*
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

'use strict';

app.controller('Svc4ArchDiagramController', ['$scope', 'business', function($scope, Business) {

  $scope.svcv4Mode = false;
  $scope.diagramToggleAllState = true;
  $scope.diagramToggleAllText = 'Expand All';
  
  Business.articleservice.getArchitecture('DI2E-SVCV4-A').then(function(result) {  
    $scope.svcv4data=result;
  });

  $scope.diagramToggleAll = function(diagramData){
    _.each(diagramData.children, function(level1){
      level1.show = $scope.diagramToggleAllState;
      _.each(level1.children, function(level1child){
        level1child.show= $scope.diagramToggleAllState;
      });
    });
    $scope.diagramToggleAllState = !$scope.diagramToggleAllState;
    if ($scope.diagramToggleAllState) {
      $scope.diagramToggleAllText = 'Expand All';
    } else {
      $scope.diagramToggleAllText = 'Collapse All';
    }
  };
  $scope.hidePopup = function(){
    $('.popover').remove();
  };
  
}]);

app.directive('svcv4Diagram', function() {
  return {
    restrict: 'E',
    templateUrl: 'views/diagram/diagram.html'
  };
});
