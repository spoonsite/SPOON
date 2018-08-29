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
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-CREATE', 'ADMIN-ROLE-MANAGEMENT-UPDATE']
								},
								{
									text: 'Add',
									itemId: 'add',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction-add',
									width: '100px',
									scale: 'medium',
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-CREATE'],
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
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-UPDATE'],
									handler: function() {
										var record = roleGrid.getSelectionModel().getSelection()[0];
										actionAddEditRole(record);
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-UPDATE']
								},
								{
									text: 'Manage Users',
									itemId: 'users',
									disabled: true,
									width: '170px',
									iconCls: 'fa fa-2x fa-users icon-button-color-default icon-correction-users',
									scale: 'medium',
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-UPDATE'],
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
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-UPDATE'],
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
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-UPDATE'],
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
									requiredPermissions: ['ADMIN-ROLE-MANAGEMENT-DELETE'],
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

				// var loadRoleGrid = function (record, store, dataIndex, dataType, itemType, grid) {
				var loadRoleGrid = function (gridData) {

					var roleRecord = gridData.roleRecord;
					var grid = gridData.grid;
					var store = grid.getStore();
					var enabledColumnIndex = gridData.enabledColumnIndex;
					var dataType = gridData.dataType;
					var itemType = gridData.itemType;

					// Array<String> representation of all permissions
					roleRecord.data[dataType] = roleRecord.data[dataType] || [];
					var data = roleRecord.getData()[dataType].map(function (item) {
						return item[itemType];
					});

					store.each(function (item) {
						var enabled = false;
						if (data.indexOf(item.getData().code) !== -1) {
							enabled = true;
						}

						item.data.sortColumn = (item.data.permissionPredecessor ? item.data.permissionPredecessor : '') + item.data.code;

						// not using item.set() because it will put the grid in a "dirty" state
						item.data[enabledColumnIndex] = enabled;
					});

					grid.view.refresh();
					var sortableCol = grid.getColumns().reduce(function (acc, col) {
						if (col.dataIndex === 'sortColumn') {
							return col;
						}
					}, {});
					if (sortableCol) {
						sortableCol.sort('up');
					}
				};
				
				var actionManagePermissions = function(record) {
						
					var permissionWin = Ext.create('Ext.window.Window', {
						title: record.get('roleName') + ' Permissions',
						iconCls: 'fa fa-key icon-correction-key icon-button-color-key icon-small-vertical-correction',
						closeAction: 'destroy',
						maximizable: true,
						canClose: false,
						width: 1000,
						height: '80%',
						layout: {
							type: 'hbox',
							align: 'stretch'
						},
						modal: true,
						listeners: {
							beforeclose: function () {
								
								if (permissionWin.getComponent('permissionGrid').isDirty) {
									if (permissionWin.canClose) {
										return true;
									}
									else {
										Ext.Msg.show({
											title: 'Unsaved data',
											message: 'You have unsaved changes. Discard changes?',
											buttons: Ext.Msg.YESNO,
											buttonText: {
												yes: "Discard",
												no: "Cancel"
											},
											icon: Ext.Msg.WARNING,
											fn: function (btn) {
												if (btn === 'yes') {
													permissionWin.canClose = true;
													permissionWin.close();
												}
											}
										});
										return false;
									}
								}
								else {
									return true;
								}
							}
						},
						items: [
							{
								xtype: 'grid',
								sortableColumns: false,
								itemId: 'permissionGrid',
								title: 'Active Permissions for ' + record.get('roleName'),
								width: '100%',
								isDirty: false,
								bufferedRenderer: false,
								findSubPermissions: function (record) {
									return this.getStore().getData().items.filter(function (item) {
										return item.getData().permissionPredecessor === record.getData().code;
									});
								},
								findParentPermission: function (record) {
									if (record.getData().permissionPredecessor) {
										return this.getStore().getData().items.reduce(function (acc, item) {
											if (record.getData().permissionPredecessor === item.getData().code) {
												acc = item;
											}
											return acc;
										});
									}
									return null;
								},
								findRelatedPermissions: function (grouping) {

									var store = this.getStore();

									return store.getData().items.filter(function (item) {
										return item.getData().groupBy === grouping;
									});
								},
								setDirty: function (isDirty) {
									this.isDirty = isDirty;
									permissionWin.down('[itemId=dirtyLabel]').setHidden(false);
								},
								features: [
									{ 
										ftype: 'grouping',
										groupHeaderTpl: Ext.create('Ext.XTemplate',
											'<div style="height: 2.8em;">',
												'<div style="float: left; width: 70%;">{name}</div>',
												'<div style="float: right; width: 29%; text-align: right; display: block;">',
													'<button class="x-btn-default-toolbar-large" button-type="check-group" style="display: inline-block; margin: 0 10px 0 0; cursor: pointer;"><i button-type="check-group" class="fa fa-plus-circle" aria-hidden="true"></i> Add All</button>',
													'<button class="x-btn-default-toolbar-large" button-type="uncheck-group" style="display: inline-block; margin: 0 0 0 0; cursor: pointer;"><i button-type="uncheck-group" class="fa fa-minus-circle" aria-hidden="true"></i> Clear All</button>',
												'</div>',
											'</div>'
										),
										startCollapsed: true
									}
								],
								viewConfig: {
									listeners: {
										groupclick: function (view, node, group, event) {
											var targetType = event.target.getAttribute('button-type');
											var buttonIsTarget = targetType === 'check-group' || targetType === 'uncheck-group' ? true : false;
											var isCollapsed = typeof event.record.isCollapsedPlaceholder === 'undefined' ? false : true;
											var grid = permissionWin.getComponent('permissionGrid');
											var itemsToBeUpdated = grid.findRelatedPermissions(event.record.getData().groupBy);

											if (!isCollapsed && buttonIsTarget) {
												grid.setDirty(true);
												Ext.Array.forEach(itemsToBeUpdated, function (item) {
													item.set('permissionEnabled', targetType === 'check-group' ? true : false);
												});
												
												return false;
											}

											return true;
										}
									}
								},
								columns: [
									{
										xtype: 'checkcolumn',
										text: 'Enabled',
										dataIndex: 'permissionEnabled',
										flex: 1,
										listeners: {
											checkchange: function (self, rowIndex, checked, record) {

												// remove/add classes of sub-permissions to enable/disable permission rows
												var grid = this.up('grid');
												var subPermissionRecords = grid.findSubPermissions(record);
												if (checked) {
													Ext.Array.forEach(subPermissionRecords, function (item) {
														var row = grid.getView().getRowByRecord(item);
														row.classList.remove('permission-row-disabled');
													});
												}
												else {
													Ext.Array.forEach(subPermissionRecords, function (item) {
														var row = grid.getView().getRowByRecord(item);
														item.set('permissionEnabled', false);
														row.classList.add('permission-row-disabled');
													});
												}
												// set the grid to a dirty state
												grid.setDirty(true);
											}
										}
									},
									{
										text: 'Code',
										dataIndex: 'code',
										flex: 6,
										renderer: function (value, metaData) {

											// indent the record if it has a "permissionPredecessor"

											var grid = this;
											var record = grid.getStore().getData().items[metaData.recordIndex];
											
											if (record.getData().permissionPredecessor) {
												var parentRecord = grid.findParentPermission(record);
												if (parentRecord) {

													// defer so the view will have time to render.
													if (!parentRecord.getData().permissionEnabled) {
														Ext.defer(function () {
															var row = grid.getView().getRowByRecord(record);
															row.classList.add('permission-row-disabled');
														}, 1);
													}
													return '<span style="margin-left: 25px;">' + value + ' <i style="pointer-events: auto; color: #888;" class="fa fa-question-circle" data-qtip="' + 'PERMISSION REQUIRES: ' + parentRecord.getData().code +'"></i></span>';
												}
											}

											return '<span>' + value + '</span>';
										}
									},
									{ text: 'Description', dataIndex: 'description', flex: 6, minWidth: 200 },
									{ text: 'Sort Order', dataIndex: 'sortColumn', hidden: true }
								],
								store: {
									autoLoad: true,
									groupField: 'groupBy',
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/securitypermissions'
									},
									listeners: {
										load: function (store, records, opts) {
											
											var grid = permissionWin.down('[itemId=permissionGrid]');
											loadRoleGrid({
												roleRecord: record,
												grid: grid,
												enabledColumnIndex: 'permissionEnabled',
												dataType: 'permissions',
												itemType: 'permission'
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
								style: 'margin-top:20px',
								items: [
									{
										text: 'Save',
										iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
										scale: 'medium',
										height: '40px',
										handler: function(){
											
											var permissions = [];
											var grid = permissionWin.getComponent('permissionGrid');
											grid.getStore().each(function (item) {
												
												if (item.get('permissionEnabled')) {
													permissions.push({
														permission: item.get('code')
													})
												}
											});
											
											var data = record.data;
											data.permissions = permissions;
											
											permissionWin.setLoading('Saving permissions... <br> This may take a while');
											Ext.Ajax.request({
												url: 'api/v1/resource/securityroles/' + encodeURIComponent(record.get('roleName')),
												method: 'PUT',
												jsonData: data,
												timeout: 120000,
												callback: function(){
													permissionWin.setLoading(false);
												},
												success: function(response, opts) {
													Ext.toast('Updated Permissions.')
													grid.setDirty(false);
													permissionWin.close();
												}
											});
											
										}
									},
									{
										xtype: 'label',
										itemId: 'dirtyLabel',
										hidden: true,
										html: '<div style="color: #ff0033; font-weight: bold;">You have unsaved changes</div>'
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
						height: '70%',
						isDirty: false,
						canClose: false,
						setDirty: function (isDirty) {
							this.isDirty = isDirty;
							this.down('[itemId=dirtyLabel]').setHidden(false);
						},
						layout: {
							type: 'fit'
						},
						modal: true,
						listeners: {
							beforeclose: function () {

								if (dataWin.isDirty) {
									if (dataWin.canClose) {
										return true;
									}
									else {
										Ext.Msg.show({
											title: 'Unsaved data',
											message: 'You have unsaved changes. Discard changes?',
											buttons: Ext.Msg.YESNO,
											buttonText: {
												yes: "Discard",
												no: "Cancel"
											},
											icon: Ext.Msg.WARNING,
											fn: function (btn) {
												if (btn === 'yes') {
													dataWin.canClose = true;
													dataWin.close();
												}
											}
										});
										return false;
									}
								}
								else {
									return true;
								}
							}
						},
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
												itemId: 'dataSourcesGrid',
												title: 'Restricted <i class="fa fa-question-circle"  data-qtip="Drag record to remove restriction from ' + record.get('roleName') + '"></i>',
												width: '100%',
												columnLines: true,
												store: {	
													autoLoad: true,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/lookuptypes/DataSource'
													},
													listeners: {
														load: function(store, records, opts) {

															var grid = dataWin.down('[itemId=dataSourcesGrid]');
															loadRoleGrid({
																roleRecord: record,
																grid: grid,
																enabledColumnIndex: 'enabled',
																dataType: 'dataSecurity',
																itemType: 'dataSource'
															});
														}
													}
												},
												columns: [
													{
														xtype: 'checkcolumn',
														text: 'Enabled',
														dataIndex: 'enabled',
														flex: 1,
														listeners: {
															checkchange: function () {
																dataWin.setDirty(true);
															}
														}
													},
													{ text: 'Code', dataIndex: 'code', flex: 3},
													{ text: 'Description', dataIndex: 'description', flex: 6, minWidth: 200}
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
												itemId: 'dataSensitivityGrid',
												title: 'Restricted <i class="fa fa-question-circle"  data-qtip="Drag record to add to ' + record.get('roleName') + '"></i>',
												width: '100%',
												columnLines: true,
												store: {	
													autoLoad: true,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/lookuptypes/DataSensitivity'
													},
													listeners: {
														load: function(store, records, opts) {

															var grid = dataWin.down('[itemId=dataSensitivityGrid]');
															loadRoleGrid({
																roleRecord: record,
																grid: grid,
																enabledColumnIndex: 'enabled',
																dataType: 'dataSecurity',
																itemType: 'dataSensitivity'
															});
														}
													}
												},
												columns: [
													{
														xtype: 'checkcolumn',
														text: 'Enabled',
														dataIndex: 'enabled',
														flex: 1,
														listeners: {
															checkchange: function () {
																dataWin.setDirty(true);
															}
														}
													},
													{ text: 'Code', dataIndex: 'code', flex: 3 },
													{ text: 'Description', dataIndex: 'description', flex: 6, minWidth: 200 }
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

											var dataSourcesGrid = dataWin.down('[itemId=dataSourcesGrid]');
											var dataSensitivityGrid = dataWin.down('[itemId=dataSensitivityGrid]');

											dataSourcesGrid.getStore().each(function (item) {
												if (item.get('enabled')) {
													dataRestrictions.push({
														dataSource: item.get('code')
													});
												}
											});

											dataSensitivityGrid.getStore().each(function (item) {
												if (item.get('enabled')) {
													dataRestrictions.push({
														dataSensitivity: item.get('code')
													});
												}
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
													dataWin.setDirty(false);
													dataWin.close();
												}
											});											
										}
									},
									{
										xtype: 'label',
										itemId: 'dirtyLabel',
										hidden: true,
										html: '<div style="color: #ff0033; font-weight: bold;">You have unsaved changes</div>'
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
										text: 'Keep Flag',
										dataIndex: 'keep',
										align: 'center',
										renderer: CoreUtil.renderer.booleanRenderer,
										width: 100
									},
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
														url: 'api/v1/resource/userprofiles',
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

			});
			
        </script>

    </stripes:layout-component>
</stripes:layout-render>
			