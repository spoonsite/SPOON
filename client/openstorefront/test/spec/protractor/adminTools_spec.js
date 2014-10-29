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


    it('Click left menus of Admin Tools and check for resulting right pane data', function() {
        // TODO:  Fill this in when coding is complete, look for more specific info on "expect"

        // Manage Attributes (highest level no sub-trees)
        element.all(by.css('.indented')).get(3).click();
        expect(element.all(by.css('.ngHeaderContainer')).count()).toEqual(1);

        // Manage Lookups (highest level no sub-trees)
        element.all(by.css('.indented')).get(5).click();
        expect(element.all(by.css('.ng-scope')).count()).toEqual(33);

        // Manage Components
        element.all(by.css('.indented')).get(7).click();
        expect(element.all(by.css('.ng-scope')).count()).toEqual(37);

        // Manage Branding
        element.all(by.css('.indented')).get(8).click();
        expect(element.all(by.css('.ng-scope')).count()).toEqual(30);

    });

});

