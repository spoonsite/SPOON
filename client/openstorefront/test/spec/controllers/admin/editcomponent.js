'use strict';

describe('Controller: AdminEditcomponentCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminEditcomponentCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminEditcomponentCtrl = $controller('AdminEditcomponentCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
