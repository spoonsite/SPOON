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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/externalLayout.jsp">
    <stripes:layout-component name="contents">
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				
				var mainForm = Ext.create('Ext.form.Panel', {
					region: 'center',
					scrollable: true,
					style: 'background-image: url(images/grid.png);',					
					bodyStyle: 'padding-top: 40px; padding-left: 20%; padding-right: 20%;',
					layout: 'anchor',					
					items: [
						{
							xtype: 'fieldset',
							title: 'Login Credentials',
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
									name: 'password',
									allowBlank: false,
									maxLength: 80,
									minLength: 8
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Confirm Password <span class="field-required" />',
									name: 'confirmPassword',
									allowBlank: false,
									maxLength: 80,
									minLength: 8
								}
							]
						},						
						{
							xtype: 'fieldset',
							title: 'User Information',
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
							html: '<h1 style="text-align: center">${appTitle}</h1>'
							
						},
						mainForm
					]
				});
				
				
			});
			
		</script>
		
	</stripes:layout-component>
</stripes:layout-render>	
		
