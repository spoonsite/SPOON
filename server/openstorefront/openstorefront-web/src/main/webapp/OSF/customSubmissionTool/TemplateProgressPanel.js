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
Ext.define('OSF.customSubmissionTool.TemplateProgressPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-csf-templateprogresspanel',

	title: 'Template Progress (<i style="color:red;" class="fa fa-lg fa-close"></i> Incomplete)',
	layout: 'fit',

	initComponent: function () {

		this.callParent();
		var templateProgressPanel = this;
				
		var items = [
			{
				xtype: 'grid',	
				border: true,
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
							if (value === 'Required') {
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
					data: [
						{entity: 'Entry', field: 'Description', required: 'Required', mapped: true, fieldTypes: ['textarea','textfield']},									
						{entity: 'Entry', field: 'Entry Type', required: 'Required', mapped: true, fieldTypes: ['dropdown','radio','checkbox']},
						{entity: 'Entry', field: 'Name', required: 'Required', mapped: false, fieldTypes: ['textarea','textfield']},
						{entity: 'Entry', field: 'GUID', required: 'Not Required', mapped: false, fieldTypes: ['grid']},
						{entity: 'Entry', field: 'Version', required: 'Not Required', mapped: false, fieldTypes: ['checkbox', 'radio', 'grid']},
						{entity: 'Contact', field: 'First Name', required: 'Required', mapped: false, fieldTypes: ['radio','dropdown','checkbox']},
						{entity: 'Contact', field: 'Last Name', required: 'Required', mapped: true, fieldTypes: ['grid', 'radio']}
					],
					fields: ['nameMapping', 'required']
				})
			}
		];
		templateProgressPanel.add(items);
		
	}
	
	
});
