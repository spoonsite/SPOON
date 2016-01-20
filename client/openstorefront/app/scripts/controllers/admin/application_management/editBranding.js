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
    $scope.fullArchList = [];
    $scope.allSelected = false;
    $scope.selectedTopicTypes = [];
    $scope.editBrandingId=brandingtemplate.brandingId;
    $scope.saveNeeded = false;
    $scope.selectAllTypes = {};
    $scope.topicSearchItems = [];
    $scope.turnOnSaveNeeded = function(){
              $scope.saveNeeded =true;
    };
  
    $scope.loadFullTopicList = function () {
        var filterObj = angular.copy(utils.queryFilter);
        Business.articleservice.getTypes(filterObj, true).then(function (attributeTypes) {
            $scope.fullTopicList = [];
            $scope.fullTopicList = attributeTypes.data;
            $scope.loadBrandingViewTopicList();
            $scope.loadArchList();
        }, function () {
            $scope.fullTopicList = [];
        });
    };

    $scope.loadBrandingViewTopicList = function () {
        Business.brandingservice.getBrandingView($scope.editBrandingId, true).then(function (brandingView) {
            $scope.topicSearchItems = _.pluck(brandingView.topicSearchViews, 'attributeType');
        }, function () {
            $scope.topicSearchItems = [];
        });
    };

    $scope.loadArchList = function(){
       _.forEach($scope.fullTopicList, function(topic){
           if(topic.architectureFlg){
               $scope.fullArchList.push(topic.attributeType);
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

      var result = {};
      result.brandingtemplate = $scope.brandingtemplate;

      $uiModalInstance.dismiss(result);
    };

    $scope.save = function (validity) {

        if (validity) {
            var convertTopics = [];
            _.each($scope.topicSearchItems, function(attr){
                convertTopics.push({'attributeType': attr});
            });
            var sndObj = {'branding': $scope.brandingtemplate, 'topicSearchItems': convertTopics };
            console.log("Saving Object", sndObj);
            //Send this to server
            Business.brandingservice.saveBrandingView($scope.editBrandingId, sndObj).then(function (result) {
                $uiModalInstance.close(result);
            }, function () {
            sndObj = {};
            console.log("Error: Save template failed.");
        });
               
        }
    };

    $scope.checkColor = function(color){
       console.log("Color"+color); 
       var isOk  = /(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i.test(color);
       console.log("isOk",isOk);
       if(!isOk){
           color="#";
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