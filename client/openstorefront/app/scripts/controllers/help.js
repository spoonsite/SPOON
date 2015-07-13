/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

app.controller('helpCtrl', ['$scope', '$draggableInstance', 'business', 
  function ($scope, $draggableInstance, Business) {
 
    $scope.control = {};
    $scope.control.showHelp = true;
   
    $scope.help = {};
    $scope.helpSection = {};     
   
    $scope.loadHelp = function() {
      $scope.$emit('$TRIGGERLOAD', 'helpLoader');   
      Business.systemservice.getHelp().then(function(results){
        $scope.$emit('$TRIGGERUNLOAD', 'helpLoader');
        $scope.help = results;        
        $scope.showHelpSection($scope.help.helpSection);        
      }, function(results) {
        $scope.$emit('$TRIGGERUNLOAD', 'helpLoader');
      });
    };
    $scope.loadHelp();
    
    $scope.displayFeedback = function(){
      Business.systemservice.getShowFeedback().then(function(results){
          if (results && results.code === "TRUE") {
            $scope.control.showFeedback = true;
          }
      });      
    };
    $scope.displayFeedback();
   
   $scope.showHelpSection = function(helpSection) {
     
     if ($scope.helpSection.selected) {
       $scope.helpSection.selected = false;
     }
     $scope.helpSection = helpSection;     
     $scope.helpSection.selected = true;
   };
   
   $scope.popout = function(){
      utils.openWindow('help', 'Help');
      $draggableInstance.close();
   };
   
   $scope.print = function() {
    $scope.popupWin = utils.openWindow('helpprint', '', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=840, height=840', $scope.popupWin);     
   };   
    
}]);


app.controller('helpSingleCtrl', ['$scope', 'business', 'printView', '$timeout',  
  function ($scope, Business, printView, $timeout) {
 
    $scope.control = {};
    $scope.control.showHelp = true;
   
    $scope.help = {};
    $scope.helpSection = {}; 
    
    $scope.helpFlat = [];
    $scope.popupWin;
    
    $scope.flattenHelp = function(helpAll) {
      $scope.helpFlat.push(helpAll.helpSection);
       _.forEach(helpAll.childSections, function(section) {
          $scope.flattenHelp(section);
       });
    };
    
    $scope.loadHelp = function() {
      $scope.$emit('$TRIGGERLOAD', 'helpLoader');   
      Business.systemservice.getHelp().then(function(results){
        $scope.$emit('$TRIGGERUNLOAD', 'helpLoader');
        $scope.help = results;        
        $scope.showHelpSection($scope.help.helpSection);    
        
        //flatten help
        $scope.flattenHelp($scope.help);
        
        if (printView.print) {
          $timeout(function(){
            window.print();
          }, 1000);
        }
        
      }, function(results) {
     //   $scope.$emit('$TRIGGERUNLOAD', 'helpLoader');
      });
    };
    $scope.loadHelp();
    
    $scope.displayFeedback = function(){
      Business.systemservice.getShowFeedback().then(function(results){
          if (results && results.code === "TRUE") {
            $scope.control.showFeedback = true;
          }
      });      
    };
    $scope.displayFeedback();    
   
   $scope.showHelpSection = function(helpSection) {
     if ($scope.helpSection.selected) {
       $scope.helpSection.selected = false;
     }
     $scope.helpSection = helpSection;     
     $scope.helpSection.selected = true;    
   };   
    
    
   $scope.print = function() {
    $scope.popupWin = utils.openWindow('helpprint', '', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=840, height=840', $scope.popupWin);     
   };
    
    
}]);