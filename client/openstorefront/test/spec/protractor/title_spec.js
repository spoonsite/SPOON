'use strict';

var pageTitle = 'DI2E Storefront';

describe('title__Page Title', function() {
  it('has a title of- ' + pageTitle, function() {
    browser.get(theSite, 2500);
    browser.driver.sleep(12000);
    expect(browser.getTitle()).toEqual(pageTitle);
  }, 25000);
});
