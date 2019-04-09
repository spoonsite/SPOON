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
/**
 * This is use to display and attribute code selection.
 * It supports all the features. 
 * 
 */

/* global Ext */
Ext.define('OSF.common.AttributeCodeSelect', {
	extend: 'Ext.container.Container',
	alias: 'widget.AttributeCodeSelect',
		
	/**
	 * Applies to underlying field
	 */	
	fieldConfig: {},
	
	/**
	 * @required 
	 */
	attributeTypeView: null,
	required: true,
	layout: 'hbox',	
	showLabel: true,
	
	initComponent: function () {
		this.callParent();
		var attributePanel = this;

		if (attributePanel.attributeTypeView) {
			
			attributePanel.createField(attributePanel.attributeTypeView);
			
		} 
	},
	
	createField: function(attributeTypeView) {
		var attributePanel = this;
		
		attributePanel.attributeTypeView = attributeTypeView;
		attributePanel.removeAll();
		
		//support: multiple, numbers, user-codes			
		var requireType = attributePanel.required ? ' <span class="field-required" />' : '';

		var forceSelection=true;
		var editable=false;
		var typeAhead=false;					
		if (attributePanel.attributeTypeView.allowUserGeneratedCodes) {
			forceSelection=false;
			editable=true;
			typeAhead=true;	
		}			
		
		var numberVType = attributePanel.attributeTypeView.attributeValueType === 'NUMBER' ? 'AttributeNumber' : undefined;			
			
		var extraCfg = {};
		var xtype = 'Ext.form.field.ComboBox';
		if (attributePanel.attributeTypeView.allowMultipleFlg) {
			xtype = 'Ext.form.field.Tag';			
			
			
			var validator = undefined;
			if (numberVType){			
				validator = function(valueRaw) {
					var values = attributePanel.getValue();	
					var valid = true;
					var msg = ''
					
					Ext.Array.each(values, function(value) {
						if (attributePanel.attributeTypeView.attributeValueType === 'NUMBER') {			
							//check precision; this will enforce max allowed
							validatorResponse = CoreUtil.validateNumber(value);
							valid = validatorResponse.valid;
							msg = validatorResponse.msg;
						}
					});	
					return valid ? valid : msg;	
				};
			} 
			numberVType = undefined;
			
			extraCfg = {
				createNewOnEnter: true,
				createNewOnBlur: true,
				validator: validator
			};
		} 

		attributePanel.field = Ext.create(xtype, Ext.apply({
				fieldLabel: attributePanel.showLabel ? attributePanel.attributeTypeView.description + requireType : '',
				forceSelection: forceSelection,
				name: attributePanel.name ? attributePanel.name : 'attributeCode',
				queryMode: 'local',
				vtype: numberVType,
				selectOnFocus: false,
				editable: editable,
				typeAhead: typeAhead,
				filterPickList: true,
				margin: '0 0 5 0',
				flex: 3,
				allowBlank: !attributePanel.required,					
				labelWidth: 300,
				labelSeparator: '',
				valueField: 'code',
				displayField: 'label',
				listeners: attributePanel.fieldListeners,
				store: Ext.create('Ext.data.Store', {
					data: attributePanel.attributeTypeView.codes
				}),
				listConfig: {
					getInnerTpl: function () {
						return '{label} <tpl if="description"><i class="fa fa-question-circle" data-qtip=\'{description}\'></i></tpl>';
					}
				}
			}, attributePanel.fieldConfig, extraCfg));

		if (attributePanel.attributeUnit && attributePanel.attributeUnitList) {
			var unitList = attributePanel.attributeUnitList;
			var baseUnit = {
				"conversionFactor": 1,
				"unit": attributePanel.attributeUnit,
				"description": "Default Unit"
			};

			// remove the base unit if in the list
			unitList = unitList.filter(function(el) { return el.unit !== baseUnit.unit })
			unitList.push(baseUnit);

			var processList = function(list) {
				var data = [];
				Ext.Array.forEach(list, function(el) {
					data.push({
						'value': el.unit,
						'text': el.unit,
						'conversionFactor': el.conversionFactor,
						'description': el.description
					})
				})
				return data;
			}

			var storeList = processList(unitList);
			attributePanel.unit = Ext.create('Ext.form.field.ComboBox',{
					allowBlank: false,
					editable: false,
					margin: '0 0 5 0',
					queryMode: 'local',
					flex: 1,
					store: Ext.create('Ext.data.Store', {
						fields: [
							'value',
							'text',
							'conversionFactor',
							'description'
						],
						data: storeList
					}),
					listConfig: {
						getInnerTpl: function () {
							return '{text} <tpl if="description"><i class="fa fa-question-circle" data-qtip=\'{description}\'></i></tpl>';
						}
					}
				}
			)

			// set the default unit value
			// Will later get replaced in AttributeSuggested.js
			// if a preferred unit is found.
			// If no code is found, the base unit will be set for the user.
			attributePanel.unit.setValue(baseUnit.unit);
		}

		attributePanel.add(attributePanel.field);

		if (attributePanel.unit
		    && attributePanel.attributeUnitList
			&& (attributePanel.attributeUnitList.length === 1
				|| attributePanel.attributeUnitList.length === 0 
			)
		) {
			attributePanel.add(Ext.create('Ext.panel.Panel', {
				html: baseUnit.unit,
				style: 'padding-left: 10px;',
				flex: 1
			}))

		} else if (attributePanel.unit) {
			attributePanel.add(attributePanel.unit);
		}
		
	},
	
	getField: function() {
		var attributePanel = this;
		return attributePanel.field;
	},

	getUnit: function() {
		var attributePanel = this;
		if (attributePanel.unit) {
			return attributePanel.unit.getValue();
		} else {
			return null;
		}
	},

	setUnit: function(unit) {
		var attributePanel = this;
		if (attributePanel.unit) {
			attributePanel.unit.setValue(unit);
		}
	},

	getConversionFactor: function() {
		var attributePanel = this;
		if (attributePanel.unit) {
			return attributePanel.unit.getSelectedRecord().data.conversionFactor;
		} else {
			return null;
		}
	},
	
	getSelection: function() {
		var attributePanel = this;
		return attributePanel.field.getSelection();
	},	
	
	/**
	 * Get the value of the files
	 * @param {boolean} convertNumbers 
	 * @returns Array of codes; regardless of underlying type
	 */
	getValue: function(convertNumbers) {
		var attributePanel = this;
		if (attributePanel.field) {
			var values = [];
			if (Ext.isArray(attributePanel.field.getValue())) {
				values = attributePanel.field.getValue();
			} else {
				values.push(
					attributePanel.field.getValue()
				);				
			}			
			if (convertNumbers) {
				var newValues = [];
				Ext.Array.each(values, function(value){
					if (Ext.isNumber(value)) {
						newValues.push(value.toString());
					} else {
						newValues.push(value);
					}
				});
				newValues = values;
			}
			return values;
		} else {
			return null;
		}
	},
	setValue: function(value) {
		var attributePanel = this;
		if (attributePanel.field) {	
			return attributePanel.field.setValue(value);
		}		
	},
	valid: function(form) {
		var valid = true;
		var msg = '';
		var attributePanel = this;
				
		var values = attributePanel.getValue();
		
		Ext.Array.each(values, function(value){
			if (attributePanel.attributeTypeView.attributeValueType === 'NUMBER') {			
				//check precision; this will enforce max allowed
				var validatorResponse = CoreUtil.validateNumber(value);
				valid = validatorResponse.valid;
				msg = validatorResponse.msg;
				if (!valid) {
					form.getForm().markInvalid({
						attributeCode: msg
					});
				}
			}
		});
		
		return valid;
	}

});
	