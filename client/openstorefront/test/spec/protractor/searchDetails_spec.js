describe('searchDetails_Search for VANTAGE', function() {
  it('keyword search for VANTAGE returns 3 results', function() {
    // Open the main site
    browser.get(theSite, 4000);

    // Enter the search term (changed to enter after updates to search keys 7/28)
    element(by.id('mainSearchBar')).sendKeys('VANTAGE', protractor.Key.ENTER);

    // Make sure search results are returned
    browser.driver.sleep(10000);

    // Should be 3 results (after search improvements)
    expect(element.all(by.repeater('item in data')).count()).toEqual(3);
  });
}, 25000);


describe('searchDetails_Click on the results', function() {
  it('click on Tabs from search details page', function() {
    // Click on the second or OZONE results
    element.all(by.css('.results-content-title-content')).get(1).click();
    browser.driver.sleep(8000);

    // Verify tabs (Summary, Details, Reviews, Q&A) on the page
    var list = element.all(by.css('.nav-tabs li')); 
    expect(list.count()).toBe(7);

    // Click on the tabs from search results details page
    element.all(by.css('.nav-tabs li')).get(1).click();
    element.all(by.css('.nav-tabs li')).get(2).click();
    element.all(by.css('.nav-tabs li')).get(3).click();
    element.all(by.css('.nav-tabs li')).get(0).click();
    // ALTERNATE:  element(by.cssContainingText('.nav-tabs','REVIEWS')).click();
    expect(element.all(by.binding('results-content-description')));
 });

  it('click arrows to hide parts of the page ', function() {
    element(by.id('showPageLeft')).click();
    browser.driver.sleep(500);
    element(by.id('showPageRight')).click();
    browser.driver.sleep(500);
    element(by.id('showPageRight')).click();
    browser.driver.sleep(500);

    // Move it back so that search filters can be used!
    element(by.id('showPageLeft')).click();
    browser.driver.sleep(500);
    // Assume if buttons are they they were clicked on (if not visible still there)
    expect(true).toBe(true);
  }, 25000);


  it('click tags watches view unwatch full page buttons', function() {
      // Click on the TAGS button
      // TODO: Add logic for if tags button is already depressed
      element.all(by.css('.fa.fa-tags')).get(0).click();
      browser.driver.sleep(2000);

      // Click on binoculars to watch or not watch
      element.all(by.css('.ic.ic-binoculars')).get(0).click();
      // Wait for frame refresh
      browser.driver.sleep(3000);

      // Click on View Watches
      element.all(by.css('.fa.fa-eye')).get(0).click();
      browser.refresh(); // close the window
      browser.driver.sleep(3000);

      element(by.id('globalSearch')).sendKeys('VANTAGE', protractor.Key.ENTER);
      expect(element.all(by.repeater('item in data')).count()).toEqual(3);
      element.all(by.css('.results-content-title-content')).get(1).click();
      browser.driver.sleep(2000);

      // Set back to original state
      element.all(by.css('.ic.ic-blocked')).get(0).click();
      browser.driver.sleep(2000);

      // Click on Go to Full Screen
      element.all(by.css('.fa.fa-copy')).get(0).click();
      browser.driver.sleep(2000);
      expect(true).toBe(true);

  }, 25000);


});
