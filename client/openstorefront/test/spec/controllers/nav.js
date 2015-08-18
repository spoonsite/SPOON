 'use strict';

 describe('Controller: NavCtrl', function () {

// load the controller's module
   beforeEach(module('openstorefrontApp'));

   var NavCtrl,
     scope;

// Initialize the controller and a mock scope
   beforeEach(inject(function ($controller, $rootScope) {
     scope = $rootScope.$new();
     NavCtrl = $controller('NavCtrl', {
       $scope: scope
     });
   }));

   it('should have the correct initializations', function () {
     expect(scope._scopename).toEqual('nav');
     expect(scope.navLocation).toEqual('views/nav/nav.html');
     expect(scope.beforeLogin).toEqual(null);
     expect(scope.typeahead).toEqual(null);
   });
 });
