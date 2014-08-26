'use strict';

var pageTitle = 'DI2E Storefront';

describe('title', function() {
  it('has a title of- ' + pageTitle, function() {
    browser.get('http://di2e.github.io/openstorefront');
    expect(browser.getTitle()).toEqual(pageTitle);
  });
});
