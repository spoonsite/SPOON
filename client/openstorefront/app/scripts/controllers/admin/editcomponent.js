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

app.controller('AdminComponentEditCtrl', ['$scope', '$q', '$filter', '$uiModalInstance', 'component', 'editMode', 'business', '$uiModal', 'FileUploader', 
  function ($scope, $q, $filter, $uiModalInstance, component, editMode, Business, $uiModal, FileUploader) {
    
    $scope.editMode = editMode;
    $scope.editModeText = $scope.editMode ? 'Edit ' + component.component.name : 'Add Component';
    $scope.componentForm = component.component !== undefined ? angular.copy(component.component) : {};
    $scope.editorOptions = getCkBasicConfig();
        
    $scope.statusFilterOptions = [
      {code: 'A', desc: 'Active'},
      {code: 'I', desc: 'Inactive'}
    ];
    
    $scope.predicate = [];
    $scope.reverse = [];      
    
    var basicForm = {
      saveText: 'Add',
      edit: false
    };
    
    var basicFilter = {
      status: $scope.statusFilterOptions[0]
    };    
    
    $scope.generalForm = {};   
    $scope.generalForm.requiredAttribute = {};
    $scope.flags = {};
    if (component.integrationManagement){
      $scope.flags.showIntegrationBanner = true;
      $scope.integrationText = component.integrationManagement;
    }
    $scope.componentAttributeQueryFilter = angular.copy(utils.queryFilter);      

          
    $scope.attributeForm = angular.copy(basicForm);
    $scope.componentAttributeViewQueryFilter = angular.copy(utils.queryFilter);      
    $scope.componentAttributeViewQueryFilter.status = $scope.statusFilterOptions[0].code;
        
    $scope.contactForm = angular.copy(basicForm);
    $scope.contactQueryFilter = angular.copy(utils.queryFilter);   
    $scope.contactQueryFilter.status = $scope.statusFilterOptions[0].code;
    
    $scope.resourceForm = angular.copy(basicForm);
    $scope.resourceQueryFilter = angular.copy(utils.queryFilter);   
    $scope.resourceQueryFilter.status = $scope.statusFilterOptions[0].code;
    
    $scope.mediaForm = angular.copy(basicForm);
    $scope.mediaQueryFilter = angular.copy(utils.queryFilter);   
    $scope.mediaQueryFilter.status = $scope.statusFilterOptions[0].code;    
    
    $scope.dependencyForm = angular.copy(basicForm);
    $scope.dependencyFilter = angular.copy(utils.queryFilter);   
    $scope.dependencyFilter.status = $scope.statusFilterOptions[0].code;      
    
    $scope.metadataForm = angular.copy(basicForm);
    $scope.metadataFilter = angular.copy(utils.queryFilter);   
    $scope.metadataFilter.status = $scope.statusFilterOptions[0].code;    
    
    $scope.evaluationForm = {};
    
    $scope.EMAIL_REGEXP = /^[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*$/i;
    
    $scope.setPredicate = function (predicate, table) {
      if ($scope.predicate[table] === predicate) {
        $scope.reverse[table] = !$scope.reverse[table];
      } else {
        $scope.predicate[table] = predicate;
        $scope.reverse[table] = false;
      }
    };
    
//<editor-fold   desc="COMMON Section">    

    $scope.loadLookup = function(lookup, entity, loader){
      $scope.$emit('$TRIGGERLOAD', loader);

      Business.lookupservice.getLookupCodes(lookup, 'A').then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', loader);
        if (results) {
          $scope[entity]= results;
        }        
      });      
    };
    
    $scope.loadEntity = function(entityOptions){
      if ($scope.componentForm.componentId) {
        $scope.$emit('$TRIGGERLOAD', entityOptions.loader);        
        Business.componentservice.getComponentSubEntity({
          componentId: $scope.componentForm.componentId,
          entity: entityOptions.entity,
          queryParamFilter: entityOptions.filter
        }).then(function (results) {
          $scope.$emit('$TRIGGERUNLOAD', entityOptions.loader);
          if (results) {
            $scope[entityOptions.entity] = results;
          }
        });
      }
    };
    
    $scope.toggleEntityStatus = function(entityOptions){
      $scope.$emit('$TRIGGERLOAD', entityOptions.loader);
      if(entityOptions.entity.activeStatus === 'A') {
        Business.componentservice.inactivateEnity({
          componentId: $scope.componentForm.componentId,
          entityId: entityOptions.entityId,
          entity: entityOptions.entityName
        }).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', entityOptions.loader);
          entityOptions.loadEntity();             
        });        
      } else {
        Business.componentservice.activateEntity({
          componentId: $scope.componentForm.componentId,
          entityId: entityOptions.entityId,
          entity: entityOptions.entityName
        }).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', entityOptions.loader);
          entityOptions.loadEntity();  
        });        
      }
    };    
    
    $scope.editEntity = function(form, entity){
      $scope[form] = angular.copy(entity);     
      $scope[form].saveText = "Update";
      $scope[form].edit = true; 
      $scope[form].collapse = false;          
    };

    $scope.cancelEdit = function(form){
      $scope[form] = {};
      $scope[form].saveText = "Add";
      $scope[form].edit = false;       
    };
    
    $scope.saveEntity = function(options){
      $scope.$emit('$TRIGGERLOAD', options.loader);
      
      if ($scope[options.formName].edit){
        Business.componentservice.updateSubComponentEntity({
          componentId: $scope.componentForm.componentId,
          entityName: options.entityName,
          entity: options.entity,
          entityId: options.entityId
        }).then(function (result) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', options.loader);
          if (result) {
            if (result && result !== 'false' && isNotRequestError(result)){      
              removeError();
              triggerAlert('Saved successfully', options.alertId, options.alertDiv, 3000);
              $scope.cancelEdit(options.formName);
              options.loadEntity();                
            } else {
              removeError();
              triggerError(result, true);
            }
          }
        });         
      } else {
        Business.componentservice.addSubComponentEntity({
          componentId: $scope.componentForm.componentId,
          entityName: options.entityName,
          entity: options.entity
        }).then(function (result) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', options.loader);
          if (result) {
            if (result && result !== 'false' && isNotRequestError(result)){  
              removeError();
              triggerAlert('Saved successfully', options.alertId, options.alertDiv, 3000);
              $scope.cancelEdit(options.formName);
              options.loadEntity(); 
            } else {
              removeError();
              triggerError(result, true);
            }
          }
        });       
      }      
    };   
    
    $scope.hardDeleteEntity = function(options){
      var response = window.confirm("Are you sure you want to DELETE "+ options.entityName + "?");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', options.loader);
        Business.componentservice.forceRemoveEnity({
          componentId: $scope.componentForm.componentId,
          entity: options.entityPath,
          entityId: options.entityId
        }).then(function (result) {          
          $scope.$emit('$TRIGGERUNLOAD', options.loader);
          options.loadEntity();
        });
      }
    };

//</editor-fold>          
    
//<editor-fold   desc="General Section">
    $scope.loadComponentList = function(){
      Business.componentservice.getComponentLookupList().then(function (results) {
        if (results) {          
          $scope.parentComponents = results;                    
          $scope.parentComponents.push({
            code: '',
            description: '  '
          });
          $scope.generalForm.parentComp = _.find($scope.parentComponents, {code: $scope.componentForm.parentComponentId});          
        }
      });      
    };
       
    
    $scope.loadApprovalStatus  = function(){
      Business.componentservice.getComponentApproveStatus().then(function (results) {
        if (results) {
          $scope.approvalStatuses = results;
        }
      });      
    };
    
    $scope.getAttributes = function (override) { 
      Business.getFilters(override, false).then(function (result) {
        $scope.allAttributes = result ? angular.copy(result) : [];
        $scope.requiredAttributes = _.filter($scope.allAttributes, {requiredFlg: true});
       
      });
    };    
    
    $scope.loadComponentAttributes = function(){
      if ($scope.componentForm.componentId) {
        $scope.componentAttributeQueryFilter.status = 'A';    
        Business.componentservice.getComponentAttributes($scope.componentForm.componentId, $scope.componentAttributeQueryFilter).then(function (results) {
          if (results) {
            $scope.activeComponentAttributes = results;                    
            _.forEach($scope.activeComponentAttributes, function (item) {
              $scope.generalForm.requiredAttribute[item.componentAttributePk.attributeType] = item.componentAttributePk.attributeCode;
            });            
          }
        });
      }
    };
    
    $scope.loadAllComponentForms = function(){
      $scope.$emit('$TRIGGERLOAD', 'generalFormLoader');
      
      var deferred = $q.defer();
      deferred.promise.then(function(){
        $scope.loadComponentList();
      }).then(function(){
        $scope.loadApprovalStatus();  
      }).then(function(){
        $scope.getAttributes(true); 
      }).then(function(){
        $scope.loadComponentAttributes();
      }).then(function(){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'generalFormLoader');
      });      
      deferred.resolve('Start');      
    };
    $scope.loadAllComponentForms();
    
    $scope.getCodesForType = function(type){
      var foundType = _.find($scope.allAttributes, {type: type});
      return foundType !== undefined ? foundType.codes : [];
    };  
    
    $scope.saveComponent = function(){
      $scope.$emit('$TRIGGERLOAD', 'generalFormLoader');
      
      var requiredForComponent = {
        component: $scope.componentForm,
        attributes: []
      };
      
      _.forOwn($scope.generalForm.requiredAttribute, function (value, key) {
        requiredForComponent.attributes.push({
          componentAttributePk: {
            attributeType: key,
            attributeCode: value
          }
        });
      });
      
    if (requiredForComponent.component.releaseDate){
      requiredForComponent.component.releaseDate = $filter('date')(requiredForComponent.component.releaseDate, "yyyy-MM-dd'T'HH:mm:ss.sss");
      //requiredForComponent.component.releaseDate = requiredForComponent.component.releaseDate.sub
    }
      
      if ($scope.editMode){
        Business.componentservice.updateComponent(requiredForComponent, $scope.componentForm.componentId).then(function (result) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'generalFormLoader');
          if (result) {
            if (result && result !== 'false' && isNotRequestError(result)){      
              removeError();
              triggerAlert('Saved successfully', 'saveGeneralComponent', 'componentWindowDiv', 3000);
              $scope.componentForm = result.component;
              $scope.$emit('$TRIGGEREVENT', '$REFRESH_COMPONENTS');  
            } else {
              removeError();
              triggerError(result, true);
            }
          }
        });         
      } else {
        Business.componentservice.addComponent(requiredForComponent).then(function (result) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'generalFormLoader');
          if (result) {
            if (result && result !== 'false' && isNotRequestError(result)){  
              removeError();
              triggerAlert('Saved successfully', 'saveGeneralComponent', 'componentWindowDiv', 3000);
              $scope.componentForm = result.component;
              $scope.editMode = true;
              $scope.editModeText = 'Edit ' + $scope.componentForm.name;
              $scope.$emit('$TRIGGEREVENT', '$REFRESH_COMPONENTS');  
            } else {
              removeError();
              triggerError(result, true);
            }
          }
        });       
      } 
      
    };
    
    
    
//</editor-fold>     
    
//<editor-fold   desc="ATTRIBUTE Section">

    $scope.loadComponentAttributesView = function(){
      if ($scope.componentForm.componentId) {
        $scope.$emit('$TRIGGERLOAD', 'attributeFormLoader');     
        Business.componentservice.getComponentAttributeView($scope.componentForm.componentId, $scope.componentAttributeViewQueryFilter).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'attributeFormLoader');
          if (results) {
            $scope.componentAttributesView = results; 
            $scope.componentAttributesView = _.filter($scope.componentAttributesView, {requiredFlg: false});
          }
        });
      }
    };    
    $scope.loadComponentAttributesView();   
    
   
    $scope.toggleAttributeStatus = function(attribute){
      $scope.$emit('$TRIGGERLOAD', 'attributeFormLoader');
      
      if(attribute.activeStatus === 'A') {
        Business.componentservice.inactivateAttribute($scope.componentForm.componentId, attribute.type, attribute.code).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'attributeFormLoader');
          $scope.loadComponentAttributesView();              
        });        
      } else {
        Business.componentservice.activateAttribute($scope.componentForm.componentId, attribute.type, attribute.code).then(function (results) {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'attributeFormLoader');
          $scope.loadComponentAttributesView();    
        });        
      }
    }; 
    
    $scope.saveAttribute = function(){
      $scope.$emit('$TRIGGERLOAD', 'attributeFormLoader');
      var componentAttribute = {
        componentAttributePk: {
          attributeType : $scope.attributeForm.type,
          attributeCode : $scope.attributeForm.code          
        }
      };
      Business.componentservice.saveAttribute($scope.componentForm.componentId, componentAttribute).then(function (result) {
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'attributeFormLoader');
        if (result){
          if (result && result !== 'false' && isNotRequestError(result)) {
            removeError();
            triggerAlert('Saved successfully', 'saveAttributes', 'componentWindowDiv', 3000);
            $scope.attributeForm.type = "";
            $scope.attributeForm.code = "";
            $scope.loadComponentAttributesView();
          } else {
            removeError();
            triggerError(result, true);
          }
        }        
      });       
    };

//</editor-fold> 

//<editor-fold   desc="Contact Section">
    
    $scope.loadLookup('ContactType', 'contactTypes', 'contactFormLoader');  

    $scope.loadContacts = function() {
      $scope.loadEntity({
      filter: $scope.contactQueryFilter,
      entity: 'contacts',
      loader: 'contactFormLoader'
     });
    };
    $scope.loadContacts();    
   
    $scope.toggleContactStatus = function(contact){
      $scope.toggleEntityStatus({
        entity: contact,        
        entityId: contact.contactId,
        entityName: 'contacts',        
        loader: 'contactFormLoader',
        loadEntity: function(){
          $scope.loadContacts();
        }
      });
    };    
   
    $scope.saveContact = function () {
      $scope.saveEntity({
        alertId: 'saveContact',
        alertDiv: 'contactManagementDivId',
        loader: 'contactFormLoader',
        entityName: 'contacts',
        entity: $scope.contactForm,
        entityId: $scope.contactForm.contactId,
        formName: 'contactForm',
        loadEntity: function () {
          $scope.loadContacts();
        }
      });       
     };

//</editor-fold>  

//<editor-fold   desc="Resource Section">
    
    $scope.loadLookup('ResourceType', 'resourceTypes', 'resourceFormLoader');  

    $scope.loadResources = function() {
      $scope.loadEntity({
      filter: $scope.resourceQueryFilter,
      entity: 'resources',
      loader: 'resourceFormLoader'
     });
    };
    $scope.loadResources();
    
    $scope.toggleResourceStatus = function(resource){
      $scope.toggleEntityStatus({
        entity: resource,        
        entityId: resource.resourceId,
        entityName: 'resources',        
        loader: 'resourceFormLoader',
        loadEntity: function(){
          $scope.loadResources();
        }
      });
    };
    
    $scope.deleteResource = function(resource){
     $scope.hardDeleteEntity({
        loader: 'resourceFormLoader',
        entityName: 'Resource',
        entityPath: 'resources',
        entityId: resource.resourceId,
        loadEntity: function(){
          $scope.loadResources();
        }        
     }); 
    };
   
    $scope.saveResource = function () {
      $scope.resourceForm.link = $scope.resourceForm.originalLink;
      if ($scope.resourceForm.originalLink || 
        $scope.resourceUploader.queue.length === 0) {
        
        if (!$scope.resourceForm.originalLink) {
          $scope.resourceForm.fileName = $scope.resourceForm.localResourceName;    
          $scope.resourceForm.originalName = $scope.resourceForm.originalFileName;  
        } else {
          $scope.resourceForm.mimeType = '';
        }
        
        $scope.saveEntity({
          alertId: 'saveResource',
          alertDiv: 'componentWindowDiv',
          loader: 'resourceFormLoader',
          entityName: 'resources',
          entity: $scope.resourceForm,
          entityId: $scope.resourceForm.resourceId,
          formName: 'resourceForm',
          loadEntity: function () {
            $scope.loadResources();
          }
        });
      } else {      
        $scope.resourceUploader.uploadAll();
      }
     };   
     
    $scope.cancelResourceEdit = function () {
      $scope.cancelEdit('resourceForm');      
    };
     
    $scope.resourceUploader = new FileUploader({
      url: 'Resource.action?UploadResource',
      alias: 'file',
      queueLimit: 1,  
      removeAfterUpload: true,
      onBeforeUploadItem: function(item) {
        $scope.$emit('$TRIGGERLOAD', 'resourceFormLoader');
        
        item.formData.push({
          "componentResource.componentId" : $scope.componentForm.componentId
        });
        item.formData.push({
          "componentResource.resourceType" : $scope.resourceForm.resourceType
        });
        item.formData.push({
          "componentResource.description" : $scope.resourceForm.description
        });
        item.formData.push({
          "componentResource.restricted" : $scope.resourceForm.restricted
        });        
        if ($scope.resourceForm.resourceId) {
          item.formData.push({
            "componentResource.resourceId" : $scope.resourceForm.resourceId
          });
        }
       },
      onSuccessItem: function (item, response, status, headers) {
        $scope.$emit('$TRIGGERUNLOAD', 'resourceFormLoader');
             
        //check response for a fail ticket or a error model
        if (response.success) {
          triggerAlert('Uploaded successfully', 'saveResource', 'componentWindowDiv', 3000); 
          $scope.cancelResourceEdit();
          $scope.loadResources();
        } else {
          if (response.errors) {
            var uploadError = response.errors.file;
            var enityError = response.errors.componentResource;
            var errorMessage = uploadError !== undefined ? uploadError : '  ' + enityError !== undefined ? enityError : '';
            triggerAlert('Unable to upload resource. Message: <br> ' + errorMessage, 'saveResource', 'componentWindowDiv', 6000);
          } else {
            triggerAlert('Unable to upload resource. ', 'saveResource', 'componentWindowDiv', 6000);
          }
        }
      },
      onErrorItem: function (item, response, status, headers) {
        $scope.$emit('$TRIGGERUNLOAD', 'resourceFormLoader');
        triggerAlert('Unable to upload resource. Failure communicating with server. ', 'saveResource', 'componentWindowDiv', 6000);      
      }      
    });     
    

//</editor-fold> 
  
//<editor-fold   desc="Media Section">

    $scope.loadLookup('MediaType', 'mediaTypes', 'mediaFormLoader');

    $scope.loadMedia = function () {
      $scope.loadEntity({
        filter: $scope.mediaQueryFilter,
        entity: 'media',
        loader: 'mediaFormLoader'
      });
    };
    $scope.loadMedia();
    
    $scope.deleteMedia = function(media){
     $scope.hardDeleteEntity({
        loader: 'mediaFormLoader',
        entityName: 'Media',
        entityPath: 'media',
        entityId: media.componentMediaId,
        loadEntity: function(){
          $scope.loadMedia();
        }        
     }); 
    };    
    
    $scope.toggleMediaStatus = function(media){
      $scope.toggleEntityStatus({
        entity: media,        
        entityId: media.componentMediaId,
        entityName: 'media',        
        loader: 'mediaFormLoader',
        loadEntity: function(){
          $scope.loadMedia();
        }
      });
    };    
    
    $scope.saveMedia = function () {
      $scope.mediaForm.link = $scope.mediaForm.originalLink;
      if ($scope.mediaForm.originalLink || 
        $scope.mediaUploader.queue.length === 0) {
        
        if (!$scope.mediaForm.originalLink) {          
          $scope.mediaForm.originalName = $scope.mediaForm.originalFileName;  
        } else {
          $scope.mediaForm.mimeType = '';
        }
        
        $scope.saveEntity({
          alertId: 'saveMedia',
          alertDiv: 'componentWindowDiv',
          loader: 'mediaFormLoader',
          entityName: 'media',
          entity: $scope.mediaForm,
          entityId: $scope.mediaForm.componentMediaId,
          formName: 'mediaForm',
          loadEntity: function () {
            $scope.loadMedia();
          }
        });
      } else {      
        $scope.mediaUploader.uploadAll();
      }
     };   
     
    $scope.cancelMediaEdit = function () {
      $scope.cancelEdit('mediaForm');      
    };
     
    $scope.mediaUploader = new FileUploader({
      url: 'Media.action?UploadMedia',
      alias: 'file',
      queueLimit: 1,  
      removeAfterUpload: true,
      onBeforeUploadItem: function(item) {
        $scope.$emit('$TRIGGERLOAD', 'mediaFormLoader');
        
        item.formData.push({
          "componentMedia.componentId" : $scope.componentForm.componentId
        });
        item.formData.push({
          "componentMedia.mediaTypeCode" : $scope.mediaForm.mediaTypeCode
        });
        item.formData.push({
          "componentMedia.caption" : $scope.mediaForm.caption
        });
        if ($scope.mediaForm.componentMediaId) {
          item.formData.push({
            "componentMedia.componentMediaId": $scope.mediaForm.componentMediaId
          });
        }
       },
      onSuccessItem: function (item, response, status, headers) {
        $scope.$emit('$TRIGGERUNLOAD', 'mediaFormLoader');
               
        //check response for a fail ticket or a error model
        if (response.success) {
          triggerAlert('Uploaded successfully', 'saveResource', 'componentWindowDiv', 3000);          
          $scope.cancelMediaEdit();
          $scope.loadMedia();          
        } else {
          if (response.errors) {
            var uploadError = response.errors.file;
            var enityError = response.errors.componentMedia;
            var errorMessage = uploadError !== undefined ? uploadError : '  ' + enityError !== undefined ? enityError : '';
            triggerAlert('Unable to upload media. Message: <br> ' + errorMessage, 'saveMedia', 'componentWindowDiv', 6000);
          } else {
            triggerAlert('Unable to upload media. ', 'saveMedia', 'componentWindowDiv', 6000);
          }
        }
      },
      onErrorItem: function (item, response, status, headers) {
        $scope.$emit('$TRIGGERUNLOAD', 'mediaFormLoader');
        triggerAlert('Unable to upload media. Failure communicating with server. ', 'saveMedia', 'componentWindowDiv', 6000);        
      }      
    });     
    

//</editor-fold>
  
//<editor-fold   desc="Dependancy Section">
  
    $scope.loadDependancies = function() {
      $scope.loadEntity({
      filter: $scope.dependencyFilter,
      entity: 'dependencies',
      loader: 'dependencyFormLoader'
     });
    };
    $scope.loadDependancies();    
   
    $scope.toggleDependancyStatus = function(depend){
      $scope.toggleEntityStatus({
        entity: depend,        
        entityId: depend.dependencyId,
        entityName: 'dependencies',        
        loader: 'dependencyFormLoader',
        loadEntity: function(){
          $scope.loadDependancies();
        }
      });
    };    
   
    $scope.saveDependancy = function () {
      $scope.saveEntity({
        alertId: 'saveDependancy',
        alertDiv: 'componentWindowDiv',
        loader: 'dependencyFormLoader',
        entityName: 'dependencies',
        entity: $scope.dependencyForm,
        entityId: $scope.dependencyForm.dependencyId,
        formName: 'dependencyForm',
        loadEntity: function () {
          $scope.loadDependancies();
        }
      });       
     };

//</editor-fold>    
  
//<editor-fold   desc="Metadata Section">
  
    $scope.loadMetadata = function() {
      $scope.loadEntity({
      filter: $scope.metadataFilter,
      entity: 'metadata',
      loader: 'metadataFormLoader'
     });
    };
    $scope.loadMetadata();    
   
    $scope.toggleMetadataStatus = function(meta){
      $scope.toggleEntityStatus({
        entity: meta,        
        entityId: meta.metadataId,
        entityName: 'metadata',        
        loader: 'metadataFormLoader',
        loadEntity: function(){
          $scope.loadMetadata();
        }
      });
    };    
   
    $scope.saveMetadata = function () {
      $scope.saveEntity({
        alertId: 'saveMetadata',
        alertDiv: 'componentWindowDiv',
        loader: 'metadataFormLoader',
        entityName: 'metadata',
        entity: $scope.metadataForm,
        entityId: $scope.metadataForm.metadataId,
        formName: 'metadataForm',
        loadEntity: function () {
          $scope.loadMetadata();
        }
      });       
     };

//</editor-fold>   
  
//<editor-fold   desc="Evaluation Section">

    $scope.loadEvaluationInfo = function(){
      $scope.$emit('$TRIGGERLOAD', 'evaluationLoader');

      Business.lookupservice.getLookupCodes('EvaluationSection', 'A').then(function (results) {      
        if (results) {
          $scope.activeSections = results;
          if ($scope.componentForm.componentId) {
            Business.componentservice.getEvaluationSections($scope.componentForm.componentId).then(function (data) {
              $scope.$emit('$TRIGGERUNLOAD', 'evaluationLoader');
              if (data) {
                $scope.allSections = data;  
                $scope.sections = [];
                $scope.oldSections = [];
                _.forEach($scope.allSections, function(record){
                    var foundSection = _.find($scope.activeSections, {code : record.evaluationSection});
                    if (foundSection) {
                      $scope.sections.push(record);                    
                    } else {
                      $scope.oldSections.push(record);
                    }
                });
                _.forEach($scope.activeSections, function(record){
                    var foundSection = _.find($scope.allSections, {evaluationSection : record.code});
                    if (!foundSection) {
                      $scope.sections.push({
                        name: record.description,
                        evaluationSection: record.code                        
                      });                          
                    }
                });
               $scope.sections = _.sortBy($scope.sections, 'name');
               $scope.oldSections = _.sortBy($scope.oldSections, 'name');
              }
            });
          }
        }  
      });      
    };
    $scope.loadEvaluationInfo();
    
    $scope.clearAllEvaluationSections = function(){
      var response = window.confirm("Are you sure you want to DELETE all evaluation information for this component?");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', 'evaluationLoader');      
        Business.componentservice.deleteAllEvaluationSection($scope.componentForm.componentId).then(function (results) {
          $scope.$emit('$TRIGGERUNLOAD', 'evaluationLoader');    
          triggerAlert('Remove all successfully', 'saveEvalSection', 'componentWindowDiv', 3000);
          $scope.loadEvaluationInfo();
        });
      }
    };
    
    $scope.saveEvalSection = function(section){
      if (section.score) {
        if (section.score < 1){
          section.score = 1;
        } else if (section.score > 5){
          section.score = 5;
        }        
      }
      $scope.saveEntity({
        alertId: 'saveEvalSection',
        alertDiv: 'componentWindowDiv',
        loader: 'evaluationFormLoader',
        entityName: 'sections',
        entity: {
          "componentEvaluationSectionPk": {
            "evaluationSection": section.evaluationSection
          },
          "score": section.score
        },
        entityId: section.evaluationSection,
        formName: 'evaluationForm',
        loadEntity: function () {
          $scope.loadEvaluationInfo();
        }
      });       
      
    };
    
    $scope.removeEvaluationSection = function(section){
      var response = window.confirm("Are you sure you want to DELETE section " + section.name + " for this component? (This will reset any unsaved changes)");
      if (response) {
        $scope.$emit('$TRIGGERLOAD', 'evaluationLoader');      
        Business.componentservice.deleteEvaluationSection($scope.componentForm.componentId, section.evaluationSection).then(function (results) {
          $scope.$emit('$TRIGGERUNLOAD', 'evaluationLoader');           
          $scope.loadEvaluationInfo();
        });
      }      
    };    
    
    $scope.checkEvalScore = function(section){
        if (section.score) {
          if (section.score < 1) {
            section.score = 1;
          } else if (section.score > 5) {
            section.score = 5;
          }
        }      
    };
    
    $scope.saveAllEvalSections = function () {
      var allSections = [];
      _.forEach($scope.sections, function (section) {
        if (section.score) {
          if (section.score < 1) {
            section.score = 1;
          } else if (section.score > 5) {
            section.score = 5;
          }
        }
        allSections.push({
          "componentEvaluationSectionPk": {
            "evaluationSection": section.evaluationSection
          },
          "score": section.score          
        });
      });

      $scope.$emit('$TRIGGERLOAD', 'evaluationLoader');
      Business.componentservice.saveAllEvaluationSections({
        componentId: $scope.componentForm.componentId,
        sections: allSections
      }).then(function (results) {
        $scope.$emit('$TRIGGERUNLOAD', 'evaluationLoader');
        triggerAlert('Saved all successfully', 'saveEvalSection', 'componentWindowDiv', 3000);
        $scope.loadEvaluationInfo();
      });
    };
            
    

//</editor-fold>   
    
    $scope.close = function () {
      $uiModalInstance.dismiss('close');
    };
    
}]);
