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

app.directive('architecture',['business', '$timeout', function (Business, $timeout) {
  return {
    templateUrl: 'views/component/architecture.html',
    restrict: 'E',
    scope: {},
    compile: function(tElem, tAttrs){
      return {
        pre: function(scope, element, attrs){
          console.log('scope', scope);
          
          scope.tree = {};
          scope.tree.data = [];
          scope.myTree = {};
          scope.branch = null;
          var setupChildren = function(child) {
            var temp = {
              'label': child.name, 
              'detailedDesc': child.description,
              'key': child.attributeCode + child.attributeType,
              'attributeType': child.attributeType,
              'attributeCode': child.attributeCode,
            };
            temp.children = []
            _.each(child.children, function(item){
              temp.children.push(setupChildren(item));
            })
            return temp;
          }
          Business.articleservice.getArchitecture('DI2E-SVCV4-A').then(function(result){
            if (result) {
              console.log('result', result);
              scope.tree.data.push(setupChildren(result));
              // console.log('scope.tree', scope.tree);
            }
          }, function(){
            scope.tree.data = [];
          });
        }, //
        post: function(scope, iElem, iAttrs){
          console.log('scope', scope);
          
          scope.search = '';
          scope.full = false;

          scope.$watch('full', function(){
            if (scope.full){
              $('body').css('overflow','hidden');
            } else {
              $('body').css('overflow', 'auto');
            }
          })
          
          scope.selected = function(branch){
            console.log('branch', branch);
            scope.branch = branch;
            scope.myTree.selectBranch(branch);
          }
          scope.log = function(){
            // console.log('You pressed enter on the input: ', scope.search);
          }
          scope.expandAll = function(){
            scope.myTree.expandAll();
          };
          scope.collapseAll = function(){
            scope.myTree.collapseAll();
          };
        }
      }
    }
  }
}]);
