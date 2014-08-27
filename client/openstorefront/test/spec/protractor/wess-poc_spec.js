describe('wess-poc_', function() {
    it('clears the search', function() {
        browser.get('http://wess.usurf.usu.edu');
        element(by.id('searchButton')).click();
        element(by.id('clearSearch')).click();

        element.all(by.css('.criteriaTitle')).get(0).click();
        browser.driver.sleep(2000);
    });

    it('go to Settings and click on buttons', function() {
        element.all(by.css('.ui-btn.ui-icon-gear')).get(0).click();
        browser.driver.sleep(1500);

        element(by.id('mapMode')).sendKeys(protractor.Key.ENTER);
        browser.driver.sleep(1500);

        element(by.id('resultsMode')).sendKeys(protractor.Key.ENTER);
        browser.driver.sleep(1500);

        element(by.id('mapFileImagery')).sendKeys(protractor.Key.ENTER);
        browser.driver.sleep(1500);

        element(by.id('mapFileBounds')).sendKeys(protractor.Key.ENTER);
        browser.driver.sleep(1500);


    });

});

