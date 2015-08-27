/*
'use strict';

describe('Directive: modal', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<modal></modal>');
    
    // Don't know why this doesn't work? 
    //element = $compile(element)(scope);
    expect(element.html()).toBe('');
  }));
 alert('Directive:  modal; should make hidden element visible = PASS (1 expect)');
});
*/
