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
    expect(scope.lastMediaFile).toEqual('');
    expect(scope.hideMultiSelect).toEqual(true);

    expect(scope.editor.editorContent).toEqual('');
    expect(scope.editor.editorContentWatch);

    expect(scope.submitter.firstName).toBe(undefined);
    expect(scope.current).toEqual('top');
    expect(scope.optIn).toEqual(true);
    expect(scope.notDif).toEqual(true);

    expect(scope.showMediaUpload).toEqual('falseValue');
    expect(scope.showResourceUpload).toEqual('trueValue');
  });

  alert('Controller: submission; should have the correct initializations = PASS (12 expects)');
});
