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
					bodyStyle: 'padding: 40px;',
					layout: 'anchor',					
					items: [
						{
							xtype: 'panel',							
							titleAlign: 'center',
							title: '<span style="">Login Credentials</span>',
							style: 'background-image: url(images/grid.png); box-shadow: 5px 10px 15px;',
							layout: 'center',
							items: [
								{
									xtype: 'panel',
									layout: 'anchor',
									defaults: {
										width: 600,
										labelSeparator: '',
										labelAlign: 'top',
										msgTarget: 'under'
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
											xtype: 'panel',
											html: 'Password Requires: <ul><li>At least 1 Capital Letter</li>'+
												  '<li>At least 1 Number</li>' +
												  '<li>At least 1 Special Character (i.e. ?!@#$%*)</li></ul>'
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
								}
							]
						},						
						{
							xtype: 'panel',
							title: '<span style="">User Information</span>',
							titleAlign: 'center',
							style: 'background-image: url(images/grid.png); box-shadow: 5px 10px 15px;',
							margin: '20 0 0 0',
							layout: 'center',						
							items: [
								{
									xtype: 'panel',
									layout: 'anchor',
									defaults: {
										width: 600,
										labelSeparator: '',
										labelAlign: 'top',
										msgTarget: 'under'
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
											xtype: 'combobox',
											name: 'organization',
											allowBlank: false,
											maxLength: 120,
											fieldLabel: 'Organization <span class="field-required" />',
											forceSelection: false,
											valueField: 'description',
											displayField: 'description',
											store: {
												autoLoad: true,
												proxy: {
													type: 'ajax',
													url: 'api/v1/resource/organizations/lookup'
												}
											}
										},	
										{
											xtype: 'textfield',
											fieldLabel: 'Position Title',
											name: 'positionTitle',																					
											maxLength: 255
										},
										{
											xtype: 'textfield',
											fieldLabel: 'Business Email <span class="field-required" />',
											name: 'email',
											vtype: 'email',
											allowBlank: false,
											maxLength: 1024
										},	
										{
											xtype: 'textfield',
											fieldLabel: 'Business Phone <span class="field-required" />',
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
							]
						},
						{
							xtype: 'panel',
							title: '<span style="">Verification Code</span>',
							titleAlign: 'center',
							style: 'background-image: url(images/grid.png); box-shadow: 5px 10px 15px;',
							margin: '20 0 0 0',
							layout: 'center',	
							height: 200,
							items: [
								{
									xtype: 'panel',
									layout: 'anchor',
									defaults: {
										width: 600,
										labelSeparator: '',
										labelAlign: 'top',
										msgTarget: 'under'
									},	
									items: [ {
											xtype: 'hiddenfield',
											id: 'registrationId',
											name: 'registrationId'
										},
										{
											xtype: 'label',
											cls: 'x-form-item-label x-form-item-label-default field-label-basic x-form-item-label-top x-unselectable',
											text:  'Click on the "Get Verification Code" button and a verification code will be sent to your email address'
										},
										{
											xtype: 'button',
											text: "Get Verification Code",
											name: 'verificationCodeButton',
											id: 'verificationCodeButton',
											iconCls: 'fa fa-2x fa-lock icon-button-color-default icon-vertical-correction-check',
											allowBlank: false,
											width: 200,
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

													if (data.userTypeCode === '') {
														delete data.userTypeCode;
													}

													CoreUtil.submitForm({
														url: 'api/v1/resource/userregistrations',
														method: 'POST',
														data: data,
														form: form,
														success: function(action, opts) {
															var registration = Ext.decode(action.responseText);
															Ext.getCmp('registrationId').setValue(registration.registrationId);
														}
													});
												}
											}
										},
										{
											xtype: 'textfield',
											fieldLabel: 'Enter the verification code from your your email here <span class="field-required" />',
											name: 'verificationCode',
											allowBlank: false,
											maxLength: 80
										}										
									]
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
									id: 'Signup',
									iconCls: 'fa fa-2x fa-check icon-button-color-save icon-vertical-correction-check',
									scale: 'medium',
									formBind: true,
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
											
											if (data.userTypeCode === '') {
												delete data.userTypeCode;
											}
											
											CoreUtil.submitForm({
												url: 'api/v1/resource/userregistrations',
												method: 'PUT',
												data: data,
												form: form,
												success: function(action, opts) {
																										
													var message = '<i class="fa fa-2x fa-check-circle icon-button-color-refresh"></i><span class="approval-message">Account Created Successfully.</span>';													
													if (!${autoApprove}) {
														message += '<br><br><i class="fa fa-lg fa-warning icon-button-color-warning"></i>&nbsp;&nbsp;An Admin must approve the account before the account is activated.<br><br>An email will be sent when the account is approved.';
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
																		iconCls: 'fa fa-2x fa-sign-in icon-button-color-default icon-vertical-correction',
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
									iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
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
		
