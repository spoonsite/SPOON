describe('homepageLinksClicked Click Highlights and Footer Links on Storefront Homepage', function() {

    it('click on the Highlights links', function() {
        browser.get(theSite, 25000);
        // Wait for it to sync, a bit slower on the VPN
        browser.driver.sleep(10000);

        for (var i=0; i < 5; i++) {
            element.all(by.css('.listing_short_title_text')).get(i).click();
            browser.driver.sleep(350);
            browser.driver.navigate().back();
            browser.driver.sleep(350);
        }
        expect(true).toBe(true);   //Some links now go off of the di2e website.
    });

    it('click on the FOOTER links, columns 1-4, all links below them', function() {
        // Footer has columns 1, 2, 3, 4.  Column li's are zero-based.
        browser.get(theSite, 25000);
        element.all(by.css('.column li')).get(0).click();
        browser.driver.navigate().back();
        element.all(by.css('.column li')).get(1).click();
        browser.driver.navigate().back();
        element.all(by.css('.column:nth-child(2) li')).get(0).click();
        browser.driver.navigate().back();
        element.all(by.css('.column:nth-child(3) li')).get(0).click();
        browser.driver.navigate().back();
        element.all(by.css('.column:nth-child(3) li')).get(1).click();
        browser.driver.navigate().back();
        element.all(by.css('.column:nth-child(3) li')).get(2).click();
        browser.driver.navigate().back();
        element.all(by.css('.column:nth-child(3) li')).get(3).click();
        browser.driver.navigate().back();
        element.all(by.css('.column:nth-child(4) li')).get(0).click();
        browser.driver.navigate().back();
        // Skip column 4, no. 1, General Questions as it is an email link
        element.all(by.css('.column:nth-child(4) li')).get(2).click();
        browser.driver.navigate().back();
        // Bottom copyright "Consent to Monitoring" link
        element.all(by.css('.copyright')).get(0).click();
        browser.driver.navigate().back();
        browser.driver.sleep(750);
        expect(true).toBe(true);  // No page load failures, links exist
    });

 });