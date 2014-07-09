'use strict';

app.controller('AdminEditattributesCtrl',['$scope','business',  function ($scope, Business) {
  $scope.filters = Business.getFilters();

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
      {field: 'name', displayName: 'Name', enableCellEdit: true},
      {field:'key', displayName:'Code', maxWidth: 150, enableCellEdit: true},
      {field:'src', displayName:'Icon Src', enableCellEdit: true},
      {field:'key', displayName:'Collection', maxWidth: 150, cellTemplate: '<div class="ngCellText" ng-click="editCollection(row.getProperty(col.field))"><a>Edit Code Collection</a></div>', enableCellEdit: false, groupable: false, sortable: false}
    //
    ]
  };

  $scope.$watch('filters', function() {
    // This is where we know something changed on the model for the collection that
    // The user was editing. (Useful for inline editing, possibly useful for modal editing as well.)
    // console.log('Checks', $scope.collectionContent[0].longDesc);
  }, true);
}]);
