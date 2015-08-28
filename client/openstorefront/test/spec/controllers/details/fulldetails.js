'use strict';

describe('Controller: DetailsFulldetailsCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var DetailsFulldetailsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DetailsFulldetailsCtrl = $controller('DetailsFulldetailsCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.user).toEqual({});
    });
  alert('Controller:  DetailsFulldetailsCtrl; should have the correct initializations = PASS (1 expect)');
});
