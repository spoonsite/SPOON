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

/*global getCkConfig*/

app.controller('AdminCtrl', ['$scope', 'business', '$location', '$timeout', function ($scope, Business, $location, $timeout) {

  //this object is used to contain the tree functions
  $scope.myTree = {};
  $scope.systemTree = {};

  $scope.collection = null;
  $scope.collectionSelection = null;
  $scope.data = [];
  $scope.systemTools = [];
  $scope.incLoc = '';
  $scope.saveContent = '';
  $scope.editedTopic = 'Types';
  $scope.toolTitle = 'Admin Tools'
  $scope.menuPanel = {};
  $scope.menuPanel.data = {};
  $scope.menuPanel.data.open = true;
  $scope.menuPanel.system = {};
  $scope.menuPanel.system.open = true;
  $scope.oneAtATime = false;

  Business.getFilters().then(function(result) {
    if (result) {
      $scope.filters = angular.copy(result);
    } else {
      $scope.filters = null;
    }
  });



  /***************************************************************
  * This function watches the editor content. When the model is updated, we'll
  * call the function for saving the data.
  ***************************************************************/
  $scope.$watch('editorContent', function() {
    $scope.saveData();
  });


  /***************************************************************
  * This function changes the content in the admin tool section to the tool the
  * admin clicks on
  ***************************************************************/
  $scope.editor = function(branch, tree, otherTree) {
    $location.search('tool', branch.label);
    $scope.incLoc = branch.location;
    $scope.toolTitle = branch.toolTitle;
    $scope.detailedDesc = branch.detailedDesc;
    if (branch.toolTitle !== 'Manage Articles') {
      $scope.type = null;
      $scope.code = null;
    }
    if (tree && otherTree) {
      tree.selectBranch(branch);
      otherTree.selectBranch(branch);
    }
  };


  /***************************************************************
  * This function recursively searches through the tree in order to find the
  * branch that we want to go to (used to set location and active class)
  ***************************************************************/
  var checkCollection = function(branch, index, key) {
    while (branch[index]) {
      if (branch[index].key === key) {
        return branch[index];
      }
      if (branch[index].children && branch[index].children.length > 0) {
        return checkCollection(branch[index].children, 0, key);
      }
      index = index + 1;
    }
    return null;
  };

  /***************************************************************
  * This function takes a key, finds the branch with that key, and then
  * sends us there 
  ***************************************************************/
  $scope.editCollection = function(key) {
    var branch = checkCollection($scope.data, 0, 'codes');
    $scope.collectionSelection = _.find($scope.filters, {'type': key});
    $scope.editor(branch);
  };
  /***************************************************************
  * This function takes a key, finds the branch with that key, and then
  * sends us there 
  ***************************************************************/
  $scope.editLanding = function(type, code) {
    var branch = checkCollection($scope.data, 0, 'landing');
    $scope.type = type;
    $scope.code = code;
    $scope.editor(branch, $scope.myTree);
  };

  /***************************************************************
  * This function checks our tree to see if there was a parent collection
  ***************************************************************/
  $scope.checkParent = function() {
    var branch = $scope.myTree.getSelectedBranch();
    if (branch) {
      return $scope.myTree.getParentBranch(branch);
    } else {
      return false;
    }
  };

  /***************************************************************
  * This function takes us back to the parent tool
  ***************************************************************/
  $scope.goToParent = function () {
    $scope.editor($scope.checkParent());
  };

  /***************************************************************
  * This function is used to parse the data that may contain one of our custom
  * tags for a list of components
  ***************************************************************/
  $scope.parseComponentInsert = function (content) {
    // if (content !== null && content !== undefined && content !== '') {
    //   var data = content;
    //   // console.log('data', data);
    //   var splitData = data.split('### Component List ###');
    //   // console.log('sp', splitData);
    //   data = splitData.join('\n<component-list click-callback="updateDetails" class-list="" data="data" cols="3" search="doSearchKey" ></component-list>\n');
    //   // console.log('data', data);
    //   return data;
    // }
    return content;
  };

  /***************************************************************
  * This function is used to parse the data that may contain one of our custom
  * tags for the editor to be able to represent (since it doesn't like our tags)
  ***************************************************************/
  $scope.parseForEditor = function(content) {
    if (content !== null && content !== undefined && content !== '') {
      var parser = new DOMParser();
      var doc = parser.parseFromString(content, 'text/html');
      // $(doc).find('component-list').replaceWith('### Component List ###');
      return $(doc).find('body').html();
    }
    return content;
  };

  /***************************************************************
  * This action is called when the editor is saved on a landing page
  ***************************************************************/
  $scope.saveData = function() {
    $scope.saveContent = $scope.parseComponentInsert($scope.editorContent);
    // console.log('Save', $scope.saveContent);
  };


  /***************************************************************
  * This function sets up the menu on the left for the admin tools.
  * It isn't completely dynamic right now because the tools are pretty static.
  * If we ever actually put this information into the database, things might change.
  ***************************************************************/
  (function() {
    var attributes = {};
    attributes.label = 'Attributes';
    attributes.location='views/admin/data_management/editattributes.html';
    attributes.children = [];
    attributes.toolTitle = 'Manage Attributes';
    attributes.detailedDesc = "Attributes are used to categorize components and other listings.  They can be searched on and  filtered.  They represent the metadata for a listing.  Attribute Types represent a category and a code respresents a specific value.  The data is linked by the type and code which allows for a simple change of the description.";
    attributes.key = 'attributes';
    attributes.parentKey = null;
    attributes.data = $scope.filters;   
    // attributes.children.push({'label':'Manage Codes', 'location':'views/admin/data_management/editcodes.html', 'toolTitle': 'Manage Attribute Codes', 'key': 'codes', 'parentKey': 'attributes'});
    // attributes.children.push({'label':'Manage Landing Pages', 'location':'views/admin/managelanding.html', 'toolTitle': 'Manage Attribute Landing Pages', 'key': 'landing', 'parentKey': 'attributes'});

    var lookupTables = {
      label: 'Lookups',
      location:  'views/admin/manageLookups.html',
      children:  [
        //
        {
          label: 'Reviews PROs List',
          location: 'views/admin/manageProsList.html',
          toolTitle: 'Manage PROs List',
          key: 'REVIEW_PROS',
          parentKey: 'LOOKUP'
        },
        {
          label: 'Reviews CONs List',
          location: 'views/admin/manageConsList.html',
          toolTitle: 'Manage CONs List',
          key: 'REVIEW_CONS',
          parentKey: 'LOOKUP'
        }
      //
      ],
      toolTitle: 'Manage lookups tables',
      key: 'LOOKUP',
      parentKey: null
    };


    $scope.data.push({
      'label': 'Articles', 
      'location':'views/admin/managelanding.html', 
      'toolTitle': 'Manage Articles',
      'detailedDesc': "Articles, also called topic landing pages, are detail pages of topics of interest with optional related listings.  Articles are assigned to an Attribute Code which allows for searching and filter by topic. ",
      'key': 'landing'
    });

    $scope.data.push(attributes);
    
    $scope.data.push({
      'label': 'Components', 
      'location':'views/admin/managecomponents.html', 
      'toolTitle': 'Manage Components', 
      'detailedDesc': "Components represent the main listing item in the application.  This tool allows for manipulating all data related to a component.",
      'key': 'components' 
    });

    $scope.data.push({
      'label': 'Highlights', 
      'location':'views/admin/managehighlights.html', 
      'toolTitle': 'Manage Highlights', 
      'detailedDesc': "Allows for the configuration of highlights that show up on the front page",
      'key': 'highlights' 
    });

    $scope.data.push({
      'label': 'Integration Management', 
      'location':'views/admin/configuration/default.html', 
      'toolTitle': 'Integration Management', 
      'detailedDesc': "Allows for the configuration of data integration with external systems such as JIRA",
      'key': 'integration' 
    });
    
    $scope.data.push({
      'label': 'Lookups', 
      'location': 'views/admin/manageLookups.html', 
      'toolTitle': 'Manage Lookups', 
      'detailedDesc': 'Lookups are  tables of valid values that are used to classify data in  a consistent way.',
      'key': 'lookups' 
    });    
    
    $scope.data.push({
      'label': 'Media', 
      'location':'views/admin/manageMedia.html', 
      'toolTitle': 'Manage General Media', 
      'detailedDesc': "Media that can be used for articles and badges",
      'key': 'media' 
    });    
    
    $scope.data.push({
      'label': 'Questions', 
      'location':'views/admin/manageQuestions.html', 
      'toolTitle': 'Manage Question', 
      'detailedDesc': "User questions and answers about a component.",
      'key': 'questions' 
    });   
    
    $scope.data.push({
      'label': 'Reviews', 
      'location':'views/admin/manageReviews.html', 
      'toolTitle': 'Manage User Reivews', 
      'detailedDesc': "User reviews and ratings about a component.",
      'key': 'reviews' 
    });    

    $scope.data.push({
      'label': 'Tags', 
      'location':'views/admin/manageTags.html', 
      'toolTitle': 'Manage Tags', 
      'detailedDesc': "Tags are user-definable labels that are associated to a component.",
      'key': 'tags' 
    });

    $scope.data.push({
      'label': 'Organizations', 
      'location':'views/admin/manageOrganizations.html', 
      'toolTitle': 'Organization Management',
      'detailedDesc': "Organizations found within the metadata in the site.",
      'key': 'ORGANIZATIONS' 
    });

    $scope.data.push({
      'label': 'User Profiles', 
      'location':'views/admin/manageUserProfiles.html', 
      'toolTitle': 'User Profile Management',
      'detailedDesc': "A user profile represents a user in the system and contains the user's information.",
      'key': 'USER_PROFILE' 
    });


    $scope.systemTools.push({
      'label': 'Alerts', 
      'location':'views/admin/application_management/manageAlerts.html', 
      'toolTitle': 'Manage Alerts',
      'detailedDesc': "Alerts are triggers set up to watch the data, that an administrator can subscribe to.",
      'key': 'alerts'
    });    

    $scope.systemTools.push({
      'label': 'Jobs', 
      'location':'views/admin/application_management/manageJobs.html',      
      'toolTitle': 'Job Management', 
      'detailedDesc': 'Allows for controling and viewing scheduled jobs and background tasks',
      'key': 'JOBS' 
    });

    $scope.systemTools.push({
      'label': 'Reports', 
      'location':'views/admin/application_management/manageReports.html', 
      'toolTitle': 'Manage Reports',
      'detailedDesc': "System generated hard reports.",
      'key': 'reports'
    });  

    $scope.systemTools.push({
      'label': 'System', 'location':'views/admin/application_management/manageSystem.html', 
      'toolTitle': 'System Management', 
      'detailedDesc': 'Allows for viewing system status and managing system properties',
      'key': 'SYSTEM'
    });

    $scope.systemTools.push({
      'label': 'Tracking', 
      'location':'views/admin/application_management/tracking.html', 
      'toolTitle': 'Manage Tracking', 
      'detailedDesc': "Track system, user, and component data.",
      'key': 'tracking' 
    });

    $scope.systemTools.push({
      'label': 'User Messages', 
      'location':'views/admin/application_management/manageUserMessages.html', 
      'toolTitle': 'User Message Management', 
      'detailedDesc': 'User messages are queued messages for users.  This primary usage is for watches.  This tool allows for viewing of queued message as well as viewing of archived messages. ',
      'key': 'USER_MESSAGE' 
    });    
    
    // $scope.data.push({'label': 'About Admin Tools', 'location':'views/admin/about.html', 'toolTitle': 'About Admin Tools', 'key': 'tools' });

    // $scope.data.push(lookupTables);

    // $scope.data.push({'label': 'Manage Branding', 'location': 'views/admin/application_management/editbranding.html', 'toolTitle': 'Manage Branding', 'key': 'branding' });
    
  }());
  $timeout(function(){ //
    var search = $location.search();
    if (!angular.equals({}, search)) {
      if (search.tool) {
        var branch = _.find($scope.data, {'label': search.tool})
        if (!branch) {
          branch = _.find($scope.systemTools, {'label': search.tool});
          if (branch) {
            $scope.editor(branch, $scope.myTree, $scope.systemTree);
          }
        } else {
          $scope.editor(branch, $scope.myTree, $scope.systemTree);
        }
      }
    } else {
      var branch = _.find($scope.data, {'label': 'Components'})
      if (branch){
        $scope.editor(branch, $scope.myTree, $scope.systemTree);
      }
    }
  }, 300)

}]);