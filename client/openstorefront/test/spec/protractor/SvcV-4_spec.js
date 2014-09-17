// theSite = 'http://store-prod.usu.di2e.net:8080/openstorefront';
// theSite = 'http://store-dev.usu.di2e.net:8080/openstorefront';
// theSite = 'http://di2e.github.io';

// GLOBAL Variable for the tests
theSite = 'http://di2e.github.io';


/*
describe('OpenAM Login page, if needed', function() {

    // browser.getTitle() is a function or object
    it('Log in on the OpenAM page', function() {
        // Navigate to the site
        browser.get(theSite, 3500);

        if(browser.getTitle() != 'DI24E Storefront') {
            // Log on to the OpenAM Server
            console.log('You need to log on to the OpenAM server!');
        }
    });
});
*/

describe('SvcV-4_button from the home page', function() {
    it('Expand the buttons in the categories', function () {
        // Navigate to the site
        browser.get(theSite, 3500);

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