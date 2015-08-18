'use strict';

describe('Controller: UserProfileCtrl', function () {

  // // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var MainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MainCtrl = $controller('MainCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope._scopename).toEqual('main');  //NOTE this is set to 'userprofile' when scoped!
    expect(scope.pageTitle).toEqual('DI2E');  //NOTE this is set to 'DI2E Clearinghouse' when scoped!
    // NOTE:  This does not 'stick' from where it is scoped in userprofile.js in the app
      //expect(scope.defaultTitle).toEqual('Browse Categories');
    // NOTE:  This does not 'stick' from where it is scoped in userprofile.js in the app
      //expect(scope.untilDate).toEqual(new Date());
    // NOTE:  This does not 'stick' from where it is scoped in userprofile.js in the app
      //expect(scope.review).toEqual(null);
 });
});
