'use strict';

describe('Controller: AdminAlertCtrl', function () {

// load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminAlertCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminAlertCtrl = $controller('AdminAlertCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.alerts).toEqual([]);
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
     });
  alert('Controller:  AdminAlertCtrl; should have the correct initializations = PASS (3 expects)');
});
