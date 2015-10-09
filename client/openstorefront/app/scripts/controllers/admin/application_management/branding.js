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
        $scope.tabs = {};
        $scope.tabs.general = true;
        $scope.selectedTypes = [];
        $scope.topicList = [];
        $scope.allSelected = false;
        $scope.selectedBrandTopicList = [];
        $scope.brandingSelectionOptions = {
          itemSelected: '1',
          options: [
              {id: '1', name: '1'},
              {id: '2', name: '2'},
              {id: '3', name: '3'}
          ]
        };
        $scope.saveNeeded = false;
        
        $scope.turnOnSaveNeeded = function(){
            $scope.saveNeeded =true;
        };
        
        $scope.selectTab = function (tab) {
            _.forIn($scope.tabs, function (value, key) {
                $scope.tabs[key] = false;
            });
            
            $scope.tabs[tab] = true;
        };
        
        $scope.loadFullTopicList = function () {
            var filterObj = angular.copy(utils.queryFilter);
            Business.articleservice.getTypes(filterObj, true).then(function (attributeTypes) {
                $scope.topicList = [];
                $scope.topicList = attributeTypes.data;
                
                $scope.loadBrandingViewTopicList();
            }, function () {
                $scope.topicList = [];
            });
        };
        
        $scope.loadBrandingViewTopicList = function(){
          
          if($scope.brandingSelectionOptions.itemSelected === null){
              console.log("Error: You must have the branding selection set to a value");
              return;
          }
          Business.brandingservice.getTopicSearchItems($scope.brandingSelectionOptions.itemSelected, true).then(function(brandTopicList) {
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
              $scope.selectedTypes = [];
          });  
        };
       

        $scope.selectType = function (topic) {
            
            if (topic.selected) {
                topic.selected = !topic.selected;
                if (topic.selected === false) {
                    $scope.selectedTypes = _.reject($scope.selectedTypes, function (type) {
                        return type === topic.attributeType;
                    });
                    $scope.selectAllTypes.flag = false;
                } else {
                    $scope.selectedTypes.push(topic.attributeType);
                }
            } else {
                topic.selected = true;
                $scope.selectedTypes.push(topic.attributeType);
            }

        };

        $scope.selectAllTypes = function () {
            $scope.selectedTypes = [];
            
            _.forEach($scope.topicList, function (topic) {
                topic.selected = !$scope.selectAllTypes.flag; //click happens before state change
                if (topic.selected) {
                    $scope.selectedTypes.push(topic.attributeType);
                }
            });
        };

        $scope.loadAllData = function(){
            $scope.loadFullTopicList($scope);
        };
        $scope.loadAllData();
        
        $scope.save = function () {
            console.log("Saving Changes");
//      $scope.$emit('$TRIGGERLOAD', 'formLoader'); 
//      Business.systemservice.updateAppProperty($scope.propetyForm.key, $scope.propetyForm.value).then(function (results) {      
//       $scope.$emit('$TRIGGERUNLOAD', 'formLoader');
//       $scope.$emit('$TRIGGEREVENT', '$REFRESH_APP_PROPS');       
//       $uiModalInstance.dismiss('success');
//      }, function(){
//        triggerAlert('Unable to save. ', 'editLogger', 'body', 3000);
//        $scope.$emit('$TRIGGERUNLOAD', 'formLoader'); 
//      });
        };
    }]);