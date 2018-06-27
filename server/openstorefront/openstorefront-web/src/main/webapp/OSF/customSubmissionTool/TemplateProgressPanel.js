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

Ext.define('OSF.customSubmissionTool.TemplateProgressPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-csf-templateprogresspanel',

	title: 'Mappable Fields',
	layout: 'fit',

	initComponent: function () {

		this.callParent();
		var templateProgressPanel = this;
				
		var items = [
			{
				xtype: 'grid',	
				itemId: 'grid',
				border: true,
				columnLines: true,
				columns: [
					{text: 'mapped', dataIndex: 'mapped', align: 'center', width: 75,
						renderer: function(value) {
							if (value) {
								return '<i style="color:green;" class="fa fa-check" aria-hidden="true"></i>';
							} else {
								return '<i style="color:red;" class="fa fa-close" aria-hidden="true"></i>';
							}
						}
					},
					{text: 'Entity ', dataIndex: 'entity', flex: 6, hidden: true},
					{text: 'Field', dataIndex: 'field', flex: 6},
					{
						text: 'Required',
						dataIndex: 'required',
						align: 'center',
						flex: 4,
						renderer: function (value) {
							if (value) {
								return '<i style="color:red;" class="fa fa-check" aria-hidden="true"></i>';
							}
							return '';
						}
					},								
					{
						text: 'Eligible Fields',
						dataIndex: 'fieldTypes',
						flex: 5,
						renderer: function (fieldArray) {

							// should be an array of strings - field types.
							var iconString = '';
							if (typeof fieldArray === 'object' && fieldArray.length) {
								Ext.Array.forEach(fieldArray, function (field, index) {
									switch (field) {
										case 'textfield':
											iconString += '<i class="fa fa-minus" aria-hidden="true" data-qtip="Short Answer"></i>&nbsp;&nbsp;';
											break;

										case 'textarea':
											iconString += '<i class="fa fa-align-left" aria-hidden="true" data-qtip="Long Answer"></i>&nbsp;&nbsp;';
											break;

										case 'date':
											iconString += '<i class="fa fa-calendar" aria-hidden="true" data-qtip="Calendar"></i>&nbsp;&nbsp;';
											break;

										case 'radio':
											iconString += '<i class="fa fa-dot-circle-o" aria-hidden="true" data-qtip="Radio"></i>&nbsp;&nbsp;';
											break;

										case 'checkbox':
											iconString += '<i class="fa fa-check-square-o" aria-hidden="true" data-qtip="Checkbox"></i>&nbsp;&nbsp;';
											break;

										case 'dropdown':
											iconString += '<i class="fa fa-chevron-circle-down" aria-hidden="true" data-qtip="Dropdown"></i>&nbsp;&nbsp;';
											break;

										case 'grid':
											iconString += '<i class="fa fa-table" aria-hidden="true" data-qtip="Grid"></i>&nbsp;&nbsp;';
											break;

										default:

									}
								});
							}

							return iconString;
						}
					}
				],
				features: [{ ftype: 'grouping' }],
				store: Ext.create('Ext.data.Store', {
					sortInfo: { field: "required", direction: "ASC" },
					groupField: 'entity',					
					fields: ['nameMapping', 'required']
				})
			}
		];
		templateProgressPanel.add(items);
		templateProgressPanel.updateTemplateProgress();
		
	},
	getMappableFieldForName: function(fieldName) {
		var templateProgressPanel = this;
		
		var foundRecord;
		
		var grid = templateProgressPanel.queryById('grid');		
		grid.getStore().each(function(item){
			if (item.get('field') === fieldName) {
				foundRecord = item;
			}
		});
		return foundRecord;
	},	
	updateTemplateProgress: function(){
		//assumes the template has been set.
		var templateProgressPanel = this;
		
		var updateProgress = function() {
			var grid = templateProgressPanel.queryById('grid');
			
			grid.getStore().each(function(record){
				record.set('mapped', false, {
					dirty: false
				});
			});
			
			grid.getStore().each(function(record){
				Ext.Array.each(templateProgressPanel.templateRecord.sections, function(section) {
					Ext.Array.each(section.fields, function(field) {
						if (record.get('field') === field.fieldName) {
							record.set('mapped', true, {
								dirty: false
							});
						} 
					});								
				});
				
			});
			
		};
		
		if (!templateProgressPanel.componentMeta) {
			
			templateProgressPanel.setLoading(true);
			Ext.Ajax.request({
				url: 'api/v1/service/metadata/Component',
				callback: function() {
					templateProgressPanel.setLoading(false);
				},
				success: function(response, opts) {
					templateProgressPanel.componentMeta = Ext.decode(response.responseText);
					
					var fields = [];
					Ext.Array.each(templateProgressPanel.componentMeta.fieldModels, function(model){
						
						var consumeField = false;
						Ext.Array.each(model.constraints, function(constraint) {
							if (constraint.name === 'ConsumeField') {
								consumeField = true;
							}
						});
						
						if (consumeField && 
								model.name !== 'componentType' &&
								model.name !== 'approvalState' &&
								model.name !== 'changeApprovalMode' &&
								model.name !== 'dataSource' &&
								model.name !== 'securityMarkingType' &&
								model.name !== 'dataSensitivity' &&
								model.name !== 'externalId' &&
								model.name !== 'guid' &&
								model.name !== 'notifyOfApprovalEmail'
							) {
							var required = false;
							Ext.Array.each(model.constraints, function(constraint) {
								if (constraint.name === 'NotNull') {
									required = true;
								}
							});
							
							var mapped = false;							
							Ext.Array.each(templateProgressPanel.templateRecord.sections, function(section) {
								Ext.Array.each(section.fields, function(field) {
									if (model.name === field.fieldName) {
										mapped = true;
									}
								});								
							});
							
							
							var fieldTypes = [];
							if (model.type === 'String') {
								fieldTypes.push('textfield');
								fieldTypes.push('textarea');
							} else if (model.type === 'Date') {
								fieldTypes.push('date');								
							}							
							
							fields.push({
								entity: 'Component',
								field: model.name,
								required: required,
								mapped: mapped,
								fieldTypes: fieldTypes 
							});
						}
						
					});	
					
					templateProgressPanel.queryById('grid').getStore().add(fields);
				}
			});
			
		} else {
			updateProgress();
		}
		
	},

	removeMapping: function(mapping){
		var templateProgressPanel = this;
		var grid = templateProgressPanel.queryById('grid');
		var record = grid.getStore().queryRecords('field',mapping)[0];
		record.set('mapped', false, {
			dirty: false
		});
	},

	getAvaliableMappableFields: function() {
		var templateProgressPanel = this;
		
		var records = [];		
		templateProgressPanel.queryById('grid').getStore().each(function(record){
			if (!record.get('mapped')) {
				records.push(record.copy());
			}
		});
		
		return records;
	}
	
});
