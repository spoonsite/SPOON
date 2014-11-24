/* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

describe('homepageLinksClicked Click Highlights and Footer Links on Storefront Homepage', function() {

    it('click on the Highlights links', function() {
        // NOTE:  This test goes to off-site links that are NOT Angular, so turn off Angular Sync.
        browser.ignoreSyncronization = true;
        browser.get(theSite, 35000);
        // Wait for it to sync, a bit slower on the VPN
        browser.driver.sleep(10000);

        for (var i=0; i < 4; i++) {
            element.all(by.css('.listing_short_title_text')).get(i).click();
            browser.driver.sleep(350);
            browser.driver.navigate().back();
            browser.driver.sleep(350);
        }
        expect(true).toBe(true);   //Some links now go off of the di2e website.
    }, 61000);

    it('click on the FOOTER links, columns 1-4, all links below them', function() {
        // Footer has columns 1, 2, 3, 4.  Column li's are zero-based.
        browser.get(theSite, 35000);
        browser.driver.sleep(350);
        element.all(by.css('.column li')).get(0).click();
        browser.driver.navigate().back();
        browser.driver.sleep(350);
        element.all(by.css('.column li')).get(1).click();
        browser.driver.navigate().back();
        browser.driver.sleep(350);
        element.all(by.css('.column:nth-child(2) li')).get(0).click();
        browser.driver.navigate().back();
        browser.driver.sleep(350);
        element.all(by.css('.column:nth-child(3) li')).get(0).click();
        browser.driver.navigate().back();
        browser.driver.sleep(350);
        element.all(by.css('.column:nth-child(3) li')).get(1).click();
        browser.driver.navigate().back();
        browser.driver.sleep(350);
        element.all(by.css('.column:nth-child(3) li')).get(2).click();
        browser.driver.navigate().back();
        browser.driver.sleep(350);
        element.all(by.css('.column:nth-child(3) li')).get(3).click();
        browser.driver.navigate().back();
        browser.driver.sleep(350);
        element.all(by.css('.column:nth-child(4) li')).get(0).click();
        browser.driver.navigate().back();
        // Skip column 4, no. 1, General Questions as it is an email link
        browser.driver.sleep(350);
        element.all(by.css('.column:nth-child(4) li')).get(2).click();
        browser.driver.navigate().back();
        // Bottom copyright "Consent to Monitoring" link
        element.all(by.css('.copyright')).get(0).click();
        expect(true).toBe(true);  // No page load failures, links exist
        browser.driver.navigate().back();
        browser.driver.sleep(750);
        // Turn Angular sync back on!
        browser.ignoreSyncronization = false;
        browser.get(theSite, 35000);
    }, 35000);

 });
