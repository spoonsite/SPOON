 'use strict';

 describe('Controller: CompareCtrl', function () {

 //   load the controller's module
   beforeEach(module('openstorefrontApp'));

   var CompareCtrl,
     scope;

 //   Initialize the controller and a mock scope
   beforeEach(inject(function ($controller, $rootScope) {
     scope = $rootScope.$new();
     CompareCtrl = $controller('CompareCtrl', {
       $scope: scope
     });
   }));

   it('should have the correct initializations', function () {
     expect(scope.showChoices).toEqual(false);
     expect(scope.pair).toEqual([]);
     expect(scope.data).toEqual(null);
     expect(scope.id).toEqual(null);
     expect(scope.article).toEqual(null);
   });
   it('should return correct values from spies', function () {
      spyOn(scope, 'resetSide');
      spyOn(scope, 'setCompare');
      
      scope.resetSide(false);
      scope.setCompare(null, null);
      
      expect(scope.resetSide).toHaveBeenCalledWith(false);
      expect(scope.setCompare).toHaveBeenCalledWith(null, null);
   });
 });
