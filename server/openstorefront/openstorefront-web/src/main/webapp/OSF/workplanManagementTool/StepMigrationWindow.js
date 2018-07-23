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

Ext.define('OSF.workplanManagementTool.StepMigrationWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.osf.wp.migrationwindow',

	config: {
		wpWindow: null
	},
	onMigrate: function () {},

	height: 400,
	width: 500,
	modal: true,
	layout: {
		type: 'vbox',
		pack: 'center',
		align: 'middle'
	},

	initComponent: function () {
		this.callParent();

		this.setTitle('Migrate Records From <b>' + this.getWpWindow().getSelectedStep().name + '</b>');
		this.down('[itemId=currentStepLabel]').setHtml(this.getWpWindow().getSelectedStep().name);

		var migrationWindow = this;

		this.add({
			xtype: 'combo',
			queyMode: 'local',
			displayField: 'name',
			valueField: 'stepId',
			style: 'font-size: 2em; margin-top: 1.5em;',
			allowBlank: false,
			itemId: 'targetStepCombo',
			store: {
				field: ['name', 'stepId'],
				data: migrationWindow.getWpWindow().getWorkplanConfig().steps.filter(function (step) {
					return step.stepId !== migrationWindow.getWpWindow().getSelectedStep().stepId;
				})
			},
			listeners: {
				change: function (combo, newVal, oldVal) {
					migrationWindow.down('[itemId=migrateButton]').setDisabled(newVal ? false : true);
				}
			}
		});
	},

	items: [
		{
			xtype: 'container',
			itemId: 'currentStepLabel',
			html: '',
			style: 'font-size: 2em; margin-bottom: 1.5em;'
		},
		{
			xtype: 'container',
			html: '<i class="fa fa-long-arrow-down fa-3x" aria-hidden="true"></i>'
		}
	],

	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Migrate',
					itemId: 'migrateButton',
					disabled: true,
					iconCls: 'fa fa-2x fa-sign-out icon-button-color-save icon-vertical-correction',
					handler: function () {

						var migrationWindow = this.up('window');
						var workplanWindow = migrationWindow.getWpWindow();
						var targetStepId = migrationWindow.down('[itemId=targetStepCombo]').getValue();

						workplanWindow.getMigrationsToPerform().push({
							initialStepId: workplanWindow.getSelectedStep().stepId,
							targetStepId: targetStepId
						});

						Ext.Array.forEach(workplanWindow.getWorkplanConfig().steps, function (step, index) {
							if (step.stepId === targetStepId) {
								step.isNewStep = false;
							}
						});

						migrationWindow.onMigrate();
						migrationWindow.close();
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Cancel',
					iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
					handler: function () {
						this.up('window').close();
					}
				}
			]
		}
	]
});
