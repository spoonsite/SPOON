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

app.controller('AdminCtrl', ['$scope', 'business', function ($scope, Business) {

  //this object is used to contain the tree functions
  $scope.myTree = {};

  $scope.collection = null;
  $scope.collectionSelection = null;
  $scope.data = [];
  $scope.incLoc = '';
  $scope.saveContent = '';
  $scope.editedTopic = 'Types';
  $scope.toolTitle = 'idAM Landing Page';

  Business.getFilters().then(function(result) {
    if (result) {
      $scope.filters = angular.copy(result);
    } else {
      $scope.filters = null;
    }
  });

  /***************************************************************
  * If we don't have a landing page, we're going to set up one for now so that
  * there will always be one in the editor when we look, unless we click on a button
  * that says 'add landing page'
  ***************************************************************/
  if (!$scope.landingRoute) {
    Business.landingPage('IDAM', 'views/temp/landingpage.html', true).then(function (result) { /*jshint unused:false*/
      $scope.landingRoute = result.value;
    });
  }
  

  /***************************************************************
  * This function watches the landingRoute so that when that changes, we load
  * the new content into the editor that we want to be able to look at.
  ***************************************************************/
  $scope.$watch('landingRoute', function() {
    $.get($scope.landingRoute).then(function(responseData) {
      $scope.editorContent = $scope.parseForEditor(responseData);
      $scope.$apply();
    });
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
  $scope.editor = function(branch) {
    $scope.incLoc = branch.location;
    $scope.toolTitle = branch.toolTitle;
    $scope.myTree.selectBranch(branch);
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
  $scope.editLanding = function(route) {
    var branch = checkCollection($scope.data, 0, 'landing');
    $scope.landingRoute = route;
    $scope.editor(branch);
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
    if (content !== null && content !== undefined && content !== '') {
      var data = content;
      // console.log('data', data);
      var splitData = data.split('### Component List ###');
      // console.log('sp', splitData);
      data = splitData.join('\n<component-list click-callback="updateDetails" class-list="" data="data" cols="3" search="doSearchKey" ></component-list>\n');
      // console.log('data', data);
      return data;
    }
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
      $(doc).find('component-list').replaceWith('### Component List ###');
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


  // setup editor options
  $scope.editorOptions = getCkConfig();

  /***************************************************************
  * This function sets up the menu on the left for the admin tools.
  * It isn't completely dynamic right now because the tools are pretty static.
  * If we ever actually put this information into the database, things might change.
  ***************************************************************/
  (function() {
    var attributes = {};
    attributes.label = 'Manage Attributes';
    attributes.location='views/admin/editattributes.html';
    attributes.children = [];
    attributes.toolTitle = 'Manage Attributes';
    attributes.key = 'attributes';
    attributes.parentKey = null;
    attributes.data = $scope.filters;
    attributes.children.push({'label':'Manage Codes', 'location':'views/admin/editcodes.html', 'toolTitle': 'Manage Attribute Codes', 'key': 'codes', 'parentKey': 'attributes'});
    attributes.children.push({'label':'Manage Landing Pages', 'location':'views/admin/editlanding.html', 'toolTitle': 'Manage Attribute Landing Pages', 'key': 'landing', 'parentKey': 'attributes'});

    var lookupTables = {
      label: 'Manage Lookups',
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


    $scope.data.push({'label': 'Integration Management', 'location':'views/admin/configuration/default.html', 'toolTitle': 'Integration Management', 'key': 'integration' });
    
    $scope.data.push({'label': 'User Profiles', 'location':'views/admin/manageUserProfiles.html', 'toolTitle': 'User Profile Management', 'key': 'USER_PROFILE' });
    $scope.data.push({'label': 'User Messages', 'location':'views/admin/manageUserMessages.html', 'toolTitle': 'User Message Management', 'key': 'USER_MESSAGE' });
    $scope.data.push({'label': 'Job', 'location':'views/admin/manageJobs.html', 'toolTitle': 'Job Management', 'key': 'JOBS' });
    $scope.data.push({'label': 'System', 'location':'views/admin/manageSystem.html', 'toolTitle': 'System Management', 'key': 'SYSTEM' });
    
    // $scope.data.push({'label': 'About Admin Tools', 'location':'views/admin/about.html', 'toolTitle': 'About Admin Tools', 'key': 'tools' });
    $scope.data.push(attributes);
    // $scope.data.push(lookupTables);

    // $scope.data.push({'label': 'Manage Components', 'location':'views/admin/editcomponents.html', 'toolTitle': 'Manage Components', 'key': 'components' });
    // $scope.data.push({'label': 'Manage Branding', 'location': 'views/admin/editbranding.html', 'toolTitle': 'Manage Branding', 'key': 'branding' });
  }());

}]);