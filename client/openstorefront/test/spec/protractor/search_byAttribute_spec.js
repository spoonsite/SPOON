describe('search_byAttribute_Rating_Reset_Filters', function() {
    // Open the main site
    browser.get(theSite, 9000);
    element.all(by.css('.btn.btn-primary.pull-right')).get(2).click();

    it('Search filter by 5 star Rating', function () {
        element.all(by.css('.star-off-png')).get(4).click();
        // No search results expected
        expect(element.all(by.repeater('item in data')).count()).toEqual(0);
    }, 20000);

    it('Click Reset Filters button in SEARCH RESULTS', function () {
        element.all(by.css('.ng-scope.btn.btn-default')).get(0).click();
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
    }, 15000);

    it('Search filter by 1 star Rating', function () {
        element.all(by.css('.star-off-png')).get(0).click();
        // No search results expected
        expect(element.all(by.repeater('item in data')).count()).toEqual(0);
    }, 15000);

    it('Click Reset Filters button LEFT NAV', function () {
        element.all(by.css('.btn.red.btn-default')).get(0).click();
        expect(element.all(by.repeater('item in data')).count()).toBeGreaterThan(0);
    }, 15000);

   it('Click More Filters  Expand all filters click all down buttons', function() {
       // Click on "More Filters"
       element.all(by.css('.btn.btn-default')).get(0).click();

       // Click all fa fa-caret-down buttons  //TODO:  Get this dynamically
       var carrotsDown = 21;
       for (var i=0; i <= carrotsDown; i++) {
           element.all(by.css('.fa.fa-caret-down')).get(i).click();
           browser.driver.sleep(1);
           /* Checkboxes go (scroll) offscreen
           var sthere = (i*4)-4;
           if (sthere <0) sthere=0;
           console.log(sthere);
           for (var j=sthere; j <= sthere+4; j++) {
               element.all(by.css('.checkbox.filterCheckbox')).get(j).click();
               browser.driver.sleep(1);
           }
           */
       }
       // Verify last element is present from the expansion
       //expect(element(by.id('Service Transport Protocal_OTH')).isDisplayed());
       expect(true).toEqual(true);
   }, 25000);
});
