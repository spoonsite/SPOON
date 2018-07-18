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

Ext.define('OSF.workplanManagementTool.StepFormPanel', {
	extend: 'OSF.workplanManagementTool.WPDefaultPanel',
	alias: 'widget.osf.wp.stepForm',

	style: 'background: #fff;',
	title: 'Step Configuration',
	layout: {
		type: 'vbox',
		pack: 'center',
		align: 'middle'
	},
	canSave: true,
	items: [
		{
			xtype: 'container',
			itemId: 'defaultContainer',
			style: 'border: 1px solid #ccc; border-radius: 5px; padding: 15px;',
			html: '<span style="font-size: 20px;">You can modify step configurations here<br />Select or create a step <b>above</b> to continue</span>',
			hidden: true
		},
		{
			xtype: 'form',
			itemId: 'stepFormPanel',
			hidden: true,
			style: 'text-align: left;',
			width: '100%',
			padding: '5%',
			scrollable: true,
			layout: {
				type: 'table',
				columns: 2
			},
			defaults: {
				labelAlign: 'top',
				width: '90%',
				canAlertOnChange: false,
				listeners: {
					change: function (field, newVal, oldVal) {

						var wpWindow = field.up('window');
						var stepForm = field.up('[itemId=stepFormPanel]').getForm();

						// save the current form
						if (wpWindow.stepForm.canSave) {
							Ext.apply(wpWindow.getSelectedStep(), stepForm.getValues());
						}
						if (field.canAlertOnChange) {
							wpWindow.stepForm.alert();
						}
					}
				}
			},
			items: [
				{
					xtype: 'textfield',
					fieldLabel: 'Step name (?)',
					name: 'name',
					canAlertOnChange: true
				},
				{
					xtype: 'combo',
					fieldLabel: 'Active On (?)',
					name: 'activeOn',
					width: '100%',
					editable: false
				},
				{
					xtype: 'textarea',
					fieldLabel: 'Short Description (?)',
					name: 'description'
				},
				{
					xtype: 'combo',
					fieldLabel: 'Role Access (?)',
					name: 'allowedRoles',
					width: '100%',
					editable: false,
					displayField: 'description',
					valueField: 'roleName',
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/securityroles'
						}
					}
				},
				{
					xtype: 'grid',
					title: 'Step Actions (?)',
					colspan: 2,
					width: '100%',
					style: 'border: 3px solid green; min-height: 300px;'
				}
			]
		}
	],

	alertChange: function () {

		var defaultContainer = this.down('[itemId=defaultContainer]');
		var stepFormPanel = this.down('[itemId=stepFormPanel]');
		if (this.getWpWindow().getSelectedStep() === null) {
			defaultContainer.show();
			stepFormPanel.hide();
		}
		else {
			defaultContainer.hide();
			stepFormPanel.show();

			stepFormPanel.getForm().setValues(this.getWpWindow().getSelectedStep());
		}
	},

	clearForm: function () {
		this.down('form').getForm().reset();
	}
});
