 'use strict';

 describe('Controller: LandingCtrl', function () {

   // load the controller's module
   beforeEach(module('openstorefrontApp'));

   var LandingCtrl,
     scope;

   // Initialize the controller and a mock scope
   beforeEach(inject(function ($controller, $rootScope) {
     scope = $rootScope.$new();
     LandingCtrl = $controller('LandingCtrl', {
       $scope: scope
     });
   }));

   it('should have the correct initializations', function () {
     expect(scope.data).toEqual([]);
     expect(scope.landingRoute).toEqual(null);
   });
 });
