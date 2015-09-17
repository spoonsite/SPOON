 'use strict';

 describe('Controller: AdminCtrl', function () {

 // load the controller's module
 beforeEach(module('openstorefrontApp'));

 var AdminCtrl, scope;

 // Initialize the controller and a mock scope
 beforeEach(inject(function ($controller, $rootScope) {
   scope = $rootScope.$new();
   AdminCtrl = $controller('AdminCtrl', {
     $scope: scope
   });
 }));

 it('should have the correct initializations', function () {
   expect(scope.collection).toEqual(null);
   expect(scope.collectionSelection).toEqual(null);
   expect(scope.incLoc).toEqual('');
   expect(scope.saveContent).toEqual('');
   expect(scope.editedTopic).toEqual('Types');
   expect(scope.toolTitle).toEqual('Admin Tools');
   expect(scope.menuPanel.data.open).toEqual(true);
   expect(scope.menuPanel.system.open).toEqual(true);
   expect(scope.oneAtATime).toEqual(false);

// Does not work on arrays {} and []
   expect(scope.myTree).toEqual({});
   expect(scope.systemTree).toEqual({});
   expect(scope.data).toEqual([
       { label: 'Articles', location: 'views/admin/managelanding.html', toolTitle: 'Manage Articles', detailedDesc: 'Articles, also called topic landing pages, are detail pages of topics of interest with optional related listings.  Articles are assigned to an Attribute Code which allows for searching and filter by topic. ', key: 'landing' },
       { label: 'Attributes', location: 'views/admin/data_management/editattributes.html', children: [  ], toolTitle: 'Manage Attributes', detailedDesc: 'Attributes are used to categorize components and other listings.  They can be searched on and  filtered.  They represent the metadata for a listing.  Attribute Types represent a category and a code respresents a specific value.  The data is linked by the type and code which allows for a simple change of the description.', key: 'attributes', parentKey: null, data: undefined },
       { label: 'Components', location: 'views/admin/managecomponents.html', toolTitle: 'Manage Components', detailedDesc: 'Components represent the main listing item in the application.  This tool allows for manipulating all data related to a component.', key: 'components' },
       { label: 'Highlights', location: 'views/admin/managehighlights.html', toolTitle: 'Manage Highlights', detailedDesc: 'Allows for the configuration of highlights that show up on the front page', key: 'highlights' },
       { label: 'Integration Management', location: 'views/admin/configuration/default.html', toolTitle: 'Integration Management', detailedDesc: 'Allows for the configuration of data integration with external systems such as JIRA', key: 'integration' }, 
       { label: 'Lookups', location: 'views/admin/manageLookups.html', toolTitle: 'Manage Lookups', detailedDesc: 'Lookups are  tables of valid values that are used to classify data in  a consistent way.', key: 'lookups' }, 
       { label: 'Media', location: 'views/admin/manageMedia.html', toolTitle: 'Manage General Media', detailedDesc: 'Media that can be used for articles and badges', key: 'media' }, 
       { label: 'Questions', location: 'views/admin/manageQuestions.html', toolTitle: 'Manage Question', detailedDesc: 'User questions and answers about a component.', key: 'questions' }, 
       { label: 'Reviews', location: 'views/admin/manageReviews.html', toolTitle: 'Manage User Reivews', detailedDesc: 'User reviews and ratings about a component.', key: 'reviews' }, 
       { label: 'Tags', location: 'views/admin/manageTags.html', toolTitle: 'Manage Tags', detailedDesc: 'Tags are user-definable labels that are associated to a component.', key: 'tags' },
       { label: 'Organizations', location: 'views/admin/manageOrganizations.html', toolTitle: 'Organization Management', detailedDesc: "Organizations found within the metadata in the site.", key: 'ORGANIZATIONS' },
       { label: 'User Profiles', location: 'views/admin/manageUserProfiles.html', toolTitle: 'User Profile Management', detailedDesc: 'A user profile represents a user in the system and contains the user\'s information.', key: 'USER_PROFILE' }
     ]);
   expect(scope.systemTools).toEqual([
       { label: 'Alerts', location: 'views/admin/application_management/manageAlerts.html', toolTitle: 'Manage Alerts', detailedDesc: 'Alerts are triggers set up to watch the data, that an administrator can subscribe to.', key: 'alerts' },
       { label: 'Jobs', location: 'views/admin/application_management/manageJobs.html', toolTitle: 'Job Management', detailedDesc: 'Allows for controling and viewing scheduled jobs and background tasks', key: 'JOBS' },
       { label: 'Reports', location: 'views/admin/application_management/manageReports.html', toolTitle: 'Manage Reports', detailedDesc: 'System generated hard reports.', key: 'reports' },
       { label: 'System', location: 'views/admin/application_management/manageSystem.html', toolTitle: 'System Management', detailedDesc: 'Allows for viewing system status and managing system properties', key: 'SYSTEM' },
       { label: 'Tracking', location: 'views/admin/application_management/tracking.html', toolTitle: 'Manage Tracking', detailedDesc: 'Track system, user, and component data.', key: 'tracking' },
       { label: 'User Messages', location: 'views/admin/application_management/manageUserMessages.html', toolTitle: 'User Message Management', detailedDesc: 'User messages are queued messages for users.  This primary usage is for watches.  This tool allows for viewing of queued message as well as viewing of archived messages. ', key: 'USER_MESSAGE' } ]);
   expect(scope.menuPanel).toEqual({data: {open: true}, system: {open: true}});
   expect(scope.menuPanel.data).toEqual({ open: true });
   expect(scope.menuPanel.system).toEqual({ open: true });
 });
 alert('Controller: AdminCtrl; should have the correct initializations = PASS (16 expects)');
});
