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

describe('Click on the second search result "WESS OZONE Widget"', function() {
  it('I am taken to the results summary tab', function() {
      // Click on the second or OZONE results
      element.all(by.css('.results-content-title-content')).get(1).click();
        
    expect(element.all(by.binding('results-content-description'))).toString();
    console.log(element.all(by.binding('results-content-description')).toString());
  });
});