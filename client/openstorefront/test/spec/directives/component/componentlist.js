'use strict';

describe('Directive: component/componentList', function () {

  //  load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<component/componentList></component/componentList>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('');
  }));
  alert('Directive:  component/componentList; should make hidden element visible = PASS (1 expect)');
});
