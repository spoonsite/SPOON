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

		<script src="scripts/component/messageWindow.js?v=${appVersion}" type="text/javascript"></script>
		
		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>	
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				
				var registrationGridStore = Ext.create('Ext.data.Store',{
					pageSize: 100,
					autoLoad: true,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'createDts',
							direction: 'DESC'
						})
					],					
					fields: [
						{
							name: 'createDts',
							type:	'date',
							dateFormat: 'c'
						}
					],
					proxy: CoreUtil.pagingProxy({
						type: 'ajax',
						url: 'api/v1/resource/userregistrations',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}							
					})
				});
				
				var registrationGrid = Ext.create('Ext.grid.Panel',{
					iconCls: 'fa fa-lg fa-user-plus',
					title: 'Registrations',
					columnLines: true,
					store: registrationGridStore,
					viewConfig: {
						enableTextSelection: true
					},	
					
					columns: [
						{ text: 'Username', dataIndex: 'username', width: 200 },
						{ text: 'First name', dataIndex: 'firstName', width: 200 },
						{ text: 'Last name', dataIndex: 'lastName', width: 200 },
						{ text: 'Organization', dataIndex: 'organization', width: 200 },
						{ text: 'Position Title', dataIndex: 'positionTitle', width: 200 },						
						{ text: 'Email', dataIndex: 'email', flex: 1, minWidth: 200 },
						{ text: 'Phone', dataIndex: 'phone', width: 150 },
						{ text: 'User Type', dataIndex: 'userTypeCode', align: 'center', width: 200, 
							renderer: function(value, meta, record) {
								if (record.get('userTypeDescription')) {
									meta.tdAttr = 'data-qtip="' + record.get('userTypeDescription') + '"';
								}
								return value;
							}
						},
						{ text: 'Registration Date', dataIndex: 'createDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Status', dataIndex: 'userProfileId', align: 'center', width: 150, 
							renderer: function(value, meta, record) {
								return (record.get('userProfileId')) ? "Complete" : "Pending";
							}
						}
					],
					listeners: {
						selectionChange: function(selectionModel, records, opts) {
							var tools = registrationGrid.getComponent('tools');
							if (records.length > 0) {								
								tools.getComponent('delete').setDisabled(false);	
								tools.getComponent('message').setDisabled(false);
							} else {								
								tools.getComponent('delete').setDisabled(true);		
								tools.getComponent('message').setDisabled(true);
							}
						}
					},	
					
					dockedItems: [
						{
							xtype: 'toolbar',
							itemId: 'tools',
							items: [
								{
									text: 'Refresh',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									scale: 'medium',
									handler: function(){
										actionRefreshRegs();
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-CREATE']
								},
								{
									text: 'Add',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction-add',
									scale: 'medium',
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-CREATE'],
									handler: function(){
										actionAddUser();
									}									
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-CREATE']
								},
								{
									text: '&nbsp;Message',
									itemId: 'message',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-envelope-o icon-vertical-correction icon-button-color-default',
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-CREATE'],								
									handler: function () {
										var record = registrationGrid.getSelection()[0];
										actionMessageUser(record);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									disabled: true,
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									scale: 'medium',
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-DELETE'],
									handler: function(){
										var record = registrationGrid.getSelectionModel().getSelection()[0];
										
										Ext.Msg.show({
											title:'Delete Registration?',
											iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
											message: 'Are you sure you want to remove this registration?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													registrationGrid.setLoading('Deleting the user registration...');
													
													Ext.Ajax.request({
														url: 'api/v1/resource/userregistrations/' + record.get('registrationId'),
														method: 'DELETE',
														callback: function(){
															registrationGrid.setLoading(false);
														},
														success: function(){
															actionRefreshRegs();
														}
													})
												}
											}
										});
									}									
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: registrationGridStore,
							displayInfo: true
						}
					]
				});
				
				var actionRefreshRegs = function() {
					registrationGrid.getStore().load();
				};
				
				var actionMessageUser = function(record) {
					
					var messageWindow = Ext.create('OSF.component.MessageWindow', {
						closeAction: 'destroy',
						initialToUsers: record.get('email')
					}).show();
					
				};
				
				
				var actionAddUser = function() {
					
					var addUserWin = Ext.create('Ext.window.Window', {
						title: 'Add User',
						iconCls: 'fa fa-user-plus',
						closeAction: 'destroy',
						modal: true,
						width: 700,
						height: '85%',
						maximizable: true,
						layout: 'fit',
						items: [
							{
								xtype: 'form',
								bodyStyle: 'padding: 10px;',
								scrollable: true,
								layout: 'anchor',
								items: [
									{
										xtype: 'fieldset',
										title: 'Login Credentials',										
										layout: 'anchor',
										defaults: {
											width: '75%',
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
												fieldLabel: 'Business Email <span class="field-required" />',
												name: 'email',
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
												xtype: 'textfield',
												fieldLabel: 'Position Title',
												name: 'positionTitle',												
												maxLength: 255
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
												text: 'Signup',
												iconCls: 'fa fa-2x fa-check icon-button-color-save icon-vertical-correction-check',
												formBind: true,
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
														if (data.userTypeCode === '') {
															delete data.userTypeCode;
														}

														CoreUtil.submitForm({
															url: 'api/v1/resource/userregistrations/admin',
															method: 'POST',
															data: data,
															form: form,
															success: function(action, opts) {
																Ext.toast('Successfully added new user.');
																actionRefreshRegs();
																actionRefreshUsers();
																addUserWin.close();
															},
															failure: function(action, opts) {
																form.setScrollY(0);
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
													addUserWin.close();
												}									
											}											
										]
									}
								]
							}
						]
					});
					addUserWin.show();
					
				};
				
				
				var userGridStore = Ext.create('Ext.data.Store',{
					pageSize: 100,
					autoLoad: false,
					fields: [
						{
							name: 'lastLoginAttempt',
							type:	'date',
							dateFormat: 'c'
						},
						{
							name: 'passwordUpdateDts',
							type:	'date',
							dateFormat: 'c'
						}
					],
					proxy: CoreUtil.pagingProxy({
						type: 'ajax',
						url: 'api/v1/resource/users',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}							
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								status: Ext.getCmp('filterActiveStatus').getValue() ? Ext.getCmp('filterActiveStatus').getValue() : 'A',
								approvalState: Ext.getCmp('filterApprovalStatus').getValue() ? Ext.getCmp('filterApprovalStatus').getValue() : 'A',
								searchField: Ext.getCmp('filterUserSearchField').getValue(),
								searchValue: Ext.getCmp('filterSearchValue').getValue()
							};
						}
					}						
				});
				
				var securityPolicy;
				var initalUserLoad = function() {
				
					Ext.Ajax.request({
						url: 'api/v1/resource/securitypolicy',
						success: function(response, opts) {
							securityPolicy = Ext.decode(response.responseText);
							userGridStore.load();
						}
					});		
				};
				initalUserLoad();
				
				var userGrid = Ext.create('Ext.grid.Panel',{
					iconCls: 'fa fa-lg fa-users',
					title: 'Users',
					columnLines: true,
					store: userGridStore,
					columns: [
						{ text: 'Username', dataIndex: 'username', width: 200 },
						{ text: 'Firstname', dataIndex: 'firstname', width: 200 },
						{ text: 'Lastname', dataIndex: 'lastname', width: 200 },
						{ text: 'Email', dataIndex: 'email', minWidth: 200, flex: 1 },
						{ text: 'Approval Status', dataIndex: 'approvalStatus', width: 175, align: 'center',
							renderer: function(value, meta, record){
								return record.get('approvalStatusDescription');
							}
						},
						{ text: 'Last Login', dataIndex: 'lastLoginAttempt', align: 'center', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Failed Login Attempts', align: 'center', dataIndex: 'failedLoginAttempts', width: 200,
							renderer: function(value, meta, record) {
								if (value > securityPolicy.loginLockoutMaxAttempts) {
									meta.tdCls = 'alert-danger';
								} 
								return value;
							}
						},
						{ text: 'Pending Password Reset', dataIndex: 'pendingUserPasswordReset', width: 175, align: 'center',
							renderer: function(value, meta, record){
								if (value) {
									meta.tdCls = 'alert-warning';
									return 'Pending';
								}
								return '';
							}
						},
						{ text: 'Password Update Date', align: 'center', dataIndex: 'passwordUpdateDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Active', align: 'center', dataIndex: 'activeStatus', width: 125,
							renderer: function(value, meta, record){
								if (value === 'A') {
									return 'Active';
								} else {
									meta.tdCls = 'alert-danger';
									return 'Locked/Disabled';
								}
							}
						}
					],
					viewConfig: {
						enableTextSelection: true
					},	
					
					autoEl: {
						'data-test' : 'xPanelTable'
					},
					
					listeners: {
						selectionChange: function(selectionModel, records, opts) {
							var tools = userGrid.getComponent('tools');
							if (records.length > 0) {
								
								if (records[0].get('activeStatus') === 'A') {
									tools.getComponent('unlock').setDisabled(true);
								} else {
									tools.getComponent('unlock').setDisabled(false);
								}
								tools.getComponent('reset').setDisabled(false);		
								if (records[0].get('activeStatus') === 'A') {
									tools.getComponent('disable').setDisabled(false);
								} else {
									tools.getComponent('disable').setDisabled(true);
								}
								tools.getComponent('delete').setDisabled(false);
								tools.getComponent('role').setDisabled(false);	
								tools.getComponent('message').setDisabled(false);	
								
								if (records[0].get('approvalStatus') === 'P') {
									tools.getComponent('approve').setDisabled(false);
									tools.getComponent('unlock').setDisabled(true);									
									tools.getComponent('disable').setDisabled(true);
								} else {
									tools.getComponent('approve').setDisabled(true);
								}
								tools.getComponent('resetLogin').setDisabled(false);
							} else {
								tools.getComponent('unlock').setDisabled(true);
								tools.getComponent('reset').setDisabled(true);								
								tools.getComponent('disable').setDisabled(true);
								tools.getComponent('resetLogin').setDisabled(true);
								tools.getComponent('delete').setDisabled(true);	
								tools.getComponent('approve').setDisabled(true);
								tools.getComponent('role').setDisabled(true);
								tools.getComponent('message').setDisabled(true);
							}
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'filterActiveStatus',									
									emptyText: 'Active',
									fieldLabel: 'Active Status',
									name: 'activeStatus',									
									typeAhead: false,
									editable: false,
									width: 200,	
									autoEl: {
										'data-test' : 'userActiveStatus'
									},
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshUsers();
										}
									},
									storeConfig: {
										customStore: {
											fields: [
												'code',
												'description'
											],
											data: [												
												{
													code: 'A',
													description: 'Active'
												},
												{
													code: 'I',
													description: 'Locked/Disabled'
												}
											]
										}
									}
								}),															
								Ext.create('OSF.component.StandardComboBox', {
									id: 'filterApprovalStatus',									
									emptyText: 'Approved',
									fieldLabel: 'Approval Status',
									name: 'activeStatus',									
									typeAhead: false,
									editable: false,
									width: 200,							
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshUsers();
										}
									},
									storeConfig: {
										url: 'api/v1/resource/lookuptypes/UserApprovalStatus'
									}
								}),
								Ext.create('OSF.component.StandardComboBox', {
									id: 'filterUserSearchField',									
									value: 'username',
									fieldLabel: 'Search Field',
									name: 'selectField',									
									typeAhead: false,
									editable: false,
									width: 200,																
									storeConfig: {
										customStore: {
											fields: [
												'code',
												'description'
											],
											data: [												
												{
													code: 'username',
													description: 'Username'
												},
												{
													code: 'firstName',
													description: 'First Name'
												},
												{
													code: 'lastName',
													description: 'Last Name'
												},
												{
													code: 'email',
													description: 'Email'
												}
											]
										}
									}
								}),
								{
									xtype: 'textfield',
									id: 'filterSearchValue',
									width: 200,
									labelAlign: 'top',
									fieldLabel: 'Search Value',
									labelSeparator: '',
									name: 'searchValue',									
									maxLength: 255,
									listeners: {
										change: {
											buffer: 1000,
											fn: function() {
												actionRefreshUsers();
											}
										}
									}									
								}							
							]
						},
						{
							xtype: 'toolbar',
							itemId: 'tools',
							overflowHandler: 'menu',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									scale: 'medium',
									handler: function(){
										actionRefreshUsers();
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-UPDATE']
								},
								{
									text: 'Approve',
									itemId: 'approve',
									iconCls: 'fa fa-2x fa-check icon-button-color-save icon-vertical-correction-check',
									scale: 'medium',
									disabled: true,
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-UPDATE'],
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionApproveUser(record);
									}
								},
								{
									text: 'Reset Password',
									itemId: 'reset',
									iconCls: 'fa fa-2x fa-rotate-left icon-button-color-refresh icon-vertical-correction',
									scale: 'medium',
									disabled: true,
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-UPDATE'],
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionResetPassword(record);
									}
								},
								{
									text: 'Manage Roles',
									itemId: 'role',
									disabled: true,	
									hidden: true,
									iconCls: 'fa fa-2x fa-key icon-correction-key icon-button-color-key',
									scale: 'medium',
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-UPDATE'],
									handler: function() {
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionManageRoles(record);
									}
								},
								{
									text: 'Message',
									itemId: 'message',
									disabled: true,
									scale: 'medium',
									width: '130px',
									iconCls: 'fa fa-2x fa-envelope-o icon-button-color-default icon-vertical-correction-send',	
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-CREATE'],
									handler: function () {
										var record = userGrid.getSelection()[0];
										actionMessageUser(record);
									}										
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-UPDATE']
								},
								{
									text: 'Unlock Account',
									itemId: 'unlock',
									iconCls: 'fa fa-2x fa-unlock-alt icon-button-color-default icon-vertical-correction',
									scale: 'medium',
									disabled: true,
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-UPDATE'],
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionUnlockUser(record);
									}
								},
								{
									text: 'Lock Account',
									itemId: 'disable',
									iconCls: 'fa fa-2x fa-lock icon-button-color-default icon-vertical-correction',
									scale: 'medium',
									tooltip: 'Locks/Inactivates user account',
									disabled: true,
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-UPDATE'],
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionDisableUser(record);
									}
								},
								{
									text: 'Reset Failed Logins',
									itemId: 'resetLogin',
									iconCls: 'fa fa-2x fa-rotate-right icon-button-color-default icon-vertical-correction',
									scale: 'medium',
									tooltip: 'Resets Login Attempts',
									disabled: true,
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-UPDATE'],
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionResetLoginAttempts(record);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									scale: 'medium',
									width: '110px',
									disabled: true,
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-DELETE'],
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionDeleteUser(record);
									}									
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: userGridStore,
							displayInfo: true
						}
					]
				});
				
				var actionRefreshUsers = function() {
					userGrid.getStore().loadPage(1);
				};
				
				var actionApproveUser = function(record) {
					userGrid.setLoading('Approving user...');								
					Ext.Ajax.request({
						url: 'api/v1/resource/users/' + encodeURIComponent(record.get('username')) + '/approve',
						method: 'PUT',
						callback: function(){
							userGrid.setLoading(false);
						},
						success: function(response, opts) {
							actionRefreshUsers();
						}
					});						
				};
				
				var actionUnlockUser = function(record) {
					userGrid.setLoading('Unlocking user...');								
					Ext.Ajax.request({
						url: 'api/v1/resource/users/' + encodeURIComponent(record.get('username')) + '/unlock',
						method: 'PUT',
						callback: function(){
							userGrid.setLoading(false);
						},
						success: function(response, opts) {
							actionRefreshUsers();
						}
					});						
				};
				
				var actionResetLoginAttempts = function(record) {
					userGrid.setLoading('Resetting login attempts...');								
					Ext.Ajax.request({
						url: 'api/v1/resource/users/' + encodeURIComponent(record.get('username')) + '/resetfailedlogins',
						method: 'PUT',
						callback: function(){
							userGrid.setLoading(false);
						},
						success: function(response, opts) {
							actionRefreshUsers();
						}
					});						
				};
				
				var actionResetPassword = function(record) {
										
					var formWindow = Ext.create('Ext.window.Window', {
						title: 'Reset Password',
						iconCls: 'fa fa-warning',
						closeAction: 'destroy',
						modal: true,
						layout: 'fit',
						width: 400,
						height: 235,
						items: [
							{
								xtype: 'form',
								scrollable: true,
								bodyStyle: 'padding: 10px;',
								layout: 'anchor',
								items: [
									{
										xtype: 'textfield',
										labelAlign: 'top',
										fieldLabel: 'New Password <span class="field-required" />',
										fieldSeparator: '',
										name: 'password',
										width: '100%',
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
												text: 'Save',
												formBind: true,
												iconCls: 'fa fa-lg fa-save icon-button-color-save',
												handler: function() {
													
													var form = this.up('form');
													var data = form.getValues();
													
													formWindow.setLoading('Validating...');
													Ext.Ajax.request({
														url: 'api/v1/service/security/checkPassword',
														method: 'POST',														
														jsonData: data,
														callback: function() {
															formWindow.setLoading(false);
														},
														success: function(response, opts) {
															var checkData = Ext.decode(response.responseText);															
															if (checkData.success) {
																
																formWindow.setLoading('Updating password...');
																Ext.Ajax.request({
																	url: 'api/v1/resource/users/' + encodeURIComponent(record.get('username')) + '/resetpassword',
																	method: 'PUT',
																	jsonData: data,
																	callback: function() {
																		formWindow.setLoading(false);
																	},
																	success: function(response, opts) {
																		Ext.toast('Reset password Sucessfully');
																		formWindow.close();
																	}
																});
																
															} else {
																var errorObj = {};
																Ext.Array.each(checkData.errors.entry, function (item, index, entry) {
																	errorObj[item.key] = item.value;
																});
																form.getForm().markInvalid(errorObj);																
															}
														}
													});													
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Cancel',
												iconCls: 'fa fa-lg fa-close icon-button-color-warning',
												handler: function() {
													formWindow.close();
												}												
											}
										]
									}
								]
							}
						]
						
					});
					formWindow.show();
					
				};
				
				var actionDisableUser = function(record) {
				
					var doDisable = function() {
						userGrid.setLoading('Disabling user...');								
						Ext.Ajax.request({
							url: 'api/v1/resource/users/' + record.get('username') + '/disable',
							method: 'PUT',
							callback: function(){
								userGrid.setLoading(false);
							},
							success: function(response, opts) {
								actionRefreshUsers();
							}
						});							
					};
				
					if (record.get('username') === currentUser.username) {
						Ext.Msg.show({
							title: 'Disable Own Account?',
							message: '<br>Are you sure you want to disabled your account? <br> <b>You will be unable to relogin.</b>',
							buttons: Ext.Msg.YESNO,
							icon: Ext.Msg.WARNING,
							fn: function(btn) {
								if (btn === 'yes') {
									doDisable();
								}
							}
						});
					} else {
						doDisable();
					}
				};
				
				var actionDeleteUser = function(record) {
					Ext.Msg.show({
						title:'Delete User?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete user, <b>' + record.get('username') + '</b>?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								userGrid.setLoading('Deleting user...');								
								Ext.Ajax.request({
									url: 'api/v1/resource/users/' + record.get('username'),
									method: 'DELETE',
									callback: function(){
										userGrid.setLoading(false);
									},
									success: function(response, opts) {
										actionRefreshUsers();
									}
								});								
							} 
						}
					});	
				};
				
				var actionManageRoles = function(record) {
					
					var roleWin = Ext.create('Ext.window.Window', {
						title: 'Roles for user: ' + record.get('username'),						
						iconCls: 'fa fa-users',
						closeAction: 'destroy',
						width: 1000,
						height: 500,
						layout: {
							type: 'fit'
						},
						modal: true,
						items: [
							{
								xtype: 'grid',
								itemId: 'grid',																		
								columnLines: true,
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/users/' + encodeURIComponent(record.get('username')) + '/roles'
									},
									listeners: {
										load: function(store, records, opts) {
											
											var grid = roleWin.getComponent('grid');
											var form = grid.getComponent('form');
											var roleField = form.getComponent('roleName');
											roleField.getStore().on('load', function(roleStore, roleRecords, roleOpts){
												
												roleStore.filterBy(function(item) {
													var keep = true;
													store.each(function(userRole){
														if (item.get('code') === userRole.get('roleName')) {
															keep = false;
														}
													});											
													
													return keep;
												});
											});
											roleField.getStore().load();
											
										}
									}
								},
								columns: [
									{text: 'Role Name', dataIndex: 'roleName', width: 150},
									{text: 'Description', dataIndex: 'description', flex: 1, minWidth: 150},
									{
										xtype:'actioncolumn',
										width: 50,
										items:[
											{												
												iconCls: 'x-fa fa-trash icon-button-color-warning icon-small-horizontal-correction-trash',
												tooltip: 'delete',
												handler: function(grid, rowIndex, colIndex) {
													var selectedRole = grid.getStore().getAt(rowIndex);
													
													Ext.Msg.show({
														title:'Delete role from user?',
														iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
														message: 'Are you sure you want to delete <b>' + selectedRole.get('roleName') + '</b> from the user?',
														buttons: Ext.Msg.YESNO,
														icon: Ext.Msg.QUESTION,
														fn: function(btn) {
															if (btn === 'yes') {
																
																grid.setLoading('Removing User...');
																Ext.Ajax.request({
																	url: 'api/v1/resource/securityroles/' + encodeURIComponent(selectedRole.get('roleName')) + '/users/' + encodeURIComponent(record.get('username')),
																	method: 'DELETE',
																	callback: function() {
																		grid.setLoading(false);
																	},
																	success: function(response, opts) {
																		grid.getStore().load();
																	}	
																});																
															}
														}
													});													
												}
											}
										]
									}
								],
								dockedItems: [
									{
										xtype: 'form',
										itemId: 'form',
										dock: 'top',
										bodyStyle: 'padding: 10px;',
										layout: 'anchor',
										items: [
											{
												xtype: 'combobox',
												itemId: 'roleName',
												name: 'roleName',
												width: '100%',
												valueField: 'code',
												displayField: 'description',
												labelAlign: 'top',												
												fieldLabel: 'Add Role <span class="field-required" />',
												allowBlank: false,
												editable: false,
												forceSelection: true,
												queryMode: 'local',
												store: {
													autoLoad: false,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/securityroles/lookup',														
													}
												}
											}
										],
										dockedItems: [
											{
												xtype: 'toolbar',
												dock: 'bottom',
												style: 'margin-bottom:15px',
												items: [
													{
														xtype: 'tbfill'
													},
													{
														text: 'Add',
														formBind: true,
														iconCls: 'fa fa-lg fa-plus icon-button-color-save',
														handler: function(){
															var form = this.up('form');
															var data = form.getValues();
															var addRoleData = {
																username: record.get('username'),
																role: data.roleName
															};
															
															CoreUtil.submitForm({
																url: 'api/v1/resource/securityroles/' + encodeURIComponent(data.roleName) + '/users',
																method: 'POST',
																loadingText: 'Adding Role...',
																data: addRoleData,																
																form: form,
																success: function(response, opts) {
																	roleWin.getComponent('grid').getStore().load();
																	form.reset();																	
																}
															});															
														}
													},
													{
														text: 'Cancel',
														iconCls: 'fa fa-lg fa-close icon-button-color-warning',
														handler: function(){
															var form = this.up('form');
															form.reset();
														}														
													},
													{
														xtype: 'tbfill'
													}
												]
											}
										]
									}
								]
							}
						]
					});
					roleWin.show();					
		
				};
				
				var mainPanel = Ext.create('Ext.tab.Panel', {
					title: 'User Management <i class="fa fa-question-circle"  data-qtip="Manage users that are using the built in security realm."></i>',
					items: [
						userGrid,
						registrationGrid
					]
				});
				
				addComponentToMainViewPort(mainPanel);								
				
				var currentUser;
				CoreService.userservice.getCurrentUser().then(function(user){
					currentUser = user;
					if (CoreService.userservice.userHasPermission(user, "ADMIN-ROLES-PAGE")) {
						userGrid.getComponent('tools').getComponent('role').setHidden(false);					
					}									
				});					
				
			});
        </script>

    </stripes:layout-component>
</stripes:layout-render>
		