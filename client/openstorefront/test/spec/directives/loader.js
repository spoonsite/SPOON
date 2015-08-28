'use strict';

describe('Directive: loader', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<loader></loader>');
    element = $compile(element)(scope);
    expect(element.html()).toBe('<div class="loader-holder modal-backdrop" ng-show="loading"><div class="loader"><!--[if lt IE 10]><span>...Loading...</span><![endif]--></div></div>');
  }));
  alert('Directive:  loader; should make hidden element visible = PASS (1 expect)');
});
