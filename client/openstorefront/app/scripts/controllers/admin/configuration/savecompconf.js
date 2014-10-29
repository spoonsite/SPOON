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

app.controller('SavecompconfCtrl',['$scope','business',  function ($scope, Business) {
  $scope.$emit('$TRIGGERLOAD', 'editLoad');
  $scope.component;
  $scope.issue;
  $scope.componentId;
  $scope.jiraProject;
  $scope.jiraIssue;
  $scope.issueId;
  $scope.config;
  $scope.typeahead;



  $scope.getProjects = function() {
    Business.configurationservice.getProjects().then(function(result){
      $scope.projects = result? result: [];
    }, function() {
      $scope.projects = [];
    });
  }

  $scope.getProjects();

  $scope.getIssueOptions = function(project) {
    if (project && project.code) {
      Business.configurationservice.getIssueOptions(project).then(function(result){
        $scope.issueOptions = result? result: [];
      }, function() {
        $scope.issueOptions = [];
      });
    }
  }

  $scope.saveComponentConf = function(){
    if (!(!$scope.componentId || $scope.componentId === -1) && !(!$scope.issueId || $scope.issueId === -1) && !(!$scope.jiraIssue || !$scope.jiraProject)) {
      console.log('$scope', $scope.componentId);
      console.log('$scope', $scope.issueId);
      console.log('$scope', $scope.jiraIssue);
      console.log('$scope', $scope.jiraProject);

      var conf = {};
      conf.componentId = $scope.componentId;
      conf.issueId = $scope.issueId;
      console.log('$scope.componentCron', $scope.componentCron);
      //save the object;
      console.log('conf', conf);
    } else {
      triggerAlert('<i class="fa fa-warning"></i>&nbsp;You must select a project and issue type!', 'newConfig', '.modal-dialog', 6000);
    }
    return false;
  }

  $scope.ready = function() {

    $scope.$watch('componentCron', function(){
      // console.log('Title', $scope.componentCron);
    }, true);

    $scope.$watch('component', function(value) {
      if (value && typeof value === 'object') {
        if (value.componentId){
          $scope.componentId = value.componentId;
        } else {
          $scope.componentId = -1;
        }
      } else if ($scope.componentId !== undefined && $scope.componentId !== null) {
        $scope.componentId = -1;
      }
    }, true);

    $scope.config = Business.getLocal('configId');

    $scope.$watch('config', function() {
      if ($scope.config && $scope.typeahead) {
        console.log('$scope.config', $scope.config);
        
        if ($scope.config.componentId) {
          $scope.component = _.find($scope.typeahead, {'componentId': $scope.config.componentId});
        }
        if ($scope.config.issueNumber){
          $scope.issue = $scope.config.issueNumber;
        }
        if ($scope.config.overRideRefreshRate) {
          $scope.overRideDefault = true;          
          $scope.componentCron = $scope.config.overRideRefreshRate;
        }
      }
    });
  };

  $scope.$watch('issue', function(value) {
    if (value) {
      value = value.replace(' ', '');
      var id = value.split('-');
      if (id.length  === 2){
        $scope.issueId = id[1];
      } else {
        $scope.issueId = -1;
      }
    } else if ($scope.issueId !== undefined && $scope.issueId !== null) {
      $scope.issueId = -1;
    }
  }, true);

  $scope.$watch('jiraProject', function(value){
    if (value && typeof value === 'object') {
      $scope.jiraIssue = null;
      $scope.getIssueOptions(value);
    } 
    $scope.jiraIssue = false;
    $scope.issueOptions = [];
  })


  Business.componentservice.getComponentList().then(function(result) {
    Business.typeahead(result, null).then(function(value){
      if (value) {
        console.log('value', value);
        $scope.typeahead = value;
      } else {
        $scope.typeahead = null;
      }
      $scope.ready();
      $scope.$emit('$TRIGGERUNLOAD', 'editLoad');
    }, function() {
      $scope.ready();
    });
  }, function() {
    $scope.ready();
  });


}]);