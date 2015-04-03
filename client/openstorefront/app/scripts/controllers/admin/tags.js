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

 app.controller('AdminTagsCtrl', ['$scope', 'business', '$rootScope', '$uiModal', function ($scope, Business, $rootScope, $uiModal) {

  $scope.predicate = [];
  $scope.reverse = [];
  $scope.tagForm = {};
  $scope.predicate['tag'] = 'componentName';

  $scope.setPredicate = function (predicate, table) {
    if ($scope.predicate[table] === predicate) {
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
  };    

  $scope.loadTags = function() {
    $scope.$emit('$TRIGGERLOAD', 'tagsLoader');        
    Business.componentservice.getComponentAllTags().then(function (results) {
      $scope.$emit('$TRIGGERUNLOAD', 'tagsLoader');
      if (results) {
        $scope.tags = results;
      }
    });     
  };
  $scope.loadTags();    

  var compare = function (a, b) {
    return ((a.description == b.description) ? 0 : ((a.description > b.description) ? 1 : -1));
  }

  $scope.loadComponentList = function(){
    Business.componentservice.getComponentLookupList().then(function (results) {
      if (results) {
        $scope.components = results.sort(compare);                            
      }
    });      
  }; 
  $scope.loadComponentList();

  $scope.saveTag = function () {
    $scope.$emit('$TRIGGERLOAD', 'tagsLoader');    
    Business.componentservice.addSubComponentEntity({
      componentId: $scope.tagForm.componentId,
      entityName: "tags",
      entity: $scope.tagForm
    }).then(function (result) {
      $scope.$emit('$TRIGGERUNLOAD', 'tagsLoader'); 
      if (result) {
        if (result && result !== 'false' && isNotRequestError(result)){  
          removeError();
          triggerAlert('Saved successfully', 'tagSave', 'body', 3000);
          $scope.tagForm = {};
          $scope.loadTags();
        } else {
          removeError();
          triggerError(result, true);
        }
      }
    });      
  };

  $scope.removeTag = function(tag){       
    $scope.$emit('$TRIGGERLOAD', 'tagsLoader');        

    Business.componentservice.inactivateEnity({
      componentId: tag.componentId,
      entityId: tag.tagId,
      entity: 'tags'
    }).then(function (results) {
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'tagsLoader');
      $scope.loadTags();            
    });         
  };    

}]);

