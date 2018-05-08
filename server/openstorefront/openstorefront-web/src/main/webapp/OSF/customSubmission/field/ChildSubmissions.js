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

Ext.define('OSF.customSubmission.field.ChildSubmissions', {
	extend: 'OSF.customSubmission.SubmissionBaseGrid',
	xtype: 'osf-submissionform-childsubmissions',
		
	title: '',
	fieldType: 'SUBMISSIONS',
	
	columns: [
		{ text: 'Entry Type', dataIndex: 'componentType', width: 200 },
		{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 200 }
	],
	
	initComponent: function () {
		var grid = this;
		grid.callParent();	
		
	},	
	
	actionAddEdit: function(record) {
		var grid = this;
		
		var addEditWin = Ext.create('Ext.window.Window', {
			title: 'Add/Edit Entry',
			modal: true,
			width: 800,
			height: 600,
			closeMode: 'destroy',
			maximizable: true,
			layout: 'fit',
			items: [
				{
					xtype: 'osf-customSubmission-SubmissionformFullControl',
					itemId: 'form',
					scrollable: true,
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Save',
									formBind: true,
									iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
									handler: function () {
										var form = this.up('form');
										var data = form.getValues();
										
										//get the name
										
										grid.getStore().add(data);
										this.up('window').close();
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
		addEditWin.show();
		
		//load the form template based on the type
		
		
		
		if (record) {
			addEditWin.queryById('form').loadRecord(record);
		}		
		
	}
	
	
});
