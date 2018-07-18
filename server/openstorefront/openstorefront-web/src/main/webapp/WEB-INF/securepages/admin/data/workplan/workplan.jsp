<%--
/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
	<%-- 
	Document	: faq
	Created on	: July 13, 2018, 2:25:32 PM
	Author		: cyearsley
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../../layout/adminheader.jsp">
		</stripes:layout-render>

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.require('OSF.workplanManagementTool.Window');

			Ext.onReady(function () {

				var workplanWindow = Ext.create('OSF.workplanManagementTool.Window', {
					id: 'workplanWindow',
					title: 'Add/Edit Workplans',
					iconCls: 'fa fa-edit icon-button-color-edit',
					modal: true,
					width: '75%',
					height: '75%',
					maximizable: true,
				});

				var workplanGrid = Ext.create('Ext.grid.Panel', {
					id: 'workplanGrid',
					title: 'Workplan Management <i class="fa fa-question-circle" data-qtip="Add, edit, and delete workplans here" ></i>',
					// requiredPermissions: [''],
					permissionCheckFailure: function () {
						Ext.toast({
							html: 'You do not have permissions to view the data on this page',
							title: 'Invalid Permissions',
							align: 'b'
						});
					},
					columnLines: true,
					// store: {
					// 	autoLoad: true,
					// 	id: 'workplanGridStore',
					// 	remoteSort: false,
					// 	fields: [],
					// 	proxy: {
					// 		type: 'ajax',
					// 		url: 'api/v1/resource/~~~~~~',
					// 		reader: {
					// 			type: 'json',
					// 			rootProperty: 'data',
					// 			totalProperty: 'totalNumber'
					// 		}
					// 	},
					// 	listeners: {
					// 		beforeLoad: function (store, operation, eOpts) {
					// 			store.getProxy().extraParams = {
					// 				status: Ext.getCmp('filterActiveStatus').getValue()
					// 			};
					// 		}
					// 	}
					// },
					columns: [
						{ text: 'Workplan Name', dataIndex: 'name', flex: 4 },
						{ text: 'Type', dataIndex: 'type', flex: 2 },
						{ text: 'Active', dataIndex: 'active', flex: 1 },
						{ text: 'Create User', dataIndex: 'createUser', flex: 1, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', flex: 1, hidden: true, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
						{ text: 'Update User', dataIndex: 'updateUser', flex: 1, hidden: true },
						{ text: 'Update Date', dataIndex: 'updateDts', flex: 1, hidden: true, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
					],
					listeners: {
						selectionchange: function (selModel, selected, opts) {
							// var tools = Ext.getCmp('workplanGrid').getComponent('tools');

							// if (selected.length > 0) {
							// 	tools.getComponent('edit').setDisabled(false);
							// 	tools.getComponent('togglestatus').setDisabled(false);
							// 	tools.getComponent('delete').setDisabled(false);
							// } else {
							// 	tools.getComponent('edit').setDisabled(true);
							// 	tools.getComponent('togglestatus').setDisabled(true);
							// 	tools.getComponent('delete').setDisabled(true);
							// }
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
									scale: 'medium',
									id: 'faqRefreshBtn',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator',
									// requiredPermissions: ['']
								},
								{
									text: 'Add Workplan',
									itemId: 'add',
									id: 'faqMngAddBtn',
									scale: 'medium',
									width: '175px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									// requiredPermissions: [''],
									handler: function () {
										actionAdd();
									}
								},
								{
									text: 'Edit',
									id: 'faqMngEditBtn',
									itemId: 'edit',
									scale: 'medium',
									width: '100px',
									disabled: true,
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									// requiredPermissions: [''],
									handler: function () {
										actionEdit(Ext.getCmp('workplanGrid').getSelectionModel().getSelection()[0]);
									}
								},
								{
									xtype: 'tbseparator',
									// requiredPermissions: [''],
								},
								{
									text: 'Set Active',
									itemId: 'setactive',
									// autoEl: {
									// 	'data-test': 'toggleStatusFAQBtn'
									// },
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									// requiredPermissions: [''],
									handler: function () {
										actionToggleStatus(Ext.getCmp('workplanGrid').getSelectionModel().getSelection()[0]);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									// autoEl: {
									// 	'data-test': 'faqsDeleteBtn'
									// },
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									// requiredPermissions: [''],
									handler: function () {
										actionDelete(Ext.getCmp('workplanGrid').getSelectionModel().getSelection()[0]);
									}
								}
							]
						}
					]
				});


				var actionRefresh = function () {
					// Ext.getCmp('workplanGrid').getStore().load({
					// 	url: 'api/v1/resource/faq'
					// });
				};

				var actionAdd = function (record) {

					record = record || null;
					workplanWindow
						.loadWorkplan(record)
						.show();
				};

				var actionEdit = function (record) {
					// addEditWindow.show();
					// addEditWindow.getComponent('faqForm').reset();
					// addEditWindow.getComponent('faqForm').loadRecord(record);
				};

				var actionToggleStatus = function (record) {

					// record.set('activeStatus', record.get('activeStatus') === 'A' ? 'I' : 'A');
					// record.data.type = undefined;

					// workplanGrid.setLoading('Updating Status...');
					// Ext.Ajax.request({
					// 	url: 'api/v1/resource/faq/' + record.get('faqId') + '?status=all',
					// 	jsonData: record.data,
					// 	method: 'PUT',
					// 	callback: function () {
					// 		workplanGrid.setLoading(false);
					// 	},
					// 	success: function (response, opts) {
					// 		actionRefresh();
					// 	}
					// });
				};

				var actionDelete = function (record) {
					// workplanGrid.setLoading('Deleting FAQ...');
					// Ext.Ajax.request({
					// 	url: 'api/v1/resource/faq/' + record.get('faqId'),
					// 	method: 'DELETE',
					// 	callback: function () {
					// 		workplanGrid.setLoading(false);
					// 	},
					// 	success: function (response, opts) {
					// 		actionRefresh();
					// 	}
					// });
				};

				addComponentToMainViewPort(workplanGrid);

			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>