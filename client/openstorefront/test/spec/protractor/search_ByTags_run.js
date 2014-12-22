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

describe('Search by TAGS', function() {
    it('Search for iSpatial and clear out text box', function() {
        // Open the main site
        browser.get(theSite, 25000);

        // Enter the search term
        element(by.id('mainSearchBar')).sendKeys('iSpatial', protractor.Key.ENTER);

        // Click on the result
        element.all(by.css('.results-content-title-content')).get(0).click();

        // Click tags button to show text input field
        element.all(by.css('.fa-tags')).get(0).click();
        browser.driver.sleep(1000);

        // Clear out the text field, does NOT clear tags!
        element.all(by.css('.input.ng-pristine.ng-valid')).get(1).clear();

    }, 64000);


    it ('Add a new tag iAddedThis to iSpatial', function() {
        element.all(by.css('.input.ng-pristine.ng-valid')).get(1).sendKeys('iAddedThis', protractor.Key.DOWN, protractor.Key.ENTER);
        browser.driver.sleep(3500);
    }, 44000);


    it ('Search by the added tag iAddedThis and show 1 result', function() {
        // Clear the Search
        element(by.id('globalSearch')).clear();
        element(by.id('globalSearch')).sendKeys(protractor.Key.ENTER);
        expect(element.all(by.repeater('item in data')).count()).toEqual(59);

        element.all(by.css('.input.ng-pristine.ng-valid')).get(0).clear();
        element.all(by.css('.input.ng-pristine.ng-valid')).get(0).sendKeys('iAddedThis');
        element.all(by.css('.input.ng-valid.ng-dirty')).get(0).sendKeys(protractor.Key.DOWN, protractor.Key.ENTER);
        browser.driver.sleep(4000);

        // Verify one (iSpatial) result
        expect(element.all(by.repeater('item in data')).count()).toEqual(1);

        // Click on the result
        element.all(by.css('.results-content-title-content')).get(0).click();
    });


    it ('Delete the newly added tag iAddedThis', function () {
        // Click tags button to show text input field
        element.all(by.css('.fa-tags')).get(0).click();
        browser.driver.sleep(1000);

        element.all(by.css('.remove-button.ng-scope')).get(0).click();
        browser.driver.sleep(3500);

        expect(element.all(by.css('.remove-button.ng-scope')).count()).toEqual(0);
    })

});
