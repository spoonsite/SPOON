// GLOBAL Variable for the tests
//theSite = 'http://di2e.github.io/openstorefront/index.html';
//openAM = false;

theSite = 'https://storefront1.di2e.net/openstorefront/index.html';
openAM = true;

//theSite = 'http://store-prod.usu.di2e.net:8080/openstorefront/index.html';
//openAM = true;

describe('SvcV-4  Log in to the site if needed', function() {
    // Log in to the OpenAM page if needed
    it('Log in to the OpenAM site as required', function() {
        if (openAM) {
            // For non-Angular page turn OFF synchronization
            browser.ignoreSynchronization = true;
            browser.get(theSite, 3500);
            browser.driver.sleep(1000);
            browser.driver.findElement(by.id('IDToken1')).sendKeys('amadmin'); // amadmin
            browser.driver.findElement(by.id('IDToken2')).sendKeys('password', protractor.Key.ENTER);
            browser.driver.sleep(1000);

            // refresh past too many redirects error
            browser.ignoreSyncronization = false;
            browser.refresh();
            browser.ignoreSyncronization = false;
            browser.driver.sleep(3000);
        }
    expect(true).toBe(true);
    });


});

describe('SvcV-4_button from the home page', function() {
    it('Expand the buttons in the categories', function () {
        // Navigate to the site
        browser.ignoreSyncronization = false;
        browser.get(theSite, 4000);

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