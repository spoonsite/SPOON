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
/*global angular,$,console,$compile,$q,$http,$templateCache, app*/
app.directive('searchTopicPopover', ['$compile', '$templateCache', '$q', '$http', 'business', '$timeout', function ($compile, $templateCache, $q, $http, Business, $timeout) {

        //
        // loadDataForPopup: Gets the data from the database needed for this popup.
        //
        var loadDataForPopup = function (scope) {
                        
            Business.brandingservice.getAllTopicSearchItems().then(function (result) {
                console.log("Result", result);
                scope.topicList = result;
            }, function (result) {
                console.log("Error Result:", result);
                scope.topicList = [];
            });
        };

        //
        // getTemplate: Loads in the html file used as a template for the popover and the data to populate the popover.
        //
        var getPopupView = function () {

            var def = $q.defer(), tplate = $templateCache.get("popSearchTopic.html");
            
            console.log("template:", tplate);
            if (typeof tplate === "undefined") {
               
                $http.get("views/popover/popSearchTopic.html")
                        .success(function (data) {
                            $templateCache.put("popSearchTopic.html", data);
                            def.resolve(data);
                        });
            } else {
                def.resolve(tplate);
            }
            return def.promise;
        };
  
        return {
            restrict: 'E',
            template: '',
            scope: {
                popVis: "=?visible"
            },
            link: function (scope, element) {
               
               
                loadDataForPopup(scope);
                
                
                getPopupView().then(function (loadedHTML) {
                    console.log("html:"+loadedHTML+" topicList:"+scope.topicList);
                    element.html(loadedHTML);
                    $compile(element.contents())(scope);
                    console.log("html2:"+loadedHTML);
                });
                
                scope.topicsClicked = function () {
                    console.log("topicsClicked");
                    if(scope.popVis){
                        scope.popVis = false;       
                    }
                    else{
                        scope.popVis = true;
                    }
                };
                
                scope.$on('setVis', function(){
                    console.log("Got setVis");
                   scope.topicsClicked();
                });
            }
            
            
        };

    }]);
