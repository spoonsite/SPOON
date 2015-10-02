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
app.directive('vitalPopover', ['$compile', '$templateCache', '$q', '$http', 'business', '$timeout', function ($compile, $templateCache, $q, $http, Business, $timeout) {

        //
        // loadDataForPopup: Gets the data from the database needed for this popup.
        //
        var vitalPop = 0;
        var totalOn = 0;
        var loadDataForPopup = function (scope) {
            Business.componentservice.searchByAttribute(scope.attribute).then(function (result) {
                /*console.log("Result", result); */
                scope.articleList = _.filter(result, function (item) {
                    return item.componentType === 'ARTICLE';
                });

                scope.componentList = _.difference(result, scope.articleList);
            }, function (result) {
                //console.log(result);
                scope.articleList = [];
                scope.componentList = [];
            });
        };

        //
        // getTemplate: Loads in the html file used as a template for the popover and the data to populate the popover.
        //
        var getPopupView = function () {

            var def = $q.defer(),
                    template = $templateCache.get("popVital.html");

            if (typeof template === "undefined") {
                $http.get("views/popover/popVital.html")
                        .success(function (data) {
                            $templateCache.put("popVital.html", data);
                            def.resolve(data);
                        });
            } else {
                def.resolve(template);
            }
            return def.promise;
        };


        return {
            restrict: 'E',
            template: '<span><a href="">{{popLink}}</a></span>',
            scope: {
                id: "=",
                attribute: "=",
                popTrigger: "@",
                popHtml: "@",
                popPlacement: "@",
                popTitle: "=",
                popLink: "=",
                popContainer: "="
            },
            link: function (scope, element, attrs) {
                scope.local_id = vitalPop++;

                loadDataForPopup(scope);
                var shown = 0;


                getPopupView(scope).then(function (popOverContent) {
                    scope.popLink = scope.popLink || 'more...';
                    scope.uncompiled_title = '<div><button type="button btn" class="close closeVitalPop"><span aria-hidden="true">&times;</span></button><h4>' + scope.popTitle + '</h4></div>' || '<button type="button btn"  class="close closeVitalPop"><span aria-hidden="true">&times;</span></button><h4>Vital Attribute Name</h4>';
                    scope.compiled_title = $compile(scope.uncompiled_title)(scope);

                    $(element).popover({
                        trigger: scope.popTrigger || 'click',
                        html: scope.popHtml || 'true',
                        content: $compile(popOverContent)(scope),
                        placement: scope.popPlacement || 'auto top',
                        title: scope.compiled_title,
                        container: scope.popContainer || ''
                    });

                    scope.closePopover = function ($event, button) {
                        $(element).popover('hide');
                        $timeout(function () {
                            $('.close.closeVitalPop').off('click', scope.closePopover);
                        });
                    };

                    $(element).on('shown.bs.popover', function (e) {
                        shown++;
                        totalOn++;
                        scope.$emit('$TRIGGEREVENT', 'POPOVER_SHUTDOWN', false);
                        scope.$emit('$TRIGGEREVENT', 'HIDE_VITAL_POPOVER', scope.local_id);
                        $timeout(function () {
                            $('.close.closeVitalPop').on('click', scope.closePopover);
                        });

                    });

                    $(element).on('hidden.bs.popover', function () {
                        shown--;
                        totalOn--;
                        if (!totalOn) {
                            scope.$emit('$TRIGGEREVENT', 'POPOVER_SHUTDOWN', true);
                        }
                    });

                    scope.$on('HIDE_VITAL_POPOVER', function (event, id) {
                        if (id !== scope.local_id && shown) {
                            $(element).popover('hide');
                        }
                    });
                });

                scope.getType = function (attr) {
                    var found = _.find(attr, {'type': 'TYPE'});
                    if (found) {
                        return found.label;
                    }
                    return '';
                };
            }
        };
    }]);