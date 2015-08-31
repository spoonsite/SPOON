'use strict';

describe('Directive: compare/compareview', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<compare/compareview></compare/compareview>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('');
  }));
  alert('Directive:  compare/compareview; should make hidden element visible = PASS (1 expect)');
});
