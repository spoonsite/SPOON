<%--
Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){
			
			
			var contents = Ext.create('OSF.ux.IFrame', {
				frameName: 'toolFrame',
				src: ''
			});
			
			var contentPanel = Ext.create('Ext.panel.Panel', {
				frame: true,
				layout: 'fit',
				items: [
					contents
				]
			});
			
			var pageMap = [];
			pageMap['Articles'] = '/openstorefront/admin?tool=Articles';
			pageMap['Attributes'] = 'Router.action?page=admin/data/attributes.jsp';
			pageMap['Dashboard'] = 'Router.action?page=shared/dashboard.jsp';
			pageMap['Contacts'] = 'Router.action?page=admin/data/contacts.jsp';
			pageMap['Entries'] = 'Router.action?page=admin/data/components.jsp';			
			pageMap['Entry-Types'] = 'Router.action?page=admin/data/entryType.jsp';
			pageMap['Highlights'] = 'Router.action?page=admin/data/highlights.jsp';
			pageMap['Integrations'] = 'Router.action?page=admin/data/integrations.jsp';
			pageMap['Imports'] = 'Router.action?page=admin/data/imports.jsp';
			pageMap['Lookups'] = 'Router.action?page=admin/data/lookup.jsp';
			pageMap['Media'] = 'Router.action?page=admin/data/media.jsp';
			pageMap['Organizations'] = 'Router.action?page=admin/data/organizations.jsp';	
			pageMap['Questions'] = 'Router.action?page=admin/data/user-data/questions.jsp';
			pageMap['Reviews'] = 'Router.action?page=admin/data/user-data/reviews.jsp';
			pageMap['Watches'] = 'Router.action?page=admin/data/user-data/watches.jsp';
			pageMap['Tags'] = 'Router.action?page=admin/data/user-data/tags.jsp';
			pageMap['User-Profiles'] = 'Router.action?page=admin/data/user-data/userProfiles.jsp';		
			pageMap['Alerts'] = 'Router.action?page=admin/application/alerts.jsp';
			pageMap['Branding'] = 'Router.action?page=admin/application/branding.jsp';
			pageMap['Jobs'] = 'Router.action?page=admin/application/jobs.jsp';
			pageMap['Reports'] = 'Router.action?page=shared/reports.jsp';
			pageMap['System'] = 'Router.action?page=admin/application/system.jsp';
			pageMap['Tracking'] = 'Router.action?page=admin/application/tracking.jsp';
			pageMap['Messages'] = 'Router.action?page=admin/application/messages.jsp';
			pageMap['Entry-Template'] = 'Router.action?page=admin/data/entryTemplate.jsp';
			pageMap['Searches'] = 'Router.action?page=admin/data/searches.jsp';
			pageMap['Feedback'] = 'Router.action?page=admin/application/feedback.jsp';
			pageMap['Relationships'] = 'Router.action?page=admin/data/relationships.jsp';
			

		//Data Menu
			var dataMenu = [];
			dataMenu.push({
				text: 'Attributes',
				handler: function(){
					actionLoadContent('Attributes');
				}
			});
			dataMenu.push({
				text: 'Contacts',
				handler: function(){
					actionLoadContent('Contacts');
				}
			});			
			dataMenu.push({
				text: 'Entries',
				handler: function(){
					actionLoadContent('Entries');
				}
			});
			dataMenu.push({
				text: 'Entry Types',
				handler: function(){
					actionLoadContent('Entry-Types');
				}
			});
			dataMenu.push({
				text: 'Entry Templates',
				handler: function(){
					actionLoadContent('Entry-Template');
				}
			});			
			dataMenu.push({
				text: 'Highlights',
				handler: function(){
					actionLoadContent('Highlights');
				}
			});
			dataMenu.push({
				text: 'Integrations',
				handler: function(){
					actionLoadContent('Integrations');
				}
			});
			dataMenu.push({
				text: 'Imports',
				handler: function(){
					actionLoadContent('Imports');
				}
			});
			dataMenu.push({
				text: 'Lookups',
				handler: function(){
					actionLoadContent('Lookups');
				}
			});
			dataMenu.push({
				text: 'Media',
				handler: function(){
					actionLoadContent('Media');
				}
			});
			dataMenu.push({
				text: 'Organizations',
				handler: function(){
					actionLoadContent('Organizations');
				}				
			});	
			dataMenu.push({
				text: 'Relationships',
				handler: function(){
					actionLoadContent('Relationships');
				}				
			});			
			dataMenu.push({
				text: 'Searches',
				handler: function(){
					actionLoadContent('Searches');
				}				
			});	
			dataMenu.push({
				text: 'User Data',
				menu: {
					items: [
						{
							text: 'Questions',
							handler: function(){
								actionLoadContent('Questions');
							}							
						},
						{
							text: 'Reviews',
							handler: function(){
								actionLoadContent('Reviews');
							}							
						},
						{
							text: 'Watches',
							handler: function(){
								actionLoadContent('Watches');
							}							
						},						
						{
							text: 'Tags',
							handler: function(){
								actionLoadContent('Tags');
							}							
						},
						{
							text: 'User Profiles',
							handler: function(){
								actionLoadContent('User-Profiles');
							}							
						}
					]
				}
			});
			
			var alertMenu = [];
			alertMenu.push({
				text: 'Alerts',
				handler: function(){
					actionLoadContent('Alerts');
				}
			});			
			alertMenu.push({
				text: 'Branding',
				handler: function(){
					actionLoadContent('Branding');
				}				
			});	
			alertMenu.push({
				text: 'Feedback',
				handler: function(){
					actionLoadContent('Feedback');
				}				
			});			
			alertMenu.push({
				text: 'Jobs',
				handler: function(){
					actionLoadContent('Jobs');
				}				
			});
			alertMenu.push({
				text: 'Messages',
				handler: function(){
					actionLoadContent('Messages');
				}				
			});			
			alertMenu.push({
				text: 'Reports',
				handler: function(){
					actionLoadContent('Reports');
				}				
			});
			alertMenu.push({
				text: 'System',
				handler: function(){
					actionLoadContent('System');
				}				
			});
			alertMenu.push({
				text: 'Tracking',
				handler: function(){
					actionLoadContent('Tracking');
				}				
			});			
			alertMenu.push({
				xtype: 'menuseparator'				
			});
			alertMenu.push({
				text: 'API Documentation',
				href: 'API.action',
				hrefTarget: '_blank'
			});			

			var notificationWin = Ext.create('OSF.component.NotificationWindow', {				
			});	

			var feedbackWin = Ext.create('OSF.component.FeedbackWindow',{				
			});

			var helpWin = Ext.create('OSF.component.HelpWindow', {				
			});	

			Ext.create('Ext.container.Viewport', {
				layout: 'border',
				items: [{
					xtype: 'panel',
					region: 'north',
					id: 'topNavPanel',
					border: false,
					//margin: '0 0 5 0',
					cls: 'border_accent',
					dockedItems: [						
						{
							xtype: 'toolbar',
							dock: 'top',								
							cls: 'nav-back-color',
							items: [
								{
									xtype: 'image',																	
									height: 53,
									cls: 'linkItem',
									title: 'Go back to Home Page',
									src: '${branding.secondaryLogoUrl}',
									listeners: {
										el: {
											click: function() {
												window.location.replace('index.jsp');
											}
										}
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'tbtext',
									text: 'Admin Tools',
									id: 'titleTextId',
									cls: 'page-title'
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'button',
									scale   : 'large',
									ui: 'default',
									iconCls: 'fa fa-2x fa-envelope icon-top-padding',
									iconAlign: 'left',
									text: 'Notifications',
									handler: function() {
										notificationWin.show();
										notificationWin.refreshData();
									}
								},								
								{
									xtype: 'button',
									id: 'userMenuBtn',
									scale   : 'large',
									ui: 'default',
									text: 'User Menu',
									minWidth: 150,
									maxWidth: 250,
									menu: {						
										items: [
											{
												text: 'Home',
												iconCls: 'fa fa-home',
												href: 'index.jsp'
											},
											{
												text: 'User Tools',
												iconCls: 'fa fa-user',
												href: 'usertools.jsp'
											},											
											{
												xtype: 'menuseparator'
											},
											{
												text: '<b>Help</b>',
												iconCls: 'fa fa-question-circle',
												handler: function() {
													helpWin.show();
												}
											},
											{
												text: '<b>Feedback / issues</b>',
												iconCls: 'fa fa-commenting',
												handler: function() {
													feedbackWin.show();
												}
											},
											{
												xtype: 'menuseparator'
											},
											{
												text: 'Logout',
												iconCls: 'fa fa-sign-out',
												href: 'Login.action?Logout'												
											}
										],
										listeners: {
											beforerender: function () {
											 this.setWidth(this.up('button').getWidth());
											}
										}
									}
								}
							]
						}						
					]
				},
				{
					region: 'center',
					xtype: 'panel',
					layout: 'fit',					
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',														
							items:[
								{
									text: 'Dashboard',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-home',
									handler: function(){
										actionLoadContent('Dashboard');
									}									
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Data Management',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-database',
									menu: {										
										items: dataMenu,
										listeners: {
											beforerender: function () {
											 this.setWidth(this.up('button').getWidth());
											}					
										}
									}
								},
								{
									text: 'Application Management',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-gears',
									menu: {										
										items: alertMenu,
										listeners: {
											beforerender: function () {
											 this.setWidth(this.up('button').getWidth());
											}					
										}
									}
								}
							]
						}
					],					
					items: [
						contentPanel
					]
				}]
			});		
			
			CoreService.brandingservice.getCurrentBranding().then(function(response, opts){
				var branding = Ext.decode(response.responseText);
				if (branding.securityBannerText && branding.securityBannerText !== '') {
					Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
						securityBannerText: branding.securityBannerText
					}), 0);
				}
			});				

			CoreService.usersevice.getCurrentUser().then(function(response, opts){
				var usercontext = Ext.decode(response.responseText);
				
				var userMenuText = usercontext.username;
				if (usercontext.firstName && usercontext.lastName)
				{
					userMenuText = usercontext.firstName + ' ' + usercontext.lastName;
				}
				Ext.getCmp('userMenuBtn').setText(userMenuText);				
				setupServerNotifications(usercontext);	
			});

			var actionLoadContent = function(key) {
				window.location.href = 'admin.jsp?load=' + key;
			};
			/*
			var historyToken = Ext.util.History.getToken();			
			if (historyToken) {
				actionLoadContent(historyToken);
			} else {	
				actionLoadContent('Dashboard');
			}	
			*/
			
			var paramRoute = '${param.load}';
			if (paramRoute && paramRoute !== '') {
				var url = pageMap[paramRoute];
				if (url){
					Ext.getCmp('titleTextId').setText('Admin Tools - ' + paramRoute.replace('-', ' '));
					contents.load(url);				
					//Ext.util.History.add(key);				
				} else {
					Ext.toast("Page key Not Found");
					contents.load(pageMap['Dashboard']);	
				}
			}  else {
				Ext.getCmp('titleTextId').setText('Admin Tools - Dashboard');
				contents.load(pageMap['Dashboard']);
			}			
			
		});
			
	</script>
    </stripes:layout-component>
</stripes:layout-render>
        
