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

/*global setupMain*/

app.controller('SearchToolsCtrl', ['$scope', 'business', '$uiModal', '$location', '$rootScope', function ($scope, Business, $uiModal, $location, $rootScope) {/*jshint unused: false*/
  
  $scope.searchToolWin = null;

  $scope.closeSearchTools = function(saveData){
      
    Business.saveLocal('ADVANCED_SEARCH', saveData);
    //console.log("Change Location", $location.path());
    $location.search({});
    //if ($location.path() !== '/results') {
        $location.path('results');
    //} else {
    //    $route.reload();
    //}  
  };

  $scope.openSearchTools = function(){
         var searchToolWin = Ext.create('OSF.component.SearchToolWindow', { 
         closeAction: 'destroy',
         angularScope: $scope
     });     
     searchToolWin.show();      
  };
}]);
