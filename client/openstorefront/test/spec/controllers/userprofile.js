'use strict';

describe('Controller: UserProfileCtrl', function () {

  // // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var UserProfileCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    UserProfileCtrl = $controller('UserProfileCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope._scopename).toEqual('userprofile');
    expect(scope.pageTitle).toEqual('DI2E Clearinghouse');
    expect(scope.defaultTitle).toEqual('Browse Categories');
    expect(scope.untilDate.toString()).toEqual(new Date().toString());
    expect(scope.review).toEqual(null);
 });
});
