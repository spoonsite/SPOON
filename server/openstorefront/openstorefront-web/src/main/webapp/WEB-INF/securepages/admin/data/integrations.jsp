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
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<stripes:layout-render name="../../../../layout/adminheader.jsp">		
	</stripes:layout-render>
		
	<script src="scripts/component/integrationConfigWindow.js?v=${appVersion}" type="text/javascript"></script>
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	

			var integrationWindow = Ext.create('OSF.component.IntegrationWindow', {	
			});		

			var componentConfigStore = Ext.create('Ext.data.Store', {
				id: 'componentConfigStore',
				autoLoad: true,
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/components/integration?status=ALL'
				}
			});

			var componentConfigGrid = Ext.create('Ext.grid.Panel', {
				title: 'Component Configuration',
				id: 'componentConfigGrid',
				store: componentConfigStore,
				selModel: 'checkboxmodel',
				columnLines: true,
				listeners: {
					selectionchange: function (grid, record, index, opts) {
						if (Ext.getCmp('componentConfigGrid').getSelectionModel().getCount() === 1) {
							Ext.getCmp('componentConfigGrid-tools-run').setText('Run Job');
							Ext.getCmp('componentConfigGrid-tools-run').enable();
							Ext.getCmp('componentConfigGrid-tools-edit').enable();
							Ext.getCmp('componentConfigGrid-tools-toggleActivation').enable();
							Ext.getCmp('componentConfigGrid-tools-delete').enable();
						} 
						else if (Ext.getCmp('componentConfigGrid').getSelectionModel().getCount() > 1) {
							Ext.getCmp('componentConfigGrid-tools-toggleActivation').enable();
							Ext.getCmp('componentConfigGrid-tools-run').setText('Run Jobs');
							Ext.getCmp('componentConfigGrid-tools-run').enable();
							Ext.getCmp('componentConfigGrid-tools-edit').disable();
							Ext.getCmp('componentConfigGrid-tools-delete').enable();
						}
						else {
							Ext.getCmp('componentConfigGrid-tools-run').setText('Run Job');
							Ext.getCmp('componentConfigGrid-tools-run').disable();
							Ext.getCmp('componentConfigGrid-tools-edit').disable();
							Ext.getCmp('componentConfigGrid-tools-toggleActivation').disable();
							Ext.getCmp('componentConfigGrid-tools-delete').disable();
						}
					}
				},
				columns: [
					{ text: 'Component', dataIndex: 'componentName', flex: 2},
					{ 
						text: 'Start Time', 
						dataIndex: 'lastStartTime', 
						xtype: 'datecolumn',
						format: 'm/d/y H:i:s A',
						flex: 1
					},
					{ 
						text: 'End Time', 
						dataIndex: 'lastEndTime',
						xtype: 'datecolumn',
						format: 'm/d/y H:i:s A',
						flex: 1
					},
					{ 
						text: 'Status', 
						dataIndex: 'status', 
						flex: 1,
						renderer: function(value, metadata) {
							if (value === 'C') return 'Complete';
							else if (value === 'E') return 'Error';
							else if (value === 'P') return 'Pending';
							else if (value === 'W') return 'Working';
						}
					},
					{ 
						text: 'Active Status', 
						dataIndex: 'activeStatus', 
						flex: 0.5,
						renderer: function(value, metadata) {
							if (value === 'A') {
								metadata.tdCls = 'alert-success';
								return "Active";
							}
							if (value === 'I') {
								metadata.tdCls = 'alert-warning';
								return "Inactive";
							}
						}
					}

				],
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'top',
						items: [
							{
								text: 'Refresh',
								id: 'componentConfigGrid-tools-refresh',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
								handler: function () {
									componentConfigStore.load();
									componentConfigGrid.getSelectionModel().deselectAll();
								}
							},
							{
								requiredPermissions: ['ADMIN-INTEGRATION-CREATE'],
								xtype: 'tbseparator'
							},
							{
								text: 'Add New Configuration',
								id: 'componentConfigGrid-tools-add',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
								requiredPermissions: ['ADMIN-INTEGRATION-CREATE'],
								handler: function () {
									actionAddNewConfiguration();
								}
							},
							{
								text: 'Edit',
								id: 'componentConfigGrid-tools-edit',
								scale: 'medium',
								width: '100px',
								iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
								disabled: true,
								requiredPermissions: ['ADMIN-INTEGRATION-UPDATE'],
								handler: function () {
									var record = componentConfigGrid.getSelection()[0];
									actionEditIntegration(record);
								}
							},
							{
								requiredPermissions: ['ADMIN-INTEGRATION-RUNINTEGRATION'],
								xtype: 'tbseparator'
							},
							{
								text: 'Run Job',
								id: 'componentConfigGrid-tools-run',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-bolt icon-button-color-run icon-vertical-correction',
								disabled: true,
								requiredPermissions: ['ADMIN-INTEGRATION-RUNINTEGRATION'],
								handler: function () {

									actionRunJob();
								}
							},
							{
								text: 'Toggle Status',
								id: 'componentConfigGrid-tools-toggleActivation',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
								disabled: true,
								requiredPermissions: ['ADMIN-INTEGRATION-TOGGLESTATUS'],
								handler: function () {
									if (Ext.getCmp('componentConfigGrid').getSelectionModel().getCount() === 1) {
										var record = componentConfigGrid.getSelection()[0];
										actionToggleIntegration(record);
									} else {
										var records = componentConfigGrid.getSelection();
										actionToggleIntegrations(records);
									}
									
								}
							},
							{
								requiredPermissions: ['ADMIN-INTEGRATION-RUNALL'],
								xtype: 'tbfill'
							},
							{
								text: 'Run All Jobs',
								id: 'componentConfigGrid-tools-runAll',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-bolt icon-button-color-run icon-vertical-correction',
								tooltip: 'Run all jobs with an active component configuration.',
								requiredPermissions: ['ADMIN-INTEGRATION-RUNALL'],
								handler: function () {
									actionRunAllJobs();
								}
							},
							{
								requiredPermissions: ['ADMIN-INTEGRATION-DELETE'],
								xtype: 'tbseparator'
							},
							{
								text: 'Delete',
								id: 'componentConfigGrid-tools-delete',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-trash icon-vertical-correction icon-button-color-warning icon-vertical-correction',
								disabled: true,
								requiredPermissions: ['ADMIN-INTEGRATION-DELETE'],
								handler: function () {
									var ending = componentConfigGrid.getSelection().length > 1 ? "s" : "";
									var msg = 'Are you sure you want to delete ' + componentConfigGrid.getSelection().length + ' configuration' + ending + '?';
									Ext.Msg.show({
										title: 'Delete Configuration?',
										iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
										message: msg,
										closeAction: 'destroy',
										buttons: Ext.Msg.YESNO,
										icon: Ext.Msg.QUESTION,
										fn: function(btn) {
												if (btn === 'yes') {
													actionDeleteIntegration();
											} 
										}
									});
								}
							}							
						]
					}
				]
			});

			var entryPickWindow = Ext.create('Ext.window.Window' , {
				id: 'entryPickWindow',
				title: 'Add New Configuration',
				iconCls: 'fa fa-lg fa-plus icon-small-vertical-correction',
				modal: true,
				width: '40%',
				y: '10em',
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						id: 'entryPickForm',
						style: 'padding: 20px;',
						width: '100%',
						items: [
							{
								xtype: 'combobox',
								fieldLabel: 'Choose an entry for integration configuration',
								labelAlign: 'top',
								displayField: 'description',
								valueField: 'code',
								width: '100%',
								emptyText: 'Select an entry',
								store: Ext.create('Ext.data.Store', {
									id: 'entryPickStore',
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/components/lookup?'
									},
									sorters: [{
										property: 'description',
										direction: 'ASC'
									}]
								}),
								listeners: {
									select: function(combo, record, eOpts) {
										integrationWindow.show();
										integrationWindow.loadConfigs(record.getData().code);
										integrationWindow.setTitle('Integration: ' + record.getData().description);
										entryPickWindow.hide();
									}
								}
							}

						]
					}
				]
			});

			var actionAddNewConfiguration = function actionAddNewConfiguration() {
				entryPickWindow.show();
				Ext.getCmp('entryPickForm').reset();
			};

			var actionRunJob = function actionRunJob() {
				
				var selection = componentConfigGrid.getSelection();
				
				for (var i = 0; i < selection.length; i++) {
					
					var record = selection[i];
				
					var componentId = record.getData().componentId;
					var componentName = record.getData().componentName;
					var url = 'api/v1/resource/components/';
					url += componentId + '/integration/run';
					var method = 'POST';
					
					var data = {
						
						componentName: componentName
					};

					Ext.Ajax.request({
						url: url,
						method: method,
						requestJson: data,
						success: function (response, opts) {
							console.log(response);
							if (response.status === 304) {
								var message = 'Unable to run job for "' + response.request.requestJson.componentName + '".  Job is inactive or already running.';
							}
							else {
								var message = 'Successfully started job for "' + response.request.requestJson.componentName + '"';
							}
							Ext.toast(message, '', 'tr');
							Ext.getCmp('componentConfigGrid').getStore().load();
							Ext.getCmp('componentConfigGrid').getSelectionModel().deselectAll();
						},
						failure: function (response, opts) {
							Ext.MessageBox.alert('Failed to start job',
												 'Error: Could not start job for "' + response.request.requestJson.componentName + '"');
						}
					});
				}

			};

			var actionRunAllJobs = function actionRunAlljobs() {

				Ext.Ajax.request({
					url: 'api/v1/resource/components/integrations/run',
					method: 'POST',
					success: function (response, opts) {
						Ext.toast('Sent request to run all jobs', '', 'tr');
						Ext.getCmp('componentConfigGrid').getStore().load();
						Ext.getCmp('componentConfigGrid').getSelectionModel().deselectAll();
					},
					failure: function (response, opts) {
						Ext.MessageBox.alert('Failed', 'Could not complete request to run all jobs.');
					}
				});


			};

			var actionEditIntegration = function actionEditIntegration(record) {
				integrationWindow.show();
				integrationWindow.loadConfigs(record.getData().componentId);
				integrationWindow.setTitle('Edit Integration: ' + record.getData().componentName);
			};

			var actionToggleIntegration = function actionToggleIntegration(record) {
				var componentId = record.getData().componentId;
				var componentName = record.getData().componentName;
				var activeStatus = record.getData().activeStatus;
				var url = 'api/v1/resource/components/';
				url += componentId + '/integration/';
				var method = 'PUT';
				if (activeStatus === 'A') {
					var what = 'deactivate';
					url += 'inactivate';
				}
				else {
					var what = 'activate';
					url += 'activate';
				}

				Ext.Ajax.request({
					url: url,
					method: method,
					success: function (response, opts) {
						var message = 'Successfully ' + what + 'd integration for "' + componentName + '"';
						Ext.toast(message, '', 'tr');
						Ext.getCmp('componentConfigGrid').getStore().load();
						Ext.getCmp('componentConfigGrid').getSelectionModel().deselectAll();
					},
					failure: function (response, opts) {
						Ext.MessageBox.alert('Failed to' + what,
											 "Error: Could not " + what + ' integration for "' + componentName + '"');
					}
				});
			};

			var actionToggleIntegrations = function actionToggleIntegrations(records) {
				var url = 'api/v1/resource/components/integration/togglemultiple';
				var method = 'PUT';
				
				var data = [];

				Ext.Array.each(records, function(record) {
					data.push(record.get('componentId'));
				});


				Ext.Ajax.request({
					url: url,
					method: method,
					jsonData: data,
					success: function (response, opts) {
						var message = 'Successfully toggled statuses';
						Ext.toast(message, '', 'tr');
						Ext.getCmp('componentConfigGrid').getStore().load();
						Ext.getCmp('componentConfigGrid').getSelectionModel().deselectAll();
					},
					failure: function (response, opts) {
						Ext.MessageBox.alert('Failed to toggle', "Failed to toggle statuses.");
					}
				});
			};


			var actionDeleteIntegration = function actionDeleteIntegration() {
				
				var selection = componentConfigGrid.getSelection();
				
				for (var i = 0; i < selection.length; i++) {
					
					var record = selection[i];
				
					var componentId = record.getData().componentId;
					var componentName = record.getData().componentName;
					var url = 'api/v1/resource/components/';
					url += componentId + '/integration';
					var method = 'DELETE';
					
					var data = {
						
						componentName: componentName
					};

					Ext.Ajax.request({
						url: url,
						method: method,
						requestJson: data,
						success: function (response, opts) {
							if (response.status === 304) {
								var message = 'Unable to delete integration for "' + response.request.requestJson.componentName + '". Integration is currently running.';
							}
							else {
								var message = 'Successfully deleted integration for "' + response.request.requestJson.componentName + '"';
							}
							Ext.toast(message, '', 'tr');
							Ext.getCmp('componentConfigGrid').getStore().load();
							Ext.getCmp('componentConfigGrid').getSelectionModel().deselectAll();
						},
						failure: function (response, opts) {
							Ext.MessageBox.alert('Failed to delete',
												 'Error: Could not delete integration for "' + response.request.requestJson.componentName + '"');
						}
					});
				}
			};


			var jiraConfigStore = Ext.create('Ext.data.Store', {
				id: 'jiraConfigStore',
				autoLoad: true,
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/attributes/attributexreftypes/detail'
				}
			});

			var addEditMappingWin = Ext.create('Ext.window.Window', {
				id: 'addEditMappingWin',
				iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
				title: 'Add/Edit Mapping',
				modal: true,
				width: '60%',
				height: '90%',
				y: '5em',
				layout: 'fit',
				closable: false,
				items: [
					{
						xtype: 'form',
						id: 'addEditMappingForm',
						width: '100%',
						padding: '10px',
						scrollable: true,
						defaults: {
							width: '100%'
						},
						items: [
							{
								xtype: 'combobox',
								id: 'jiraProjectSelection',
								name: 'projectType',
								fieldLabel: 'Select a Jira Project:',
								displayField: 'description',
								valueField: 'code',
								store: Ext.create('Ext.data.Store', {
									id: 'jiraProjectStore',
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/service/jira/projects'
									}
								}),
								listeners: {
									select: function (combo, record, eOpts) {
										Ext.getCmp('jiraFieldSelection').select(null);
										Ext.getCmp('jiraProjectIssueSelection').select(null);
										Ext.getCmp('jiraAssignmentInstructions').hide();
										Ext.getCmp('jiraAssignmentPanel').hide();
										var code = record.getData().code;
										var url = 'api/v1/service/jira/projects/';
										url += code;

										Ext.getCmp('jiraProjectIssueSelection').setStore({
											autoLoad: true,
											sorters: [
												{
													property: 'name',
													direction: 'ASC'
												}
											],
											proxy: {
												type: 'ajax',
												url: url												
											}
										});
									}
								}
							},
							{
								xtype: 'combobox',
								id: 'jiraProjectIssueSelection',
								name: 'issueType',
								fieldLabel: 'Select a Jira Project Issue Type:',
								displayField: 'name',
								valueField: 'name',
								editable: false,
								listeners: {
									select: function (combo, record, eOpts) {
										Ext.getCmp('jiraFieldSelection').select(null);
										Ext.getCmp('jiraAssignmentInstructions').hide();
										Ext.getCmp('jiraAssignmentPanel').hide();
										var projectSelection = Ext.getCmp('jiraProjectSelection').getSelection();
										var projectCode = projectSelection.getData().code;
										var issueType = combo.getValue();
										var url = 'api/v1/service/jira/projects/';
										url += projectCode + '/' + issueType + '/fields';

										Ext.getCmp('jiraFieldSelection').setStore({
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: url
											}
										});
									}
								}
							},
							{
								xtype: 'combobox',
								fieldLabel: 'Select a Storefront Attribute Type:',
								id: 'attributeTypeSelection',
								name: 'attributeType',
								valueField: 'attributeType',
								displayField: 'description',
								store: Ext.create('Ext.data.Store', {
									id: 'attributeTypeStore',
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/attributes'
									}
								}),
								listeners: {
									select: function (combo, record, eOpts) {
										var codes = record.getData().codes;
										var form = Ext.getCmp('fieldAssignmentForm');

										// Clear form upon new selection
										form.removeAll();

										// Create assignment fields for storefront codes
										Ext.Array.each(codes, function(code) {
											form.add({
												xtype: 'textfield',
												fieldLabel: code.label,
												name: code.code,
												id: code.code,
												anchor: '100%',
												listeners: {
													afterrender: function(thisField, eOpts) {
														initializeDropTarget(thisField); 
													}
												}
											});
										});

										if (Ext.getCmp('jiraFieldSelection').getSelection() != undefined) {
											Ext.getCmp('jiraAssignmentInstructions').show();
											Ext.getCmp('jiraAssignmentPanel').show();
										}
									}
								}
							},
							{
								xtype: 'combobox',
								id: 'jiraFieldSelection',
								name: 'fieldName',
								displayField: 'name',
								valueField: 'name',
								fieldLabel: 'Select the Jira Field:',
								editable: false,
								listeners: {
									select: function (combo, record, eOpts) {
										var allowedValues = record.getData().allowedValues;
										var form = Ext.getCmp('fieldAssignmentForm');
										var jiraCodesStore = Ext.getStore('jiraCodesStore');

										// Clear store upon selection
										jiraCodesStore.removeAll();

										// Add store entries for each Jira code
										Ext.Array.each(allowedValues, function(value) {
											jiraCodesStore.add(value);
										});

										if (Ext.getCmp('attributeTypeSelection').getSelection() != undefined) {
											Ext.getCmp('jiraAssignmentInstructions').show();
											Ext.getCmp('jiraAssignmentPanel').show();
										}

									}
								}
							},
							{
								xtype: 'label',
								id: 'jiraAssignmentInstructions',
								hidden: true,
								text: 'Assign Jira codes to Storefront codes by dragging them from the list of Jira codes and dropping them into the Storefront fields.'
							},
							{
								xtype: 'panel',
								id: 'jiraAssignmentPanel',
								hidden: true,
								layout: {
									type: 'hbox',
									align: 'stretch',
									padding: '10px;'
								},
								items: [
									{
										xtype: 'grid',
										title: 'List of Jira Codes',
										viewConfig: {
											enableDD: true,
											plugins: {
												ptype: 'gridviewdragdrop',
												enableDrop: false,
												ddGroup: 'mapping'
											}
										},
										store: Ext.create('Ext.data.Store', {
											id: 'jiraCodesStore'
										}),
										flex: 1,
										hideHeaders: true,
										columns: [
											{ text: 'Jira Codes', dataIndex: 'label', flex: 1}
										]
									},
									{
										xtype: 'form',
										title: 'Matching Storefront Code',
										dockedItems: [
											{
												xtype: 'toolbar',
												dock: 'bottom',
												items: [
													{ xtype: 'tbfill' },
													{
														text: 'Reset',
														iconCls: 'fa fa-lg fa-undo icon-button-color-refresh',
														handler: function() {
															var jfs = Ext.getCmp('jiraFieldSelection');
															var ats = Ext.getCmp('attributeTypeSelection');
															var jfsSelection = jfs.getSelection();
															var atsSelection = ats.getSelection();
															jfs.fireEvent('select', jfs, jfsSelection);
															ats.fireEvent('select', ats, atsSelection);
														}
													}
												]
											}
										],
										width: '100%',
										style: {
											paddingLeft: '20px'
										},
										flex: 1,
										id: 'fieldAssignmentForm'
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
								iconCls: 'fa fa-lg fa-save icon-button-color-save',
								handler: function() {
									var fields = Ext.getCmp('fieldAssignmentForm').getForm().getValues();
									var form = Ext.getCmp('addEditMappingForm').getForm().getValues();
									var jiraFieldRecord = Ext.getCmp('jiraFieldSelection').getSelection().getData();

									var map = [];

									for (var key in fields) {
										if (fields.hasOwnProperty(key)) {
											var thisMapping = {};
											thisMapping.attributeType = form.attributeType;
											thisMapping.localCode = key;
											thisMapping.externalCode = fields[key];
											if (thisMapping.externalCode !== '') map.push(thisMapping);	
										}
									};

									var data = {};
									data.type = form;
									data.type.integrationType = 'JIRA';
									data.type.fieldId = jiraFieldRecord.key;
									data.map = map;
									var url = 'api/v1/resource/attributes/attributexreftypes/';
									url += 'detail';
									var method = 'POST';

									Ext.Ajax.request({
										url: url,
										method: method,
										jsonData: data,
										success: function (response, opts) {
											var message = 'Successfully submitted mapping';
											Ext.toast(message, '', 'tr');
											addEditMappingWin.close();
											jiraConfigStore.load();
										},
										failure: function (response, opts) {
											Ext.MessageBox.alert('Failed', 'Could not submit mapping');
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
								id: 'cancelAddWindow',
								handler: function() {
									Ext.getCmp('addEditMappingForm').reset();
									addEditMappingWin.close();
									Ext.getStore('jiraCodesStore').removeAll();
									Ext.getCmp('fieldAssignmentForm').removeAll();
									Ext.getCmp('jiraAssignmentInstructions').hide();
									Ext.getCmp('jiraAssignmentPanel').hide();
								}
							}
						]
					}
				]
			});


			var initializeDropTarget = function initializeDropTarget(targetField) {
				var fieldDropTarget = new Ext.dd.DropTarget(targetField.id, {
					ddGroup: 'mapping',
					notifyDrop: function(ddSource, e, data) {
						if (targetField.getValue() == '') {
							var selectedRecord = ddSource.dragData.records[0];
							targetField.setValue(selectedRecord.getData().label);
							ddSource.view.store.remove(selectedRecord);
						}
						else { 
							Ext.Msg.alert('Mapping Already Set','The Storefront code mapping is already set for "' + targetField.getFieldLabel() + '". To re-assign this mapping, you must click "Reset" to clear the mapping fields.');
						}

						return true;
					}
				});
			};

			var jiraConfigGrid = Ext.create('Ext.grid.Panel', {
				title: 'Jira Mapping Configuration',
				id: 'jiraConfigGrid',
				store: jiraConfigStore,
				requiredPermissions: ['ADMIN-INTEGRATION-EXTERNAL'],
				actionOnInvalidPermission: 'destroy',
				columns: [
					{ text: 'Project Id', dataIndex: 'projectType', flex: 1},
					{ text: 'Issue Type', dataIndex: 'issueType', flex: 1},
					{ text: 'Attribute', dataIndex: 'attributeName', flex: 2},
					{ text: 'Custom Field', dataIndex: 'fieldName', flex: 2}
				],
				listeners: {
					selectionchange: function (grid, record, index, opts) {
						if (Ext.getCmp('jiraConfigGrid').getSelectionModel().hasSelection()) {
							Ext.getCmp('jiraConfigGrid-tools-edit').enable();
							Ext.getCmp('jiraConfigGrid-tools-delete').enable();
						} 
						else {
							Ext.getCmp('jiraConfigGrid-tools-edit').disable();
							Ext.getCmp('jiraConfigGrid-tools-delete').disable();
						}
					}
				},
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'top',
						items: [
							{
								text: 'Refresh',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
								handler: function () {
									jiraConfigStore.load();
									jiraConfigGrid.getSelectionModel().deselectAll();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Add New Mapping',
								id: 'jiraConfigGrid-tools-add',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
								requiredPermissions: ['ADMIN-INTEGRATION-CREATE'],
								handler: function () {
									actionAddMapping();
								}
							},
							{
								text: 'Edit',
								id: 'jiraConfigGrid-tools-edit',
								scale: 'medium',
								width: '100px',
								iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
								disabled: true,
								requiredPermissions: ['ADMIN-INTEGRATION-UPDATE'],
								handler: function () {
									var record = jiraConfigGrid.getSelection()[0];
									actionEditMapping(record);
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Delete',
								id: 'jiraConfigGrid-tools-delete',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-trash icon-button-color-warning',
								disabled: true,
								requiredPermissions: ['ADMIN-INTEGRATION-DELETE'],
								handler: function () {
									var record = jiraConfigGrid.getSelection()[0];
									var title = 'Delete Mapping';
									var msg = 'Are you sure you want to delete this mapping?';
									Ext.MessageBox.confirm(title, msg, function (btn) {
										if (btn === 'yes') {
											actionDeleteMapping(record);
										}
									});

								}
							}	
						]
					}
				]

			});

			var actionAddMapping = function actionAddMapping() {
				addEditMappingWin.setTitle('Add Mapping');
				addEditMappingWin.show();
			};

			var actionEditMapping = function actionEditMapping(record) {
				var data = record.getData();
				addEditMappingWin.setTitle('Edit Mapping');
				addEditMappingWin.show();
				var jps = Ext.getCmp('jiraProjectSelection');
				var jpis = Ext.getCmp('jiraProjectIssueSelection');
				var ats = Ext.getCmp('attributeTypeSelection');
				var jfs = Ext.getCmp('jiraFieldSelection');
				addEditMappingWin.setLoading(true);
				jps.getStore().load(function() {
					jps.select(data.projectType);
					jps.fireEvent('select',jps,jps.getSelection());
					jpis.getStore().load(function() {
						jpis.select(data.issueType);
						jpis.fireEvent('select',jpis,jpis.getSelection());
						ats.getStore().load(function() {
							ats.select(data.attributeType);
							ats.fireEvent('select',ats,ats.getSelection());
							jfs.getStore().load(function() {
								jfs.select(data.fieldName);
								jfs.fireEvent('select',jfs,jfs.getSelection());
								var form = Ext.getCmp('fieldAssignmentForm').getForm();
								var store = Ext.getStore('jiraCodesStore');

								Ext.Array.each(data.mapping, function(thisMap) {
									var field = form.findField(thisMap.localCode);
									field.setValue(thisMap.externalCode);
									store.remove(store.findRecord('label', thisMap.externalCode));
								});
								addEditMappingWin.setLoading(false);
							});
						});
					});
				});
			};

			var actionDeleteMapping = function actionDeleteMapping(record) {
				var attributeType = record.getData().attributeType;
				var attributeName = record.getData().attributeName;
				var url = 'api/v1/resource/attributes/attributexreftypes/';
				url += attributeType;
				var method = 'DELETE';

				Ext.Ajax.request({
					url: url,
					method: method,
					success: function (response, opts) {
						var message = 'Successfully deleted mapping for "' + attributeName + '"';
						Ext.toast(message, '', 'tr');
						Ext.getCmp('jiraConfigGrid').getStore().load();
						Ext.getCmp('jiraConfigGrid').getSelectionModel().deselectAll();
					},
					failure: function (response, opts) {
						Ext.MessageBox.alert('Failed to delete',
											 'Error: Could not delete mapping for "' + attributeName + '"');
					}
				});
			};


			var mainPanel = Ext.create('Ext.tab.Panel', {
				title: 'Manage Integration <i class="fa fa-question-circle"  data-qtip="Allows for the configuration of data integration with external systems such as JIRA"></i>',
				items: [
					componentConfigGrid,
					jiraConfigGrid
				]
			});


			addComponentToMainViewPort(mainPanel);


		});		
	</script>	
		
	</stripes:layout-component>
</stripes:layout-render>	
