describe('When I search the entire store', function() {
    it('should return all results', function() {
        // Open the main site
        browser.get('http://di2e.github.io/openstorefront');

        // Search on ALL entries (null search term)
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

        // Should return 58 results
        expect(element.all(by.repeater('item in data')).count()).toEqual(58);

    });
});
