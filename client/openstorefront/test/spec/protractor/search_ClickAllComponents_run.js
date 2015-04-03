/* Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
var totalResults = 59; // Articles present


describe('search_ClickAllComponents', function() {
    it('Global search (all blank) returns ' + totalResults + ' expected current db results', function() {
        // Open the main site
        browser.get(theSite, 4000);

        // Search on ALL entries (null search term)
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

        // Wait for it to sync, a bit slower on the VPN
        browser.driver.sleep(12000);
    });
});

// CLICK all search results to look for SLOW Search Results pages
describe('From the global search all search results', function() {
  componentCount= 0;
  it('clicking on EACH of the search results does NOT timeout', function () {
    // Go through each repeater search item ('item in data')
    element.all(by.repeater('item in data')).each(function (theItem) {
      theTitle = theItem.element(by.css('.results-content-title'));
      theTitle.click().then(function () {
        // Use .then due to asynchronous code this would complete before the .click() otherwise
        componentCount ++;
        var theTime = new Date().toLocaleTimeString();
        // Manually check the output to see if big delays between components exist.
        console.log(componentCount + ': Current System Time:  ' + theTime);
      });
    });
  });
});  // If this goes past the defaultTimeoutInterval: setting in protractor.conf.js it will fail.
