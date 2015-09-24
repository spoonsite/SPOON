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

app.directive('filterquery',['business', '$q', function (Business, $q) {
  var returnFilterQuerySource = function(element, attrs){
    if (attrs.type === 'user') {
      return 'views/services/user.html';
    } else if (attrs.type === 'component') {
      return 'views/services/component.html';
    } else if (attrs.type === 'article') {
      return 'views/services/article.html';
    } else {
      return 'views/services/default.html';
    }
  }
  return {
    templateUrl: returnFilterQuerySource,
    restrict: 'E',
    scope:{
      url: '@',
      max: '@',
      sortBy: '@',
      sortOrder: '@',
      showExport: '@',
      allowAllOption: '@',
      data: '=',
      setFeatures: '=',
      control: '=',
      all: '@',
      callback: '=?' /*This callback is actually an array that contains the pointer function,
      and then the array of arguments expecting the query object to be the last parameter*/
    },
    link: function postLink(scope, element, attrs) {
      scope.defaultMax = 50;
      scope.setFeatures = scope.setFeatures || {'dates': true, 'max': true, 'activeState': true, 'approvalState': false, 'componentType': false, 'clear': true, 'pages': true};
      scope.internalControl = scope.control || {};
      
      scope.defaultMax = scope.max? parseInt(scope.max): 50;
      scope.today = new Date();
      scope.query = {};
      scope.query.filterObj = angular.copy(utils.queryFilter);
      if (scope.callback && scope.callback[1]) {
        scope.callback[1].push(scope.query.filterObj);
      }
      scope.query.url = scope.url || '';
      scope.query.filterObj.offset = 0;
      scope.query.filterObj.max = scope.defaultMax;
      scope.pagination = {};
      scope.pagination.currentPage = 1;
      scope.pagination.itemsPerPage = scope.defaultMax;
      scope.pagination.maxSize = 5;
      scope.showPagination = true;
      scope.maxResults;
      scope.maxPerPage;
      scope.query.filterObj.sortField = scope.sortBy || 'eventDts';
      scope.query.filterObj.sortOrder = scope.sortOrder || 'DESC';
      scope.oldField = scope.sortBy || 'eventDts';
      scope.popover = {
        "title": "Additional filters"
      };

      attrs.$observe('url', function(value){
        if (value) {
          scope.url = value;
          scope.query.url = scope.url || '';
        }
      });

      Business.lookupservice.getLookupCodes('TrackEventCode').then(function(result){
        scope.eventCodes = result? result: [];
      }, function(){
        scope.eventCodes = [];
      })

      Business.lookupservice.getLookupCodes('UserTypeCode').then(function(result){
        scope.userCodes = result? result: [];
      }, function(){
        scope.userCodes = [];
      })

      scope.sendRequest = function(){
        var deferred = $q.defer();
        var query = angular.copy(scope.query);
        query.filterObj.approvalState = scope.internalControl.approvalState;
        if (query.filterObj.approvalState === 'ALL') {
          query.filterObj.approvalState = null;
        }
        query.filterObj.componentType = scope.internalControl.componentType;
        if (query.filterObj.componentType === 'ALL') {
          query.filterObj.componentType = null;
        }
        if (query.filterObj.end) {
          var d = new Date(query.filterObj.end);
          d.setHours(d.getHours()+24);
          query.filterObj.end = d.toISOString();
        }
        if (query.filterObj.start) {
          query.filterObj.start = new Date(query.filterObj.start).toISOString();
        }
        if (attrs.type === 'user' || attrs.type === 'component' || attrs.type === 'article') {
          
          // for tracking types
          var func = (scope.callback && scope.callback.length)? scope.callback[0].apply(null, scope.callback[1]) : Business.trackingservice.get(query);
          func.then(function(result){
            scope.backupResult = result;
            scope.data = result? result.result: [];
            scope.pagination.totalItems = result.count;
            deferred.resolve(result);
          }, function(){
            deferred.resolve();
            scope.data = [];
          }); 
        }else {
          
          var func = (scope.callback && scope.callback.length)? scope.callback[0].apply(null, scope.callback[1]) : Business.get(query);
          func.then(function(result){
            scope.backupResult = result;
            scope.data = result? result: [];
            scope.pagination.totalItems = result.totalNumber;
            deferred.resolve(result);
          }, function(){
            scope.data = [];
            deferred.resolve();
          });
        }

        if (scope.internalControl && scope.internalControl.onRefresh){
          scope.internalControl.onRefresh();
        }

        return deferred.promise;
      }

      scope.sendRequest();

      scope.pageChanged = function(){
        scope.query.filterObj.offset = (scope.pagination.currentPage - 1) * scope.pagination.itemsPerPage;
        
        scope.sendRequest();
      };

      scope.switchOrder = function(sortOrder) {
        if (sortOrder === 'DESC') {
          return 'ASC';
        } else {
          return 'DESC';
        }
      };
      
      scope.updateFilter = function() {
        if (scope.pagination.applyChanges){ 
          scope.pagination.applyChanges = false; 
          scope.pagination.applyChangesText='Filter Options'; 
          scope.sendRequest(); 
        } else { 
          scope.pagination.applyChangesText='Apply'; 
          scope.pagination.applyChanges=true; 
        }        
      };

      scope.clearSort = function() {
        if (scope.internalControl.setPredicate) {
          scope.query.filterObj.sortField = scope.sortBy || 'eventDts';
          scope.internalControl.setPredicate(scope.query.filterObj.sortField);
        } else {
          scope.query.filterObj.sortField = scope.sortBy || 'eventDts';
        }
        scope.query.filterObj.sortOrder = scope.sortOrder || 'ASC';
        scope.sendRequest();
      }

      scope.checkMax = function(){
        if (scope.maxResults) {
          scope.query.filterObj.offset = 0;
          scope.query.filterObj.max = scope.maxResults;
          scope.showPagination = false;
        } else {
          scope.showPagination = true;
        }
      }

      scope.generateDownloadLink = function(){
        var url = 'api/v1/resource/';
        if (attrs.type === 'user'){
          url += 'usertracking/export?';
        } else if (attrs.type === 'component'){
          url += 'componenttracking/export?';
        } else if (attrs.type === 'article'){
          url += 'articletracking/export?';
        }
        var query = angular.copy(scope.query.filterObj);
        if (scope.maxResults) {
          query.offset = 0;
          query.max = scope.maxResults;
        } else {
          query.offset = 0;
          query.max = 0;
        }
        url += query.toQuery();
        return url;
      }

      scope.toTop = function(){
        jQuery('html,body').animate({scrollTop:0},0);
      }

      scope.setPageMax = function(maxPerPage){
        if (maxPerPage){
          scope.query.filterObj.max = maxPerPage;
          scope.pagination.itemsPerPage = maxPerPage;
        } else {
          scope.query.filterObj.max = 20;
          scope.pagination.itemsPerPage = 20;
        }
      }

      scope.changeSortOrder = function(field){
        if (scope.oldField && scope.oldField === field) {
          scope.query.filterObj.sortField = field;
          scope.query.filterObj.sortOrder = scope.switchOrder(scope.query.filterObj.sortOrder);
        } else {
          scope.query.filterObj.sortField = field;
          scope.query.filterObj.sortOrder = scope.sortOrder || 'ASC';
        }
        scope.oldField = field;
        scope.sendRequest();
      }

      scope.getEventType = function(code){
        if (scope.eventCodes && scope.eventCodes.length){
          var found = _.find(scope.eventCodes, {'code': code});
          if (found) {
            return found.description;
          } else {
            return code;
          }
        }
        return code;
      }
      scope.getUserType = function(code){
        if (scope.userCodes && scope.userCodes.length){
          var found = _.find(scope.userCodes, {'code': code});
          if (found) {
            return found.description;
          } else {
            return code;
          }
        }
        return code;
      }

      scope.getDate = function(d) {
        return utils.getDate(d);
      }

      scope.internalControl.refresh = function(){
        return scope.sendRequest();
      }
      scope.internalControl.changeSortOrder = function(field){
        scope.changeSortOrder(field);
      }
      scope.control = scope.internalControl;
    }
  };
}]);
