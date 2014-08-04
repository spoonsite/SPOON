describe('Search for "VANTAGE"', function() {
  it('returns two results', function() {
    // Open the main site
    browser.get('http://di2e.github.io/openstorefront');
    
    // Enter the search term (changed to enter after updates to search keys 7/28)
    element(by.id('mainSearchBar')).sendKeys('VANTAGE', protractor.Key.ENTER);

    // Should only be two results
    expect(element.all(by.repeater('item in data')).count()).toEqual(2);
  });
});


describe('Click on the results page.  From the search results page', function() {
  it('click on the Details, Reviews, and Q&A tabs -- content loads from those tabs', function() {
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
    expect(element.all(by.binding('results-content-description'))).toString();
  });    
  
  it('all tags have a reference (are not null between the tags)', function() {
    element.all(by.xpath('/a')).then(function(links) {
        for (var i = 0; i < links.length; i++) {
            expect(links[i].getAttribute('href')).not.toBeNull();
            }
        });
    });

});
