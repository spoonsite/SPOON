describe('Search the entire store (null search term)', function() {
    
    // *** varies, depending on what is in the sample database ***
    var totalResults = 59;
    
    it('returns all expected ' + totalResults + ' current results', function() {
        // Open the main site
        browser.get('http://di2e.github.io/openstorefront');

        // Search on ALL entries (null search term)
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

        // Should return 58 results
        expect(element.all(by.repeater('item in data')).count()).toEqual(totalResults);

    });
});
