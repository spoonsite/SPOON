<%-- 
/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
    Document   : adminheader
    Created on : Sep 19, 2016, 12:44:55 PM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.openstorefront.web.action.BaseToolAction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<!DOCTYPE html>
<stripes:layout-definition>
	
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){
			
			/**
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
		**/
			
			var pageMap = [];
			<%
				BaseToolAction baseToolAction = (BaseToolAction)request.getAttribute("actionBean");
				for (String key : baseToolAction.getPageMap().keySet()) {
					out.println("			pageMap['" + key + "'] = '" + baseToolAction.getPageMap().get(key).getPage() + "';");
				}
			%>


		//Data Menu
		var dataMenu = [];
			dataMenu.push({
				text: 'Attributes',
				permission: 'ADMIN-ATTRIBUTE-MANAGEMENT',
				handler: function(){
					actionLoadContent('Attributes');
				}
			});
			dataMenu.push({
				text: 'Contacts',
				permission: 'ADMIN-CONTACT-MANAGEMENT',
				handler: function(){
					actionLoadContent('Contacts');
				}
			});			
			dataMenu.push({
				text: 'Entries',
				permission: 'ADMIN-ENTRY-MANAGEMENT',
				handler: function(){
					actionLoadContent('Entries');
				}
			});
			dataMenu.push({
				text: 'Entry Types',
				permission: 'ADMIN-ENTRY-TYPES',
				handler: function(){
					actionLoadContent('Entry-Types');
				}
			});
			dataMenu.push({
				text: 'Entry Templates',
				permission: 'ADMIN-ENTRY-TEMPLATES',
				handler: function(){
					actionLoadContent('Entry-Template');
				}
			});			
			dataMenu.push({
				text: 'Highlights',
				permission: 'ADMIN-HIGHLIGHTS',
				handler: function(){
					actionLoadContent('Highlights');
				}
			});
			dataMenu.push({
				text: 'Integrations',
				permission: 'ADMIN-INTEGRATION',
				handler: function(){
					actionLoadContent('Integrations');
				}
			});
			dataMenu.push({
				text: 'Imports',
				permission: 'ADMIN-DATA-IMPORT-EXPORT',
				handler: function(){
					actionLoadContent('Imports');
				}
			});
			dataMenu.push({
				text: 'Lookups',
				permission: 'ADMIN-LOOKUPS',
				handler: function(){
					actionLoadContent('Lookups');
				}
			});
			dataMenu.push({
				text: 'Media',
				permission: 'ADMIN-MEDIA',
				handler: function(){
					actionLoadContent('Media');
				}
			});
			dataMenu.push({
				text: 'Organizations',
				permission: 'ADMIN-ORGANIZATION',
				handler: function(){
					actionLoadContent('Organizations');
				}				
			});	
			dataMenu.push({
				text: 'Relationships',
				permission: 'ADMIN-ENTRY-MANAGEMENT',
				handler: function(){
					actionLoadContent('Relationships');
				}				
			});			
			dataMenu.push({
				text: 'Searches',
				permission: 'ADMIN-SEARCH',
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
							permission: 'ADMIN-QUESTIONS',
							handler: function(){
								actionLoadContent('Questions');
							}							
						},
						{
							text: 'Reviews',
							permission: 'ADMIN-REVIEW',
							handler: function(){
								actionLoadContent('Reviews');
							}							
						},
						{
							text: 'Watches',
							permission: 'ADMIN-WATCHES',
							handler: function(){
								actionLoadContent('Watches');
							}							
						},						
						{
							text: 'Tags',
							permission: 'ADMIN-ENTRY-MANAGEMENT',
							handler: function(){
								actionLoadContent('Tags');
							}							
						},
						{
							text: 'User Profiles',
							permission: 'ADMIN-USER-MANAGEMENT-PROFILES',
							handler: function(){
								actionLoadContent('User-Profiles');
							}							
						}
					],
					width: '150px',
				}
			});
			
			var evaluationMenu = [];
			evaluationMenu.push({
				text: 'Evaluations',
				permission: 'ADMIN-EVALUATION-MANAGEMENT',
				handler: function(){
					actionLoadContent('Evaluations');
				}				
			});
			evaluationMenu.push({
				text: 'Templates',
				menu: {
					items: [
						{
							text: 'Evaluation Templates',
							permission: 'ADMIN-EVALUATION-TEMPLATE',
							handler: function(){
								actionLoadContent('Evaluation-Templates');
							}							
						},
						{
							text: 'Checklist Templates',
							permission: 'ADMIN-EVALUATION-TEMPLATE-CHECKLIST',
							handler: function(){
								actionLoadContent('Checklist-Templates');
							}							
						},
						{
							text: 'Checklist Questions',
							permission: 'ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION',
							handler: function(){
								actionLoadContent('Checklist-Questions');
							}							
						},							
						{
							text: 'Section Templates',
							permission: 'ADMIN-EVALUATION-TEMPLATE-SECTION',
							handler: function(){
								actionLoadContent('Section-Templates');
							}							
						}											
					],
					width: '200px',
				}
			});

			
			
			
			var appMenu = [];
			appMenu.push({
				text: 'Alerts',
				permission: 'ADMIN-ALERT-MANAGEMENT',
				handler: function(){
					actionLoadContent('Alerts');
				}
			});			
			appMenu.push({
				text: 'Branding',
				permission: 'ADMIN-BRANDING',
				handler: function(){
					actionLoadContent('Branding');
				}				
			});	
			appMenu.push({
				text: 'Feedback',
				permission: 'ADMIN-FEEDBACK',
				handler: function(){
					actionLoadContent('Feedback');
				}				
			});			
			appMenu.push({
				text: 'Jobs',
				permission: 'ADMIN-JOB-MANAGEMENT',
				handler: function(){
					actionLoadContent('Jobs');
				}				
			});
			appMenu.push({
				text: 'Messages',
				permission: 'ADMIN-MESSAGE-MANAGEMENT',
				handler: function(){
					actionLoadContent('Messages');
				}				
			});			
			appMenu.push({
				text: 'Reports',
				permission: 'REPORTS',
				handler: function(){
					actionLoadContent('Reports');
				}				
			});
			appMenu.push({
				text: 'Security',
				permission: 'ADMIN-SECURITY',
				handler: function(){
					actionLoadContent('Security');
				}				
			});
			appMenu.push({
				text: 'Security Roles',
				permission: 'ADMIN-ROLE-MANAGEMENT',
				handler: function(){
					actionLoadContent('Security-Roles');
				}				
			});		
			appMenu.push({
				text: 'System',
				permission: 'ADMIN-SYSTEM-MANAGEMENT',
				handler: function(){
					actionLoadContent('System');
				}				
			});
			appMenu.push({
				text: 'System Archives',
				permission: 'ADMIN-SYSTEM-MANAGEMENT',
				handler: function(){
					actionLoadContent('System-Archives');
				}				
			});			
			appMenu.push({
				text: 'Tracking',
				permission: 'ADMIN-TRACKING',
				handler: function(){
					actionLoadContent('Tracking');
				}				
			});
			appMenu.push({
				text: 'User Management',
				itemId: 'usermanagementMenu',
				permission: 'ADMIN-USER-MANAGEMENT',
				handler: function(){
					actionLoadContent('User-Management');
				}				
			});				
			appMenu.push({
				xtype: 'menuseparator'				
			});
			appMenu.push({
				text: 'API Documentation',
				permission: 'API-DOCS',
				href: 'API.action',
				hrefTarget: '_blank'
			});			

			var notificationWin = Ext.create('OSF.component.NotificationWindow', {				
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
									alt: 'logo',
									cls: 'linkItem',
									title: 'Go back to Home Page',
									src: '${branding.secondaryLogoUrl}',
									listeners: {
										el: {
											click: function() {
												window.location.replace('Landing.action');
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
									iconCls: 'fa fa-2x fa-envelope-o',
									iconAlign: 'left',
									text: 'Notifications',
									handler: function() {
										notificationWin.show();
										notificationWin.refreshData();
									}
								},	
								Ext.create('OSF.component.UserMenu', {
									showAdminTools: false,
									ui: 'default',
									initCallBack: function(usercontext) {
										setupServerNotifications(usercontext);	
									}
								})
							]
						}						
					]
				},
				{
					region: 'center',
					id: 'mainViewPortPanel',
					xtype: 'panel',
					layout: 'fit',
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							overflowHandler: 'scroller',
							items:[
								{
									text: 'Dashboard',
									id: 'dashboardAdminHomeButton',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-home icon-button-color-default',
									margin: '0 10 0 0',
									handler: function(){
										actionLoadContent('Dashboard');
									}									
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Data Management',
									id: 'dataManagementBtn',
									hidden: true,
									scale   : 'large',
									iconCls: 'fa fa-2x fa-database icon-button-color-default',
									margin: '0 13 0 0',
									menu: {																				
										listeners: {
											beforerender: function () {
											 this.setWidth(this.up('button').getWidth());
											}					
										}
									}
								},
								{
									text: 'Evaluation Management',
									id: 'evaluationManagementBtn',
									hidden: true,
									scale   : 'large',
									iconCls: 'fa fa-2x fa-file-text-o icon-button-color-default',
									margin: '0 15 0 0',
									menu: {																				
										listeners: {
											beforerender: function () {
											 this.setWidth(this.up('button').getWidth());
											}					
										}
									}
								},										
								{
									text: 'Application Management',
									id: 'applicationManagementBtn',
									hidden: true,
									scale   : 'large',
									iconCls: 'fa fa-2x fa-gears icon-button-color-default',
									menu: {																				
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
				}]
			});		
			
			CoreService.brandingservice.getCurrentBranding().then(function(branding){				
				if (branding.securityBannerText && branding.securityBannerText !== '') {
					Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
						securityBannerText: branding.securityBannerText
					}), 0);
				}
			});				

			CoreService.userservice.getCurrentUser().then(function(user){
								
				var addMenuItems = function(menuToAddTo, menuToCheck) {
					
					Ext.Array.each(menuToCheck, function(menuItem){
						if (menuItem.menu) {
							var tempMenuItems = [];
							addMenuItems(tempMenuItems, menuItem.menu.items);
							if (tempMenuItems.length > 0) {
								menuItem.menu.items = tempMenuItems;
								menuToAddTo.push(menuItem);
							}
						} else {
							if (menuItem.permission) {
								if (CoreService.userservice.userHasPermisson(user, menuItem.permission)) {
									menuToAddTo.push(menuItem);
								}						
							} else {
								menuToAddTo.push(menuItem);
							}
						}
					});
					
				};
				var dataMenuItems = [];
				addMenuItems(dataMenuItems, dataMenu);

				if (dataMenuItems.length > 0) {
					Ext.getCmp('dataManagementBtn').setHidden(false);
					Ext.getCmp('dataManagementBtn').getMenu().add(dataMenuItems);
				}
				
				var evalMenuItems = [];
				addMenuItems(evalMenuItems, evaluationMenu);				
				if (evalMenuItems.length > 0) {
					Ext.getCmp('evaluationManagementBtn').setHidden(false);
					Ext.getCmp('evaluationManagementBtn').getMenu().add(evalMenuItems);
				}
				
				var appMenuItems = [];
				addMenuItems(appMenuItems, appMenu);				
				if (appMenuItems.length > 0) {
					Ext.getCmp('applicationManagementBtn').setHidden(false);
					Ext.getCmp('applicationManagementBtn').getMenu().add(appMenuItems);
				}				
				
				//Hide built in security page if not needed
				Ext.Ajax.request({
					url: 'api/v1/service/security/realmname',
					success: function(response, opts) {
						var data = Ext.decode(response.responseText);

						Ext.Array.each(data, function(realm) {
							if (realm.code !== 'StorefrontRealm') {								
								var userManagementMenu = Ext.getCmp('applicationManagementBtn').queryById('usermanagementMenu');
								if (userManagementMenu) {
									userManagementMenu.setHidden(true);
								}
							}
						});
					}
				});	
				
			});	

			var actionLoadContent = function(key) {
				window.location.href = 'AdminTool.action?load=' + key;
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
					//contents.load(url);				
					//Ext.util.History.add(key);				
				} else {
					Ext.toast("Page key Not Found");
					//contents.load(pageMap['Dashboard']);	
				}
			}  else {
				Ext.getCmp('titleTextId').setText('Admin Tools - Dashboard');
				//contents.load(pageMap['Dashboard']);
			}			
			
		});
		
		var addComponentToMainViewPort = function(component) {			
			Ext.getCmp('mainViewPortPanel').add(component);
			Ext.getCmp('mainViewPortPanel').updateLayout(true, true);
		};
			
	</script>
</stripes:layout-definition>