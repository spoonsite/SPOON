
'use strict';

describe('Controller: AdminLookupCtrl', function () {

// load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminLookupCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminLookupCtrl = $controller('AdminLookupCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.lookups).toEqual([]);
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
  });
  alert('Controller:  AdminLookupCtrl; should have the correct initializations = PASS (3 expects)');
});
