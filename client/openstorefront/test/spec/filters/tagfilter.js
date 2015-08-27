'use strict';

describe('Filter: tagFilter', function () {

  // load the filter's module
  beforeEach(module('openstorefrontApp'));

  // initialize a new instance of the filter before each test
  var tagFilter;
  beforeEach(inject(function ($filter) {
    tagFilter = $filter('tagFilter');
  }));

  it('should return what you feed into it', function () {
    //var theArr = [123, 456, 7890, -99, '-a' , 'TheEND!'];
    var theObj = [{something: 'blah'}, {something2: 'blah'}];
    expect(tagFilter(theObj)).toBe(theObj);
  });
  alert('Filter:  tagFilter; should return what you feed into it = PASS (1 expect)');
});
