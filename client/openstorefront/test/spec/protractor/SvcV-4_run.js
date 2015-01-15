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

describe('SvcV-4_button from the home page', function() {
    it('Expand the buttons in the categories', function () {
        // Navigate to the site
        browser.ignoreSynchronization = false;
        browser.get(theSite, 8000);

        // Click the SvcV-4 button
        element.all(by.css('.btn.btn-primary.pull-right')).get(0).click();
        browser.driver.sleep(5000);

        // Expand
        numNow = 17;  // Couldn't get this dynamically, could change?
        // loop get(0).click();
        for (var i = 0; i <= numNow; i++) {
            element.all(by.css('.diagram-toggle-btn')).get(i).click();
            browser.driver.sleep(100);
        }
        // Brittle
        expect(element.all(by.css('.btn')).count()).toEqual(7);
    }, 63000);
});
