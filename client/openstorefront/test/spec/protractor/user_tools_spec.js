describe('user-tools_User logged in drop-down', function() {
    browser.get('http://di2e.github.io/openstorefront');

    it('Click on UserName then User Profile', function () {
        // Click on User Name
        element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        // Click on 2nd menu item which is "User Profile"
        element.all(by.css('.dropdown-menu a')).get(1).click();
        expect(element(by.id('myCheckValue')));
        // Close the window
        element.all(by.css('.close')).get(0).click();
        browser.driver.sleep(1000);
    });

    it('Click on UserName then Watches', function() {
        element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        element.all(by.css('.dropdown-menu a')).get(2).click();
        // TODO:  Expect
        browser.driver.sleep(1000);
        element.all(by.css('close')).get(0).click();
        browser.driver.sleep(1000);
    });

    it('Click on UserName then Component Reviews', function() {
        element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        element.all(by.css('.dropdown-menu a')).get(3).click();
        // TODO:  Expect
        browser.driver.sleep(1000);
        element.all(by.css('close')).get(0).click();
        browser.driver.sleep(1000);
    });

/*  CURRENTLY YOU CANNOT SAVE AN UPDATED USER PROFILE, WAIT UNTIL SERVER SIDE IS WORKING?
   it('Update User Info and Save', function() {
        // Check the Update User Info checkbox to allow editing
        element(by.id('myCheckValue')).sendKeys(protractor.Key.SPACEBAR);
        browser.driver.sleep(1000);

        element(by.id('firstname')).sendKeys('ProtractorFirstName');
        element(by.id('lastname')).sendKeys('ProtractorLastName');
        element.all(by.css('.btn.btn-primary')).get(0).click();
        element.all(by.css('.btn.btn-default')).get(0).click();

       browser.driver.sleep(6500);

   });

   it('Revert the changes', function () {

   });
UPDATE LATER
*/

});