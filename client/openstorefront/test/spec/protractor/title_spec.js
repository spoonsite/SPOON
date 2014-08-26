'use strict';

var pageTitle = 'DI2E Storefront';
2014-Aug-26
describe('Storefront_homepage', function() {
  it('has a title of- ' + pageTitle, function() {
    browser.get('http://di2e.github.io/openstorefront');
    expect(browser.getTitle()).toEqual(pageTitle);
  });
});
