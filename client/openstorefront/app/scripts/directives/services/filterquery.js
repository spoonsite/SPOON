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

app.directive('filterquery',['business', function (Business) {
  var returnFilterQuerySource = function(element, attrs){
    if (attrs.type === 'user') {
      return 'views/services/user.html';
    } else if (attrs.type === 'component') {
      return 'views/services/component.html';
    } else if (attrs.type === 'article') {
      return 'views/services/article.html';
    }
  }
  return {
    templateUrl: returnFilterQuerySource,
    restrict: 'E',
    scope:{
      url: '@',
      max: '@'
    },
    link: function postLink(scope, element, attrs) {
      scope.defaultMax = 50;
      scope.defaultMax = scope.max? parseInt(scope.max): 50;
      scope.today = new Date();
      scope.query = {};
      scope.query.filterObj = angular.copy(utils.queryFilter);
      scope.query.url = scope.url || '';
      scope.query.filterObj.offset = 0;
      scope.query.filterObj.max = scope.defaultMax;
      scope.pagination = {};
      scope.pagination.currentPage = 1;
      scope.pagination.itemsPerPage = scope.defaultMax;
      scope.pagination.maxSize = 6;
      scope.showPagination = true;
      scope.maxResults;
      scope.maxPerPage;
      scope.query.filterObj.sortField = 'eventDts';
      scope.query.filterObj.sortOrder = 'DESC';
      scope.popover = {
        "title": "Additional filters"
      };


      Business.lookupservice.getLookupCodes('TrackEventCode').then(function(result){
        // console.log('track event codes', result);
        
        scope.eventCodes = result? result: [];
      }, function(){
        scope.eventCodes = [];
      })
      Business.lookupservice.getLookupCodes('UserTypeCode').then(function(result){
        // console.log('track event codes', result);
        
        scope.userCodes = result? result: [];
      }, function(){
        scope.userCodes = [];
      })

      scope.sendRequest = function(){
        var query = angular.copy(scope.query);
        if (query.filterObj.end) {
          var d = new Date(query.filterObj.end);
          d.setHours(d.getHours()+24);
          query.filterObj.end = d.toISOString();
        }
        if (query.filterObj.start) {
          query.filterObj.start = new Date(query.filterObj.start).toISOString();
        }
        // console.log('We sent the request', query);
        Business.trackingservice.get(query).then(function(result){
          // console.log('result', result);
          scope.backupResult = result;
          scope.data = result? result.result: [];
          scope.pagination.totalItems = result.count;
          // console.log('pagination', scope.pagination);
        }, function(){
          scope.data = [];
          // console.log('The request failed');
        });
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

      scope.toTop = function(){
        jQuery('html,body').animate({scrollTop:0},0);
      }

      scope.setPageMax = function(){
        if (scope.maxPerPage){
          scope.query.filterObj.max = scope.maxPerPage;
          scope.pagination.itemsPerPage = scope.maxPerPage
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
          scope.query.filterObj.sortOrder = 'DESC';
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
    }
  };
}]);
