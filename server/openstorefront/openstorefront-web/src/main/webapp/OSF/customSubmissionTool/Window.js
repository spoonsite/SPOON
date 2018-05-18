/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
 /* Author: cyearsley */
/* global Ext, CoreUtil, CoreService, data */

Ext.define('OSF.customSubmissionTool.Window', {
	extend: 'Ext.window.Window',
	requires: [
		'OSF.customSubmissionTool.FormBuilderPanel',
		'OSF.customSubmissionTool.EntryTypeSelectWindow',
		'OSF.customSubmission.SubmissionFormFullControl'
	],

	config: {
		template: undefined
	},
	formBuilderPanel: undefined,

	maximizable: true,	
	width: '90%',
	height: '90%',
	
	modal: true,
	title: 'Form Builder Tool',
	iconCls: 'fa fa-edit',
	layout: 'fit',
	closeAction: 'destroy',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Save & Continue',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-save icon-button-color-save icon-vertical-correction',
					handler: function() {
						var submissionWindow = this.up('window');
						
						if (submissionWindow.template.name === '' || submissionWindow.template.description === ''){
							Ext.Msg.show({
								title:'Validation',
								message: 'Name and Description are required.',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR,
								fn: function(btn) {									
								}
							});
						} else {
						
							submissionWindow.setLoading("Saving...");
							Ext.Ajax.request({
								url: 'api/v1/resource/submissiontemplates',
								method: 'POST',
								jsonData: submissionWindow.template,
								callback: function(){
									submissionWindow.setLoading(false);
								},
								success: function(response, opts){
									var updatedTemplate = Ext.decode(response.responseText);
									Ext.toast('Saved Template Successfully');
																		
									submissionWindow.template.updateUser = updatedTemplate.updateUser;
									submissionWindow.template.updateDts = Ext.Date.parse(updatedTemplate.updateDts, 'c');
									
									//update all section id and field ids
									

									submissionWindow.formBuilderPanel.updateTemplate(updatedTemplate);
									if (submissionWindow.saveCallback) {
										submissionWindow.saveCallback(updatedTemplate);
									}
								}
							});
						}
						
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Preview',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction',
					handler: function() {
						var submissionWindow = this.up('window');
						
						var entryTypeSelect = Ext.create('OSF.customSubmissionTool.EntryTypeSelectWindow', {
							selectCallBack: function(entryType) {
								var previewWin = Ext.create('Ext.window.Window', {
									title: 'Preview',
									layout: 'fit',
									modal: true,
									closeAction: 'destroy',
									width: '80%',
									height: '80%',
									maximizable: true,
									items: [
										{
											xtype: 'osf-customSubmission-SubmissionformFullControl',
											itemId: 'form'							
										}
									]
								});
								previewWin.show();
								previewWin.queryById('form').load(submissionWindow.template, entryType);						
							}
						});
						entryTypeSelect.show();						
					}					
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Close',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
					handler: function() {						
						this.up('window').close();
					}
				}				
			]
		}
	],
	initComponent: function () {
		this.callParent();
		var csfWindow = this;

		if (!csfWindow.formBuilderPanel) {			
			csfWindow.formBuilderPanel = Ext.create('OSF.customSubmissionTool.FormBuilderPanel', {
				templateRecord: csfWindow.template
			});
		}

		csfWindow.add(csfWindow.formBuilderPanel);
	}

});
