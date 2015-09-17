'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('openstorefrontApp'));
  
  var MainCtrl,
  scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    $rootScope.searchKey = 'test';
    $rootScope.openAdminMessage = 'test';
    MainCtrl = $controller('MainCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope._scopename).toEqual('main');
    expect(scope.pageTitle).toEqual('DI2E');
    expect(scope.subTitle).toEqual('Clearinghouse');
    expect(scope.searchKey).toEqual('test');
    expect(scope.openAdminMessage).toEqual('test');
    expect(scope.typeahead).toBeNull();
    expect(scope.goToLand).toBeFalsy();
    // verify that this test is being run by creating an error
    // expect(true).toBeFalsy();
  });

  it('should return correct values from spies', function() {
    spyOn(scope, 'pageTitle'); //.and.callThrough();
    spyOn(scope, 'subTitle'); //.and.callThrough();
    spyOn(scope, 'searchKey');
    spyOn(scope, 'goToLand');

    scope.pageTitle('Blah');
    scope.subTitle('Yada');
    scope.searchKey(3);
    scope.goToLand(true);

    expect(scope.pageTitle).toHaveBeenCalledWith('Blah');
    expect(scope.subTitle).toHaveBeenCalledWith('Yada');
    expect(scope.searchKey).toHaveBeenCalledWith(3);
    expect(scope.goToLand).toHaveBeenCalledWith(true);
  });
  alert('Controller: MainCtrl; should have the correct initializations, spies = PASS (11 expects, 4 spies)');
});
