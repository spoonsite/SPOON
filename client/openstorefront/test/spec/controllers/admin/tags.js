'use strict';

describe('Controller: AdminTagsCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminTagsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminTagsCtrl = $controller('AdminTagsCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.reverse).toEqual([]);
    expect(scope.tagForm).toEqual({});
    expect(scope.predicate['tag']).toEqual('componentName');
  });
  alert('Controller:  AdminTagsCtrl; should have the correct initializations = PASS (3 expects)');
});

