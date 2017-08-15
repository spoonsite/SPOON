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

    Document   : resetPassword
    Created on : Feb 17, 2017, 12:09:24 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/externalLayout.jsp">
    <stripes:layout-component name="contents">
		
		<style> 
			
			.header-background{
				color: white;
				background: #7d7e7d;
				background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/Pgo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgdmlld0JveD0iMCAwIDEgMSIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+CiAgPGxpbmVhckdyYWRpZW50IGlkPSJncmFkLXVjZ2ctZ2VuZXJhdGVkIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgeDE9IjAlIiB5MT0iMCUiIHgyPSIwJSIgeTI9IjEwMCUiPgogICAgPHN0b3Agb2Zmc2V0PSIwJSIgc3RvcC1jb2xvcj0iIzdkN2U3ZCIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjAlIiBzdG9wLWNvbG9yPSIjNTk1OTU5IiBzdG9wLW9wYWNpdHk9IjEiLz4KICAgIDxzdG9wIG9mZnNldD0iMTAwJSIgc3RvcC1jb2xvcj0iIzBlMGUwZSIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgPC9saW5lYXJHcmFkaWVudD4KICA8cmVjdCB4PSIwIiB5PSIwIiB3aWR0aD0iMSIgaGVpZ2h0PSIxIiBmaWxsPSJ1cmwoI2dyYWQtdWNnZy1nZW5lcmF0ZWQpIiAvPgo8L3N2Zz4=);
				background: -moz-linear-gradient(top, #7d7e7d 0%, #595959 0%, #0e0e0e 100%);
				background: -webkit-linear-gradient(top, #7d7e7d 0%,#595959 0%,#0e0e0e 100%);
				background: linear-gradient(to bottom, #7d7e7d 0%,#595959 0%,#0e0e0e 100%);
				filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#7d7e7d', endColorstr='#0e0e0e',GradientType=0 );				
			}
			
		</style>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				
				
				var resetWindow = Ext.create('Ext.window.Window', {
					title: 'Reset Password',
					iconCls: 'fa fa-key icon-correction-key icon-small-vertical-correction',
					y: 200,
					width: 500,
					height: 450,
					resizable: false,
					closable: false,
					draggable: false,
					onEsc: Ext.emptyFn,
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							bodyStyle: 'padding: 20px;',
							layout: 'anchor',
							defaults: {
								width: '100%',
								labelAlign: 'top',
								labelSeparator: ''
							},
							items: [
								{
									xtype: 'textfield',
									fieldLabel: 'Username <span class="field-required" />',
									name: 'username',
									allowBlank: false,
									maxLength: 80
								},
								{
									xtype: 'textfield',
									fieldLabel: 'New Password <span class="field-required" />',
									inputType: 'password',
									name: 'password',
									allowBlank: false,
									maxLength: 80,
									minLength: 8
								},
								{
									xtype: 'panel',
									html: 'Password Requires: <ul><li>At least 1 Capital Letter</li>'+
										  '<li>At least 1 Number</li>' +
										  '<li>At least 1 Special Character (Ie. ?!@#$%*)</li></ul>'
								},								
								{
									xtype: 'textfield',
									fieldLabel: 'Confirm Password <span class="field-required" />',
									inputType: 'password',
									name: 'confirmPassword',
									allowBlank: false,
									maxLength: 80,
									minLength: 8
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{											
											text: 'Send Approval',
											iconCls: 'fa fa-2x fa-envelope-o icon-vertical-correction-send icon-button-color-default',
											scale: 'medium',
											width: '160px',
											formBind: true,
											handler: function() {
												
												//verify password
												var form = this.up('form');
												var data = form.getValues();

												if (data.password !== data.confirmPassword) {
													Ext.Msg.show({
														title:'Validation',
														message: 'Password and the Confirm Password must match',
														buttons: Ext.Msg.OK,
														icon: Ext.Msg.Error,
														fn: function(btn) {
														}
													});
													form.getForm().markInvalid({
														confirmPassword: 'Must match password'
													});											
												} else {
													
													
													CoreUtil.submitForm({
														url: 'api/v1/service/security/checkPassword',
														method: 'POST',														
														data: data,
														form: form,
														loadingText: 'Validating...',
														success: function(response, opts) {
															
															CoreUtil.submitForm({
																url: 'api/v1/service/security/' + data.username + '/resetpassword',
																method: 'PUT',
																data: data,
																form: form,
																loadingText: 'Sending Approval...',
																success: function(response, opts) {
																	var successWin = Ext.create('Ext.window.Window', {
																		title: 'Sent Approval',																		
																		bodyStyle: 'padding: 20px;',
																		closeAction: 'destroy',
																		width: 400,
																		height: 300,
																		scrollable: true,
																		closable: false,
																		onEsc: Ext.emptyFn,
																		modal: true,
																		html: '<i class="fa fa-2x fa-check text-success"></i>Check your email associated with your user. Follow the instructions in the email to complete the reset.',
																		dockedItems: [
																			{
																				xtype: 'toolbar',
																				dock: 'bottom',
																				items: [
																					{
																						xtype: 'tbfill'
																					},
																					{ 
																						text: 'Return to Login',
																						width: '170px',
																						iconCls: 'fa fa-2x fa-sign-in icon-button-color-default icon-vertical-correction-view',
																						scale: 'medium',
																						handler: function() {
																							window.location.href = 'login.jsp';
																						}
																					},
																					{
																						xtype: 'tbfill'
																					}
																				]
																			}
																		]
																	});
																	successWin.show();
																}
															});
														}
													});
													
													
												}
											}
										}, 
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
											scale: 'medium',
											handler: function(){										
												window.location.href = 'login.jsp';
											}
										}
									]
								}								
							]
						}
					]
					
				});
				resetWindow.show();
				
				Ext.create('Ext.container.Viewport', {
					layout: 'border',					
					items: [
						{
							region: 'north',							
							bodyStyle: 'padding-top: 20px;padding-bottom: 20px;',
							bodyCls: 'header-background',
							html: '<h1 style="text-align: center">${appTitle}</h1>'
							
						},
						{
							region: 'center',
							scrollable: true,
							bodyStyle: 'background-image: url(images/grid.png); background-repeat: repeat;',
						}
					]
				});				
				
				
			});
		
		</script>
		
	</stripes:layout-component>
</stripes:layout-render>			
