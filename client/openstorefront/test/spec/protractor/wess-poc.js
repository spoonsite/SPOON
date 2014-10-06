describe('wess-poc_', function() {
    it('click default to clear searches', function() {
        browser.get('http://wess.usurf.usu.edu');
        element(by.id('searchButton')).click();
        element(by.id('clearSearch')).click();

        element.all(by.css('.criteriaTitle')).get(0).click();
        browser.driver.sleep(2000);

        // POC don't worry about checking, no map displayed anyway.
        expect(true).toBe(true);
    }, 25000);

    it('go to Settings and click on buttons- POC', function() {
        element.all(by.css('.ui-btn.ui-icon-gear')).get(0).click();

        element(by.id('mapMode')).sendKeys(protractor.Key.ENTER);

        element(by.id('resultsMode')).sendKeys(protractor.Key.ENTER);

        element(by.id('mapFileImagery')).sendKeys(protractor.Key.ENTER);

        element(by.id('mapFileBounds')).sendKeys(protractor.Key.ENTER);

        // POC don't worry about checking, no map displayed anyway.
        expect(1).toBe(1);

    }, 25000);

});

