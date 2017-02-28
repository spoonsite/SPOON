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

    Document   : registration
    Created on : Feb 17, 2017, 12:06:25 PM
    Author     : dshurtleff
--%>
<%@page import="edu.usu.sdl.openstorefront.core.entity.SecurityPolicy"%>
<%@page import="edu.usu.sdl.openstorefront.service.ServiceProxy"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/externalLayout.jsp">
    <stripes:layout-component name="contents">
		
		<%
		
			SecurityPolicy securityPolicy = ServiceProxy.getProxy().getSecurityService().getSecurityPolicy();
			request.setAttribute("autoApprove", securityPolicy.getAutoApproveUsers());
		
			if (!securityPolicy.getAllowRegistration()) {				
				response.sendRedirect("noregistration.jsp");
			}

		%>	
		
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
				
				var mainForm = Ext.create('Ext.form.Panel', {
					region: 'center',
					title: 'Fill out the form to signup for an account',
					iconCls: 'fa fa-warning',
					scrollable: true,
					style: 'background-image: url(images/grid.png);',					
					bodyStyle: 'padding-top: 40px; padding-left: 20%; padding-right: 20%;',
					layout: 'anchor',					
					items: [
						{
							xtype: 'fieldset',
							title: '<span style="color: black;">Login Credentials</span>',
							style: 'box-shadow: 5px 10px 15px;',
							layout: 'anchor',
							defaults: {
								width: '75%',
								labelSeparator: '',
								labelAlign: 'top'
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
									fieldLabel: 'Password <span class="field-required" />',
									inputType: 'password',
									name: 'password',
									allowBlank: false,
									maxLength: 80,
									minLength: 8
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
							]
						},						
						{
							xtype: 'fieldset',
							title: '<span style="color: black;">User Information</span>',
							style: 'box-shadow: 5px 10px 15px;',
							layout: 'anchor',
							defaults: {
								width: '75%',
								labelSeparator: '',
								labelAlign: 'top'
							},							
							items: [
								{
									xtype: 'textfield',
									fieldLabel: 'First Name <span class="field-required" />',
									name: 'firstName',
									allowBlank: false,
									maxLength: 80
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Last Name <span class="field-required" />',
									name: 'lastName',
									allowBlank: false,
									maxLength: 80
								},			
								{
									xtype: 'textfield',
									fieldLabel: 'Organization',
									name: 'organization',
									allowBlank: true,
									maxLength: 120
								},								
								{
									xtype: 'textfield',
									fieldLabel: 'Email <span class="field-required" />',
									name: 'email',
									allowBlank: false,
									maxLength: 1024
								},	
								{
									xtype: 'textfield',
									fieldLabel: 'Phone <span class="field-required" />',
									name: 'phone',
									allowBlank: false,
									maxLength: 80
								},
								{
									xtype: 'combobox',
									fieldLabel: 'User Type',
									name: 'userTypeCode',
									valueField: 'code',
									displayField: 'description',									
									editable: false,
									forceSelection: true,									
									store: {
										autoLoad: true,
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/lookuptypes/UserTypeCode'
										},
										listeners: {
											load: function(store, records, opts) {
												store.add({
													code: null,
													description: 'Select'
												});
											}
										}
									}
								}
							]
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									xtype: 'tbspacer',
									width: '20%'
								},
								{
									text: 'Signup',
									iconCls: 'fa fa-2x fa-check',
									scale: 'medium',
									handler: function(){										
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
												url: 'api/v1/resource/userregistrations',
												method: 'POST',
												data: data,
												form: form,
												success: function(action, opts) {
																										
													var message = '<h2>Account was created Successfully.</h2>';													
													if (!${autoApprove}) {
														message += '<br><br><i class="fa fa-2x fa-warning text-warning"></i> An Admin must approve the account before the account is activated.<br><br>An rmail will be sent when the account is approved.';
													} else {
														message += '<br><br> Login to continue';
													}
													
													var accountRegWin = Ext.create('Ext.window.Window', {
														title: 'Registration Complete',
														bodyStyle: 'padding: 20px;',
														closeAction: 'destroy',
														width: 400,
														height: 300,
														scrollable: true,
														closable: false,
														onEsc: Ext.emptyFn,
														modal: true,
														html: message,
														dockedItems: [
															{
																xtype: 'toolbar',
																dock: 'bottom',
																items: [
																	{
																		xtype: 'tbfill'
																	},
																	{ 
																		text: 'Login',
																		iconCls: 'fa fa-2x fa-sign-in',
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
													accountRegWin.show();
													
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
									iconCls: 'fa fa-2x fa-close',
									scale: 'medium',
									handler: function(){										
										window.location.href = 'login.jsp';
									}									
								},
								{
									xtype: 'tbspacer',
									width: '20%'
								}
							]
						}
					]
				});
				
				
				Ext.create('Ext.container.Viewport', {
					layout: 'border',
					bodyStyle: 'background-image: url(images/grid.png); background-repeat: repeat;',
					items: [
						{
							region: 'north',							
							bodyStyle: 'padding-top: 20px;padding-bottom: 20px;',
							bodyCls: 'header-background',
							html: '<h1 style="text-align: center">${appTitle}</h1>'
							
						},
						mainForm
					]
				});
				
				
			});
			
		</script>
		
	</stripes:layout-component>
</stripes:layout-render>	
		
