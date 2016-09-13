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

Ext.define('OSF.component.ImportWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.ImportWindow',
	title: 'Import Data',
	modal: true,
	width: '50%',
	height: '70%',
	layout: 'fit',	
	y: 100,
	fileTypeReadyOnly: true,
	fileTypeValue: 'COMPONENT',	
	initComponent: function () {
		this.callParent();

		var importWindow = this;

		importWindow.formPanel = Ext.create('Ext.form.Panel', {
			bodyStyle: 'padding: 20px;',
			autoScroll: true,
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Upload',
							formBind: true,
							iconCls: 'fa fa-upload',
							handler: function () {
								var uploadForm = this.up('form');
								uploadForm.submit({
									submitEmptyText: false,
									url: 'Upload.action?ImportData',
									success: function(form, action) {
										Ext.toast('File has been queued for processing.', 'Upload Successfully', 'br');	
										if (importWindow.uploadSuccess) {
											importWindow.uploadSuccess(form, action);
										}
										uploadForm.reset();
										uploadForm.getComponent('fileTypeCB').setValue(importWindow.fileTypeValue);
										importWindow.close();
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
							handler: function () {
								importWindow.close();
							}
						}
					]
				}
			],
			items: [
				Ext.create('OSF.component.StandardComboBox', {
					itemId: 'fileTypeCB',
					name: 'fileType',
					allowBlank: false,
					margin: '0 0 0 0',
					editable: false,
					typeAhead: false,
					width: '100%',
					readOnly: importWindow.fileTypeReadyOnly,
					fieldLabel: 'File Type <span class="field-required" />',
					storeConfig: {
						url: 'api/v1/resource/lookuptypes/FileType'
					},
					listeners: {
						change: function (field, newValue, oldvalue, opts) {
							field.up('form').getComponent('fileFormatCB').getStore().load({
								url: 'api/v1/resource/filehistory/filetypes/' + newValue + '/formats'
							});

							//set options
							var optionPanel = field.up('form').getComponent('optionPanel');
							optionPanel.getLayout().setActiveItem(optionPanel.getComponent('optionPanel-' + newValue));
						}
					}
				}),
				Ext.create('OSF.component.StandardComboBox', {
					itemId: 'fileFormatCB',
					name: 'fileFormat',
					allowBlank: false,
					margin: '0 0 0 0',
					editable: false,
					typeAhead: false,
					width: '100%',
					fieldLabel: 'File Format <span class="field-required" />',
					storeConfig: {
						url: 'api/v1/resource/filehistory/filetypes/' + importWindow.fileTypeValue + '/formats',
						autoLoad: false
					},
					listeners: {
						change: function (field, newValue, oldvalue, opts) {
							var selectedType = field.up('form').getComponent('fileTypeCB').getValue();

							field.up('form').getComponent('fileFormatMappingCB').getStore().load({
								url: 'api/v1/resource/filehistory/formats/' + newValue + '/mappings'
							});
						}
					}
				}),
				Ext.create('OSF.component.StandardComboBox', {
					itemId: 'fileFormatMappingCB',
					name: 'dataMappingId',
					margin: '0 0 0 0',
					editable: false,
					typeAhead: false,
					width: '100%',
					fieldLabel: 'Data Mapping',
					storeConfig: {
						url: 'api/v1/resource/lookuptypes/FileType',
						autoLoad: false
					}
				}),
				{
					xtype: 'combobox',
					name: 'dataSource',
					editable: false,
					typeAhead: false,
					width: '100%',
					fieldLabel: 'Data Source',
					valueField: 'code',
					displayField: 'description',					
					labelAlign: 'top',	
					labelSeparator: '',
					emptyText: 'Select',
					queryMode: 'remote',
					store: {
						field: [
							'code',
							'description'
						],
						listeners: {
							load: function(myStore){
								myStore.add([{
									code: null,
									description: 'Select'
								}]);
							}
						},
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/DataSource'
						}
					}
				},			
				{
					xtype: 'filefield',
					name: 'uploadFile',
					width: '100%',
					labelAlign: 'top',
					labelSeparator: '',
					allowBlank: false,
					fieldLabel: 'Import <span class="field-required" />',
					buttonText: 'Select File...',
					listeners: {
						change: CoreUtil.handleMaxFileLimit
					}
				},
				{
					xtype: 'fieldset',
					itemId: 'optionPanel',
					title: 'Options',
					layout: 'card',
					items: [
						{
							xtype: 'panel',
							itemId: 'optionPanel-NONE'
						},
						{
							xtype: 'panel',
							itemId: 'optionPanel-SYSTEM'
						},
						{
							xtype: 'panel',
							itemId: 'optionPanel-ATTRIBUTE'
						},
						{
							xtype: 'panel',
							itemId: 'optionPanel-COMPONENT',
							items: [
								{
									xtype: 'checkboxfield',
									name: 'componentUploadOptions.uploadReviews',
									boxLabel: 'Upload Reviews'
								},
								{
									xtype: 'checkboxfield',
									name: 'componentUploadOptions.uploadQuestions',
									boxLabel: 'Upload Questions'
								},
								{
									xtype: 'checkboxfield',
									name: 'componentUploadOptions.uploadTags',
									boxLabel: 'Upload Tags'
								},
								{
									xtype: 'checkboxfield',
									name: 'componentUploadOptions.uploadIntegration',
									boxLabel: 'Upload Integration'
								},
								{
									xtype: 'checkboxfield',
									name: 'componentUploadOptions.skipRequiredAttributes',
									checked: true,
									boxLabel: 'Skip Required Attributes'
								}
							]
						}
					]
				}

			]
		});

		importWindow.add(importWindow.formPanel);
		importWindow.formPanel.getComponent('fileTypeCB').setValue(importWindow.fileTypeValue);

	}


});


