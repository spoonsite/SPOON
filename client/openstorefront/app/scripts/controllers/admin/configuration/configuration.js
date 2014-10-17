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

app.controller('AdminConfigurationCtrl',['$scope','business',  function ($scope, Business) {
  $scope.username;
  $scope.password;
  $scope.component;
  $scope.issue;
  $scope.componentId;
  $scope.issueId;
  $scope.typeahead;
  $scope.overRideDefault = false;
  $scope.component_cron;
  $scope.types = [
  {
    'label': 'Jira Configuration',
    'code': 'jira'
  }
  ]
  $scope.type = 'jira';

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
  });

  $scope.$watch('issue', function(value) {
    if (value && typeof value === 'object') {
      if (value.componentId){
        $scope.issueId = value.componentId;
      } else {
        $scope.issueId = -1;
      }
    } else if ($scope.issueId !== undefined && $scope.issueId !== null) {
      $scope.issueId = -1;
    }
  });

  Business.componentservice.getComponentList().then(function(result) {
    // console.log('result', result);
    
    Business.typeahead(result, null).then(function(value){
      if (value) {
        $scope.typeahead = value;
      } else {
        $scope.typeahead = null;
      }
    });
  });

  $scope.$watch('component_cron', function(value){
    console.log('value', value);
  })


  $scope.saveComponentConf = function(){
    var conf = {};
    conf.componentId = $scope.componentId;
    conf.issueId = $scope.issueId;
    console.log('$scope.component_cron', $scope.component_cron);
    
    conf.refreshRate = $scope.component_cron? $scope.component_cron: '';
    //save the object;
    console.log('conf', conf);
    return false;
  }
  $scope.saveMappingConf = function(){
    var conf = {};
    conf.componentId = $scope.componentId;
    conf.issueId = $scope.issueId;
    console.log('$scope.component_cron', $scope.component_cron);
    conf.refreshRate = $scope.component_cron? $scope.component_cron: '';
    //save the object;
    console.log('conf', conf);
    return false;
  }
  $scope.saveGlobalConf = function(){
    var conf = {};
    conf.componentId = $scope.componentId;
    conf.issueId = $scope.issueId;
    console.log('$scope.component_cron', $scope.component_cron);
    conf.refreshRate = $scope.component_cron? $scope.component_cron: '';
    //save the object;
    console.log('conf', conf);
    return false;
  }

}]);
