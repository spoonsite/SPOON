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

Ext.define('OSF.customSubmission.field.ContactsGrid', {
	extend: 'OSF.customSubmission.SubmissionBaseGrid',
	alias: 'widget.osf-submissionform-contactgrid',
	requires: [
		'OSF.customSubmission.form.Contacts'
	],
	
	title: '',
	fieldType: 'CONTACT_MULTI',
	
	columns: [
		{ text: 'Contact Type', dataIndex: 'contactTypeLabel', width: 200 },
		{ text: 'Organization', dataIndex: 'organization', width: 200 },
		{ text: 'First Name', dataIndex: 'firstName', width: 200 },
		{ text: 'Last Name', dataIndex: 'lastName', width: 200 },
		{ text: 'Email', dataIndex: 'email', width: 200 },
		{ text: 'Phone', dataIndex: 'phone', flex: 1, minWidth: 150 },
		{ text: 'Security Marking', dataIndex: 'securityMarking', width: 200, hidden: true },
		{ text: 'Data Sensitivity', dataIndex: 'dataSensitivity', width: 200, hidden: true }
	],
	
	initComponent: function () {
		var grid = this;
		grid.callParent();	
		
		if (grid.fieldTemplate.contactType) {
			Ext.Array.each(grid.getColumns(), function(column) {
				if (column.dataIndex === 'contactTypeLabel') {
					column.setHidden(true);
				}
			});
		}
		
	},	
	
	actionAddEdit: function(record) {
		var grid = this;
		
		var addEditWin = Ext.create('Ext.window.Window', {
			title: 'Add/Edit Contact',
			modal: true,
			width: 800,
			height: 600,
			closeMode: 'destroy',
			layout: 'fit',
			items: [
				{
					xtype: 'osf-submissionform-contact',
					itemId: 'form',
					fieldTemplate: grid.fieldTemplate,
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
										
										var contactTypeField = form.queryById('contactType');
										if (contactTypeField) {
											data.contactTypeLabel = contactTypeField.getSelection()[0].get('description');
										}
										
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
		
		if (record) {
			addEditWin.queryById('form').loadRecord(record);
		}		
		
	},
	
	showOnEntryType: function() {
		var grid = this;		
		return grid.componentType.dataEntryContacts || false;		
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
