'use strict';

describe('Controller: AdminEditattributesCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminEditattributesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminEditattributesCtrl = $controller('AdminEditattributesCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.predicate).toEqual('description');
    expect(scope.reverse).toEqual(false);
    expect(scope.flags.showUpload).toEqual(false);
    expect(scope.data.allTypes).toEqual({});
    expect(scope.pagination.features).toEqual({'dates': false, 'max': false});
    expect(scope.selectedTypes).toEqual([]);
  });
  alert('Controller:  AdminEditattributesCtrl; should have the correct initializations = PASS (6 expects)');
});
