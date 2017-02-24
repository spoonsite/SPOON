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
					out.println("			pageMap['" + key + "'] = '" + baseToolAction.getPageMap().get(key) + "';");
				}
			%>


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
				text: 'Metadata',
				handler: function(){
					actionLoadContent('Metadata');
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
					],
					width: '150px',
				}
			});
			
			var evaluationMenu = [];
			evaluationMenu.push({
				text: 'Evaluations',
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
							handler: function(){
								actionLoadContent('Evaluation-Templates');
							}							
						},
						{
							text: 'Checklist Templates',
							handler: function(){
								actionLoadContent('Checklist-Templates');
							}							
						},
						{
							text: 'Checklist Questions',
							handler: function(){
								actionLoadContent('Checklist-Questions');
							}							
						},							
						{
							text: 'Section Templates',
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
				handler: function(){
					actionLoadContent('Alerts');
				}
			});			
			appMenu.push({
				text: 'Branding',
				handler: function(){
					actionLoadContent('Branding');
				}				
			});	
			appMenu.push({
				text: 'Feedback',
				handler: function(){
					actionLoadContent('Feedback');
				}				
			});			
			appMenu.push({
				text: 'Jobs',
				handler: function(){
					actionLoadContent('Jobs');
				}				
			});
			appMenu.push({
				text: 'Messages',
				handler: function(){
					actionLoadContent('Messages');
				}				
			});			
			appMenu.push({
				text: 'Reports',
				handler: function(){
					actionLoadContent('Reports');
				}				
			});
			appMenu.push({
				text: 'System',
				handler: function(){
					actionLoadContent('System');
				}				
			});
			appMenu.push({
				text: 'Tracking',
				handler: function(){
					actionLoadContent('Tracking');
				}				
			});			
			appMenu.push({
				xtype: 'menuseparator'				
			});
			appMenu.push({
				text: 'API Documentation',
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
							items:[
								{
									text: 'Dashboard',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-home icon-button-color-default',
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
									iconCls: 'fa fa-2x fa-database icon-button-color-default',
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
									text: 'Evaluation Management',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-file-text-o icon-button-color-default',
									menu: {										
										items: evaluationMenu,
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
									iconCls: 'fa fa-2x fa-gears icon-button-color-default',
									menu: {										
										items: appMenu,
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
			
			CoreService.brandingservice.getCurrentBranding().then(function(response, opts){
				var branding = Ext.decode(response.responseText);
				if (branding.securityBannerText && branding.securityBannerText !== '') {
					Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
						securityBannerText: branding.securityBannerText
					}), 0);
				}
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