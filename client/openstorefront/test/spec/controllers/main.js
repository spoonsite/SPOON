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

  //  spyOn(scope, 'pageTitle').and.callThrough();
  //  expect(scope.pageTitle).toHaveBeenCalled();
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

});
