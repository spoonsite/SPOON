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
  $scope.typeahead;
  $scope.overRideDefault = false;
  $scope.componentCron = '0 0 * * *';
  $scope.types = [
  {
    'label': 'Jira Configuration',
    'code': 'jira'
  }
  ]
  $scope.type = 'jira';


  $scope.getXRefTypes = function(){
    Business.configurationservice.getXRefTypes().then(function(result){
      console.log('result', result);
      if (result.length > 0) {
        $scope.xRefTypes = result;
        $scope.selectedMapping = result[0];
      }
    });
  }
  
  $scope.getXRefTypes();


  $scope.setupModal = function(id) {
    if (id) {
      Business.saveLocal('configId', id)
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
    var conf = {};
    conf.componentId = $scope.componentId;
    conf.issueId = $scope.issueId;
    console.log('$scope.componentCron', $scope.componentCron);
    conf.refreshRate = $scope.componentCron? $scope.componentCron: '';
    //save the object;
    console.log('conf', conf);
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

}]);
