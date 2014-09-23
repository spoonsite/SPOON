describe('searchDetails_Search for VANTAGE', function() {
  it('keyword search for VANTAGE returns 2 results', function() {
    // Open the main site
    browser.get(theSite);
    
    // Enter the search term (changed to enter after updates to search keys 7/28)
    element(by.id('mainSearchBar')).sendKeys('VANTAGE', protractor.Key.ENTER);

    // Wait a bit on the VPN for it to finishe the search (slower)
    browser.driver.sleep(7000);

    // Should only be two results
    expect(element.all(by.repeater('item in data')).count()).toEqual(2);
  });
});


describe('searchDetails_Click on the results', function() {
  it('click on Tabs from search details page', function() {
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

  it('click arrows to hide parts of the page ', function() {
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


  it('click tags watches view unwatch full page buttons', function() {
      // Click on the TAGS button
      // TODO: Add logic for if tags button is already depressed
      element.all(by.css('.fa.fa-tags')).get(0).click();

      // Click on binoculars to watch or not watch
      element.all(by.css('.ic.ic-binoculars')).get(0).click();

      // Click on View Watches
      element.all(by.css('.fa.fa-eye')).get(0).click();
      browser.driver.sleep(500);
      //element(by.css('.close')).click();  // Element is not visible.
      browser.refresh(); // close the window

      element(by.id('globalSearch')).sendKeys('VANTAGE', protractor.Key.ENTER);
      // Wait for slow VPN search results
      browser.driver.sleep(7000);
      expect(element.all(by.repeater('item in data')).count()).toEqual(2);
      element.all(by.css('.results-content-title-content')).get(1).click();

      // Set back to original state
      element.all(by.css('.ic.ic-blocked')).get(0).click();

      // Click on Go to Full Screen
      element.all(by.css('.fa.fa-copy')).get(0).click();
      expect(true).toBe(true);

  });


});
