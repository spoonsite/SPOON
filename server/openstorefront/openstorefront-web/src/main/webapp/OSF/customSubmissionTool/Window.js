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
						submissionWindow.saveTemplate();						
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
									itemId: 'form',
									showCustomButton: true,
									hideSave: true,
									customButtonHandler: function() {
										previewWin.close();
									}											
								}
							]
						});
						previewWin.show();
						previewWin.queryById('form').load(submissionWindow.template, submissionWindow.template.entryType);						
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
						var csfWindow = this.up('window');
						csfWindow.close();
					}
				}				
			]
		}
	],
	listeners: {
		beforeclose: function(csfWindow, opts) {
			if (csfWindow.proceedWithClose){
				return true;
			} else {
				if (csfWindow.formBuilderPanel.hasChanges()) {
					Ext.Msg.show({
						title:'Save Changes?',
						message: 'Would you like to save your changes?',
						buttons: Ext.Msg.YESNOCANCEL,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								csfWindow.saveTemplate(function(){
									csfWindow.proceedWithClose = true;
									csfWindow.close();
								});
							} else if (btn === 'no') {
								csfWindow.proceedWithClose = true;
								csfWindow.close();
							} 
						}
					});
					return false;
				}
			}
		}
	},
	initComponent: function () {
		this.callParent();
		var csfWindow = this;

		if (!csfWindow.formBuilderPanel) {			
			csfWindow.formBuilderPanel = Ext.create('OSF.customSubmissionTool.FormBuilderPanel', {
				templateRecord: csfWindow.template,
				controlWindow: csfWindow
			});
		}

		csfWindow.add(csfWindow.formBuilderPanel);
	},
	saveTemplate: function(afterSave) {
		var submissionWindow = this;
		
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

			//set section order			
			if (submissionWindow.template.sections) {
				Ext.Array.forEach(submissionWindow.template.sections, function(section, index){
					section.sectionOrder = index;
					Ext.Array.forEach(section.fields, function(field, indexField) {
						field.fieldOrder = indexField;
					});					
				});
			}

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
					
					if (updatedTemplate.errors && updatedTemplate.success === false) {
						//read errors;
						var details = '';
						if (updatedTemplate.errors) {
							Ext.Array.each(updatedTemplate.errors.entry, function (item, index, entry) {
								details += item.key + ': ' + item.value + '<br>';
							});
						}
						
						Ext.Msg.show({
							title:'Save Error',
							message: 'Failed to Save; Check Input. <br><br> <b>Details:</b><br> '+ details ,
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR,
							fn: function(btn) {								
							}
						});						
						
					} else {

						var handleResponse = function () {

							submissionWindow.template.updateUser = updatedTemplate.updateUser;
							submissionWindow.template.updateDts = Ext.Date.parse(updatedTemplate.updateDts, 'c');
	
							//update all section id and field ids
							submissionWindow.formBuilderPanel.markAsSaved();
							submissionWindow.formBuilderPanel.updateTemplate(updatedTemplate);
							if (submissionWindow.saveCallback) {
								submissionWindow.saveCallback(updatedTemplate);
							}
							if (afterSave) {
								afterSave(updatedTemplate);
							}
						};

						if (updatedTemplate.activeStatus === 'A') {
							Ext.Ajax.request({
								url: 'api/v1/resource/submissiontemplates/' + updatedTemplate.submissionTemplateId + '/inactivate',
								method: 'PUT',
								success: function (response, opt) {
									handleResponse();
								}
							});
						}
						else {
							handleResponse();
						}
					}
				}
			});
		}		
		
	}

});
