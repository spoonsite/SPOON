'use strict';

describe('Directive: services/socket', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<services/socket></services/socket>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('Atmosphere Chat. Default transport is WebSocket, fallback is long-polling Detecting ' +
      'what the browser and server are supporting   Connecting...  ');
    //expect(element.text()).toBe('this is the services/socket directive');
  }));
});
