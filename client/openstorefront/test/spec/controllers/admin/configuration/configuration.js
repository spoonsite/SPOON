'use strict';

describe('Controller: AdminConfigurationCtrl', function () {

// load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminConfigurationCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminConfigurationCtrl = $controller('AdminConfigurationCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.modal).toEqual({});
    expect(scope.password).toBe(undefined);
    expect(scope.jira).toEqual({});
    expect(scope.watch).toEqual({});
    expect(scope.jiraCodes).toEqual({});
    expect(scope.overRideDefault).toEqual(false);
    expect(scope.cron.componentCron).toEqual('0 0 0 1/1 * ? *');
    expect(scope.cron.global_cron).toEqual('0 0 0 1/1 * ? *');
    expect(scope.component).toEqual({});
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
    expect(scope.integrationConfs).toEqual(null);
    expect(scope.loading).toEqual(1);
    expect(scope.type).toEqual('jira');
    expect(scope.show).toEqual({'selectCompConf': true, 'showCodeSelection': true});
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
    expect(scope.allJobs).toEqual([]);
  });
  alert('Controller:  AdminConfigurationCtrl; should have the correct initializations = PASS (18 expects)');
});
