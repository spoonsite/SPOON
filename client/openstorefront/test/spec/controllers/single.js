'use strict';

describe('Controller: SingleCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var SingleCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SingleCtrl = $controller('SingleCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
