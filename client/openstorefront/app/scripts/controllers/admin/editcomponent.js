'use strict';

/*global triggerError, removeError, isEmpty*/

app.controller('AdminEditcomponentCtrl', ['$scope', 'business', '$timeout', function ($scope, Business, $timeout) {

  $scope._scopename = 'editComponent?';
  $scope.componentListKey = null;
  $scope.subComponent = {};
  $scope.relatedComponent = {};
  $scope.search = null;
  $scope.item = null;
  $scope.found = false;
  $scope.currentDependency = {};
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
    'current': ''
  };

  $scope.requiredAttributes = [];
  Business.getFilters().then(function(result){
    if (result) {
      $scope.filters = result;
      _.each($scope.filters, function(filter){
        if (filter.requiredFlg) {
          $scope.requiredAttributes[filter.description] = filter.codes[0];
        }
      });
    }
  });

  $scope.base = {};

  $scope.resetForm = function(){
    $scope.found = false;
  };


  /***************************************************************
  * Dependency functions
  ***************************************************************/
  $scope.editDependency = function($index) {
    $scope.editDep = true;
    $scope.depIndex = $index;
    $scope.currentDependency = angular.copy($scope.item.dependencies[$index]);
  };

  $scope.deleteDependency = function(index) {
    $scope.item.dependencies.splice(index, 1);
    $scope.currentDependency = {};
  };

  $scope.resetDependency = function() {
    $scope.currentDependency = $scope.item.dependencies[$scope.depIndex];
  };

  $scope.clearDependency = function() {
    $scope.currentDependency.dependency = null;
    $scope.currentDependency.version = null;
    $scope.currentDependency.dependencyReferenceLink = null;
    $scope.currentDependency.comment = null;
  };

  var checkDependency = function() {
    var passed = true;
    if (!($scope.currentDependency.comment && $scope.currentDependency.comment !== '')){
      passed = false;
      triggerError({
        'errors': [
        {'dependencyComment': 'Dependencies must have a comment to describe them.'}
        ]
      });
    } else {
      removeError('dependencyComment');
    }
    if (!($scope.currentDependency.dependency && $scope.currentDependency.dependency !== '')){
      passed = false;
      triggerError({
        'errors': [
        {'dependency': 'Dependencies must have a title.'}
        ]
      });
    } else {
      removeError('dependency');
    }
    return passed;
  };



  $scope.saveDependency = function() {
    if (checkDependency()) {
      $scope.editDep = false;
      $scope.item.dependencies[$scope.depIndex] = $scope.currentDependency;
      $scope.currentDependency = {};
    }
  };

  $scope.addDependency = function() {
    if (!isEmpty($scope.currentDependency)) {
      if (checkDependency()) {
        $scope.item.dependencies.push($scope.currentDependency);
        $scope.currentDependency = {};
      }
    }
  };


  $scope.doComponentSearch = function() {
    if (typeof $scope.componentListKey === 'object' && $scope.componentListKey) {
      $scope.item = $scope.componentListKey;
      $scope.found = true;
      $scope.search = null;
      $timeout(function(){
        $scope.nav.current = 'General';
      });

    } else if ($scope.componentListKey){
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
        $timeout(function(){
          $scope.nav.current = 'General';
        });
      }
    });
  };

  $scope.deleteSubComponent = function(id) {
    var index = _.find($scope.item.subComponents, {'componentId': id});
    var delIndex = $scope.item.subComponents.indexOf(index);
    console.log('delindex', delIndex);
    
    if (delIndex > -1) {
      $scope.item.subComponents.splice(delIndex, 1);
    }
  };
  $scope.deleteRelatedComponent = function(id) {
    var index = _.find($scope.item.relatedComponents, {'componentId': id});
    var delIndex = $scope.item.relatedComponents.indexOf(index);
    console.log('delindex', delIndex);
    
    if (delIndex > -1) {
      $scope.item.relatedComponents.splice(delIndex, 1);
    }
  };

  $scope.savebase = function() {
    console.log('scope.item', $scope.item);
  };

  $scope.$watch('subComponent', function(value) {
    if (value) {
      if (typeof value === 'object' && value.subComponent && value.subComponent.componentId && value.subComponent.name) {
        var found = _.some($scope.item.subComponents, function(thing){
          return thing.componentId === value.subComponent.componentId;
        });
        if (!found && value.subComponent.componentId !== $scope.item.componentId) {
          $scope.item.subComponents.push(value.subComponent);
        }
        $scope.subComponent.subComponent = null;
      }
    }
  }, true);
  $scope.$watch('relatedComponent', function(value) {
    if (value) {
      if (typeof value === 'object' && value.relatedComponent && value.relatedComponent.componentId && value.relatedComponent.name) {
        var found = _.some($scope.item.relatedComponents, function(thing){
          return thing.componentId === value.relatedComponent.componentId;
        });
        if (!found && value.relatedComponent.componentId !== $scope.item.componentId) {
          $scope.item.relatedComponents.push(value.relatedComponent);
        }
        $scope.relatedComponent.relatedComponent = null;
      }
    }
  }, true);

  $scope.$watch(function() {
    return $scope.item? $scope.item.parentComponent: null;
  }, function(value){
    if (value) {
      console.log('changes', $scope.item.parentComponent);
      if (value && value.componentId && value.name) {
        $scope.parentSuccess = 'good';
      } else {
        $scope.parentSuccess = 'bad';
      }
    } else {
      $scope.parentSuccess = 'bad';
    }
  }, true);

  $scope.$watch('componentListKey', function(value) { /*jshint unused:false*/
    $scope.doComponentSearch();
  });

  $scope.$on('$typeahead.select', function(event, data){
    if (data.triggerFlg) {
      if (data.componentId && data.name) {
        if (data.triggerFlg === 'parent') {
          delete data.triggerFlg;
          if ($scope.item.componentId !== data.componentId) {
            $scope.item.parentComponent = data;
          } else {
            $scope.parentSuccess = false;
            $scope.item.parentComponent = null;
          }
        } else if (data.triggerFlg === 'sub'){
          delete data.triggerFlg;
          $scope.subComponent.subComponent = data;
        } else {
          delete data.triggerFlg;
          $scope.relatedComponent.relatedComponent = data;
        }
        $scope.$apply();
      } else if (data === '') {
        if (data.triggerFlg === 'parent') {
          $scope.item.parentComponent = null;
        } else {
          $scope.subComponent.subComponent = null;
        }
      }
    }
  });


}]);
