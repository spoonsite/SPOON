<%--
/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
<%-- 
    Document   : feedback
    Created on : Jun 15, 2017, 8:14:45 AM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){
			
			var notificationWin = Ext.create('OSF.component.NotificationWindow', {				
			});	

			Ext.create('Ext.container.Viewport', {
				layout: 'border',
				items: [{
					xtype: 'panel',
					region: 'north',
					id: 'topNavPanel',
					border: false,					
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
									listeners: {
										el: {
											click: function() {
												window.location.replace('Landing.action');
											}
										}
									},
									src: '${branding.secondaryLogoUrl}'
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'tbtext',
									text: 'Feedback',									
									cls: 'page-title'
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'button',
									scale   : 'large',
									ui: 'default',
									iconCls: 'fa fa-2x fa-envelope-o icon-top-padding',
									iconAlign: 'left',
									text: 'Notifications',
									handler: function() {
										notificationWin.show();
										notificationWin.refreshData();
									}
								},								
								Ext.create('OSF.component.UserMenu', {																		
									ui: 'default',
									showFeedback: false,									
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
					xtype: 'panel',	
					layout: 'fit',
					items: [
						Ext.create('OSF.component.FeedbackWindow', {
							id: 'feedbackWin',
							float: false,
							maximized: true,
							closable: false,
							buttonSize: 'large',
							successHandler: function(win) {
								Ext.Msg.show({
									title:'Success',
									message: 'Sent Feedback Successfully',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.INFO,
									fn: function(btn) {
										if (btn === 'ok') {
											window.location.href = 'Landing.action';
										}
									}
								});
							},
							closeHandler: function(win){
								window.location.href = 'Landing.action';
							},
							header: {
								cls: 'x-panel-header-default'
							}
						})
					]
				}]
			});
			
			Ext.getCmp('feedbackWin').show();
			
			
			
			CoreService.brandingservice.getCurrentBranding().then(function(branding){				
				if (branding.securityBannerText && branding.securityBannerText !== '') {
					Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
						securityBannerText: branding.securityBannerText
					}), 0);
				}
			});
			
		});	
	</script>		
		
		
	</stripes:layout-component>	
</stripes:layout-render>	