describe('search_byAttribute_Rating_Reset_Attribute', function() {
    // Open the main site
    browser.get('http://di2e.github.io/openstorefront');
    element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

    it('Search filter by 5 star Rating', function () {
        element.all(by.css('.star-off-png')).get(4).click();
        // No search results expected
        expect(element.all(by.repeater('item in data')).count()).toEqual(0);
    });

    it('Click Reset Filters button when no results are found', function () {
        element.all(by.css('.ng-scope.btn.btn-default')).get(0).click();
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
    })
});

//describe('search_byAttribute_filter', function() {
//   it('expand and check filters', function() {

   //});

//});