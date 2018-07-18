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
	
	style: 'background: #fff',
	title: 'Workplan Config',
	width: 300,

	items: [
		{
			xtype: 'form',
			itemId: 'workplanForm',
			padding: 10,
			items: [
				{
					xtype: 'textfield',
					name: 'name',
					fieldLabel: 'Workplan Name <span class="field-required" />',
					allowBlank: false,
					labelAlign: 'top',
					width: '100%',
					style: 'border-bottom: 1px solid #ccc; padding-bottom: 15px;'
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
					name: 'changeRequestedColor',
					style: 'margin-left: 10%;',
					width: 20,
					value: '#ff0000'
				},
				{
					xtype: 'combo',
					fieldLabel: 'Workplan For <span class="field-required" />',
					valueField: 'value',
					displayField: 'name',
					labelAlign: 'top',
					style: 'border-top: 1px solid #ccc;',
					allowBlank: false,
					width: '100%',
					store: {
						fields: ['name', 'value'],
						data: [
							{ name: 'Entry Type', value: 'ENTRYTYPE' },
							{ name: 'Evaluation', value: 'EVAL' }
						]
					},
					listeners: {
						change: function (oldVal, newVal) {
							var entryTypeCombo = this.up().down('[itemId=entryTypeCombo]').show();
							if (newVal === 'ENTRYTYPE') {
								entryTypeCombo.show();
							}
							else {
								entryTypeCombo.hide();
							}
						}
					}
				},
				{
					xtype: 'combo',
					itemId: 'entryTypeCombo',
					fieldLabel: 'Entry Type <span class="field-required" />',
					valueField: 'value',
					displayField: 'name',
					labelAlign: 'top',
					style: 'margin-top: 20px',
					allowBlank: false,
					hidden: true,
					store: {
						fields: ['name', 'value'],
						data: [
							{ name: 'Entry Type #1', value: 'ENTRYTYPE1' },
							{ name: 'Entry Type #2', value: 'ENTRYTYPE2' },
							{ name: 'Entry Type #3', value: 'ENTRYTYPE3' },
							{ name: 'Entry Type #4', value: 'ENTRYTYPE4' },
							{ name: 'Entry Type #5', value: 'ENTRYTYPE5' },
						]
					}
				}
			]
		}
	],

	alertChange: function () {

		var workplanForm = this.down('[itemId=workplanForm]');
		workplanForm.getForm().setValues(this.getWpWindow().getWorkplanConfig());
	}
});
