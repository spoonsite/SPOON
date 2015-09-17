'use strict';

describe('Controller: AdminManageOrganizationsCtrl', function () {

// load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminManageOrganizationsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminManageOrganizationsCtrl = $controller('AdminManageOrganizationsCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.queryFilter.max).toEqual(500);
    expect(scope.queryFilter.sortField).toEqual('name');
    expect(scope.data.organizations).toEqual({});
    expect(scope.filteredOrganizations).toEqual([]);
    expect(scope.flags.showUpload).toEqual(false);
    expect(scope.organizationUploadOptions).toEqual({});
    expect(scope.selectAllComps.flag).toEqual(false);
    expect(scope.submitter).toEqual(null);
    expect(scope.pagination.control.approvalState).toEqual('ALL');
    expect(scope.pagination.control.organizationType).toEqual('ALL');
    expect(scope.pagination.features).toEqual({'dates': false, 'max': false, 'activeStatus': false});
  });
  alert('Controller:  AdminManageOrganizationsCtrl; should have the correct initializations = PASS (11 expects)');
});
