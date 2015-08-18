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

// besplin, 18 Aug 2015
 it('should have the correct initializations', function () {
   expect(scope.collection).toEqual(null);
   expect(scope.collectionSelection).toEqual(null);
   expect(scope.incLoc).toEqual('');
   expect(scope.saveContent).toEqual('');
   expect(scope.editedTopic).toEqual('Types');
   expect(scope.toolTitle).toEqual('Admin Tools');
   expect(scope.menuPanel.data.open).toEqual(true);
   expect(scope.menuPanel.system.open).toEqual(true);
   expect(scope.oneAtATime).toEqual(false);

  /* expect(scope.myTree).toEqual(array);
     expect(scope.systemTree).toEqual(array);
     expect(scope.data).toEqual(array);
     expect(scope.systemTools).toEqual(array);
     expect(scope.menuPanel).toEqual(array);
     expect(scope.menuPanel.data).toEqual(array);
     expect(scope.menuPanel.system).toEqual(array);
  */

 });
});
