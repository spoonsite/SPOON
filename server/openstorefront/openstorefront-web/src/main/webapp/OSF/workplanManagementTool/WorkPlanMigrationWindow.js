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

Ext.define('OSF.workplanManagementTool.WorkPlanMigrationWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.osf.wp.workPlanMigrationWindow',

	config: {
		workplanGrid: null
	},
	height: 625,
	width: 500,
	scrollable: 'y',
	title: 'Migrate Workplan Records',
	modal: true,
	layout: {
		type: 'vbox',
		pack: 'center',
		align: 'middle'
	},
	listeners: {
		show: function () {
			var migrationWindow = this;
			var wpToBeDeleted = migrationWindow.getWorkplanGrid().getSelection()[0].getData();
			
			migrationWindow.down('[itemId=currentStepLabel]').setHtml(wpToBeDeleted.name + ' <i class="fa fa-question-circle" data-qtip="This is the name of the <b>work plan</b> being deleted" ></i>');
		}
	},
	items: [
		{
			xtype: 'container',
			height: '13%',
			minHeight: 80,
			width: '100%',
			layout: {
				type: 'hbox',
				pack: 'center',
				align: 'middle'
			},
			items: [
				{
					xtype: 'container',
					itemId: 'currentStepLabel',
					labelAlign: 'top',
					style: 'font-weight: bold;'
				},
				{
					xtype: 'container',
					margin: '0 10px',
					html: '<i class="fa fa-long-arrow-right fa-2x" aria-hidden="true"></i>'
				},
				{
					xtype: 'combo',
					itemId: 'targetPlanCombo',
					style: 'margin-top: 0.5em;',
					width: '50%',
					editable: false,
					labelAlign: 'top',
					fieldLabel: 'Target Work Plan',
					displayField: 'name',
					valueField: 'workPlanId',
					store: {
						autoLoad: true,
						fields: ['name', 'workPlanId'],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/workplans'
						},
						listeners: {
							load: function (store, records) {

								Ext.Array.forEach(store.getData().items, function (record, index) {

									var selectedRecord = Ext.getCmp('workplanGrid').getSelection()[0];
									if (record.getData().workPlanId === selectedRecord.getData().workPlanId) {
										store.removeAt(index, 1);
									}
								});
							}
						}
					},
					listeners: {
						change: function (combo, newVal, oldVal) {
							
							var migrationWindow = this.up('window');
							migrationWindow.buildMigrationFields(newVal);
							migrationWindow.down('[itemId=migrationFormContainer]').show();
						}
					}
				}
			]
		},
		{
			xtype: 'fieldset',
			height: '83%',
			width: '100%',
			margin: 10,
			scrollable: 'y',
			title: 'Step-to-Step Migration <i class="fa fa-question-circle" data-qtip="This will display a list of possible step migrations once a \'Target Work Plan\' has been selected" ></i>',
			itemId: 'migrationFormContainer',
			layout: {
				type: 'vbox',
				pack: 'center',
				align: 'middle'
			},
			items: [
				{
					xtype: 'container',
					html: 'Seclect a target workplan to start workplan migration'
				},
				{
					xtype: 'label',
					html: '<h2>OR</h2>'
				},
				{
					xtype: 'button',
					text: 'Delete Work Plan',
					iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
					handler: function () {
						this.up('window').migrateDeleteWorkPlan();
					}
				}
			]
		}
	],
	buildMigrationFields: function (targetWorkplanId) {

		var workplanGrid = Ext.getCmp('workplanGrid');
		var migrationWindow = this;
		var migrationFormContainer = migrationWindow.down('[itemId=migrationFormContainer]');
		var recordToBeDeleted = workplanGrid.getSelection()[0].getData();
		var targetRecord = workplanGrid.getStore().getData().items.filter(function (item) {
			return targetWorkplanId === item.getData().workPlanId;
		})[0];

		// remove all other fields in the migrationFormContainer
		migrationFormContainer.removeAll();

		var targetComboStore = Ext.create('Ext.data.Store', {
			data: targetRecord.getData().steps.map(function (step) {

				return Ext.apply(step, { displayView: '<b>' + step.stepOrder + '.</b> ' + step.name });
			})
		});

		Ext.Array.forEach(recordToBeDeleted.steps, function (step) {

			migrationFormContainer.add({
				xtype: 'container',
				width: '100%',
				layout: {
					type: 'hbox',
					pack: 'center',
					align: 'middle'
				},
				style: 'margin-bottom: 20px;',
				items:[
					{
						xtype: 'container',
						html: '<b>' + step.stepOrder + '.</b> ' + step.name,
						width: '30%',
						style: 'text-align: center;'
					},
					{
						xtype: 'container',
						margin: '0 10px',
						html: '<i class="fa fa-long-arrow-right fa-2x" aria-hidden="true"></i>',
						width: '10%'
					},
					{
						xtype: 'combo',
						itemId: 'targetStepCombo',
						initialStepId: step.workPlanStepId,
						fieldLabel: 'Target Step',
						labelAlign: 'top',
						width: '59%',
						displayField: 'displayView',
						valueField: 'workPlanStepId',
						store: targetComboStore,
						editable: false,
						listeners: {
							change: function (combo, newVal, oldVal) {
								this.up('window').validateMigrationFields();
							}
						},
						displayTpl: Ext.create('Ext.XTemplate',
							'<tpl for=".">',
								'{stepOrder}. {name}',
							'</tpl>'
						)
					}
				]
			});
		});
	},
	validateMigrationFields: function () {

		var isValid = true;
		var migrationWindow = this;
		var migrateButton = migrationWindow.down('[itemId=migrateButton]');
		var stepMigrationTargetFields = migrationWindow.query('[itemId=targetStepCombo]');

		Ext.Array.forEach(stepMigrationTargetFields, function (field) {
			if (field.getValue() === null) {
				isValid = false;
			}
		});

		if (!isValid) {
			migrateButton.disable();
		}
		else {
			migrateButton.enable();
		}
	},
	migrateDeleteWorkPlan: function () {
		
		var migrationWindow = this;
		var stepMigrationTargetFields = migrationWindow.query('[itemId=targetStepCombo]');
		var selectedRecord = migrationWindow.getWorkplanGrid().getSelection()[0].getData();
		var migrationData = {
			workPlanId: migrationWindow.down('[itemId=targetPlanCombo]').getValue(), // the target workplan
			stepMigrations: []
		};

		Ext.Array.forEach(stepMigrationTargetFields, function (field) {
			migrationData.stepMigrations.push({
				fromStepId: field.initialStepId,
				toStepId: field.getValue()
			});
		});

		Ext.Ajax.request({
			url: 'api/v1/resource/workplans/' + selectedRecord.workPlanId,
			method: 'DELETE',
			jsonData: migrationData,
			success: function (res) {

				migrationWindow.getWorkplanGrid().getStore().load();
				migrationWindow.close();
			}
		});
	},
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

						this.up('window').migrateDeleteWorkPlan();
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
