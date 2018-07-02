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
	 * Applies to underly field
	 */	
	fieldConfig: {},
	
	/**
	 * @required 
	 */
	attributeTypeView: null,
	required: true,
	width: '100%',
	layout: 'fit',	
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
					
					Ext.Array.each(values, function(value) {
						if (attributePanel.attributeTypeView.attributeValueType === 'NUMBER') {			
							//check percision; this will enforce max allowed
							if (Ext.String.endsWith(value, ".")) {
								valid = 'Number must not have a decimal point or have at least one digit after the decimal point.';
							}
							try {
								var valueNumber = Number(value);
								if (isNaN(valueNumber)) {						
									valid = 'Value must be a valid number';
								}
							} catch (e) {
								valid = 'Number must not have a decimal point or have at least one digit after the decimal point.';
							}
						}
					});	
					return valid;	
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
				allowBlank: !attributePanel.required,					
				labelWidth: 300,
				labelSepartor: '',
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

		attributePanel.add(attributePanel.field);		
		
	},
	
	getField: function() {
		var attributePanel = this;
		return attributePanel.field;
	},
	
	getSelection: function() {
		var attributePanel = this;
		return attributePanel.field.getSelection();
	},	
	
	/**
	 * Get the value of the files
	 * @param {boolean} convertNumbers 
	 * @returns Array of codes; reguardless of unlying type
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
		var attributePanel = this;
				
		var values = attributePanel.getValue();
		
		Ext.Array.each(values, function(value){
			if (attributePanel.attributeTypeView.attributeValueType === 'NUMBER') {			
			    //check percision; this will enforce max allowed
				if (Ext.String.endsWith(value, ".")) {
					valid = false;
					form.getForm().markInvalid({
						attributeCode: 'Number must not have a decimal point or have at least one digit after the decimal point.'
					});
				}
				try {
					var valueNumber = Number(value);
					if (isNaN(valueNumber)) {						
						valid = false;
					}
				} catch (e) {
					valid = false;
					form.getForm().markInvalid({
						attributeCode: 'Number must not have a decimal point or have at least one digit after the decimal point.'
					});
				}
			}
		});
		
		return valid;
	}

});
	