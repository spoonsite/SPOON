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

/* global Ext */

Ext.define('OSF.customSubmission.field.ResourceSimple', {
	extend: 'Ext.panel.Panel',
	xtype: 'osf-submissionform-resourcesimple',
	
	layout: 'hbox',
	width: '100%',
	maxWidth: 800,
	
	fieldType: 'RESOURCE_SIMPLE',
	
	initComponent: function () {
		this.callParent();
		
		var resourcePanel = this;
		
		if (resourcePanel.fieldTemplate.allowPrivateResource) {
			resourcePanel.setMaxWidth(860);
		}

		var labelData = {
			question:  resourcePanel.createQuestionLabel(),
			uploadedFile: ''
		};
		
		resourcePanel.label = Ext.create('Ext.panel.Panel', {		
			flex: 1,
			margin: '0 10 0 0',
			data: labelData,
			tpl: new Ext.XTemplate(
				'{question}',
				'<div>{uploadedFile}</div>'
			)
		});	

		resourcePanel.add([
			resourcePanel.label,
			{
				xtype: 'panel',
				layout: 'hbox',
				items: [
					{
						xtype: 'button',
						text: 'Upload',
						width: 100,
						handler: function() {
							resourcePanel.uploadWindow();
						}
					},
					{
						xtype: 'checkbox',
						margin: '0 0 0 5',						
						hidden: resourcePanel.fieldTemplate.allowPrivateResource ? false : true,
						boxLabel: 'Private'						
					}
				]
			}
			
		]);
	
	},
	
	uploadWindow: function() {
		//prompt for upload
		
		var uploadWindow = Ext.create('Ext.window.Window', {
			title: 'Upload File',
			modal: true,
			width: 500,
			height: 200,
			layout: 'fit',
			items: [
				{
					xtype: 'form',
					scrollable: true,
					layout: 'anchor',
					bodyStyle: 'padding: 10px;',
					items: [
						{
							xtype: 'fileFieldMaxLabel',
							itemId: 'upload',
							width: '100%',
							allowBlank: false,
							name: 'file',
							labelAlign: 'top'
						}						
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Upload',
									formBind: true,
									iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
									handler: function () {
										
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Cancel',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
									handler: function () {
										this.up('window').close();												
									}
								}								
							]
						}
					]
				}
			]
		});
		
		uploadWindow.show();
		
	},
	
	reviewDisplayValue: function() {
		var field = this;
		var value = field.uploadedFile;
		return (value && value !== '') ? value : '(No Data Entered)';	
	},
	
	isValid: function() {
		var field = this;
		
		if (field.fieldTemplate.required) {
			return field.uploadedFile;
		} else {
			return true;
		}
	}
	
});


