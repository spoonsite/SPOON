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
        console.log("Branding Controller Loaded");
        $scope.predicate = 'description';
        $scope.reverse = false;
        $scope.flags = {};
        $scope.flags.showUpload = false;
        
        $scope.fullBrandingList = [];
        $scope.fullTopicList = [];
        $scope.allSelected = false;
        $scope.selectedBrandTopicList = [];
        $scope.selectedTopicTypes = [];
        $scope.editBrandingId=-1;
        $scope.saveNeeded = false;
        
        
        $scope.setPredicate = function (predicate, override) {
            if ($scope.predicate === predicate) {
                $scope.reverse = !$scope.reverse;
            } else {
                $scope.predicate = predicate;
                $scope.reverse = !!override;
            }
            $scope.pagination.control.changeSortOrder(predicate);
        };
        
        
        $scope.turnOnSaveNeeded = function(){
            $scope.saveNeeded =true;
        };
        
        $scope.loadFullBrandingList = function() {
             Business.brandingservice.getBrandingView().then(function (brandingViews){
                 $scope.fullBrandingList = [];
                 console.log("Branding:", brandingViews);
                 $scope.fullBrandingList = brandingViews.data;
             }, function () {
                 console.log("Error: Couldn't load the branding views.");
                 $scope.fullBrandingList = [];
             }); 
        }; 
        
        $scope.loadFullTopicList = function () {
            var filterObj = angular.copy(utils.queryFilter);
            Business.articleservice.getTypes(filterObj, true).then(function (attributeTypes) {
                $scope.fullTopicList = [];
                $scope.fullTtopicList = attributeTypes.data;
                
                $scope.loadBrandingViewTopicList();
            }, function () {
                $scope.fullTopicList = [];
            });
        };
        
        $scope.loadBrandingViewTopicList = function(){
          
          if($scope.editBrandingId === -1){
              console.log("Error: You must have the branding selection set to a value");
              return;
          }
          Business.brandingservice.getTopicSearchItems($scope.editBrandingId, true).then(function(brandTopicList) {
              $scope.selectedBrandTopicList = brandTopicList;
              _.forEach($scope.selectedBrandTopicList, function (topic) {
                  var found = _.find($scope.topicList, {'attributeType': topic.attributeType});
                  if(found){
                      found.selected = true;
                  }
                  $scope.selectType(topic);
              });
          }, function () {
              $scope.selectedBrandTopicList = [];
              $scope.selectedTopicTypes = [];
          });  
        };
       

        $scope.selectType = function (topic) {
            
            if (topic.selected) {
                topic.selected = !topic.selected;
                if (topic.selected === false) {
                    $scope.selectedTopicTypes = _.reject($scope.selectedTopicTypes, function (type) {
                        return type === topic.attributeType;
                    });
                    $scope.selectAllTypes.flag = false;
                } else {
                    $scope.selectedTopicTypes.push(topic.attributeType);
                }
            } else {
                topic.selected = true;
                $scope.selectedTopicTypes.push(topic.attributeType);
            }

        };

        $scope.selectAllTypes = function () {
            $scope.selectedTopicTypes = [];
            
            _.forEach($scope.fullTopicList, function (topic) {
                topic.selected = !$scope.selectAllTypes.flag; //click happens before state change
                if (topic.selected) {
                    $scope.selectedTopicTypes.push(topic.attributeType);
                }
            });
        };

        $scope.loadAllData = function(){
            $scope.loadFullBrandingList();
            $scope.loadFullTopicList();
            
            console.log("Branding list:",$scope.fullBrandingList);
        };
        $scope.loadAllData();
        
        $scope.deleteTemplate = function (template) {
          console.log("Delete Template");
//            var cont = confirm("You are about to permanently remove an attribute from the system. This will affect all related components.  Continue?");
//            if (cont) {
//                $scope.deactivateButtons = true;
//                $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
//                Business.articleservice.deleteFilter(filter.attributeType).then(function () {
//                    $timeout(function () {
//                        triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusAttributes', 'body', 3000);
//                        $scope.getFilters(true);
//                        $scope.deactivateButtons = false;
//                        $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
//                    }, 1000);
//                }, function () {
//                    $timeout(function () {
//                        $scope.getFilters(true);
//                        $scope.deactivateButtons = false;
//                        $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
//                    }, 1000);
//                });
//            }
        };
        
        $scope.editTemplate = function(type){
          console.log("Edit Template");
//        var modalInstance = $uiModal.open({
//        templateUrl: 'views/admin/data_management/editcodes.html',
//                controller: 'AdminEditcodesCtrl',
//                size: 'lg',
//                backdrop: 'static',
//                resolve: {
//                type: function () {
//                return type;
//                },
//                        size: function() {
//                        return 'lg';
//                        }
               };

        
        $scope.changeActivity = function (filter) {
            
            console.log("Change Activity");
//            var cont = confirm("You are about to change the active status of an Attribute (Enabled or disabled). This will affect all related component attributes.  Continue?");
//            if (cont) {
//                $scope.deactivateButtons = true;
//                if (filter.activeStatus === 'A') {
//                    $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
//                    Business.articleservice.deactivateFilter(filter.attributeType).then(function () {
//                        $timeout(function () {
//                            triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusAttributes', 'body', 3000);
//                            $scope.getFilters(true);
//                            $scope.deactivateButtons = false;
//                            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
//                        }, 1000);
//                    }, function () {
//                        $timeout(function () {
//                            $scope.getFilters(true);
//                            $scope.deactivateButtons = false;
//                            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
//                        }, 1000);
//                    })
//                } else {
//                    $scope.$emit('$TRIGGERLOAD', 'adminAttributes');
//                    Business.articleservice.activateFilter(filter.attributeType).then(function () {
//                        $timeout(function () {
//                            triggerAlert('Summited a task to apply the status change.  The status will update when the task is complete.', 'statusAttributes', 'body', 3000);
//                            $scope.getFilters(true);
//                            $scope.deactivateButtons = false;
//                            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
//                        }, 1000);
//                    }, function () {
//                        $timeout(function () {
//                            $scope.getFilters(true);
//                            $scope.deactivateButtons = false;
//                            $scope.$emit('$TRIGGERUNLOAD', 'adminAttributes');
//                        }, 1000);
//                    })
//                }
//            }
        };
        
        $scope.cloneTemplate = function() {
            
            console.log("Clone Template");
            
        };
        
        
        
        $scope.saveTemplate = function () {
            console.log("Saving Changes");

        };
    }]);