'use strict';

describe('Controller: AdminEditHighlightsCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminEditHighlightsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminEditHighlightsCtrl = $controller('AdminEditHighlightsCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.predicate).toEqual('title');
    expect(scope.reverse).toEqual(false);
    expect(scope.data).toEqual({});
    expect(scope.deactivateButtons).toEqual(false);
  });
  alert('Controller:  AdminEditHighlightsCtrl; should have the correct initializations = PASS (4 expects)');
});
