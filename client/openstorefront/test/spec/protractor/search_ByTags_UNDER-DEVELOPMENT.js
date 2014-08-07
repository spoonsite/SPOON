describe('Search by TAGS', function() {
    it('Search for "iSpatial" and clear out any tags', function() {
        // Open the main site
        browser.get('http://di2e.github.io/openstorefront');

        // Enter the search term
        element(by.id('mainSearchBar')).sendKeys('iSpatial', protractor.Key.ENTER);

        // Click on the result
        element.all(by.css('.results-content-title-content')).get(0).click();

        // Click on the TAGS button
        element.all(by.css('.fa-tags')).get(0).click();

        // Clear out any existing tags
        var theInput = element(by.css('.tags'));
        theInput.clear();

        // EXPECT 'Search for "iSpatial" and clear out any tags associated with this search result'
        //expect(element.all(by.repeater('tag-list')).count().toEqual(0));

    });

    // it 'Add new tags'

    // Search by those tags

    // Delete the tags



});