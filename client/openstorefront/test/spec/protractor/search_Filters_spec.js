describe('search_Filters', function() {
    // FILTER BY TEXT
    var theText1 = 'DIB';
    var numFound1 = 3;
    it('Filter by text- ' + theText1 + ' returned ' + numFound1 + ' expected results plus ' + theText1 + ' was in the search results', function () {
        textSearch(theText1, numFound1);
    }, 30000);

    var theText3 = 'iSpatial';
    var numFound3 = 1;
    it('Filter by text- ' + theText3 + ' returned ' + numFound3 + ' expected results plus ' + theText3 + ' was in the search results', function () {
        textSearch(theText3, numFound3);
    }, 30000);

    var theText4 = 'widgets';
    var numFound4 = 8;
    it('Filter by text- ' + theText4 + ' returned ' + numFound4 + ' expected results plus ' + theText4 + ' was in the search results', function () {
        textSearch(theText4, numFound4);
    }, 30000);


// FILTER BY ATTRIBUTE
    browser.ignoreSyncronization = false;
    browser.get(theSite, 15000);
    element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

    // Wait for it to sync, a bit slower on the VPN
    browser.driver.sleep(12000);

    // Expand "DI2E Evaluation Level"
    // *************** 1 won't work, says it is out of range *************************
    element.all(by.css('.overflow-pair-right')).get(0).click();
    browser.driver.sleep(6000);

    var checkB0 = 'DI2E Evaluation Level_LEVEL0';
    var expResults0 = 37;
    it('Filter By Attribute- ' + checkB0 + ' gives ' + expResults0 + ' results', function() {
        attribFilter(checkB0, expResults0);
    }, 20000);

    var checkB1 = 'DI2E Evaluation Level_LEVEL1';
    var expResults1 = 4;
    it('Filter By Attribute- ' + checkB1 + ' gives ' + expResults1 + ' results', function() {
        attribFilter(checkB1, expResults1);
    }, 20000);

    var checkBNA = 'DI2E Evaluation Level_NA';
    var expResultsNA = 17;
    it('Filter By Attribute- ' + checkBNA + ' gives ' + expResultsNA + ' results', function() {
        attribFilter(checkBNA, expResultsNA);
    }, 20000);

    // *************** 1 won't work, says it is out of range *************************
    element.all(by.css('.fa.fa-caret-down')).get(0).click();
    browser.driver.sleep(300);

    // FUNCTIONS
    function textSearch (theText, numFound) {
        // Search on ALL entries (null search term)
        browser.ignoreSyncronization = false;
        browser.get(theSite, 15000);
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

        // Wait for it to sync, a bit slower on the VPN
        browser.driver.sleep(12000);

        // Clear the box
        var theInput = element(by.model('query'));
        theInput.clear();

        // Enter text
        theInput.sendKeys(theText);

        // Should be numFound results
        expect(element.all(by.repeater('item in data')).count()).toEqual(numFound);

        // May not always show up onscreen?
        var newest = element(by.binding('item.description'));
        expect(newest.getText()).toContain(theText);
    }

    function attribFilter(checkB, expResults) {
        element(by.id(checkB)).click();
        browser.driver.sleep(1800);
        expect(element.all(by.repeater('item in data')).count()).toEqual(expResults);
        element(by.id(checkB)).click();
        browser.driver.sleep(1500);
    }
});





