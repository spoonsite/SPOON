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

// FUNCTION
function textSearch (theText, numFound) {
  // Search on ALL entries (null search term)
  browser.get(theSite, 15000);
  element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

  // Wait for it to sync, a bit slower on the VPN
  browser.driver.sleep(12000);

  // Clear the box
  var theInput = element(by.model('query'));
  theInput.clear();

  // Enter text
  theInput.sendKeys(theText);

  // Should be numFound results
  expect(element.all(by.repeater('item in data')).count()).toEqual(numFound);

  // May not always show up onscreen?
  var newest = element(by.binding('item.description'));
  expect(newest.getText()).toContain(theText);
}

// ***** MAIN TEST ***** //
describe('search_Filters_Text', function() {

  // FILTER BY TEXT
  var theText1 = 'DIB';  var numFound1 = 3;
  it('Filter by text- ' + theText1 + ' returned ' + numFound1 + ' expected results plus ' + theText1 +
      ' was in the search results', function () {
        textSearch(theText1, numFound1);
  }, 61000);

  var theText3 = 'iSpatial';  var numFound3 = 1;
  it('Filter by text- ' + theText3 + ' returned ' + numFound3 + ' expected results plus ' + theText3 +
      ' was in the search results', function () {
        textSearch(theText3, numFound3);
  }, 30000);

  var theText4 = 'widgets';  var numFound4 = 7;
  it('Filter by text- ' + theText4 + ' returned ' + numFound4 + ' expected results plus ' + theText4 +
      ' was in the search results', function () {
        textSearch(theText4, numFound4);
  }, 30000);

});






