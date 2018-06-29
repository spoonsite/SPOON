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

Ext.define('OSF.customSubmission.field.Organization', {
	extend: 'Ext.form.field.ComboBox',	
	xtype: 'osf-submissionform-organization',
	
	width: '100%',
	maxWidth: 800,	
	labelAlign: 'top',
	forceSelection: false,
	valueField: 'description',
	displayField: 'description',
	emptyText: 'Select or Type in',
	editable: true,
	typeAhead: true,
	queryMode: 'local',
	store: {	
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/organizations/lookup'
		},
		sorters: [{
			property: 'description',
			direction: 'ASC'
		}]
	},	
		
	fieldTemplate: {
		fieldType: null,
		mappingType: 'COMPONENT',
		questionNumber: null,
		label: null,
		labelTooltip: null,
		required: null
	},	
	
	initComponent: function () {
		var field = this;
		field.callParent();			
		
		if (field.fieldTemplate.required) {
			field.setConfig({
					allowBlank: false
			});				
		}		
		
		field.setFieldLabel(field.createQuestionLabel());
		
		var initialData = field.section.submissionForm.getFieldData(field.fieldTemplate.fieldId);
		if (initialData) {
			field.setValue(initialData);
		}		
		
	},
	
	reviewDisplayValue: function() {
		var field = this;
		var value = field.getValue();
		return (value && value !== '') ? value : '(No Data Entered)';	
	},	
	getUserData: function() {
		var field = this;
		
		var userSubmissionField = {			
			templateFieldId: field.fieldTemplate.fieldId,
			rawValue: field.getValue()
		};		
		return userSubmissionField;			
	}	
	
});

