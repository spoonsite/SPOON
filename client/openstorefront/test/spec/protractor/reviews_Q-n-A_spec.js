describe('reviews_Q-n-A', function() {
  it('Get to the details page, review tab of VANTAGE Software Suite', function() {
    // Open the main site
    browser.get(theSite, 9000);

    // Enter the search term (changed to enter after updates to search keys 7/28)
    element(by.id('mainSearchBar')).sendKeys('VANTAGE', protractor.Key.ENTER);

    // Make sure search results are returned
    browser.driver.sleep(10500);

    // Should be 2 results (after search improvements)
    expect(element.all(by.repeater('item in data')).count()).toEqual(2);

    // Click on the first or VANTAGE Software Suite Result
    element.all(by.css('.results-content-title-content')).get(0).click();
    browser.driver.sleep(7000);

    // Verify tabs (Summary, Details, Reviews, Q&A) on the page
    var list = element.all(by.css('.nav-tabs li'));
    expect(list.count()).toBe(7);

    // Click on the REVIEW tab from search results details page
    element.all(by.css('.nav-tabs li')).get(2).click();

  }, 61000);

  it('Write a Review verify then delete review', function() {
    // Click on Review button
    element.all(by.css('.btn.btn-primary')).get(4).click();
    browser.driver.sleep(1000);

    // Weird, works in byAttribute, but not here, says another element would receive the click
    //element.all(by.css('star-off-png')).get(4).click();

    element(by.id('title')).sendKeys('A sweet suite of products!');
    element(by.id('lastUsed')).sendKeys('10-2014');
    element(by.id('comment')).sendKeys('A really great suite for image processing, storage, and dissemination to other tie fighter squadrons.');
    element(by.id('organization')).sendKeys('9999th Wing of the Death Star Tie Fighters', protractor.Key.ENTER);

    expect(element.all(by.repeater('btn-primary')).count()).toEqual(0);
    browser.driver.sleep(3500);

    // Now, DELETE the review
    element(by.css('.btn.btn-sm')).click();
    browser.driver.sleep(1000);

  }, 59000);


  it('Write a question then answer it', function() {
    // Click on the Q&A tab in the search results area ov VANTAGE Software Suite (already on page)
    element(by.id('qaTab')).click();

    element(by.id('1question')).sendKeys('Why do birds, suddenly appear, every time you are near?',
      protractor.Key.TAB, protractor.Key.TAB, protractor.Key.TAB, protractor.Key.TAB,
      'The Carpenters', protractor.Key.ENTER);
    browser.driver.sleep(5000);

    // Answer Question
    element.all(by.css('.btn.btn-sm.btn-default')).get(0).click();
    element(by.id('1response')).sendKeys('Because, just like me, they long to be, close to you.  Whoooaaaooaoaoa, close to you!',
      protractor.Key.TAB, protractor.Key.TAB, protractor.Key.TAB, protractor.Key.TAB,
      'Cupid', protractor.Key.ENTER);
    browser.driver.sleep(5000);

    // Delete
    element.all(by.css('.btn.btn-sm.btn-default')).get(2).click();
    browser.driver.sleep(5000);

  }, 35000);

});
