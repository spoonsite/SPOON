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

app.controller('AdminConfigurationCtrl',['$scope','business', '$q', '$timeout',  function ($scope, Business, $q, $timeout) {
  $scope.username;
  $scope.modal = {};
  $scope.password;
  $scope.selectedMapping;
  $scope.projects;
  $scope.issueOptions;
  $scope.jiraProject;
  $scope.jiraIssue;
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

  Business.configurationservice.getMappingTypes().then(function(result){
    if (result) {
      $scope.previousMappings = result;
    } else {
      $scope.previousMappings = [];
    }
  }, function() {
    $scope.previousMappings = [];
  });

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
        console.log('we hit the setup');
        var found = _.find($scope.previousMappings, {'attributeType': value.storeField.attributeType, 'projectType': $scope.jiraProject.code, 'issueType': $scope.jiraIssue.name});
        console.log('found', found);
        
        if (found) {
          $timeout(function() {
            _.each(found.mapping, function(map){
              console.log('map', map);
              var code = _.find($scope.masterSelected, {'label': map.externalCode});
              if (code) {
                console.log('code', code);
                var indexOf = $scope.masterSelected.indexOf(code);
                $scope.masterSelected.splice(indexOf, 1);
                var index = _.find($scope.storeCodes, function(item){
                  return (item.attributeCodePk.attributeType === map.attributeType && item.attributeCodePk.attributeCode === map.localCode);
                })
                if (index && index.selected && index.selected.indexOf(code) < 0) {
                  index.selected.push(code);
                } else if (index && !index.selected) {
                  index.selected = [];
                  index.selected.push(code);
                }
              }
            })
          }, 200);
        }
      }
    }, function() {
      $scope.loading--;
      $scope.storeCodes = [];
    });
}
$scope.getXRefTypes = function(){
  Business.configurationservice.getXRefTypes().then(function(result){
    if (result.length > 0) {
      $scope.xRefTypes = result;
      $scope.selectedMapping = result[0];
    }
  });
}

$scope.setup = function() {
  $scope.loading++;
  $scope.getXRefTypes();
  $scope.getProjects();
}

$scope.setup();

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
  $scope.$emit('$TRIGGEREVENT', 'updateBody');
  deferred.resolve();
  return deferred.promise;
};



$scope.saveMappingConf = function(){
  if ($scope.storeCodes.length && $scope.watch.jiraField) {

      // console.log('storeCodes', $scope.storeCodes);
      // console.log('Field', $scope.jiraField);
      // console.log('Project', $scope.jiraProject);
      // console.log('Issue', $scope.jiraIssue);
      var xRefMaps = [];
      var type = $scope.storeCodes[0].attributeCodePk.attributeType;
      _.each($scope.storeCodes, function(localCode){
        _.each(localCode.selected, function(externalCode){
          xRefMaps.push({
            'attributeType': type,
            'localCode': localCode.attributeCodePk.attributeCode,
            'externalCode': externalCode.value
          });
        });
      });
      var body = {};
      body.type = {
        'attributeType': type,
        'fieldName': $scope.watch.jiraField.name,
        'fieldId': $scope.watch.jiraField.id,
        'fieldKey': $scope.watch.jiraField.key,
        'projectType': $scope.jiraProject.code,
        'issueType': $scope.jiraIssue.name,
        'integrationType': 'JIRA'
      };
      body.map = xRefMaps;

      Business.configurationservice.saveMappingConf(body).then(function(result){
        // console.log('result', result);
      }, function(){
        // triggerAlert('There was an error saving the mapping', 'mappingFields', 'body', 6000);
      })
      
    }
    return false;
  }

  $scope.saveGlobalConf = function(){
    var conf = {};
    conf.componentId = $scope.componentId;
    conf.issueId = $scope.issueId;
    conf.refreshRate = $scope.componentCron? $scope.componentCron: '';
    //save the object;
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
        $scope.jiraProject = found;
        Business.configurationservice.getIssueOptions(found).then(function(result){
          $scope.issueOptions = result? result: [];
          $timeout(function(){
            found = _.find($scope.issueOptions, {'name': mappingModel.issueType});
            if (found) {
              var issueType = found;
              $scope.jiraIssue = found;
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
              $scope.jiraIssue = false;
              $scope.loading--;
              triggerAlert('There was an error loading this mapping for edit', 'mappingFields', 'body', 6000);
            }
          }, 100);
        }); //
      } else { //
        $scope.jiraProject = false;
        $scope.loading--;
        triggerAlert('There was an error loading this mapping for edit', 'mappingFields', 'body', 6000);
      }
    }
  }

  $scope.moveLeft = function(code) {
    console.log('code - move Left', code);
    console.log('masterSelected', $scope.masterSelected);
    console.log('$scope.masterSelect', $scope.jiraCodes.masterSelect);
    
    if(!code.selected){
      code.selected = [];
    }
    var right = $scope.jiraCodes.masterSelect;
    for (var i = 0; i < right.length; i++) {
      var el = right[i];
    console.log('code.selected.indexOf(el) should be < 0', code.selected.indexOf(el));
      if (code.selected.indexOf(el) < 0) {
        code.selected.push(el);
      }
    console.log('masterselected index should be > -1', $scope.masterSelected.indexOf(el));
      var indexOf = $scope.masterSelected.indexOf(el);
      $scope.masterSelected.splice(indexOf, 1);
    }
  };

  $scope.moveRight = function(code) {
    console.log('code - move Right', code);
    console.log('masterSelected', $scope.masterSelected);
    if(!code.selected){
      code.selected = [];
    }
    var toRemove = code.toRemove;
    for (var i = 0; i < toRemove.length; i++) {
      var el = toRemove[i];
      if ($scope.masterSelected.indexOf(el) < 0) {
        $scope.masterSelected.push(el);
      }
      var indexOf = code.selected.indexOf(el);
      code.selected.splice(indexOf, 1);
    }
  };

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

  $scope.$watch('jiraProject', function(value){
    if (value && typeof value === 'object') {
      $scope.jiraIssue = null;
      $scope.loading++;
      $scope.getIssueOptions(value);
    } 
    $scope.jiraIssue = false;
    $scope.issueOptions = [];
  })

  $scope.$watch('jiraIssue', function(value){
    if (value && typeof value === 'object') {
      $scope.loading++;
      $scope.loading++;
      $scope.getJiraFields($scope.jiraProject, $scope.jiraIssue);
      $scope.getStoreFields();
    } 
    $scope.watch.storeField = false;
    $scope.storeFields = null;
    $scope.jiraField = false;
    $scope.fields = [];
    $scope.storeCodes = [];
  })

  $scope.$watch('watch', function(value, oldvalue) { //
    if (value && value.storeField && typeof value.storeField === 'object' && !angular.equals(value.storeField, oldvalue.storeField)) {
      var found = _.find($scope.previousMappings, {'attributeType': value.storeField.attributeType, 'projectType': $scope.jiraProject.code, 'issueType': $scope.jiraIssue.name});
      if (found) {
        $scope.jiraField = null;
        triggerAlert('This attribute has previously been mapped. The fields have been prepopulated with the old values.', 'mappingFields', 'body', 6000);
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
