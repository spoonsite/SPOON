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
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>	
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {

                
				var shiroConfig = Ext.create('Ext.form.Panel', {
					title: 'Shiro Config',
					iconCls: 'fa fa-lg fa-gear',
					scrollable: true,
					layout: 'fit',
					requiredPermissions: ['ADMIN-SECURITY-SHIRO-CONFIG'],
					actionOnInvalidPermission: 'destroy',
					items: [
						{
							xtype: 'textarea',							
							name: 'data'							
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Save',
									iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
									scale: 'medium',
									height: '40px',
									handler: function() {
										var form = this.up('form');
										
										var data = form.getValues();
										
										CoreUtil.submitForm({
											url: 'api/v1/service/security/shiroconfig',
											method: 'PUT',											
											data: data,
											form: form,
											success: function(action, opts) {
												Ext.toast('Updated Shiro Config.<br><br>  <i class="fa fa-warning"></i> Requires a server restart to apply.');
											}											
										});										
									}
								}
							]
						}
					]
				});				
				
				var policyForm = Ext.create('Ext.form.Panel', {
					title: 'Security Policy',
					iconCls: 'fa fa-lg fa-clipboard',
					bodyStyle: 'padding: 20px;',
					scrollable: true,
					layout: 'anchor',
					requiredPermissions: ['ADMIN-SECURITY-POLICY'],
					actionOnInvalidPermission: 'destroy',
					items: [
						{
							xtype: 'hidden',
							name: 'policyId'
						},
						{
							xtype: 'fieldset',
							title: 'General',
							width: '100%',
							layout: 'anchor',
							defaults: {
								width: '100%',
								labelAlign: 'top'
							},
							items: [
								{
									xtype: 'checkbox',
									boxLabel: 'Disable User Information Editing <i class="fa fa-question-circle" data-qtip="User must edit their information externally.<br>This also enable sync information with an external system."></i>',								
									name: 'disableUserInfoEdit'
								},		
								{
									xtype: 'textfield',
									name: 'externalUserManagementText',
									fieldLabel: 'External User Management Note <i class="fa fa-question-circle" data-qtip="This creates a message about how the user can update there user information."></i>',									
									maxLength: 255
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Allow JSON-P Support <i class="fa fa-question-circle" data-qtip="JSON-P allow external javascript application access the API"></i>',									
									name: 'allowJSONPSupport'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'CSRF Support <i class="fa fa-question-circle" data-qtip="Cross Site Request Forgery protection; May intefer with CORS."></i>',
									name: 'csrfSupport'
								},
								{
									xtype: 'textarea',
									fieldLabel: 'CORS Origin Header',
									name: 'corsOrigins',
									grow: true
								},
								{
									xtype: 'textarea',
									fieldLabel: 'CORS Method Header',
									name: 'corsMethods',
									grow: true
								},
								{
									xtype: 'textarea',
									fieldLabel: 'CORS Headers',
									name: 'corsHeaders',
									grow: true
								},
								{
									xtype: 'textarea',
									fieldLabel: 'Custom Headers',
									name: 'customHeaders',
									grow: true
								}								
							]
						},
						{
							xtype: 'fieldset',
							title: 'Spoon Realm',
							width: '100%',
							layout: 'anchor',
							defaults: {
								width: '100%',
								labelAlign: 'top'
							},
							items: [
								{
									xtype: 'checkbox',
									boxLabel: 'Allow User Registration',									
									name: 'allowRegistration'									
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Require Proof Citzenship <i class="fa fa-question-circle" data-qtip="User must provide citzenship information before approval."></i>',								
									name: 'requiresProofOfCitizenship'									
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Auto Approve Users <i class="fa fa-question-circle" data-qtip="Approve new user automatically."></i>',								
									name: 'autoApproveUsers'									
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Require an Admin to unlock <i class="fa fa-question-circle" data-qtip="Require an Admin to unlock a locked account."></i>',									
									name: 'requireAdminUnlock'	
								},
								{
									xtype: 'numberfield',
									fieldLabel: 'Reset Timeout (Minutes)',
									name: 'resetLockoutTimeMinutes',
									minValue: 1,
									maxValue: 1440
								},
								{
									xtype: 'numberfield',
									fieldLabel: 'Login Max Failed Attempt',
									name: 'loginLockoutMaxAttempts',
									minValue: 0,
									maxValue: 25
								},
								{
									xtype: 'numberfield',
									fieldLabel: 'Minimum password length',
									name: 'minPasswordLength',
									minValue: 0,
									maxValue: 80
								}	
							]
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Save',
									iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
									scale: 'medium',
									height: '40px',
									handler: function() {
										var form = this.up('form');
										
										var data = form.getValues();
										
										CoreUtil.submitForm({
											url: 'api/v1/resource/securitypolicy/' + data.policyId,
											method: 'PUT',
											data: data,
											form: form,
											success: function(action, opts) {
												Ext.toast('Updated Security Policy');
											}											
										});
										
									}
								}
							]
						}
					]					
				});
												
				var mainPanel = Ext.create('Ext.tab.Panel', {
					title: 'Security Management <i class="fa fa-question-circle"  data-qtip="Manage security policy for the application"></i>',
					items: [policyForm,shiroConfig],
					listeners:{
						remove: function(){
							if(this.items.length===0){
								Ext.toast({html: 'You do not have access permissions to see any data on this view.', title:'No Access', align:'b'}); 
								
							}
						}	
					}
				});
				
				addComponentToMainViewPort(mainPanel);
				
				
				policyForm.setLoading(true);
				Ext.Ajax.request({
					url: 'api/v1/resource/securitypolicy',
					callback: function() {
						policyForm.setLoading(false);
					},
					success: function(response, opts){
						var data = Ext.decode(response.responseText);
						
						var record = Ext.create('Ext.data.Model',{});
						record.set(data);
						policyForm.loadRecord(record);
					}
				});
		
				shiroConfig.setLoading(true);
				Ext.Ajax.request({
					url: 'api/v1/service/security/shiroconfig',
					callback: function() {
						shiroConfig.setLoading(false);
					},
					success: function(response, opts){
												
						var record = Ext.create('Ext.data.Model',{});
						record.set({
							data: response.responseText
						});					
						shiroConfig.loadRecord(record);
					}
				});		
				
			});
        </script>

    </stripes:layout-component>
</stripes:layout-render>
			
			
