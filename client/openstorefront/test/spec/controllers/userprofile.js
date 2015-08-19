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
    expect(scope._scopename).toEqual('userprofile');  //NOTE this is set to 'userprofile' when scoped!
    expect(scope.pageTitle).toEqual('DI2E Clearinghouse');  //NOTE this is set to 'DI2E Clearinghouse' when scoped!
    expect(scope.defaultTitle).toEqual('Browse Categories');

    var coeff = 1000;  // round the milliseconds
    var thenRounded = new Date(Math.round(scope.untilDate / coeff) * coeff);
    var nowRounded = new Date(Math.round(new Date() / coeff) * coeff);
    expect(thenRounded).toEqual(nowRounded);

    expect(scope.review).toEqual(null);
 });
});
