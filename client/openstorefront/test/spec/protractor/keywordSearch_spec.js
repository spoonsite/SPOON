describe('When I search for Common Map Widget API', function() {
  it('should return two results', function() {
    // Open the main site
    browser.get('http://di2e.github.io/openstorefront');
    
    // Enter the search term
    element.all(by.model('searchKey')).get(1).sendKeys('Common Map Widget API');
    
    // Click on the search button
    element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

    // Should only be two results
    expect(element.all(by.repeater('item in data')).count()).toEqual(2);
  });
});