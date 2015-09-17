'use strict';

describe('Controller: DetailsReviewCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var DetailsReviewCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DetailsReviewCtrl = $controller('DetailsReviewCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.review).toEqual({});
    expect(scope.user).toEqual({});
    expect(scope.backup).toEqual({});
    expect(scope.untilDate.toString()).toEqual(new Date().toString());
  });
  alert('Controller:  DetailsReviewCtrl; should have the correct initializations = PASS (4 expects)');
});
