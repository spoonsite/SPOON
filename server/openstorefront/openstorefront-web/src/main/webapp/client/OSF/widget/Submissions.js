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

Ext.define('OSF.widget.Submissions', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.Submissions',

	layout: 'fit',
	
	initComponent: function () {
		this.callParent();
		
		var submissionPanel = this;
		
		submissionPanel.grid = Ext.create('Ext.grid.Panel', {	
			columnLines: true,
			store: {
				sorters: [{
					property: 'name',
					direction: 'ASC'
				}],						
				autoLoad: true,
				fields: [
					{
						name: 'submittedDts',
						type:	'date',
						dateFormat: 'c'
					},
					{
						name: 'submitApproveDts',
						type:	'date',
						dateFormat: 'c',
						mapping: function(data) {
							if (data.approvalState === 'A') {
								return data.approvedDts;
							} else {
								return data.submittedDts;
							}
						}
					}
				],
				proxy: {
					type: 'ajax',
					url: '../api/v1/resource/componentsubmissions'
				}
			},	
			columns: [
				{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 200 },								
				{ text: 'Status', align: 'center', dataIndex: 'approvalState', width: 200,
					renderer: function(value, metaData){
						var text = value;
						if (value === 'A') {
							text = 'Approved';
							metaData.tdCls = 'alert-success';
						} else if (value === 'P') {
							text = 'Pending';
							metaData.tdCls = 'alert-warning';
						} else if (value === 'N') {
							text = 'Not Submitted';
						}
						return text;
					}
				},
				{ text: 'Submit/Approve Date', align: 'center', dataIndex: 'submitApproveDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },				
				{ text: 'Pending Change', align: 'center', dataIndex: 'statusOfPendingChange', width: 150 }	
			]
		});	
		
		submissionPanel.add(submissionPanel.grid);
		
	},
	refresh: function() {
		var submissionPanel = this;
		submissionPanel.grid.getStore().reload();
	}

});
