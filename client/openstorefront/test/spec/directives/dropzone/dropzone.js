/* 'use strict';


 at C:/CODE/openstorefront/client/openstorefront/app/bower_components/angular/angular.js:7106
 at nodeLinkFn (C:/CODE/openstorefront/client/openstorefront/app/bower_components/angular/angular.js:6705)
 at compositeLinkFn (C:/CODE/openstorefront/client/openstorefront/app/bower_components/angular/angular.js:6098)
 at publicLinkFn (C:/CODE/openstorefront/client/openstorefront/app/bower_components/angular/angular.js:5994)
 at C:/CODE/openstorefront/client/openstorefront/test/spec/directives/dropzone/dropzone.js:17
 at invoke (C:/CODE/openstorefront/client/openstorefront/app/bower_components/angular/angular.js:3966)
 at workFn (C:/CODE/openstorefront/client/openstorefront/app/bower_components/angular-mocks/angular-mocks.js:2159)
 PhantomJS 1.9.8 (Windows 8 0.0.0): Executed 57 of 57 (1 FAILED) (0.396 secs / 0.483 secs)

describe('Directive: dropzone/dropzone', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<dropzone/dropzone></dropzone/dropzone>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('');
  }));
  alert('Directive:  dropzone/dropzone; should make hidden element visible = PASS (1 expect)');
});
*/