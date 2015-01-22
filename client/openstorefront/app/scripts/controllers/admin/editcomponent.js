'use strict';

/*global triggerError, removeError, isEmpty*/

app.controller('AdminEditcomponentCtrl', ['$scope', 'business', '$timeout', function ($scope, Business, $timeout) {

    $scope.predicate = [];
    $scope.reverse = [];
    $scope.statusFilterOptions = [
      {code: 'A', desc: 'Active'},
      {code: 'I', desc: 'Inactive'}
    ];
    $scope.queryFilter = angular.copy(utils.queryFilter);    
    $scope.queryFilter.max = 500;
    $scope.queryFilter.sortField = 'name';
    $scope.maxPageNumber = 1;
    $scope.pageNumber = 1;    
    $scope.components = [];
    $scope.filteredComponents = [];
    $scope.allComponents = [];
    $scope.componentFilter = {};
    $scope.componentFilter.search = '';
    $scope.componentFilter.status = $scope.statusFilterOptions[0];


    $scope.firstPage = function () {
      $scope.pageNumber = 1;
      $scope.refreshComponents();
    };

    $scope.lastPage = function () {
      $scope.pageNumber = $scope.maxPageNumber;
      $scope.refreshComponents();
    };

    $scope.prevPage = function () {
      $scope.pageNumber = $scope.pageNumber - 1;
      if ($scope.pageNumber < 1) {
        $scope.pageNumber = 1;
      }
      $scope.refreshComponents();
    };

    $scope.nextPage = function () {
      $scope.pageNumber = $scope.pageNumber + 1;
      if ($scope.pageNumber > $scope.maxPageNumber) {
        $scope.pageNumber = $scope.maxPageNumber;
      }
      $scope.refreshComponents();
    };

    $scope.setPageSize = function () {
      $scope.pageNumber = 1;
      $scope.refreshComponents();
    };

    $scope.jumpPage = function () {
      if ($scope.pageNumber > $scope.maxPageNumber) {
        $scope.pageNumber = $scope.maxPageNumber;
      }
      $scope.refreshComponents();
    };

    $scope.setPredicate = function (predicate, table) {
      if ($scope.predicate[table] === predicate) {
        $scope.reverse[table] = !$scope.reverse[table];
      } else {
        $scope.predicate[table] = predicate;
        $scope.reverse[table] = false;
      }
    };


    $scope.refreshComponents = function () {
      $scope.$emit('$TRIGGERLOAD', 'ticketLoader');
      $scope.queryFilter.status = $scope.componentFilter.status.code;
      $scope.queryFilter.offset = ($scope.pageNumber - 1) * $scope.queryFilter.max;
      Business.componentservice.getFilteredComponents($scope.queryFilter).then(function (results) {
        if (results) {
          $scope.componentFilter.search = '';
          $scope.components = results;
          $scope.filteredComponents = $scope.components.components; 
          $scope.allComponents = $scope.components.components;

          $scope.maxPageNumber = Math.ceil($scope.components.totalNumber / $scope.queryFilter.max);
        }
        $scope.$emit('$TRIGGERUNLOAD', 'ticketLoader');
      });
    };
    $scope.refreshComponents();

    $scope.filterComponentResults = function(){
      if ($scope.componentFilter.search === '') {
        $scope.filteredComponents = $scope.allComponents;
      } else {      
        $scope.filteredComponents = _.filter($scope.allComponents, function (item){
          return item.component.name.toLowerCase().indexOf($scope.componentFilter.search.toLowerCase()) === 0;
        });
      }
    };




//  $scope._scopename = 'editComponent?';
//  $scope.base = {};
//  $scope.subComponent = {};
//  $scope.relatedComponent = {};
//  $scope.currentDependency = {};
//  $scope.requiredAttributes = [];
//  $scope.item = null;
//  $scope.search = null;
//  $scope.componentListKey = null;
//  $scope.found = false;
//  $scope.nav = {
//    'bars': [
//    {
//      'title': 'General',
//      'location': 'views/admin/component/generalform.html'
//    }, {
//      'title': 'Evaluation',
//      'location': 'views/admin/component/evaluationform.html'
//    }, {
//      'title': 'Attributes/Vitals',
//      'location': 'views/admin/component/componentvitalsform.html'
//    }, {
//      'title': 'Artifacts',
//      'location': 'views/admin/component/componentartifactsform.html'
//    }, {
//      'title': 'Contacts',
//      'location': 'views/admin/component/pocform.html'
//    }, {
//      'title': 'Reviews/Tags/Q&A',
//      'location': 'views/admin/component/reviews.html'
//    }],
//    'current': ''
//  };
//
//  Business.getFilters().then(function(result){
//    if (result) {
//      $scope.filters = angular.copy(result);
//      _.each($scope.filters, function(filter){
//        if (filter.requiredFlg) {
//          $scope.requiredAttributes[filter.description] = filter.codes[0];
//        }
//      });
//    }
//  });
//
//
//  /***************************************************************
//  * Dependency functions
//  ***************************************************************/
//  $scope.resetForm = function(){
//    $scope.found = false;
//  };
//
//  $scope.editDependency = function($index) {
//    $scope.editDep = true;
//    $scope.depIndex = $index;
//    $scope.currentDependency = angular.copy($scope.item.dependencies[$index]);
//  };
//
//  $scope.deleteDependency = function(index) {
//    $scope.item.dependencies.splice(index, 1);
//    $scope.currentDependency = {};
//  };
//
//  $scope.resetDependency = function() {
//    $scope.currentDependency = $scope.item.dependencies[$scope.depIndex];
//  };
//
//  $scope.clearDependency = function() {
//    $scope.currentDependency.dependency = null;
//    $scope.currentDependency.version = null;
//    $scope.currentDependency.dependencyReferenceLink = null;
//    $scope.currentDependency.comment = null;
//  };
//
//  var checkDependency = function() {
//    var passed = true;
//    if (!($scope.currentDependency.comment && $scope.currentDependency.comment !== '')){
//      passed = false;
//      triggerError({
//        'errors': [
//        {'dependencyComment': 'Dependencies must have a comment to describe them.'}
//        ]
//      });
//    } else {
//      removeError('dependencyComment');
//    }
//    if (!($scope.currentDependency.dependency && $scope.currentDependency.dependency !== '')){
//      passed = false;
//      triggerError({
//        'errors': [
//        {'dependency': 'Dependencies must have a title.'}
//        ]
//      });
//    } else {
//      removeError('dependency');
//    }
//    return passed;
//  };
//
//  $scope.saveDependency = function() {
//    if (checkDependency()) {
//      $scope.editDep = false;
//      $scope.item.dependencies[$scope.depIndex] = $scope.currentDependency;
//      $scope.currentDependency = {};
//    }
//  };
//
//  $scope.addDependency = function() {
//    if (!isEmpty($scope.currentDependency)) {
//      if (checkDependency()) {
//        $scope.item.dependencies.push($scope.currentDependency);
//        $scope.currentDependency = {};
//      }
//    }
//  };
//
//  $scope.doComponentSearch = function() {
//    if (typeof $scope.componentListKey === 'object' && $scope.componentListKey) {
//      $scope.item = $scope.componentListKey;
//      $scope.found = true;
//      $scope.search = null;
//      $timeout(function(){
//        $scope.nav.current = 'General';
//      });
//
//    } else if ($scope.componentListKey){
//      Business.componentservice.doSearch('search', $scope.componentListKey).then(function(result){
//        if (result && result.data) {
//          $scope.search = result.data;
//        } else {
//          $scope.search = [];
//        }
//      });
//    }
//  };
//
//  $scope.setItem = function(id) {
//    Business.componentservice.getComponentDetails(id).then(function(result){
//      if (result) {
//        $scope.item = result;
//        $scope.found = true;
//        $timeout(function(){
//          $scope.nav.current = 'General';
//        });
//      }
//    });
//  };
//
//  $scope.deleteSubComponent = function(id) {
//    var index = _.find($scope.item.subComponents, {'componentId': id});
//    var delIndex = $scope.item.subComponents.indexOf(index);
//    // console.log('delindex', delIndex);
//    
//    if (delIndex > -1) {
//      $scope.item.subComponents.splice(delIndex, 1);
//    }
//  };
//  
//  $scope.deleteRelatedComponent = function(id) {
//    var index = _.find($scope.item.relatedComponents, {'componentId': id});
//    var delIndex = $scope.item.relatedComponents.indexOf(index);
//    // console.log('delindex', delIndex);
//    
//    if (delIndex > -1) {
//      $scope.item.relatedComponents.splice(delIndex, 1);
//    }
//  };
//
//  $scope.savebase = function() {
//    // console.log('scope.item', $scope.item);
//  };
//
//  $scope.$watch('subComponent', function(value) {
//    if (value) {
//      if (typeof value === 'object' && value.subComponent && value.subComponent.componentId && value.subComponent.name) {
//        var found = _.some($scope.item.subComponents, function(thing){
//          return thing.componentId === value.subComponent.componentId;
//        });
//        if (!found && value.subComponent.componentId !== $scope.item.componentId) {
//          $scope.item.subComponents.push(value.subComponent);
//        }
//        $scope.subComponent.subComponent = null;
//      }
//    }
//  }, true);
//  $scope.$watch('relatedComponent', function(value) {
//    if (value) {
//      if (typeof value === 'object' && value.relatedComponent && value.relatedComponent.componentId && value.relatedComponent.name) {
//        var found = _.some($scope.item.relatedComponents, function(thing){
//          return thing.componentId === value.relatedComponent.componentId;
//        });
//        if (!found && value.relatedComponent.componentId !== $scope.item.componentId) {
//          $scope.item.relatedComponents.push(value.relatedComponent);
//        }
//        $scope.relatedComponent.relatedComponent = null;
//      }
//    }
//  }, true);
//
//  $scope.$watch(function() {
//    return $scope.item? $scope.item.parentComponent: null;
//  }, function(value){
//    if (value) {
//      // console.log('changes', $scope.item.parentComponent);
//      if (value && value.componentId && value.name) {
//        $scope.parentSuccess = 'good';
//      } else {
//        $scope.parentSuccess = 'bad';
//      }
//    } else {
//      $scope.parentSuccess = 'bad';
//    }
//  }, true);
//
//  $scope.$watch('componentListKey', function(value) { /*jshint unused:false*/
//    $scope.doComponentSearch();
//  });
//
//  $scope.$on('$typeahead.select', function(event, data){
//    if (data.triggerFlg) {
//      if (data.componentId && data.name) {
//        if (data.triggerFlg === 'parent') {
//          delete data.triggerFlg;
//          if ($scope.item.componentId !== data.componentId) {
//            $scope.item.parentComponent = data;
//          } else {
//            $scope.parentSuccess = false;
//            $scope.item.parentComponent = null;
//          }
//        } else if (data.triggerFlg === 'sub'){
//          delete data.triggerFlg;
//          $scope.subComponent.subComponent = data;
//        } else {
//          delete data.triggerFlg;
//          $scope.relatedComponent.relatedComponent = data;
//        }
//        $scope.$apply();
//      } else if (data === '') {
//        if (data.triggerFlg === 'parent') {
//          $scope.item.parentComponent = null;
//        } else {
//          $scope.subComponent.subComponent = null;
//        }
//      }
//    }
//  });


}]);
