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

// GLOBAL Variable for the tests
// CURRENT PRODUCTION  ***** DO NOT RUN WITHOUT PERMISSION FROM DI2E AND COORDINATION *****
//theSite = 'https://storefront.di2e.net/openstorefront/index.html';
//openAM = false; other = true;

// TESTING SITE
theSite = 'http://store-dev.usu.di2e.net/openstorefront/index.html';
openAM = false; other = true;


// Leave OUTSIDE of describe, it, expect-- as it is NOT an Angular website page.
if (openAM) {
    // For non-Angular page turn OFF synchronization
    browser.ignoreSynchronization = true;
    browser.get(theSite, 3500);
    browser.driver.sleep(1000);
    browser.driver.findElement(by.id('IDToken1')).sendKeys('amadmin'); // (amadmin) Set to valid account
    browser.driver.findElement(by.id('IDToken2')).sendKeys('password', protractor.Key.ENTER);
    browser.driver.sleep(1000);
}

// Other Non-OpenAM site)
if (other) {
    browser.ignoreSynchronization = true;
    browser.get(theSite, 3500);
    element.all(by.css('.form-control')).get(0).sendKeys('admin');
    element.all(by.css('.form-control')).get(1).sendKeys('secret', protractor.Key.ENTER);
    browser.driver.sleep(2000);
}

