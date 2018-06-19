/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

Ext.define('OSF.form.Dependencies', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Dependencies',

	requiredPermissions: ['ADMIN-ENTRY-DEPENDENCY-MANAGEMENT'],
	beforePermissionsCheckFailure: function () { return false; },
	beforePermissionsCheckSuccess: function () { return false; },
	preventDefaultAction: true,
	layout: 'fit',
	hideSecurityMarking: false,
	
	initComponent: function () {		
		this.callParent();
		
		var dependanciesPanel = this;
		
		dependanciesPanel.dependenciesGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: Ext.create('Ext.data.Store', {
				fields: [
					"dependencyId",
					"dependencyName",
					"version",
					"dependancyReferenceLink",
					"comment",
					{
						name: 'updateDts',
						type:	'date',
						dateFormat: 'c'
					},														
					"activeStatus"
				],
				autoLoad: false,
				proxy: {
					type: 'ajax'							
				}
			}),
			columns: [
				{ text: 'Name', dataIndex: 'dependencyName',  width: 200 },
				{ text: 'Version',  dataIndex: 'version', width: 150 },
				{ text: 'Link',  dataIndex: 'dependancyReferenceLink', width: 200 },
				{ text: 'Comment',  dataIndex: 'comment', flex: 1, minWidth: 200 },
				{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
				{ text: 'Data Sensitivity',  dataIndex: 'dataSensitivity', width: 200, hidden: true },
				{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: dependanciesPanel.hideSecurityMarking }
			],
			listeners: {
				itemdblclick: function(grid, record, item, index, e, opts){
					this.down('form').reset();
					this.down('form').loadRecord(record);
				},
				selectionchange: function(grid, record, index, opts){
					var fullgrid = dependanciesPanel.dependenciesGrid;
					if (fullgrid.getSelectionModel().getCount() === 1) {
						fullgrid.down('toolbar').getComponent('editBtn').setDisabled(false);
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
					} else {
						fullgrid.down('toolbar').getComponent('editBtn').setDisabled(true);
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
					}
				}						
			},
			dockedItems: [
				{
					xtype: 'form',
					title: 'Add/Edit Dependency',
					collapsible: true,
					titleCollapse: true,
					border: true,
					layout: 'vbox',
					bodyStyle: 'padding: 10px;',
					margin: '0 0 5 0', 
					defaults: {
						labelAlign: 'right',
						width: '100%'
					},
					buttonAlign: 'center',
					buttons: [
						{
							xtype: 'button',
							text: 'Save',
							formBind: true,
							margin: '0 20 0 0',
							iconCls: 'fa fa-lg fa-save',
							handler: function(){	
								var form = this.up('form');
								var data = form.getValues();
								var componentId = dependanciesPanel.dependenciesGrid.componentId;

								var method = 'POST';
								var update = '';
								if (data.dependencyId) {
									update = '/' + data.dependencyId;
									method = 'PUT';
								}

								CoreUtil.submitForm({
									url: 'api/v1/resource/components/' + componentId + '/dependencies' + update,
									method: method,
									data: data,
									form: form,
									success: function(){
										dependanciesPanel.dependenciesGrid.getStore().reload();
										form.reset();
									}
								});
							}
						},
						{
							xtype: 'button',
							text: 'Cancel',										
							iconCls: 'fa fa-lg fa-close',
							handler: function(){
								this.up('form').reset();
							}									
						}								
					],
					items: [
						{
							xtype: 'hidden',
							name: 'dependencyId'
						},
						{
							xtype: 'textfield',
							fieldLabel: 'Name <span class="field-required" />',									
							allowBlank: false,									
							maxLength: '255',
							name: 'dependencyName'
						},
						{
							xtype: 'textfield',
							fieldLabel: 'Version <span class="field-required" />',									
							allowBlank: false,								
							maxLength: '255',
							name: 'version'
						},
						{
							xtype: 'textfield',
							fieldLabel: 'External Link',															
							emptyText: 'http://dependency.com/download',									
							maxLength: '255',
							name: 'dependancyReferenceLink'
						},
						{
							xtype: 'textfield',
							fieldLabel: 'Comment',																											
							maxLength: '255',
							name: 'comment'
						},
						Ext.create('OSF.component.SecurityComboBox', {								
						}),
						Ext.create('OSF.component.DataSensitivityComboBox', {			
							width: '100%'
						})						
					]
				},						
				{
					xtype: 'toolbar',
					items: [
						{
							xtype: 'combobox',
							fieldLabel: 'Filter Status',
							store: {
								data: [												
									{ code: 'A', description: 'Active' },
									{ code: 'I', description: 'Inactive' }
								]
							},
							forceSelection: true,
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'A',
							listeners: {
								change: function(combo, newValue, oldValue, opts){
									this.up('grid').getStore().load({
										url: 'api/v1/resource/components/' + dependanciesPanel.dependenciesGrid.componentId + '/dependencies/view',
										params: {
											status: newValue
										}
									});
								}
							}
						}, 								
						{
							text: 'Refresh',
							iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
							handler: function(){
								this.up('grid').getStore().reload();
							}
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Edit',
							itemId: 'editBtn',
							iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
							handler: function(){
								this.up('grid').down('form').reset();
								this.up('grid').down('form').loadRecord(dependanciesPanel.dependenciesGrid.getSelection()[0]);
							}									
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Toggle Status',
							itemId: 'toggleStatusBtn',
							iconCls: 'fa fa-lg fa-power-off icon-button-color-default',			
							disabled: true,
							handler: function(){
								CoreUtil.actionSubComponentToggleStatus(dependanciesPanel.dependenciesGrid, 'dependencyId', 'dependencies');
							}
						}
					]
				}
			]						
		});
		
		dependanciesPanel.add(dependanciesPanel.dependenciesGrid);
		
	},
	loadData: function(evaluationId, componentId, data, opts, callback) {
		var dependanciesPanel = this;
		
		dependanciesPanel.componentId = componentId;
		dependanciesPanel.dependenciesGrid.componentId = componentId;
		
		dependanciesPanel.dependenciesGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/dependencies/view'
		});	
		
		if (opts && opts.commentPanel) {
			opts.commentPanel.loadComments(evaluationId, "Dependencies", componentId);
		}

		if (callback) {
			callback();
		}
	}
	
});


