'use strict';

/*global triggerError, removeError, isEmpty*/

app.controller('AdminManageOrganizationsCtrl', ['$scope', 'business', '$timeout', '$uiModal', '$q',
  function ($scope, Business, $timeout, $uiModal, $q) {
    $scope.predicate;
    $scope.reverse = false;
    $scope.statusFilterOptions = [
    {code: 'A', desc: 'Active'},
    {code: 'I', desc: 'Inactive'}
    ];
    $scope.queryFilter = angular.copy(utils.queryFilter);    
    $scope.queryFilter.max = 500;
    $scope.queryFilter.sortField = 'name'; 
    $scope.data = {};
    $scope.data.organizations = {};
    $scope.filteredOrganizations = [];
    $scope.flags = {};
    $scope.flags.showUpload = false;
    $scope.organizationUploadOptions = {};
    $scope.selectedOrganizations = [];
    $scope.selectAllComps = {};
    $scope.selectAllComps.flag = false;
    $scope.submitter = null;
    $scope.pagination = {};
    $scope.pagination.control = {};
    $scope.pagination.control.approvalState ='ALL';
    $scope.pagination.control.organizationType ='ALL';
    $scope.pagination.features = {'dates': false, 'max': false};    

    $scope.loadLookup = function(lookup, entity, loader){
      $scope.$emit('$TRIGGERLOAD', loader);

      Business.lookupservice.getLookupCodes(lookup, 'A').then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', loader);
        if (results) {
          $scope[entity]= results;
        }        
      });      
    };
    $scope.loadLookup('SecurityMarkingType', 'securityTypes', 'orgLoader'); 

    $scope.getSecurityDesc = function(type){
      var found = _.find($scope.securityTypes, {'code': type});
      return found? found.description : type; 
    }

    $scope.addToChecked = function(){
      console.log('$scope', $scope);
    }

    $scope.setPredicate = function (predicate) {
      if ($scope.predicate === predicate) {
        $scope.reverse = !$scope.reverse;
      } else {
        $scope.predicate = predicate;
        $scope.reverse = false;
      }
      $scope.pagination.control.changeSortOrder(predicate);
    };

    $scope.pagination.control.setPredicate = function(val){
      $scope.setPredicate(val, 'organizations');
    };

    if ($scope.pagination.control) {
      $scope.pagination.control.onRefresh = function(){
        $scope.selectedOrganizations = [];
        $scope.$emit('$TRIGGERUNLOAD', 'organizationLoader');
      }
    }

    $scope.refreshOrganizations = function () {
      if ($scope.pagination.control && $scope.pagination.control.refresh) {
        $scope.$emit('$TRIGGERLOAD', 'organizationLoader');
        $scope.pagination.control.refresh().then(function(){
          $scope.selectedOrganizations = [];
          $scope.$emit('$TRIGGERUNLOAD', 'organizationLoader');
        });
      }
    };
    $scope.$on('$REFRESH_ORGANIZATIONS', function(){       
      $scope.refreshOrganizations();
    });     

    $scope.editOrganization = function(organization){
      var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/data_management/editOrganization.html',
        controller: 'AdminOrganizationEditCtrl',
        backdrop: 'static',
        size: 'lg',
        resolve: {
          organization: function () {
            return organization;
          },
          editMode: function(){
            return !!organization;
          },
          allOrganizations: function(){
            return $scope.filteredOrganizations;
          }
        }
      });     
    };

    $scope.viewOrphans = function(){
      var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/data_management/viewOrgOrphans.html',
        controller: 'AdminOrganizationOrphanCtrl',
        backdrop: 'static',
        size: 'lg',
        resolve: {}
      });     
    };

    $scope.viewRefs = function(organization){
      var modalInstance = $uiModal.open({
        templateUrl: 'views/admin/data_management/viewOrgOrphans.html',
        controller: 'AdminOrganizationRefCtrl',
        backdrop: 'static',
        size: 'lg',
        resolve: {
          organization: function(){
            return organization;
          }
        }
      });     
    };
    
    $scope.toggleStatus = function(organization){
      var mode = 'INACTIVATE';
      if (organization.activeStatus !== 'A') {
        mode = 'ACTIVATE';
      }

      var response = window.confirm("Are you sure you want  " + mode + " " + organization.name + "?");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', 'organizationLoader');
        if (organization.activeStatus === 'A') {
          Business.organizationservice.inactivateorganization(organization.organizationId).then(function (results) {
            $scope.refreshOrganizations();
            $scope.$emit('$TRIGGERUNLOAD', 'organizationLoader');
          });
        } else {
          Business.organizationservice.activateorganization(organization.organizationId).then(function (results) {
            $scope.refreshOrganizations();
            $scope.$emit('$TRIGGERUNLOAD', 'organizationLoader');
          });
        }
      }
    };
    
    $scope.exportOrganization = function(organizationId){
      window.location.href = "api/v1/resource/organizations/" + organizationId + "/export";
    };    
    
    $scope.preview = function(organization) {
      utils.openWindow('single?id='+ organization.organizationId, 'organization Preview', "resizable=yes,scrollbars=yes,height=650,width=1000");
    };    
    
    $scope.deleteOrganization = function(organization){
      var response = window.confirm("Are you sure you want DELETE "+ organization.name + "?");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', 'organizationLoader');
        Business.organizationservice.deleteOrganization(organization.organizationId).then(function (result) {          
          $scope.$emit('$TRIGGERUNLOAD', 'organizationLoader');
          $scope.refreshOrganizations();
        });
      }
    }; 

    $scope.runExtract = function(){
      $scope.$emit('$TRIGGERLOAD', 'organizationLoader');
      Business.organizationservice.extract().then(function (result) {          
        $scope.$emit('$TRIGGERUNLOAD', 'organizationLoader');
        $scope.refreshOrganizations();
      });
    }; 

    $scope.handleSelection = function(organization){
      $timeout(function(){
        if (organization.selected) {
          console.log('orga', organization);
          
          $scope.setSelected(organization.organizationId);
        } else {
          $scope.unsetSelected(organization.organizationId);
        }
      })
    }

    $scope.setSelected = function(id){
      $scope.selectedOrganizations.push(id)
    }

    $scope.unsetSelected = function(id){
      var index = _.indexOf($scope.selectedOrganizations, id);
      if (index > -1) {
        $scope.selectedOrganizations.splice(index, 1);
      }
      console.log('$scope.selectedOrganizations', $scope.selectedOrganizations);
      
    }

    $scope.switchSelected = function(){
      if ($scope.selectedOrganizations.length < 2) {
        return;
      }
      var first = $scope.selectedOrganizations[0];
      var second = $scope.selectedOrganizations[1];
      $scope.selectedOrganizations[1] = first;
      $scope.selectedOrganizations[0] = second;
    }

    $scope.removeSelections = function(){
      if ($scope.data && $scope.data.organizations && $scope.data.organizations.data) {
        _.each($scope.selectedOrganizations, function(selection){
          var found = _.find($scope.data.organizations.data, {organizationId: selection});
          if (found) {
            found.selected = false;
          }
        })
        $scope.selectedOrganizations = [];
      }
    }

    $scope.mergeSelections = function (){
      var deferred = $q.defer();
      if (!($scope.selectedOrganizations.length === 2)){
        $scope.removeSelections();
        triggerAlert('There was an error merging selections. We\'ve reset your selections so you can try again.', 'selectionsAlert', 'body', 8000);
        deferred.reject(false);
        return deferred.promise;
      }
      var first = $scope.selectedOrganizations[0];
      var second = $scope.selectedOrganizations[1];

      $scope.$emit('$TRIGGERLOAD', 'orgLoader');
      Business.organizationservice.mergeOrganizations(first, second).then(function (result) {
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'orgLoader');
        if (result) {
          if (result && result !== 'false' && isNotRequestError(result)){
            removeError();
            triggerAlert('Organizations have been merged', 'saveGeneralOrganization', 'body', 3000);
            $scope.organizationForm = result.organization;
            $scope.refreshOrganizations();
            deferred.resolve();
          } else {
            $scope.refreshOrganizations();
            removeError();
            triggerError(result, true);
            deferred.reject(result);
          }
        }
      }, function(response){
        $scope.refreshOrganizations();
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'orgLoader');
        triggerAlert('Organizations have been merged', 'saveGeneralOrganization', 'body', 3000);
        deferred.reject();
      });
      return deferred.promise;
    }

    $scope.getOrgById = function(id){
      if ($scope.data && $scope.data.organizations && $scope.data.organizations.data) {
        var found = _.find($scope.data.organizations.data, {organizationId: id});
        return found || {name: '<i>(please select another)</i>'};
      } else {
        return {name: ''};
      }
    }

    
    /*{
      fixedOffset: $('#header') // how far from the top the table should fix to.,
      scrollableArea: $('#header') //which surrounding element is scrolling.
    }*/
    // to resize manually -- $(window).trigger('resize.stickyTableHeaders');

    var stickThatTable = function(){
      var offset = $('.top').outerHeight() + $('#editOrganizationToolbar').outerHeight();
      $(".stickytable").stickyTableHeaders({
        fixedOffset: offset
      });
    }

    $(window).resize(stickThatTable);
    $timeout(stickThatTable, 100);

  }
]); //

app.controller('AdminOrganizationEditCtrl', ['$scope', '$q', '$filter', '$uiModalInstance', 'organization', 'editMode', 'allOrganizations', 'business', '$uiModal', '$draggable', '$rootScope', '$timeout',
  function ($scope, $q, $filter, $uiModalInstance, organization, editMode, allOrganizations, Business, $uiModal, $draggable, $rootScope, $timeout) {

    $scope.editMode           = editMode;
    $scope.allOrganizations   = allOrganizations;
    $scope.editModeText       = $scope.editMode ? 'Edit ' + organization.name : 'Add Organization';
    $scope.editorOptions      = getCkBasicConfig(false);
    $scope.sendAdminMessage   = $rootScope.openAdminMessage;
    $scope.organization       = angular.copy(organization);
    if (!!!$scope.organization) {
      $scope.organization = {};
    }

    $scope.editor = {};
    $scope.editor.editorContent = '';
    $scope.editor.editorContentWatch;

    console.log('org', organization);
    console.log('SCOPE', $scope);

    $scope.statusFilterOptions = [
    {code: 'A', desc: 'Active'},
    {code: 'I', desc: 'Inactive'}
    ];
    
    $scope.predicate;
    $scope.reverse = false; 

    var basicForm = {
      saveText: 'Add',
      edit: false
    };

    var basicFilter = {
      status: $scope.statusFilterOptions[0]
    };    

    $scope.EMAIL_REGEXP = /^[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*$/i;

    $scope.setPredicate = function (predicate, table) {
      if ($scope.predicate[table] === predicate) {
        $scope.reverse[table] = !$scope.reverse[table];
      } else {
        $scope.predicate[table] = predicate;
        $scope.reverse[table] = false;
      }
    };

    $scope.loadLookup = function(lookup, entity, loader){
      $scope.$emit('$TRIGGERLOAD', loader);

      Business.lookupservice.getLookupCodes(lookup, 'A').then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', loader);
        if (results) {
          $scope[entity]= results;
        }        
      });      
    };
    $scope.loadLookup('OrganizationType', 'organizationTypes', 'orgLoader'); 
    $scope.loadLookup('SecurityMarkingType', 'securityTypes', 'orgLoader'); 

    $scope.saveOrganization = function(){
      var deferred = $q.defer();
      $scope.$emit('$TRIGGERLOAD', 'orgLoader');
      Business.organizationservice.saveOrganization($scope.organization).then(function (result) {
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'orgLoader');
        if (result) {
          if (result && result !== 'false' && isNotRequestError(result)){
            removeError();
            triggerAlert('Saved successfully', 'saveGeneralOrganization', 'organizationWindowDiv', 3000);
            $scope.organizationForm = result.organization;
            $scope.$emit('$TRIGGEREVENT', '$REFRESH_ORGANIZATIONS');  
            deferred.resolve();
          } else {
            removeError();
            triggerError(result, true);
            deferred.reject(result);
          }
        }
      }, function(response){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'orgLoader');
        deferred.reject();
      });
      return deferred.promise;
    };

    $scope.ok = function () {
      $scope.saveOrganization().then(function(){
        $uiModalInstance.close();
      }, function(){
        triggerAlert('Something went wrong with the save, please review the form and try again.', 'saveGeneralOrganization', 'organizationWindowDiv', 6000);
      });
    };

    $scope.close = function () {
      $uiModalInstance.dismiss('close');
    };

    $uiModalInstance.opened.then(function(){
      $timeout(function(){
      }, 300);
    })
  }
]);//


app.controller('AdminOrganizationOrphanCtrl', ['$scope', '$q', '$filter', '$uiModalInstance', 'business', '$uiModal', '$draggable', '$rootScope', '$timeout',
  function ($scope, $q, $filter, $uiModalInstance, Business, $uiModal, $draggable, $rootScope, $timeout) {

    $scope.data = [];
    $scope.getData = function(){
      $scope.$emit('$TRIGGERLOAD', 'orgNonLoader');
      Business.organizationservice.getReferencesNoOrg().then(function(result){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'orgNonLoader');
        if (result) {
          $scope.data = result;
        }
      }, function(){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'orgNonLoader');
        $scope.data = [];
      })
    }
    $scope.getData();

    $scope.goToReference = function(thing){
      var link = utils.getLinkByType(thing);
      console.log('link', link);
      if (link) {
        utils.openWindow(link);
      }
    }

    $scope.ok = function () {
      $scope.saveOrganization().then(function(){
        $uiModalInstance.close();
      }, function(){
        triggerAlert('Something went wrong with the save, please review the form and try again.', 'saveGeneralOrganization', 'organizationWindowDiv', 6000);
      });
    };

    $scope.close = function () {
      $uiModalInstance.dismiss('close');
    };
  }
]);//


app.controller('AdminOrganizationRefCtrl', ['$scope', '$q', '$filter', '$uiModalInstance', 'organization', 'business', '$uiModal', '$draggable', '$rootScope', '$timeout',
  function ($scope, $q, $filter, $uiModalInstance, organization, Business, $uiModal, $draggable, $rootScope, $timeout) {

    $scope.organization = angular.copy(organization);

    $scope.data = [];
    $scope.getData = function(override){
      $scope.$emit('$TRIGGERLOAD', 'orgNonLoader');
      Business.organizationservice.getReferences(organization.organizationId, override).then(function(result){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'orgNonLoader');
        if (result) {
          $scope.data = result;
        }
      }, function(){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'orgNonLoader');
        $scope.data = [];
      })
    }
    $scope.getData(true);

    $scope.goToReference = function(thing){
      var link = utils.getLinkByType(thing);
      console.log('link', link);
      if (link) {
        utils.openWindow(link);
      }
    }

    $scope.ok = function () {
      $scope.saveOrganization().then(function(){
        $uiModalInstance.close();
      }, function(){
        triggerAlert('Something went wrong with the save, please review the form and try again.', 'saveGeneralOrganization', 'organizationWindowDiv', 6000);
      });
    };

    $scope.close = function () {
      $uiModalInstance.dismiss('close');
    };
  }
]);//

