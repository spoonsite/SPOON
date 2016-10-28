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
/* global Ext */

Ext.define('OSF.form.Media', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Media',

	layout: 'fit',
	//bodyStyle: 'padding: 20px',
	initComponent: function () {		
		this.callParent();
		
		var mediaPanel = this;
		

		var mediaGridForm = Ext.create('Ext.form.Panel', {
			xtype: 'form',			
			title: 'Add/Edit Media',
			collapsible: true,
			titleCollapse: true,
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
					iconCls: 'fa fa-save',
					handler: function(){	
						var mainForm = this.up('form');
						var data = mainForm.getValues();
						var componentId = Ext.getCmp('mediaGrid').componentRecord.get('componentId');

						data.fileSelected = mainForm.getComponent('upload').getValue();
						data.link = data.originalLink;
						data.originalName = data.originalFileName;

						if (!data.originalFileName && ((!data.link && !data.fileSelected) || (data.link && data.fileSelected))) {

							mainForm.getForm().markInvalid({
								file: 'Either a link or a file must be entered',
								originalLink: 'Either a link or a file must be entered'
							});

						} else {
							if (!data.fileSelected) {
								var method = 'POST';
								var update = '';
								if (data.componentMediaId) {
									update = '/' + data.componentMediaId;
									method = 'PUT';
								}

								CoreUtil.submitForm({
									url: 'api/v1/resource/components/' + componentId + '/media' + update,
									method: method,
									removeBlankDataItems: true,
									data: data,
									form: mainForm,
									success: function(){
										Ext.getCmp('mediaGrid').getStore().reload();
										mainForm.reset();
										mainForm.getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
									}
								});
							} else {
								//upload

								mainForm.submit({
									url: 'Media.action?UploadMedia',
									params: {
										'componentMedia.mediaTypeCode' : data.mediaTypeCode,
										'componentMedia.caption': data.caption,
										'componentMedia.link': data.link,
										'componentMedia.hideInDisplay': data.hideInDisplay,
										'componentMedia.usedInline': data.usedInline,
										'componentMedia.componentMediaId': data.componentMediaId,
										'componentMedia.componentId': componentId
									},
									method: 'POST',
									submitEmptyText: false,
									waitMsg: 'Uploading media please wait...',
									waitTitle: 'Uploading',
									success: function(formBasic, action, opt){
										Ext.getCmp('mediaGrid').getStore().reload();
										mainForm.reset();
										mainForm.getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
									}, 
									failure: function(formBasic, action, opt) {
										var errorResponse = Ext.decode(action.response.responseText);
										var errorObj = {};        
										Ext.Array.each(errorResponse.errors.entry, function(item, index, entry) {
											errorObj[item.key.replace('componentMedia', '')] = item.value;
										});
										mainForm.markInvalid(errorObj);
									}
								});

							}
						}
					}
				},
				{
					xtype: 'button',
					text: 'Cancel',										
					iconCls: 'fa fa-close',
					handler: function(){
						this.up('form').reset();
						this.up('form').getComponent('upload').setFieldLabel('Upload Media (Limit 1GB)');
					}									
				}								
			],
			items: [
				{
					xtype: 'hidden',
					name: 'componentMediaId'
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
					name: 'mediaTypeCode',									
					allowBlank: false,								
					margin: '0 0 5 0',
					editable: false,
					typeAhead: false,
					width: '100%',
					fieldLabel: 'Media Type <span class="field-required" />',
					storeConfig: {
						url: 'api/v1/resource/lookuptypes/MediaType'
					}
				}),
				{
					xtype: 'textfield',
					fieldLabel: 'Caption <span class="field-required" />',									
					allowBlank: false,									
					maxLength: '255',
					name: 'caption'
				},
				{
					xtype: 'filefield',
					itemId: 'upload',
					fieldLabel: 'Upload Media (Limit of 1GB)',																											
					name: 'file',
					listeners: {
						change: CoreUtil.handleMaxFileLimit
					}
				},
				{
					xtype: 'textfield',
					fieldLabel: 'Link',																																	
					maxLength: '255',									
					emptyText: 'http://www.example.com/image.png',
					name: 'originalLink'
				},
				{
					xtype: 'checkbox',
					fieldLabel: 'Hide In Carousel',
					name: 'hideInDisplay'
				},
				{
					xtype: 'checkbox',
					fieldLabel: 'Used Inline <i class="fa fa-question-circle"  data-qtip="Check this box if you intend to use this media inline in a description. If selected, you will be warned later when attempting to delete the media to also delete the inline refereence in the description." ></i>',
					name: 'usedInline'
				}
//				Ext.create('OSF.component.SecurityComboBox', {	
//					hidden: !${branding.allowSecurityMarkingsFlg}
//				})								
			]
		});
			
		var mediaGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: Ext.create('Ext.data.Store', {
				fields: [
					"componentMediaId",
					"contentType",
					"mediaTypeCode",
					"link",
					"mimeType",
					"caption",
					"fileName",
					"originalFileName",
					"originalLink",
					{
						name: 'updateDts',
						type:	'date',
						dateFormat: 'c'
					},							
					"activeStatus",
					"usedInline",
					"hideInDisplay"
				],
				autoLoad: false,
				proxy: {
					type: 'ajax'							
				}
			}),					
			columns: [
				{ text: 'Media Type', dataIndex: 'contentType',  width: 200 },
				{ text: 'Caption',  dataIndex: 'caption', flex: 1, minWidth: 200 },
				{ text: 'Mime Type',  dataIndex: 'mimeType', width: 200 },
				{ text: 'Local Media Name',  dataIndex: 'originalFileName', width: 200 },
				{ text: 'Link',  dataIndex: 'originalLink', width: 200 },						
				{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
				{ text: 'Hide In Carousel', dataIndex: 'hideInDisplay', width: 150},
				{ text: 'Used Inline', dataIndex: 'usedInline', width: 150 },
				//{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
			],
			listeners: {
				itemdblclick: function(grid, record, item, index, e, opts){
					this.down('form').reset();
					this.down('form').loadRecord(record);
					if (record.get('originalFileName')) {
						this.down('form').getComponent('upload').setFieldLabel('Current File: ' + record.get('originalFileName'));
					} else {
						this.down('form').getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
					}
				},
				selectionchange: function(grid, record, index, opts){
					var fullgrid = Ext.getCmp('mediaGrid');
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
										url: 'api/v1/resource/components/' + Ext.getCmp('mediaGrid').componentRecord.get('componentId') + '/media/view',
										params: {
											status: newValue
										}
									});
								}
							}
						}, 								
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
							text: 'Edit',
							itemId: 'editBtn',
							iconCls: 'fa fa-edit',
							handler: function(){
								var record = Ext.getCmp('mediaGrid').getSelection()[0];
								this.up('grid').down('form').reset();
								this.up('grid').down('form').loadRecord(record);
								if (record.get('originalFileName')) {
									this.up('grid').down('form').getComponent('upload').setFieldLabel('Current File: ' + record.get('originalFileName'));
								} else {
									this.up('grid').down('form').getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
								}
							}									
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Toggle Status',
							itemId: 'toggleStatusBtn',
							iconCls: 'fa fa-power-off',									
							disabled: true,
							handler: function(){
								actionSubComponentToggleStatus(Ext.getCmp('mediaGrid'), 'componentMediaId', 'media');
							}
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Remove',
							itemId: 'removeBtn',
							iconCls: 'fa fa-trash',
							disabled: true,
							handler: function(){
								var record = Ext.getCmp('mediaGrid').getSelection()[0];
								if (record.get('usedInline')) {
									var msg = 'This media has been marked as being used inline. This means that the media is being used in a description, etc. ';
									msg += 'If you delete this media, that reference will no longer be valid and the media will not be available where it is referenced elsewhere.';
									msg += '<br><br>Do you still wish to delete this media?';
									Ext.Msg.confirm('Media Used Inline', msg, function (btn) { 
										if (btn ==='yes') {
											actionSubComponentToggleStatus(Ext.getCmp('mediaGrid'), 'componentMediaId', 'media', undefined, undefined, true);
										}
									});
								} else {
									actionSubComponentToggleStatus(Ext.getCmp('mediaGrid'), 'componentMediaId', 'media', undefined, undefined, true);
								}

							}									
						}
					]
				}
			]																
		});		
		mediaGrid.addDocked(mediaGridForm, 0);
		
		
		mediaPanel.add(mediaGrid);
	}
	
});

