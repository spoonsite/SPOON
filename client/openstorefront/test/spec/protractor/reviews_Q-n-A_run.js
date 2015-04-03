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

describe('reviews_Q-n-A', function() {
  it('Get to the details page, Reviews tab of VANTAGE Software Suite', function() {
    // Open the main site
    browser.get(theSite, 9000);

    // Enter the search term (changed to enter after updates to search keys 7/28)
    element(by.id('mainSearchBar')).sendKeys('VANTAGE', protractor.Key.ENTER);

    // Make sure search results are returned
    browser.driver.sleep(10500);

    // Should be 2 results (after search improvements)
    expect(element.all(by.repeater('item in data')).count()).toEqual(2);

    // Click on the first or VANTAGE Software Suite Result
    element.all(by.css('.results-content-title-content')).get(0).click();
    browser.driver.sleep(7000);

    // Verify tabs (Summary, Details, Reviews, Q&A) on the page
    var list = element.all(by.css('.nav-tabs li'));
    expect(list.count()).toBe(7);

    // Click on the REVIEW tab from search results details page
    element.all(by.css('.nav-tabs li')).get(2).click();

  }, 61000);

  it('Write a Review verify then delete review', function() {
    // Click on Review button
    element(by.css('.page2.page2Styles .tab-content .btn.btn-primary')).click();
    browser.driver.sleep(1000);

    // Weird, works in byAttribute, but not here, says another element would receive the click
    //element.all(by.css('star-off-png')).get(4).click();

    element(by.id('title')).sendKeys('A sweet suite of products!');
    element(by.id('lastUsed')).sendKeys('10-2014'); // WATCH this, it failed the first run!?!?
    element(by.id('comment')).sendKeys('A really great suite for tie fighter squadrons.');
    element(by.id('organization')).clear();
    element(by.id('organization')).sendKeys('9999th Wing of the Death Star Tie Fighters', protractor.Key.ENTER);
    browser.driver.sleep(2500);

    expect(element.all(by.repeater('btn-primary')).count()).toEqual(0);
    browser.driver.sleep(3500);

    // Now, DELETE the review
    element(by.css('.btn.btn-sm')).click();
    browser.driver.sleep(3000);

  }, 59000);


  it('Write a question then answer it', function() {
    // Click on the Q&A tab in the search results area ov VANTAGE Software Suite (already on page)
    element(by.id('qaTab')).click();

    element(by.id('1question')).sendKeys('Why do birds, suddenly appear, every time you are near?',
      protractor.Key.TAB, protractor.Key.ENTER);
    browser.driver.sleep(2000);

    element(by.css('.form-control.ng-pristine.ng-animate.ng-valid-maxlength.ng-valid.ng-valid-required')).sendKeys(protractor.Key.ENTER);
    browser.driver.sleep(5000);

    // Answer Question
    element.all(by.css('[class="btn btn-sm btn-default flipme collapsed"]')).get(0).click();
    browser.driver.sleep(2000);

    element(by.id('1response')).sendKeys('Just like me, they long to be, close to you.  Whoooaaaooaoaoa, close to you!',
      protractor.Key.TAB, protractor.Key.ENTER);
    browser.driver.sleep(2000);

    element(by.css('.form-control.ng-pristine.ng-valid-maxlength.ng-valid.ng-valid-required')).sendKeys(protractor.Key.ENTER);
    //element(by.css('.form-control.ng-pristine.ng-valid-maxlength.ng-invalid.ng-invalid-required')).sendKeys('Cupid', protractor.Key.ENTER);
    browser.driver.sleep(5000);

    // Delete
    element.all(by.css('.fa.fa-trash')).get(0).click();
    browser.driver.sleep(5000);

  }, 35000);

});
