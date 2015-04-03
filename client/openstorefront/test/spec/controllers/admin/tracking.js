'use strict';

describe('Controller: AdminTrackingCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminTrackingCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminTrackingCtrl = $controller('AdminTrackingCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
