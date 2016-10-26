/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* global Ext, CoreUtil */

Ext.define('OSF.component.IntegrationWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.IntegrationWindow',
	
	title: 'Integration',
	modal: true,
	iconCls: 'fa fa-lg fa-gear',
	layout: 'fit',
	width: '60%',
	height: '50%',
		
	initComponent: function () {
		this.callParent();

		var integrationWindow = this;
		
		var addEditWindow = Ext.create('Ext.window.Window', {
			title: 'Add/Update  Configuration',
			modal: true,
			width: '40%',
			height: 300,
			minWidth: 300,
			minHeigth: 300,
			alwaysOnTop: true,
			layout: 'fit',
			resizable: false,
			items: [
				{
					xtype: 'form',
					itemId: 'configForm',
					bodyStyle: 'padding: 10px',
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},
					items: [
						{
							xtype: 'hidden',
							name: 'integrationConfigId'
						},
						{
							xtype: 'combobox',
							fieldLabel: 'Integration Type <span class="field-required" />',
							queryMode: 'local',							
							displayField: 'description',
							valueField: 'code',
							name: 'integrationType',
							allowBlank: false,
							editable: false,
							typeAhead: false,
							width: '100%',
							store: {
								autoLoad: true,
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/lookuptypes/IntegrationType'
								}
							}
						},						
						{
							xtype: 'combobox',
							fieldLabel: 'Project Type <span class="field-required" />',
							queryMode: 'local',							
							displayField: 'projectType',
							valueField: 'projectType',
							name: 'projectType',
							allowBlank: false,
							editable: false,
							typeAhead: false,
							width: '100%',
							store: {
								autoLoad: true,
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/attributes/attributexreftypes/detail/distinct'
								}
							}
						},
						{
							xtype: 'panel',
							layout: 'hbox',
							defaults: {
								labelAlign: 'top',
								labelSeparator: ''
							},
							items: [
								{
									xtype: 'textfield',
									flex: 1,
									fieldLabel: 'Jira Issue Number <span class="field-required" />',
									name: 'issueNumber',
									allowBlank: false,
									maxLength: 120
								},
								{
									xtype: 'button',
									width: 100,
									minWidth: 100,
									text: 'Check',
									margin: '25 0 0 0',
									iconCls: 'fa fa-check',
									handler: function(){
										loadJiraIssue();
									}
								}
							]
						},
						{
							xtype: 'panel',
							itemId: 'foundTicketPanel',
							html: ''
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
									handler: function(){
										var configForm = this.up('form');
										var data = configForm.getValues();
										var addEditWin = this.up('window');
										//integrationType
										var projectTypeField = configForm.getForm().findField('projectType');
										data.issueType = projectTypeField.getSelection().get('issueType');
										
										var method = 'POST';
										var endPath = '';
										if (data.integrationConfigId) {
											method = 'PUT';
											endPath = '/' + data.integrationConfigId;
										}
										CoreUtil.submitForm({
											submitEmptyText: false,
											url: 'api/v1/resource/components/' + integrationWindow.componentId + '/integration/configs' + endPath,
											method: method,
											form: configForm,
											data: data,
											success: function(response, opts) {
												integrationWindow.integrationGrid.getStore().reload();
												addEditWin.close();
											},
											failure: function(response, opts) {
												Ext.MessageBox.alert('Error During Configuration Add/Update', "Duplicate Jira Issue Number provided.<br /><br />Please provide a different Issue Number.");
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
									handler: function(){
										this.up('window').close();
									}							
								}
							]
						}
					]
				}
			]			
		});
		
		var loadJiraIssue = function() {
			var form = addEditWindow.getComponent('configForm');
			var issueField = form.getForm().findField('issueNumber');
			if (issueField.getValue()) {
				form.setLoading('Retrieving Issue Name...');
				Ext.Ajax.request({
					url: 'api/v1/service/jira/ticket/' + issueField.getValue(),
					callback: function(){
						form.setLoading(false);
					},
					success: function(response, opt){												
						var data = response.responseText;
						data = 'Ticket Name: <b>' + data + '</b>';
						form.getComponent('foundTicketPanel').update(data);
					}
				});
			}
		};
		
		integrationWindow.integrationGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: {			
				autoLoad: false,
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/components//integration/configs'					
				}
			},
			forceFit: true,
			plugins: [
				{
					ptype: 'rowexpander',
					rowBodyTpl: '<tpl if="errorMessage">Error Information<hr><b>Message:</b> {errorMessage} <br><b>Potential Resolution:</b> {potentialResolution}<br><b>Ticket:</b> {errorTicketNumber}</tpl>'
				}
			],			
			columns: [
				{ text: 'Integration Type', align: 'center', dataIndex: 'integrationType', width: 150 },
				{
					text: 'Integration Config',
					columns: [
						{ text: 'Project Type', dataIndex: 'projectType', width: 175 },
						{ text: 'Issue Type', dataIndex: 'issueType', width: 150 },
						{ text: 'Issue Number', dataIndex: 'issueNumber', width: 150 }
					]
				},
				{ text: 'Status',  align: 'center', dataIndex: 'status', width: 150,
					renderer: function (value, metaData, record) {
						var label = '';
						if (value === 'P') {
							label = 'Pending';
							metaData.tdCls = 'alert-info';
						} else if (value === 'W') {
							label = 'Working';
							metaData.tdCls = 'alert-warning';							
						} else if (value === 'C') {
							label = 'Complete';
							metaData.tdCls = 'alert-success';							
						} else if (value === 'E') {
							label = 'Error ';
							metaData.tdCls = 'alert-danger';							
						}
						return label;
					}
				},
				{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },
				{ text: 'Last Start Date', dataIndex: 'lastStartTime', hidden: true, width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
				{ text: 'Create User', dataIndex: 'createUser', hidden: true, width: 150 },
				{ text: 'Create Date', dataIndex: 'createDts', hidden: true, width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
				{ text: 'Last Complete Date', dataIndex: 'lastEndTime', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
			],
			listeners: {	
				selectionchange: function(selectionModel, selected, opts){
					
					var mainToolbar = integrationWindow.integrationGrid.getComponent('mainToolbar');
					
					if (selected.length > 0) {
						mainToolbar.getComponent('tbEdit').setDisabled(false);
						mainToolbar.getComponent('tbRun').setDisabled(false);
						mainToolbar.getComponent('tbStatus').setDisabled(false);
						mainToolbar.getComponent('tbDelete').setDisabled(false);
					} else {
						mainToolbar.getComponent('tbEdit').setDisabled(true);
						mainToolbar.getComponent('tbRun').setDisabled(true);
						mainToolbar.getComponent('tbStatus').setDisabled(true);
						mainToolbar.getComponent('tbDelete').setDisabled(true);						
					}
				}
			},
			dockedItems: [							
				{
					xtype: 'toolbar',
					itemId: 'mainToolbar',
					dock: 'top',
					items: [
						{
							text: 'Refresh',							
							iconCls: 'fa fa-refresh',
							handler: function(){
								this.up('grid').getStore().reload();
							}							
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Add',							
							iconCls: 'fa fa-plus',
							handler: function(){
								addEditWindow.show();
								addEditWindow.getComponent('configForm').reset();
							}							
						},						
						{
							text: 'Edit',	
							itemId: 'tbEdit',
							disabled: true,
							iconCls: 'fa fa-edit',
							handler: function(){
								addEditWindow.show();
								var record = this.up('grid').getSelectionModel().getSelection()[0];
								addEditWindow.getComponent('configForm').loadRecord(record);
								loadJiraIssue();
							}							
						},
						{
							text: 'Run',	
							itemId: 'tbRun',
							disabled: true,
							iconCls: 'fa fa-play',
							handler: function(){
								var grid = this.up('grid');
								var componentId = this.up('grid').getSelectionModel().getSelection()[0].get('componentId');
								var integrationId = this.up('grid').getSelectionModel().getSelection()[0].get('integrationConfigId');
								Ext.Ajax.request({
									url: 'api/v1/resource/components/' + componentId + '/integration/configs/' + integrationId + '/run',
									method: 'POST',
									success: function(response, opt) {
										Ext.toast('Started integration task');
										grid.getStore().reload();
									}
								});
							}							
						},						
						{
							xtype: 'tbfill'
						},
						{
							text: 'Toggle Status',
							itemId: 'tbStatus',
							disabled: true,
							iconCls: 'fa fa-power-off',
							handler: function(){
								var grid = this.up('grid');
								var componentId = this.up('grid').getSelectionModel().getSelection()[0].get('componentId');
								var integrationId = this.up('grid').getSelectionModel().getSelection()[0].get('integrationConfigId');
								var activeStatus = this.up('grid').getSelectionModel().getSelection()[0].get('activeStatus');
								
								var statusUpdate = 'inactivate';
								if (activeStatus === 'I') {
									statusUpdate = 'activate';
								}
								
								Ext.Ajax.request({
									url: 'api/v1/resource/components/' + componentId + '/integration/configs/' + integrationId + '/' + statusUpdate,
									method: 'PUT',
									success: function(response, opt) {
										grid.getStore().reload();
									}
								});								
								
							}							
						},
						{
							text: 'Delete',
							itemId: 'tbDelete',
							disabled: true,							
							iconCls: 'fa fa-trash',
							handler: function(){
								var grid = this.up('grid');
								var componentId = this.up('grid').getSelectionModel().getSelection()[0].get('componentId');
								var integrationId = this.up('grid').getSelectionModel().getSelection()[0].get('integrationConfigId');
								
								Ext.Msg.show({
									title:'Confirm',
									message: 'Are you sure you want to delete selected configuration?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {
											Ext.Ajax.request({
												url: 'api/v1/resource/components/' + componentId + '/integration/configs/' + integrationId,
												method: 'DELETE',
												success: function(response, opt) {
													grid.getStore().reload();
												}
											});											
										} 
									}
								});
							}							
						}
					]						
				}
			] 
		});
		
		integrationWindow.add(integrationWindow.integrationGrid);

	},

	loadConfigs: function(componentId) {
		var integrationWindow = this;
		integrationWindow.componentId = componentId;
		integrationWindow.integrationGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/integration/configs'
		});
		
	}


});

