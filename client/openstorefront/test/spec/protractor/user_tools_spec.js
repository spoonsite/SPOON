describe('User logged in drop-down', function() {
    it('Click on Watches, then User Profile tab', function () {
        // Search on ALL entries (null search term)
        browser.get('http://di2e.github.io/openstorefront');

        // Click on User Name then middle button if logged in.  Tab to other tabs
        element.all(by.css('.dropdown-toggle.ng-binding')).get(0).click();

        // Click User Profile | Watches | Component Reviews  // .dropdown-menu > li:nth-child(5) > a:nth-child(1)
        // Opens the box with above tabs
        element.all(by.css('.dropdown-menu')).get(0).click();

        // Click on User Profile tab
        element.all(by.css('.ng-scope.firstNav')).get(0).click();
        expect(element(by.id('myCheckValue')));
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

/*    it('Click on Watches tab', function() {
        element.all(by.css('.ng-scope.active');
        expect(element.all(by.css('.panel-title.watch-content-title')));
        expect(element.all(by.css('.abcdefg')));
        browser.driver.sleep(3000);
    });


    it('Click on Component Reviews tab', function() {
        element.all(by.css('.ng-scope')).get(2).click();
        // expect
        browser.driver.sleep(3000);

    });
*/


});