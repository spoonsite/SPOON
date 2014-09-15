//var theSite = 'http://store-prod.usu.di2e.net:8080/openstorefront/index.html';  OpenAM

// GLOBAL Variable for the tests
theSite = 'http://di2e.github.io/openstorefront';

describe('SvcV-4_button from the home page', function() {

    it('Expand the buttons in the categories', function () {
        // Open the main site
        browser.get(theSite);

        // Click the SvcV-4 button
        element.all(by.css('.btn.btn-primary.pull-right')).get(1).click();

        // Expand
        numNow=15;  // Couldn't get this dynamically, could change?
        // loop get(0).click();
        for (var i=0; i <= numNow; i++) {
            element.all(by.css('.diagram-toggle-btn')).get(i).click();
            browser.driver.sleep(1);
        }

        // Brittle
        expect(element.all(by.css('.btn')).count()).toEqual(7);
    });



});