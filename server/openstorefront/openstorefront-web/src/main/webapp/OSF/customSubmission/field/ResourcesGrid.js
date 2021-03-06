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
/* global Ext, CoreUtil, CoreService */

/* Author: cyearsley */

Ext.define('OSF.customSubmission.field.ResourcesGrid', {
	extend: 'OSF.customSubmission.SubmissionBaseGrid',
	xtype: 'osf-submissionform-resourcegrid',
	requires: [
		'OSF.customSubmission.form.Resources'
	],
	
	title: '',
	fieldType: 'RESOURCE_MULTI',
	
	columns: [
		{ text: 'Resource Type', dataIndex: 'resourceTypeDesc', width: 200 },
		{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200 },
		{ text: 'Original Link', dataIndex: 'originalLink', width: 200 },		
		{ text: 'Local Name', dataIndex: 'originalFileName', width: 200 },
		{ text: 'Restricted', dataIndex: 'restricted', width: 200 },
		{ text: 'Security Marking', dataIndex: 'securityMarking', width: 200, hidden: true },
		{ text: 'Data Sensitivity', dataIndex: 'dataSensitivity', width: 200, hidden: true }
	],
	
	initComponent: function () {
		var grid = this;
		grid.callParent();	
		
		if (grid.section) {
			var initialData = grid.section.submissionForm.getFieldData(grid.fieldTemplate.fieldId);
			if (initialData) {
				var data = Ext.decode(initialData);				
				grid.getStore().loadData(data);
			}			
		}
		
	},	
	
	actionAddEdit: function(record) {
		var grid = this;
		
		var addEditWin = Ext.create('Ext.window.Window', {
			title: 'Add/Edit Resource',
			modal: true,
			width: 800,
			height: 600,
			alwaysOnTop: 99,
			closeMode: 'destroy',
			layout: 'fit',
			items: [
				{
					xtype: 'osf-submissionform-resource',
					itemId: 'form',
					scrollable: true,
					originalRecord: record,
					section: grid.section,
					fieldId: grid.fieldTemplate.fieldId,
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
										form.handleUpload(data,  function(){
											data.resourceTypeDesc = form.queryById('resourceType').getSelection().get('description');
											data.originalFileName = form.originalFileName;
											data.link = data.originalLink;
										
											if (record) {
												record.set(data, {
													dirty: false
												});
											} else {
												grid.getStore().add(data);
											}
										});
										
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
		
		if (record) {
			addEditWin.queryById('form').loadRecord(record);
		}		
		
	},
	
	showOnEntryType: function() {
		var grid = this;		
		return grid.componentType.dataEntryResources || false;		
	},	
	getUserData: function() {
		var grid = this;
		
		var data = [];
		grid.getStore().each(function(record){
			data.push(record.getData());
		});
		
		var userSubmissionField = {			
			templateFieldId: grid.fieldTemplate.fieldId,
			rawValue: Ext.encode(data)
		};		
		return userSubmissionField;			
	}		
	
});