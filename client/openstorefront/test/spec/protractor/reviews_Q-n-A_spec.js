describe('reviews_Q-n-A', function() {
  it('Get to the details of VANTAGE Software Suite', function() {
    // Open the main site
    browser.get(theSite, 9000);

    // Enter the search term (changed to enter after updates to search keys 7/28)
    element(by.id('mainSearchBar')).sendKeys('VANTAGE', protractor.Key.ENTER);

    // Make sure search results are returned
    browser.driver.sleep(14000);

    // Should be 2 results (after search improvements)
    expect(element.all(by.repeater('item in data')).count()).toEqual(2);

    // Click on the first or VANTAGE Software Suite Result
    element.all(by.css('.results-content-title-content')).get(0).click();
    browser.driver.sleep(12000);

    // Verify tabs (Summary, Details, Reviews, Q&A) on the page
    var list = element.all(by.css('.nav-tabs li'));
    expect(list.count()).toBe(7);

    // Click on the REVIEW tab from search results details page
    element.all(by.css('.nav-tabs li')).get(2).click();

  }, 21000);



});
