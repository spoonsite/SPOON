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
  $scope.issueId;
  $scope.config;
  $scope.typeahead;
  $scope.noProjects = false;
  $scope.checkTicketTimeout;
  $scope.ticketContents;
  $scope.loading = 0;
  $scope.integrationConfs;



  $scope.checkTicket = function(ticketId) {
    console.log('we are checking a ticket');
    
    if ($scope.checkTicketTimeout) {
      clearTimeout($scope.checkTicketTimeout);
    }
    $scope.checkTicketTimeout = setTimeout(function() {
      console.log('We hit the timeout');
      $scope.loading++;
      Business.configurationservice.checkTicket(ticketId).then(function(result){
        $scope.ticketContents = result;
        $scope.loading--;
      }, function(){
        $scope.ticketContents = null;
        $scope.loading--;
      })
    }, 1000);
  }

  $scope.$watch('loading', function(value){ //
    if (value > 0){
      $scope.$emit('$TRIGGERLOAD', 'ticketContLoad');
    } else {
      $scope.$emit('$TRIGGERUNLOAD', 'ticketContLoad');
    }
  }) 


  $scope.saveComponentConf = function(){
    if (!(!$scope.componentId || $scope.componentId === -1) && !(!$scope.issueId || $scope.issueId === -1) && $scope.jiraProject) {

      var conf = $scope.conf? $scope.conf: {};
      if ($scope.integrationConfigId) {
        conf.integrationConfigId = $scope.integrationConfigId;
      }
      conf.issueNumber = $scope.issue;
      conf.projectType = $scope.jiraProject.projectType;
      conf.issueType = $scope.jiraProject.issueType
      conf.integrationType = $scope.integrationType? $scope.integrationType: 'JIRA';

      console.log('conf', conf);

      Business.configurationservice.saveIntegrationConf($scope.componentId, conf).then(function(result){
        console.log('conf result', result);
        console.log('conf', conf);
        $scope.$emit('$TRIGGEREVENT', '$UPDATECONFFORID', $scope.componentId);
        triggerAlert('The configuration was saved', 'saveIntegrationConf','.modal-dialog', 5000);
      }, function(result){
        triggerAlert('<i class="fa fa-warning"></i>&nbsp;There was an error saving the configuration!', 'saveIntegrationConf','.modal-dialog', 5000);
        console.log('Failed', result);
        
      });
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
        if ($scope.config.integrationType){
          $scope.integrationType = $scope.config.integrationType;
        }
        if ($scope.config.integrationConfigId){
          $scope.integrationConfigId = $scope.config.integrationConfigId;
        }
        if ($scope.config.projectType && $scope.config.issueType){
          $scope.jiraProject = _.find($scope.projects, {'projectType': $scope.config.projectType, 'issueType': $scope.config.issueType});
        }
        $scope.conf = $scope.config;
      }
    });
  };

  $scope.$watch('issue', function(value) {
    if (value) {
      value = value.replace(' ', '');
      var id = value.split('-');
      if (id.length  === 2){
        $scope.issueId = id[1];
        $scope.checkTicket(value);
      } else {
        $scope.issueId = -1;
      }
    } else if ($scope.issueId !== undefined && $scope.issueId !== null) {
      $scope.issueId = -1;
    }
  }, true);

  Business.componentservice.getComponentList().then(function(result) {
    Business.typeahead(result, null).then(function(value){
      if (value) {
        console.log('value', value);
        $scope.typeahead = value;
      } else {
        $scope.typeahead = null;
      }
      Business.configurationservice.getMappingTypes(true).then(function(result){
        console.log('result', result);
        _.each(result, function(item){
          item.description = item.projectType + ' - ' + item.issueType;
        });
        $scope.$emit('$TRIGGERUNLOAD', 'editLoad');
        $scope.noProjects = false;
        $scope.projects = result? result: [];
        $scope.ready();
      }, function() {
        $scope.noProjects = true;
        $scope.projects = [];
        $scope.ready();
      });
    }, function() {
      $scope.$emit('$TRIGGERUNLOAD', 'editLoad');
      $scope.ready();
    });
  }, function() {
    $scope.$emit('$TRIGGERUNLOAD', 'editLoad');
    $scope.ready();
  });


}]);


// quick custom filter to adjust display of options on the project select list.
app.filter('formatOption', [function(){
  return function( option ){
    return option.projectType + ' (' + option.issueType + ')';
  };
}]);