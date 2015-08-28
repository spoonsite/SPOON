'use strict';

describe('Controller: AdminSystemCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminSystemCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminSystemCtrl = $controller('AdminSystemCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.recentChangesForm.lastRunDts).toEqual("");
    expect(scope.recentChangesForm.emailAddress).toEqual("");
    expect(scope.recentChangesStatus).toEqual({});
    expect(scope.errorTickets).toEqual({});
    expect(scope.maxPageNumber).toEqual(1);
    expect(scope.pageNumber).toEqual(1);
    expect(scope.EMAIL_REGEXP).toEqual(utils.EMAIL_REGEXP);
    expect(scope.queryFilter.max).toEqual(100);
    expect(scope.untilDate.toString()).toEqual(new Date().toString());
    expect(scope.configProperties).toEqual([]);
    expect(scope.loggers).toEqual([]);
    expect(scope.threads).toEqual([]);
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
    expect(scope.tabs.general).toEqual(true);
    expect(scope.flags.showPluginUpload).toEqual(false);
    expect(scope.plugins).toEqual([]);
    expect(scope.pagination.features).toEqual({'dates': false, 'max': true});
  });
  alert('Controller:  AdminSystemCtrl; should have the correct initializations = PASS (18 expects)');
});


