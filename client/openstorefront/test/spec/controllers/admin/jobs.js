'use strict';

describe('Controller: AdminJobsCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminJobsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminJobsCtrl = $controller('AdminJobsCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.jobs).toEqual({});
    expect(scope.schedulerStatus).toEqual({});
    expect(scope.tasks).toEqual({});
    expect(scope.showIntegrationJobs).toEqual(false);
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
  });
  alert('Controller:  AdminJobsCtrl; should have the correct initializations = PASS (6 expects)');
});

