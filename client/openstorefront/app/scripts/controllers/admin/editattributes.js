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

app.controller('AdminEditattributesCtrl',['$scope','business', '$uiModal', function ($scope, Business, $uiModal) {
  $scope.predicate = 'description';
  $scope.reverse = false;
  $scope.getFilters = function(override) {
    Business.getFilters(override).then(function(result){
      console.log('result', result);
      $scope.filters = result? angular.copy(result): [];
    }, function(){
      $scope.filters = [];
    });
  }
  $scope.getFilters(false);

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  }

  $scope.deleteAttribute = function(filter){
    console.log('Deleted filter', filter);
  }

  $scope.editType = function(type){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/editcodes.html',
      controller: 'AdminEditcodesCtrl',
      size: 'sm',
      resolve: {
        type: function () {
          return type;
        }
      }
    });

    modalInstance.result.then(function (result) {
      //do something
      triggerAlert('Your edits were saved', 'editUserProfile', 'body', 6000);
    }, function () {
      // cancled or failed
    });
  }
}]);