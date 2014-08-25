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

/*global triggerAlert*/

app.controller('CompareCtrl', ['$scope', 'business', '$location', function ($scope, Business, $location) {

  $scope.list = $location.search().id;
  $scope.pair = [];
  $scope.showChoices = false;
  $scope.data = null;
  $scope.id = null;
  $scope.article = null;

  Business.componentservice.batchGetComponentDetails($scope.list).then(function(result){
    if (result && result.length > 0) {
      $scope.data = angular.copy(result);
    } else {
      $scope.data = null;
    }
  });

  var requestChange = function(id, article) {
    // console.log('we changed one!');
    if ($scope.pair && $scope.pair.length === 2 && id !== $scope.pair[1].componentId && id !== $scope.pair[0].componentId) {
      $scope.id = id;
      $scope.article = article;
      $scope.showChoices = true;
    } else {
      triggerAlert('This component is already present in the \'Side By Side\'', 'alreadyPresent', 'body', 1300);
    }
  };

  $scope.resetSide = function(isRight) {
    $scope.showChoices = false;
    if (isRight) {
      $scope.pair[1] = null;
    } else {
      $scope.pair[0] = null;
    }
    $scope.setCompare($scope.id, $scope.article);
    $scope.id = null;
    $scope.article = null;
  };


  $scope.setCompare = function(id, article){
    if (!article.type && !$scope.showChoices) {
      if (!$scope.pair[0] && !$scope.pair[1]) {
        $scope.pair[0] = _.find($scope.data, {'componentId': id});
        // console.log('$scope.pair[0]', $scope.pair[0]);
      } else if(!$scope.pair[1] && $scope.pair[0] && id !== $scope.pair[0].componentId) {
        $scope.pair[1] = _.find($scope.data, {'componentId': id});
        // console.log('$scope.pair[1]', $scope.pair[1]);
      } else if(!$scope.pair[0] && $scope.pair[1] && id !== $scope.pair[1].componentId) {
        $scope.pair[0] = _.find($scope.data, {'componentId': id});
        // console.log('$scope.pair[0]', $scope.pair[0]);
      } else {
        requestChange(id, article);
      }
    }
  };


}]);
