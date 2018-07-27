/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 /* Author: cyearsley */
/* global Ext, CoreUtil, CoreService, data */

Ext.define('OSF.workplanManagementTool.WPFormPanel', {
	extend: 'OSF.workplanManagementTool.WPDefaultPanel',
	alias: 'widget.osf.wp.form',
	
	style: 'background: #fff; border-bottom: 1px solid #ececec',
	title: 'Workplan Config',
	width: 300,

	items: [
		{
			xtype: 'form',
			itemId: 'workplanForm',
			padding: 10,
			defaults: {
				listeners: {
					change: function (field, newVal, oldVal) {

						field.up('window').getWorkplanConfig()[field.name] = newVal;
					}
				}
			},
			items: [
				{
					xtype: 'textfield',
					name: 'name',
					fieldLabel: 'Workplan Name <span class="field-required" />',
					allowBlank: false,
					labelAlign: 'top',
					width: '100%'
				},
				{
					xtype: 'checkbox',
					fieldLabel: 'Use Default Work Plan',
					name: 'defaultWorkPlan',
					labelAlign: 'left',
					style: 'border-bottom: 1px solid #ccc; padding-bottom: 15px;',
					width: '100%',
					labelWidth: '80%'
				},
				{
					xtype: 'label',
					html: '<span style="font-size: 13px; font-weight: bold;">Status Colors</span>'
				},
				{
					xtype: 'colorfield',
					format: '#hex6',
					fieldLabel: 'Pending',
					name: 'pendingColor',
					style: 'margin-left: 10%;',
					width: 20,
					value: '#cccccc'
				},
				{
					xtype: 'colorfield',
					format: '#hex6',
					fieldLabel: 'In Progress',
					name: 'inProgressColor',
					style: 'margin-left: 10%;',
					width: 20,
					value: '#777aea'
				},
				{
					xtype: 'colorfield',
					format: '#hex6',
					fieldLabel: 'Complete',
					name: 'completeColor',
					style: 'margin-left: 10%;',
					width: 20,
					value: '#84d053'
				},
				{
					xtype: 'colorfield',
					format: '#hex6',
					fieldLabel: 'Attention Required',
					name: 'subStatusColor',
					style: 'margin-left: 10%;',
					width: 20,
					value: '#ff0000'
				},
				{
					xtype: 'combo',
					name: 'workPlanType',
					fieldLabel: 'Workplan For <span class="field-required" />',
					valueField: 'code',
					displayField: 'description',
					labelAlign: 'top',
					style: 'border-top: 1px solid #ccc;',
					allowBlank: false,
					width: '100%',
					queryMode: 'local',
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/WorkPlanType'
						},
						fields: ['code', 'description']
					},
					listeners: {
						change: function (oldVal, newVal) {
							var entryTypeCombo = this.up().down('[itemId=entryTypeCombo]').show();
							var childrenComponentTypesCheckbox = this.up().down('[itemId=childrenComponentTypesCheckbox]').show();
							if (newVal === 'COMPONENT') {
								entryTypeCombo.show();
								childrenComponentTypesCheckbox.show();
							}
							else {
								entryTypeCombo.hide();
								childrenComponentTypesCheckbox.hide();
							}
						}
					}
				},
				{
					xtype: 'checkbox',
					fieldLabel: 'Applies to Child Component Types',
					name: 'appliesToChildComponentTypes',
					itemId: 'childrenComponentTypesCheckbox',
					hidden: true,
					labelAlign: 'left',
					width: '100%',
					labelWidth: '80%'
				},
				{
					xtype: 'EntryTypeMultiSelect',
					name: 'componentTypes',
					itemId: 'entryTypeCombo',
					fieldLabel: 'Entry Type <span class="field-required" />',
					valueField: 'code',
					displayField: 'description',
					labelAlign: 'top',
					style: 'margin-top: 20px',
					allowBlank: false,
					hidden: true,
					width: '100%',
					queryMode: 'local',
					autoLoad: true
				}
			]
		}
	],

	alertChange: function () {

		var workplanForm = this.down('[itemId=workplanForm]');
		workplanForm.getForm().setValues(this.getWpWindow().getWorkplanConfig());
	}
});
