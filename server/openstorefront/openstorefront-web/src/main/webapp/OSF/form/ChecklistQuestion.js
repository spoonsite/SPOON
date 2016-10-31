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
		
		var response = Ext.create('Ext.panel.Panel',{
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
					title: 'Question',
					animCollapse: false,
					collapsible: true,
					titleCollapse: true,
					bodyStyle: 'padding: 20px;',				
					data: {
						question: "Can the asset be found?",
						scoringCriteria: '5 - Easily found using common search engines<br>3 - Find by name and some key words<br>1 - Word of mouth only.',
						objective: 'If users are able to find the asset the chances of reuse go up.',
						narrative: 'Check google with common terms'
					},
					tpl: new Ext.XTemplate(						
						'<h1>{question}</h1>',
						'<h3>Scoring criteria:</h3>',
						'{scoringCriteria}',
						'<h3>Objective:</h3>',
						'{objective}',
						'<h3>Narrative:</h3>',
						'{narrative}'
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
					name: 'privateNotes',			
					maxLength: 32000,
					tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")			
				}				
			]
		});
	
		questionForm.add(response);
	}
	
});

