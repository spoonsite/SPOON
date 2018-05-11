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

Ext.define('OSF.customSubmission.field.Date', {
	extend: 'Ext.form.field.Date',	
	xtype: 'osf-submissionform-date',
	
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
		var field = this;
		field.callParent();
			
		if (field.fieldTemplate.required) {
			field.setConfig({
					allowBlank: false
			});				
		}			
			
		field.setFieldLabel(field.createQuestionLabel());
		
	},
	
	reviewDisplayValue: function() {
		var field = this;
		var value = field.getValue();
		return value && value !== '' ? value : '(No Data Entered)';	
	}	
	
	
});

