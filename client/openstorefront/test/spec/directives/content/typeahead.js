/* 'use strict';

//   TypeError: 'undefined' is not a function (evaluating 'scope.toBe(undefined)')



describe('Directive: content/typeahead', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<content/smartselect></content/smartselect>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('');
  }));
  alert('Directive:  content/typeahead; should make hidden element visible = PASS (1 expect)');
});
*/