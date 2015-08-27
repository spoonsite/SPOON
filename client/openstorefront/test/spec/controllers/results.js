 'use strict';

 describe('Controller: ResultsCtrl', function () {

   // load the controller's module
  beforeEach(module('openstorefrontApp'));

   var ResultsCtrl,
    scope;

   // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ResultsCtrl = $controller('ResultsCtrl', {
      $scope: scope
    });
  }));

  it('should have the correct initializations', function () {
    expect(scope._scopename).toEqual('results');
    expect(scope.orderProp).toEqual('');
    expect(scope.query).toEqual('');
    expect(scope.lastUsed.toString()).toEqual(new Date().toString());
    expect(scope.modal).toEqual({isLanding: false});
    expect(scope.details).toEqual({details: null});
    expect(scope.data).toEqual({});
    expect(scope.isPage1).toEqual(true);
    expect(scope.showSearch).toEqual(false);
    expect(scope.showDetails).toEqual(false);
    expect(scope.showMessage).toEqual(false);
    expect(scope.modal.isLanding).toEqual(false);
    expect(scope.single).toEqual(false);
    expect(scope.isArticle).toEqual(false);
    expect(scope.searchCode).toEqual('all');
    expect(scope.filteredTotal).toEqual(null);
    expect(scope.searchTitle).toEqual(null);
    expect(scope.searchDescription).toEqual(null);
    expect(scope.details.details).toEqual(null);
    expect(scope.typeahead).toEqual(null);
    expect(scope.searchGroup).toEqual([{key:'search', code:'all'}]);
    expect(scope.searchKey).toEqual('search');
    expect(scope.filters).toEqual(null);
    expect(scope.resetFilters).toEqual(null);
    expect(scope.total).toEqual(null);
    expect(scope.ratingsFilter).toEqual(0);
    expect(scope.rowsPerPage).toEqual(200);
    expect(scope.pageNumber).toEqual(1);
    expect(scope.maxPageNumber).toEqual(1);
    expect(scope.showBreadCrumbs).toEqual(false);
  });

   it('should validate properly using spies', function() {
     spyOn(scope, 'setupTagList');
     spyOn(scope, 'setSelectedTab');
     spyOn(scope, 'isItAnArticle');
     spyOn(scope, 'getNumThings');
     spyOn(scope, 'tabClass');
     spyOn(scope, 'checkTagsList');
     spyOn(scope, 'reAdjust');
     spyOn(scope, 'setupData');
     spyOn(scope, 'initializeData');
     spyOn(scope, 'resetSearch');
     spyOn(scope, 'toggleclass');
     spyOn(scope, 'doButtonOpen');
     spyOn(scope, 'doButtonClose');
     spyOn(scope, 'toggleChecks');
     spyOn(scope, 'updateDetails');
     spyOn(scope, 'goToFullPage');
     spyOn(scope, 'goToCompare');
     spyOn(scope, 'clearFilters');
     spyOn(scope, 'checkFilters');

     scope.setupTagList();
     scope.setSelectedTab();
     scope.isItAnArticle('articleObject?');
     scope.getNumThings('articleObject?');
     scope.tabClass();
     scope.checkTagsList('blah','blah','blah');
     scope.reAdjust('key');
     scope.setupData();
     scope.initializeData('key');
     scope.resetSearch();
     scope.toggleclass(1, 'class');
     scope.doButtonOpen();
     scope.doButtonClose();
     scope.toggleChecks('collection','override');
     scope.updateDetails(99, 'article');
     scope.goToFullPage(5);
     scope.goToCompare();
     scope.clearFilters();
     scope.checkFilters();

     expect(scope.setupTagList).toHaveBeenCalled();
     expect(scope.setSelectedTab).toHaveBeenCalled();
     expect(scope.isItAnArticle).toHaveBeenCalledWith('articleObject?');
     expect(scope.getNumThings).toHaveBeenCalledWith('articleObject?');
     expect(scope.tabClass).toHaveBeenCalled();
     expect(scope.checkTagsList).toHaveBeenCalledWith('blah','blah','blah');
     expect(scope.reAdjust).toHaveBeenCalledWith('key');
     expect(scope.setupData).toHaveBeenCalled();
     expect(scope.initializeData).toHaveBeenCalledWith('key');
     expect(scope.resetSearch).toHaveBeenCalled();
     expect(scope.toggleclass).toHaveBeenCalledWith(1, 'class');
     expect(scope.doButtonOpen).toHaveBeenCalled();
     expect(scope.doButtonClose).toHaveBeenCalled();
     expect(scope.toggleChecks).toHaveBeenCalledWith('collection', 'override');
     expect(scope.updateDetails).toHaveBeenCalledWith(99, 'article');
     expect(scope.goToFullPage).toHaveBeenCalledWith(5);
     expect(scope.goToCompare).toHaveBeenCalled();
     expect(scope.clearFilters).toHaveBeenCalled();
     expect(scope.checkFilters).toHaveBeenCalled();
   });
   alert('Controller: ResultsCtrl; should have the correct initializations, spies = PASS (49 expects, 19 spies)');
});