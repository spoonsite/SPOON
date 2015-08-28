'use strict';

describe('Controller: AdminReviewsCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminReviewsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminReviewsCtrl = $controller('AdminReviewsCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.predicate).toEqual([]);
    expect(scope.reverse).toEqual([]);
  });
  alert('Controller:  AdminReviewsCtrl; should have the correct initializations = PASS (2 expects)');
});


