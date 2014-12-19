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

app.controller('AdminEditcodesCtrl', ['$scope', '$uiModalInstance', 'type', 'business', function ($scope, $uiModalInstance, type, Business) {
  
  console.log('type', type);
  $scope.type = type;

  $scope.ok = function () {
    $uiModalInstance.close('success');
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
  // // Here we are grabbing the different collection key's and name's to put in the 'editcodes' tool.
  // $scope.collection = [];
  // $scope.collectionContent = null;

  // $scope.gridOptions = {
  //   data: 'collectionContent',
  //   enableCellSelection: true,
  //   enableRowSelection: true,
  //   enableCellEdit: true,
  //   multiSelect: true,
  //   selectWithCheckboxOnly: true,
  //   showSelectionCheckbox: true,
  //   // showGroupPanel: true,
  //   columnDefs: [
  //     // the number of stars determines width just like column span works on tables.
  //     {field: 'label', displayName: 'Name', width: '***', resizable: false, enableCellEdit: true},
  //     {field: 'code', displayName: 'Code', width: '**', resizable: false, enableCellEdit: true},
  //     {field: 'description', displayName: 'Description', width: '***', enableCellEdit: true},
  //     // {field: 'checked', displayName: 'Apply Filter', width: '**', resizable: false, enableCellEdit: false, cellTemplate: '<div class="ngSelectionCell "><input class="" type="checkbox" ng-checked="row.getProperty(col.field)" ng-model="COL_FIELD"/></div>'},
  //     // {field: 'landing', displayName: 'Landing Page', width: '***', resizable: false, cellTemplate: '<div ng-if="row.getProperty(col.field)" class="imitateLink ngCellText " ng-click="editLanding(row.getProperty(col.field))"><a>Edit Landing Page</a></div> <div ng-if="!row.getProperty(col.field)" class="imitateLink ngCellText " ng-click="editLanding(\'\')"><a>Add Landing Page</a></div>', enableCellEdit: false, groupable: false, sortable: false}
  //   //
  //   ]
  // };
  
  
  // Business.getFilters().then(function(result) {
  //   if (result) {
  //     $scope.filters = angular.copy(result);
  //     _.each($scope.filters, function(filter) {
  //       $scope.collection.push({'name': filter.description, 'key': filter.type});
  //     });
  //   } else {
  //     $scope.filters = null;
  //   }
  // });

  // /***************************************************************
  // * This function will grab a collection from a filter given the key
  // * of the filter.
  // ***************************************************************/
  // $scope.grabCollection = function(key) {
  //   var filter = _.where($scope.filters, {'type': key})[0];
  //   $scope.collectionContent = filter.codes;
  // };

  // if ($scope.$parent.collectionSelection) {
  //   $scope.collectionSelection = $scope.$parent.collectionSelection;
  //   $scope.grabCollection($scope.collectionSelection.type);
  // }
  // // $scope.$watch('collectionContent', function() {
  // //   // This is where we know something changed on the model for the collection that
  // //   // The user was editing. (Useful for inline editing, possibly useful for modal editing as well.)
  // //   // console.log('Checks', $scope.collectionContent[0].longDesc);
  // // }, true);

}]);
