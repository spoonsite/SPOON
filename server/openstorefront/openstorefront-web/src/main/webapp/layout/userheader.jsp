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
					out.println("			pageMap['" + key + "'] = '" + baseToolAction.getPageMap().get(key).getPage() + "';");
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
				id: 'tools-menu-relationships',
				text: 'Relationships',
				hidden: true,
				handler: function(){
					actionLoadContent('Relationships');
				}			
			});			
			toolsMenu.push({
				id: 'tools-menu-reports',
				text: 'Reports',
				hidden: true,
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
									iconCls: 'fa fa-2x fa-envelope-o',
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
							overflowHandler: 'scroller',
							items:[
								{
									text: 'Dashboard',
									scale   : 'large',
									id: 'dashboardUserHomeButton',
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
									text: 'Profile',
									id: 'userHeaderProfileBtn',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-user icon-button-color-default',
									margin: '0 10 0 0',
									handler: function(){
										actionLoadContent('User-Profile');
									}									
								},
								{
									id: 'main-menu-submissions',
									text: 'Submissions',
									scale   : 'large',
									hidden: true,
									iconCls: 'fa fa-2x fa-list icon-button-color-default',
									margin: '0 10 0 0',
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
									iconCls: 'fa fa-2x fa-wrench icon-button-color-default',									
									width: '140px',
									menu: {										
										items: toolsMenu,
										listeners: {
											beforerender: function () {
											 this.setWidth(this.up('button').getWidth());
											}					
										}
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Change password',
									id: 'main-menu-changepassword',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-key icon-button-color-key icon-correction-key',
									hidden: true,
									handler: function(){
										actionLoadContent('Change-Password');
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
				if (CoreService.userservice.userHasPermisson(user, "REPORTS")) {
					Ext.getCmp('tools-menu-reports').setHidden(false);					
				}
				if (CoreService.userservice.userHasPermisson(user, "RELATION-VIEW-TOOL")) {
					Ext.getCmp('tools-menu-relationships').setHidden(false);
				} 
				if (CoreService.userservice.userHasPermisson(user, "USER-SUBMISSIONS")) {
					Ext.getCmp('main-menu-submissions').setHidden(false);
				}
			});	
			
			Ext.Ajax.request({
				url: 'api/v1/service/security/realmname',
				success: function(response, opts) {
					var data = Ext.decode(response.responseText);
					
					Ext.Array.each(data, function(realm) {
						if (realm.code === 'StorefrontRealm') {
							Ext.getCmp('main-menu-changepassword').setHidden(false);
						}
					});
					
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
