'use strict';

var pageTitle = 'DI2E Storefront';

describe('Storefront homepage', function() {
  it('has a title: ' + pageTitle, function() {
    browser.get('http://di2e.github.io/openstorefront');
    expect(browser.getTitle()).toEqual(pageTitle);
  });
});
