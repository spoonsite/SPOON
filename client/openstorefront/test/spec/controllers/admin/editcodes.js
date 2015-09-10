/*
'use strict';

describe('Controller: AdminEditcodesCtrl', function () {
  // load the controller's module
  beforeEach(module('openstorefrontApp'));

  var AdminEditcodesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminEditcodesCtrl = $controller('AdminEditcodesCtrl', {
      //   Error: [$injector:unpr] Unknown provider: $uiModalInstanceProvider <- $uiModalInstance
      //    at C:/CODE/openstorefront/client/openstorefront/test/spec/controllers/admin/editcodes.js:14
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.triggerShow).toEqual(0);
    expect(scope.predicate).toEqual('code');
    expect(scope.reverse).toEqual(false);
    expect(scope.current).toEqual(0);
    expect(scope.dirty).toEqual(false);
    expect(scope.addTypeFlg).toEqual(false);
    expect(scope.changed).toEqual(false);

    expect(scope.data).toEqual({});
    expect(scope.data.allCodes).toEqual({});
    expect(scope.pagination.control).toEqual({});
    expect(scope.pagination.features).toEqual({'dates': false, 'max': false});
    expect(scope.typeCodes).toEqual([]);
    expect(scope.defaultCodesLookup).toEqual([]);
  });
  alert('Controller:  AdminEditCodesCtrl; should have the correct initializations = PASS (13 expects)');
});
*/

