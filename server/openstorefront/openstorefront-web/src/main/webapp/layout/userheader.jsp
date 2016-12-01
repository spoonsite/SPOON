<%-- 
    Document   : userheader
    Created on : Sep 20, 2016, 10:50:31 AM
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
			<%
				BaseToolAction baseToolAction = (BaseToolAction)request.getAttribute("actionBean");
				for (String key : baseToolAction.getPageMap().keySet()) {
					out.println("			pageMap['" + key + "'] = '" + baseToolAction.getPageMap().get(key) + "';");
				}
			%>


			//Data Menu
			var toolsMenu = [];
			toolsMenu.push({
				text: 'Questions',
				handler: function(){
					actionLoadContent('Questions');
				}
			});
			toolsMenu.push({
				text: 'Relationships',
				handler: function(){
					actionLoadContent('Relationships');
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
								Ext.create('OSF.component.UserMenu', {
									showUserTools: false,
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
				window.location.href = 'UserTool.action?load=' + key;
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
					Ext.getCmp('titleTextId').setText('User Tools - ' + paramRoute.replace('-', ' '));
					//contents.load(url);				
					//Ext.util.History.add(key);				
				} else {
					Ext.toast("Page key Not Found");
					//contents.load(pageMap['Dashboard']);	
				}
			} else {
				Ext.getCmp('titleTextId').setText('User Tools - Dashboard');
				//contents.load(pageMap['Dashboard']);
			}
			
		});	
		
		var addComponentToMainViewPort = function(component) {
			Ext.getCmp('mainViewPortPanel').add(component);
			Ext.getCmp('mainViewPortPanel').updateLayout(true, true);
		};		
		
	</script>
	
</stripes:layout-definition>
