describe('Search ALL then filter on "Sort By:"', function() {
    it('Select Sort By: "Name (Z-A)" and return Vega as the first search result', function() {
        // Search on ALL entries (null search term)
        browser.get('http://di2e.github.io/openstorefront');
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

        // Verify some results are returned, doesn't matter at this point how many
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);

        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);

        // VERIFY it is sorted Z - A
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var ztoa = element(by.binding('item.description'));
        expect(ztoa.getText()).toBe('Vega is a 3D map widget with support for high-resolution imagery in various formats including WMS, WFS, and ArcGIS. Vega also supports 3-dimensional terrain, and time-based data and has tools for drawing shapes and points and importing/exporting data. ...');
    });

    it('Select Sort By: "Last Update (newest)" and return IdAM as the first search result', function() {
        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);

        // VERIFY it is sorted correctly
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var newest = element(by.binding('item.description'));
        expect(newest.getText()).toBe('Identity and Access Management Article.....');
    });

    it('Select Sort By: "Last Update (oldest)" and return "Common Map API Javascript Library" as the first search result', function() {
        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);

        // VERIFY it is sorted correctly
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var oldest = element(by.binding('item.description'));
        expect(oldest.getText()).toBe('Core Map API is a library of JavaScript (JS) functions that hide the underlying Common Map Widget API so that developers only have to include the JS library and call the appropriate JS functions that way they are kept away from managing or interacting directly with the channels. Purpose/Goal of the cpce-map-api-jsDocs.zip?api=v2 ...');
    });

    it('Select Sort By: "Name (A-Z)" and return "Common Map API Javascript Library" as the first search result', function() {
        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.UP);
        element(by.model('orderProp')).sendKeys(protractor.Key.UP);
        element(by.model('orderProp')).sendKeys(protractor.Key.UP);

        // VERIFY it is sorted correctly
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var atoz = element(by.binding('item.description'));
        expect(atoz.getText()).toBe('The Central Authentication Service (CAS) is a single sign-on protocol for the web. Its purpose is to permit a user to access multiple applications while providing their credentials (such as userid and password) only once. It also allows web applications to authenticate users without gaining access to ...');
    });
});