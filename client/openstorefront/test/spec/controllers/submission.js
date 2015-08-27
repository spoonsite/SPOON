'use strict';

describe('Controller: submission', function () {

  //   load the controller's module
  beforeEach(module('openstorefrontApp'));

  var SubmissionCtrl,
    scope;

  //   Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SubmissionCtrl = $controller('SubmissionCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.test).toEqual('This is a test');
    expect(scope.badgeFound).toEqual(false);
    expect(scrope.lastMediaFile).toEqual('');
    expect(scope.hideMultiSlect).toEqual('true');

  });
});
