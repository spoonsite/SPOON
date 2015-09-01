/* 'use strict';

// TypeError: 'undefined' is not an object (evaluating 'input[x].description.toLowerCase')
//  at C:/CODE/openstorefront/client/openstorefront/app/scripts/filters/loosefilter.js:31

describe('Filter: looseFilter', function () {
  // load the filter's module
  beforeEach(module('openstorefrontApp'));

  // initialize a new instance of the filter before each test
  var looseFilter;
  beforeEach(inject(function ($filter) {
    looseFilter = $filter('looseFilter');
  }));

  it('should return search results from the array"', function () {
    var myInput = ["The quick brown fox jumped over the lazy dog", 'blah', 'yup'];
    var mySearchText = ["Brown Fox", 'lazy dog'];
    expect(looseFilter(myInput, mySearchText).toString()).toBe(true);
  });
  alert('Filter:  looseFilter; should return the input in an x dimensional array = PASS (1 expect)');
});
*/