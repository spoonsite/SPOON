'use strict';

app.controller('AdminEditattributesCtrl',['$scope','business',  function ($scope, Business) {
  $scope.filters = Business.getFilters();

  $scope.gridOptions = {
      data: 'filters',
      enableCellSelection: true,
      enableRowSelection: true,
      enableCellEdit: true,
      multiSelect: true,
      columnDefs: [
        //
        {field: 'name', displayName: 'Name', enableCellEdit: true},
        {field:'key', displayName:'Key', enableCellEdit: true},
        {field:'src', displayName:'Icon Src', enableCellEdit: true},
        {field:'key', displayName:'Collection', cellTemplate: '<div class="ngCellText" ng-click="editCollection(row.getProperty(col.field))"><a>Edit Code Collection</a></div>', enableCellEdit: false, groupable: false, sortable: false}
      //
      ],
      showGroupPanel: true
    };
}]);
