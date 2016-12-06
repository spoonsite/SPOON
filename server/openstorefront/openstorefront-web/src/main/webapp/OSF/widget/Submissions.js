/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
					url: 'api/v1/resource/componentsubmissions'
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
