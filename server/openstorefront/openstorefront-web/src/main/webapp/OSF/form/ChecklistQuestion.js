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

Ext.define('OSF.form.ChecklistQuestion', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.ChecklistQuestion',

	scrollable: true,
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
						'<tpl if="scoringCriteria"><h3>Scoring criteria:</h3>',
						'{scoringCriteria}</tpl>',
						'<tpl if="objective"><h3>Objective:</h3>',
						'{objective}</tpl>',
						'<tpl if="narrative"><h3>Narrative:</h3>',
						'{narrative}</tpl>'
					)
				}
			],
			items: [
				{
					xtype: 'combobox',
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
					xtype: 'panel',						
					html: '<b>Response</b>'
				},
				{
					xtype: 'tinymce_textarea',											
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
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: { border: '0' },
					height: 250,
					width: '100%',
					name: 'privateNote',			
					maxLength: 32000,
					tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")			
				}				
			]
		});
	
		questionForm.add(questionForm.response);
	},
	loadData: function(evaluationId, componentId, data, opts) {
		
		var questionForm = this;
		
		questionForm.response.getComponent('question').update(data.question)
		
		var record = Ext.create('Ext.data.Model', {			
		});
		record.set(data);		
		questionForm.loadRecord(record);
		
		opts.commentPanel.loadComments(evaluationId, "Checklist Question - " + data.question.qid, data.question.questionId);
	}
	
});

