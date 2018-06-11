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

Ext.define('OSF.customSubmission.form.Resources', {
	extend: 'OSF.customSubmission.SubmissionBaseForm',
	xtype: 'osf-submissionform-resource',
	
	layout: 'anchor',
	bodyStyle: 'padding: 10px',
	fieldType: 'RESOURCE',
	localResource: false,
	
	defaults: {
		width: '100%',
		maxWidth: 800,
		labelAlign: 'top',
		labelSeparator: ''		
	},
	
	initComponent: function () {
		this.callParent();
		
		var resourcePanel = this;

		resourcePanel.add([
			{
				xtype: 'StandardComboBox',
				itemId: 'resourceType',
				name: 'resourceType',
				allowBlank: false,
				margin: '0 0 15 0',
				editable: false,
				typeAhead: false,
				fieldLabel: 'Resource Type: <span class="field-required" />',
				storeConfig: {
					url: 'api/v1/resource/lookuptypes/ResourceType'
				}
			},
			{
				xtype: 'textfield',
				labelAlign: 'top',
				fieldLabel: 'Description',
				maxLength: '255',				
				name: 'description'
			},
			{
				xtype: 'checkbox',
				name: 'restricted',
				boxLabel: '<strong>Restricted</strong>'
			},
			{
				xtype: 'button',
				margin: '0 0 15 0',
				text: 'External Link',
				menu: [
					{
						text: 'External Link',
						handler: function () {
							var form = this.up('form');
							var button = this.up('button');
							button.setText('External Link');
							resourcePanel.localResource = false;
							
							form.getForm().findField('file').setHidden(true);
							form.getForm().findField('originalLink').setHidden(false);
						}
					},
					{
						text: 'Local Resource',
						handler: function (btn) {
							var form = this.up('form');
							var button = this.up('button');
							button.setText('Local Resource');
							resourcePanel.localResource = true;
							
							form.getForm().findField('file').setHidden(false);
							form.getForm().findField('originalLink').setHidden(true);
						}
					}
				]
			},
			{
				xtype: 'textfield',
				fieldLabel: 'Link',
				maxLength: '255',
				emptyText: 'http://www.example.com/resource',
				labelAlign: 'top',
				name: 'originalLink',
				colName: 'externalLink'
			},
			{
				xtype: 'fileFieldMaxLabel',
				itemId: 'upload',
				name: 'file',
				colName: 'filePath',	
				labelAlign: 'top',
				hidden: true
			},
			{
				xtype: 'SecurityComboBox'			
			},
			{
				xtype: 'DataSensitivityComboBox'
			}
		]);
		
		if (resourcePanel.section && resourcePanel.fieldTemplate) {
			var initialData = resourcePanel.section.submissionForm.getFieldData(resourcePanel.fieldTemplate.fieldId);
			if (initialData) {
				var data = Ext.decode(initialData);
				var record = Ext.create('Ext.data.Model', {				
				});
				record.set(data[0]);
				resourcePanel.loadRecord(record);			
			}			
		}		
		
		if (resourcePanel.section) {
			if (!resourcePanel.section.submissionForm.userSubmission) {
				resourcePanel.previewMode = true;			
			} else {
				resourcePanel.userSubmissionId = resourcePanel.section.submissionForm.userSubmission.userSubmissionId;
			}
		} else {
			resourcePanel.previewMode = true;
		}		
		
	},
	handleUpload: function() {
		var resourcePanel = this;
		
		if (resourcePanel.previewMode && resourcePanel.localResource) {			
			Ext.Msg.show({
				title:'Preview Mode',
				message: 'Unable to upload file in preview mode.',
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				fn: function(btn) {
				}
			});			
		} else if (resourcePanel.localResource && !resourcePanel.previewMode) {
			
			//upload file
			
			
		}
	},	
	getSubmissionValue: function() {
		var resourcePanel = this;
		
		//Add handling of the local resources
		
		var data = resourcePanel.getValues();
		
		var userSubmissionField = {			
			templateFieldId: resourcePanel.fieldTemplate.fieldId,
			rawValue: Ext.encode([
				data
			])
		};		
		return userSubmissionField;		
	}

});
