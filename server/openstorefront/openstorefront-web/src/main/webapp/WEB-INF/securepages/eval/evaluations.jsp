<%--
/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

    Document   : evaluation
    Created on : Mar 20, 2017, 2:12:17 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../layout/${actionBean.headerPage}">
		</stripes:layout-render>

		<link rel="stylesheet" href="css/evaluations.css">

		<script src="scripts/component/evaluationForm.js?v=${appVersion}" type="text/javascript"></script>

        <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				var evalGridStore = Ext.create('Ext.data.Store', {
					pageSize: 100,
					autoLoad: false,
					remoteSort: true,
					fields: [
						{
							name: 'createDts',
							type: 'date',
							dateFormat: 'c'
						},
						{
							name: 'updateDts',
							type: 'date',
							dateFormat: 'c'
						}
					],
					proxy: CoreUtil.pagingProxy({
						type: 'ajax',
						url: 'api/v1/resource/evaluations',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								'assignedUser': Ext.getCmp('filterAssignedUser').getValue(),
								'assignedGroup': Ext.getCmp('filterAssignedGroup').getValue(),
								'workflowStatus': Ext.getCmp('filterWorkflowStatus').getValue()
							};
						}
					}
				});

				var evaluationGrid = Ext.create('Ext.grid.Panel', {
					id: 'evaluationGrid',
					title: 'Evaluation &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="Allows editing evaluations for entries" ></i>',
					columnLines: true,
					store: evalGridStore,
					columns: [
						{ text: 'Entry Name', dataIndex: 'componentName', width: 250},
						{ text: 'Version', dataIndex: 'version', align: 'center', flex: 1 },
						{ text: 'Published', dataIndex: 'published', align: 'center', flex: 1,
							renderer: CoreUtil.renderer.booleanRenderer
						},
						{ text: 'Assigned Group', dataIndex: 'assignedGroup', align: 'center', flex: 1 },
						{ text: 'Assigned User', dataIndex: 'assignedUser', align: 'center', flex: 1},
						{ text: 'Status', dataIndex: 'workflowStatus', align: 'center', flex: 1,
							renderer: function(value, meta, record) {
								if (value === 'INPROGRESS') {
									meta.tdCls = 'alert-warning';
								} else if (value === 'WAIT') {
									meta.tdCls = 'alert-info';
								} else if (value === 'COMPLETE') {
									meta.tdCls = 'alert-success';
								} else if (value === 'HOLD') {
									meta.tdCls = 'alert-danager';
								}

								return record.get('workflowStatusDescription');
							}
						},
						{ text: 'Create User', dataIndex: 'createUser', flex: 1, hidden: true  },
						{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  flex: 1 },
						{ text: 'Update User', dataIndex: 'updateUser', flex: 1 }
					],
					listeners: {
						selectionchange: function(selModel, selected, opts) {
							var tools = evaluationGrid.getComponent('tools');
							var evalGrid = Ext.getCmp('evaluationGrid');

							if (evalGrid.getSelectionModel().getCount() === 1) {
								Ext.getCmp('lookupGrid-tools-preview').setDisabled(false);
								tools.getComponent('edit').setDisabled(false);
							} else {
								Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
							}

							if(selected.length > 0){
								if(!selected[0].data.published){
									tools.getComponent('assignUser').setDisabled(false);
									tools.getComponent('edit').setText('Edit');
									tools.getComponent('edit').setIconCls('fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit');
								}
								else {
									tools.getComponent('assignUser').setDisabled(true);
									tools.getComponent('edit').setText('Details');
									tools.getComponent('edit').setIconCls('fa fa-2x fa-tasks icon-button-color-edit icon-vertical-correction-edit');
								}
							}
							else{
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('assignUser').setDisabled(true);
							}
						}
					},
					dockedItems: [,
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: evalGridStore,
							displayInfo: true
						},
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'UserSingleSelectComboBox',
									id: 'filterAssignedUser',
									name: 'assignedUser',
									fieldLabel: 'Assigned User',
									addAll: true,
									width: 250,
									emptyText: 'All',
									typeAhead: true,
									editable: true,
									forceSelection: true,
									anyMatch: true,
									store: {
										autoLoad: true,
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/userprofiles/lookup'
										}
									},
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefresh();
										}
									}
								},
								{
									xtype: 'combobox',
									id: 'filterAssignedGroup',
									name: 'assignedGroup',
									fieldLabel: 'Assign to Group',
									displayField: 'description',
									valueField: 'code',
									emptyText: 'All',
									labelAlign: 'top',
									width: 250,
									editable: false,
									forceSelection: true,
									store: {
										autoLoad: true,
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/securityroles/lookup'
										},
										listeners: {
											load: function(store, records, opts) {
												store.add({
													code: null,
													description: 'All'
												});
											}
										}
									},
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefresh();
										}
									}
								},
								Ext.create('OSF.component.StandardComboBox', {
									id: 'filterWorkflowStatus',
									name: 'workflowStatus',
									margin: '0 0 5 0',
									matchFieldWidth: false,
									editable: false,
									typeAhead: false,
									emptyText: 'All',
									minWidth: 200,
									fieldLabel: 'Workflow Status',
									storeConfig: {
										url: 'api/v1/resource/lookuptypes/WorkflowStatus',
										addRecords: [
											{
												code: null,
												description: 'All'
											}
										]
									},
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefresh();
										}
									}
								})
							]
						},
						{
							xtype: 'toolbar',
							itemId: 'tools',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh',
									scale: 'medium',
									handler: function(){
										actionRefresh();
									}
								},
								{
									text: 'View',
									id: 'lookupGrid-tools-preview',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									disabled: true,
									requiredPermissions: ['USER-EVALUATIONS-READ'],
									handler: function () {
										var selection = Ext.getCmp('evaluationGrid').getSelection()[0];
										actionPreviewComponent(selection.get('componentId'), selection.data.evaluationId);
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['USER-EVALUATIONS-UPDATE']
								},
								{
									text: 'Edit',
									itemId: 'edit',
									disabled: true,
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit',
									scale: 'medium',
									width: '100px',
									requiredPermissions: ['USER-EVALUATIONS-UPDATE'],
									handler: function(){
										var record = evaluationGrid.getSelection()[0];
										actionEdit(record);
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['USER-EVALUATIONS-ASSIGN-USER']
								},
								{
									text: 'Assign User',
									itemId: 'assignUser',
									disabled: true,
									iconCls: 'fa fa-2x fa-user icon-button-color-default',
									scale: 'medium',
									requiredPermissions: ['USER-EVALUATIONS-ASSIGN-USER'],
									handler: function(){
										var record = evaluationGrid.getSelection()[0];
										actionAssignUser(record);
									}
								}
							]
						}
					]
				});

				addComponentToMainViewPort(evaluationGrid);

				var actionRefresh = function() {
					evaluationGrid.getStore().load();
				};

				var actionEdit = function(record) {
					evaluationGrid.setLoading('Checking evaluation entry...');
					Ext.Ajax.request({
						url: 'api/v1/resource/evaluations/' + record.get('evaluationId') + '/checkentry',
						method: 'PUT',
						callback: function(){
							evaluationGrid.setLoading(false);
						},
						success: function(response, opts) {
							var evalformWin = Ext.create('OSF.component.EvaluationFormWindow', {
								title: 'Evaluation Form - ' + record.get('componentName'),
								isPublishedEvaluation: record.data.published
							});
							evalformWin.show();

							var evaluation = Ext.decode(response.responseText);
							evalformWin.loadEval(record, function(){
								actionRefresh();
							});

							if (evaluation.componentId !== record.get('componentId')) {
								actionRefresh();
							}
						}
					});
				};

				var actionAssignUser = function(record) {
					var assignWin = Ext.create('Ext.window.Window', {
						title: 'Assign User',
						iconCls: 'fa fa-user',
						closeAction: 'destroy',
						modal: true,
						width: 400,
						height: 200,
						layout: 'fit',
						items: [
							{
								xtype: 'form',
								itemId: 'form',
								bodyStyle: 'padding: 10px;',
								layout: 'anchor',
								items: [
									{
										xtype: 'combobox',
										name: 'assignedUser',
										fieldLabel: 'Assign to User',
										displayField: 'description',
										valueField: 'code',
										queryMode: 'remote',
										emptyText: 'Unassigned',
										labelAlign: 'top',
										width: '100%',
										typeAhead: true,
										editable: true,
										forceSelection: true,
										hideTrigger: true,
										store: {
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/userprofiles/lookup'
											},
											listeners: {
												load: function(store, records, opts) {
													store.add({
														code: null,
														description: 'Unassigned'
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
												text: 'Save',
												formBind: true,
												iconCls: 'fa fa-lg fa-save icon-button-color-save',
												handler: function() {
													var form = this.up('form');
													var formData = form.getValues();

													var data = record.data;

													data = Ext.apply(data, formData);

													CoreUtil.submitForm({
														url: 'api/v1/resource/evaluations/' + data.evaluationId + '/assignuser/' + data.assignedUser,
														method: 'PUT',
														form: form,
														data: data,
														success: function(action, opts) {
															actionRefresh();
															assignWin.close();
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
												handler: function(){
													assignWin.close();
												}
											}
										]
									}
								]
							}
						]
					});
					assignWin.show();

					assignWin.queryById('form').loadRecord(record);

				};

				CoreService.userservice.getCurrentUser().then(function(user){
					Ext.getCmp('filterAssignedUser').setValue(user.username);
					//actionRefresh();
				});

				var previewContents = Ext.create('OSF.ux.IFrame', {
					src: ''
				});

				var previewComponentWin = Ext.create('Ext.window.Window', {
					width: '70%',
					height: '80%',
					maximizable: true,
					title: 'Preview',
					iconCls: 'fa fa-lg fa-eye',
					modal: true,
					layout: 'fit',
					items: [
						previewContents
					],
					tools: [
						{
							type: 'up',
							tooltip: 'popout preview',
							handler: function(){
								var changeRequestComponentId = Ext.getCmp('evaluationGrid').getSelection()[0].get('componentId');
								var evalId = Ext.getCmp('evaluationGrid').getSelection()[0].get('evaluationId');
								window.open('view.jsp?fullPage=true&id=' + changeRequestComponentId + '&evalId=' + evalId, "Preview");
							}
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Previous',
									id: 'previewWinTools-previousBtn',
									iconCls: 'fa fa-lg fa-arrow-left icon-button-color-default',
									handler: function() {
										actionPreviewNextRecord(false);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
									handler: function() {
										this.up('window').hide();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Next',
									id: 'previewWinTools-nextBtn',
									iconCls: 'fa fa-lg fa-arrow-right icon-button-color-default',
									iconAlign: 'right',
									handler: function() {
										actionPreviewNextRecord(true);
									}
								}
							]
						}
					]
				});

				var actionPreviewNextRecord = function(next) {
					if (next) {
						Ext.getCmp('evaluationGrid').getSelectionModel().selectNext();
					} else {
						Ext.getCmp('evaluationGrid').getSelectionModel().selectPrevious();
					}
					var selection = Ext.getCmp('evaluationGrid').getSelection()[0];
					actionPreviewComponent(selection.get('componentId'), selection.data.evaluationId);
				};

				var actionPreviewComponent = function(componentId, evalId){
					previewComponentWin.show();
					previewContents.load('view.jsp?fullPage=true&embedded=true&hideSecurityBanner=true&id=' + componentId + '&evalId=' + evalId);
				};

			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
