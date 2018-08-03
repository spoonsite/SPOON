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
	Document	: workplan
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
			Ext.require('OSF.workplanManagementTool.WorkPlanMigrationWindow');

			Ext.onReady(function () {

				var workplanWindow = Ext.create('OSF.workplanManagementTool.Window', {
					id: 'workplanWindow',
					title: 'Add/Edit Workplans',
					iconCls: 'fa fa-edit icon-button-color-edit',
					modal: true,
					width: '75%',
					height: 840,
					maximizable: true,
				});

				var workplanGrid = Ext.create('Ext.grid.Panel', {
					id: 'workplanGrid',
					title: 'Workplan Management <i class="fa fa-question-circle" data-qtip="Add, edit, and delete workplans here" ></i>',
					requiredPermissions: ['ADMIN-WORKPLAN-READ'],
					permissionCheckFailure: function () {
						Ext.toast({
							html: 'You do not have permissions to view the data on this page',
							title: 'Invalid Permissions',
							align: 'b'
						});
					},
					columnLines: true,
					store: {
						autoLoad: true,
						id: 'workplanGridStore',
						fields: ['name', 'workPlanType', 'activeStatus'],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/workplans'
						}
					},
					columns: [
						{
							text: 'Workplan Name',
							dataIndex: 'name',
							flex: 4,
							renderer: function (value, meta, record) {

								return record.getData().defaultWorkPlan ? (value + ' <b>(Default Work Plan)</b>') : value;
							}
						},
						{ text: 'Type', dataIndex: 'workPlanType', flex: 2 },
						{ text: 'Apply to Sub Entry Types', dataIndex: 'appliesToChildComponentTypes', width: 200 },
						{
							text: 'Entry Types',
							dataIndex: 'componentTypes',
							flex: 10,
							renderer: function (value) {
								return value.map(function (item) {
									return item.componentType;
								}).join(', ');
							}
						},
						{ text: 'Active', dataIndex: 'activeStatus', flex: 1 },
						{ text: 'Create User', dataIndex: 'createUser', flex: 1, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', flex: 1, hidden: true, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
						{ text: 'Update User', dataIndex: 'updateUser', flex: 1, hidden: true },
						{ text: 'Update Date', dataIndex: 'updateDts', flex: 1, hidden: true, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
					],
					listeners: {
						selectionchange: function (selModel, selected, opts) {
							var tools = Ext.getCmp('workplanGrid').getComponent('tools');
							
							if (selected.length > 0) {
								
								tools.getComponent('edit').setDisabled(false);
								tools.getComponent('setactive').setDisabled(false);
								tools.getComponent('delete').setDisabled(false);

								if (selected[0].getData().defaultWorkPlan) {

									tools.getComponent('delete').setDisabled(true);
									tools.getComponent('setactive').setDisabled(true);
								}
								
								if (selected[0].getData().activeStatus === 'A') {
									tools.getComponent('setactive').setDisabled(true);
								}

							} else {

								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('setactive').setDisabled(true);
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
									scale: 'medium',
									id: 'faqRefreshBtn',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-WORKPLAN-CREATE', 'ADMIN-WORKPLAN-UPDATE']
								},
								{
									text: 'Add Workplan',
									itemId: 'add',
									id: 'faqMngAddBtn',
									scale: 'medium',
									width: '175px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									requiredPermissions: ['ADMIN-WORKPLAN-CREATE'],
									handler: function () {
										actionAddEdit();
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
									requiredPermissions: ['ADMIN-WORKPLAN-UPDATE'],
									handler: function () {
										actionAddEdit(Ext.getCmp('workplanGrid').getSelectionModel().getSelection()[0].getData());
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-WORKPLAN-UPDATE'],
								},
								{
									text: 'Set Active',
									itemId: 'setactive',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									requiredPermissions: ['ADMIN-WORKPLAN-UPDATE'],
									handler: function () {

										var workPlanGrid = Ext.getCmp('workplanGrid');
										var selectedRecord = Ext.getCmp('workplanGrid').getSelectionModel().getSelection()[0].getData();
										var hasConflictingEntryTypes = false;

										// filter out default workplan, self, and all inactive records
										workPlanGrid.getStore().getData().items.filter(function (item) {
											return item.getData().workPlanId !== selectedRecord.workPlanId && !item.getData().defaultWorkPlan && item.getData().activeStatus !== 'I';
										}).forEach(function (item) {

											// compare componentType strings, to see if there are any shared component types
											selectedRecord.componentTypes.map(function (obj) {
												return obj.componentType;
											}).forEach(function (ct) {
												hasConflictingEntryTypes = item.getData().componentTypes.map(function (obj) {
													return obj.componentType;
												}).indexOf(ct) !== -1 ? true : hasConflictingEntryTypes;
											})
										});
										

										if (hasConflictingEntryTypes) {
											Ext.Msg.show({
												title: 'Work Plan Conflict!',
												message: 'The work plan you are trying to activate contains entry types that are allocated<br />' +
															'to another work plan. Activating this work plan will <b>inactivate the conflicting<br />' +
															'work plan. Are you sure you want to active this work plan?</b>',
												buttons: Ext.Msg.YESNO,
												buttonText: {
													yes: "Activate",
													no: "Cancel"
												},
												icon: Ext.Msg.WARNING,
												fn: function (btn) {
													if (btn === 'yes') {
														actionToggleStatus(selectedRecord);
													}
												}
											});
										}
										else {
											actionToggleStatus(selectedRecord);
										}
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									requiredPermissions: ['ADMIN-WORKPLAN-DELETE'],
									handler: function () {
										actionDelete(Ext.getCmp('workplanGrid').getSelectionModel().getSelection()[0]);
									}
								}
							]
						}
					]
				});


				var actionRefresh = function () {
					Ext.getCmp('workplanGrid').getStore().load();
				};

				var actionAddEdit = function (record) {

					record = record || null;
					workplanWindow
						.loadWorkplan(record)
						.show();
				};

				var actionToggleStatus = function (record) {

					Ext.Ajax.request({
						method: 'PUT',
						url: 'api/v1/resource/workplans/' + record.workPlanId + '/activate',
						success: function (res) {
							Ext.getCmp('workplanGrid').getStore().load();
						}
					});
				};

				var actionDelete = function (record) {
					
					Ext.create({
						xtype: 'osf.wp.workPlanMigrationWindow',
						workplanGrid: workplanGrid
					}).show();
				};

				addComponentToMainViewPort(workplanGrid);

			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>