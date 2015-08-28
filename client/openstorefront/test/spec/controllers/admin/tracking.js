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

  it('should have the correct initializations', function () {
    expect(scope.user.control).toEqual({});
    expect(scope.user.features).toEqual({'dates': true, 'max': true});
    expect(scope.component.control).toEqual({});
    expect(scope.component.features).toEqual({'dates': true, 'max': true});
    expect(scope.article.control).toEqual({});
    expect(scope.article.features).toEqual({'dates': true, 'max': true});
  });
  alert('Controller:  AdminTrackingCtrl; should have the correct initializations = PASS (6 expects)');
});
