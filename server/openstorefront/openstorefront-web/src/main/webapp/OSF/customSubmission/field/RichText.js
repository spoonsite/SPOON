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

/* global Ext, CoreUtil */

Ext.define('OSF.customSubmission.field.RichText', {
	extend: 'Ext.panel.Panel',	
	xtype: 'osf-submissionform-richtext',
	
	width: '100%',
	maxWidth: 800,
	layout: 'fit',
	
	fieldTemplate: {
		fieldType: null,
		mappingType: 'COMPONENT',
		questionNumber: null,
		label: null,
		labelTooltip: null,
		required: null
	},	
	
	initComponent: function () {
		var fieldPanel = this;
		fieldPanel.callParent();
			
		fieldPanel.label = Ext.create('Ext.panel.Panel', {
			dock: 'top',		
			html: fieldPanel.createQuestionLabel()
		});	
			
		fieldPanel.textArea = Ext.create('Ext.ux.form.TinyMCETextArea', {					
			fieldStyle: 'font-family: Courier New; font-size: 12px;',
			style: { border: '0' },			
			name: 'description',
			allowBlank: fieldPanel.fieldTemplate.required ? false : true,
			height: 250,
			maxLength: 65536,
			tinyMCEConfig: Ext.apply(CoreUtil.tinymceSearchEntryConfig("osfmediaretriever"), {
//					mediaSelectionUrl: function(){					
//						return 'api/v1/resource/components/' + entryForm.componentId + '/media/view';					
//					}
			})	
		});	
			
		fieldPanel.addDocked(fieldPanel.label);	
			
		fieldPanel.add([		
			fieldPanel.textArea
		]);
		
		var initialData = fieldPanel.section.submissionForm.getFieldData(fieldPanel.fieldTemplate.fieldId);
		if (initialData) {
			fieldPanel.textArea.setValue(initialData);
		}		
		
	},
	
	reviewDisplayValue: function() {
		var textField = this;
		var value = textField.textArea.getValue();	
		return (value && value !== '') ? value : '(No Data Entered)';					
	},
	
	isValid : function() {
		var textField = this;
		return textField.textArea.isValid();
	},
	getUserData: function() {
		var textField = this;
		
		var userSubmissionField = {			
			templateFieldId: textField.fieldTemplate.fieldId,
			rawValue: textField.textArea.getValue()
		};		
		return userSubmissionField;			
	}
		
});
