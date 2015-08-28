'use strict';

describe('Controller: AdminUserProfileCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminUserProfileCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminUserProfileCtrl = $controller('AdminUserProfileCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.predicate).toEqual('activeStatus');
    expect(scope.deactivateButtons).toEqual(false);
    expect(scope.userProfiles).toEqual({});
  });
  alert('Controller:  AdminUserProfileCtrl; should have the correct initializations = PASS (3 expects)');
});

