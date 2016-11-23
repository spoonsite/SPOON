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

Ext.define('OSF.form.ChecklistSummary', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.ChecklistSummary',

	layout: 'border',

	initComponent: function () {		
		this.callParent();
		
		var summaryForm = this;
				
		summaryForm.topPanel = Ext.create('Ext.panel.Panel', {
			region: 'north',
			items: [
				{
					xtype: 'panel',	
					dock: 'top',
					html: '<b>Summary</b>'
				},
				{
					xtype: 'tinymce_textarea',						
					dock: 'top',
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: { border: '0' },
					height: 300,
					width: '100%',
					name: 'summary',			
					maxLength: 32000,
					tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")			
				}
			]
		});	
				
		summaryForm.recommendations = Ext.create('Ext.grid.Panel', {
			title: 'Recommendations',
			region: 'center',
			columnLines: true,
			store: {				
			},
			columns: [
				{ text: 'Type', dataIndex: 'recommendationTypeDescription', width: 200 },
				{ text: 'Recommendation', dataIndex: 'recommendation', flex: 1, minWidth: 200 },
				{ text: 'Reason', dataIndex: 'reason', width: 250 }
			],
			dockedItems: [	
				{
					xtype: 'toolbar',
					items: [
						{
							text: 'Add',
							iconCls: 'fa fa-2x fa-plus text-success',
							scale: 'medium',							
							handler: function() {
								actionAddEdit();
							}
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Edit',
							itemId: 'edit',
							disabled: true,
							iconCls: 'fa fa-2x fa-edit',
							scale: 'medium',							
							handler: function() {
								var record = summaryForm.recommendations.getSelectionModel().getSelection[0];
								actionAddEdit(record);
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Delete',
							itemId: 'delete',
							disabled: true,
							iconCls: 'fa fa-2x fa-close text-danger',
							scale: 'medium',							
							handler: function() {
								var record = summaryForm.recommendations.getSelectionModel().getSelection[0];								
								actionDelete(record);
							}
						}
					]
				}
			]			
		});
		summaryForm.add(summaryForm.topPanel);
		summaryForm.add(summaryForm.recommendations);
		
		var actionAddEdit = function(record) {
			
		};

		var actionDelete = function(record) {
			
		};
	},
	loadData: function(evaluationId, componentId, data, opts) {
		
		var summaryForm = this;
		
		var record = Ext.create('Ext.data.Model', {			
		});
		record.set(data.evaluationChecklist);
		summaryForm.loadRecord(record);		
		summaryForm.recommendations.getStore().loadData(data.recommendations);
		
		opts.commentPanel.loadComments(evaluationId, "Checklist Summary", data.evaluationChecklist.checklistId);	
	}
	
});
