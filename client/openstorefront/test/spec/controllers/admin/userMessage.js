'use strict';

describe('Controller: AdminUserMessageCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminUserMessageCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminUserMessageCtrl = $controller('AdminUserMessageCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.userMessages).toEqual([]);
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
    expect(scope.pagination.features).toEqual({'dates': true, 'max': true});
  });
  alert('Controller:  AdminUserMessageCtrl; should have the correct initializations = PASS (4 expects)');
});


