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
														method = 'PUT';
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
											
											var permissions = [];
											
											permissionWin.getComponent('rolePermissionsGrid').getStore().each(function(item){												
												permissions.push({
													permission: item.get('code')
												});
											});									
											
											var data = record.data;
											data.permissions = permissions;
											
											permissionWin.setLoading('Saving Permissions...');
											Ext.Ajax.request({
												url: 'api/v1/resource/securityroles/' + encodeURIComponent(record.get('roleName')),
												method: 'PUT',
												jsonData: data,
												callback: function(){
													permissionWin.setLoading(false);
												},
												success: function(response, opts) {
													Ext.toast('Updated Permissions.')
													permissionWin.close();
												}
											});
											
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
						title: 'Data Restrictions for Role: ' + record.get('roleName'),						
						iconCls: 'fa fa-legal',
						closeAction: 'destroy',
						width: 1000,
						height: 500,
						layout: {
							type: 'fit'
						},
						modal: true,
						items: [
							{
								xtype: 'tabpanel',
								items: [
									{
										xtype: 'panel',																
										title: 'Data Sources',
										layout: {
											type: 'hbox',
											align: 'stretch'
										},
										items: [
											{
												xtype: 'grid',
												id: 'dataSourcesGrid',
												title: 'Available (Drag record to Add)',
												width: '50%',
												margin: '0 5 0 0',
												columnLines: true,
												store: {	
													autoLoad: true,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/lookuptypes/DataSource'
													},
													listeners: {
														load: function(store, records, opts) {
															
															var sourcesInList = [];
															Ext.Array.each(record.data.dataSecurity, function(inListSource){
																Ext.getCmp('dataSourcesGrid').getStore().filterBy(function(item){
																	var include = true;
																	if (item.get('code') === sourcesInList.dataSource) {
																		sourcesInList.push(item);
																		include = false;
																	}
																	return include;
																});
															});
															Ext.getCmp('dataSourcesInRoleGrid').getStore().loadRecords(sourcesInList);
															
														}
													}											
												},
												viewConfig: {
													plugins: {
														ptype: 'gridviewdragdrop',
														dragText: 'Drag and drop to Add to Role'
													}
												},										
												columns: [
													{ text: 'Code', dataIndex: 'code', width: 200},
													{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200}
												]
											},
											{
												xtype: 'grid',
												id: 'dataSourcesInRoleGrid',
												title: 'In Role (Drag record to Remove)',
												width: '50%',
												margin: '0 5 0 0',
												columnLines: true,
												store: {																						
												},
												viewConfig: {
													plugins: {
														ptype: 'gridviewdragdrop',
														dragText: 'Drag and drop to Available to Remove from Role'
													}
												},										
												columns: [
													{ text: 'Code', dataIndex: 'code', width: 200},
													{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200}
												]										
											}
										]
									},
									{
										xtype: 'panel',
										title: 'Data Sensitivity',
										width: '100%',
										height: '50%',
										layout: {
											type: 'hbox',
											align: 'stretch'
										},
										items: [
											{
												xtype: 'grid',
												id: 'dataSensitivityGrid',
												title: 'Available',
												width: '50%',
												margin: '0 5 0 0',
												columnLines: true,
												store: {	
													autoLoad: true,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/lookuptypes/DataSensitivity'
													},
													listeners: {
														load: function(store, records, opts) {
															
															var sourcesInList = [];
															Ext.Array.each(record.data.dataSecurity, function(inListSource){
																Ext.getCmp('dataSensitivityGrid').getStore().filterBy(function(item){
																	var include = true;
																	if (item.get('code') === sourcesInList.dataSensitivity) {
																		sourcesInList.push(item);
																		include = false;
																	}
																	return include;
																});
															});
															Ext.getCmp('dataSensitivitiesInRoleGrid').getStore().loadRecords(sourcesInList);
														}
													}											
												},
												viewConfig: {
													plugins: {
														ptype: 'gridviewdragdrop',
														dragText: 'Drag and drop to Add to Role'
													}
												},										
												columns: [
													{ text: 'Code', dataIndex: 'code', width: 200},
													{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200}
												]
											},
											{
												xtype: 'grid',
												id: 'dataSensitivitiesInRoleGrid',
												title: 'In Role',
												width: '50%',
												margin: '0 5 0 0',
												columnLines: true,
												store: {																						
												},
												viewConfig: {
													plugins: {
														ptype: 'gridviewdragdrop',
														dragText: 'Drag and drop to Available to Remove from Role'
													}
												},										
												columns: [
													{ text: 'Code', dataIndex: 'code', width: 200},
													{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200}
												]										
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
										text: 'Save',
										iconCls: 'fa fa-2x fa-save',
										scale: 'medium',
										handler: function() {
											
											//pull data sources
											
											//dataSourcesInRoleGrid
											
											//data sensitivity
											
											//dataSensitivitiesInRoleGrid
											
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
											dataWin.close();
										}										
									}
								]
							}
						]
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
								itemId: 'grid',										
								title: 'Users In Role',
								columnLines: true,
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/securityroles/' + encodeURIComponent(record.get('roleName')) + '/users'
									}
								},
								columns: [
									{text: 'username', dataIndex: 'username', flex: 1, minWidth: 150},
									{
										xtype:'actioncolumn',
										width: 50,
										items:[
											{												
												iconCls: 'x-fa fa-trash',
												tooltip: 'delete',
												handler: function(grid, rowIndex, colIndex) {
													var selectedUser = grid.getStore().getAt(rowIndex);
													
													Ext.Msg.show({
														title:'Delete user from Role?',
														message: 'Are you sure you want to remove ' + selectedUser.get('username') + ' from the role?',
														buttons: Ext.Msg.YESNO,
														icon: Ext.Msg.QUESTION,
														fn: function(btn) {
															if (btn === 'yes') {
																
																grid.setLoading('Removing User...');
																Ext.Ajax.request({
																	url: 'api/v1/resource/securityroles/' + encodeURIComponent(record.get('roleName')) + '/users/' + encodeURIComponent(selectedUser.get('username')),
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
										dock: 'top',
										bodyStyle: 'padding: 10px;',
										layout: 'anchor',
										items: [
											{
												xtype: 'combobox',
												name: 'username',
												width: '100%',
												valueField: 'username',
												tpl: Ext.create('Ext.XTemplate',
													'<ul class="x-list-plain"><tpl for=".">',
														'<li role="option" class="x-boundlist-item">{firstname} {lastname} - {email}</li>',
													'</tpl></ul>'
												),											
												displayTpl: Ext.create('Ext.XTemplate',
													'<tpl for=".">',
														'{firstname} {lastname} - {email}',
													'</tpl>'
												),
												labelAlign: 'top',												
												fieldLabel: 'Add User <span class="field-required" />',
												allowBlank: false,
												forceSelection: true,
												queryMode: 'remote',
												store: {
													autoLoad: false,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/users',
														reader: {
															type: 'json',
															rootProperty: 'data',
															totalProperty: 'totalNumber'
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
														xtype: 'tbfill'
													},
													{
														text: 'Add',
														formBind: true,
														iconCls: 'fa fa-plus',
														handler: function(){
															var form = this.up('form');
															var data = form.getValues();
															var addUserData = {
																username: data.username,
																role: record.get('roleName')
															};
															
															CoreUtil.submitForm({
																url: 'api/v1/resource/securityroles/' + encodeURIComponent(record.get('roleName')) + '/users',
																method: 'POST',
																loadingText: 'Adding User...',
																data: addUserData,																
																form: form,
																success: function(response, opts) {
																	userWin.getComponent('grid').getStore().load();
																	form.reset();																	
																}
															});															
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
			