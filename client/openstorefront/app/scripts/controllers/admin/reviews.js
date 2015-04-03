/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

app.controller('AdminReviewsCtrl', ['$scope', 'business', '$rootScope', '$uiModal', function ($scope, Business, $rootScope, $uiModal) {
    
    $scope.predicate = [];
    $scope.reverse = [];
    
    $scope.statusFilterOptions = [
      {code: 'A', desc: 'Active'},
      {code: 'I', desc: 'Inactive'}
    ];
    
    $scope.reviewFilter = angular.copy(utils.queryFilter);   
    $scope.reviewFilter.status = $scope.statusFilterOptions[0].code;       
     
    $scope.setPredicate = function (predicate, table) {
      if ($scope.predicate[table] === predicate) {
        $scope.reverse[table] = !$scope.reverse[table];
      } else {
        $scope.predicate[table] = predicate;
        $scope.reverse[table] = false;
      }
    };     
    
    $scope.loadReviews = function() {
      $scope.$emit('$TRIGGERLOAD', 'reviewFormLoader');        
      Business.componentservice.getComponentAllReviews($scope.reviewFilter).then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', 'reviewFormLoader');
        if (results) {
          $scope.reviews = results;
        }
      });     
    };
    $scope.loadReviews(); 
    
    $scope.toggleEntityStatus = function(entityOptions){
      $scope.$emit('$TRIGGERLOAD', entityOptions.loader);
      if(entityOptions.entity.activeStatus === 'A') {
        Business.componentservice.inactivateEnity({
          componentId: entityOptions.componentId,
          entityId: entityOptions.entityId,
          entity: entityOptions.entityName
        }).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', entityOptions.loader);
          entityOptions.loadEntity();             
        });        
      } else {
        Business.componentservice.activateEntity({
          componentId: entityOptions.componentId,
          entityId: entityOptions.entityId,
          entity: entityOptions.entityName
        }).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', entityOptions.loader);
          entityOptions.loadEntity();  
        });        
      }
    };    
    
    $scope.toggleReviewStatus = function(review){
      $scope.toggleEntityStatus({
        componentId: review.componentId,
        entity: review,        
        entityId: review.reviewId,
        entityName: 'reviews',        
        loader: 'reviewFormLoader',
        loadEntity: function(){
          $scope.loadReviews();
        }
      });
    };     
    
}]);
