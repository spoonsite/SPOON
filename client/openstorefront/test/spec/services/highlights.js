'use strict';

describe('Service: highlights', function () {

  // load the service's module
  beforeEach(module('openstorefrontApp'));

  // instantiate service
  var highlights;
  beforeEach(inject(function (_highlights_) {
    highlights = _highlights_;
  }));

  it('should do something', function () {
    expect(!!highlights).toBe(true);
  });

});
