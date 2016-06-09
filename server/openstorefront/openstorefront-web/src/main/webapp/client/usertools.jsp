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
			pageMap['Dashboard'] = 'Router.action?page=shared/dashboard.jsp';
			pageMap['User-Profile'] = 'Router.action?page=user/userProfile.jsp';
			pageMap['Watches'] = 'Router.action?page=user/watches.jsp';
			pageMap['Reviews'] = 'Router.action?page=user/reviews.jsp';
			pageMap['Questions'] = 'Router.action?page=user/questions.jsp';
			pageMap['Submissions'] = 'Router.action?page=user/submissionManagement.jsp';
			pageMap['Reports'] = 'Router.action?page=shared/reports.jsp';
			pageMap['Searches'] = 'Router.action?page=user/searches.jsp';


			//Data Menu
			var toolsMenu = [];
			toolsMenu.push({
				text: 'Questions',
				handler: function(){
					actionLoadContent('Questions');
				}
			});				
			toolsMenu.push({
				text: 'Reports',
				handler: function(){
					actionLoadContent('Reports');
				}			
			});
			toolsMenu.push({
				text: 'Reviews',
				handler: function(){
					actionLoadContent('Reviews');
				}				
			});
			toolsMenu.push({
				text: 'Searches',
				handler: function(){
					actionLoadContent('Searches');
				}				
			});			
			toolsMenu.push({
				text: 'Watches',
				handler: function(){
					actionLoadContent('Watches');
				}			
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
												window.location.replace('../');
											}
										}
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'tbtext',
									id: 'titleTextId',
									text: 'User Tools',									
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
												href: '../'
											},
											{
												text: 'Admin Tools',
												id: 'menuAdminTools',
												iconCls: 'fa fa-gear',
												hidden: true,
												href: 'admin.jsp'
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
												href: '../Login.action?Logout'												
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
									text: 'Profile',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-user',
									handler: function(){
										actionLoadContent('User-Profile');
									}									
								},
								{
									text: 'Submissions',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-list',
									handler: function(){
										actionLoadContent('Submissions');
									}									
								},
								{							
									xtype: 'tbseparator'
								},										
								{
									text: 'Tools',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-wrench',
									menu: {										
										items: toolsMenu,
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
				if (usercontext.admin) {
					Ext.getCmp('menuAdminTools').setHidden(false);
				}				
				
				setupServerNotifications(usercontext);	
			});
			
			var actionLoadContent = function(key) {
				var url = pageMap[key];
				if (url){
					Ext.getCmp('titleTextId').setText('User Tools - ' + key.replace('-', ' '));
					contents.load(url);				
					Ext.util.History.add(key);				
				} else {
					Ext.toast("Page key Not Found");
					contents.load(pageMap['Dashboard']);	
				}
			};
			
			var historyToken = Ext.util.History.getToken();			
			if (historyToken) {
				actionLoadContent(historyToken);
			} else {	
				actionLoadContent('Dashboard');
			}	
			
		});	
	</script>
    </stripes:layout-component>
</stripes:layout-render>
        
