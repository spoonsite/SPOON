describe('storefront homepage', function() {
  it('should have a title', function() {
    browser.get('http://di2e.github.io/openstorefront');
    expect(browser.getTitle()).toEqual('DI2E Storefront');
  });
});
