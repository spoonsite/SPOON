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

app.controller('AdminConfigurationCtrl',['$scope','business', '$q', '$timeout', function ($scope, Business, $q, $timeout) {
  $scope.username;
  $scope.modal = {};
  $scope.password;
  $scope.selectedMapping;
  $scope.projects;
  $scope.issueOptions;
  $scope.jira = {};
  $scope.jira.jiraProject;
  $scope.jira.jiraIssue;
  $scope.watch = {};
  $scope.watch.storeField;
  $scope.watch.jiraField;
  $scope.storeCodes;
  $scope.masterSelected;
  $scope.jiraCodes = {};
  $scope.jiraCodes.masterSelect;
  $scope.componentConfMap;
  $scope.componentId;
  $scope.typeahead;
  $scope.previousMappings;
  $scope.overRideDefault = false;
  $scope.cron = {};
  $scope.cron.componentCron = '0 0 0 1/1 * ? *';
  $scope.cron.global_cron = '0 0 0 1/1 * ? *';
  $scope.component = {};
  $scope.component.compId;
  $scope.integrationConfs = null;
  $scope.show = {
    'selectCompConf': true,
    'showCodeSelection': true
  };

  $scope.loading = 0;

  $scope.types = [
  {
    'label': 'Jira Configuration',
    'code': 'jira'
  }
  ]
  $scope.type = 'jira';

  Business.componentservice.getComponentList().then(function(result) {
    Business.typeahead(result, null).then(function(value){
      if (value) {
        $scope.typeahead = value;
      } else {
        $scope.typeahead = null;
      }
    }, function() {
    });
  }, function() {
  });

  $scope.getGlobalConfig = function(){
    Business.configurationservice.getGlobalConfig().then(function(result) {
      // console.log('refreshRate', result);
      $scope.cron.global_cron = result? result.jiraRefreshRate? result.jiraRefreshRate: null: null;
      $scope.cron.global_cron_display = result? result.cronExpressionDescription? result.cronExpressionDescription + ' (' + result.jiraRefreshRate + ')': null: null;
    }, function() {
      $scope.cron.global_cron = null;
    });
  };

  $scope.getAllJobs = function(){
    Business.configurationservice.getAllJobs().then(function(result){
      // console.log('result', result);
      $scope.allJobs = result? result: [];
    }, function(){
      $scope.allJobs = [];
    });
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
        }
        $scope.show.selectCompConf = false;
      }, function(){
        triggerAlert('There were no configurations found for that component', 'integrationConfs', 'body', 5000);
        $scope.integrationConfs = [];
        $scope.show.selectCompConf = true;
      });
    }
  };


  $scope.getMappingTypes = function(){
    Business.configurationservice.getMappingTypes().then(function(result){
      if (result) {
        $scope.previousMappings = result;
      } else {
        $scope.previousMappings = [];
      }
    }, function() {
      $scope.previousMappings = [];
    });
  }

  $scope.getProjects = function() {
    Business.configurationservice.getProjects().then(function(result){
      $scope.projects = result? result: [];
      $scope.loading--;
      if (!$scope.projects.length) {
        $scope.noProjects = true;
      } else {
        $scope.noProjects = false;
      }
    }, function() {
      $scope.loading--;
      $scope.projects = [];
      $scope.noProjects = true;
    });
  }

  $scope.getIssueOptions = function(project) {
    if (project && project.code) {
      Business.configurationservice.getIssueOptions(project).then(function(result){
        $scope.loading--;
        $scope.issueOptions = result? result: [];

      }, function() {
        $scope.loading--;
        $scope.issueOptions = [];
      });
    }
  }

  $scope.getJiraFields = function(project, issueType) {
    if (project && project.code && issueType && issueType.name) {
      Business.configurationservice.getJiraFields(project, issueType).then(function(result){
        $scope.loading--;
        $scope.fields = result? result: [];

      }, function() {
        $scope.loading--;
        $scope.fields = [];
      });
    }
  }

  $scope.getStoreFields = function() {
    Business.configurationservice.getStoreFields().then(function(result){
      $scope.loading--;
      $scope.storeFields = result? result: [];

    }, function() {
      $scope.loading--;
      $scope.storeFields = [];
    });
  }

  $scope.getStoreCodes = function(attribute, setup, value) {
    Business.configurationservice.getStoreCodes(attribute).then(function(result){
      $scope.loading--;
      $scope.storeCodes = result? result: [];
      if (setup) {
        // console.log('we hit the setup');
        var found = _.find($scope.previousMappings, {'attributeType': value.storeField.attributeType, 'projectType': $scope.jira.jiraProject.code, 'issueType': $scope.jira.jiraIssue.name});
        // console.log('found', found);
        
        if (found) {
          $timeout(function() {
            _.each(found.mapping, function(map){
              // console.log('map', map);
              var code = _.find($scope.masterSelected, {'label': map.externalCode});
              if (code) {
                // console.log('code', code);
                var indexOf = $scope.masterSelected.indexOf(code);
                $scope.masterSelected.splice(indexOf, 1);
                var index = _.find($scope.storeCodes, function(item){
                  return (item.attributeCodePk.attributeType === map.attributeType && item.attributeCodePk.attributeCode === map.localCode);
                });
                if (index && index.selected && index.selected.indexOf(code) < 0) {
                  index.selected.push(code);
                } else if (index && !index.selected) {
                  index.selected = [];
                  index.selected.push(code);
                }
              }
            });
            $scope.jiraCodes.masterSelect = null;
            $('.codeSelection:selected').removeAttr("selected");
            $timeout(function(){
              $('.codeSelection').each(function(){
                var width = $(this).width();
                $(this).width(0);
                $(this).width(width);
              })
            });
          }, 200);
        } else { //
        }
      }
    }, function() {
      $scope.loading--;
      $scope.storeCodes = [];
    });
  }; //

  $scope.getRefTypes = function(){
    Business.configurationservice.getXRefTypes().then(function(result){
      if (result && result.length > 0) {
        $scope.xRefTypes = result;
        $scope.selectedMapping = result[0];
      }
    });
  };

  (function(){
    $scope.loading++;
    $scope.getGlobalConfig();
    $scope.getAllJobs();
    $scope.getMappingTypes();
    $scope.getProjects();
    $scope.getRefTypes();
  }());

  $scope.sendToModal = function(mapping) {
    $scope.getConfigId(mapping).then(function(result){
      if (result && result.type) {
        $scope.setupModal(result); 
        $scope.openModal('compConf');
      }
    }, function(result){

      return false;
    })
  }

  $scope.getConfigId = function(mapping) {
    var deferred = $q.defer();
    if(!mapping) {
      deferred.reject(false);
    } else {
      Business.configurationservice.getConfigId(mapping).then(function(result){
        if (result) {
          deferred.resolve(result);
        }
      }, function(){
        deferred.reject(false);
      });
    }
    return deferred.promise
  }


  $scope.setupModal = function(config) {

    if (config) {
      Business.saveLocal('configId', config)
    }
    var deferred = $q.defer();
    $scope.modal.classes = '';
    $scope.modal.nav = {
      'current': 'Save New Configuration',
      'bars': [
      {
        'title': 'Save New Configuration',
        'include': 'views/admin/configuration/savecompconf.html'
      }
      ]
    };
    // console.log('we hit the modal', $scope.modal);
    $scope.$emit('$TRIGGEREVENT', 'updateBody');
    deferred.resolve();
    return deferred.promise;
  };

  $scope.deleteMapping = function(mapping) {
    var response = window.confirm("Are you sure you want to delete this Mapping?");
    if (mapping && response){
      Business.configurationservice.deleteMapping(mapping.attributeType).then(function(result) {
        triggerAlert('The mapping has been deleted', 'mappingFields', 'body', 6000);
        $scope.getMappingTypes();
      })
    }
  }



  $scope.saveMappingConf = function(){
    if ($scope.storeCodes.length && $scope.watch.jiraField) {

      var xRefMaps = [];
      var type = $scope.storeCodes[0].attributeCodePk.attributeType;
      _.each($scope.storeCodes, function(localCode){
        _.each(localCode.selected, function(externalCode){
          xRefMaps.push({
            'attributeType': type,
            'localCode': localCode.attributeCodePk.attributeCode,
            'externalCode': externalCode.label
          });
        });
      });
      var body = {};
      body.type = {
        'attributeType': type,
        'fieldName': $scope.watch.jiraField.name,
        'fieldId': $scope.watch.jiraField.id,
        'projectType': $scope.jira.jiraProject.code,
        'issueType': $scope.jira.jiraIssue.name,
        'integrationType': 'JIRA'
      };
      body.map = xRefMaps;

      Business.configurationservice.saveMappingConf(body).then(function(result){
        if ($scope.masterSelected.length) {
          triggerAlert('<i class="fa fa-warning"></i>&nbsp;There are unmapped codes. This could lead to an error.<br>The mapping was saved successfully', 'mappingFields', 'body', 8000);
        } else {
          triggerAlert('The mapping was saved successfully', 'mappingFields', 'body', 6000);
        }
        $scope.getMappingTypes();
        // console.log('result', result);
      }, function(){
        // triggerAlert('There was an error saving the mapping', 'mappingFields', 'body', 6000);
      })

    }
    return false;
  }

  $scope.saveGlobalConf = function(){
    // console.log('$scope.global_cron', $scope.cron.global_cron);
    Business.configurationservice.saveGlobalConf($scope.cron.global_cron).then(function(result){
      // console.log('saved global config', result);
      $scope.getGlobalConfig();
      // do something if you want to after you save the global conf
    });
    return false;
  }

  $scope.saveCompRefresh = function(){
    Business.configurationservice.saveCompRefresh($scope.component.compId, $scope.cron.componentCron).then(function(result){
      // do something if you want to after you save the component's cron
    });
    return false;
  }
  $scope.removeCompRefresh = function(){
    Business.configurationservice.removeCompRefresh($scope.component.compId).then(function(result){
      $scope.getAllJobs();
    });
    return false;
  }

  $scope.forceRefresh = function() {
    return false;
  }

  $scope.isInMasterList = function(value) {
    return !!_.find($scope.masterSelected, {'value': value});
  }


  $scope.setEdit = function(mappingModel){
    if (mappingModel && $scope.projects && $scope.projects.length) {
      $scope.loading++;
      var found = _.find($scope.projects, {'code': mappingModel.projectType});
      if (found) {
        var project = found;
        $scope.jira.jiraProject = found;
        Business.configurationservice.getIssueOptions(found).then(function(result){
          $scope.issueOptions = result? result: [];
          $timeout(function(){
            found = _.find($scope.issueOptions, {'name': mappingModel.issueType});
            if (found) {
              var issueType = found;
              $scope.jira.jiraIssue = found;
              if (project && project.code && issueType && issueType.name) {
                Business.configurationservice.getStoreFields().then(function(storeFields){
                  $scope.storeFields = storeFields? storeFields: [];
                  $timeout(function(){
                    var found = _.find($scope.storeFields, {'attributeType': mappingModel.attributeType});
                    if (found) {
                      Business.configurationservice.getJiraFields(project, issueType).then(function(jiraFields){
                        $scope.jiraFields = jiraFields? jiraFields: [];
                        $timeout(function(){
                          $scope.watch.storeField = found;
                          $scope.loading--;
                        }, 100);
                      });
                    } else {
                      $scope.loading--;
                      triggerAlert('There was an error loading this mapping for edit', 'mappingFields', 'body', 6000);
                    }
                  }, 100);
                });
              }  else {
                $scope.watch.storeField = undefined;
                $scope.loading--;
                triggerAlert('There was an error loading this mapping for edit', 'mappingFields', 'body', 6000);
              }
            } else {
              $scope.jira.jiraIssue = false;
              $scope.loading--;
              triggerAlert('There was an error loading this mapping for edit', 'mappingFields', 'body', 6000);
            }
          }, 100);
        }); //
      } else { //
        $scope.jira.jiraProject = false;
        $scope.loading--;
        triggerAlert('There was an error loading this mapping for edit', 'mappingFields', 'body', 6000);
      }
    }
  }

  $scope.moveLeft = function(code) {
    // console.log('code - move Left', code);
    // console.log('masterSelected', $scope.masterSelected);
    // console.log('$scope.masterSelect', $scope.jiraCodes.masterSelect);
    if(!code.selected){
      code.selected = [];
    }
    if ($scope.jiraCodes.masterSelect) {

      var right = $scope.jiraCodes.masterSelect;
      for (var i = 0; i < right.length; i++) {
        var el = right[i];
        // console.log('code.selected.indexOf(el) should be < 0', code.selected.indexOf(el));
        if (code.selected.indexOf(el) < 0) {
          code.selected.push(el);
        }
        // console.log('masterselected index should be > -1', $scope.masterSelected.indexOf(el));
        var indexOf = $scope.masterSelected.indexOf(el);
        $scope.masterSelected.splice(indexOf, 1);
      }
    }
    $scope.jiraCodes.masterSelect = null;
    $('.codeSelection:selected').removeAttr("selected");
    $timeout(function(){
      $('.codeSelection').each(function(){
        var width = $(this).width();
        $(this).width(0);
        $(this).width(width);
      });
    }, 10);
  };

  $scope.moveRight = function(code) {
    // console.log('code - move Right', code);
    // console.log('masterSelected', $scope.masterSelected);
    if(!code.selected){
      code.selected = [];
    }
    if (code.toRemove)
    {
      var toRemove = code.toRemove;
      for (var i = 0; i < toRemove.length; i++) {
        var el = toRemove[i];
        if ($scope.masterSelected.indexOf(el) < 0) {
          $scope.masterSelected.push(el);
        }
        var indexOf = code.selected.indexOf(el);
        code.selected.splice(indexOf, 1);
      }
    }
    code.toRemove = null;
    $('.codeSelection:selected').removeAttr("selected");
    $timeout(function(){
      $('.codeSelection').each(function(){
        var width = $(this).width();
        $(this).width(0);
        $(this).width(width);
      });
    }, 10);
  };



  $scope.deactivateJob = function(componentId) {
    Business.configurationservice.deactivateJob(componentId).then(function(){
      $scope.getAllJobs();
    });
  }
  $scope.activateJob = function(componentId) {
    Business.configurationservice.activateJob(componentId).then(function(){
      $scope.getAllJobs();
    });
  }
  $scope.deleteJob = function(componentId) {
    Business.configurationservice.deleteJob(componentId).then(function(){
      $scope.getAllJobs();
      $scope.compId = '';
      $scope.selectCompConf = true;
    }, function(){
      $scope.getAllJobs();
      $scope.compId = '';
      $scope.selectCompConf = true;
    });
  }
  $scope.deactivateConfig = function(componentId, configId) {
    Business.configurationservice.deactivateConfig(componentId, configId).then(function(){
      $scope.getIntegrationConf(componentId);
    });
  }
  $scope.activateConfig = function(componentId, configId) {
    Business.configurationservice.activateConfig(componentId, configId).then(function(){
      $scope.getIntegrationConf(componentId);
    });
  }
  $scope.deleteConfig = function(componentId, configId) {
    Business.configurationservice.deleteConfig(componentId, configId).then(function(){
      $scope.getIntegrationConf(componentId);
    }, function(){
      $scope.getIntegrationConf(componentId);
    });
  }
  $scope.refreshJob = function(componentId) {
    Business.configurationservice.runJob(componentId).then(function(){
      $timeout(function(){
        $scope.getAllJobs();
      }, 2000);
    });
  }
  $scope.refreshConfig = function(componentId, configId) {
    Business.configurationservice.runConfig(componentId, configId).then(function(){
      $timeout(function(){
        $scope.getIntegrationConf(componentId);
      }, 2000);
    });
  }
  $scope.runAllJobs = function() {
    Business.configurationservice.runAllJobs().then(function(){
      $timeout(function(){
        $scope.getAllJobs();
        $scope.component.compId = '';
        $scope.show.selectCompConf = true;
      }, 2000);
    }, function(){
      $timeout(function(){
        $scope.getAllJobs();
        $scope.component.compId = '';
        $scope.show.selectCompConf = true;
      }, 2000);
    });
  }

  $scope.calcStatus = function(val)
  {
    switch(val){
      case 'C':
      return 'Complete'
      break;
      case 'E':
      return 'Error'
      break;
      case 'W':
      return 'Working'
      break;
      default:
      return 'Error'
      break;
    }
  }


  $scope.$on('$UPDATECONFFORID', function(event, id){
    $scope.getIntegrationConf(id);
    $scope.getAllJobs();
  })

  $scope.tabs = [{
    'title': 'All jobs',
    'loc': 'views/admin/configuration/jobs.html'
  },{
    'title': 'Component Job Configurations',
    'loc': 'views/admin/configuration/configuration.html'
  }]


  $scope.$watch('componentConfMap', function(value) {
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

  $scope.$watch('selectedMapping', function(value){
    if (value) {
      Business.configurationservice.getConfigurations(value).then(function(result){
        if (result && result.length > 0) {
          $scope.configurations = result;
        } else {
          $scope.configurations = null;
        }
      }, function(result) {
        $scope.configurations = null;
      });
    }
  });

  $scope.$watch('jira', function(newvalue, oldvalue){
    if ( newvalue.jiraProject && typeof newvalue.jiraProject === 'object') {
      if (!angular.equals(newvalue.jiraProject, oldvalue.jiraProject)) {
        $scope.loading++;
        $scope.getIssueOptions(newvalue.jiraProject);
        newvalue.jiraIssue = null;
      }
    } else {
      newvalue.jiraIssue = null;
      $scope.issueOptions = [];
    }
    if (newvalue.jiraIssue && typeof newvalue.jiraIssue === 'object') {
      if (!angular.equals(newvalue.jiraIssue, oldvalue.jiraIssue)) {
        $scope.loading++;
        $scope.loading++;
        $scope.getJiraFields(newvalue.jiraProject, newvalue.jiraIssue);
        $scope.getStoreFields();
      }
    } else {
      $scope.watch.storeField = false;
      $scope.storeFields = null;
      $scope.jiraField = false;
      $scope.fields = [];
      $scope.storeCodes = [];
    } 
  }, true);

  $scope.$watch('component', function(value){
    if (value && value.compId && !isNaN(parseInt(value.compId))){
      // console.log('$scope.compId2', value.compId);
      $scope.getIntegrationConf(value.compId);
    } 
  }, true)

  $scope.$watch('watch', function(value, oldvalue) { //
    if (value && value.storeField && typeof value.storeField === 'object' && !angular.equals(value.storeField, oldvalue.storeField)) {
      var found = _.find($scope.previousMappings, {'attributeType': value.storeField.attributeType, 'projectType': $scope.jira.jiraProject.code, 'issueType': $scope.jira.jiraIssue.name});
      if (found) {
        $scope.jiraField = null;
        triggerAlert('This attribute has previously been mapped. The fields have been prepopulated with the old values. If you save this mapping, it will override the old values.', 'mappingFields', 'body', 8000);
        var field = _.find($scope.fields, {'id': found.fieldId});
        if (field) {
          $scope.watch.jiraField = field;
          value.jiraField = field;
        }
      } else {
        value.jiraField = null;
      }
    }
    if (value && value.jiraField && typeof value.jiraField === 'object' && value.storeField && typeof value.storeField === 'object') {
      $scope.masterSelected = angular.copy(value.jiraField.allowedValues);
      $scope.loading++;
      $scope.getStoreCodes(value.storeField, true, value);
    }
  }, true);

  $scope.$watch('loading', function(value){ //
    if (value > 0){
      $scope.$emit('$TRIGGERLOAD', 'JiraConfigLoad');
    } else {
      $scope.$emit('$TRIGGERUNLOAD', 'JiraConfigLoad');
    }
  })  

}]);

