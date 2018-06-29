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
/* global Ext, CoreUtil, CoreService */

/* Author: cyearsley */

Ext.define('OSF.customSubmission.form.Media', {
	extend: 'OSF.customSubmission.SubmissionBaseForm',
	xtype: 'osf-submissionform-media',
	
	layout: 'anchor',
	bodyStyle: 'padding: 10px',
	fieldType: 'MEDIA',
	localResource: true,

	defaults: {
		width: '100%',
		maxWidth: 800,
		labelAlign: 'top',
		labelSeparator: ''		
	},	
	
	initComponent: function () {
		this.callParent();
		
		var mediaPanel = this;

		mediaPanel.add([
			{
				xtype: 'StandardComboBox',
				itemId: 'mediaTypeCode',
				name: 'mediaTypeCode',				
				allowBlank: false,
				margin: '0 0 15 0',
				editable: false,
				typeAhead: false,
				fieldLabel: 'Media Type: <span class="field-required" />',
				labelAlign: 'left',
				storeConfig: {
					url: 'api/v1/resource/lookuptypes/MediaType'
				}
			},
			{
				xtype: 'textfield',
				fieldLabel: 'Caption <span class="field-required" />',
				labelAlign: 'left',
				allowBlank: false,
				maxLength: '255',
				name: 'caption'
			},
			{
				xtype: 'checkbox',
				boxLabel: '<strong>Hide In Carousel</strong>',
				name: 'hideInDisplay',
				colName: 'hideInCarousel'
			},
			{
				xtype: 'checkbox',
				boxLabel: '<strong>Used Inline</strong>',
				name: 'usedInline'
			},
			{
				xtype: 'checkbox',
				boxLabel: '<strong>Icon</strong> <i class="fa fa-question-circle"  data-qtip="Designates a media item to be used as an icon. There should only be one active on a entry at a time."></i>',
				name: 'iconFlag',
				colName: 'showIcon'
			},
			{
				xtype: 'button',
				text: 'Local Resource',
				margin: '0 0 15 0',
				menu: [
					{
						text: 'Local Resource',
						handler: function () {
							var form = this.up('form');
							var button = this.up('button');
							button.setText('Local Resource');
							mediaPanel.localResource = true;
							
							form.getForm().findField('file').setHidden(false);
							form.getForm().findField('originalLink').setHidden(true);
							
							form.query('[name="iconFlag"]')[0].setDisabled(false);
						}
					},
					{
						text: 'External Link',
						handler: function () {
							var form = this.up('form');
							var button = this.up('button');
							button.setText('External Link');
							mediaPanel.localResource = false;
							
							form.getForm().findField('file').setHidden(true);
							form.getForm().findField('originalLink').setHidden(false);
							
							form.query('[name="iconFlag"]')[0].setDisabled(true);
						}
					}
				]
			},
			{
				xtype: 'fileFieldMaxLabel',
				itemId: 'upload',
				name: 'file',
				colName: 'filePath',
				resourceLabel: 'Upload Media'
			},
			{
				xtype: 'textfield',
				fieldLabel: 'Link',
				hidden: true,
				maxLength: '255',
				emptyText: 'http://www.example.com/image.png',
				name: 'originalLink',
				colName: 'externalLink'
			},
			{
				xtype: 'SecurityComboBox'
			},
			{
				xtype: 'DataSensitivityComboBox'
			}
		]);
		
		if (mediaPanel.section && mediaPanel.fieldTemplate) {
			var initialData = mediaPanel.section.submissionForm.getFieldData(mediaPanel.fieldTemplate.fieldId);
			if (initialData) {
				var data = Ext.decode(initialData);
				var record = Ext.create('Ext.data.Model', {				
				});
				record.set(data[0]);
				mediaPanel.loadRecord(record);			
			}			
		}		
		
		if (mediaPanel.section) {
			if (!mediaPanel.section.submissionForm.userSubmission) {
				mediaPanel.previewMode = true;			
			} else {
				mediaPanel.userSubmissionId = mediaPanel.section.submissionForm.userSubmission.userSubmissionId;
			}
		} else {
			mediaPanel.previewMode = true;
		}
		
	},
	handleUpload: function(actualRecord, successCallback) {
		var mediaPanel = this;
		
		if (mediaPanel.previewMode && mediaPanel.localResource) {			
			Ext.Msg.show({
				title:'Preview Mode',
				message: 'Unable to upload file in preview mode.',
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				fn: function(btn) {
				}
			});			
		} else if (mediaPanel.localResource && !mediaPanel.previewMode) {
			
			//upload file
			
			var form = mediaPanel;
			var data = form.getValues();


			data.fileSelected = form.queryById('upload').getValue();
			data.link = data.originalLink;
			data.originalName = data.originalFileName;

			if (!data.originalFileName && ((!data.link && !data.fileSelected) || (data.link && data.fileSelected))) {

				form.getForm().markInvalid({
					file: 'Either a link or a file must be entered',
					originalLink: 'Either a link or a file must be entered'
				});

			} else {
				if (data.fileSelected) {
					//upload

					var progressMsg = Ext.MessageBox.show({
						title: 'Media Upload',
						msg: 'Uploading media please wait...',
						width: 300,
						height: 150,
						closable: false,
						progressText: 'Uploading...',
						wait: true,
						waitConfig: {interval: 300}
					});

					form.submit({
						url: 'Media.action?UploadSubmissionMedia',
						params: {
							'userSubmissionId': mediaPanel.userSubmissionId,
							'submissionTemplateFieldId': mediaPanel.fieldId
						},
						method: 'POST',
						submitEmptyText: false,
						success: function (form, action, opt) {
							progressMsg.hide();
						},
						failure: function (form, action, opt) {
							var data = Ext.decode(action.response.responseText);
							if (data.success && data.success === false){
								Ext.Msg.show({
									title: 'Upload Failed',
									msg: 'The file upload was not successful. Check that the file meets the requirements and try again.',
									buttons: Ext.Msg.OK
								});																					
							} else {
								//false positive the return object doesn't have success																
								Ext.toast('Uploaded Successfully', '', 'tr');													
								actualRecord.file = {
									mediaFileId: data.file.mediaFileId									
								};
								actualRecord.originalFileName = data.file.originalName;
								mediaPanel.originalFileName = data.file.originalName;
								
								if (successCallback) {
									successCallback();
								}
							}
							progressMsg.hide();
						}
					});
				}
			}
		} else {
			if (successCallback) {
				successCallback();
			}
		}
	},
	getSubmissionValue: function() {		
		var mediaPanel = this;
		
		//Add handling of the local resources
		
		var data = mediaPanel.getValues();
		
		var userSubmissionField = {			
			templateFieldId: mediaPanel.fieldTemplate.fieldId,
			rawValue: Ext.encode([
				data
			])
		};		
		return userSubmissionField;	
	}	
	
});
