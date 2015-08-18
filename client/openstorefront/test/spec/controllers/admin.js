 'use strict';

 describe('Controller: AdminCtrl', function () {

 // load the controller's module
 beforeEach(module('openstorefrontApp'));

 var AdminCtrl, scope;

 // Initialize the controller and a mock scope
 beforeEach(inject(function ($controller, $rootScope) {
   scope = $rootScope.$new();
   AdminCtrl = $controller('AdminCtrl', {
     $scope: scope
   });
 }));

 it('should have the correct initializations', function () {
   expect(scope.toolTitle).toEqual('Admin Tools');
   expect(scope.collection).toEqual(null);

 });
});
