// GLOBAL Variable for the tests
//theSite = 'http://di2e.github.io/openstorefront/index.html';
//openAM = false;

theSite = 'https://storefront1.di2e.net/openstorefront/index.html';
openAM = true;


//theSite = 'http://store-prod.usu.di2e.net:8080/openstorefront/index.html';
//openAM = true;

// Leave OUTSIDE of describe, it, expect as it is NOT an Angular website page.
if (openAM) {
    // For non-Angular page turn OFF synchronization
    browser.ignoreSynchronization = true;
    browser.get(theSite, 3500);
    browser.driver.sleep(1000);
    browser.driver.findElement(by.id('IDToken1')).sendKeys('amadmin'); // (amadmin) Set to valid account
    browser.driver.findElement(by.id('IDToken2')).sendKeys('password', protractor.Key.ENTER);
    browser.driver.sleep(1000);
}

browser.ignoreSynchronization = false;
