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
   expect(scope.collection).toEqual(null);
   expect(scope.collectionSelection).toEqual(null);
   expect(scope.incLoc).toEqual('');
   expect(scope.saveContent).toEqual('');
   expect(scope.editedTopic).toEqual('Types');
   expect(scope.toolTitle).toEqual('Admin Tools');
   expect(scope.menuPanel.data.open).toEqual(true);
   expect(scope.menuPanel.system.open).toEqual(true);
   expect(scope.oneAtATime).toEqual(false);
   expect(scope.myTree).toEqual({});
   expect(scope.systemTree).toEqual({});
   /*expect(scope.data).toEqual({'label': 'Articles',
     'location':'views/admin/editlanding.html',
     'toolTitle': 'Manage Articles',
     'detailedDesc': "Articles, also called topic landing pages, are detail pages of topics of interest with optional related listings.  Articles are assigned to an Attribute Code which allows for searching and filter by topic. ",
     'key': 'landing'});
   expect(scope.systemTools).toEqual([]);
   expect(scope.menuPanel).toEqual({});
   expect(scope.menuPanel.data).toEqual({});
   expect(scope.menuPanel.system).toEqual({});
   */
 });
});
