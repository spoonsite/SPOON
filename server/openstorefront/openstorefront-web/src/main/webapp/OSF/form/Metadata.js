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

Ext.define('OSF.form.Metadata', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Metadata',

	layout: 'fit',
	hideSecurityMarking: false,
	
	initComponent: function () {		
		this.callParent();
		
		var metadataPanel = this;
		
		var metadataValueLoadTask = new Ext.util.DelayedTask(function() {
			var labelString = metadataPanel.metadataGrid.getComponent('form').getComponent('metadataLabelComboBox').getValue();
			if (labelString) {
				var valueStore = metadataPanel.metadataGrid.getComponent('form').getComponent('metadataValueComboBox').getStore();
				valueStore.getProxy().setUrl('api/v1/resource/componentmetadata/lookup/values');
				valueStore.getProxy().setExtraParams({label: labelString});
				valueStore.load();
			}
		});

		metadataPanel.metadataGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: Ext.create('Ext.data.Store', {
				fields: [
					"metadataId",
					"label",
					"value",
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
				{ 
					text: 'Label',
					dataIndex: 'label',
					width: 200 
				},
				{ 
					text: 'Value',
					dataIndex: 'value',
					flex: 1,
					minWidth: 200 
				},
				{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
				{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: metadataPanel.hideSecurityMarking }
			],
			listeners: {
				itemdblclick: function(grid, record, item, index, e, opts){
					this.down('form').loadRecord(record);
				},
				selectionchange: function(grid, record, index, opts){
					var fullgrid = metadataPanel.metadataGrid;
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
					itemId: 'form',
					title: 'Add/Edit Metadata',					
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
								var componentId = metadataPanel.metadataGrid.componentId;

								var method = 'POST';
								var update = '';
								if (data.metadataId) {
									update = '/' + data.metadataId;
									method = 'PUT';
								}

								CoreUtil.submitForm({
									url: 'api/v1/resource/components/' + componentId + '/metadata' + update,
									method: method,
									data: data,
									form: form,
									success: function(){
										metadataPanel.metadataGrid.getStore().reload();
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
							name: 'metadataId'
						},
						{
							xtype: 'combobox',
							itemId: 'metadataLabelComboBox',
							fieldLabel: 'Label <span class="field-required" />',									
							allowBlank: false,									
							maxLength: '255',									
							name: 'label',
							valueField: 'code',
							displayField: 'description',
							typeAhead: 'true',
							store: Ext.create('Ext.data.Store', {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/componentmetadata/lookup'
								},
								autoLoad: true
							}),
							listeners: {
								change: function (combo, newValue, oldValue, eOpts) {
									metadataValueLoadTask.delay(500);
								}
							}
						},
						{
							xtype: 'combobox',
							itemId: 'metadataValueComboBox',
							fieldLabel: 'Value <span class="field-required" />',									
							allowBlank: false,									
							maxLength: '255',									
							name: 'value',
							valueField: 'code',
							displayField: 'description',
							typeAhead: 'true',
							store: Ext.create('Ext.data.Store', {								
								proxy: {
									type: 'ajax'
								}
							})
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
										url: 'api/v1/resource/components/' + metadataPanel.metadataGrid.componentId + '/metadata/view',
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
								this.up('grid').down('form').loadRecord(metadataPanel.metadataGrid.getSelection()[0]);
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
								CoreUtil.actionSubComponentToggleStatus(metadataPanel.metadataGrid, 'metadataId', 'metadata');
							}
						}
					]
				}
			]									
		});		
		
		metadataPanel.add(metadataPanel.metadataGrid);
	},	
	loadData: function(evaluationId, componentId, data, opts) {
		var metadataPanel = this;
		
		metadataPanel.componentId = componentId;
		metadataPanel.metadataGrid.componentId = componentId;
		
		metadataPanel.metadataGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/metadata/view'
		});
		
		if (opts && opts.commentPanel) {
			opts.commentPanel.loadComments(evaluationId, "Metadata", componentId);
		}
	}
	
});

