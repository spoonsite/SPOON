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
      console.log(name + ': compile => ' + tElem.html());
      return {
        pre: function(scope, element, attrs){
          scope.tree = {};
          scope.tree.data = [];
          scope.search = '';
          scope.full = false;
          scope.myTree = {};
          scope.data = [];

          scope.$watch('full', function(){
            if (scope.full){
              $('body').css('overflow','hidden');
            } else {
              $('body').css('overflow', 'auto');
            }
          })


          var setupChildren = function(child) {
            var temp = {
              'label': child.name, 
              'detailedDesc': child.description,
              'key': child.attributeCode + child.attributeType
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
              console.log('scope.tree', scope.tree);
            }
          }, function(){
            scope.tree.data = [];
          });

          scope.log = function(){
            console.log('You pressed enter on the input: ', scope.search);
          }

          scope.editor = function(branch) {
            $scope.incLoc = branch.location;
            $scope.toolTitle = branch.toolTitle;
            $scope.detailedDesc = branch.detailedDesc;
            $scope.myTree.selectBranch(branch);
          };

          (function() {


            scope.data.push({
              'label': 'Articles', 
              'location':'views/admin/editlanding.html', 
              'toolTitle': 'Manage Articles',
              'detailedDesc': "Articles, also called topic landing pages, are detail pages of topics of interest with optional related listings.  Articles are assigned to Attribute Code which allows for searching and filter by topic. ",
              'key': 'landing'
            });

            scope.data.push({
              'label': 'Components', 
              'location':'views/admin/editcomponents.html', 
              'toolTitle': 'Manage Components', 
              'detailedDesc': "Components represent the main listing item in the application.  This tool allows for manipulating all data related to a component.",
              'key': 'components' 
            });

            scope.data.push({
              'label': 'Highlights', 
              'location':'views/admin/edithighlights.html', 
              'toolTitle': 'Manage Highlights', 
              'detailedDesc': "Allows for the configuration of highlights that show up on the front page",
              'key': 'highlights' 
            });

            scope.data.push({
              'label': 'Integration Management', 
              'location':'views/admin/configuration/default.html', 
              'toolTitle': 'Integration Management', 
              'detailedDesc': "Allows for the configuration of data integration with external systems such as JIRA",
              'key': 'integration' 
            });

            scope.data.push({
              'label': 'Jobs', 
              'location':'views/admin/manageJobs.html',      
              'toolTitle': 'Job Management', 
              'detailedDesc': 'Allows for controling and viewing scheduled jobs and background tasks',
              'key': 'JOBS' 
            });

            scope.data.push({
              'label': 'Lookups', 
              'location': 'views/admin/manageLookups.html', 
              'toolTitle': 'Manage Lookups', 
              'detailedDesc': 'Lookups are  tables of valid-values that are used to classify data in  a consistent way.',
              'key': 'lookups' 
            });    

            scope.data.push({
              'label': 'Questions', 
              'location':'views/admin/manageQuestions.html', 
              'toolTitle': 'Manage Question', 
              'detailedDesc': "User questions and answers about a component.",
              'key': 'questions' 
            });   

            scope.data.push({
              'label': 'Reviews', 
              'location':'views/admin/manageReviews.html', 
              'toolTitle': 'Manage User Reivews', 
              'detailedDesc': "User reviews and ratings about a component.",
              'key': 'reviews' 
            });    

            scope.data.push({
              'label': 'System', 'location':'views/admin/manageSystem.html', 
              'toolTitle': 'System Management', 
              'detailedDesc': 'Allows for viewing system status and managing system properties',
              'key': 'SYSTEM'
            });

            scope.data.push({
              'label': 'Tags', 
              'location':'views/admin/manageTags.html', 
              'toolTitle': 'Manage Tags', 
              'detailedDesc': "Tags are user-definable labels that are associated to a component.",
              'key': 'tags' 
            });

            scope.data.push({
              'label': 'User Profiles', 
              'location':'views/admin/manageUserProfiles.html', 
              'toolTitle': 'User Profile Management',
              'detailedDesc': "A user profile represents a user in the system and contains the user's information.",
              'key': 'USER_PROFILE' 
            });

            scope.data.push({
              'label': 'User Messages', 
              'location':'views/admin/manageUserMessages.html', 
              'toolTitle': 'User Message Management', 
              'detailedDesc': 'User messages are queued messages for users.  This primary usage is for watches.  This tool allows for viewing of queued message as well as viewing of archived messages. ',
              'key': 'USER_MESSAGE' 
            });
          }());
        }, //
        post: function(scope, iElem, iAttrs){
          console.log(name + ': post link => ' + iElem.html());
        }
      }
    }
  }
}]);
