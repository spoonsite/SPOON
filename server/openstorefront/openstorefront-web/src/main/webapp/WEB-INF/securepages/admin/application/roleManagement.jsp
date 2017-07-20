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
						{ text: 'Landing Page', dataIndex: 'landingPage', width: 225 },
						{ text: 'Landing Page Priority', dataIndex: 'landingPagePriority', width: 225 },						
						{ text: 'Allow Unspecified Data Source', dataIndex: 'allowUnspecifiedDataSource', width: 225, align: 'center',
							
							renderer: CoreUtil.renderer.booleanRenderer
						},
						{ text: 'Allow Unspecified Data Sensitivity', dataIndex: 'allowUnspecifiedDataSensitivity', width: 225, align: 'center',
							
							renderer: CoreUtil.renderer.booleanRenderer
						},
						{ text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s', hidden: true },
						{ text: 'Create User', dataIndex: 'createUser', width: 200, hidden: true  },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s', hidden: true },
						{ text: 'Update User', dataIndex: 'updateUser', width: 200, hidden: true  }
					],
			
					autoEl: {
						'data-test' : 'securityRolesTable'
					},
					
			listeners: {
						selectionchange: function(selModel, records, opts) {
							var tools = roleGrid.getComponent('tools');
							if (records.length > 0) {					
								tools.getComponent('edit').setDisabled(false);
								if (records[0].get('roleName') !== 'DEFAULT-GROUP') {
									tools.getComponent('users').setDisabled(false);
								} else {
									tools.getComponent('users').setDisabled(true);
								}
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
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									scale: 'medium',
									width: '110px',
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
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction-add',
									width: '100px',
									scale: 'medium',
									handler: function() {
										actionAddEditRole();
									}
								},								
								{
									text: 'Edit',
									itemId: 'edit',
									disabled: true,
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
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
									width: '170px',
									iconCls: 'fa fa-2x fa-users icon-button-color-default icon-correction-users',
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
									iconCls: 'fa fa-2x fa-key icon-correction-key icon-button-color-key',
									scale: 'medium',
									width: '200px',
									handler: function() {
										var record = roleGrid.getSelectionModel().getSelection()[0];
										actionManagePermissions(record);
									}
								},								
								{
									text: 'Manage Data Restrictions',
									itemId: 'dataRestrictions',
									disabled: true,
									iconCls: 'fa fa-2x fa-legal icon-correction-gavel icon-button-color-default',
									width: '240px',
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
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									width: '100px',
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
						iconCls: 'fa fa-2x fa-user',
						closeAction: 'destroy',
						layout: 'fit',
						modal: true,
						width: '50%',
						minHeight: 525,
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
										itemId: 'roleName',
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
												iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
												scale: 'medium',
												height: '40px',
												handler: function() {
													var form = this.up('form');
													
													var data = form.getValues();													
													
													var method = 'POST';
													var urlEnd = '';
													if (record) {
														method = 'PUT';
														urlEnd = data.roleName;
														data.permissions = record.data.permissions;
														data.dataSecurity = record.data.dataSecurity;
													}	
													if (!data.allowUnspecifiedDataSource) {
														data.allowUnspecifiedDataSource = false;
													}
													if (!data.allowUnspecifiedDataSensitivity) {
														data.allowUnspecifiedDataSensitivity = false;
													}													
													
													CoreUtil.submitForm({
														url: 'api/v1/resource/securityroles/' + encodeURIComponent(urlEnd),
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
												iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
												scale: 'medium',
												height: '40px',
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
						addEditWin.queryById('roleName').setReadOnly(true);
					}				
				};
				
				var actionManageDelete = function(record) {
					
					//prompt of moving existing user to new role
					var deleteWin = Ext.create('Ext.window.Window', {
						title: 'Delete Role: ' + record.get('roleName') + '?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
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
												text: 'Confirm',																								
												iconCls: 'fa fa-lg fa-check icon-button-color-save',
												scale: 'small',
												handler: function(){
													var form = this.up('form');
													
													var data = form.getValues();		
													deleteWin.setLoading('Deleting role...');
													Ext.Ajax.request({
														url: 'api/v1/resource/securityroles/' + encodeURIComponent(record.get('roleName')) + '?movetorole=' + data.movetorole,
														method: 'DELETE',
														callback: function() {
															deleteWin.setLoading(false);
														},
														success: function() {
															actionRefresh();
															deleteWin.close();
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
												scale: 'small',
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
						title: record.get('roleName') + ' Permissions',						
						iconCls: 'fa fa-key icon-correction-key icon-button-color-key icon-small-vertical-correction',
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
								autoEl: {
									'data-test' : 'permissionsAvailableTable'
								},

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
											var permissionsAvailable = [];
											
											permissionWin.getComponent('availableGrid').getStore().each(function(item){
												var roleHasPermission = false;
												Ext.Array.each(record.data.permissions, function(inListPermission){													
													if (item.get('code') === inListPermission.permission) {														roleHasPermission = true;
														roleHasPermission = true;
													} 
												});
												if (roleHasPermission) {
													permissionsInList.push(item);
												} else {
													permissionsAvailable.push(item);
												}
											});
											
											permissionWin.getComponent('availableGrid').getStore().removeAll();
											permissionWin.getComponent('availableGrid').getStore().loadRecords(permissionsAvailable);
											permissionWin.getComponent('rolePermissionsGrid').getStore().loadRecords(permissionsInList);
										}
									}
								},
								viewConfig: {
									plugins: {
										ptype: 'gridviewdragdrop',
										dragText: 'Drag and drop to <b>Current Role Permissions</b> to add'
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
								title: 'Current Role Permissions',
								autoEl: {
									'data-test' : 'currentRolePermissionsTable'
								},
								width: '50%',
								columnLines: true,
								store: {									
								},
								viewConfig: {
									plugins: {
										ptype: 'gridviewdragdrop',
										dragText: 'Drag and drop to <b>Permissions Available</b> to delete Role'
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
								style: 'margin-top:20px',
								items: [
									{
										text: 'Save',
										iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
										scale: 'medium',
										height: '40px',
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
										iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
										scale: 'medium',
										height: '40px',
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
						title:  record.get('roleName') + ' Data Restrictions',						
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
												title: 'Restricted <i class="fa fa-question-circle"  data-qtip="Drag record to remove restriction from ' + record.get('roleName') + '"></i>',
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
															var sourcesAvaliable = [];
															Ext.getCmp('dataSourcesGrid').getStore().each(function(item){
																var include = false;
																Ext.Array.each(record.data.dataSecurity, function(inListSource){
																
																	if (item.get('code') === inListSource.dataSource) {																		
																		include = true;
																	}
																});
																if (include) {
																	sourcesInList.push(item);
																} else {
																	sourcesAvaliable.push(item);
																}
															});
															Ext.getCmp('dataSourcesGrid').getStore().removeAll();
															Ext.getCmp('dataSourcesGrid').getStore().loadRecords(sourcesAvaliable);
															Ext.getCmp('dataSourcesInRoleGrid').getStore().loadRecords(sourcesInList);
															
														}
													}											
												},
												viewConfig: {
													plugins: {
														ptype: 'gridviewdragdrop',
														dragText: 'Add: {0}',
														dragTextField: 'description'
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
												title: 'Accessible <i class="fa fa-question-circle"  data-qtip="Drag record to add restriction to ' + record.get('roleName') + '"></i>',
												width: '50%',
												margin: '0 5 0 0',
												columnLines: true,
												store: {																						
												},
												viewConfig: {
													plugins: {
														ptype: 'gridviewdragdrop',
														dragText: 'Remove: {0}',
														dragTextField: 'description'
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
												title: 'Restricted <i class="fa fa-question-circle"  data-qtip="Drag record to add to ' + record.get('roleName') + '"></i>',
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
															var sourcesAvaliable = [];
															Ext.getCmp('dataSensitivityGrid').getStore().each(function(item){
																var include = false;
																Ext.Array.each(record.data.dataSecurity, function(inListSource){
																
																	if (item.get('code') === inListSource.dataSensitivity) {																		
																		include = true;
																	}
																});
																if (include) {
																	sourcesInList.push(item);
																} else {
																	sourcesAvaliable.push(item);
																}
															});
															Ext.getCmp('dataSensitivityGrid').getStore().removeAll();
															Ext.getCmp('dataSensitivityGrid').getStore().loadRecords(sourcesAvaliable);
															Ext.getCmp('dataSensitivitiesInRoleGrid').getStore().loadRecords(sourcesInList);
														}
													}											
												},
												viewConfig: {
													plugins: {
														ptype: 'gridviewdragdrop',
														dragText: 'Add: {0}',
														dragTextField: 'description'
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
												title: 'Accessible <i class="fa fa-question-circle"  data-qtip="Drag record to remove from ' + record.get('roleName') + '"></i>',
												width: '50%',
												margin: '0 5 0 0',
												columnLines: true,
												store: {																						
												},
												viewConfig: {
													plugins: {
														ptype: 'gridviewdragdrop',
														dragText: 'Remove: {0}',
														dragTextField: 'description'
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
										iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
										scale: 'medium',
										height: '40px',
										handler: function() {
											
											var dataRestrictions = [];
											
											Ext.getCmp('dataSourcesInRoleGrid').getStore().each(function(item){												
												dataRestrictions.push({
													dataSource: item.get('code')
												});
											});			
											
											Ext.getCmp('dataSensitivitiesInRoleGrid').getStore().each(function(item){												
												dataRestrictions.push({
													dataSensitivity: item.get('code')
												});
											});											
											
											var data = record.data;
											data.dataSecurity = dataRestrictions;
											
											dataWin.setLoading('Saving Data Restrictions...');
											Ext.Ajax.request({
												url: 'api/v1/resource/securityroles/' + encodeURIComponent(record.get('roleName')),
												method: 'PUT',
												jsonData: data,
												callback: function(){
													dataWin.setLoading(false);
												},
												success: function(response, opts) {
													Ext.toast('Updated Data Restrictions.')
													dataWin.close();
												}
											});											
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
										scale: 'medium',
										height: '40px',
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
						title: 'Users with ' + record.get('roleName') + ' role',						
						iconCls: 'fa fa-users',
						closeAction: 'destroy',
						width: 700,
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
										url: 'api/v1/resource/securityroles/' + encodeURIComponent(record.get('roleName')) + '/users'
									}
								},
								columns: [
									{text: 'Username', dataIndex: 'username', flex: 1, minWidth: 150},
									{
										xtype:'actioncolumn',
										width: 50,
										items:[
											{												
												iconCls: 'x-fa fa-trash icon-button-color-warning icon-small-horizontal-correction-trash',
												tooltip: 'delete',
												handler: function(grid, rowIndex, colIndex) {
													var selectedUser = grid.getStore().getAt(rowIndex);
													
													Ext.Msg.show({
														title:'Delete role from user?',
														iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
														message: 'Are you sure you want to delete ' + record.get('roleName') + ' role from ' + selectedUser.get('username') + '?',
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
														'<li role="option" class="x-boundlist-item">{username} <span style="color: #a8a8a8;"> - {email}</span></li>',
													'</tpl></ul>'
												),											
												displayTpl: Ext.create('Ext.XTemplate',
													'<tpl for=".">',
														'{username}',
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
												style: 'margin-bottom:15px',
												items: [
													{
														xtype: 'tbfill'
													},
													{
														text: 'Add',
														formBind: true,
														iconCls: 'fa fa-lg fa-plus icon-button-color-save',
														width: '80px',
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
			