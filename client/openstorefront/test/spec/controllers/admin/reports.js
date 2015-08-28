'use strict';

describe('Controller: AdminReportCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminReportCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminReportCtrl = $controller('AdminReportCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
  });
  alert('Controller:  AdminReportCtrl; should have the correct initializations = PASS (2 expects)');
});


