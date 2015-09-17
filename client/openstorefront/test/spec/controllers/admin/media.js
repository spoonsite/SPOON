'use strict';

describe('Controller: AdminMediaCtrl', function () {

// load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminMediaCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminMediaCtrl = $controller('AdminMediaCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
  });
  alert('Controller:  AdminMediaCtrl; should have the correct initializations = PASS (2 expects)');
});
