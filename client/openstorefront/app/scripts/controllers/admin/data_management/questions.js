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

app.controller('AdminQuestionCtrl', ['$scope', 'business', '$rootScope', '$uiModal', function ($scope, Business, $rootScope, $uiModal) {
    
    $scope.predicate = [];
    $scope.reverse = [];
     
    $scope.statusFilterOptions = [
      {code: 'A', desc: 'Active'},
      {code: 'I', desc: 'Inactive'}
    ];
    
    $scope.questionFilter = angular.copy(utils.queryFilter);   
    $scope.questionFilter.status = $scope.statusFilterOptions[0].code;  
    $scope.questionDetailsShow = [];
     
    $scope.setPredicate = function (predicate, table) {
      if ($scope.predicate[table] === predicate) {
        $scope.reverse[table] = !$scope.reverse[table];
      } else {
        $scope.predicate[table] = predicate;
        $scope.reverse[table] = false;
      }
    }; 
    
    
  $scope.loadQuestions = function() {
    $scope.$emit('$TRIGGERLOAD', 'questionFormLoader');        
    Business.componentservice.getComponentAllQuestions($scope.questionFilter).then(function (results) {
      $scope.$emit('$TRIGGERUNLOAD', 'questionFormLoader');
      if (results) {
        $scope.questions = results;
        _.forEach(results, function(question){
          if (!($scope.questionDetailsShow[question.questionId])){
            $scope.questionDetailsShow[question.questionId] = {};
            $scope.questionDetailsShow[question.questionId].flag = false;
            $scope.questionDetailsShow[question.questionId].showResponsesText = 'Show';
          }
        });        
      }
    });     
  };
  $scope.loadQuestions();     

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

  $scope.toggleQuestionStatus = function(question){
    $scope.toggleEntityStatus({
      componentId: question.componentId,
      entity: question,        
      entityId: question.questionId,
      entityName: 'questions',        
      loader: 'questionFormLoader',
      loadEntity: function(){
        $scope.loadQuestions();
      }
    });
  }; 
  
  $scope.showQuestionResponses = function(question){
     $scope.questionDetailsShow[question.questionId].flag = !$scope.questionDetailsShow[question.questionId].flag;
     if ($scope.questionDetailsShow[question.questionId].flag === false){
       $scope.questionDetailsShow[question.questionId].showResponsesText = 'Show';
     } else {
       $scope.questionDetailsShow[question.questionId].showResponsesText = 'Hide';
     }        
  };
 
  
  $scope.toggleQuestionResponseStatus = function(questionResponse, question){
      $scope.$emit('$TRIGGERLOAD', 'questionFormLoader');
      if (questionResponse.activeStatus === 'A') {
        Business.componentservice.inactivateQuestionResponse({
          componentId: question.componentId,
          questionId: question.questionId,
          responseId: questionResponse.responseId
        }).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'questionFormLoader');
          $scope.loadQuestions();
        });
      } else {
        Business.componentservice.activateQuestionResponse({
          componentId: question.componentId,
          questionId: question.questionId,
          responseId: questionResponse.responseId
        }).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'questionFormLoader');
          $scope.loadQuestions();
        });
      }
  };      
    
}]);

