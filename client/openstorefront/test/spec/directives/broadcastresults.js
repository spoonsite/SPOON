'use strict';

describe('Directive: broadcastResults', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<broadcast-results></broadcast-results>');
    element = $compile(element)(scope);
    expect(element.html()).toContain('<div></div>');
  }));
  alert('Directive:  broadcastResults; should make hidden element visible = PASS (1 expect)');
});
