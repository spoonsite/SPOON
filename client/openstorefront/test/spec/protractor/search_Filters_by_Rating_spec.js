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

describe('search_Filters_by_Rating', function() {
    // Open the main site
    browser.get(theSite, 9000);
    element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

    it('Search filter by 5 star Rating', function () {
        element.all(by.css('.star-off-png')).get(4).click();
        // No search results expected
        expect(element.all(by.repeater('item in data')).count()).toEqual(0);
    }, 20000);

    it('Click Reset Filters button in SEARCH RESULTS', function () {
        element.all(by.css('.ng-scope.btn.btn-default')).get(0).click();
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
    }, 15000);

    it('Search filter by 1 star Rating', function () {
        element.all(by.css('.star-off-png')).get(0).click();
        // No search results expected
        expect(element.all(by.repeater('item in data')).count()).toEqual(0);
    }, 15000);

    it('Click Reset Filters button LEFT NAV', function () {
        element.all(by.css('.btn.red.btn-default')).get(0).click();
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
    }, 15000);

   it('Click More Filters  Expand all filters click all down buttons', function() {
       // Click on "More Filters"
       element.all(by.css('.btn.btn-default')).get(0).click();

       // Click all fa fa-caret-down buttons  //TODO:  Get this dynamically
       var carrotsDown = 21;
       for (var i=0; i <= carrotsDown; i++) {
           element.all(by.css('.fa.fa-caret-down')).get(i).click();
           browser.driver.sleep(1);
       }
       // Verify last element is present from the expansion
       expect(element(by.id('Service Transport Protocal_OTH')).isDisplayed());
       expect(true).toEqual(true);
   }, 25000);
});
