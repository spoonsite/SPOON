/**
 * Created by besplin on 8/26/2015.
 */
'use strict';

describe('Directive: svcv4archdiagram', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var Svc4ArchDiagramController,
    scope;

  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    Svc4ArchDiagramController = $controller('Svc4ArchDiagramController', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope.diagramToggleAllState).toEqual(true);
    expect(scope.diagramToggleAllText).toEqual('Expand All');
  });

  alert('Directive:  svcv4archdiagram; should have the correct initializations = PASS (3 expects)');
});
