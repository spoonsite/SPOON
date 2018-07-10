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

Ext.define('OSF.customSubmission.field.Number', {
	extend: 'Ext.form.field.Number',	
	xtype: 'osf-submissionform-number',
	
	width: '100%',
	maxWidth: 800,	
	labelAlign: 'top',
	
	fieldTemplate: {
		fieldType: null,
		mappingType: 'COMPONENT',
		questionNumber: null,
		label: null,
		labelTooltip: null,
		required: null
	},	
	
	initComponent: function () {
		var numberField = this;
		numberField.callParent();
			
		if (numberField.fieldTemplate.required) {
			numberField.setConfig({
					allowBlank: false
			});				
		}				
			
		numberField.setFieldLabel(numberField.createQuestionLabel());
		
		var initialData = numberField.section.submissionForm.getFieldData(numberField.fieldTemplate.fieldId);
		if (initialData) {
			numberField.setValue(initialData);
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

