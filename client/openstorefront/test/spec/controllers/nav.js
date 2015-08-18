 'use strict';

 describe('Controller: NavCtrl', function () {

   // load the controller's module
   beforeEach(module('openstorefrontApp'));

   var NavCtrl,
     scope;

   // Initialize the controller and a mock scope
   beforeEach(inject(function ($controller, $rootScope) {
     scope = $rootScope.$new();
     $rootScope.searchKey = 'test'
     NavCtrl = $controller('NavCtrl', {
       $scope: scope
     });
   }));

   it('should attach a list of awesomeThings to the scope', function () {
     expect(scope._scopename).toEqual('nav');
     expect(scope.navLocation).toEqual('views/nav/nav.html');
     expect(scope.searchKey).toEqual('test');
     expect(scope.user).toEqual({});
     expect(scope.beforeLogin).toEqual(null);
     expect(scope.typeahead).toEqual(null);
    });
 });
