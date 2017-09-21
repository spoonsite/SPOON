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

Ext.define('OSF.form.ChecklistQuestion', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.ChecklistQuestion',

	scrollable: true,
	listeners: {
		close: function(panel, opts) {
			if (panel.saveTask) {
				panel.saveTask.cancel();
			}
		}
	},	
	dockedItems: [
		{
			xtype: 'toolbar',
			itemId: 'tools',
			dock: 'bottom',
			items: [		
				{
					xtype: 'combo',
					itemId: 'workflowStatus',
					name: 'workflowStatus',										
					labelAlign: 'right',												
					margin: '0 0 5 0',
					editable: false,
					typeAhead: false,
					width: 400,
					fieldLabel: 'Status <span class="field-required" />',	
					displayField: 'description',
					valueField: 'code',
					labelSeparator: '',
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/WorkflowStatus'
						}
					}			
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Save',
					itemId: 'saveBtn',
					iconCls: 'fa fa-lg fa-save icon-button-color-save',
					handler: function() {
						var questionForm = this.up('panel');
						questionForm.saveData();
					}
				},				
				{
					xtype: 'tbtext',
					itemId: 'status'
				}				
			]
		}
	],
	initComponent: function () {		
		this.callParent();
		
		var questionForm = this;
		
		questionForm.response = Ext.create('Ext.panel.Panel',{
			bodyStyle: 'padding: 20px;',
			layout: 'anchor',
			
			defaults: {
				width: '100%',
				labelAlign: 'right'
			},
			dockedItems: [
				{
					xtype: 'panel',
					dock: 'top',
					itemId: 'question',
					title: 'Question',					
					animCollapse: false,
					collapsible: true,
					titleCollapse: true,
					bodyStyle: 'background: white; padding: 20px;',									
					tpl: new Ext.XTemplate(						
						'<div class="checklist-question">{question}</div>',
						'({evaluationSectionDescription})<br>',
						'<tpl if="scoreCriteria"><h3 class="checklist-question-sectionheader">Scoring criteria:</h3>',
						'{scoreCriteria}</tpl>',
						'<tpl if="objective"><h3 class="checklist-question-sectionheader">Objective:</h3>',
						'{objective}</tpl>',
						'<tpl if="narrative"><h3 class="checklist-question-sectionheader">Narrative:</h3>',
						'{narrative}</tpl>'
					)
				}
			],
			items: [
				{
					xtype: 'combobox',
					itemId: 'score',
					name: 'score',
					fieldCls: 'eval-form-field',
					labelClsExtra: 'eval-form-field-label',					
					fieldLabel: 'Score',
					margin: '0 0 0 -25',					
					width: 175,
					valueField: 'code',
					displayField: 'description',
					editable: false,
					typeAhead: false,
					store: {
						data: [
							{ code: '1', description: '1' },
							{ code: '2', description: '2' },
							{ code: '3', description: '3' },
							{ code: '4', description: '4' },
							{ code: '5', description: '5' }
						]
					}				
				},
				{
					xtype: 'checkbox',
					itemId: 'notApplicable',
					name: 'notApplicable',
					boxLabel: 'Not Applicable'
				},
				{
					xtype: 'panel',						
					html: '<b>Response</b>'
				},
				{
					xtype: 'tinymce_textarea',	
					itemId: 'response',
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: { border: '0' },
					height: 250,
					width: '100%',
					name: 'response',			
					maxLength: 32000,
					tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")						
				},
				{
					xtype: 'panel',						
					html: '<b>Private Notes</b>'
				},
				{
					xtype: 'tinymce_textarea',
					itemId: 'privateNote',
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: { border: '0' },
					height: 250,
					width: '100%',
					name: 'privateNote',			
					maxLength: 32000,
					tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")					
				},
				{
					xtype: 'checkbox',
					name: 'privateFlg',
					boxLabel: 'Private <i class="fa fa-question-circle" data-qtip="Hides when published"></i>',
					listeners: {
						change: function(field, newValue, oldValue, opts) {
							questionForm.markUnsaved();	
						}
					}					
				}
			]
		});
	
		questionForm.add(questionForm.response);
		
		questionForm.saveTask = new Ext.util.DelayedTask(function(){
			questionForm.saveData();
		});
	},
	loadData: function(evaluationId, componentId, data, opts) {
		
		var questionForm = this;
		
		questionForm.response.getComponent('question').setTitle("Question - " + data.question.qid);
		
		questionForm.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/evaluations/' + evaluationId + '/checklist/' + data.checklistId + '/responses/' + data.responseId,
			callback: function() {
				questionForm.setLoading(false);
			}, 
			success: function(response, localOpts) {
				Ext.defer(function(){
					var responseData = Ext.decode(response.responseText);
					var questionFormComponentExists = true;

					// Check and see if the form component exists.
					try {
						questionForm.response.getComponent('question');
						questionForm.getComponent('tools').getComponent('workflowStatus');
						questionForm.response.getComponent('notApplicable');
						questionForm.response.getComponent('score');
						questionForm.response.getComponent('response');
						questionForm.response.getComponent('privateNote');
					} catch (e) {
						questionFormComponentExists = false;
					}
					
					if (questionFormComponentExists) {
						questionForm.response.getComponent('question').update(responseData.question);

						var record = Ext.create('Ext.data.Model', {			
						});
						record.set(responseData);	

						questionForm.loadRecord(record);
						questionForm.evaluationId = evaluationId;
						questionForm.checklistResponse = responseData;

						if (opts && opts.mainForm) {
							questionForm.refreshCallback = opts.mainForm.refreshCallback;
						}		

					//Add change detection
						questionForm.getComponent('tools').getComponent('workflowStatus').on('change', function(field, newValue, oldValue){
							questionForm.saveData();
						}, undefined, {
							buffer: 1000
						});

						questionForm.response.getComponent('score').on('change', function(field, newValue, oldValue){
							questionForm.markUnsaved();
						}, undefined, {
							buffer: 1000
						});
						
						questionForm.response.getComponent('notApplicable').on('change', function(field, newValue, oldValue){
							var scoreField = questionForm.response.getComponent('score');
							if (newValue) {
								scoreField.setValue(null);
								scoreField.setDisabled(true);
							} else {
								scoreField.setDisabled(false);								
							}
							questionForm.markUnsaved();	
						}, undefined, {
							buffer: 1000
						});					

						questionForm.response.getComponent('response').on('change', function(field, newValue, oldValue){
							questionForm.markUnsaved();
						}, undefined, {
							buffer: 2000
						});

						questionForm.response.getComponent('privateNote').on('change', function(field, newValue, oldValue){
							questionForm.markUnsaved();
						}, undefined, {
							buffer: 2000
						});					
					}
				}, 100);
			}
		});
				
		opts.commentPanel.loadComments(evaluationId, "Checklist Question - " + data.question.qid, data.question.questionId);
	},
	markUnsaved: function () {
		var questionForm = this;
		questionForm.saveTask.delay(1000*60*3);	
		
		if (!questionForm.unsavedChanges) {
			questionForm.getComponent('tools').getComponent('status').setText('<span style="color: red; font-weight: bold;">Unsaved Changes</span>');		
			questionForm.unsavedChanges = true;
		}
		
	},	
	saveData: function() {
		var questionForm = this;
		
		var data = questionForm.getValues();
		
		questionForm.saveTask.cancel();
		
		questionForm.getComponent('tools').getComponent('saveBtn').setLoading("Saving...");
		CoreUtil.submitForm({
			url: 'api/v1/resource/evaluations/' + 
				questionForm.evaluationId 
				 + '/checklist/' + 
				 questionForm.checklistResponse.checklistId
				 + '/responses/' + 
				questionForm.checklistResponse.responseId,
			method: 'PUT',
			data: data,
			form: questionForm,
			noLoadmask: true,
			callback: function(form, action) {
				questionForm.getComponent('tools').getComponent('saveBtn').setLoading(false);
			},			
			success: function(action, opts) {
				var chkResponse = Ext.decode(action.responseText);
				
				Ext.toast('Saved Response');
				questionForm.getComponent('tools').getComponent('status').setText('Saved at ' + Ext.Date.format(new Date(), 'g:i:s A'));
				questionForm.unsavedChanges = false;
				
				if (questionForm.refreshCallback) {
					questionForm.refreshCallback(chkResponse);
				}
			}	
		});
			
	}
	
});

