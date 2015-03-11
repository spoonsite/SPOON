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
        browser.ignoreSynchronization = true;
        browser.get(theSite, 8000);

        // Click the SvcV-4 button
        element.all(by.css('.btn.btn-primary.pull-right')).get(0).click();
        browser.driver.sleep(5000);

        // Expand by clicking + button on "DI2E SvcV-4 Alignment"
        element(by.css('.indented.tree-icon.fa.fa-plus')).click();
        // click on 1 Infrastructure Services
        element.all(by.css('.indented.tree-icon.fa.fa-plus')).get(0).click();
        // click on 1.2 Security Management
        element(by.css('.indented.tree-label.ng-binding')).click();


        // Click Expand All, Collapse All
        element.all(by.css('.btn.btn-default')).get(0).click();
        expect(element.all(by.css('.indented.tree-icon.fa.fa-minus')).count()).toEqual(123);
        element.all(by.css('.btn.btn-default')).get(1).click();
        expect(element.all(by.css('.indented.tree-icon.fa.fa-minus')).count()).toEqual(0);
    }, 63000);
});
