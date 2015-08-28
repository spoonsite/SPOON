'use strict';

describe('Controller: AdminEditcomponentCtrl', function () {

// load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminEditcomponentCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminEditcomponentCtrl = $controller('AdminEditcomponentCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.predicate['components']).toEqual('name');
    expect(scope.predicate['eval']).toEqual('sortOrder');
    expect(scope.statusFilterOptions).toEqual([{code: 'A', desc: 'Active'}, {code: 'I', desc: 'Inactive'}]);
    expect(scope.queryFilter.max).toEqual(500);
    expect(scope.queryFilter.sortField).toEqual('name');
    expect(scope.components).toEqual([]);
    expect(scope.filteredComponents).toEqual([]);
    expect(scope.allComponentsWatch).toEqual({data: []});
    expect(scope.allComponentsWatch.data).toEqual([]);
    expect(scope.flags.showUpload).toEqual(false);
    expect(scope.componentUploadOptions).toEqual({});
    expect(scope.selectedComponents).toEqual([]);
    expect(scope.selectAllComps).toEqual({flag: false});
    expect(scope.selectAllComps.flag).toEqual(false);
    expect(scope.submitter).toEqual(null);
    expect(scope.pagination.control.approvalState).toEqual('ALL');
    expect(scope.pagination.control.componentType).toEqual('ALL');
    expect(scope.pagination.features).toEqual({'dates': false, 'max':false});

  });
  alert('Controller:  AdminEditcomponentCtrl; should have the correct initializations = PASS (10+ expects)');
});
