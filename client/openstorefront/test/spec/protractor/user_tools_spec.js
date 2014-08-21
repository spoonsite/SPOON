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
        expect(element(by.id('myCheckValue')).isDisplayed());
    });

   it('Update User Info and Save', function() {
        element(by.id('myCheckValue')).sendKeys(protractor.Key.SPACEBAR);

   });

   it('Revert the changes', function () {

   });

});