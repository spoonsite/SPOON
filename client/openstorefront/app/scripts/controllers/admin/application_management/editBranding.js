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

app.controller('AdminEditBrandingCtrl', ['$scope', '$uiModalInstance', '$uiModal', 'brandingtemplate', 'size', 'business', '$timeout', '$filter', function ($scope, $uiModalInstance, $uiModal, brandingtemplate, size, Business, $timeout, $filter) {
    $scope.brandingtemplate = angular.copy(brandingtemplate);
    $scope.triggerShow = 0;
    $scope.backup = angular.copy(brandingtemplate);
    $scope.size = size;
    $scope.predicate = 'description';
    $scope.editorOptions = getCkBasicConfig(true);

    $scope.fullBrandingList= [];
    $scope.fullTopicList = [];
    $scope.allSelected = false;
    $scope.selectedTopicTypes = [];
    $scope.editBrandingId=brandingtemplate.brandingId;
    $scope.saveNeeded = false;
    $scope.selectAllTypes = {};
    
    $scope.turnOnSaveNeeded = function(){
              $scope.saveNeeded =true;
    };
  
    $scope.loadFullTopicList = function () {
        var filterObj = angular.copy(utils.queryFilter);
        Business.articleservice.getTypes(filterObj, true).then(function (attributeTypes) {
            $scope.fullTopicList = [];
            $scope.fullTopicList = attributeTypes.data;
            console.log("Load Full Topic List:",$scope.fullTopicList);
            $scope.loadBrandingViewTopicList();
        }, function () {
            $scope.fullTopicList = [];
        });
    };

    $scope.loadBrandingViewTopicList = function () {
        console.log("Branding View ID:",$scope.editBrandingId);
        Business.brandingservice.getBrandingView($scope.editBrandingId, true).then(function (brandingView) {
            console.log("brandingView",brandingView);
            $scope.fullBrandingList = brandingView.topicSearchViews;
            console.log("Load Selected Topic List:",$scope.fullBrandingList);
            
            _.forEach($scope.fullBrandingList, function (topic) {
                var found = _.find(topic, {'attributeType': topic.attributeType});
                if (found) {
                    found.selected = true;
                }
                $scope.selectType(topic);
            });
        }, function () {
            $scope.fullBrandingList = [];
            $scope.selectedTopicTypes = [];
        });
    };


    $scope.selectType = function (topic) {

        if (topic.selected) {
            topic.selected = !topic.selected;
            if (topic.selected === false) {
                $scope.selectedTopicTypes = _.reject($scope.selectedTopicTypes, function (type) {
                    return type.attributeType === topic.attributeType;
                });
                $scope.selectAllTypes.flag = false;
            } else {
                $scope.selectedTopicTypes.push(topic);
            }
        } else {
            topic.selected = true;
            $scope.selectedTopicTypes.push(topic);
        }

    };

    $scope.selectAllTypes = function () {
        $scope.selectedTopicTypes = [];

        _.forEach($scope.fullTopicList, function (topic) {
            topic.selected = !$scope.selectAllTypes.flag; //click happens before state change
            if (topic.selected) {
                $scope.selectedTopicTypes.push(topic);
            }
        });
    };

    $scope.loadAllData = function () {
        $scope.loadFullTopicList();

    };
    $scope.loadAllData();
        
    $scope.ok = function () {
      // console.log('we closed the edit code');

      var result = {};
      $uiModalInstance.close(result);
    };

    $scope.cancel = function () {
      // console.log('we closed the edit code');

      var result = {};
      result.brandingtemplate = $scope.brandingtemplate;

      $uiModalInstance.dismiss(result);
    };

    $scope.save = function(validity){
        
        if(validity){
            console.log("Valid:",validity);
            console.log("Template",$scope.brandingtemplate);
            console.log("Topics",$scope.selectedTopicTypes);
            
            var convertTopics = [];
            
            _.forEach($scope.selectedTopicTypes, function (topic) {
                var tmp = {'attributeType':topic};
                convertTopics.push(tmp);
            });
            
            console.log("Converted Topics",convertTopics);
            
            var sndObj = {'branding':$scope.brandingtemplate,'topicSearchItems':convertTopics};
            //Send this to server
            
            
         }
    };

    

    $timeout(function() {
      $('[data-toggle=\'tooltip\']').tooltip();
    }, 300);
}]);

app.filter('offset', function() {
  return function(input, start) {
    start = parseInt(start, 10);
    return input.slice(start);
  };
});