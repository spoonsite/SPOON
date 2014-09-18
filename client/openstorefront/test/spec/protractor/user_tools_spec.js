describe('user-tools_User logged in drop-down', function() {
    it('Click on UserName then User Profile', function () {
        browser.get(theSite);
        // Click on User Name
        element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        // Click on 2nd menu item which is "User Profile"
        element.all(by.css('.dropdown-menu a')).get(1).click();
        expect(element.all(by.css('.btn')).count()).toEqual(9);
        // Close the window
        element.all(by.css('.close')).get(0).click();
        browser.driver.sleep(1000);
    });

    it('Click on UserName then Watches', function() {
        // Sets the .get(x) indexes back, otherwise they become 'dirty' and off!
        browser.refresh();
        element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        element.all(by.css('.dropdown-menu a')).get(2).click();
        expect(element.all(by.css('.btn')).count()).toEqual(17);
        element.all(by.css('.close')).get(0).click();
        browser.driver.sleep(1000);
    });

    it('Click on UserName then Component Reviews', function() {
        browser.refresh();
        element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();
        element.all(by.css('.dropdown-menu a')).get(3).click();
        expect(element.all(by.css('.btn')).count()).toEqual(37);
        element.all(by.css('.close')).get(0).click();
        browser.driver.sleep(1000);
    });

    /* // DOWN ARROW in Component Reviews
    it('Click buttons in Component Reviews', function() {
        // Click to expand
        element.all(by.css(.fa.fa-arrow-down')).get(0).click();
        // Click EDIT

    });
    */

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