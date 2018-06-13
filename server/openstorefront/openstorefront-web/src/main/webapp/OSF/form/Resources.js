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

Ext.define('OSF.form.Resources', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Resources',

	layout: 'fit',
	hideSecurityMarking: false,
	
	initComponent: function () {		
		this.callParent();
		
		var resourcePanel = this;
		
		resourcePanel.resourceGridForm = Ext.create('Ext.form.Panel', {
			title: 'Add/Edit Resources',
			collapsible: true,
			titleCollapse: true,
			viewConfig: {
				enableTextSelection: true
			},
			border: true,
			layout: 'vbox',
			bodyStyle: 'padding: 10px;',
			margin: '0 0 5 0', 
			defaults: {
				labelAlign: 'right',
				labelWidth: 200,
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
						var componentId = resourcePanel.resourcesGrid.componentId;

						data.fileSelected = form.getComponent('upload').getValue();
						data.link = data.originalLink;
						data.originalName = data.originalFileName;

						if (!data.originalFileName && ((!data.link && !data.fileSelected) || (data.link && data.fileSelected))) {

							form.getForm().markInvalid({
								file: 'Either a link or a file must be entered',
								originalLink: 'Either a link or a file must be entered'
							});

						} else {
							if (!data.fileSelected) {
								var method = 'POST';
								var update = '';
								if (data.resourceId) {
									update = '/' + data.resourceId;
									method = 'PUT';
								}

								CoreUtil.submitForm({
									url: 'api/v1/resource/components/' + componentId + '/resources' + update,
									method: method,
									removeBlankDataItems: true,
									data: data,
									form: form,
									success: function(){
										resourcePanel.resourcesGrid.getStore().reload();
										form.reset();
										form.getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
									}
								});
							} else {
								
								//upload
								var progressMsg = Ext.MessageBox.show({
									title: 'Resource Upload',
									msg: 'Uploading resource please wait...',
									width: 300,
									height: 150,
									closable: false,
									progressText: 'Uploading...',
									wait: true,
									waitConfig: {interval: 300}
								});
								
								form.submit({
									url: 'Resource.action?UploadResource',
									params: {
										'componentResource.resourceType' : data.resourceType,
										'componentResource.description': data.description,
										'componentResource.restricted': data.restricted,
										'componentResource.link': data.link,
										'componentResource.resourceId': data.resourceId,
										'componentResource.componentId': componentId
									},
									method: 'POST',
									submitEmptyText: false,
									waitMsg: 'Uploading please wait...',
									waitTitle: 'Uploading',													
									success: function(formBasic, action, opt){
										resourcePanel.resourcesGrid.getStore().reload();
										form.reset();
										form.getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
										progressMsg.hide();
									}, 
									failure: function(formBasic, action, opt) {
										var errorResponse = Ext.decode(action.response.responseText);
										var errorObj = {};        
										Ext.Array.each(errorResponse.errors.entry, function(item, index, entry) {
											errorObj[item.key.replace('componentResource', '')] = item.value;
										});
										form.markInvalid(errorObj);
										progressMsg.hide();
									}
								});

							}
						}
					}
				},
				{
					xtype: 'button',
					text: 'Cancel',										
					iconCls: 'fa fa-lg fa-close',
					handler: function(){
						this.up('form').reset();
						this.up('form').getComponent('upload').setFieldLabel('Upload Resource (Limit 1GB)');
					}									
				}								
			],
			items: [
				{
					xtype: 'hidden',
					name: 'resourceId'
				},
				{
					xtype: 'hidden',
					name: 'originalFileName'
				},
				{
					xtype: 'hidden',
					name: 'fileName'
				},
				{
					xtype: 'hidden',
					name: 'mimeType'
				},
				Ext.create('OSF.component.StandardComboBox', {
					name: 'resourceType',									
					allowBlank: false,								
					margin: '0 0 5 0',
					editable: false,
					typeAhead: false,
					width: '100%',
					fieldLabel: 'Resource Type <span class="field-required" />',
					storeConfig: {
						url: 'api/v1/resource/lookuptypes/ResourceType'
					}
				}),
				{
					xtype: 'textfield',
					fieldLabel: 'Description',																									
					maxLength: '255',
					name: 'description'
				},
				{
					xtype: 'checkbox',
					name: 'restricted',
					padding: '0 0 0 210',
					boxLabel: 'Restricted'
				},
				{
					xtype: 'textfield',
					fieldLabel: 'Link',																																	
					maxLength: '255',									
					emptyText: 'http://www.example.com/resource',
					name: 'originalLink'
				},
				{
					xtype: 'fileFieldMaxLabel',
					itemId: 'upload',
					name: 'file',
					width: '100%',
					resourceLabel: 'Upload Resource'
				},
				Ext.create('OSF.component.SecurityComboBox', {						
				}),
				Ext.create('OSF.component.DataSensitivityComboBox', {												
					width: '100%'
				})				
			]
		});

		resourcePanel.resourcesGrid = Ext.create('Ext.grid.Panel', {	
			columnLines: true,
			viewConfig: {
				enableTextSelection: true
			},
			store: Ext.create('Ext.data.Store', {
				fields: [
					"resourceId",
					"resourceType",
					"resourceTypeDesc",
					{ name: "description", mapping: function(data){
						return data.description ? data.description : '';
					}},
					"link",
					"localResourceName",
					"mimeType",
					"originalFileName",
					"actualLink",
					"restricted",
					"originalLink",
					"componentId",							
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
				{ text: 'Resource Type', dataIndex: 'resourceTypeDesc',  width: 200 },
				{ text: 'Description', dataIndex: 'description', width: 150 },
				{ text: 'Link',  dataIndex: 'originalLink', flex: 1, minWidth: 200 },
				{ text: 'Mime Type',  dataIndex: 'mimeType', width: 200 },
				{ text: 'Local Resource Name',  dataIndex: 'originalFileName', width: 200 },
				{ text: 'Restricted',  dataIndex: 'restricted', width: 150 },						
				{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
				{ text: 'Data Sensitivity',  dataIndex: 'dataSensitivity', width: 200, hidden: true },
				{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: resourcePanel.hideSecurityMarking  }
			],
			listeners: {
				itemdblclick: function(grid, record, item, index, e, opts){
					this.down('form').reset();
					this.down('form').loadRecord(record);
					if (record.get('originalFileName')) {
						this.down('form').getComponent('upload').setFieldLabel('Current File: ' + record.get('originalFileName'));
					} else {
						this.down('form').getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
					}
				},
				selectionchange: function(grid, record, index, opts){
					var fullgrid = resourcePanel.resourcesGrid;
					if (fullgrid.getSelectionModel().getCount() === 1) {
						fullgrid.down('toolbar').getComponent('editBtn').setDisabled(false);
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
					} else {
						fullgrid.down('toolbar').getComponent('editBtn').setDisabled(true);
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
					}
				}						
			},
			dockedItems: [												
				{
					xtype: 'toolbar',
					docked: 'top',
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
										url: 'api/v1/resource/components/' + resourcePanel.resourcesGrid.componentId + '/resources/view',
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
								var record = resourcePanel.resourcesGrid.getSelection()[0];
								this.up('grid').down('form').reset();
								this.up('grid').down('form').loadRecord(record);
								if (record.get('originalFileName')) {
									this.up('grid').down('form').getComponent('upload').setFieldLabel('Current File: ' + record.get('originalFileName'));
								} else {
									this.up('grid').down('form').getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
								}
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
								CoreUtil.actionSubComponentToggleStatus(resourcePanel.resourcesGrid, 'resourceId', 'resources');
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Delete',
							itemId: 'removeBtn',
							iconCls: 'fa fa-lg fa-trash icon-button-color-warning',		
							disabled: true,
							handler: function(){
								CoreUtil.actionSubComponentToggleStatus(resourcePanel.resourcesGrid, 'resourceId', 'resources', undefined, undefined, true);
							}									
						}
					]
				}
			]					
		});		
		resourcePanel.resourcesGrid.addDocked(resourcePanel.resourceGridForm, 0);
		
		resourcePanel.add(resourcePanel.resourcesGrid);
	},	
	loadData: function(evaluationId, componentId, data, opts, callback) {
		var resourcePanel = this;
		
		resourcePanel.componentId = componentId;
		resourcePanel.resourcesGrid.componentId = componentId;
		
		resourcePanel.resourcesGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/resources/view'
		});
		
		if (opts && opts.commentPanel) {
			opts.commentPanel.loadComments(evaluationId, "Resources", componentId);
		}

		if (callback) {
			callback();
		}
	}
	
});


