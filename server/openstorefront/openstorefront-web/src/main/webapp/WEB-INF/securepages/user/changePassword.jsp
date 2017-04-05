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

    Document   : changePassword
    Created on : Mar 2, 2017, 2:02:17 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../layout/userheader.jsp">		
		</stripes:layout-render>			
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function() {
				
				var mainPanel = Ext.create("Ext.form.Panel", {
					title: 'Change Password <i class="fa fa-question-circle"  data-qtip="Allows you to change your password."></i>',
					scrollable: true,
					bodyStyle: 'padding: 10px;',
					layout: 'anchor',
					defaults: {
						fieldSeparator: '',
						labelAlign: 'top',						
						allowBlank: false
					},
					items: [
						{
							xtype: 'textfield',
							fieldLabel: 'Existing Password <span class="field-required" />',
							inputType: 'password',							
							name: 'existingPassword',							
							width: 250,
							maxLength: 80
						},						
						{
							xtype: 'textfield',							
							fieldLabel: 'New Password <span class="field-required" />',							
							inputType: 'password',
							name: 'password',							
							width: 250,
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
							fieldLabel: 'Confirm New Password <span class="field-required" />',
							inputType: 'password',							
							name: 'confirmPassword',							
							width: 250,
							maxLength: 80,
							minLength: 8
						},
						{
							xtype: 'button',
							text: 'Update Password',
							formBind: true,
							iconCls: 'fa fa-2x fa-save icon-vertical-correction-save',
							width: '180px',
							scale: 'medium',
							handler: function() {

								var form = this.up('form');
								var data = form.getValues();

								if (data.password !== data.confirmPassword) {
									Ext.Msg.show({
										title:'Error',
										iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
										message: '<b>New Password and Confirm New Password must match.</b>',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.Error,
										fn: function(btn) {
										}
									});
									form.getForm().markInvalid({
										confirmPassword: 'Confirm Password must match New Password'
									});											
								} else {

									CoreUtil.submitForm({
										url: 'api/v1/service/security/checkPassword',
										method: 'POST',														
										data: data,
										form: form,
										loadingText: 'Validating...',
										success: function(action, opts){
											CoreUtil.submitForm({
												url: 'api/v1/resource/users/currentuser/resetpassword',
												method: 'PUT',
												data: data,
												form: form,
												loadingText: 'Updating...',
												success: function(action, opts){
													Ext.toast('Changed password Sucessfully');
													form.reset();
												}
											});										
										}
									});
									
								}
							}
						}
					]

				});
				
				addComponentToMainViewPort(mainPanel);				
				
			});
		</script>
		
	</stripes:layout-component>
</stripes:layout-render>			
