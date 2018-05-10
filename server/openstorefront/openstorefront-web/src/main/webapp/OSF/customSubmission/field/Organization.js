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
	editable: true,
	queryMode: 'remote',
	store: {				
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
		field.setFieldLabel(field.createQuestionLabel());
		
	},
	
	reviewDisplayValue: function() {
		var textField = this;
		return textField.getValue();	
	}	
	
});

