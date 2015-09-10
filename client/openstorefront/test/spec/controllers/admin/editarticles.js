'use strict';

describe('Controller: adminEditArticlesCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var adminEditArticlesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    adminEditArticlesCtrl = $controller('adminEditArticlesCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.predicate).toEqual('title');
    expect(scope.reverse).toEqual(false);
    expect(scope.selectedTypes).toEqual([]);
  });
  alert('Controller:  adminEditArticlesCtrl; should have the correct initializations = PASS (3 expects)');
});
