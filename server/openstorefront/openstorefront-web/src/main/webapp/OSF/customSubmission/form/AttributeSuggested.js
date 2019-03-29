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

/* global Ext, fieldPanel */

// copy of AttributeRequired.js
// TODO:
//     - fetch list of related attributes for entry type
//     - allow submitter to select preferred unit from list of compatible units
//     - allow submitter to add any other attribute like the optional attributes
//     - Request New Attribute from the admin if attribute not found in global list
Ext.define('OSF.customSubmission.form.AttributeSuggested', {
	extend: 'OSF.customSubmission.SubmissionBaseForm',	
	xtype: 'osf-submissionform-attributesuggested',
	requires: [
		'OSF.common.AttributeCodeSelect'
	],
	width: '100%',
	layout: 'anchor',
	
	initComponent: function () {
		var formPanel = this;
		formPanel.callParent();
		
		//load required for entry type and generate form
		formPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/attributes/optional?componentType=' + formPanel.componentType.componentType + '&submissionOnly=true',
			callback: function() {
				formPanel.setLoading(false);
			},
			success: function(response, opts) {
				var attributeTypes = Ext.decode(response.responseText);
				
				if (attributeTypes.length === 0) {
					//hide parent
					formPanel.up().setHidden(true);
				}
				
				var fields = [];
				Ext.Array.each(attributeTypes, function(attributeType){
				//TODO: update other spots that use the AttributeCodeSelect
				//      to use attribute units and the unit list
					fields.push({
						xtype: 'AttributeCodeSelect',
						required: false,
						attributeType: attributeType.attributeType,
						attributeTypeView: attributeType,
						attributeUnit: attributeType.attributeUnit,
						attributeUnitList: attributeType.attributeUnitList
					});										
				});
				formPanel.add(fields);
				formPanel.updateLayout(true, true);
				
				// rawValue
				var initialData = formPanel.section.submissionForm.getFieldData(formPanel.fieldTemplate.fieldId);
				if (initialData) {
					var data = Ext.decode(initialData);				
					
					//data from the initial load is not in the expected form					
					Ext.Array.each(data, function(item) {
						if (!item.componentAttributePk){
							item.componentAttributePk = {
								attributeType: item.type,
								attributeCode: item.code
							};
						}
					});
					
					//group values by type
					var typeGroup = {};
					var typeGroupUnit = {};
					Ext.Array.each(data, function(item) {
						if (typeGroup[item.componentAttributePk.attributeType]) {
							typeGroup[item.componentAttributePk.attributeType].push(item.componentAttributePk.attributeCode);
						} else {
							typeGroup[item.componentAttributePk.attributeType] = [];
							// all items in the group need to be in the same unit
							typeGroupUnit[item.componentAttributePk.attributeType] = item.preferredUnit;
							typeGroup[item.componentAttributePk.attributeType].push(item.componentAttributePk.attributeCode);
						}
					});
					
					//find field and set values
					Ext.Object.each(typeGroup, function(key, value){
						Ext.Array.each(formPanel.items.items, function(field){
							if (field.attributeType === key) {
								field.setValue(value * typeGroupUnit[key].conversionFactor); // multiply by conversion factor
								field.setUnit(typeGroupUnit[key].unit);
							}
						});
					});
				}	
			}
		});
		
		
	},
	reviewDisplayValue: function() {
		var attributePanel = this;
		
		var template = new Ext.XTemplate(
			'<table class="submission-review-table">' + 
			'<tbody>' + 
			'	<tpl for=".">'+
			'		<tr class="submission-review-row">' +
			'			<td class="submission-review-label">'+
			'				{label}' +
			'			</td>' +
			'			<td class="submission-review-data" style="min-width: 150px">' +
			'				{value}' +
			'			</td>' +
			'			<td class="submission-review-data" style="min-width: 150px">' +
			'				<tpl if="value">{unit}</tpl>' +
			'			</td>' +
			'		</tr>' +
			'	</tpl>'+
			'</tbody>' +
			'</table>'
		);

		var data = [];

		Ext.Array.each(attributePanel.items.items, function(field) {
			data.push({
				label: field.attributeTypeView.description,
				value: field.getField().getDisplayValue(),
				unit: field.getUnit()
			});
		});
		
		return template.apply(data);
	},
	getSubmissionValue: function() {
		var attributePanel = this;

		var data = [];
		Ext.Array.each(attributePanel.items.items, function(field) {
			
			var allValues = field.getValue();
			Ext.Array.each(allValues, function(value){
				if (value !== null) {
					data.push({
						componentAttributePk: {
							attributeType: field.attributeTypeView.attributeType,
							attributeCode: value / field.getConversionFactor() // normalize the value to save it
						},
						preferredUnit: { unit: field.getUnit(), conversionFactor: field.getConversionFactor() } // save the factor to restore the value
					});
				}
			});
		});
		console.log("DATA: ", data);
		
		var userSubmissionField = {			
			templateFieldId: attributePanel.fieldTemplate.fieldId,
			rawValue: Ext.encode(data)
		};		
		return userSubmissionField;			
	}
	
});
