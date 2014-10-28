describe('Search by TAGS', function() {
    it('Search for iSpatial and clear out any tags', function() {
        // Open the main site
        browser.get(theSite, 25000);

        // Enter the search term
        element(by.id('mainSearchBar')).sendKeys('iSpatial', protractor.Key.ENTER);

        // Click on the result
        element.all(by.css('.results-content-title-content')).get(0).click();

        element.all(by.css('.fa-tags')).get(0).click();
        browser.driver.sleep(1000);

       // Clear out any existing tags
            var theInput = element.all(by.css('.tags')).get(1);
            theInput.click();   // Gets focus but won't HOLD it!
            theInput.sendKeys(' ');
            browser.driver.sleep(2000);
            theInput.clear();
            browser.driver.sleep(2000);


        // EXPECT 'Search for "iSpatial" and clear out any tags associated with this search result'
        //expect(element.all(by.repeater('tag-list')).count().toEqual(0));

    });

    // it 'Add new tags'  *** ADDS to search bar NOT the tags bar ***
/*    it ('Add a new tag iAddedThis to iSpatial', function() {
        element(by.css('input')).sendKeys('iAddedThis', protractor.Key.ENTER);

    });
*/
    // Search by those tags

    // Delete the tags



});
