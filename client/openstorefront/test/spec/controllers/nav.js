 'use strict';

 describe('Controller: NavCtrl', function () {

// load the controller's module
   beforeEach(module('openstorefrontApp'));

   var NavCtrl,
     scope;

   // Initialize the controller and a mock scope
   beforeEach(inject(function ($controller, $rootScope) {
     scope = $rootScope.$new();
     $rootScope.searchKey = 'test';
     NavCtrl = $controller('NavCtrl', {
       $scope: scope
     });
   }));

   it('should have the correct initializations', function () {
     expect(scope._scopename).toEqual('nav');
     expect(scope.navLocation).toEqual('views/nav/nav.html');
     expect(scope.searchKey).toEqual('test');
     expect(scope.user).toEqual({});
     expect(scope.beforeLogin).toEqual(null);
     expect(scope.typeahead).toEqual(null);
    });

   it('should return correct values from spies', function() {
     spyOn(scope, 'getTypeahead');
     spyOn(scope, 'goToSearch');
     spyOn(scope, 'goToLogin');
     spyOn(scope, 'sendHome');
     spyOn(scope, 'logout');
     spyOn(scope, 'openHelp');

     scope.getTypeahead();
     scope.goToSearch();
     scope.goToLogin();
     scope.sendHome();
     scope.logout();
     scope.openHelp();

     expect(scope.getTypeahead).toHaveBeenCalled();
     expect(scope.goToSearch).toHaveBeenCalled();
     expect(scope.goToLogin).toHaveBeenCalled();
     expect(scope.sendHome).toHaveBeenCalled();
     expect(scope.logout).toHaveBeenCalled();
     expect(scope.openHelp).toHaveBeenCalled();
   });
   alert('Controller: NavCtrl; should have the correct initializations, spies = PASS (12 expects, 6 spies)');
 });
