'use strict';

app.controller('AdminEditcomponentCtrl', ['$scope', 'business', function ($scope, Business) {

  $scope._scopename = 'editComponent?';
  $scope.componentListKey = null;
  $scope.search = null;
  $scope.item = null;
  $scope.found = false;
  $scope.nav = {
    'bars': [
    {
      'title': 'General',
      'location': 'views/admin/component/generalform.html'
    }, {
      'title': 'Evaluation',
      'location': 'views/admin/component/evaluationform.html'
    }, {
      'title': 'Attributes/Vitals',
      'location': 'views/admin/component/componentvitalsform.html'
    }, {
      'title': 'Artifacts',
      'location': 'views/admin/component/componentartifactsform.html'
    }, {
      'title': 'Contacts',
      'location': 'views/admin/component/pocform.html'
    }, {
      'title': 'Reviews/Tags/Q&A',
      'location': 'views/admin/component/reviews.html'
    }],
    'current': 'General'
  };

  $scope.resetForm = function(){
    $scope.found = false;
  };

  $scope.doComponentSearch = function() {
    if (typeof $scope.componentListKey === 'object') {
      console.log('object ', $scope.componentListKey);
      $scope.item = $scope.componentListKey;
      $scope.found = true;
      $scope.search = null;
      $scope.$apply();
      console.log('$scope.item', $scope.item);
      
    } else {
      console.log('String ', $scope.componentListKey);
      Business.componentservice.doSearch('search', $scope.componentListKey).then(function(result){
        if (result) {
          $scope.search = result;
        }
      });
    }
  };

  $scope.setItem = function(id) {
    Business.componentservice.getComponentDetails(id).then(function(result){
      if (result) {
        $scope.item = result;
        $scope.found = true;
        console.log('$scope.item', $scope.item);
        
      }
    });
  };

  /***************************************************************
  * Catch the enter/select event here
  ***************************************************************/
  $scope.$on('$typeahead.select', function(event, value) {
    $scope.componentListKey = value;
    $scope.doComponentSearch();
    $('#editComponent').blur();
  });
  

}]);
