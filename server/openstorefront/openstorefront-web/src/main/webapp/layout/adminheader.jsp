<%-- 
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
									iconCls: 'fa fa-2x fa-envelope',
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