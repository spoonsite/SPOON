describe('searchAll_Search entire database', function() {

    // *** varies, depending on what is in the sample database ***
    var totalResults = 59;
    
    it('returns ' + totalResults + ' expected current search results', function() {
        // Open the main site
        browser.get(theSite, 4000);

        // Search on ALL entries (null search term)
        element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

        // Wait for it to sync, a bit slower on the VPN
        browser.driver.sleep(10000);

        // Should return 58 results
        expect(element.all(by.repeater('item in data')).count()).toEqual(totalResults);
    });

/*    it('for a LONG string with special characters, pseudo search returns zero results', function() {
        // Open the main site
        browser.get(theSite);

        // Search on a LONG, special character string
        var bigEntry = '€β™±≠∞µ∑Ω①↖≤ÿñà—””…<HTML>INSERT INTO<table></table>asdljasdoiewrueowoiupewriuocvxnewrq423523#$%&^$#%@#$^#%$^@#$@!$#%@#^@#$^%#$%%$@#645987@#$$@#~~~```/???/\/\|{}{[][][;';
        element(by.id('mainSearchBar')).sendKeys(bigEntry, protractor.Key.ENTER);

        // Should return 0 results, and NOT time out!
        expect(element.all(by.repeater('item in data')).count()).toEqual(0);

    });
*/

});
