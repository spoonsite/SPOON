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

describe('user-tools_User logged in drop-down', function() {
    it('Click on UserName then User Profile', function () {
        browser.get(theSite);
        // Wait for it to sync, a bit slower on the VPN
        browser.driver.sleep(7000);
        // Click on User Name
        //element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        element.all(by.css('.nav.navbar-nav.navbar-right')).get(0).click();
        //element.all(by.css('.ng-binding.ng-scope')).get(0).click();
        browser.driver.sleep(1000);


        // Click on 2nd menu item which is "User Profile"
        element.all(by.css('.dropdown-menu a')).get(1).click();
        browser.driver.sleep(1000);
        expect(element.all(by.css('.btn')).count()).toEqual(9);

    }, 61000);


    it('Update User Info and Save', function() {
      // Check the Update User Info checkbox to allow editing
      element(by.id('myCheckValue')).click();
      browser.driver.sleep(1000);

      element(by.id('firstName')).clear();
      element(by.id('firstName')).sendKeys('ProtractorFirstName');

      element(by.id('lastName')).clear();
      element(by.id('lastName')).sendKeys('ProtractorLastName');

      element(by.id('email')).clear();
      element(by.id('email')).sendKeys('dont@sendmail.com');

      element(by.id('organization')).clear();
      element(by.id('organization')).sendKeys('Department of Redundancy Department', protractor.Key.ENTER);
      browser.driver.sleep(4000);

      var uName = element.all(by.css('.ng-binding.ng-scope')).get(2);
      expect(uName.getText()).toBe('ProtractorFirstName ');

    }, 40000);


    it('Revert the changes', function () {
      browser.refresh();
      browser.driver.sleep(5000);

      // Click on User Name
      element.all(by.css('.nav.navbar-nav.navbar-right')).get(0).click();
      browser.driver.sleep(2000);

      // Click on 2nd menu item which is "User Profile"
      element.all(by.css('.dropdown-menu a')).get(1).click();
      browser.driver.sleep(1500);
      expect(element.all(by.css('.btn')).count()).toEqual(9);

      // Check the Update User Info checkbox to allow editing
      element(by.id('myCheckValue')).click();
      browser.driver.sleep(1000);

      element(by.id('firstName')).clear();
      element(by.id('firstName')).sendKeys('admin');

      element(by.id('lastName')).clear();
      element(by.id('lastName')).sendKeys('user');

      element(by.id('email')).clear();
      element(by.id('email')).sendKeys('blaine.esplin@sdl.usu.edu');

      element(by.id('organization')).clear();
      element(by.id('organization')).sendKeys('Space Dynamics Laboratory, Utah State University', protractor.Key.ENTER);
      browser.driver.sleep(4000);

      var u2Name = element.all(by.css('.ng-binding.ng-scope')).get(2);
      expect(u2Name.getText()).toBe('admin ');

    }, 25000);



  it('Click on UserName then Watches', function() {
        // Sets the .get(x) indexes back, otherwise they become 'dirty' and off!
        browser.refresh();
        browser.driver.sleep(4000);
        element.all(by.css('.nav.navbar-nav.navbar-right')).get(0).click();
        //element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        element.all(by.css('.dropdown-menu a')).get(2).click();
        browser.driver.sleep(6000);
        expect(element.all(by.css('.btn')).count()).toEqual(7);
        element.all(by.css('.close')).get(0).click();
        browser.driver.sleep(1000);
    }, 25000);

    it('Click on UserName then Component Reviews', function() {
        browser.refresh();
        browser.driver.sleep(4000);
        element.all(by.css('.nav.navbar-nav.navbar-right')).get(0).click();
        //element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        element.all(by.css('.dropdown-menu a')).get(3).click();
        browser.driver.sleep(1800);
        expect(element.all(by.css('.btn')).count()).toEqual(7);
        element.all(by.css('.close')).get(0).click();
        browser.driver.sleep(5000);
    }, 25000);

});
