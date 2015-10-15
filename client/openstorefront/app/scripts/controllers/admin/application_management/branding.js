/* 
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

'use strict';
/*global angular,$,app,utils*/
app.controller('AdminBrandingCtrl', ['$scope', 'business', '$rootScope', '$uiModal', '$timeout',
    function ($scope, Business, $rootScope, $uiModal, $timeout) {
        $scope.predicate = 'description';
        $scope.reverse = false;
        $scope.pagination = {};
        $scope.pagination.control = {};
        $scope.data = {};
        $scope.data.allTypes = {};
  
        
        $scope.getFilters = function (override, all) {
            $scope.$emit('$TRIGGERLOAD', 'topicsLoader');
            if ($scope.pagination.control && $scope.pagination.control.refresh) {
                $scope.pagination.control.refresh().then(function () {
                    $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                });
            } else {
                $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
            }
        };
       
        $scope.setPredicate = function (predicate, override) {
            if ($scope.predicate === predicate) {
                $scope.reverse = !$scope.reverse;
            } else {
                $scope.predicate = predicate;
                $scope.reverse = !!override;
            }
            $scope.pagination.control.changeSortOrder(predicate);
        };
        
        if ($scope.pagination.control) {
            $scope.pagination.control.onRefresh = function () {
                $scope.selectedTypes = [];
                $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
            };
        }

        $scope.loadFullBrandingList = function() {
             Business.brandingservice.getBrandingViews(true).then(function (brandingViews){
                 $scope.fullBrandingList = [];
                 console.log("Branding:", brandingViews);
                 $scope.fullBrandingList = brandingViews;
             }, function () {
                 console.log("Error: Couldn't load the branding views.");
                 $scope.fullBrandingList = [];
             }); 
        }; 

        $scope.loadAllData = function(){
            
            $timeout(function () {
               $scope.getFilters();
               $scope.loadFullBrandingList();
            }, 20);
            
            console.log("Branding list:",$scope.fullBrandingList);
        };
        $scope.loadAllData();
        
        $scope.deleteTemplate = function (template) {
          console.log("Delete Template");
            var cont = confirm("You are about to permanently remove a branding template from the system.  Continue?");
            if (cont) {
                $scope.deactivateButtons = true;
                $scope.$emit('$TRIGGERLOAD', 'topicsLoader');
                Business.brandingservice.deleteBranding(template.brandingId).then(function () {
                    $timeout(function () {
                        triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusBranding', 'body', 3000);
                        $scope.loadAllData();
                        $scope.deactivateButtons = false;
                        $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                    }, 1000);
                }, function () {
                    $timeout(function () {
                        $scope.loadAllData();
                        $scope.deactivateButtons = false;
                        $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                    }, 1000);
                });
            }
        };
        
        $scope.editTemplate = function (brandingtemplate) {
             
                var modalInstance = $uiModal.open({
                    templateUrl: 'views/admin/application_management/editbranding.html',
                    controller: 'AdminEditBrandingCtrl',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        brandingtemplate: function () {
                            return brandingtemplate;
                        },
                        size: function () {
                            return 'lg';
                        }
                    }
                });

                modalInstance.result.then(function (result) {
                    console.log("Result from modal:",result);
                }, function () {
                    console.log("Modal Cancelled");
                });
        };
        
        
        $scope.changeActivity = function (brandingtemplate) {   
            console.log("Change Activity");
            var cont = confirm("You are about to change the active status of a branding template (Enabled or disabled). Continue?");
            if (cont) {
                $scope.deactivateButtons = true;
                if (brandingtemplate.activeStatus === 'A') {
                    $scope.$emit('$TRIGGERLOAD', 'topicsLoader');
                    Business.brandingservice.setBrandingToDefault().then(function () {
                        $timeout(function () {
                            //triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusBranding', 'body', 3000);
                            $scope.loadAllData();
                            $scope.deactivateButtons = false;
                            $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                        }, 1000);
                    }, function () {
                        $timeout(function () {
                            $scope.loadAllData();
                            $scope.deactivateButtons = false;
                            $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                        }, 1000);
                    });
                } else {
                    $scope.$emit('$TRIGGERLOAD', 'topicsLoader');
                    Business.brandingservice.setBrandingActive(brandingtemplate.brandingId).then(function () {
                        $timeout(function () {
                            //triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusBranding', 'body', 3000);
                            $scope.loadAllData();
                            $scope.deactivateButtons = false;
                            $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                        }, 1000);
                    }, function () {
                        $timeout(function () {
                            $scope.loadAllData();
                            $scope.deactivateButtons = false;
                            $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                        }, 1000);
                    });
                }
            }
        };
        
        $scope.cloneTemplate = function (brandingtemplate) {

            console.log("Clone Template");
            $scope.$emit('$TRIGGERLOAD', 'topicsLoader');
            Business.brandingservice.getBrandingView(brandingtemplate.brandingId, true).then(function (brandingView) {
                brandingView.branding.name += 'copy';
                brandingView.branding.brandingId = null;
                Business.brandingservice.createBrandingView(brandingView).then(function () {
                    console.log("Successfully Created");
                    $scope.loadAllData();
                    $scope.deactivateButtons = false;
                    $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                }, function () {
                    $scope.loadAllData();
                    $scope.deactivateButtons = false;
                    $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                    console.log("Could not create new template on the server");
                });
            }, function () {

                $scope.deactivateButtons = false;
                $scope.$emit('$TRIGGERUNLOAD', 'topicsLoader');
                console.log("Could not clone branding template:", brandingtemplate);
            });
        };
    }]);
        
app.filter('formatActive', function() {
    return function(input) {
      input = input || '';
      var out = "";
      if(input === "I"){out="Inactive";}
      else if(input === "A"){out="Active";}
      else if(input === "P"){out="Pending";}
      return out;
    };
  });