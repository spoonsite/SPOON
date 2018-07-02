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
/* global Ext, CoreUtil */

Ext.define('OSF.component.ImportWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.ImportWindow',
	title: 'Import Data',
	iconCls: 'fa fa-lg fa-upload icon-small-vertical-correction',
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
							iconCls: 'fa fa-lg fa-upload icon-button-color-default',
							requiredPermissions: ['ADMIN_DATA_IMPORT_EXPORT'],
							handler: function () {
								var uploadForm = this.up('form');
								var data = uploadForm.getValues();
								var selectedFormat = uploadForm.getComponent('fileFormatCB').getSelection();
								//check 
								if (selectedFormat.get('supportsDataMap') && !data.dataMappingId) {
									uploadForm.getComponent('fileFormatCB').markInvalid('A Data Mapping is required for this format.');									
									Ext.Msg.show({
										title:'Validation',
										message: 'A Data Mapping is required for this format.',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR,
										fn: function(btn) {        
										}
									});	
								} else {
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
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Cancel',
							iconCls: 'fa fa-lg fa-close icon-button-color-warning',
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
				Ext.create('OSF.component.DataSourceComboBox', {			
					name: 'dataSource',
					hideOnNoData: true,
					width: '100%'
				}),
				{
					xtype: 'fileFieldMaxLabel',
					resourceLabel: 'Import',
					itemId: 'upload',
					buttonText: 'Select File...',
					name: 'uploadFile',
					width: '100%',
					labelAlign: 'top',
					allowBlank: false
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


