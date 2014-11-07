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

/* 03 Oct 2014:  Admin tools not available on storefront1.di2e.net.  Devin says it is not expected for IOC?
                  Would have to request privileges from the di2e help desk.
   28 Oct 2014:  Put this back in as Admin Tools are available on store-accept
*/
describe('adminTools', function() {
    it('Admin Tools Menu is available from the drop-down under username', function () {
        browser.get(theSite);

        // Click on User Name
        element.all(by.css('.nav.navbar-nav.navbar-right')).get(0).click();

        // Click on the first dropdown menu item which is "Admin Tools"
        element.all(by.css('.dropdown-menu a')).get(0).click();


        expect(element.all(by.css('.adminTools')).count()).toEqual(1);
    });

/*  THIS HAS BEEN REMOVED AND CHANGED
    it('Click left menus of Admin Tools and check for resulting right pane data', function() {
        // TODO:  Fill this in when coding is complete, look for more specific info on "expect"

        // Manage Attributes (highest level no sub-trees)
        element.all(by.css('.indented')).get(3).click();
        expect(element.all(by.css('.ngHeaderContainer')).count()).toEqual(1);

        // Manage Lookups (highest level no sub-trees)
        element.all(by.css('.indented')).get(5).click();
        expect(element.all(by.css('.ng-scope')).count()).toEqual(33);

        // Manage Branding
        element.all(by.css('.indented')).get(8).click();
        expect(element.all(by.css('.ng-scope')).count()).toEqual(30);
    });


    it('Click on Manage Components and each tab under VANTAGE Software', function() {
      // Manage Components
      element.all(by.css('.indented')).get(7).click();
      expect(element.all(by.css('.ng-scope')).count()).toEqual(37);

      // Search components for "vantage" should have 2 results
      element(by.id('editComponent')).sendKeys('Vantage', protractor.Key.TAB);
      expect(element.all(by.repeater('thing in search track by $index')).count()).toEqual(2);

      // Click on first result, Vantage Software Suite
      element.all(by.repeater('thing in search track by $index')).get(0).click();

      // Click on the tabs
      for (var z=0; z <= 5; z++) {
        element.all(by.repeater('bar in nav.bars')).get(z).click();
      }
    })
*/
});

