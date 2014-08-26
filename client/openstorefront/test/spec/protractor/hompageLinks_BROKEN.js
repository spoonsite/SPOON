describe('homepageLinks_ Click on the listing links on the Storefront home page', function() {
    it('the pages load and the links are not broken.', function() {

        // TODO:  Figure out how to dynamically get the # of links
        //console.log(element.all(by.css('.listing_short_title_text')).count());

        // Quit working on dynamic idAM landing page (see Jonathan?) 31 Jul commenting out for now

        for (var i=0; i < 4; i++) {
            browser.get('http://di2e.github.io/openstorefront', 25000);
            element.all(by.css('.listing_short_title_text')).get(i).click();
            expect(browser.getTitle()).toEqual('DI2E Storefront', 12000);
        }


    });

    /* 04 Aug 2014
       Error:  Error while waiting for Protractor to sync with the page: {}
       NOTE:  searchDetails_spec.js works fine but this page has a tricky "http://a

     */

    // from stackoverflow (http://stackoverflow.com/questions/21251210/check-whether-anchor-have-href-test-case-using-protractor)
    it('all tags have a reference (are not null between the tags)', function() {
        element.all(by.xpath('/a')).then(function(links) {
            for (var i = 0; i < links.length; i++) {
                expect(links[i].getAttribute('href')).not.toBeNull();
            }
        });
    });

});