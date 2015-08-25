'use strict';

describe('Filter: partition', function () {
  // load the filter's module
  beforeEach(module('openstorefrontApp'));

  // initialize a new instance of the filter before each test
  var partition;
  beforeEach(inject(function ($filter) {
    partition = $filter('partition');
  }));

  it('should return the input prefixed with "partition filter:"', function () {
    var myArray = ["hello", "world", "testing", 1, 2, 3];
    expect(partition(myArray, 2).toString()).toBe([ ["hello", "world"], ["testing", 1], [2,3]].toString());
  });

});
