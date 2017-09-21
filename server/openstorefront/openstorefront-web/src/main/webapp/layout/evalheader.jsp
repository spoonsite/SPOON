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
					out.println("			pageMap['" + key  + "'] = '" + baseToolAction.getPageMap().get(key).getPage() + "';");
				}
			%>

			
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
									text: 'Evaluation Tools',									
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
									showEvaluatorTools: false,
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
									text: 'Evaluations',
									scale   : 'large',
									iconCls: 'fa fa-2x fa-file-text-o icon-button-color-default',
									margin: '0 10 0 0',
									handler: function(){
										actionLoadContent('Evaluations');
									}									
								},
								{							
									xtype: 'tbseparator'
								},											
								{
									itemId: 'main-menu-reports',
									text: 'Reports',
									scale   : 'large',
									hidden: true,
									iconCls: 'fa fa-2x fa-list icon-button-color-default',
									handler: function(){
										actionLoadContent('Reports');
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
				if (CoreService.userservice.userHasPermisson(user, ['REPORTS'])) {
					Ext.getCmp('mainViewPortPanel').queryById('main-menu-reports').setHidden(false);					
				}
				
			});	
			
						
			var actionLoadContent = function(key) {
				window.location.href = 'EvaluationTool.action?load=' + key;
			};
				
			var paramRoute = '${param.load}';
			if (paramRoute && paramRoute !== '') {
				var url = pageMap[paramRoute];
				if (url){
					Ext.getCmp('titleTextId').setText('Evaluation Tools - ' + paramRoute.replace('-', ' '));			
				} else {
					Ext.toast("Page key Not Found");
				}
			} else {
				Ext.getCmp('titleTextId').setText('Evaluation Tools - Dashboard');
			}
			
		});	
		
		var addComponentToMainViewPort = function(component) {
			Ext.getCmp('mainViewPortPanel').add(component);
			Ext.getCmp('mainViewPortPanel').updateLayout(true, true);
		};		
		
	</script>
	
</stripes:layout-definition>
