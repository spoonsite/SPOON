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

app.controller('AdminEditHighlightsCtrl',['$scope','business', '$uiModal', '$timeout', function ($scope, Business, $uiModal, $timeout) {
  $scope.predicate = 'description';
  $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminHighlights');
  $scope.reverse = false;
  $scope.data = {};


  Business.highlightservice.getHighlights(true).then(function(result){
    $scope.data.highlights = result? result: [];
  }, function(){
    $scope.data.highlights = [];
  })

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  }

  $scope.getHighlightsDesc = function(desc){
    if (desc && desc !== undefined  && desc !== null && desc !== '') {
      // var result = desc.substring(0, 200);
      // if (desc.length > 200){
      //   result += '...';
      // }
      // return result;
      return desc;
    }
    return ' ';
  }

  $scope.deleteHighlights = function(highlight){
    //Do stuff if we're deleting the highlight
  }

  $scope.editHighlights = function(highlight){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/edithighlightsmodal.html',
      controller: 'AdminEditHighlightsModalCtrl',
      size: 'lg',
      resolve: {
        highlight: function(){
          return highlight;
        }
      }
    });

    modalInstance.result.then(function (result) {
      $scope.$emit('$TRIGGERLOAD', 'adminHighlights');
    }, function (result) {
      $scope.$emit('$TRIGGERLOAD', 'adminHighlights');
    });
  }

  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);
}]);

app.controller('AdminEditHighlightsModalCtrl',['$scope', '$uiModalInstance', 'highlight', 'business', '$location', function ($scope, $uiModalInstance, highlight, Business, $location) {

  $scope.highlight = angular.copy(highlight);
  $scope.editorContent = '';
  $scope.editorOptions = getCkConfig(true);

  $scope.ok = function () {
    $uiModalInstance.close();
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };

}]);

