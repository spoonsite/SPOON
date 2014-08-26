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

app.controller('AdminEditattributesCtrl',['$scope','business',  function ($scope, Business) {
  Business.getFilters().then(function(result){
    if (result) {
      $scope.filters = angular.copy(result);
    } else {
      $scope.filters = null;
    }
  });

  $scope.gridOptions = {
    data: 'filters',
    enableCellSelection: true,
    enableRowSelection: true,
    enableCellEdit: true,
    multiSelect: true,
    selectWithCheckboxOnly: true,
    showSelectionCheckbox: true,
    // showGroupPanel: true,
    columnDefs: [
      //
      {field: 'description', displayName: 'Name', enableCellEdit: true},
      {field: 'type', displayName: 'Code', maxWidth: 150, enableCellEdit: true},
      {field: 'visibleFlg', displayName: 'Visible Flag', enableCellEdit: true},
      {field: 'requiredFlg', displayName: 'Required Flag', enableCellEdit: true},
      {field: 'archtechtureFlg', displayName: 'Architecture Flag', enableCellEdit: true},
      {field: 'importantFlg', displayName: 'Important Flag', enableCellEdit: true},
      {field: 'allowMutlipleFlg', displayName: 'Allow Multiple Flag', enableCellEdit: true},
      {field: 'type', displayName: 'Codes', maxWidth: 150, cellTemplate: '<div class="ngCellText" ng-click="editCollection(row.getProperty(col.field))"><a>Edit Code Collection</a></div>', enableCellEdit: false, groupable: false, sortable: false}
    //
    ]
  };

  // $scope.$watch('filters', function() {
  //   // This is where we know something changed on the model for the collection that
  //   // The user was editing. (Useful for inline editing, possibly useful for modal editing as well.)
  //   // console.log('Checks', $scope.collectionContent[0].longDesc);
  // }, true);
}]);
