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

Ext.define('OSF.form.ChecklistAll', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.ChecklistAll',
	
	layout: 'fit',
	initComponent: function () {		
		this.callParent();
		
		var questionForm = this;
	   
		questionForm.questionGrid = Ext.create('Ext.grid.Panel', {								
			columnLines: true,
			store: {
				fields: [
					{ name: 'qid', mapping: function(data) {
						return data.question.qid;
					}},
					{ name: 'evaluationSectionDescription', mapping: function(data) {
						return data.question.evaluationSectionDescription;
					}},
					{ name: 'questionText', mapping: function(data) {
						return data.question.question;
					}},
					{ name: 'scoreCriteria', mapping: function(data) {
						return data.question.scoreCriteria;
					}},
					{ name: 'objective', mapping: function(data) {
						return data.question.objective;
					}},
					{ name: 'narrative', mapping: function(data) {
						return data.question.narrative;
					}}					
				],
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/evaluations/{evaluationId}/checklist/{checklistId}/responses'
				}
			},
			listeners: {
				selectionchange: function(selModel, records, index, opts){
					var tools = questionForm.questionGrid.getComponent('tools');
					if (records.length > 0) {
						tools.getComponent('edit').setDisabled(false);
					} else {
						tools.getComponent('edit').setDisabled(true);
					}
				},
				rowdblclick:  function(table, record, tr, rowIndex, e, eOpts) {
					actionEdit(record);
				} 
			},
			bufferedRenderer: false,
			viewConfig: {
				enableTextSelection: true
			},				
			columns: [
				{ text: 'QID', dataIndex: 'qid', align: 'center', width: 100 },
				{ text: 'Status', dataIndex: 'workflowStatus', align: 'center',
					renderer: function(value, meta, record) {
						if (value === 'COMPLETE') {
							meta.tdCls = 'alert-success';							
						} else if (value === 'INPROGRESS') {
							meta.tdCls = 'alert-info';							
						} else if (value === 'HOLD') {
							meta.tdCls = 'alert-danger';							
						} else if (value === 'WAIT') {
							meta.tdCls = 'alert-warning';							
						}
						
						return record.get('workflowStatusDescription');
					}
				},
				{ text: 'Section', dataIndex: 'evaluationSectionDescription', align: 'center', width: 175 },
				{ text: 'Question', dataIndex: 'questionText', flex: 1, minWidth: 250, cellWrap: true },
				{ text: 'Scoring Criteria', dataIndex: 'scoreCriteria', width: 250, cellWrap: true, hidden: true },
				{ text: 'Objective', dataIndex: 'objective', width: 250, cellWrap: true, hidden: true },
				{ text: 'Narrative', dataIndex: 'narrative', width: 250, cellWrap: true, hidden: true },
				{
					text: 'Answer',
					columns: [
						{ text: 'Score', dataIndex: 'score', align: 'center', width: 100, 
							renderer: function(value, metaData, record) {	
								if (record.get('notApplicable')) {
									metaData.tdStyle = 'background: gray; color: white;';
									return '<br>NA';
								} else{
									metaData.tdStyle = "font-size: 2em;";
									return value ? '<br>' + value : value;
								}
							}
						},
						{ text: 'Response', dataIndex: 'response', width: 250, cellWrap: true },
						{ text: 'Private Notes', dataIndex: 'privateNote', width: 250, cellWrap: true }							
					]
				},
				{ text: 'Private', dataIndex: 'privateFlg', width: 170, align: 'center', hidden: true,
					renderer: function(value, meta) {
						if (value) {
							meta.tdCls = 'alert-danger';
							return '<i class="fa fa-lg fa-eye-slash"></i>';
						}
					}
				}
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					itemId: 'tools',
					dock: 'top',
					items: [
						{
							text: 'Edit',
							itemId: 'edit',
							iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
							scale: 'medium',
							width: '100px',
							disabled: true,
							handler: function() {
								var record = questionForm.questionGrid.getSelectionModel().getSelection()[0];
								actionEdit(record);
							}
						}
					]
				}
			]
		});
		
		var actionEdit = function(record) {
			
			var editWin = Ext.create('Ext.window.Window', {
				title: "Edit Response - " + record.get('qid'),
				modal: true,
				alwaysOnTop: true,
				layout: 'fit',
				closeAction: 'destroy',
				width: '70%',
				height: '60%',
				maximizable: true,
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'bottom',
						items: [
							{
								text: 'Save',
								iconCls: 'fa fa-2x fa-save icon-button-color-save icon-vertical-correction-save',
								width: '100px',
								scale: 'medium',
								handler: function() {
									var form = editWin.getComponent('form');
									var data = form.getValues();
									
									//save then update record
									CoreUtil.submitForm({
										url: 'api/v1/resource/evaluations/' + 
											questionForm.evaluationId 
											 + '/checklist/' + 
											record.get('checklistId')
											 + '/responses/' + 
											record.get('responseId'),
										method: 'PUT',
										data: data,
										form: form,
										success: function(action, opts) {
											var chkResponse = Ext.decode(action.responseText);
											
											Ext.toast('Saved Response');
											record.set(chkResponse, {
												dirty: false
											});
											if (!chkResponse.notApplicable) {
												record.set({
													notApplicable: null
												}, {
													dirty: false
												});
											}
											if (questionForm.refreshCallback) {
												questionForm.refreshCallback(chkResponse);
											}											
											
											editWin.close();
										}	
									});									
								}
							}, 
							{
								xtype: 'tbfill'
							},
							{
								text: 'Cancel',
								iconCls: 'fa fa-2x fa-close text-danger icon-vertical-correction',
								scale: 'medium',
								handler: function() {
									editWin.close();
								}								
							}
						]
					}
				],
				items: [					
					{
						xtype: 'form',
						itemId: 'form',
						bodyStyle: 'padding: 20px;',
						layout: 'anchor',
						scrollable: true,
						defaults: {
							width: '100%',
							labelAlign: 'right'
						},
						items: [
							{
								bodyStyle: 'font-size: 19px; line-height: 1.25em;',
								html: record.get('questionText')
							},
							{
								title: 'Scoring Criteria',
								itemId: 'scoreCriteria',
								collapsible: true,
								titleCollapse: true,
								collapsed: true,
								hidden: true,
								margin: '0 0 10 0',
								html: record.get('scoreCriteria')
							},
							{
								xtype: 'hidden',
								name: 'responseId'
							},
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
								name: 'notApplicable',
								boxLabel: 'Not Applicable',
								listeners: {
									change: function(field, newValue, oldValue){
										var scoreField = field.up('form').getComponent('score');
										if (newValue) {
											scoreField.setValue(null);
											scoreField.setDisabled(true);
										} else {
											scoreField.setDisabled(false);										
										}
									}
								}
							},							
							{
								xtype: 'panel',	
								dock: 'top',
								html: '<b>Response</b>'
							},
							{
								xtype: 'tinymce_textarea',						
								dock: 'top',
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
								dock: 'top',
								html: '<b>Private Notes</b>'
							},
							{
								xtype: 'tinymce_textarea',						
								dock: 'top',
								fieldStyle: 'font-family: Courier New; font-size: 12px;',
								style: { border: '0' },
								height: 250,
								width: '100%',
								name: 'privateNote',			
								maxLength: 32000,
								tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")			
							},
							{
								xtype: 'combo',
								itemId: 'workflowStatus',
								name: 'workflowStatus',																		
								margin: '0 0 5 0',
								editable: false,
								typeAhead: false,								
								fieldLabel: 'Status <span class="field-required" />',	
								displayField: 'description',
								valueField: 'code',
								labelSeparator: '',
								labelAlign: 'top',
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/lookuptypes/WorkflowStatus'
									}
								}			
							},
							{
								xtype: 'checkbox',
								name: 'privateFlg',
								boxLabel: 'Private <i class="fa fa-question-circle" data-qtip="Hides when published"></i>'													
							}							
						]
					}
				]
				
			});
			editWin.show();
			editWin.getComponent('form').loadRecord(record);
			if (record.get('scoreCriteria')) {
				editWin.queryById('scoreCriteria').setHidden(false);
			}
			
		};
	
		questionForm.add(questionForm.questionGrid);
		
	},
	loadData: function(evaluationId, componentId, data, opts) {
		
		var questionForm = this;
				
		questionForm.chkListData = data;
		questionForm.evaluationId = evaluationId;
		questionForm.questionGrid.getStore().load({
			url: 'api/v1/resource/evaluations/' + questionForm.evaluationId + '/checklist/' + data.evaluationChecklist.checklistId + '/responses'
		});
		
		if (opts && opts.mainForm) {
			questionForm.refreshCallback = opts.mainForm.refreshCallback;
		}		
		
		opts.commentPanel.loadComments(evaluationId, "Checklist All", evaluationId);	
	}
	
});


