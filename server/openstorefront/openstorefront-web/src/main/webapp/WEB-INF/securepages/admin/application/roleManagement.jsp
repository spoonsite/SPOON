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
						height: 400,
						items: [
							{
								xtype: 'form',
								itemId: 'form',
								scrollable: true,
								bodyStyle: 'padding: 20px;',
								layout: 'anchor',
								defaults: {
									width: '100%',
									labelAlign: 'top'
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
					
				};
				
				var actionManagePermissions = function(record) {
					
				};				
				
				var actionManageData = function(record) {
					
					
				};				
				
				var actionManageUsers = function(record) {
					
				};					
				
			});
			
        </script>

    </stripes:layout-component>
</stripes:layout-render>
			