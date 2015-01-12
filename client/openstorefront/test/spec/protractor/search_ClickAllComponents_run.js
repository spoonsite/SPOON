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
// *** varies, depending on what is in the sample database ***
var totalResults = 71; // Articles present


describe('searchAll_Search entire database', function() {
    it('Global search (all blank) returns ' + totalResults + ' expected current db results', function() {
        // Open the main site
        browser.get(theSite, 4000);

        // Search on ALL entries (null search term)
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

        // Wait for it to sync, a bit slower on the VPN
        browser.driver.sleep(12000);
    });
});

describe('From the global search all search results', function() {
  it('clicking on each of the search results does NOT timeout', function () {
    element.all(by.repeater('item in data')).each(function (theItem) {
      theTitle = theItem.element(by.css('.results-content-title'));
      //textTitle = theTitle.element(by.css('.results-content-title-content.ng-binding')).getText();
      //console.log(textTitle);  // STILL ouputs and object not the component text
      theTitle.click();
    });
  });
});
