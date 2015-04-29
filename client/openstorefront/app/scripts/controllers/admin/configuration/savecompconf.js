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

app.controller('SavecompconfCtrl',['$scope','business', '$q', 'componentId', 'size', '$uiModalInstance', function ($scope, Business, $q, componentId, size, $uiModalInstance) {
  $scope.$emit('$TRIGGERLOAD', 'editLoad');
  $scope.componentId = componentId || null;
  $scope.noProjects = false;
  $scope.loading = 0;
  $scope.config = null;
  $scope.component;
  $scope.issueId;
  $scope.typeahead;
  $scope.checkTicketTimeout;
  $scope.ticketContents;
  $scope.integrationConfs;
  $scope.show = {};
  $scope.data = {};
  $scope.data.grabCompId;
  $scope.data.issue;
  $scope.data.jiraProject;

  $scope.$watch('componentId', function(value){
    if (!$scope.componentId) {
    }
  })

  $scope.$watch('data', function(value) {
    if (value.grabCompId && typeof value.grabCompId === 'object') {
      if (value.grabCompId.code){
        $scope.componentId = value.grabCompId.code;
        Business.componentservice.getComponentDetails($scope.componentId).then(function(result){
          if (result){
            console.log('result', result.name);
            $scope.getTypeahead(result.name).then(function(results){
              console.log('result', _.find(results, {'code': $scope.componentId}));
              $scope.component = _.find(results, {'code': $scope.componentId});
              $scope.getIntegrationConf($scope.componentId);
            })
          }
        })
      }
    }
    if (value.issue){
      value.issue = value.issue.replace(' ', '');
      var id = value.issue.split('-');
      if (id.length  === 2){
        $scope.issueId = id[1];
        $scope.checkTicket(value.issue);
      } else {
        $scope.issueId = -1;
      }
    } else if ($scope.issueId !== undefined && $scope.issueId !== null) {
      $scope.issueId = -1;
    }
  }, true);

$scope.calcStatus = function(val)
{
  return utils.calcStatus(val);
}

$scope.getIntegrationConf = function(compId) {
  if (compId) {
      // console.log('Inside getIntegrationConf compId', compId);
      Business.configurationservice.getIntegrationConf(compId).then(function(result){
        // console.log('result', result);
        
        $scope.integrationConfs = result? result: [];
        if ($scope.integrationConfs.length){
          _.each($scope.integrationConfs, function(conf){
            conf.component = _.find($scope.typeahead, {'componentId': conf.componentId});
          })
        } else {
          $scope.config = {};
        }
        $scope.show.selectCompConf = false;
      }, function(){
        triggerAlert('There were no configurations found for that component', 'integrationConfs', 'body', 5000);
        $scope.integrationConfs = [];
        $scope.show.selectCompConf = true;
      });
    }
  };
  $scope.getIntegrationConf($scope.componentId)


  $scope.getTypeahead = function(val){
    var deferred = $q.defer();
    Business.typeahead(val).then(function(result){
      deferred.resolve(result);
    }, function(){
      deferred.resolve([]);
    })
    return deferred.promise;
  }

  $scope.checkTicket = function(ticketId) {

    if ($scope.checkTicketTimeout) {
      clearTimeout($scope.checkTicketTimeout);
    }
    $scope.checkTicketTimeout = setTimeout(function() {
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
    if (!(!$scope.componentId || $scope.componentId === -1) && !(!$scope.issueId || $scope.issueId === -1) && $scope.data.jiraProject) {

      var conf = $scope.conf? $scope.conf: {};
      
      if ($scope.integrationConfigId) {
        conf.integrationConfigId = $scope.integrationConfigId;
      }
      conf.issueNumber = $scope.data.issue;
      conf.projectType = $scope.data.jiraProject.projectType;
      conf.issueType = $scope.data.jiraProject.issueType
      conf.integrationType = $scope.integrationType? $scope.integrationType: 'JIRA';
      conf.integrationType = $scope.integrationType? $scope.integrationType: 'JIRA';

      // console.log('conf', conf);

      Business.configurationservice.saveIntegrationConf($scope.componentId, conf).then(function(result){
        // console.log('conf result', result);
        // console.log('conf', conf);
        $scope.$emit('$TRIGGEREVENT', '$UPDATECONFFORID', $scope.componentId);
        triggerAlert('The configuration was saved', 'saveIntegrationConf','.modal-dialog', 5000);
        $scope.data.jiraProject = null;
        $scope.data.issue = null;
        $scope.getIntegrationConf($scope.componentId);
      }, function(result){
        triggerAlert('<i class="fa fa-warning"></i>&nbsp;There was an error saving the configuration!', 'saveIntegrationConf','.modal-dialog', 5000);
        // console.log('Failed', result);
        
      });
    } else {
      triggerAlert('<i class="fa fa-warning"></i>&nbsp;You must select a project and issue type!', 'newConfig', '.modal-dialog', 6000);
    }
    return false;
  }

  $scope.setConfig = function(conf){
    $scope.config = angular.copy(conf);
  }
  $scope.ready = function() {
    $scope.$watch('componentCron', function(){
      // console.log('Title', $scope.componentCron);
    }, true);

    $scope.$watch('component', function(value) {
      if (value && typeof value === 'object') {
        if (value.code){
          $scope.componentId = value.code;
        } else {
          $scope.componentId = -1;
        }
      } else if ($scope.componentId !== undefined && $scope.componentId !== null) {
        Business.componentservice.getComponentDetails($scope.componentId).then(function(result){
          if (result){
            $scope.getTypeahead(result.name).then(function(results){
              $scope.component = _.find(results, {'code': $scope.componentId});
            })
          } else {
            $scope.componentId = -1;
          }
        }, function(){
          $scope.componentId = -1;
        })
      }
    }, true);


    $scope.$watch('config', function() {
      if ($scope.config) {
        // console.log('$scope.config', $scope.config);

        if ($scope.config.componentId) {
          Business.componentservice.getComponentDetails($scope.config.componentId).then(function(result){
            if (result){
              
              $scope.getTypeahead(result.name).then(function(results){
                $scope.component = _.find(results, {'code': $scope.config.componentId});
              })
            }
          })
        }
        if ($scope.config.issueNumber){
          $scope.data.issue = $scope.config.issueNumber;
        }
        if ($scope.config.integrationType){
          $scope.integrationType = $scope.config.integrationType;
        }
        if ($scope.config.integrationConfigId){
          $scope.integrationConfigId = $scope.config.integrationConfigId;
        }
        if ($scope.config.projectType && $scope.config.issueType){
          $scope.data.jiraProject = _.find($scope.projects, {'projectType': $scope.config.projectType, 'issueType': $scope.config.issueType});
        }
        $scope.conf = $scope.config;
      }
    });
  };//


  $scope.deactivateConfig = function(componentId, configId) {
    Business.configurationservice.deactivateConfig(componentId, configId).then(function(){
      triggerAlert('Deactivation of configuration complete. Refresh page to see changes.', 'mappingFields', 'body', 8000);
      $scope.getIntegrationConf(componentId);
    });
  }
  $scope.activateConfig = function(componentId, configId) {
    Business.configurationservice.activateConfig(componentId, configId).then(function(){
      triggerAlert('Activation of configuration complete. Refresh page to see changes.', 'mappingFields', 'body', 8000);
      $scope.getIntegrationConf(componentId);
    });
  }
  $scope.deleteConfig = function(componentId, configId) {
    Business.configurationservice.deleteConfig(componentId, configId).then(function(){
      triggerAlert('Deletion of configuration complete. Refresh page to see changes.', 'mappingFields', 'body', 8000);
      $scope.getIntegrationConf(componentId);
    }, function(){
      $scope.getIntegrationConf(componentId);
    });
  }
  $scope.refreshJob = function(componentId) {
    Business.configurationservice.runJob(componentId).then(function(){
      triggerAlert('Running job. Go to Jobs tool to see progress.', 'mappingFields', 'body', 8000);
      $timeout(function(){
        $scope.getAllJobs();
      }, 2000);
    });
  }


  Business.configurationservice.getMappingTypes(true).then(function(result){
    // console.log('result', result);
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


  $scope.ok = function () {
    $uiModalInstance.close();
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss();
  };

}]);


// quick custom filter to adjust display of options on the project select list.
app.filter('formatOption', [function(){
  return function( option ){
    return option.projectType + ' (' + option.issueType + ')';
  };
}]);