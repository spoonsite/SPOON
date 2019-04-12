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

 /* Author: cyearsley */

/* global Ext, CoreService */

Ext.define('OSF.customSubmission.field.AttributesGrid', {
	extend: 'OSF.customSubmission.SubmissionBaseGrid',	
	alias: 'widget.osf-submissionform-attributegrid',
	requires: [
		'OSF.customSubmission.form.Attributes'
	],
	
	title: '',
	fieldType: 'ATTRIBUTE_MULTI',
	
	columns: [
		{ text: 'Type', dataIndex: 'typeDescription', flex: 1, minWidth: 200 },
		{ text: 'Code', dataIndex: 'codeDescription', flex: 2, minWidth: 200 },
		{ text: 'Unit', dataIndex: 'unit', flex: 1, minWidth: 200 }
	],
	
	initComponent: function () {
		var grid = this;
		grid.callParent();	
		CoreService.attributeservice.warmCache();
		
		if (grid.section) {
			var initialData = grid.section.submissionForm.getFieldData(grid.fieldTemplate.fieldId);
			if (initialData) {
				var data = Ext.decode(initialData);				
				//separate hidden and required from editable
				grid.hiddenAttributes = [];
				var editable = [];
				Ext.Array.each(data, function(attributeView){
					if (attributeView.hideOnSubmission) {
						grid.hiddenAttributes.push(attributeView);
					} else {
						//check required
						var required = false;
						if (attributeView.requiredRestrictions) {
							Ext.Array.each(attributeView.requiredRestrictions, function(restriction){
								if (restriction.componentType === grid.componentType.componentType) {
									required = true;
								}
							});
						}
						
						if (required) {
							grid.hiddenAttributes.push(attributeView);
						} else{
							editable.push(attributeView);
						}
					}
				});
				
				grid.getStore().loadData(editable);				
			}			
		}		
	},
	
	actionAddEdit: function(record) {
		var grid = this;
		
		var addEditWin = Ext.create('Ext.window.Window', {
			title: 'Add/Edit Attribute',
			modal: true,
			width: 800,
			height: 255,
			closeMode: 'destroy',
			layout: 'fit',
			alwaysOnTop: 99,
			items: [
				{
					xtype: 'osf-submissionform-attribute',
					itemId: 'form',
					scrollable: true,
					originalRecord: record,
					componentType: grid.componentType,
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
										data.typeDescription = CoreService.attributeservice.translateType(data.type);
										data.codeDescription = CoreService.attributeservice.translateCode(data.type, data.code);
										
										if (record) {
											record.set(data, {
												dirty: false
											});
										} else {
											grid.getStore().add(data);
										}
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
			addEditWin.queryById('form').initLoadRecord = function(){
				addEditWin.queryById('form').loadRecord(record);
			};
		}
	},
	
	showOnEntryType: function() {
		var grid = this;		
		return grid.componentType.dataEntryAttributes || false;		
	},
	getUserData: function() {
		var grid = this;
		
		var data = [];
		
		var allAttributes = [];
		grid.getStore().each(function(record){
			allAttributes.push(record.getData());
		});
		
		//Skip hidden as they can 
//		if (grid.hiddenAttributes) {
//			allAttributes = allAttributes.concat(grid.hiddenAttributes);
//		}
		Ext.Array.each(allAttributes, function(dataItem){
			data.push(Ext.apply(dataItem, {				
				componentAttributePk: {
					attributeType: dataItem.type,
					attributeCode: dataItem.code
				}
			}));
		});	
		
		var userSubmissionField = {			
			templateFieldId: grid.fieldTemplate.fieldId,
			rawValue: Ext.encode(data)
		};		
		return userSubmissionField;			
	}
	
});
