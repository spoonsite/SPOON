  // FUNCTION
  function attribFilter(checkB, expResults) {
    element(by.id(checkB)).click();
    browser.driver.sleep(1800);

    // ***** NOTE: ***** switch from element.all(by.repeater('item in data')) to by.css('.results-content') !!!
    expect(element.all(by.css('.results-content')).count()).toEqual(expResults);
    element(by.id(checkB)).click();
    browser.driver.sleep(1500);
  }

describe('search_Filters_by_Attribute', function() {
  it('Get to search results', function () {
    // FILTER BY ATTRIBUTE
    browser.ignoreSynchronization = false;
    browser.get(theSite, 15000);
    browser.driver.sleep(3000);

    // Search ALL
    element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

    // Wait for it to sync, a bit slower on the VPN
    browser.driver.sleep(14000);

    // Expand "DI2E Evaluation Level"
    element.all(by.css('.overflow-pair-right')).get(1).click();
    browser.driver.sleep(1000);
  });

  var checkB0 = 'DI2E Evaluation Level_LEVEL0'; var expResults0 = 37;
  it('Filter By Attribute- ' + checkB0 + ' gives ' + expResults0 + ' results', function() {
    attribFilter(checkB0, expResults0);
  }, 63000);

  var checkB1 = 'DI2E Evaluation Level_LEVEL1'; var expResults1 = 4;
  it('Filter By Attribute- ' + checkB1 + ' gives ' + expResults1 + ' results', function() {
    attribFilter(checkB1, expResults1);
  }, 45000);

  var checkBNA = 'DI2E Evaluation Level_NA';    var expResultsNA = 17;
  it('Filter By Attribute- ' + checkBNA + ' gives ' + expResultsNA + ' results', function() {
    attribFilter(checkBNA, expResultsNA);
  }, 45000);

});



