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
				
				
				var roleGrid = Ext.create('Ext.grid.Panel', {
					title: 'Security Role Management <i class="fa fa-question-circle"  data-qtip="Manage security roles that allow access to features in the application."></i>',
					columnLines: true,
					store: {
						fields: [
							{
								name: 'createDts',
								type:	'date',
								dateFormat: 'c'
							},
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							}	
						],						
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/securityroles'
						}
					},
					columns: [
						{ text: 'Name', dataIndex: 'roleName', width: 200 },
						{ text: 'Description', dataIndex: 'description',flex: 1, minWidth: 200 },
						{ text: 'Landing Page', dataIndex: 'landingPage', width: 200 },
						{ text: 'Landing Page Priority', dataIndex: 'landingPagePriority', width: 200 },						
						{ text: 'Allow Unspecified Data Source', dataIndex: 'allowUnspecifiedDataSource', width: 200 },
						{ text: 'Allow Unspecified Data Sensitivity', dataIndex: 'allowUnspecifiedDataSensitivity', width: 200 },
						{ text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s', hidden: true },
						{ text: 'Create User', dataIndex: 'createUser', width: 200, hidden: true  },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s', hidden: true },
						{ text: 'Update User', dataIndex: 'updateUser', width: 200, hidden: true  }
					],
					listeners: {
						selectionchange: function(selModel, records, opts) {
							var tools = roleGrid.getComponent('tools');
							if (records.length > 0) {					
								tools.getComponent('edit').setDisabled(false);
								tools.getComponent('users').setDisabled(false);
								tools.getComponent('permissions').setDisabled(false);
								tools.getComponent('dataRestrictions').setDisabled(false);
								tools.getComponent('delete').setDisabled(false);
							} else {
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('users').setDisabled(true);
								tools.getComponent('permissions').setDisabled(true);
								tools.getComponent('dataRestrictions').setDisabled(true);
								tools.getComponent('delete').setDisabled(true);								
							}
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							itemId: 'tools',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									iconCls: 'fa fa-2x fa-refresh',
									scale: 'medium',
									handler: function() {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									itemId: 'add',
									iconCls: 'fa fa-2x fa-plus',
									scale: 'medium',
									handler: function() {
										actionAddEditRole();
									}
								},								
								{
									text: 'Edit',
									itemId: 'edit',
									disabled: true,
									iconCls: 'fa fa-2x fa-edit',
									scale: 'medium',
									handler: function() {
										var record = roleGrid.getSelectionModel().getSelection()[0];
										actionAddEditRole(record);
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Manage Users',
									itemId: 'users',
									disabled: true,
									hidden: true,
									iconCls: 'fa fa-2x fa-users',
									scale: 'medium',
									handler: function() {
										var record = roleGrid.getSelectionModel().getSelection()[0];
										actionManageUsers(record);
									}
								},								
								{
									text: 'Manage Permissions',
									itemId: 'permissions',
									disabled: true,
									iconCls: 'fa fa-2x fa-key',
									scale: 'medium',
									handler: function() {
										var record = roleGrid.getSelectionModel().getSelection()[0];
										actionManagePermissions(record);
									}
								},								
								{
									text: 'Manage Data Restrictions',
									itemId: 'dataRestrictions',
									disabled: true,
									iconCls: 'fa fa-2x fa-legal',
									scale: 'medium',
									handler: function() {
										var record = roleGrid.getSelectionModel().getSelection()[0];
										actionManageData(record);										
									}
								},								
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									disabled: true,
									iconCls: 'fa fa-2x fa-trash',
									scale: 'medium',
									handler: function() {
										var record = roleGrid.getSelectionModel().getSelection()[0];
										actionManageDelete(record);										
									}									
								}
							]
						}
					]
					
				});
				
				addComponentToMainViewPort(roleGrid);
				
				var actionRefresh = function() {
					roleGrid.getStore().reload();
				};
				
				var actionAddEditRole = function(record) {
					
					var addEditWin = Ext.create('Ext.window.Window', {
						title: 'Add/Edit Roles',
						closeAction: 'destroy',
						layout: 'fit',
						modal: true,
						width: '50%',
						height: 510,
						items: [
							{
								xtype: 'form',
								itemId: 'form',
								scrollable: true,
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
										name: 'roleName',
										fieldLabel: 'Role Name <span class="field-required" /> (Must be unique)',
										maxLength: 255,
										allowBlank: true
									},
									{
										xtype: 'textfield',
										name: 'description',
										fieldLabel: 'Description <span class="field-required" />',
										maxLength: 255,
										allowBlank: true										
									},
									{
										xtype: 'textfield',
										name: 'landingPage',
										fieldLabel: 'Landing Page <span class="field-required" />',
										maxLength: 255,
										allowBlank: true										
									},
									{
										xtype: 'numberfield',
										name: 'landingPagePriority',
										fieldLabel: 'Landing Page Priority',
										minValue: -1,
										maxValue: 2000,
										allowBlank: true										
									},
									{
										xtype: 'checkbox',
										name: 'allowUnspecifiedDataSource',
										boxLabel: 'Allow Unspecified Data Source'
									},
									{
										xtype: 'checkbox',
										name: 'allowUnspecifiedDataSensitivity',
										boxLabel: 'Allow Unspecified Data Sensitivity'
									}
								],
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items:[
											{
												text: 'Save',
												formBind: true,
												iconCls: 'fa fa-2x fa-save',
												scale: 'medium',
												handler: function() {
													var form = this.up('form');
													
													var data = form.getValues();													
													
													var method = 'POST';
													var urlEnd = '';
													if (record) {
														urlEnd = '/' + data.roleName;
														data.permissions = record.data.permissions;
														data.dataSecurity = record.data.dataSecurity;
													}	
													
													CoreUtil.submitForm({
														url: 'api/v1/resource/securityroles' + urlEnd,
														method: method,
														data: data,
														form: form,
														success: function(action, opts) {
															actionRefresh();
															addEditWin.close();
														}
													});													
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Close',
												iconCls: 'fa fa-2x fa-close',
												scale: 'medium',
												handler: function() {
													addEditWin.close();
												}												
											}
										]
									}
								]
							}
						]						
					});
					addEditWin.show();
					
					if (record) {
						addEditWin.getComponent('form').loadRecord(record);
					}					
				};
				
				var actionManageDelete = function(record) {
					
					//prompt of moving existing user to new role
					var deleteWin = Ext.create('Ext.window.Window', {
						title: 'Delete Role: ' + record.get('roleName') + '?',						
						iconCls: 'fa fa-question',
						closeAction: 'destroy',
						width: 400,
						height: 200,
						layout: 'fit',
						modal: true,
						items: [
							{
								xtype: 'form',
								scrollable: true,
								layout: 'anchor',
								bodyStyle: 'padding: 10px',
								items: [
									{
										xtype: 'combobox',
										name: 'movetorole',
										fieldLabel: 'Move users to role (Optional)',
										labelAlign: 'top',
										valueField: 'roleName',
										displayField: 'roleName',
										forceSelection: true,
										width: '100%',
										store: {
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/securityroles'
											},
											listeners: {
												load: function(store, records, opts) {
													//remove currently selected record
													store.filterBy(function(item){
														if (item.get('roleName') === record.get('roleName')) {
															return false;
														}
														return true;
													});
													
												}
											}
										}
									}									
								],
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Delete Role',																								
												iconCls: 'fa fa-2x fa-trash',
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
													deleteWin.close();
												}												
											}
										]
									}
								]
							}							
						]
					});
					deleteWin.show();
					
				};
				
				var actionManagePermissions = function(record) {
						
					var permissionWin = Ext.create('Ext.window.Window', {
						title: 'Permissions for Role: ' + record.get('roleName'),						
						iconCls: 'fa fa-key',
						closeAction: 'destroy',
						width: 1000,
						height: 500,
						layout: {
							type: 'hbox',
							align: 'stretch'
						},
						modal: true,
						items: [
							{
								xtype: 'grid',
								itemId: 'availableGrid',
								title: 'Permissions Available',
								width: '50%',
								margin: '0 5 0 0',
								columnLines: true,
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/lookuptypes/SecurityPermission'
									},
									listeners: {
										load: function(store, records, opts) {
											var permissionsInList = [];
											Ext.Array.each(record.data.permissions, function(inListPermission){
												permissionWin.getComponent('availableGrid').getStore().filterBy(function(item){
													var include = true;
													if (item.get('code') === inListPermission.permission) {
														permissionsInList.push(item);
														include = false;
													}
													return include;
												});
											});
											permissionWin.getComponent('rolePermissionsGrid').getStore().loadRecords (permissionsInList);
										}
									}
								},
								viewConfig: {
									plugins: {
										ptype: 'gridviewdragdrop',
										dragText: 'Drag and drop to Add to Role Permissions'
									}
								},
								columns: [
									{ text: 'Code', dataIndex: 'code', width: 200},
									{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200}
								]
							},
							{
								xtype: 'grid',
								itemId: 'rolePermissionsGrid',
								title: 'Permissions In Role',
								width: '50%',
								columnLines: true,
								store: {									
								},
								viewConfig: {
									plugins: {
										ptype: 'gridviewdragdrop',
										dragText: 'Drag and drop to Available to Remove Role'
									}
								},
								columns: [
									{ text: 'Code', dataIndex: 'code', width: 200},
									{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200}
								]						
							}
						],
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Save',
										iconCls: 'fa fa-2x fa-save',
										scale: 'medium',
										handler: function(){
											
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Close',
										iconCls: 'fa fa-2x fa-close',
										scale: 'medium',
										handler: function(){
											permissionWin.close();
										}										
									}
								]
							}
						]
					});
					permissionWin.show();
					
					
						
				};				
				
				var actionManageData = function(record) {
					
					var dataWin = Ext.create('Ext.window.Window', {
						
					});					
					dataWin.show();
				};				
				
				var actionManageUsers = function(record) {
					
					var userWin = Ext.create('Ext.window.Window', {
						title: 'Users for Role: ' + record.get('roleName'),						
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
								title: 'Users In Role',
								columnLines: true,
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/securityroles/' + record.get('roleName') + '/users'
									}
								},
								columns: [
									{text: 'username', dataIndex: 'username', flex: 1, minWidth: 150},
									{
										xtype:'actioncolumn',
										width: 50,
										items:[
											{
												iconCls: 'fa fa-trash',
												tooltip: 'delete',
												handler: function(grid, rowIndex, colIndex) {
													
												}
											}
										]
									}
								],
								dockedItems: [
									{
										xtype: 'form',
										dock: 'top',
										bodyStyle: 'padding: 10px;',
										items: [
											{
												xtype: 'combobox',
												name: 'username',
												valueField: 'username',
												displayField: 'username',
												labelAlign: 'top',												
												fieldLabel: 'Add User <span class="field-required" />',
												allowBlank: false
											}
										],
										dockedItems: [
											{
												xtype: 'toolbar',
												dock: 'bottom',
												items: [
													{
														xtype: 'tbfill'
													},
													{
														text: 'Add',
														formBind: true,
														iconCls: 'fa fa-plus',
														handler: function(){
															
														}
													},
													{
														text: 'Cancel',
														iconCls: 'fa fa-close',
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
					userWin.show();
					
				};
				
				
				CoreService.userservice.getCurrentUser().then(function(user){
					if (CoreService.userservice.userHasPermisson(user, "ADMIN-USER-MANAGEMENT")) {
						roleGrid.getComponent('tools').getComponent('users').setHidden(false);					
					}									
				});					
				
			});
			
        </script>

    </stripes:layout-component>
</stripes:layout-render>
			