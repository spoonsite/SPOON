// theSite = 'http://store-prod.usu.di2e.net:8080/openstorefront';
// theSite = 'http://di2e.github.io';

// GLOBAL Variable for the tests
theSite = 'http://store-prod.usu.di2e.net:8080/openstorefront/index.html';

// TODO:  Get the signon working
// Manual signon for now; 31 seconds
browser.driver.sleep(31000);
// Page unloads (so it thinks) as it logs in, need to figure this out

/* describe('OpenAM Login page, if needed', function() {
    // Log on to OpenAM DEV store page (different GUI than prod page)
    it('Log in on the DEV OpenAM page', function() {
        // Navigate to the site
        browser.get(theSite, 2500);

        browser.driver.sleep(9000);

        // ***** NO ANGULAR ON THE PAGE, DO IT BY HTML? *****
        if(element(by.css('group-field-block')).isPresent()){
            console.log('You need to log on to the OpenAM server!');
        }
        else console.log('logged on');

        browser.driver.sleep(15000);
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