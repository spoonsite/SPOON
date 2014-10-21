// GLOBAL Variable for the tests
//theSite = 'http://di2e.github.io/openstorefront/index.html';
//openAM = false; other = false;

//theSite = 'http://store-prod.usu.di2e.net:8080/openstorefront/index.html';
//openAM = true; other = false;

// CURRENT PRODUCTION
//theSite = 'https://storefront1.di2e.net/openstorefront/index.html';
//openAM = true; other = false;

// TESTING SITE
theSite = 'http://store-accept.usu.di2e.net/openstorefront/index.html';
openAM = false; other = true;


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

// Other Non-OpenAM site, but still need to login!
if (other) {
    console.log('**********  Please manually log in, you have ~30 seconds.  **********');
    console.log(theSite);
    console.log('UN:admin,  PW:secret');
    console.log('*********************************************************************');
    browser.driver.sleep(33000);
}

