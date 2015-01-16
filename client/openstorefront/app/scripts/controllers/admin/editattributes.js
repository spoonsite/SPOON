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

app.controller('AdminEditattributesCtrl',['$scope','business', '$uiModal', '$timeout', function ($scope, Business, $uiModal, $timeout) {
  $scope.predicate = 'description';
  $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminAttributes');
  $scope.reverse = false;
  $scope.getFilters = function(override) {
    Business.getFilters(override, true).then(function(result){
      $scope.filters = result? angular.copy(result): [];
      $timeout(function(){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminAttributes');
      })
    }, function(){
      $scope.filters = [];
    });
  }
  $scope.getFilters(false);

  $scope.refreshFilterCache = function() {
    Business.getFilters(true, false);
  };

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  }

  $scope.editLanding = function(type, code) {
    $scope.$parent.editLanding(type, code);
  }

  $scope.deleteAttribute = function(filter){
    // console.log('Deleted filter', filter);
  }

  $scope.changeActivity = function(filter){
    var cont = confirm("You are about to change the active status of an Attribute (Enabled or disabled). Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      if (filter.activeStatus === 'A') {
        $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
        Business.articleservice.deactivateFilter(filter.type).then(function(){
          $timeout(function(){
            $scope.getFilters(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getFilters(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
          }, 1000);
        })
      } else {
        $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
        Business.articleservice.activateFilter(filter.type).then(function() {
          $timeout(function(){
            $scope.getFilters(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getFilters(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
          }, 1000);
        })
      }
    }
  }

  $scope.editType = function(type){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/editcodes.html',
      controller: 'AdminEditcodesCtrl',
      size: 'lg',
      resolve: {
        type: function () {
          return type;
        },
        size: function() {
          return 'lg';
        }
      }
    });

    modalInstance.result.then(function (result) {
      if (result && result.refresh === true) {
        $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
        $scope.getFilters(true);
        $scope.refreshFilterCache()
      } else if (result.type && result.code) {
        $scope.editLanding(result.type, result.code);
      }
    }, function (result) {
      if (result && result.refresh === true) {
        $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
        $scope.getFilters(true);
        $scope.refreshFilterCache()
      } else if (result.type && result.code) {
        $scope.editLanding(result.type, result.code);
      }  
    });
  }

  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);
}]);