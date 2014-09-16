// theSite = 'http://store-prod.usu.di2e.net:8080/openstorefront';
// theSite = 'http://store-dev.usu.di2e.net:8080/openstorefront';
// theSite = 'http://di2e.github.io';

// GLOBAL Variable for the tests
theSite = 'http://di2e.github.io';

/* Trying to find you a better page, for now you can go to
    https://ri1-openam-01.di2e.net/openam 
   login/logout and it will kill your storefront session
   On the dev server the URL will be
    http://store-dev.usu.di2e.net/openam
*/

// VPN access NOT working, it blocks access to starting the Selenium Server on Localhost
/* describe('OpenAM Login page, if needed', function() {
    // Log on to OpenAM DEV store page (different GUI than prod page)
    it('Log in on the DEV OpenAM page', function() {
        // Navigate to the site
        browser.get('http://store-dev.usu.di2e.net/openam', 2500);

        browser.driver.sleep(9000);

        if(element(by.css('group-field-block')).isPresent()){
            console.log('You need to log on to the OpenAM server!');
        }
        else console.log('logged on');
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