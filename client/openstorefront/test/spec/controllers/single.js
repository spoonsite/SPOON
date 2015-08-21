'use strict';

describe('Controller: SingleCtrl', function () {

 // load the controller's module
 beforeEach(module('openstorefrontApp'));

 var SingleCtrl,
   scope;

 // Initialize the controller and a mock scope
 beforeEach(inject(function ($controller, $rootScope) {
   scope = $rootScope.$new();
   SingleCtrl = $controller('SingleCtrl', {
     $scope: scope
   });
 }));

 it('should have the correct initializations', function () {
   expect(scope.data).toEqual({});
   expect(scope.details).toEqual({details: null});
   expect(scope.modal).toEqual({isLanding: false});
   expect(scope.single).toEqual(true);
   expect(scope.details.details).toEqual(null);
   expect(scope.modal.isLanding).toEqual(false);
   expect(scope.showDetails).toEqual(false);
   expect(scope.showBreadCrumbs).toEqual(true);
 });

  it('should validate properly using spies', function() {
    spyOn(scope, 'updateDetails');
    scope.updateDetails(-1);
    expect(scope.updateDetails).toHaveBeenCalledWith(-1);
  });

});
