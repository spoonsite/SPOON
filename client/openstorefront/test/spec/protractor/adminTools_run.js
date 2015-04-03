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
   28 Oct 2014:  Put this back in as Admin Tools are available on store-accept.
   07 Nov 2014:  Admin Tools updated with v1.1 of Storefront.  Deleting old tests, adding new ones.
   21 Dec 2014:  Admin Tools received a MAJOR upgrade and additions for v1.2 of the Storefront.  Still need
                  to write automation to test User Profiles, User Messages, Jobs, and System tabs.
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

/*
    it('TODO-  Change a few values under Global Configuration then change back', function() {

        // Hourly Tab *
        browser.driver.sleep(2000);
        element.all(by.css('.ng-isolate-scope')).get(0).click();
        element(by.id('HoursInput')).sendKeys('99');
        browser.driver.sleep(500);

        element(by.id('AtMinutes')).click();
        element(by.id('AtMinutes')).sendKeys(protractor.Key.DOWN);
        element(by.id('AtMinutes')).sendKeys(protractor.Key.ENTER);

        element(by.css('.btn.btn-default')).click();


    });

    it('TODO-  Component Configuration Tab', function() {

    });
});


describe('TODO-  adminTools_Jira Configuration Tab', function() {
    it('TODO-  Add a new mapping', function() {

    });

    it('TODO-  Edit the mapping', function() {

    });

    it('TODO-  Delete the mapping', function() {

    });
*/
});
