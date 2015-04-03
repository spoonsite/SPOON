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

describe('search__SortBy', function() {
    it('Name Z-A returns Vega as the first search result', function() {
        // Search on ALL entries (null search term)
        browser.get(theSite, 11000);
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();
        browser.driver.sleep(11500);

        // Verify some results are returned, doesn't matter at this point how many
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);

        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        browser.driver.sleep(400);
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        browser.driver.sleep(400);
        // Add tab for Firefox as it needs to loose focus in order to dynamically sort (Chrome is automatic without the tabbing)
        element(by.model('orderProp')).sendKeys(protractor.Key.TAB);
        browser.driver.sleep(1000);

        // VERIFY it is sorted Z - A
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var ztoa = element(by.binding('item.description'));
        expect(ztoa.getText()).toBe('Vega is a 3D map widget with support for high-resolution imagery in various formats including WMS, WFS, and ArcGIS. Vega also supports 3-dimensional terrain, and time-based data and has tools for drawing shapes and points and importing/exporting data.');
    }, 20000);

    it('Last Update newest returns CLAVIN the first search result', function() {

        // 18 Aug added Sort By:  Rating (high-low); Rating (low-high)
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        element(by.model('orderProp')).sendKeys(protractor.Key.TAB);

        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        element(by.model('orderProp')).sendKeys(protractor.Key.TAB);

        // VERIFY it is sorted correctly
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var newest = element(by.binding('item.description'));
        expect(newest.getText()).toBe('All uses; no restrictions');
    }, 20000);

    it('Last Update oldest returns EMP as the first search result', function() {
        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        element(by.model('orderProp')).sendKeys(protractor.Key.TAB);

        // VERIFY it is sorted correctly
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var oldest = element(by.binding('item.description'));
        expect(oldest.getText()).toBe('The Extensible Mapping Platform (EMP) is a US Government Open Source project providing a framework for building robust OWF map widgets and map centric web applications. The EMP project is currently managed by US Army Tactical Mission Command (TMC) in partnership with DI2E, and developed by CECOM Software ...');
    }, 20000);

    it('Name A-Z returns CAS as the first search result', function() {
        // 18 Aug. 2014 Need to go UP more to get past Rating (low-high) and Rating (high-low)
        element(by.model('orderProp')).sendKeys(protractor.Key.UP);
        element(by.model('orderProp')).sendKeys(protractor.Key.UP);
        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.UP);
        element(by.model('orderProp')).sendKeys(protractor.Key.UP);
        element(by.model('orderProp')).sendKeys(protractor.Key.UP);
        element(by.model('orderProp')).sendKeys(protractor.Key.TAB);

        // VERIFY it is sorted correctly
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var atoz = element(by.binding('item.description'));
        expect(atoz.getText()).toBe('The Central Authentication Service (CAS) is a single sign-on protocol for the web. Its purpose is to permit a user to access multiple applications while providing their credentials (such as userid and password) only once. It also allows web applications to authenticate users without gaining access to ...');
    }, 20000);
});
