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
  $scope.predicate = 'title';
  $scope.$emit('$TRIGGERLOAD', 'adminHighlights');
  $scope.reverse = false;
  $scope.data = {};
  $scope.deactivateButtons = false;
  
  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  }

  $scope.getHighlights = function() {
    Business.highlightservice.getHighlights(true, true).then(function(result){
      $scope.data.highlights = result? result: [];
      $scope.$emit('$TRIGGERUNLOAD', 'adminHighlights');
    }, function(){
      $scope.$emit('$TRIGGERUNLOAD', 'adminHighlights');
      $scope.data.highlights = [];
    })
  }
  $scope.getHighlights();

  $scope.setPredicate = function(predicate, override){
    if ($scope.predicate === predicate){
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = !!override;
    }
  }

  $scope.getHighlightCodes = function() {
    Business.lookupservice.getHighlightCodes().then(function(result){
      console.log('highlightCodes', result);
      
      $scope.highlightCodes = result || [];
    }, function(){
      $scope.highlightCodes = [];
    })
  }
  $scope.getHighlightCodes()

  $scope.getHighlightCode = function(code){
    if($scope.highlightCodes && $scope.highlightCodes.length) {
      var found = _.find($scope.highlightCodes, {'code': code});
      if (found) {
        return found.description;
      } else {
        return code;
      }
    } else {
      return code;
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

  $scope.changeActivity = function(highlight){
    var cont = confirm("You are about to change the active status of an Attribute (Enabled or disabled). Continue?");
    if (cont) {
      $scope.deactivateButtons = true;
      if (highlight.activeStatus === 'A') {
        $scope.$emit('$TRIGGERLOAD', 'adminHighlights');
        Business.highlightservice.deactivateHighlight(highlight.highlightId).then(function(){
          $timeout(function(){
            $scope.getHighlights(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminHighlights');
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getHighlights(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminHighlights');
          }, 1000);
        })
      } else {
        $scope.$emit('$TRIGGERLOAD', 'adminHighlights');
        Business.highlightservice.activateHighlight(highlight.highlightId).then(function() {
          $timeout(function(){
            $scope.getHighlights(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminHighlights');
          }, 1000);
        }, function(){
          $timeout(function(){
            $scope.getHighlights(true);
            $scope.deactivateButtons = false;
            $scope.$emit('$TRIGGERUNLOAD', 'adminHighlights');
          }, 1000);
        })
      }
    }
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
      $timeout(function(){
        $scope.getHighlights(true);
      }, 1000);
    }, function (result) {
      $timeout(function(){
        $scope.getHighlights(true);
      }, 1000);
      $scope.$emit('$TRIGGERLOAD', 'adminHighlights');
    });
  }

  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);
}]);

app.controller('AdminEditHighlightsModalCtrl',['$scope', '$uiModalInstance', 'highlight', 'business', '$location', function ($scope, $uiModalInstance, highlight, Business, $location) {

  $scope.highlight = highlight? angular.copy(highlight) : {};
  $scope.editorContent = angular.copy($scope.highlight.description) || ' ';
  $scope.editorOptions = getCkConfig(true);
  $scope.highlightCodes;

  Business.lookupservice.getHighlightCodes().then(function(result){
    console.log('highlightCodes', result);
    
    $scope.highlightCodes = result || [];
  }, function(){
    $scope.highlightCodes = [];
  })


  $scope.ok = function () {
    $scope.highlight.description = $scope.editorContentWatch;
    console.log('$scope.highlight', $scope.highlight);
    Business.highlightservice.saveHighlight($scope.highlight).then(function(result){
      triggerAlert('The changes to the highlight were saved', 'highlightAlert', 'body', 6000);
      $uiModalInstance.close();
    }, function(){
      triggerAlert('There were errors when saving the highlight', 'highlightAlert', 'highlightEditModal', 6000);
    })
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };

}]);