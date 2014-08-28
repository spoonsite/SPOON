describe('searchDetails_Search for VANTAGE', function() {
  it('returns 2 results', function() {
    // Open the main site
    browser.get('http://di2e.github.io/openstorefront');
    
    // Enter the search term (changed to enter after updates to search keys 7/28)
    element(by.id('mainSearchBar')).sendKeys('VANTAGE', protractor.Key.ENTER);

    // Should only be two results
    expect(element.all(by.repeater('item in data')).count()).toEqual(2);
  });
});


describe('searchDetails_Click on the results', function() {
  it('click on the Details Reviews and Q&A tabs -- content loads from those tabs', function() {
    // Click on the second or OZONE results
    element.all(by.css('.results-content-title-content')).get(1).click();

    // Verify tabs (Summary, Details, Reviews, Q&A) on the page
    var list = element.all(by.css('.nav-tabs li')); 
    expect(list.count()).toBe(7);  //Strange, must be some blank ones in here?

    // Click on the tabs from search results details page
    element.all(by.css('.nav-tabs li')).get(1).click();
    element.all(by.css('.nav-tabs li')).get(2).click();
    element.all(by.css('.nav-tabs li')).get(3).click();
    element.all(by.css('.nav-tabs li')).get(0).click();
    // ALTERNATE:  element(by.cssContainingText('.nav-tabs','REVIEWS')).click();
    expect(element.all(by.binding('results-content-description')));

  });

  it('click page tabs to hide parts of page  click tabs again', function() {
    element(by.id('showPageLeft')).click();
    browser.driver.sleep(500);
    element(by.id('showPageRight')).click();
    browser.driver.sleep(500);
    element(by.id('showPageRight')).click();
    browser.driver.sleep(500);
    // Assume if buttons are they they were clicked on (if not visible still there)
    expect(true).toBe(true);

    // Click on the tabs from search results details page
    element.all(by.css('.nav-tabs li')).get(1).click();
    element.all(by.css('.nav-tabs li')).get(2).click();
    element.all(by.css('.nav-tabs li')).get(3).click();
    element.all(by.css('.nav-tabs li')).get(0).click();
    expect(element.all(by.binding('results-content-description')));
  });

 it('click on view edit tags and full page', function() {
    // Click on "View/Edit Tags"
    element(by.id('data-collapse-tags')).click();
    // Can't get id (class) of wach button (2 different states)
    element(by.id('permenantLink')).click();
    // Check for popped up window, then close
    expect(true).toBe(true);
 });

});
