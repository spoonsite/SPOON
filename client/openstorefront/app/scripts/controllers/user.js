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

app.controller('UserCtrl', ['$scope', 'business', '$location', '$timeout', '$rootScope', function ($scope, Business, $location, $timeout, $rootScope) {

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
  $scope.toolTitle = 'User Tools'
  $scope.menuPanel = {};
  $scope.menuPanel.data = {};
  $scope.menuPanel.data.open = true;
  $scope.menuPanel.system = {};
  $scope.menuPanel.system.open = true;
  $scope.oneAtATime = false;
  $rootScope.navOption = {};
  $rootScope.navOption.hideNav = true;  

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
  $scope.editor = function(branch, tree) {
    $location.search('tool', branch.label);
    $scope.incLoc = branch.location;
    $scope.toolTitle = branch.toolTitle;
    $scope.detailedDesc = branch.detailedDesc;
    if (branch.toolTitle !== 'Manage Articles') {
      $scope.type = null;
      $scope.code = null;
    }
    if (tree) {
      tree.selectBranch(branch);
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
    // var attributes = {};
    // attributes.label = 'Attributes';
    // attributes.location='views/admin/data_management/editattributes.html';
    // attributes.children = [];
    // attributes.toolTitle = 'Manage Attributes';
    // attributes.detailedDesc = "Attributes are used to categorize components and other listings.  They can be searched on and  filtered.  They represent the metadata for a listing.  Attribute Types represent a category and a code respresents a specific value.  The data is linked by the type and code which allows for a simple change of the description.";
    // attributes.key = 'attributes';
    // attributes.parentKey = null;
    // attributes.data = $scope.filters;   
    // attributes.children.push({'label':'Manage Codes', 'location':'views/admin/data_management/editcodes.html', 'toolTitle': 'Manage Attribute Codes', 'key': 'codes', 'parentKey': 'attributes'});
    // attributes.children.push({'label':'Manage Landing Pages', 'location':'views/admin/managelanding.html', 'toolTitle': 'Manage Attribute Landing Pages', 'key': 'landing', 'parentKey': 'attributes'});

    // var lookupTables = {
    //   label: 'Lookups',
    //   location:  'views/admin/manageLookups.html',
    //   children:  [
    //     //
    //     {
    //       label: 'Reviews PROs List',
    //       location: 'views/admin/manageProsList.html',
    //       toolTitle: 'Manage PROs List',
    //       key: 'REVIEW_PROS',
    //       parentKey: 'LOOKUP'
    //     },
    //     {
    //       label: 'Reviews CONs List',
    //       location: 'views/admin/manageConsList.html',
    //       toolTitle: 'Manage CONs List',
    //       key: 'REVIEW_CONS',
    //       parentKey: 'LOOKUP'
    //     }
    //   //
    //   ],
    //   toolTitle: 'Manage lookups tables',
    //   key: 'LOOKUP',
    //   parentKey: null
    // };


    $scope.data.push({
      'label': 'User Profile', 
      'location':'views/user/manageprofile.html', 
      'toolTitle': 'Manage Profile',
      'detailedDesc': "Manage your personal data here.",
      'key': 'userprofile'
    });

    // $scope.data.push(attributes);
    
    $scope.data.push({
      'label': 'Watches', 
      'location':'views/user/managewatches.html', 
      'toolTitle': 'Manage Watches', 
      'detailedDesc': "Watches are your way of recieving notification of changes to components in this site.",
      'key': 'watches' 
    });

    $scope.data.push({
      'label': 'Component Reviews', 
      'location':'views/user/managereviews.html', 
      'toolTitle': 'Manage Reviews', 
      'detailedDesc': "Inspect and manage the reviews you've given here",
      'key': 'reviews' 
    });

    $scope.data.push({
      'label': 'Submissions', 
      'location':'views/user/managesubmissions.html', 
      'toolTitle': 'Submissions', 
      'detailedDesc': "Manage your component submissions here",
      'key': 'submissions' 
    });
    
    // $scope.data.push({
    //   'label': 'Preferences', 
    //   'location': 'views/user/manageprefs.html', 
    //   'toolTitle': 'Preferences', 
    //   'detailedDesc': 'Inspect and manage your personal preferences here.',
    //   'key': 'prefs' 
    // }); 

    $scope.data.push({
      'label': 'Reports', 
      'location': 'views/user/managereports.html', 
      'toolTitle': 'Reports', 
      'detailedDesc': 'Create and export reports here.',
      'key': 'reports' 
    });    
    
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