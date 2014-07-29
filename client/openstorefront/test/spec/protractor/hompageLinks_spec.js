describe('When I click on the listing links on the Storefront home page', function() {
    it('loads the pages and the links are not broken.', function() {

        // TODO:  Figure out how to dynamically get the # of links
        //console.log(element.all(by.css('.listing_short_title_text')).count());

        // loop
        for (var i=0; i <5; i++) {
            browser.get('http://di2e.github.io/openstorefront');
            element.all(by.css('.listing_short_title_text')).get(i).click();
            expect(browser.getTitle()).toEqual('DI2E Storefront');
        }

    });

    // from stackoverflow (http://http://stackoverflow.com/questions/21251210/check-whether-anchor-have-href-test-case-using-protractor)
    it('all a links have hrefs', function() {
        element.all(by.xpath('/a')).then(function(links) {
            for (var i = 0; i < links.length; i++) {
                expect(links[i].getAttribute('href')).not.toBeNull();
            }
        });
    });

});