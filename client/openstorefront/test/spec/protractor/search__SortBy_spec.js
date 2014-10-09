describe('search__SortBy', function() {
    it('Name Z-A returns Vega as the first search result', function() {
        // Search on ALL entries (null search term)
        browser.get(theSite, 8000);
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();
        browser.driver.sleep(10000);

        // Verify some results are returned, doesn't matter at this point how many
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);

        // Select sort by drop down
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        element(by.model('orderProp')).sendKeys(protractor.Key.DOWN);
        // Add tab for Firefox as it needs to loose focus in order to dynamically sort (Chrome is automatic without the tabbing)
        element(by.model('orderProp')).sendKeys(protractor.Key.TAB);

        // VERIFY it is sorted Z - A
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
        var ztoa = element(by.binding('item.description'));
        expect(ztoa.getText()).toBe('Vega is a 3D map widget with support for high-resolution imagery in various formats including WMS, WFS, and ArcGIS. Vega also supports 3-dimensional terrain, and time-based data and has tools for drawing shapes and points and importing/exporting data.');
    }, 20000);

    it('Last Update newest returns DI2E SvcV-4 Alignment-1.2.1 IAM article as the first search result', function() {

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
        expect(newest.getText()).toBe('Definition: Identity and Access Management (IdAM) defines the set of services that manage permissions required to access each resource. Description: IdAM includes services that provide criteria used in access decisions and the rules and requirements assessing each request against those criteria. Resources may include applications, ...');
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

    it('Name A-Z returns Common Map API Javascript Library as the first search result', function() {
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
        expect(atoz.getText()).toBe('Definition: Identity and Access Management (IdAM) defines the set of services that manage permissions required to access each resource. Description: IdAM includes services that provide criteria used in access decisions and the rules and requirements assessing each request against those criteria. Resources may include applications, ...');
    }, 20000);
});