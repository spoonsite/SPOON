'use strict';

/*global triggerError, removeError, isEmpty*/

app.controller('AdminEditcomponentCtrl', ['$scope', 'business', '$timeout', '$uiModal',  function ($scope, Business, $timeout, $uiModal) {

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
    $scope.flags = {};
    $scope.flags.showUpload = false;

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
      $scope.$emit('$TRIGGERLOAD', 'componentLoader');
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
        $scope.$emit('$TRIGGERUNLOAD', 'componentLoader');
      });
    };
    $scope.refreshComponents();
    $scope.$on('$REFRESH_COMPONENTS', function(){       
        $scope.refreshComponents();
    });     

    $scope.filterComponentResults = function(){
      if ($scope.componentFilter.search === '') {
        $scope.filteredComponents = $scope.allComponents;
      } else {      
        $scope.filteredComponents = _.filter($scope.allComponents, function (item){
          return item.component.name.toLowerCase().indexOf($scope.componentFilter.search.toLowerCase()) === 0;
        });
      }
    };


    $scope.editComponent = function(component){
        var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/component/editComponent.html',
        controller: 'AdminComponentEditCtrl',
        size: 'lg',
        resolve: {
          component: function () {
            return component;
          },
          editMode: function(){
            return true;
          }
        }
      });     
    };
    
    $scope.addComponent = function(){
        var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/component/editComponent.html',
        controller: 'AdminComponentEditCtrl',
        size: 'lg',
        resolve: {
          component: function () {
            return {};
          },
          editMode: function(){
            return false;
          }
        }
      });       
    };
    
    $scope.toggleStatus = function(component){
      $scope.$emit('$TRIGGERLOAD', 'componentLoader');
      if (component.component.activeStatus === 'A') {
        Business.componentservice.inactivateComponent(component.component.componentId).then(function (results) {
          $scope.refreshComponents();
          $scope.$emit('$TRIGGERUNLOAD', 'componentLoader');
        });        
      } else {
        Business.componentservice.activateComponent(component.component.componentId).then(function (results) {
          $scope.refreshComponents();
          $scope.$emit('$TRIGGERUNLOAD', 'componentLoader');
        });        
      }
    };
    
    $scope.exportComponent = function(componentId){
      window.location.href = "api/v1/resource/components/" + componentId + "/export";
    };    
    
    $scope.deleteComponent = function(component){
      var response = window.confirm("Are you sure you want DELETE  "+ component.name + "?");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', 'componentLoader');
        Business.componentservice.deleteComponent(component.componentId).then(function (result) {          
          $scope.$emit('$TRIGGERUNLOAD', 'componentLoader');
          $scope.refreshComponents();
        });
      }
    }; 
     
    $scope.exportAll = function(){
       window.location.href = "api/v1/resource/components/export";
    };
     

}]);

app.controller('AdminComponentEditCtrl', ['$scope', '$uiModalInstance', 'component', 'editMode', 'business', '$uiModal', 'FileUploader',
  function ($scope, $uiModalInstance, component, editMode, Business, $uiModal, FileUploader) {
    
    $scope.editMode = editMode;
    $scope.editModeText = $scope.editMode ? 'Edit ' + component.name : 'Add Component';
    $scope.componentForm = angular.copy(component.component);
    $scope.editorOptions = getCkBasicConfig();
    
    $scope.close = function () {
      $uiModalInstance.dismiss('close');
    };
    
}]);
