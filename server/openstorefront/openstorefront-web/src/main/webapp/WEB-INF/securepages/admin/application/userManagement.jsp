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
				
				var registrationGrid = Ext.create('Ext.grid.Panel',{
					iconCls: 'fa fa-lg fa-user-plus',
					title: 'Registrations',
					columnLines: true,
					store: {						
					},
					columns: [
						{ text: 'Username', dataIndex: 'username' }
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									iconCls: 'fa fa-2x fa-refresh',
									scale: 'medium',
									handler: function(){
										
									}
								}
							]
						}
					]
				});
				
				var userGridStore = Ext.create('Ext.data.Store',{
					pageSize: 500,
					autoLoad: true,
					fields: [
						{
							name: 'lastLoginAttempt',
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
						{ text: 'Approval Status', dataIndex: 'approvalStatus', width: 200, align: 'center',
							renderer: function(value, meta, record){
								return record.get('approvalStatusDescription');
							}
						},
						{ text: 'Last Login', dataIndex: 'lastLoginAttempt', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Failed Login Attempts', align: 'center', dataIndex: 'failedLoginAttempts', width: 200 },
						{ text: 'Pending Password Reset', dataIndex: 'pendingUserPasswordReset', width: 200, align: 'center',
							renderer: function(value, meta, record){
								if (value) {
									meta.tdCls = 'button-warning';
								}
								return value;
							}
						},
						{ text: 'Active', align: 'center', dataIndex: 'activeStatus', width: 200,
							renderer: function(value, meta, record){
								if (value) {
									return 'Active';
								} else {
									meta.tdCls = 'button-danger';
									return 'Locked/Disabled';
								}
							}
						}
					],
					listeners: {
						selectionChange: function(selectionModel, records, opts) {
							var tools = userGrid.getComponent('tools');
							if (records.length > 0) {
								tools.getComponent('unlock').setDisabled(false);
								tools.getComponent('reset').setDisabled(false);								
								tools.getComponent('disable').setDisabled(false);
								tools.getComponent('delete').setDisabled(false);
								tools.getComponent('approve').setDisabled(false);								
							} else {
								tools.getComponent('unlock').setDisabled(true);
								tools.getComponent('reset').setDisabled(true);								
								tools.getComponent('disable').setDisabled(true);
								tools.getComponent('delete').setDisabled(true);	
								tools.getComponent('approve').setDisabled(true);
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
									emptyText: 'Acivate',
									fieldLabel: 'Active Status',
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
													description: 'Firstname'
												},
												{
													code: 'lastName',
													description: 'lastname'
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
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									iconCls: 'fa fa-2x fa-refresh',
									scale: 'medium',
									handler: function(){
										actionRefreshUsers();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Approve',
									itemId: 'approve',
									iconCls: 'fa fa-2x fa-check',
									scale: 'medium',
									disabled: true,
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionApproveUser(record);
									}
								},
								{
									text: 'Unlock',
									itemId: 'unlock',
									iconCls: 'fa fa-2x fa-unlock',
									scale: 'medium',
									disabled: true,
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionUnlockUser(record);
									}
								},
								{
									text: 'Reset Password',
									itemId: 'reset',
									iconCls: 'fa fa-2x fa-rotate-left',
									scale: 'medium',
									disabled: true,
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionResetPassword(record);
									}
								},
								{
									xtype: 'tbseparator'
								},								
								{
									text: ' Disable',
									itemId: 'disable',
									iconCls: 'fa fa-2x fa-user-times',
									scale: 'medium',
									disabled: true,
									handler: function(){
										var record = userGrid.getSelectionModel().getSelection()[0];
										actionDisableUser(record);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									iconCls: 'fa fa-2x fa-trash',
									scale: 'medium',
									disabled: true,
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
						url: 'api/v1/resource/users/' + record.get('username') + '/approve',
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
				
				var actionResetPassword = function(record) {
										
					var formWindow = Ext.create('Ext.window.Window', {
						title: 'Reset Password',
						iconCls: 'fa fa-rotate-left',
						closeAction: 'destroy',
						modal: true,
						layout: 'fit',
						width: 400,
						height: 200,
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
										maxLength: 80
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
												iconCls: 'fa fa-save',
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
												iconCls: 'fa fa-close',
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
				
				var actionDeleteUser = function(record) {
					Ext.Msg.show({
						title:'Delete User?',
						message: 'Are you sure you want to delete ' + record.get('username'),
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
				
				
				var mainPanel = Ext.create('Ext.tab.Panel', {
					title: 'User Management <i class="fa fa-question-circle"  data-qtip="Manage users that are using the built in security realm."></i>',
					items: [
						userGrid,
						registrationGrid
					]
				});
				
				addComponentToMainViewPort(mainPanel);				
			});
        </script>

    </stripes:layout-component>
</stripes:layout-render>
		