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

app.controller('SearchToolsCtrl', ['$scope','$uiModal', '$location', '$rootScope', function ($scope, $uiModal, $location, $rootScope) {/*jshint unused: false*/
        $scope.openSearchModal = function (title, data) {
//            console.log("searchtoolsctrl open search modal");
            title = title || 'Advanced Search';
            data = data || {
                'mode': 'topics'
            };
            var modalInstance = $uiModal.open({
                templateUrl: 'views/search/searchmodal.html',
                controller: 'DefaultModalCtrl',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    title: function () {
                        return title;
                    },
                    data: function () {
                        return data;
                    },
                    closeEventName: function () {
                        return '$CLOSE_MODALS';
                    }
                }
            });

            modalInstance.result.then(function (result) {
            }, function (result) {
            });
        };

        $scope.buttons = [
            '<div ng-controller="SearchToolsCtrl"><button type="button" data-container="body" data-toggle="tooltip" title="Show All" ng-click="searchKey=\'all\'; goToSearch(\'search\', \'all\', true);" style="padding:0px; margin-left:5px; width:35px; height:35px;  background-color:#efefef;" class="btn btn-default"><i class="glyphicon glyphicon-asterisk" style="font-size:1.3em;"></i></button>'
            + '<button type="button" data-container="body" data-toggle="tooltip" title="Topic Search"  ng-click="openSearchModal(\'Topic Search\', {mode: \'topics\'});" style="padding:0px; margin-left:5px; width:35px; height:35px; background-color:#efefef;"  class="btn btn-default"><i class="glyphicon glyphicon-th-list" style="font-size:1.3em;"></i></button>'
            + '<button type="button" data-container="body" data-toggle="tooltip" title="Architecture Search" ng-click="openSearchModal(\'Architecture Search\', {mode: \'architecture\'})" style="padding:0px; margin-left:5px; width:35px; height:35px; background-color:#efefef;" class="btn btn-default"><i class="glyphicon glyphicon-book" style="font-size:1.3em;"></i></button></div>'
        ];

        $scope.getSearchToolsHTML = function (index) {
            //create a in-memory div, set it's inner text(which jQuery automatically encodes)
            //then grab the encoded contents back out.  The div never exists on the page.
            //console.log("getSearchToolsHTML called");
            return $scope.buttons[index];//$('<div/>').text($scope.buttons[index]).html();
        };

     
        $scope.goToSearch = function () { /*jshint unused:false*/
            var key = 'All';
            if ($scope.searchKey) {
                key = $scope.searchKey;
                $rootScope.searchKey = $scope.searchKey;
            }
            $location.search({
                'type': 'search',
                'code': key
            });
            $location.path('/results');
        };
}]);
