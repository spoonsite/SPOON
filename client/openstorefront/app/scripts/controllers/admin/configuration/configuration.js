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

app.controller('AdminConfigurationCtrl',['$scope','business', '$q',  function ($scope, Business, $q) {
  $scope.username;
  $scope.modal = {};
  $scope.password;
  $scope.selectedMapping;
  $scope.projects;
  $scope.issueOptions;
  $scope.jiraProject;
  $scope.jiraIssue;
  $scope.jiraField;
  $scope.storeCodes;
  $scope.masterSelected;
  $scope.componentConfMap;
  $scope.componentId;
  $scope.typeahead;

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
  $scope.getStoreCodes = function(attribute) {
    Business.configurationservice.getStoreCodes(attribute).then(function(result){
      $scope.loading--;
      $scope.storeCodes = result? result: [];
    }, function() {
      $scope.loading--;
      $scope.storeCodes = [];
    });
  }
  $scope.getXRefTypes = function(){
    Business.configurationservice.getXRefTypes().then(function(result){
      console.log('result', result);
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
    console.log('mapping', mapping);
    
    $scope.getConfigId(mapping).then(function(result){
      console.log('result', result);
      if (result && result.type) {
        $scope.setupModal(result); 
        $scope.openModal('compConf');
      }
    }, function(result){
      console.log('There was a problem');
      
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
    if ($scope.storeCodes.length && $scope.jiraField) {

      console.log('storeCodes', $scope.storeCodes);
      console.log('Field', $scope.jiraField);
      console.log('Project', $scope.jiraProject);
      console.log('Issue', $scope.jiraIssue);
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
        'fieldName': $scope.jiraField.name,
        'fieldKey': $scope.jiraField.id,
        'projectType': $scope.jiraProject.code,
        'issueType': $scope.jiraIssue.name,
        'integrationType': 'jira'
      };
      body.map = xRefMaps;

      console.log('body', body);
      
    }
    return false;
  }

  $scope.saveGlobalConf = function(){
    var conf = {};
    conf.componentId = $scope.componentId;
    conf.issueId = $scope.issueId;
    console.log('$scope.componentCron', $scope.componentCron);
    conf.refreshRate = $scope.componentCron? $scope.componentCron: '';
    //save the object;
    console.log('conf', conf);
    return false;
  }

  $scope.forceRefresh = function() {
    return false;
  }

  $scope.isInMasterList = function(value) {
    return !!_.find($scope.masterSelected, {'value': value});
  }


  $scope.masterSelect = [];
  $scope.moveLeft = function(code) {
    if(!code.selected){
      code.selected = [];
    }
    var right = $scope.masterSelect;
    for (var i = 0; i < right.length; i++) {
      var el = right[i];
      if (code.selected.indexOf(el) < 0) {
        code.selected.push(el);
      }
      var indexOf = $scope.masterSelected.indexOf(el);
      $scope.masterSelected.splice(indexOf, 1);
    }
  };

  $scope.moveRight = function(code) {
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
    $scope.storeField = false;
    $scope.storeFields = null;
    $scope.jiraField = false;
    $scope.fields = [];
    $scope.storeCodes = [];
  })

  $scope.$watchCollection('[storeField, jiraField]', function(newValues, oldValues, scope){
    if ((newValues[0] && typeof newValues[0] === 'object') && (newValues[1] && typeof newValues[1] === 'object')) {
      // check to see if it previously existed. Show warning if it did.
      $scope.masterSelected = angular.copy(newValues[1].allowedValues);
      $scope.loading++;
      $scope.getStoreCodes(newValues[0]);
    }
  });

  $scope.$watch('loading', function(value){
    if (value > 0){
      $scope.$emit('$TRIGGERLOAD', 'JiraConfigLoad');
    } else {
      $scope.$emit('$TRIGGERUNLOAD', 'JiraConfigLoad');
    }
  })  

}]);
